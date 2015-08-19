/**
 * @author Yuuto
 */
package yuuto.inventorytools.client.gui

import java.util.List
import net.minecraft.client.gui.GuiScreen
import net.minecraft.item.ItemStack
import net.minecraft.client.renderer.entity.RenderItem
import yuuto.inventorytools.InventoryTools
import net.minecraft.client.gui.GuiButton
import yuuto.inventorytools.client.gui.button.GuiButtonItem
import yuuto.inventorytools.network.MessageToolBox

class GuiScreenToolBox(var items:Array[ItemStack]) extends GuiScreen(){
  val itemRenders:RenderItem = new RenderItem();
  if(items==null)
    items=new Array[ItemStack](0);
  var guiLeft, guiTop, xSize, ySize:Int=0;
  xSize = 300;
  ySize = 60;

  override def initGui(){
      super.initGui();

      this.guiLeft = (this.width - this.xSize) / 2;
      this.guiTop = (this.height - this.ySize) / 2;
      buttonList.clear();
      for(y <-0 until 2){
          for(x <-0 until 9){
            val index:Int=x+y*9;  
            if(items.length > index && items(index) != null)
              {
                  buttonList.asInstanceOf[List[GuiButton]].add(new GuiButtonItem(items(index), index, guiLeft + 20+x * 30, guiTop + y * 30, "", itemRenders, fontRendererObj));
              }
              else
              {
                  buttonList.asInstanceOf[List[GuiButton]].add(new GuiButtonItem(null.asInstanceOf[ItemStack], index, guiLeft +20+ x * 30, guiTop + y * 30, "", itemRenders, fontRendererObj));
              }
          }
      }
  }

  override def actionPerformed(button:GuiButton)
  {
      InventoryTools.network.sendToServer(new MessageToolBox(button.id));
      mc.thePlayer.closeScreen();
  }

  override def keyTyped(x:Char, code:Int){
      if (code == 1 || code == this.mc.gameSettings.keyBindInventory.getKeyCode())
      {
          this.mc.thePlayer.closeScreen();
      }
  }

  override def doesGuiPauseGame():Boolean=false;
}