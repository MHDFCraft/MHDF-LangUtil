package cn.ChengZhiYa.LangUtil.manager.lang;

import cn.ChengZhiYa.LangUtil.LangAPI;
import cn.ChengZhiYa.LangUtil.exception.DownloadException;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class LangManager {
    @Getter
    private JSONObject data = new JSONObject();
    @Getter
    private boolean loaded = false;

    /**
     * 下载游戏语言文件
     */
    public void downloadLang() {
        if (LangAPI.instance.getLangFile().exists()) {
            return;
        }

        LangAPI.instance.getPlugin().getLogger().info("正在下载语言文件,请稍后!");

        try {
            // 读取MC版本列表
            byte[] versionManifestBytes = LangAPI.instance.getHttpManager().downloadFile(LangAPI.instance.getHttpManager().openConnection("https://bmclapi2.bangbang93.com/mc/game/version_manifest.json"));
            JSONObject versionManifest = JSON.parseObject(versionManifestBytes);

            String serverVersion = LangAPI.instance.getServerManager().getServerVersion();
            byte[] versionBytes = null;

            // 读取当前服务端版本的版本配置
            for (JSONObject versions : versionManifest.getList("versions", JSONObject.class)) {
                if (versions.getString("id").equals(serverVersion)) {
                    versionBytes = LangAPI.instance.getHttpManager().downloadFile(
                            LangAPI.instance.getHttpManager().openConnection(versions.getString("url"))
                    );
                }
            }
            JSONObject version = JSON.parseObject(versionBytes);

            // 读取服务端版本的资源文件配置
            byte[] assetsBytes = LangAPI.instance.getHttpManager().downloadFile(LangAPI.instance.getHttpManager().openConnection(
                    Objects.requireNonNull(version).getJSONObject("assetIndex").getString("url")
            ));
            JSONObject assets = JSON.parseObject(assetsBytes).getJSONObject("objects");

            // 获取中文语言文件的哈希值
            String langHash = assets.getJSONObject("minecraft/lang/zh_cn.json") != null ?
                    assets.getJSONObject("minecraft/lang/zh_cn.json").getString("hash") :
                    assets.getJSONObject("minecraft/lang/zh_CN.lang").getString("hash");

            // 下载中文语言文件
            LangAPI.instance.getHttpManager().downloadFile(
                    LangAPI.instance.getHttpManager().openConnection("https://bmclapi2.bangbang93.com/assets/" + langHash.substring(0, 2) + "/" + langHash),
                    LangAPI.instance.getLangFile().toPath()
            );

            LangAPI.instance.getPlugin().getLogger().info("语言文件下载完成!");
        } catch (DownloadException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 加载游戏语言文件
     */
    public void reloadLang() {
        try {
            byte[] langBytes = Files.readAllBytes(LangAPI.instance.getLangFile().toPath());

            try {
                this.data = JSON.parseObject(langBytes);
            } catch (Exception e) {
                // 老版本中并不使用json格式的语言文件,因此此处进行转换
                Map<String, String> oldLangData = new HashMap<>();

                try (BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(langBytes)))) {

                    String line;
                    while ((line = reader.readLine()) != null) {
                        String[] data = line.split("=");
                        oldLangData.put(data[0], data[1]);
                    }
                }

                this.data = JSONObject.from(oldLangData);
            }

            this.loaded = true;
        } catch (IOException e) {
            throw new RuntimeException("数据读取失败", e);
        }
    }

    /**
     * 获取物品名称
     *
     * @param item 物品实例
     * @return 物品名称
     */
    public String getItemName(ItemStack item) {
        if (item.getItemMeta().hasDisplayName()) {
            return item.getItemMeta().getDisplayName();
        }

        if (!isLoaded()) {
            reloadLang();
        }

        return this.data.getString(LangAPI.instance.getItemManager().getKey(item));
    }
}
