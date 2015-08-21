package yuuto.inventorytools.compat.handlers;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;

import mcp.mobius.betterbarrels.BetterBarrels;
import mcp.mobius.betterbarrels.ServerTickHandler;
import mcp.mobius.betterbarrels.Utils;
import mcp.mobius.betterbarrels.bspace.BSpaceStorageHandler;
import mcp.mobius.betterbarrels.common.blocks.IBarrelStorage;
import mcp.mobius.betterbarrels.common.blocks.StorageLocal;
import mcp.mobius.betterbarrels.common.blocks.TileEntityBarrel;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import yuuto.inventorytools.api.dolly.BlockData;
import yuuto.inventorytools.api.dolly.InventoryMerger;
import yuuto.inventorytools.api.dolly.handlers.defaults.tile.DefaultDollyTileHandler;
import yuuto.inventorytools.until.LogHelperIT;

public class JabbaBarrelTileHandler extends DefaultDollyTileHandler{
	
	protected static JabbaBarrelTileHandler instance=null;
	protected static Field fieldLinks=null;
	public static final JabbaBarrelTileHandler getInstance(){
		if(instance==null)
			instance=new JabbaBarrelTileHandler();
		return instance;
	}
	
	private JabbaBarrelTileHandler(){}
	
	
	@Override
	public String getID() {
		return "tilehandler.jabba.betterbarrel";
	}
	
	@Override
	public void onPlaced(TileEntity tile, BlockData data, EntityPlayer player,
			World world, int x, int y, int z, int side, float hitX, float hitY,
			float hitZ) {
		if(data.tileData == null || data.tileData.hasNoTags()){
			data.block.onBlockPlacedBy(world, x, y, z, player, new ItemStack(data.block, 1, data.meta));
			data.block.onPostBlockPlaced(world, x, y, z, data.meta);
			return;
		}
		NBTTagCompound nbt=data.tileData;
		nbt.setInteger("x", x);
		nbt.setInteger("y", y);
		nbt.setInteger("z", z);
		
		ForgeDirection newBarrelRotation    = Utils.getDirectionFacingEntity(player, false);
		ForgeDirection oldBarrelRotation    = ForgeDirection.getOrientation(nbt.getInteger("rotation"));
		ForgeDirection newBarrelOrientation = Utils.getDirectionFacingEntity(player, BetterBarrels.allowVerticalPlacement);
		ForgeDirection oldBarrelOrientation = ForgeDirection.getOrientation(nbt.getInteger("orientation"));
		int[] newSideArray = new int[6];
		int[] oldSideArray = nbt.getIntArray("sideUpgrades");
		int[] newSideMetaArray = new int[6];
		int[] oldSideMetaArray = nbt.getIntArray("sideMeta");

		/* Note: the barrel should never have these values set as unknown, but this will prevent the code from crashing or infinite looping */
		if (oldBarrelRotation == ForgeDirection.UNKNOWN) oldBarrelRotation = ForgeDirection.SOUTH;
		if (oldBarrelOrientation == ForgeDirection.UNKNOWN) oldBarrelOrientation = ForgeDirection.SOUTH;

		/* Normalize the barrel so it is upright and front facing player*/
		if (oldBarrelOrientation == ForgeDirection.UP || oldBarrelOrientation == ForgeDirection.DOWN) {
			ForgeDirection rot = oldBarrelRotation.getRotation(oldBarrelOrientation);
			for (int i = 0; i < 6; i++) {
				int j = ForgeDirection.getOrientation(i).getRotation(rot).ordinal();
				newSideArray[j] = oldSideArray[i];
				newSideMetaArray[j] = oldSideMetaArray[i];
			}
			oldBarrelOrientation = oldBarrelRotation;
			oldSideArray = newSideArray.clone();
			oldSideMetaArray = newSideMetaArray.clone();
		}

		int numberRotationsVAxis = 0;
		while (newBarrelRotation != oldBarrelRotation) {
			numberRotationsVAxis += 1;
			oldBarrelRotation = oldBarrelRotation.getRotation(ForgeDirection.UP);
		}

		for (int i = 0; i < 6; i++) {
			ForgeDirection idir = ForgeDirection.getOrientation(i);
			for (int rot = 0; rot < numberRotationsVAxis; rot++) {
				idir = idir.getRotation(ForgeDirection.UP);
			}
			newSideArray[idir.ordinal()] = oldSideArray[i];
			newSideMetaArray[idir.ordinal()] = oldSideMetaArray[i];
		}

		/* if new orientation is up/down, rotate appropriately */
		if (newBarrelOrientation == ForgeDirection.UP || newBarrelOrientation == ForgeDirection.DOWN) {
			oldSideArray = newSideArray.clone();
			oldSideMetaArray = newSideMetaArray.clone();
			ForgeDirection rot = newBarrelRotation.getRotation(newBarrelOrientation.getOpposite());
			for (int i = 0; i < 6; i++) {
				int j = ForgeDirection.getOrientation(i).getRotation(rot).ordinal();
				newSideArray[j] = oldSideArray[i];
				newSideMetaArray[j] = oldSideMetaArray[i];
			}
		}

		nbt.setInteger("orientation", newBarrelOrientation.ordinal());
		nbt.setInteger("rotation", newBarrelRotation.ordinal());
		nbt.setIntArray("sideUpgrades", newSideArray);
		nbt.setIntArray("sideMeta", newSideMetaArray);
		tile.readFromNBT(nbt);
		tile.markDirty();
	}

