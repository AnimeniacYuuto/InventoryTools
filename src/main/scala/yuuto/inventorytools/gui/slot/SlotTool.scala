/**
 * @author Yuuto
 */
package yuuto.inventorytools.gui.slot

import net.minecraft.inventory.Slot
import yuuto.inventorytools.tiles.TileToolBench
import net.minecraft.item.ItemStack
import net.minecraft.entity.player.EntityPlayer

class SlotTool(tile:TileToolBench, slotIndex:Int, x:Int, y:Int) extends Slot(tile.inv, slotIndex, x, y){
  
  
  override def isItemValid(stack:ItemStack):Boolean={
      return this.inventory.isItemValidForSlot(this.slotIndex, stack);
  }
  
  override def canTakeStack(player:EntityPlayer):Boolean={
      return tile.isToolBoxOpen();
  }
}