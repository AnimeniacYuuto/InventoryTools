package yuuto.inventorytools.api.dolly;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import yuuto.inventorytools.until.LogHelperIT;

public class InventoryMerger {
	
	public static void mergeStackIntoInventory(ItemStack toMerge, IInventory target, int side){
		LogHelperIT.Debug("Inserting into invy "+target.getInventoryStackLimit()+" , "+target.getSizeInventory());
		int maxSize=Math.min(toMerge.getMaxStackSize(), target.getInventoryStackLimit());
		for(int i = 0; i < target.getSizeInventory() && toMerge.stackSize > 0; i++){
			if(!target.isItemValidForSlot(i, toMerge))
				continue;
			if(target instanceof ISidedInventory && !((ISidedInventory)target).canInsertItem(i, toMerge, side))
				continue;
			if(target.getStackInSlot(i) == null){
				LogHelperIT.Debug("Inserting into empty "+i);
				target.setInventorySlotContents(i, toMerge.copy());
				toMerge.stackSize-=maxSize;
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
