package cn.chengzhiya.langutil.manager.entity;

import cn.chengzhiya.langutil.LangAPI;
import org.bukkit.entity.Entity;

public final class ReflectionEntityManagerImpl implements EntityManager {
    @Override
    public String getKey(Entity entity) {
        StringBuilder key = new StringBuilder();

        String nmsVersion = LangAPI.instance.getServerManager().getNMSVersion();
        try {
            Class<?> craftEntityClass = Class.forName("org.bukkit.craftbukkit." + nmsVersion + ".entity.Entity");
            Object nmsEntity = LangAPI.instance.getReflectionManager().getFieldValue(
                    LangAPI.instance.getReflectionManager().getField(
                            craftEntityClass,
                            "entity",
                            true
                    ),
                    entity
            );

            Class<?> nmsEntityTypes = Class.forName("net.minecraft.server." + nmsVersion + ".EntityTypes");

            Object entityKey = LangAPI.instance.getReflectionManager().invokeMethod(
                    LangAPI.instance.getReflectionManager().getMethod(nmsEntityTypes, "b", true),
                    nmsEntity
            );
            key.append(entityKey);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return key.toString();
    }
}
