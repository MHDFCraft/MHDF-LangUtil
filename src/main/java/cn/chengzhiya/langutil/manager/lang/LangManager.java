package cn.chengzhiya.langutil.manager.lang;

import cn.chengzhiya.langutil.LangAPI;
import cn.chengzhiya.langutil.exception.DownloadException;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import lombok.Getter;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

import java.io.*;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public final class LangManager {
    @Getter
    private final String lang;
    @Getter
    private JSONObject data = new JSONObject();
    @Getter
    private boolean loaded = false;
    private int retryCount = 0;

    public LangManager(String lang) {
        this.lang = lang;
    }

    /**
     * 获取语言文件实例
     *
     * @return 语言文件实例
     */
    public File getLangFile() {
        if (LangAPI.instance.getLangFileFolder().exists()) {
            if (!LangAPI.instance.getLangFileFolder().isDirectory()) {
                LangAPI.instance.getLangFileFolder().delete();
            }
        }
        if (!LangAPI.instance.getLangFileFolder().exists()) {
            LangAPI.instance.getLangFileFolder().mkdirs();
        }
        return new File(LangAPI.instance.getLangFileFolder(), getLang());
    }

    /**
     * 下载游戏语言文件
     */
    public void downloadLang() {
        File langFile = getLangFile();
        if (langFile.exists()) {
            return;
        }

        String region = lang.split("_").length == 2 ? lang.split("_")[1] : "";
        String oldLang = lang.replace(region, region.toLowerCase(Locale.ROOT));

        LangAPI.instance.getPlugin().getLogger().info("正在下载语言文件,请稍后!");

        boolean downloadSuccess = false;

        int maxRetries = 5;
        while (retryCount < maxRetries && !downloadSuccess) {
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
                String langHash = assets.getJSONObject("minecraft/lang/" + lang + ".json") != null ?
                        assets.getJSONObject("minecraft/lang/" + lang + ".json").getString("hash") :
                        assets.getJSONObject("minecraft/lang/" + oldLang + ".lang").getString("hash");

                // 下载中文语言文件
                langFile.createNewFile();
                LangAPI.instance.getHttpManager().downloadFile(
                        LangAPI.instance.getHttpManager().openConnection("https://bmclapi2.bangbang93.com/assets/" + langHash.substring(0, 2) + "/" + langHash),
                        langFile.toPath()
                );

                LangAPI.instance.getPlugin().getLogger().info("语言文件下载完成!");
                downloadSuccess = true;
            } catch (DownloadException | IOException e) {
                retryCount++;
                if (retryCount < maxRetries) {
                    LangAPI.instance.getPlugin().getLogger().warning("下载语言文件失败，正在重试... 第 " + retryCount + " 次");
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException interruptedException) {
                        Thread.currentThread().interrupt();
                    }
                } else {
                    LangAPI.instance.getPlugin().getLogger().severe("下载语言文件失败,不再重试!");
                }
            }
        }
    }

    /**
     * 加载游戏语言文件
     */
    public void reloadLang() {
        try {
            byte[] langBytes = Files.readAllBytes(getLangFile().toPath());

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
        if (item.getItemMeta() != null && item.getItemMeta().hasDisplayName()) {
            return item.getItemMeta().getDisplayName();
        }

        if (!isLoaded()) {
            reloadLang();
        }

        return this.data.getString(LangAPI.instance.getItemManager().getKey(item));
    }

    /**
     * 获取实体名称
     *
     * @param entity 实体实例
     * @return 实体名称
     */
    public String getEntityName(Entity entity) {
        if (entity.getCustomName() != null) {
            return entity.getCustomName();
        }

        if (!isLoaded()) {
            reloadLang();
        }

        return this.data.getString(LangAPI.instance.getEntityManager().getKey(entity));
    }
}
