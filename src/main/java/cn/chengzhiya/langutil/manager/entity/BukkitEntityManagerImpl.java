package cn.chengzhiya.langutil.manager.entity;

import org.bukkit.entity.Entity;

public final class BukkitEntityManagerImpl implements EntityManager {
    @Override
    public String getKey(Entity entity) {
        return "entity" + ".minecraft." + entity.getType().getKey().getKey();
    }
}
