package yuuto.inventorytools.api.dolly;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class InventoryMerger {
	
	public static void mergeStackIntoInventory(ItemStack toMerge, IInventory target){
		int maxSize=Math.min(toMerge.getMaxStackSize(), target.getInventoryStackLimit());
		for(int i = 0; i < target.getSizeInventory() && toMerge.stackSize > 0; i++){
			if(target.getStackInSlot(i) == null){
				target.setInventorySlotContents(i, toMerge.copy());
				if(target.getStackInSlot(i) == null)
					continue;
				toMerge.stackSize-=target.getStackInSlot(i).stackSize;
				continue;
			}
			ItemStack t=target.getStackInSlot(i);
			if(!t.isItemEqual(toMerge))
				continue;
			if(!ItemStack.areItemStackTagsEqual(t, toMerge))
				continue;
			int transf=Math.min(maxSize-t.stackSize, toMerge.stackSize);
			if(maxSize < 1)
				continue;
			int origSize=t.stackSize;
			t.stackSize+=transf;
			target.setInventorySlotContents(i, t);
			toMerge.stackSize-= target.getStackInSlot(i).stackSize-origSize;
		}
	}

}
