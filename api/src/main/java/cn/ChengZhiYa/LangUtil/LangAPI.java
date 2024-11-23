package cn.ChengZhiYa.LangUtil;

import cn.ChengZhiYa.LangUtil.manager.lang.LangManager;
import cn.ChengZhiYa.LangUtil.manager.network.HttpManager;
import cn.ChengZhiYa.LangUtil.manager.server.ServerManager;
import cn.ChengZhiYa.LangUtil.nms.BukkitItemManagerImpl;
import cn.ChengZhiYa.LangUtil.nms.ItemManager;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

@Getter
public final class LangAPI {
    public static LangAPI instance;
    private final LangManager langManager = new LangManager();
    private final HttpManager httpManager = new HttpManager();
    private final ServerManager serverManager = new ServerManager();
    private final JavaPlugin plugin;
    private final File langFile;
    private ItemManager itemManager;

    /**
     * @param plugin   插件主类
     * @param langFile 语言文件的文件实例(最好不要包含后缀)
     */
    public LangAPI(JavaPlugin plugin, File langFile) {
        instance = this;
        this.plugin = plugin;
        this.langFile = langFile;

        try {
            // 检测是否可以使用getKey方法获取物品对应语言文件中的key
            Material.class.getDeclaredMethod("getKey");
            // 使用Bukkit获取物品对应语言文件中的key
            this.itemManager = new BukkitItemManagerImpl();
        } catch (NoSuchMethodException e) {
            // 使用NMS获取物品对应语言文件中的key
            try {
                Class<?> itemManager = Class.forName("cn.ChengZhiYa.LangUtil.nms." + getServerManager().getNMSVersion() + ".ItemManagerImpl");
                this.itemManager = (ItemManager) itemManager.newInstance();
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
                throw new RuntimeException("当前版本并不受支持:" + ex);
            }
        }
    }
}
