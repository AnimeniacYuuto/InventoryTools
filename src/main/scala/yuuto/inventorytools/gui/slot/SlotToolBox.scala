/**
 * @author Yuuto
 */
package yuuto.inventorytools.gui.slot

import net.minecraft.inventory.Slot
import yuuto.inventorytools.tiles.TileToolBench
import net.minecraft.item.ItemStack
import net.minecraft.entity.player.EntityPlayer

class SlotToolBox(tile:TileToolBench, x:Int, y:Int) extends Slot(tile.toolBox, 0, x, y){
  
  override def isItemValid(stack:ItemStack):Boolean={
      return this.inventory.isItemValidForSlot(0, stack);
  }
  
  override def canTakeStack(player:EntityPlayer):Boolean={
      return !tile.isToolBoxOpen();
  }
}