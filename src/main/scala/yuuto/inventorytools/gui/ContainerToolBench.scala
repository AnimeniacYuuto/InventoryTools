/**
 * @author Yuuto
 */
package yuuto.inventorytools.gui

import net.minecraft.entity.player.EntityPlayer
import yuuto.inventorytools.tiles.TileToolBench
import yuuto.yuutolib.block.ContainerAlt
import yuuto.inventorytools.gui.slot.SlotTool
import yuuto.inventorytools.gui.slot.SlotToolBox

class ContainerToolBench(tile:TileToolBench, player:EntityPlayer) extends ContainerAlt(tile.inv, player.inventory){
  init();
  
  override def bindInventorySlots():Array[Int]={
    return Array(8,97);
  }
  
  override def bindOtherSlots(){
    addSlotToContainer(new SlotToolBox(tile, 80, 21));
    for(y<-0 until 3){
      for(x<-0 until 9){
        val i:Int=x+y*9;
        if(i >= inventory.getSizeInventory())
          return;
        addSlotToContainer(new SlotTool(tile, i, 8+x*18, 48+y*18))
      }
    }
  }
  
  override def canInteractWith(player:EntityPlayer):Boolean={
    return player.getDistanceSq(this.tile.xCoord + 0.5D, this.tile.yCoord + 0.5D, this.tile.zCoord + 0.5D) <= 64.0D;
  }
  
}