package cn.chengzhiya.langutil.manager.item;

import cn.chengzhiya.langutil.LangAPI;
import org.bukkit.inventory.ItemStack;

public final class ReflectionItemManagerImpl implements ItemManager {
    @Override
    public String getKey(ItemStack item) {
        StringBuilder key = new StringBuilder();

        String nmsVersion = LangAPI.instance.getServerManager().getNMSVersion();
        try {
            Class<?> craftItemStackClass = Class.forName("org.bukkit.craftbukkit." + nmsVersion + ".inventory.CraftItemStack");
            Object craftItemStack = LangAPI.instance.getReflectionManager().invokeMethod(
                    LangAPI.instance.getReflectionManager().getMethod(craftItemStackClass, "asNMSCopy", true, ItemStack.class),
                    craftItemStackClass,
                    item
            );

            if (craftItemStack == null) {
                return key.toString();
            }

            if (item.getType().isBlock()) {
                Object craftItem = LangAPI.instance.getReflectionManager().invokeMethod(
                        LangAPI.instance.getReflectionManager().getMethod(craftItemStack.getClass(), "getItem", true, craftItemStack.getClass()),
                        craftItemStackClass,
                        craftItemStack
                );

                if (craftItem == null) {
                    return key.toString();
                }

                Class<?> craftBlockClass = Class.forName("org.bukkit.craftbukkit." + nmsVersion + ".Block");
                Object craftBlock = LangAPI.instance.getReflectionManager().invokeMethod(
                        LangAPI.instance.getReflectionManager().getMethod(craftBlockClass, "asBlock", true, craftItem.getClass()),
                        craftBlockClass,
                        craftItem
                );

                if (craftBlock == null) {
                    return key.toString();
                }

                Object itemKey = LangAPI.instance.getReflectionManager().invokeMethod(
                        LangAPI.instance.getReflectionManager().getMethod(craftBlock.getClass(), "a", true),
                        craftBlock
                );
                key.append(itemKey).append(".name");
            } else {
                Object itemKey = LangAPI.instance.getReflectionManager().invokeMethod(
                        LangAPI.instance.getReflectionManager().getMethod(craftItemStack.getClass(), "a", true),
                        craftItemStack
                );
                key.append(itemKey);
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return key.toString();
    }
}
