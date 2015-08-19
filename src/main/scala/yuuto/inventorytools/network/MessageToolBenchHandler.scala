/**
 * @author Yuuto
 */
package yuuto.inventorytools.network

import net.minecraftforge.common.DimensionManager
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler
import net.minecraft.tileentity.TileEntity
import cpw.mods.fml.common.network.simpleimpl.IMessage
import yuuto.inventorytools.tiles.TileToolBench
import net.minecraft.world.World
import cpw.mods.fml.common.network.simpleimpl.MessageContext

class MessageToolBenchHandler extends IMessageHandler[MessageToolBench, IMessage] {

    override def onMessage(message:MessageToolBench, ctx:MessageContext):IMessage={
      val mID:Int = message.id;
      val world:World = DimensionManager.getWorld(message.dim);
      val tile:TileEntity = world.getTileEntity(message.x, message.y, message.z);
      if(tile != null && tile.isInstanceOf[TileToolBench]){
        if(mID == 0)
          tile.asInstanceOf[TileToolBench].openToolBox();
        else
          tile.asInstanceOf[TileToolBench].closeToolBox();
      }
      return null;
    }
}