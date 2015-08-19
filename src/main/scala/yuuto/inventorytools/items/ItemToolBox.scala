/**
 * @author Yuuto
 */
package yuuto.inventorytools.items

import java.util.List
import yuuto.yuutolib.item.ModItem
import yuuto.inventorytools.InventoryTools
import yuuto.inventorytools.ref.ReferenceInvTools
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import cpw.mods.fml.relauncher.SideOnly
import cpw.mods.fml.relauncher.Side
import yuuto.inventorytools.util.NBTTags
import yuuto.inventorytools.util.NBTHelper
import org.lwjgl.input.Keyboard
import net.minecraft.client.resources.I18n
import net.minecraft.util.IIcon
import net.minecraft.client.renderer.texture.IIconRegister

class ItemToolBox(name:String) extends ModItem(InventoryTools.tab, ReferenceInvTools.MOD_ID, name){
  setMaxStackSize(1);
  this.hasSubtypes=true;
  protected var openIcon:IIcon=null; 
  
  @SideOnly(Side.CLIENT)
  override def registerIcons(reg:IIconRegister){
    this.itemIcon=reg.registerIcon(this.getIconString()+"Closed");
    this.openIcon=reg.registerIcon(this.getIconString()+"Opened");
  }
  
  @SideOnly(Side.CLIENT)
  override def getIconFromDamage(meta:Int):IIcon={
    if(meta==1)
      return openIcon;
    return this.itemIcon;
  }
}