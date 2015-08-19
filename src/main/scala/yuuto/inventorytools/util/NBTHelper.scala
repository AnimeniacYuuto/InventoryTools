/**
 * @author Yuuto
 */
package yuuto.inventorytools.util

import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.nbt.NBTTagList
import yuuto.inventorytools.config.ConfigurationIT
import net.minecraft.nbt.NBTBase
import yuuto.inventorytools.api.dolly.BlockData

object NBTHelper {
  def hasTag(stack:ItemStack, tag:String):Boolean={
    return stack != null && stack.hasTagCompound() && stack.getTagCompound().hasKey(tag);
  }
  
  def getToolBoxInventory(stack:ItemStack):Array[ItemStack]={
    val ret:Array[ItemStack]=new Array[ItemStack](ConfigurationIT.maxTools);
    initTagCompound(stack)
    if(!stack.getTagCompound.hasKey(NBTTags.TOOL_BOX_INV))
      return null;
    val tagList:NBTTagList=getTagList(stack.getTagCompound(), NBTTags.TOOL_BOX_INV);
    for(i<-0 until tagList.tagCount()){
      val tag:NBTTagCompound=tagList.getCompoundTagAt(i);
      val index:Int=tag.getInteger("slotIndex");
      if(index >= 0 && index < ret.length){
        ret(index)=ItemStack.loadItemStackFromNBT(tag);
      }
    }
    return ret;
  }
  def setToolBoxInventory(stack:ItemStack, inv:Array[ItemStack]){
    val tagList:NBTTagList=new NBTTagList()
    for(i<-0 until inv.length if(inv(i) != null)){
      val nbt:NBTTagCompound=inv(i).writeToNBT(new NBTTagCompound());
      nbt.setByte("slotIndex", i.asInstanceOf[Byte]);
      tagList.appendTag(nbt);
    }
    if(tagList.tagCount() < 1){
      removeTag(stack, NBTTags.TOOL_BOX_INV)
      return;
    }
    initTagCompound(stack);
    stack.getTagCompound().setTag(NBTTags.TOOL_BOX_INV, tagList);
  }
  def getDollyData(stack:ItemStack):BlockData={
    initTagCompound(stack)
    if(!stack.getTagCompound.hasKey(NBTTags.DOLLY_DATA))
      return null;
    return BlockData.LoadFromNBT(stack.getTagCompound().getCompoundTag(NBTTags.DOLLY_DATA));
  }
  def getDollyDataTag(stack:ItemStack):NBTTagCompound={
    initTagCompound(stack)
    if(!stack.getTagCompound.hasKey(NBTTags.DOLLY_DATA))
      return null;
    return stack.getTagCompound().getCompoundTag(NBTTags.DOLLY_DATA);
  }
  def setDollyData(stack:ItemStack, data:BlockData):Boolean={
    val nbt=data.WriteToNBT(new NBTTagCompound());
    if(nbt == null || nbt.hasNoTags())
      return false;
    initTagCompound(stack)
    stack.getTagCompound().setTag(NBTTags.DOLLY_DATA, nbt);
    return true;
  }
  
  def removeTag(stack:ItemStack, tag:String){
    if(!stack.hasTagCompound())
      return;
    stack.getTagCompound().removeTag(tag);
  }
  
  def initTagCompound(stack:ItemStack){
    if(!stack.hasTagCompound()){
      stack.setTagCompound(new NBTTagCompound());
    }
  }
  
  def getTagList(nbt:NBTTagCompound, tag:String):NBTTagList={
    if(!nbt.hasKey(tag)){
      nbt.setTag(tag, new NBTTagList());
    }
    val returnValue:NBTBase = nbt.getTag(tag);
    if(!returnValue.isInstanceOf[NBTTagList]){
      return new NBTTagList();
    }
    return returnValue.asInstanceOf[NBTTagList];
  }
  
}