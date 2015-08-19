/**
 * @author Yuuto
 */
package yuuto.inventorytools.client.gui

import java.util.List
import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.entity.player.EntityPlayer
import yuuto.inventorytools.gui.ContainerToolBench
import yuuto.inventorytools.tiles.TileToolBench
import org.lwjgl.opengl.GL11
import net.minecraft.client.resources.I18n
import yuuto.inventorytools.ref.ReferenceInvTools
import net.minecraft.util.ResourceLocation
import net.minecraft.client.gui.GuiButton

object GuiContainerToolBench{
  final val texture:ResourceLocation= new ResourceLocation(ReferenceInvTools.MOD_ID.toLowerCase(), "textures/gui/ToolBench.png");
}
class GuiContainerToolBench(tile:TileToolBench, player:EntityPlayer) extends GuiContainer(new ContainerToolBench(tile, player)){
  this.ySize=179
  
  override def initGui(){
    super.initGui();
    this.buttonList.asInstanceOf[List[GuiButton]].add(new GuiButton(0, this.guiLeft+53, this.guiTop+21, 16, 16, "Open"))
  }
  
  override protected def drawGuiContainerForegroundLayer(mx:Int, my:Int){
      this.fontRendererObj.drawString(I18n.format("container.inventory", new Array[Any](0)), 8, this.ySize - 96 + 2, 4210752);
  }

  override protected def drawGuiContainerBackgroundLayer(partialTick:Float, mx:Int, my:Int){
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      this.mc.getTextureManager().bindTexture(GuiContainerToolBench.texture);
      val k:Int = (this.width - this.xSize) / 2;
      val l:Int = (this.height - this.ySize) / 2;
      this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
  }
  
  override protected def actionPerformed(button:GuiButton) {
    button.id match{
      case 0=>{
        if(tile.isToolBoxOpen()){
          tile.closeToolBox();
          button.displayString="Open"
        }else{
          tile.openToolBox();
          button.displayString="Close"
        }
      }
      case i=>return super.actionPerformed(button);
    }
  }
}