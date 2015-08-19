package yuuto.inventorytools.api.dolly.handlers.defaults.tile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import yuuto.inventorytools.api.dolly.BlockData;
import yuuto.inventorytools.api.dolly.InventoryMerger;

public abstract class InventoryDollyTileHandler extends DefaultDollyTileHandler{

	@Override
	public void onTransferInventory(IInventory target, BlockData data,
			EntityPlayer player, World world, int x, int y, int z, int side,
			float hitX, float hitY, float hitZ) {
		NBTTagList tagList=getInventoryTags(data);
		if(tagList == null || tagList.tagCount() < 1)
			return;
		NBTTagList tagList2=new NBTTagList();
		for(int i = 0; i < tagList.tagCount(); i++){
			NBTTagCompound nbt=tagList.getCompoundTagAt(i);
			ItemStack stack=ItemStack.loadItemStackFromNBT(nbt);
			if(stack == null)
				continue;
			InventoryMerger.mergeStackIntoInventory(stack, target);
			if(stack.stackSize > 0){
				nbt = stack.writeToNBT(nbt);
				tagList2.appendTag(nbt);
			}
		}
		setInventory(tagList2, data);
	}

	@Override
	public boolean canTransferInventory(IInventory target, BlockData data,
			EntityPlayer player, World world, int x, int y, int z, int side,
			float hitX, float hitY, float hitZ) {
		NBTTagList tagList=getInventoryTags(data);
		return tagList != null && tagList.tagCount() > 0;
	}

	@Override
	public boolean canTransferInventory(BlockData data, EntityPlayer player) {
		NBTTagList tagList=getInventoryTags(data);
		return tagList != null && tagList.tagCount() > 0;
	}
	public abstract NBTTagList getInventoryTags(BlockData data);
	public abstract void setInventory(NBTTagList tagList, BlockData data);
	
}
