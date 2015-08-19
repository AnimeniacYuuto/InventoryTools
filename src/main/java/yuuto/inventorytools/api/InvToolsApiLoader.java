package yuuto.inventorytools.api;

import net.minecraft.tileentity.TileEntityChest;
import yuuto.inventorytools.api.dolly.DollyHandlerRegistry;
import yuuto.inventorytools.api.dolly.handlers.ChestDollyTileHandler;

public class InvToolsApiLoader {
	
	public static void initialize(){
		DollyHandlerRegistry.registerTileHandler(TileEntityChest.class, ChestDollyTileHandler.getInstance());
	}

}
