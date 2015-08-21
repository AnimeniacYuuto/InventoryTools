package yuuto.inventorytools.compat.storagedrawers;

import com.jaquadro.minecraft.storagedrawers.storage.CompDrawerData;
import com.jaquadro.minecraft.storagedrawers.storage.ICentralInventory;

public class CompDrawerDataWrapper extends CompDrawerData{

	public DummyCentralInventory centralInventoryDummy;
	
	public CompDrawerDataWrapper(DummyCentralInventory centralInventoryDummy, int slot) {
		super(centralInventoryDummy, slot);
		this.centralInventoryDummy = centralInventoryDummy;
	}

}
