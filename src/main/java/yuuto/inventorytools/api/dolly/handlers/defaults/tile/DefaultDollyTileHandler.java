package yuuto.inventorytools.api.dolly.handlers.defaults.tile;

import yuuto.inventorytools.api.dolly.BlockData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class DefaultDollyTileHandler implements IDollyTileHandler{

	@Override
	public String getID() {
		return "tilehandler.inventorytools.defualt";
	}

	@Override
	public boolean canPickUp(TileEntity tile, BlockData data, EntityPlayer player,
			World world, int x, int y, int z, int side, float hitX, float hitY,
			float hitZ) {
		return true;
	}

	@Override
	public void onPickedUp(TileEntity tile, BlockData data,
			EntityPlayer player, World world, int x, int y, int z, int side,
			float hitX, float hitY, float hitZ) {
		NBTTagCompound tag=new NBTTagCompound();
		tile.writeToNBT(tag);
		if(!tag.hasNoTags())
			data.tileData=tag;
	}

	@Override
	public void onPlaced(TileEntity tile, BlockData data, EntityPlayer player,
			World world, int x, int y, int z, int side, float hitX, float hitY,
			float hitZ) {
		if(data.tileData == null || data.tileData.hasNoTags())
			return;
		data.tileData.setInteger("x", x);
		data.tileData.setInteger("y", y);
		data.tileData.setInteger("z", z);
		tile.readFromNBT(data.tileData);		
	}

	@Override
	public void onTransferInventory(IInventory target, BlockData data,
			EntityPlayer player, World world, int x, int y, int z, int side,
			float hitX, float hitY, float hitZ) {}

	@Override
	public boolean canTransferInventory(IInventory target, BlockData data,
			EntityPlayer player, World world, int x, int y, int z, int side,
			float hitX, float hitY, float hitZ) {
		return false;
	}

	@Override
	public boolean canTransferInventory(BlockData data, EntityPlayer player) {
		return false;
	}

}
