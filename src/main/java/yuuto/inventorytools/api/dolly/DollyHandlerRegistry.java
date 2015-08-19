package yuuto.inventorytools.api.dolly;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.oredict.OreDictionary;
import yuuto.inventorytools.api.dolly.handlers.defaults.block.DefaultDollyBlockHandler;
import yuuto.inventorytools.api.dolly.handlers.defaults.block.IDollyBlockHandler;
import yuuto.inventorytools.api.dolly.handlers.defaults.tile.DefaultDollyTileHandler;
import yuuto.inventorytools.api.dolly.handlers.defaults.tile.IDollyTileHandler;
import cpw.mods.fml.common.registry.GameRegistry;

public final class DollyHandlerRegistry {
	
	private final static IDollyBlockHandler defaultBlockHandler=new DefaultDollyBlockHandler();
	private final static IDollyTileHandler defaultTileHandler=new DefaultDollyTileHandler();
	private final static List<BlockData> blackListAll=new LinkedList<BlockData>();
	private final static List<BlockData> blackListNormal=new LinkedList<BlockData>();
	private final static Map<Class<? extends TileEntity>, IDollyTileHandler> tileMap = new HashMap<Class<? extends TileEntity>, IDollyTileHandler>();
	private final static Map<String, IDollyTileHandler> tileHandlerMap = new HashMap<String, IDollyTileHandler>();
	private final static Map<BlockData, IDollyBlockHandler> blockMap=new HashMap<BlockData, IDollyBlockHandler>();
	
	public final static boolean addToBlackListAll(String id){
		String[] parts = id.split(":", 4);
		if(parts == null || parts.length < 1)
			return false;
		Block b = GameRegistry.findBlock(parts.length==1 ? "minecraft" : parts[0], parts.length==1 ? parts[0] : parts[1]);
		if(b == null)
			return false;
		int meta=OreDictionary.WILDCARD_VALUE;
		if(parts.length >= 3){
			try{
				meta=Integer.parseInt(parts[2]);
			}catch(NumberFormatException e){
				meta=OreDictionary.WILDCARD_VALUE;
			}
		}
		return addToBlackListAll(new BlockData(b, meta));
	}
	public final static boolean addToBlackListAll(Block b, int meta){
		return addToBlackListAll(new BlockData(b, meta));
	}
	public final static boolean addToBlackListAll(BlockData data){
		return blackListAll.add(data);
	}
	public final static boolean addToBlackListNormal(String id){
		System.out.println("Adding "+id+" to normal black list");
		String[] parts = id.split(":", 4);
		if(parts == null || parts.length < 1)
			return false;
		System.out.println("Ther were "+parts.length+" parts in "+id+" to add to normal");
		Block b = GameRegistry.findBlock(parts.length==1 ? "minecraft" : parts[0], parts.length==1 ? parts[0] : parts[1]);
		if(b == null)
			return false;
		System.out.println("Found Block");
		int meta=OreDictionary.WILDCARD_VALUE;
		if(parts.length >= 3){
			try{
				meta=Integer.parseInt(parts[2]);
			}catch(NumberFormatException e){
				meta=OreDictionary.WILDCARD_VALUE;
			}
		}
		System.out.println("Adding Meta "+meta);
		return addToBlackListNormal(new BlockData(b, meta));
	}
	public final static boolean addToBlackListNormal(Block b, int meta){
		return addToBlackListNormal(new BlockData(b, meta));
	}
	public final static boolean addToBlackListNormal(BlockData data){
		System.out.println("Adding data for "+data.block.getLocalizedName()+":"+data.meta);
		return blackListNormal.add(data);
	}
	
	public final static boolean registerBlockHandler(String id, IDollyBlockHandler handler){
		String[] parts = id.split(":", 4);
		if(parts == null || parts.length < 1)
			return false;
		Block b = GameRegistry.findBlock(parts.length==1 ? "minecraft" : parts[0], parts.length==1 ? parts[0] : parts[1]);
		if(b == null)
			return false;
		int meta=OreDictionary.WILDCARD_VALUE;
		if(parts.length >= 3){
			try{
				meta=Integer.parseInt(parts[2]);
			}catch(NumberFormatException e){
				meta=OreDictionary.WILDCARD_VALUE;
			}
		}
		return registerBlockHandler(new BlockData(b, meta), handler);
	}
	public final static boolean registerBlockHandler(Block b, int meta, IDollyBlockHandler handler){
		return registerBlockHandler(new BlockData(b, meta), handler);
	}
	public final static boolean registerBlockHandler(BlockData data, IDollyBlockHandler handler){
		blockMap.put(data, handler);
		return blockMap.get(data)==handler;
	}
	public final static boolean registerTileHandler(Class<? extends TileEntity> clazz, IDollyTileHandler handler){
		if(!tileHandlerMap.containsKey(handler.getID())){
			tileHandlerMap.put(handler.getID(), handler);
		}
		if(!tileMap.containsKey(clazz)){
			tileMap.put(clazz, handler);
			return tileMap.get(clazz)==handler;
		}else{
			return true;
		}
	}
	public final static boolean isBlackListed(Block b, int meta, boolean advanced){
		if(isBlackListedForAll(b, meta))
			return true;
		if(!advanced)
			return isBlackListedForNormall(b, meta);
		return false;
	}
	public final static IDollyTileHandler getTileHandler(TileEntity tile){
		System.out.println("Getting handler for "+tile.getClass().getName());
		for(Class<? extends TileEntity> clazz : tileMap.keySet()){
			System.out.println("Checking "+clazz.getName());
			if(clazz.isInstance(tile)){
				return tileMap.get(clazz);
			}
		}
		return defaultTileHandler;
	}
	public final static IDollyTileHandler getTileHandler(String name){
		if(name == null || name.trim().isEmpty())
			return null;
		IDollyTileHandler handler = tileHandlerMap.get(name);
		if(handler != null)
			return handler;
		return defaultTileHandler;
	}
	public final static IDollyBlockHandler getBlockHandler(BlockData blockData){
		for(BlockData d : blockMap.keySet()){
			if(d.block == blockData.block && (d.meta == OreDictionary.WILDCARD_VALUE || d.meta == blockData.meta))
				return blockMap.get(d);
		}
		return defaultBlockHandler;
	}
	
	private final static boolean isBlackListedForAll(Block b, int meta){
		for(BlockData d : blackListAll){
			if(d.block == b && (d.meta == OreDictionary.WILDCARD_VALUE || d.meta == meta))
				return true;
		}
		return false;
	}
	private final static boolean isBlackListedForNormall(Block b, int meta){
		System.out.println("Checking normal black list for "+b.getLocalizedName());
		for(BlockData d : blackListNormal){
			System.out.println("Checking normal black entry "+d.block.getLocalizedName());
			if(d.block == b && (d.meta == OreDictionary.WILDCARD_VALUE || d.meta == meta))
				return true;
		}
		return false;
	}
	private final boolean contains(Class<? extends TileEntity> tileClass){
		for(Class<? extends TileEntity> clazz : tileMap.keySet()){
			if(clazz == tileClass){
				return true;
			}
		}
		return false;
	}

}
