package cn.chengzhiya.langutil;

import cn.chengzhiya.langutil.manager.entity.BukkitEntityManagerImpl;
import cn.chengzhiya.langutil.manager.entity.EntityManager;
import cn.chengzhiya.langutil.manager.entity.ReflectionEntityManagerImpl;
import cn.chengzhiya.langutil.manager.item.BukkitItemManagerImpl;
import cn.chengzhiya.langutil.manager.item.ItemManager;
import cn.chengzhiya.langutil.manager.item.ReflectionItemManagerImpl;
import cn.chengzhiya.langutil.manager.lang.LangManager;
import cn.chengzhiya.langutil.manager.network.HttpManager;
import cn.chengzhiya.langutil.manager.reflection.ReflectionManager;
import cn.chengzhiya.langutil.manager.server.ServerManager;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

@Getter
public final class LangAPI {
    public static LangAPI instance;
    private final LangManager langManager = new LangManager();
    private final HttpManager httpManager = new HttpManager();
    private final ReflectionManager reflectionManager = new ReflectionManager();
    private final ServerManager serverManager = new ServerManager();
    private final JavaPlugin plugin;
    private final File langFile;

    private ItemManager itemManager;
    private EntityManager entityManager;

    /**
     * @param plugin   插件主类
     * @param langFile 语言文件的文件实例(最好不要包含后缀)
     */
    public LangAPI(JavaPlugin plugin, File langFile) {
        instance = this;
        this.plugin = plugin;
        this.langFile = langFile;

        try {
            Material.class.getDeclaredMethod("getKey");
            this.itemManager = new BukkitItemManagerImpl();
        } catch (NoSuchMethodException e) {
            this.itemManager = new ReflectionItemManagerImpl();
        }

        try {
            EntityType.class.getDeclaredMethod("getKey");
            this.entityManager = new BukkitEntityManagerImpl();
        } catch (NoSuchMethodException e) {
            this.entityManager = new ReflectionEntityManagerImpl();
        }
    }
}
