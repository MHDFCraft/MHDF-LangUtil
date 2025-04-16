package cn.chengzhiya.langutil.manager.entity;

import org.bukkit.entity.Entity;

public interface EntityManager {
    /**
     * 获取实体在服务端版本对应的语言文件中所对应key
     *
     * @param entity 实体实例
     * @return 语言文件中所对应key
     */
    String getKey(Entity entity);
}
