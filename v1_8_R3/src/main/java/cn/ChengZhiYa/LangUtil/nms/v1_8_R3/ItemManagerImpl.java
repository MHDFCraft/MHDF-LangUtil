package cn.ChengZhiYa.LangUtil.nms.v1_8_R3;

import cn.ChengZhiYa.LangUtil.nms.ItemManager;
import net.minecraft.server.v1_8_R3.Block;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

public final class ItemManagerImpl implements ItemManager {
    @Override
    public String getKey(ItemStack stack) {
        net.minecraft.server.v1_8_R3.ItemStack item = CraftItemStack.asNMSCopy(stack);
        if (stack.getType().isBlock()) {
            Block block = Block.asBlock(item.getItem());
            return block.a() + ".name";
        }
        return item.a();
    }
}
