/**
 * @author Yuuto
 */
package yuuto.inventorytools.tiles

import yuuto.yuutolib.tile.ModTile
import yuuto.yuutolib.inventory.InventorySimple
import yuuto.inventorytools.config.ConfigurationIT
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.nbt.NBTTagList
import net.minecraftforge.common.util.Constants
import yuuto.inventorytools.util.NBTHelper
import yuuto.inventorytools.util.NBTTags
import yuuto.inventorytools.api.toolbox.ToolBoxRegistry
import yuuto.inventorytools.InventoryTools
import yuuto.inventorytools.network.MessageToolBench
import yuuto.inventorytools.proxy.ProxyCommon

class TileToolBench extends ModTile{
  
  protected var toolBoxOpen:Boolean=false; 
  val toolBox:InventorySimple=new InventorySimple(1){
    override def getInventoryStackLimit():Int=1;
    override def isItemValidForSlot(slot:Int, stack:ItemStack):Boolean={
      return !toolBoxOpen && (stack == null || stack.getItem() == ProxyCommon.itemToolBox);
    }
  }
  val inv:InventorySimple=new InventorySimple(ConfigurationIT.maxTools){
    override def getInventoryStackLimit():Int=1;
    override def isItemValidForSlot(slot:Int, stack:ItemStack):Boolean={
      return toolBoxOpen && stack.getItem() != ProxyCommon.itemToolBox && ToolBoxRegistry.isValidTool(stack);
    }
  }
  
  def isToolBoxOpen():Boolean=toolBoxOpen
  
  def openToolBox(){
    if(toolBoxOpen || !hasToolBox())
      return;
    toolBoxOpen = true;
    openToolBoxInv();
    this.getToolBox().setItemDamage(1);
    if(this.getWorldObj().isRemote){
      InventoryTools.network.sendToServer(new MessageToolBench(0, this.getWorldObj().provider.dimensionId, this.xCoord, this.yCoord, this.zCoord))
    }
  }
  def openToolBoxInv(){
    if(!NBTHelper.hasTag(getToolBox(), NBTTags.TOOL_BOX_INV))
      return;
    val tb:Array[ItemStack] = NBTHelper.getToolBoxInventory(getToolBox());
    if(tb == null)
      return;
    val max=Math.min(tb.length, inv.getSizeInventory())
    for(i<-0 until max){
      inv.setInventorySlotContents(i, tb(i));
    }
  }
  def closeToolBox(){
    if(!toolBoxOpen)
      return;
    toolBoxOpen = false;
    NBTHelper.setToolBoxInventory(toolBox.getStackInSlot(0), inv.getContents());
    for(i<-0 until inv.getSizeInventory()){
      inv.setInventorySlotContents(i, null.asInstanceOf[ItemStack]);
    }
    this.getToolBox().setItemDamage(0);
    if(this.getWorldObj().isRemote){
      InventoryTools.network.sendToServer(new MessageToolBench(1, this.getWorldObj().provider.dimensionId, this.xCoord, this.yCoord, this.zCoord))
    }
  }
  
  def hasToolBox():Boolean=toolBox.getStackInSlot(0) != null;
  def getToolBox():ItemStack=toolBox.getStackInSlot(0);
  def setToolBox(stack:ItemStack)=toolBox.setInventorySlotContents(0, stack)
  
  override def writeToNBT(nbt:NBTTagCompound){
    super.writeToNBT(nbt);
    if(!hasToolBox())
      return;
    nbt.setTag("ToolBox", getToolBox().writeToNBT(new NBTTagCompound()))
    if(!toolBoxOpen)
      return;
    nbt.setBoolean("isToolBoxOpen", toolBoxOpen);
    val tagList:NBTTagList=new NBTTagList();
    for(i <- 0 until inv.getSizeInventory() if(inv.getStackInSlot(i) != null)){
      val stacknbt:NBTTagCompound=inv.getStackInSlot(i).writeToNBT(new NBTTagCompound());
      stacknbt.setByte("slotIndex", i.asInstanceOf[Byte]);
      tagList.appendTag(stacknbt);
    }
    nbt.setTag("inv", tagList);
  }
  override def readFromNBT(nbt:NBTTagCompound){
    super.readFromNBT(nbt);
    if(!nbt.hasKey("ToolBox"))
      return;
    setToolBox(ItemStack.loadItemStackFromNBT(nbt.getCompoundTag("ToolBox")))
    if(!nbt.getBoolean("isToolBoxOpen"))
      return;
    val tagList=nbt.getTagList("inv", Constants.NBT.TAG_COMPOUND);
    if(tagList.tagCount() < 1)
      return;
    for(i <-0 until tagList.tagCount()){
      val stacknbt:NBTTagCompound = tagList.getCompoundTagAt(i);
      val slotIndex:Int=stacknbt.getInteger("slotIndex");
      if(slotIndex >=0 && slotIndex < inv.getSizeInventory()){
        inv.setInventorySlotContents(slotIndex, ItemStack.loadItemStackFromNBT(stacknbt));
      }
    }
  }
  
  
  
  
}