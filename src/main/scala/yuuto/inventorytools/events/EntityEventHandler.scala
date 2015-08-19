/**
 * @author Yuuto
 */
package yuuto.inventorytools.events

import java.util.List
import yuuto.inventorytools.util.NBTTags
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import net.minecraft.item.ItemStack
import yuuto.inventorytools.util.NBTHelper
import yuuto.inventorytools.proxy.ProxyCommon
import net.minecraft.entity.item.EntityItem
import net.minecraftforge.event.entity.player.ItemTooltipEvent
import org.lwjgl.input.Keyboard
import net.minecraft.client.resources.I18n

object EntityEventHandler {
  @SubscribeEvent
  def onItemDestroyed(event:PlayerDestroyItemEvent)
  {
      val is:ItemStack = event.original;
      if(!NBTHelper.hasTag(is, NBTTags.TOOL_BOX_INV))
        return;
      val items:Array[ItemStack] = NBTHelper.getToolBoxInventory(is);
      var newItem:ItemStack=null;
      var found:Boolean=false

      var n:Int=0;
      var slot:Int= -1;
      while(!found && n < items.length){
        if(items(n) == null){}
        else{
          if(is.isItemEqual(items(n))){
            newItem=items(n);
            slot=n;
            found=true;
          }else if(items(n).getItem() == ProxyCommon.itemToolBox){
            if(newItem==null){
              newItem=items(n);
              slot=n;
            }
          }
        }
        n+=1
      }
      if(slot == -1)
        return;
      items(slot)=null;
      NBTHelper.setToolBoxInventory(newItem, items);
      if(event.entityPlayer.inventory.getCurrentItem() == null)
      {
          event.entityPlayer.inventory.setInventorySlotContents(event.entityPlayer.inventory.currentItem, newItem);
      }
      else if(event.entityPlayer.inventory.addItemStackToInventory(newItem))
      {
          return;
      }
      else
      {
          event.entityPlayer.worldObj.spawnEntityInWorld(new EntityItem(event.entityPlayer.worldObj, event.entity.posX, event.entity.posY, event.entity.posZ, newItem));
      }
  }
  @SubscribeEvent
  def getToolTips(event:ItemTooltipEvent){
    val stack=event.itemStack;
    if(!NBTHelper.hasTag(stack, NBTTags.TOOL_BOX_INV))
      return;
    if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)){
      val items = NBTHelper.getToolBoxInventory(stack);
      if(items != null){
        for(n <-0 until items.length){
          if(items(n) == null)
            event.toolTip.asInstanceOf[List[String]].add(I18n.format("InvTools.Item.string")+(n+1)+": "+"\u00A7e"+I18n.format("InvTools.Empty.string"));
          else
            event.toolTip.asInstanceOf[List[String]].add(I18n.format("InvTools.Item.string")+(n + 1) + ": " + "\u00A7e" + items(n).getDisplayName());
        }
      }
    }else{
      event.toolTip.asInstanceOf[List[String]].add(I18n.format("InvTools.ShiftForToolBoxInfo.string"))
    }
  }
}