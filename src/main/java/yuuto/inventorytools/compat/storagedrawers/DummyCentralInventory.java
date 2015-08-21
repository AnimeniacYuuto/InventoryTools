package yuuto.inventorytools.compat.storagedrawers;

import java.util.EnumSet;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import com.jaquadro.minecraft.storagedrawers.StorageDrawers;
import com.jaquadro.minecraft.storagedrawers.api.storage.IDrawer;
import com.jaquadro.minecraft.storagedrawers.api.storage.attribute.LockAttribute;
import com.jaquadro.minecraft.storagedrawers.block.tile.TileEntityDrawersComp;
import com.jaquadro.minecraft.storagedrawers.config.ConfigManager;
import com.jaquadro.minecraft.storagedrawers.network.CountUpdateMessage;
import com.jaquadro.minecraft.storagedrawers.storage.BaseDrawerData;
import com.jaquadro.minecraft.storagedrawers.storage.CompDrawerData;
import com.jaquadro.minecraft.storagedrawers.storage.ICentralInventory;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.IMessage;

public class DummyCentralInventory implements ICentralInventory
{
    ItemStack[] protoStack=null;
    public int pooledCount;
    int[] convRate;
    IDrawer[] drawers;
    EnumSet<LockAttribute> lockAttributes = null;
    int capacity;
    
    public DummyCentralInventory(ItemStack[] protoStack, int pooledCount, int[] convRate, 
    		EnumSet<LockAttribute> lockAttributes, int capacity){
    	this.protoStack=protoStack;
    	this.pooledCount = pooledCount;
    	this.convRate = convRate;
    	this.lockAttributes = lockAttributes;
    	this.capacity = capacity;
    	drawers = new IDrawer[3];
    }
	
	@Override
    public ItemStack getStoredItemPrototype (int slot) {
        return protoStack[slot];
    }
	int getDrawerCapacity(){
		return capacity;
	}
	int getDrawerCount(){
		return 3;
	}
	IDrawer getDrawer(int slot){
		if (slot < 0 || slot >= drawers.length)
            return null;

        return drawers[slot];
	}
	public IDrawer[] getDrawers(){
		return drawers;
	}
	public int[] getConvRates(){
		return convRate;
	}

    @Override
    public void setStoredItem (int slot, ItemStack itemPrototype, int amount) {
        if (itemPrototype != null && convRate != null && convRate[0] == 0) {
            //populateSlots(itemPrototype);
            for (int i = 0; i < getDrawerCount(); i++) {
                if (BaseDrawerData.areItemsEqual(protoStack[i], itemPrototype))
                    pooledCount = (pooledCount % convRate[i]) + convRate[i] * amount;
            }

            for (int i = 0; i < getDrawerCount(); i++) {
                if (i == slot)
                    continue;

                IDrawer drawer = getDrawer(i);
                if (drawer instanceof CompDrawerData)
                    ((CompDrawerData) drawer).refresh();
            }
        }
        else if (itemPrototype == null) {
            setStoredItemCount(slot, 0);
        }
    }

    @Override
    public int getStoredItemCount (int slot) {
        if (convRate == null || convRate[slot] == 0)
            return 0;

        return pooledCount / convRate[slot];
    }

    @Override
    public void setStoredItemCount (int slot, int amount) {
        if (convRate == null || convRate[slot] == 0)
            return;

        int oldCount = pooledCount;
        pooledCount = (pooledCount % convRate[slot]) + convRate[slot] * amount;

        int poolMax = getMaxCapacity(0) * convRate[0];
        if (pooledCount > poolMax)
            pooledCount = poolMax;

        if (pooledCount != oldCount) {
            if (pooledCount != 0 || isLocked(LockAttribute.LOCK_POPULATED))
                markAmountDirty();
            else {
                clear();
            }
        }
    }

    @Override
    public int getMaxCapacity (int slot) {
        if (protoStack[slot] == null || convRate == null || convRate[slot] == 0)
            return 0;

        return protoStack[slot].getItem().getItemStackLimit(protoStack[slot]) * getStackCapacity(slot);
    }

