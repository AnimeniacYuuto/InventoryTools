package yuuto.inventorytools.api.dolly.handlers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.world.World;
import yuuto.inventorytools.api.dolly.BlockData;
import yuuto.inventorytools.api.dolly.handlers.defaults.tile.InventoryDollyTileHandler;

public class ChestDollyTileHandler extends InventoryDollyTileHandler{

	protected static ChestDollyTileHandler instance=null;
	public static final ChestDollyTileHandler getInstance(){
		if(instance==null)
			instance=new ChestDollyTileHandler();
		return instance;
	}
	
	protected ChestDollyTileHandler(){}
	
	@Override
	public void onPickedUp(TileEntity tile, BlockData data,
			EntityPlayer player, World world, int x, int y, int z, int side,
			float hitX, float hitY, float hitZ){
		super.onPickedUp(tile, data, player, world, x, y, z, side, hitX, hitY, hitZ);
		//TileEntityChest chest= (TileEntityChest)tile;
		//chest.
	}
	
	@Override
	public String getID() {
		return "tilehandler.minecraft.chest";
	}
	
	@Override
	public NBTTagList getInventoryTags(BlockData data) {
		if(data.tileData == null || data.tileData.hasNoTags())
			return null;
		if(!data.tileData.hasKey("Items"))
			return null;
		return data.tileData.getTagList("Items", 10);
	}

	@Override
	public void setInventory(NBTTagList tagList, BlockData data) {
		if(data.tileData == null || data.tileData.hasNoTags())
			return;
		if(tagList == null || tagList.tagCount() < 1){
			if(data.tileData.hasKey("Items"))
				data.tileData.removeTag("Items");
			return;
		}
		data.tileData.setTag("Items", tagList);
	}

}
