package yuuto.inventorytools.compat.handlers;

import net.minecraft.nbt.NBTTagList;
import yuuto.inventorytools.api.dolly.BlockData;
import yuuto.inventorytools.api.dolly.handlers.defaults.tile.InventoryDollyTileHandler;

public class TinyStorageDollyTileHandler extends InventoryDollyTileHandler{

	protected static TinyStorageDollyTileHandler instance=null;
	public static final TinyStorageDollyTileHandler getInstance(){
		if(instance==null)
			instance=new TinyStorageDollyTileHandler();
		return instance;
	}
	
	private TinyStorageDollyTileHandler(){}
	
	@Override
	public NBTTagList getInventoryTags(BlockData data) {
		if(data.tileData == null || data.tileData.hasNoTags())
			return null;
		if(!data.tileData.hasKey("Inventory"))
			return null;
		return data.tileData.getTagList("Inventory", 10);
	}

	@Override
	public void setInventory(NBTTagList tagList, BlockData data) {
		if(data.tileData == null || data.tileData.hasNoTags())
			return;
		if(tagList == null || tagList.tagCount() < 1){
			if(data.tileData.hasKey("Inventory"))
				data.tileData.removeTag("Inventory");
			return;
		}
		data.tileData.setTag("Inventory", tagList);
	}

}
