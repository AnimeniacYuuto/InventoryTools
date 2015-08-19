package yuuto.inventorytools.api.dolly.handlers.defaults.block;

import yuuto.inventorytools.api.dolly.BlockData;
import yuuto.inventorytools.api.dolly.DollyHandlerRegistry;
import yuuto.inventorytools.api.dolly.handlers.defaults.tile.IDollyTileHandler;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class DefaultDollyBlockHandler implements IDollyBlockHandler{

	@Override
	public String getID() {
		// TODO Auto-generated method stub
		return "blockhandler.inventorytools.defualt";
	}

	@Override
	public boolean onPickedUp(BlockData blockData, EntityPlayer player, World world, int x, int y, int z, 
			int side, float hitX, float hitY, float hitZ){
		TileEntity tile=world.getTileEntity(x, y, z);
	    if(tile != null){
	      IDollyTileHandler handler=DollyHandlerRegistry.getTileHandler(tile);
	      if(handler==null)
	        return false;
	      if(!handler.canPickUp(tile, blockData, player, world, x, y, z, side, hitX, hitY, hitZ))
	    	  return false;
	      handler.onPickedUp(tile, blockData, player, world, x, y, z, side, hitX, hitY, hitZ);
	      blockData.handlerName=handler.getID();
	    }
		return true;
	}

	@Override
	public boolean onPlaced(BlockData blockData, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
		ForgeDirection targetSide = ForgeDirection.getOrientation(side);
		int targX=x;
		int targY=y;
		int targZ=z;
		Block targetBlock = world.getBlock(targX, targY, targZ);
		ItemStack blockStack=new ItemStack(blockData.block, 1, blockData.meta);
		if(targetBlock != null && !targetBlock.isReplaceable(world, targX, targY, targZ)){
			if(targetBlock == Blocks.snow)
				targetSide=ForgeDirection.UP;
			if(targetBlock != Blocks.vine && targetBlock != Blocks.tallgrass && targetBlock != Blocks.deadbush){
				targX+=targetSide.offsetX;
				targY+=targetSide.offsetY;
				targZ+=targetSide.offsetZ;
			}
		}
		if(!player.canPlayerEdit(targX, targY, targZ, side, blockStack))
			return false;
		if(targY==255 && blockData.block.getMaterial().isSolid())
			return false;
		if(!(world.canPlaceEntityOnSide(blockData.block, targX, targY, targZ, false, side, player, blockStack)))
			return false;
		IDollyTileHandler handler=DollyHandlerRegistry.getTileHandler(blockData.handlerName);
		blockData.meta=blockData.block.onBlockPlaced(world, targX, targY, targZ, side, hitX, hitY, hitZ, blockData.meta);
		if(!world.setBlock(targX, targY, targZ, blockData.block, blockData.meta, 3))
			return false;
		if(world.getBlock(targX, targY, targZ) != blockData.block)
			return true;
		if(handler!=null){
			TileEntity tile=world.getTileEntity(targX, targY, targZ);
			handler.onPlaced(tile, blockData, player, world, targX, targY, targZ, side, hitX, hitY, hitZ);
		}
		blockData.block.onBlockPlacedBy(world, targX, targY, targZ, player, blockStack);
		blockData.block.onPostBlockPlaced(world, targX, targY, targZ, blockData.meta);
		return true;
	}

}
