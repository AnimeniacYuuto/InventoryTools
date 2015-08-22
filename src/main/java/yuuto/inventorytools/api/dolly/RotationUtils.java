package yuuto.inventorytools.api.dolly;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Vec3;
import net.minecraftforge.common.util.ForgeDirection;

public class RotationUtils {
	
	public static ForgeDirection getOrientationOnPlacement(EntityPlayer player){
		return getOrientationOnPlacement(player, 0, false);
	}
	public static ForgeDirection getOrientationOnPlacement(EntityPlayer player, int blockY, boolean allowVertical) {
		ForgeDirection orientation = ForgeDirection.UNKNOWN;
		Vec3 playerLook = player.getLookVec();
		if (Math.abs(playerLook.xCoord) >= Math.abs(playerLook.zCoord)) {
			if (playerLook.xCoord > 0)
				orientation = ForgeDirection.WEST;
			else
				orientation = ForgeDirection.EAST;
		} else {
			if (playerLook.zCoord > 0)
				orientation = ForgeDirection.NORTH;
			else
				orientation = ForgeDirection.SOUTH;
		}
		
		if (allowVertical && player.posY > blockY) {
			orientation = ForgeDirection.UP;
		}

		else if (allowVertical && playerLook.yCoord >  0.73) {
			orientation = ForgeDirection.DOWN;
		}

		return orientation;
	}


}