    @Override
    public int getMaxCapacity (int slot, ItemStack itemPrototype) {
        if (itemPrototype == null || itemPrototype.getItem() == null)
            return 0;

        if (convRate == null || protoStack[0] == null || convRate[0] == 0)
            return itemPrototype.getItem().getItemStackLimit(itemPrototype) * getBaseStackCapacity();

        if (BaseDrawerData.areItemsEqual(protoStack[slot], itemPrototype))
            return getMaxCapacity(slot);

        return 0;
    }

    @Override
    public int getRemainingCapacity (int slot) {
        return getMaxCapacity(slot) - getStoredItemCount(slot);
    }

    @Override
    public int getStoredItemStackSize (int slot) {
        if (protoStack[slot] == null || convRate == null || convRate[slot] == 0)
            return 0;

        return protoStack[slot].getItem().getItemStackLimit(protoStack[slot]);
    }

    @Override
    public int getItemCapacityForInventoryStack (int slot) {
    	return getMaxCapacity(slot);
    }

    @Override
    public int getConversionRate (int slot) {
        if (protoStack[slot] == null || convRate == null || convRate[slot] == 0)
            return 0;

        return convRate[0] / convRate[slot];
    }

    @Override
    public int getStoredItemRemainder (int slot) {
    	int count = getStoredItemCount(slot);
        if (slot > 0 && convRate[slot] > 0)
            count -= getStoredItemCount(slot - 1) * (convRate[slot - 1] / convRate[slot]);

        return count;
    }

    @Override
    public boolean isSmallestUnit (int slot) {
        if (protoStack[slot] == null || convRate == null || convRate[slot] == 0)
            return false;

        return convRate[slot] == 1;
    }

    @Override
    public boolean isVoidSlot (int slot) {
        return false;
    }

    @Override
    public boolean isShroudedSlot (int slot) {
        return false;
    }

    @Override
    public boolean setIsSlotShrouded (int slot, boolean state) {
        return false;
    }

    @Override
    public boolean isLocked (int slot, LockAttribute attr) {
        return isLocked(attr);
    }
    
    public boolean isLocked(LockAttribute attr){
		if(!StorageDrawers.config.cache.enableLockUpgrades || lockAttributes == null)
			return false;
		return lockAttributes.contains(attr);
	}

    @Override
    public void writeToNBT (int slot, NBTTagCompound tag) {
        ItemStack protoStack = getStoredItemPrototype(slot);
        if (protoStack != null && protoStack.getItem() != null) {
            tag.setShort("Item", (short) Item.getIdFromItem(protoStack.getItem()));
            tag.setShort("Meta", (short) protoStack.getItemDamage());
            tag.setInteger("Count", 0); // TODO: Remove when ready to break 1.1.7 compat

            if (protoStack.getTagCompound() != null)
                tag.setTag("Tags", protoStack.getTagCompound());
        }
    }

    @Override
    public void readFromNBT (int slot, NBTTagCompound tag) {
        if (tag.hasKey("Item")) {
            Item item = Item.getItemById(tag.getShort("Item"));
            if (item != null) {
                ItemStack stack = new ItemStack(item);
                stack.setItemDamage(tag.getShort("Meta"));
                if (tag.hasKey("Tags"))
                    stack.setTagCompound(tag.getCompoundTag("Tags"));

                protoStack[slot] = stack;
            }
        }
    }

    private void clear () {
        for (int i = 0; i < getDrawerCount(); i++) {
            protoStack[i] = null;
            convRate[i] = 0;
        }

        refresh();
    }

    public void refresh () {
        for (int i = 0; i < getDrawerCount(); i++) {
            IDrawer drawer = getDrawer(i);
            if (drawer instanceof CompDrawerData)
                ((CompDrawerData) drawer).refresh();
        }
    }

    private int getStackCapacity (int slot) {
        if (convRate == null || convRate[slot] == 0)
            return 0;

        int slotStacks = getBaseStackCapacity();

        int stackLimit = convRate[0] * slotStacks;
        return stackLimit / convRate[slot];
    }

    private int getBaseStackCapacity () {
        ConfigManager config = StorageDrawers.config;
        return Utils.getMaxMultiplier() * getDrawerCapacity();
    }

    public void markAmountDirty () {}

    public void markDirty (int slot) {}
}