	@Override
	public void onTransferInventory(IInventory target, BlockData data,
			EntityPlayer player, World world, int x, int y, int z, int side,
			float hitX, float hitY, float hitZ) {
		if(data.tileData == null || data.tileData.hasNoTags())
			return;
		boolean bSpace = data.tileData.getBoolean("ender");
		IBarrelStorage storage = null;
		int id = -1;
		if(bSpace){
			LogHelperIT.Info("Loading BSpaceStorage");
			id = data.tileData.getInteger("bspaceid");
			storage = BSpaceStorageHandler.instance().getStorage(id);
			//BSpaceStorageHandler.instance().registerEnderBarrel(id, storage);
			//storage = BSpaceStorageHandler.instance().getStorageOriginal(id);
		}else{
			storage = new StorageLocal();
			storage.readTagCompound(data.tileData.getCompoundTag("storage"));
		}
		if(storage == null || !storage.hasItem() || storage.getAmount() < 1)
			return;
		boolean flag=false;
		while(!flag && storage.hasItem() && storage.getAmount() > 0){
			ItemStack stack1=storage.getStack();
			int orig=stack1.stackSize;
			InventoryMerger.mergeStackIntoInventory(stack1, target);
			storage.addStack(stack1);
			int dif = orig-stack1.stackSize;
			if(dif == 0){
				flag = true;
				break;
			}
		}
		if(bSpace){
			LogHelperIT.Info("Attempting BSpaceStorage Update");
			storage.markDirty();
			//LogHelperIT.Info("Has Links? "+BSpaceStorageHandler.instance().hasLinks(id));
			//TileEntityBarrel te = BSpaceStorageHandler.instance().getBarrel(id);
			//LogHelperIT.Info("Has Tile? "+(te !=null));
			//BSpaceStorageHandler.instance().updateAllBarrels(id);
			//BSpaceStorageHandler.instance().markAllDirty(id);
			//BSpaceStorageHandler.instance().unregisterEnderBarrel(id);
			markBSpaceDirty(id);
		}
		data.tileData.removeTag("storage");
		data.tileData.setTag("storage", storage.writeTagCompound());
	}

	@Override
	public boolean canTransferInventory(IInventory target, BlockData data,
			EntityPlayer player, World world, int x, int y, int z, int side,
			float hitX, float hitY, float hitZ) {
		return canTransferInventory(data, player);
	}

	@Override
	public boolean canTransferInventory(BlockData data, EntityPlayer player) {
		if(data.tileData == null || data.tileData.hasNoTags())
			return false;
		boolean bSpace = data.tileData.getBoolean("ender");
		IBarrelStorage storage = null;
		if(bSpace){
			int id = data.tileData.getInteger("bspaceid");
			storage = BSpaceStorageHandler.instance().getStorage(id);
		}else{
			storage = new StorageLocal();
			storage.readTagCompound(data.tileData.getCompoundTag("storage"));
		}
		if(storage == null || !storage.hasItem() || storage.getAmount() < 1)
			return false;
		return true;
	}
	
	@SuppressWarnings("unchecked")
	private void markBSpaceDirty(int id){
		if(!BSpaceStorageHandler.instance().hasLinks(id))
			return;
		Field linksField = getTransformedLinksField();
		HashMap<Integer, HashSet<Integer>> links = null;
		try {
			links = (HashMap<Integer, HashSet<Integer>>)linksField.get(BSpaceStorageHandler.instance());
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
		if(links == null)
			return;
		HashSet<Integer> linkSet = links.get(id);
		for(Integer targetID : linkSet){
			TileEntityBarrel target = BSpaceStorageHandler.instance().getBarrel(targetID);
			if (target != null)
				ServerTickHandler.INSTANCE.markDirty(target, false);
		}
	}
	private static Field getTransformedLinksField(){
		if(fieldLinks != null)
			return fieldLinks;
		try {
			fieldLinks = BSpaceStorageHandler.class.getDeclaredField("links");
			fieldLinks.setAccessible(true);
			return fieldLinks;
		} catch (NoSuchFieldException | SecurityException e) {
			e.printStackTrace();
		}
		return null;
	}
}
