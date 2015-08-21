/**
 * @author Yuuto
 */
package yuuto.inventorytools.items

import java.util.List
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.relauncher.SideOnly
import net.minecraft.block.Block
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.IInventory
import net.minecraft.item.EnumAction
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.tileentity.TileEntity
import net.minecraft.world.World
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.entity.EntityJoinWorldEvent
import yuuto.inventorytools.InventoryTools
import yuuto.inventorytools.api.dolly.BlockData
import yuuto.inventorytools.api.dolly.DollyHandlerRegistry
import yuuto.inventorytools.ref.ReferenceInvTools
import yuuto.yuutolib.item.ModItem
import cpw.mods.fml.relauncher.Side
import net.minecraft.client.resources.I18n
import net.minecraft.util.MovingObjectPosition
import net.minecraft.util.MovingObjectPosition.MovingObjectType
import yuuto.inventorytools.util.NBTHelper
import yuuto.inventorytools.util.NBTTags
import net.minecraft.util.IIcon
import net.minecraft.client.renderer.texture.IIconRegister

object ItemDolly{
  var isPickingUp:Boolean=false;
}
class ItemDolly(name:String, val adv:Boolean) extends ModItem(InventoryTools.tab, ReferenceInvTools.MOD_ID, name){
  setMaxStackSize(1);
  this.hasSubtypes=true;
  MinecraftForge.EVENT_BUS.register(this);
  val icons:Array[IIcon]=new Array[IIcon](3);
  
  @SideOnly(Side.CLIENT)
  override def getSubItems(item:Item, tab:CreativeTabs, subItems:List[_]){
    subItems.asInstanceOf[List[ItemStack]].add(new ItemStack(this, 1, 0));
  }
  
  @SideOnly(Side.CLIENT)
  override def registerIcons(reg:IIconRegister){
    if(adv){
      this.itemIcon=reg.registerIcon(ReferenceInvTools.MOD_ID.toLowerCase()+":DiamondDollyEmpty");
      this.icons(0)=this.itemIcon;
      this.icons(1)=reg.registerIcon(ReferenceInvTools.MOD_ID.toLowerCase()+":DiamondDollyFilled");
      this.icons(2)=reg.registerIcon(ReferenceInvTools.MOD_ID.toLowerCase()+":DiamondDollyTransfer");
    }else{
      this.itemIcon=reg.registerIcon(ReferenceInvTools.MOD_ID.toLowerCase()+":GoldDollyEmpty");
      this.icons(0)=this.itemIcon;
      this.icons(1)=reg.registerIcon(ReferenceInvTools.MOD_ID.toLowerCase()+":GoldDollyFilled");
      this.icons(2)=reg.registerIcon(ReferenceInvTools.MOD_ID.toLowerCase()+":GoldDollyTransfer");
    }
  }
  
  @SideOnly(Side.CLIENT)
  override def getIconFromDamage(meta:Int):IIcon={
    if(meta < 0 || meta >= icons.length)
      return this.itemIcon;
    return icons(meta);
  }
  
  
  override def onItemRightClick(stack:ItemStack, world:World, player:EntityPlayer):ItemStack={
    if(stack.getItemDamage() != 1 && stack.getItemDamage() != 2)
      return stack;
    if(world.isRemote || !player.isSneaking())
        return stack;
    val blockPos:MovingObjectPosition = player.rayTrace(7, 0.0F);
    if(blockPos.typeOfHit == MovingObjectType.MISS){
      switchMode(stack, player, world, blockPos.blockX, blockPos.blockY, blockPos.blockZ, 0, blockPos.blockX, blockPos.blockY, blockPos.blockZ);
      player.swingItem();
    }
    return stack;
  }
  override def doesSneakBypassUse( world:World, x:Int, y:Int, z:Int, player:EntityPlayer):Boolean=true;
  override def isItemTool(stack:ItemStack):Boolean=true;
  
  @SideOnly(Side.CLIENT)
  override def addInformation(stack:ItemStack, player:EntityPlayer, list:List[_], flag:Boolean){
    if(!NBTHelper.hasTag(stack, NBTTags.DOLLY_DATA))
      return;
    list.asInstanceOf[List[String]].add(I18n.format(NBTHelper.getDollyDataTag(stack).getString("unlocName")+".name"));
  }
  
