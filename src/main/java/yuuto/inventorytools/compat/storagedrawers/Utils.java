package yuuto.inventorytools.compat.storagedrawers;

import com.jaquadro.minecraft.storagedrawers.StorageDrawers;
import com.jaquadro.minecraft.storagedrawers.config.ConfigManager;

public class Utils {
	
	public static int getMaxMultiplier(){
		ConfigManager config = StorageDrawers.config;
		int multiplier = 0;
		for(int i = 2; i <= 6; i++){
			multiplier+=config.getStorageUpgradeMultiplier(6);
		}
		if(multiplier == 0)
			return config.getStorageUpgradeMultiplier(1);
		return multiplier;
	}

}
