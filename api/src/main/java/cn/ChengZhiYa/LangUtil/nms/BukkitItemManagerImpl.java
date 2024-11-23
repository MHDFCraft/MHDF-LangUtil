package cn.ChengZhiYa.LangUtil.nms;

import org.bukkit.inventory.ItemStack;

public final class BukkitItemManagerImpl implements ItemManager {
    @Override
    public String getKey(ItemStack item) {
        StringBuilder key = new StringBuilder();

        if (item.getType().isBlock()) {
            key.append("block");
        } else {
            key.append("item");
        }

        key.append(".minecraft.").append(item.getType().getKey().getKey());

        return key.toString();
    }
}
