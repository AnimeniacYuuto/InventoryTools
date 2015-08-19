package yuuto.inventorytools.api.dolly.handlers.defaults.tile;

import yuuto.inventorytools.api.dolly.BlockData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public interface IDollyTileHandler {
	
	public String getID();
	public boolean canPickUp(TileEntity tile, BlockData data, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ);
	public void onPickedUp(TileEntity tile, BlockData data, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ);
	public void onPlaced(TileEntity tile, BlockData data, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ);
	public void onTransferInventory(IInventory target, BlockData data, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ);
	public boolean canTransferInventory(IInventory target, BlockData data, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ);
	public boolean canTransferInventory(BlockData data, EntityPlayer player);

}
