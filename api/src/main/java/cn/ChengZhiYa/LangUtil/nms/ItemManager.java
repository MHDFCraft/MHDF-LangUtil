package cn.ChengZhiYa.LangUtil.nms;

import org.bukkit.inventory.ItemStack;

public interface ItemManager {
    /**
     * 获取物品在服务端版本对应的语言文件中所对应key
     *
     * @param item 物品实例
     * @return 语言文件中所对应key
     */
    String getKey(ItemStack item);
}