  override def onItemUseFirst(stack:ItemStack, player:EntityPlayer, world:World, x:Int, y:Int, z:Int, side:Int, hitX:Float, hitY:Float, hitZ:Float):Boolean={
    //if(world.isRemote)
     // return false;
    if(!player.isSneaking()){
      return false;
    }
    if(world.isRemote)
      return false;
    if(stack.getItemDamage() == 0){
      return onPickUpBlock(stack, player, world, x, y, z, side, hitX, hitY, hitZ);
    }
    if(stack.getItemDamage() == 1){
      return onPlaceBlock(stack, player, world, x, y, z, side, hitX, hitY, hitZ);
    }
    if(stack.getItemDamage() == 2){
      return transferInventory(stack, player, world, x, y, z, side, hitX, hitY, hitZ);
    }
    return false;
  }
  def onPickUpBlock(stack:ItemStack, player:EntityPlayer, world:World, x:Int, y:Int, z:Int, side:Int, hitX:Float, hitY:Float, hitZ:Float):Boolean={
    val b:Block=world.getBlock(x, y, z);
    val meta:Int=world.getBlockMetadata(x, y, z);
    if(DollyHandlerRegistry.isBlackListed(b, meta, adv))
      return false;
    val blockData:BlockData=new BlockData(b, meta);
    val blockHandler=DollyHandlerRegistry.getBlockHandler(blockData);
    if(blockHandler==null)
      return false;
    if(!blockHandler.onPickedUp(blockData, player, world, x, y, z, side, hitX, hitY, hitZ))
      return false;
    if(!NBTHelper.setDollyData(stack, blockData))
      return false;
    stack.setItemDamage(1);
    ItemDolly.isPickingUp=true;
    world.removeTileEntity(x, y, z);
    world.setBlockToAir(x, y, z);
    ItemDolly.isPickingUp=false;
    player.inventoryContainer.detectAndSendChanges();
    return true;
  }
  def onPlaceBlock(stack:ItemStack, player:EntityPlayer, world:World, x:Int, y:Int, z:Int, side:Int, hitX:Float, hitY:Float, hitZ:Float):Boolean={
    if(!NBTHelper.hasTag(stack, NBTTags.DOLLY_DATA)){
      stack.setItemDamage(0);
      player.inventoryContainer.detectAndSendChanges();
      return false;
    }
    val blockData=NBTHelper.getDollyData(stack);
    if(blockData == null){
      NBTHelper.removeTag(stack, NBTTags.DOLLY_DATA);
      stack.setItemDamage(0);
      player.inventoryContainer.detectAndSendChanges();
      return false;
    }
    val blockHandler=DollyHandlerRegistry.getBlockHandler(blockData);
    if(blockHandler == null){
      NBTHelper.removeTag(stack, NBTTags.DOLLY_DATA);
      stack.setItemDamage(0);
      player.inventoryContainer.detectAndSendChanges();
      return false;
    }
    if(!blockHandler.onPlaced(blockData, player, world, x, y, z, side, hitX, hitY, hitZ))
      return true;
    NBTHelper.removeTag(stack, NBTTags.DOLLY_DATA);
    stack.setItemDamage(0);
    player.inventoryContainer.detectAndSendChanges();
    return true;
  }
  def transferInventory(stack:ItemStack, player:EntityPlayer, world:World, x:Int, y:Int, z:Int, side:Int, hitX:Float, hitY:Float, hitZ:Float):Boolean={
    if(!NBTHelper.hasTag(stack, NBTTags.DOLLY_DATA)){
      stack.setItemDamage(0);
      player.inventoryContainer.detectAndSendChanges();
      return false;
    }
    val blockData=NBTHelper.getDollyData(stack);
    if(blockData == null){
      NBTHelper.removeTag(stack, NBTTags.DOLLY_DATA);
      stack.setItemDamage(0);
      player.inventoryContainer.detectAndSendChanges();
      return false;
    }
    val te=world.getTileEntity(x, y, z);
    if(te == null || !te.isInstanceOf[IInventory])
      return false;
    val tileHandler=DollyHandlerRegistry.getTileHandler(blockData.handlerName);
    if(tileHandler == null || !tileHandler.canTransferInventory(te.asInstanceOf[IInventory], blockData, player, world, x, y, z, side, hitX, hitY, hitZ))
      return false;
    tileHandler.onTransferInventory(te.asInstanceOf[IInventory], blockData, player, world, x, y, z, side, hitX, hitY, hitZ)
    NBTHelper.setDollyData(stack, blockData);
    if(!tileHandler.canTransferInventory(blockData, player)){
      stack.setItemDamage(1);
    }
    player.inventoryContainer.detectAndSendChanges();
    return true;
  }
  def switchMode(stack:ItemStack, player:EntityPlayer, world:World, x:Int, y:Int, z:Int, side:Int, hitX:Float, hitY:Float, hitZ:Float):Boolean={
    if(!NBTHelper.hasTag(stack, NBTTags.DOLLY_DATA)){
      stack.setItemDamage(0);
      player.inventoryContainer.detectAndSendChanges();
      return false;
    }
    val blockData=NBTHelper.getDollyData(stack);
    if(blockData == null){
      NBTHelper.removeTag(stack, NBTTags.DOLLY_DATA);
      stack.setItemDamage(0);
      player.inventoryContainer.detectAndSendChanges();
      return false;
    }
    val tileHandler=DollyHandlerRegistry.getTileHandler(blockData.handlerName);
    if(tileHandler == null || !tileHandler.canTransferInventory(blockData, player)){
      if(stack.getItemDamage() == 2){
        stack.setItemDamage(1)
        player.inventoryContainer.detectAndSendChanges();
        return stack.getItemDamage()==1;
      }
      return false;
    }
    if(stack.getItemDamage() == 1){
      stack.setItemDamage(2);
      player.inventoryContainer.detectAndSendChanges();
      return stack.getItemDamage()==2;
    }else if(stack.getItemDamage() == 2){
      stack.setItemDamage(1);
      player.inventoryContainer.detectAndSendChanges();
      return stack.getItemDamage()==1;
    }
    return false;
  }
  
  @SubscribeEvent
  def onEntitySpawn(event:EntityJoinWorldEvent){
    //if(event.entity.isInstanceOf[EntityItem] && ItemDolly.isPickingUp){
    //  event.setCanceled(true);
    //}
  }
}