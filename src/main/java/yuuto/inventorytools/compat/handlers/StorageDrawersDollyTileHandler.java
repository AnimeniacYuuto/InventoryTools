package yuuto.inventorytools.compat.handlers;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import yuuto.inventorytools.api.dolly.BlockData;
import yuuto.inventorytools.api.dolly.InventoryMerger;
import yuuto.inventorytools.api.dolly.RotationUtils;
import yuuto.inventorytools.api.dolly.handlers.defaults.tile.DefaultDollyTileHandler;
import yuuto.inventorytools.compat.storagedrawers.CompDrawerDataWrapper;
import yuuto.inventorytools.compat.storagedrawers.DummyCentralInventory;
import yuuto.inventorytools.compat.storagedrawers.DummyStorageProvider;

import com.jaquadro.minecraft.storagedrawers.api.storage.IDrawer;
import com.jaquadro.minecraft.storagedrawers.api.storage.attribute.LockAttribute;
import com.jaquadro.minecraft.storagedrawers.block.BlockCompDrawers;
import com.jaquadro.minecraft.storagedrawers.storage.CompDrawerData;
import com.jaquadro.minecraft.storagedrawers.storage.DrawerData;

public class StorageDrawersDollyTileHandler extends DefaultDollyTileHandler{
	
	protected static StorageDrawersDollyTileHandler instance=null;
	public static final StorageDrawersDollyTileHandler getInstance(){
		if(instance==null)
			instance=new StorageDrawersDollyTileHandler();
		return instance;
	}
	
	private StorageDrawersDollyTileHandler(){}
	
	
	@Override
	public String getID() {
		return "tilehandler.storagedrawers.storagedrawers";
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
		
		nbt.setInteger("Dir", (short)RotationUtils.getOrientationOnPlacement(player).ordinal());
		tile.readFromNBT(nbt);
	}
	@Override
	public void onTransferInventory(IInventory target, BlockData data,
			EntityPlayer player, World world, int x, int y, int z, int side,
			float hitX, float hitY, float hitZ) {
		if(data.tileData == null || data.tileData.hasNoTags())
			return;
		List<IDrawer> drawers=getDrawers(data);
		if(drawers == null || drawers.isEmpty())
			return;
		for(IDrawer drawer : drawers){
			while(drawer.getStoredItemCount() > 0){
				ItemStack stack = drawer.getStoredItemCopy();
				if(stack == null || stack.stackSize < 1)
					break;
				if(!drawer.canItemBeExtracted(stack))
					break;
				int orig = stack.stackSize;
				InventoryMerger.mergeStackIntoInventory(stack, target, side);
				int dif = orig-stack.stackSize;
				if(dif == 0)
					break;
				drawer.setStoredItemCount(drawer.getStoredItemCount()-dif);
			}
		}
		setDrawers(drawers, data);
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
		List<IDrawer> drawers=getDrawers(data);
		if(drawers == null || drawers.isEmpty())
			return false;
		for(IDrawer drawer : drawers){
			if(drawer.getStoredItemCount() > 0)
				return true;
		}
		return false;
	}
	
	private List<IDrawer> getDrawers(BlockData data){
		if(data.block instanceof BlockCompDrawers)
			return getCompDrawers(data);
		int capacity = data.tileData.getInteger("Cap");
		EnumSet<LockAttribute> lockAttributes = null;
        if (data.tileData.hasKey("Lock"))
            lockAttributes = LockAttribute.getEnumSet(data.tileData.getByte("Lock"));
        DummyStorageProvider provider = new DummyStorageProvider(capacity, lockAttributes);
		
		NBTTagList slots = data.tileData.getTagList("Slots", Constants.NBT.TAG_COMPOUND);
		List<IDrawer> drawers = new ArrayList<IDrawer>();
		for (int i = 0, n = slots.tagCount(); i < n; i++) {
            NBTTagCompound slot = slots.getCompoundTagAt(i);
            IDrawer drawer = createDrawer(provider, i);
            drawer.readFromNBT(slot);
            drawers.add(drawer);
        }
		return drawers;
	}
	private List<IDrawer> getCompDrawers(BlockData data){
		int capacity = data.tileData.getInteger("Cap");
		EnumSet<LockAttribute> lockAttributes = null;
        if (data.tileData.hasKey("Lock"))
            lockAttributes = LockAttribute.getEnumSet(data.tileData.getByte("Lock"));
        DummyCentralInventory centralInventory = new DummyCentralInventory(new ItemStack[3], 0, new int[3], lockAttributes, capacity);
        
        NBTTagList slots = data.tileData.getTagList("Slots", Constants.NBT.TAG_COMPOUND);
		List<IDrawer> drawers = new ArrayList<IDrawer>();
		for (int i = 0, n = slots.tagCount(); i < n; i++) {
            NBTTagCompound slot = slots.getCompoundTagAt(i);
            centralInventory.getDrawers()[i] = createDrawer(centralInventory, i);
            centralInventory.getDrawers()[i].readFromNBT(slot);
            drawers.add(centralInventory.getDrawers()[i]);
        }
		centralInventory.pooledCount = data.tileData.getInteger("Count");

        if (data.tileData.hasKey("Conv0"))
        	centralInventory.getConvRates()[0] = data.tileData.getByte("Conv0");
        if (data.tileData.hasKey("Conv1"))
        	centralInventory.getConvRates()[1] = data.tileData.getByte("Conv1");
        if (data.tileData.hasKey("Conv2"))
        	centralInventory.getConvRates()[2] = data.tileData.getByte("Conv2");

        for (int i = 0; i < drawers.size(); i++) {
            IDrawer drawer = drawers.get(i);
            if (drawer instanceof CompDrawerData)
                ((CompDrawerData) drawer).refresh();
        }

		return drawers;
	}
	private IDrawer createDrawer(DummyCentralInventory centralInventory, int slot){
		return new CompDrawerDataWrapper(centralInventory, slot);
	}
	private IDrawer createDrawer(DummyStorageProvider provider, int slot){
		return new DrawerData(provider, slot);
	}
	
	private void setDrawers(List<IDrawer> drawers, BlockData data){
		if(data.block instanceof BlockCompDrawers){
			setCompDrawers(drawers, data);
			return;
		}
		NBTTagList slots = new NBTTagList();
		for(IDrawer drawer : drawers){
			NBTTagCompound nbt = new NBTTagCompound();
			drawer.writeToNBT(nbt);
			slots.appendTag(nbt);
		}
		data.tileData.setTag("Slots", slots);
	}
	private void setCompDrawers(List<IDrawer> drawers, BlockData data){
		NBTTagList slots = new NBTTagList();
		for(IDrawer drawer : drawers){
			NBTTagCompound nbt = new NBTTagCompound();
			drawer.writeToNBT(nbt);
			slots.appendTag(nbt);
		}
		data.tileData.setTag("Slots", slots);
		DummyCentralInventory centralInventory = ((CompDrawerDataWrapper) drawers.get(0)).centralInventoryDummy;
		
		data.tileData.setInteger("Count", centralInventory.pooledCount);

        if (data.tileData.hasKey("Conv0") || centralInventory.getConvRates()[0] > 0)
        	data.tileData.setByte("Conv0", (byte)centralInventory.getConvRates()[0]);
        if (data.tileData.hasKey("Conv1") || centralInventory.getConvRates()[1] > 0)
        	data.tileData.setByte("Conv1", (byte)centralInventory.getConvRates()[1]);
        if (data.tileData.hasKey("Conv1") || centralInventory.getConvRates()[2] > 0)
        	data.tileData.setByte("Conv2", (byte)centralInventory.getConvRates()[2]);
	}

}
