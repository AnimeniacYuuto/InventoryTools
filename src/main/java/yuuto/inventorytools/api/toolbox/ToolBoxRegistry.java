package yuuto.inventorytools.api.toolbox;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import cpw.mods.fml.common.registry.GameRegistry;

public final class ToolBoxRegistry {
	
	private final static List<ItemData> blackList=new LinkedList<ItemData>();
	private final static List<ItemData> whiteList=new LinkedList<ItemData>();
	
	public static final boolean isValidTool(ItemStack stack){
		if((stack.getItem() instanceof ItemBlock) || (stack.getItem() instanceof ItemPotion) || (stack.getItem() instanceof ItemArmor) || (stack.getItem() instanceof ItemBucket))
			return false;
		if(isWhiteListed(stack))
			return true;
		if(isBlackListed(stack))
			return false;
		return stack.getMaxStackSize() == 1 && !(stack.getItem() instanceof ItemBlock) && !(stack.getItem() instanceof ItemPotion) && !(stack.getItem() instanceof ItemArmor) && !(stack.getItem() instanceof ItemBucket);
	}
	
	public final static boolean addToBlackList(String id){
		String[] parts = id.split(":", 4);
		if(parts == null || parts.length < 1)
			return false;
		ItemStack stack = GameRegistry.findItemStack(parts.length==1 ? "minecraft" : parts[0], parts.length==1 ? parts[0] : parts[1], 1);
		if(stack == null)
			return false;
		int meta=OreDictionary.WILDCARD_VALUE;
		if(parts.length >= 3){
			try{
				meta=Integer.parseInt(parts[2]);
			}catch(NumberFormatException e){
				meta=OreDictionary.WILDCARD_VALUE;
			}
		}
		return addToBlackList(new ItemData(stack.getItem(), meta));
	}
	public final static boolean addToBlackList(Item i, int meta){
		return addToBlackList(new ItemData(i, meta));
	}
	public final static boolean addToBlackList(ItemData data){
		return blackList.add(data);
	}
	public final static boolean addToWhiteList(String id){
		String[] parts = id.split(":", 4);
		if(parts.length < 1)
			return false;
		ItemStack stack = GameRegistry.findItemStack(parts.length==1 ? "minecraft" : parts[0], parts.length==1 ? parts[0] : parts[1], 1);
		if(stack == null)
			return false;
		if((stack.getItem() instanceof ItemBlock) || (stack.getItem() instanceof ItemPotion) || (stack.getItem() instanceof ItemArmor) || (stack.getItem() instanceof ItemBucket))
			return false;
		int meta=OreDictionary.WILDCARD_VALUE;
		if(parts.length >= 3){
			try{
				meta=Integer.parseInt(parts[2]);
			}catch(NumberFormatException e){
				meta=OreDictionary.WILDCARD_VALUE;
			}
		}
		return addToWhiteList(new ItemData(stack.getItem(), meta));
	}
	public final static boolean addToWhiteList(Item i, int meta){
		return addToWhiteList(new ItemData(i, meta));
	}
	public final static boolean addToWhiteList(ItemData data){
		return whiteList.add(data);
	}
	
	private static boolean isWhiteListed(ItemStack stack){
		for(ItemData d : whiteList){
			if(d.item == stack.getItem() && (d.meta == OreDictionary.WILDCARD_VALUE || d.meta == stack.getItemDamage())){
				return true;
			}
		}
		return false;
	}
	private static boolean isBlackListed(ItemStack stack){
		for(ItemData d : blackList){
			if(d.item == stack.getItem() && (d.meta == OreDictionary.WILDCARD_VALUE || d.meta == stack.getItemDamage())){
				return true;
			}
		}
		return false;
	}
}
