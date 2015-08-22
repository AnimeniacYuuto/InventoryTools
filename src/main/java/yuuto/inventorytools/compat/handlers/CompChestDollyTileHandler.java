package yuuto.inventorytools.compat.handlers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import yuuto.inventorytools.api.dolly.BlockData;
import yuuto.inventorytools.api.dolly.RotationUtils;
import yuuto.inventorytools.api.dolly.handlers.ChestDollyTileHandler;

public class CompChestDollyTileHandler extends ChestDollyTileHandler{

	protected static CompChestDollyTileHandler instanceCompChest=null;
	public static final CompChestDollyTileHandler getInstanceCompChest(){
		if(instanceCompChest==null)
			instanceCompChest=new CompChestDollyTileHandler();
		return instanceCompChest;
	}
	
	private CompChestDollyTileHandler(){}
	
	@Override
	public String getID() {
		// TODO Auto-generated method stub
		return "tilehandler.compactstorage.compactchest";
	}
	
	@Override
	public void onPlaced(TileEntity tile, BlockData data, EntityPlayer player,
			World world, int x, int y, int z, int side, float hitX, float hitY,
			float hitZ) {
		if(data.tileData == null || data.tileData.hasNoTags())
			return;
		data.tileData.setInteger("x", x);
		data.tileData.setInteger("y", y);
		data.tileData.setInteger("z", z);
		ForgeDirection dir = RotationUtils.getOrientationOnPlacement(player);
		data.tileData.setInteger("facing", dir.getOpposite().ordinal());
		tile.readFromNBT(data.tileData);		
	}
}
