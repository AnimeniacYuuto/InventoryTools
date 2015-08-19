/**
 * @author Yuuto
 */
package yuuto.inventorytools.blocks

import yuuto.inventorytools.InventoryTools
import net.minecraft.block.material.Material
import yuuto.inventorytools.ref.ReferenceInvTools
import yuuto.yuutolib.block.ModBlockContainer
import yuuto.inventorytools.tiles.TileToolBench
import net.minecraft.tileentity.TileEntity
import net.minecraft.world.World
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.IIcon
import cpw.mods.fml.relauncher.SideOnly
import net.minecraft.client.renderer.texture.IIconRegister
import cpw.mods.fml.relauncher.Side

class BlockToolBench(name:String) extends ModBlockContainer(Material.rock, InventoryTools.tab, ReferenceInvTools.MOD_ID, name){
  val icons:Array[IIcon] = new Array[IIcon](3);  
  
  override def createNewTileEntity(world:World, meta:Int):TileEntity={
    return new TileToolBench();
  }
  
 override def onBlockActivated(world:World, x:Int, y:Int, z:Int, player:EntityPlayer, side:Int, hitX:Float, hitY:Float, hitZ:Float):Boolean={
   val te:TileEntity = world.getTileEntity(x, y, z);
   if(te == null || player.isSneaking())
     return false;
   if(!te.isInstanceOf[TileToolBench])
     return false;
   if(!world.isRemote)
     player.openGui(InventoryTools, 0, world, x, y, z);
   return true;
 }
  
  @SideOnly(Side.CLIENT)
  override def registerBlockIcons(reg:IIconRegister)={
    this.blockIcon=reg.registerIcon(this.getTextureName()+"Top");
    this.icons(0)=this.blockIcon;
    this.icons(1)=reg.registerIcon(this.getTextureName()+"Side1");
    this.icons(2)=reg.registerIcon(this.getTextureName()+"Side2");
  }
  
  @SideOnly(Side.CLIENT)
  override def getIcon(side:Int, meta:Int):IIcon={
    if(side == 1)
      return icons(0);
    if(side == 2 || side == 3)
      return icons(1);
    return icons(2);
  }
}