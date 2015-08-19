/**
 * @author Yuuto
 */
package yuuto.inventorytools.client.gui.button

import net.minecraft.client.gui.GuiButton
import net.minecraft.item.ItemStack
import net.minecraft.client.gui.FontRenderer
import net.minecraft.client.renderer.entity.RenderItem
import net.minecraft.client.Minecraft
import org.lwjgl.opengl.GL11
import net.minecraft.client.renderer.OpenGlHelper

class GuiButtonItem(val stack:ItemStack, id:Int, val x:Int, val y:Int, val text:String, val itemRender:RenderItem, var fontRenderer:FontRenderer) extends GuiButton(id, x, y, 20, 20, text){
  val mc:Minecraft=Minecraft.getMinecraft();
  override def drawButton(mc:Minecraft, mx:Int, my:Int){
      if (this.visible){
          mc.getTextureManager().bindTexture(GuiButton.buttonTextures);
          GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
          GL11.glDisable(GL11.GL_LIGHTING);
          this.field_146123_n = mx >= this.xPosition && my >= this.yPosition && mx < this.xPosition + this.width && my < this.yPosition + this.height;
          val k:Int = this.getHoverState(this.field_146123_n);
          GL11.glEnable(GL11.GL_BLEND);
          OpenGlHelper.glBlendFunc(770, 771, 1, 0);
          GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
          this.drawTexturedModalRect(this.xPosition, this.yPosition, 0, 46 + k * 20, this.width / 2, this.height);
          this.drawTexturedModalRect(this.xPosition + this.width / 2, this.yPosition, 200 - this.width / 2, 46 + k * 20, this.width / 2, this.height);
          this.mouseDragged(mc, mx, my);
          var l:Int = 14737632;

          if (packedFGColour != 0){
              l = packedFGColour;
          }
          else if (!this.enabled){
              l = 10526880;
          }
          else if (this.field_146123_n){
              l = 16777120;
          }

          if(stack != null)
          {
              this.drawItemStack(stack, this.xPosition + 2, this.yPosition + 2, this.displayString);
          }
      }
  }

  private def drawItemStack(is:ItemStack, x:Int, y:Int, name:String){
      GL11.glTranslatef(0.0F, 0.0F, 32.0F);
      val zLevelOrigin:Float = this.zLevel;
      this.zLevel = 200.0F;
      itemRender.zLevel = 200.0F;
      var font:FontRenderer = null;
      if (is != null) font = is.getItem().getFontRenderer(is);
      if (font == null) font = fontRenderer;
      itemRender.renderItemAndEffectIntoGUI(font, this.mc.getTextureManager(), is, x, y);
      //itemRender.renderItemOverlayIntoGUI(font, this.mc.getTextureManager(), is, x, y);
      this.zLevel = zLevelOrigin;
      itemRender.zLevel = 0.0F;
  }
}