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
import yuuto.inventorytools.until.LogHelperIT;
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
		return addToBlackListNormal(new BlockData(b, meta));
	}
	public final static boolean addToBlackListNormal(Block b, int meta){
		return addToBlackListNormal(new BlockData(b, meta));
	}
	public final static boolean addToBlackListNormal(BlockData data){
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
	public final static boolean registerTileHandler(String clazzName, IDollyTileHandler handler){
		try{ 
			@SuppressWarnings("unchecked")
			Class<? extends TileEntity> clazz=(Class<? extends TileEntity>)Class.forName(clazzName);
			return registerTileHandler(clazz, handler);
		}catch(ClassNotFoundException e){
        	LogHelperIT.Error("Could not add "+handler.getID()+" for "+clazzName+" class not found", e);
		}catch(ClassCastException e){
			LogHelperIT.Error("Could not add "+handler.getID()+" for "+clazzName+" class is not a Tile Entity", e);
		}
		return false;
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
		for(Class<? extends TileEntity> clazz : tileMap.keySet()){
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
		for(BlockData d : blackListNormal){
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
