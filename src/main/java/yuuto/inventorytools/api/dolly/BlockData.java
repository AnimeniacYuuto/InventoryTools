package yuuto.inventorytools.api.dolly;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.OreDictionary;

public class BlockData {
	public Block block=null;
	public int meta=OreDictionary.WILDCARD_VALUE;
	public NBTTagCompound tileData =null;
	public String handlerName=null;
	
	public BlockData(){}
	public BlockData(Block b, int meta){
		this.block=b;
		this.meta = meta;
	}
	
	public NBTTagCompound WriteToNBT(NBTTagCompound nbt){
		if(block == null || block == Blocks.air)
			return nbt;
		nbt.setString("blockName", Block.blockRegistry.getNameForObject(block));
		nbt.setShort("meta", (short)meta);
		ItemStack stack = new ItemStack(block, 1, meta);
		nbt.setString("unlocName", stack.getUnlocalizedName());
		if(tileData != null && !tileData.hasNoTags()){
			nbt.setTag("tileData", tileData);
		}
		if(handlerName != null){
			nbt.setString("tileHandler", handlerName);
		}
		return nbt;
	}
	
	public static BlockData LoadFromNBT(NBTTagCompound nbt){
		BlockData data = new BlockData();
		data.block = Block.getBlockFromName(nbt.getString("blockName"));
		if(data.block == null || data.block == Blocks.air)
			return null;
		data.meta = nbt.getInteger("meta");
		if(nbt.hasKey("tileData")){
			data.tileData = nbt.getCompoundTag("tileData");
		}
		if(nbt.hasKey("tileHandler")){
			data.handlerName=nbt.getString("tileHandler");
		}
		return data;
	}

}
