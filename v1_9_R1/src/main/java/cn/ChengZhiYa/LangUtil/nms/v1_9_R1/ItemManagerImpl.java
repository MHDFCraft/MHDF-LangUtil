package cn.ChengZhiYa.LangUtil.nms.v1_9_R1;

import cn.ChengZhiYa.LangUtil.nms.ItemManager;
import net.minecraft.server.v1_9_R1.Block;
import org.bukkit.craftbukkit.v1_9_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

public final class ItemManagerImpl implements ItemManager {
    @Override
    public String getKey(ItemStack stack) {
        net.minecraft.server.v1_9_R1.ItemStack item = CraftItemStack.asNMSCopy(stack);
        if (stack.getType().isBlock()) {
            Block block = Block.asBlock(item.getItem());
            return block.a() + ".name";
        }
        return item.a();
    }
}
