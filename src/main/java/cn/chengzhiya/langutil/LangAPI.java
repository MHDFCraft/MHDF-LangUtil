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
import java.util.concurrent.ConcurrentHashMap;

@Getter
public final class LangAPI {
    public static LangAPI instance;
    private final HttpManager httpManager = new HttpManager();
    private final ReflectionManager reflectionManager = new ReflectionManager();
    private final ServerManager serverManager = new ServerManager();
    private final JavaPlugin plugin;
    private final File langFileFolder;

    private final ConcurrentHashMap<String, LangManager> langManagerHashMap = new ConcurrentHashMap<>();

    private ItemManager itemManager;
    private EntityManager entityManager;

    /**
     * @param plugin         插件主类
     * @param langFileFolder 语言文件的文件夹实例
     */
    public LangAPI(JavaPlugin plugin, File langFileFolder) {
        instance = this;
        this.plugin = plugin;
        this.langFileFolder = langFileFolder;

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

    /**
     * 获取指定语言的语言文件管理器
     *
     * @param lang 语言
     * @return 语言文件管理器
     */
    public LangManager getLangManager(String lang) {
        if (getLangManagerHashMap().get(lang) != null) {
            return getLangManagerHashMap().get(lang);
        }

        LangManager langManager = new LangManager(lang);
        getLangManagerHashMap().put(lang, langManager);

        return langManager;
    }

    /**
     * 获取中文的语言文件管理器
     *
     * @return 语言文件管理器
     */
    public LangManager getLangManager() {
        return getLangManager("zh_cn");
    }
}
