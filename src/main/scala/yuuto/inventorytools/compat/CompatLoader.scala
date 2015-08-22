/**
 * @author Yuuto
 */
package yuuto.inventorytools.compat

import cpw.mods.fml.common.Loader
import yuuto.inventorytools.api.dolly.DollyHandlerRegistry
import yuuto.inventorytools.api.dolly.handlers.ChestDollyTileHandler
import yuuto.inventorytools.compat.handlers.JabbaBarrelBlockHandler
import yuuto.inventorytools.compat.handlers.JabbaBarrelTileHandler
import yuuto.inventorytools.compat.handlers.TinyStorageDollyTileHandler
import yuuto.inventorytools.compat.handlers.StorageDrawersDollyTileHandler
import yuuto.inventorytools.compat.handlers.CompChestDollyBlockHandler
import yuuto.inventorytools.compat.handlers.CompChestDollyTileHandler

object CompatLoader {
  def loadCompatHandlers(){
    if(Loader.isModLoaded("IronChest"))
      DollyHandlerRegistry.registerTileHandler("cpw.mods.ironchest.TileEntityIronChest", ChestDollyTileHandler.getInstance());
    if(Loader.isModLoaded("RefinedRelocation"))
      DollyHandlerRegistry.registerTileHandler("com.dynious.refinedrelocation.tileentity.TileSortingChest", ChestDollyTileHandler.getInstance());
    if(Loader.isModLoaded("JABBA")){
      DollyHandlerRegistry.registerBlockHandler("JABBA:barrel", JabbaBarrelBlockHandler.getInstance());
      DollyHandlerRegistry.registerTileHandler("mcp.mobius.betterbarrels.common.blocks.TileEntityBarrel", JabbaBarrelTileHandler.getInstance());
    }
    if(Loader.isModLoaded("TinyStorage")){
      DollyHandlerRegistry.registerTileHandler("com.timthebrick.tinystorage.common.tileentity.TileEntityTinyStorage", TinyStorageDollyTileHandler.getInstance())
    }
    if(Loader.isModLoaded("compactstorage")){
      DollyHandlerRegistry.registerBlockHandler("compactstorage:compactChest", CompChestDollyBlockHandler.getInstance());
      DollyHandlerRegistry.registerBlockHandler("compactstorage:sortingCompactChest", CompChestDollyBlockHandler.getInstance());
      DollyHandlerRegistry.registerTileHandler("com.tattyseal.compactstorage.tileentity.TileEntityChest", CompChestDollyTileHandler.getInstanceCompChest());
    }
    if(Loader.isModLoaded("StorageDrawers")){
      DollyHandlerRegistry.registerTileHandler("com.jaquadro.minecraft.storagedrawers.block.tile.TileEntityDrawers", StorageDrawersDollyTileHandler.getInstance());
    }
  }
}