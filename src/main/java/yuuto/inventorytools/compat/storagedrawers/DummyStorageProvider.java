package yuuto.inventorytools.compat.storagedrawers;

import java.util.EnumSet;

import com.jaquadro.minecraft.storagedrawers.StorageDrawers;
import com.jaquadro.minecraft.storagedrawers.api.storage.attribute.LockAttribute;
import com.jaquadro.minecraft.storagedrawers.config.ConfigManager;
import com.jaquadro.minecraft.storagedrawers.storage.DefaultStorageProvider;

public class DummyStorageProvider extends DefaultStorageProvider{

	int capacity = 1;
	EnumSet<LockAttribute> lockAttributes = null;
	
	public DummyStorageProvider(int capacity, EnumSet<LockAttribute> lockAttributes) {
		super(null, null);
		this.capacity=capacity;
		this.lockAttributes = lockAttributes;
	}
	
	@Override
    public void markAmountDirty (int slot) {}
	@Override
    public void markDirty (int slot) {}
	
	@Override
    public int getSlotStackCapacity (int slot) {		
		return Utils.getMaxMultiplier() * capacity;
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

}
