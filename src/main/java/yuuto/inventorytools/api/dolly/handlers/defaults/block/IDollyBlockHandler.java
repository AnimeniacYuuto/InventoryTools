package yuuto.inventorytools.api.dolly.handlers.defaults.block;

import yuuto.inventorytools.api.dolly.BlockData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface IDollyBlockHandler {
	public String getID();
	public boolean onPickedUp(BlockData blockData, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ);
	public boolean onPlaced(BlockData blockData, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ);
}
