package yuuto.inventorytools.api.toolbox;

import net.minecraft.item.Item;
import net.minecraftforge.oredict.OreDictionary;

public class ItemData {
	public Item item=null;
	public int meta=OreDictionary.WILDCARD_VALUE;
	
	public ItemData(){}
	public ItemData(Item b, int meta){
		this.item=b;
		this.meta = meta;
	}

}
