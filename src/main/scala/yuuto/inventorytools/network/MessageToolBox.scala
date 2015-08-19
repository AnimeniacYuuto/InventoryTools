/**
 * @author Yuuto
 */
package yuuto.inventorytools.network

import io.netty.buffer.ByteBuf
import cpw.mods.fml.common.network.simpleimpl.IMessage

class MessageToolBox(var id:Int) extends IMessage {
    
    def this()=this(0);
  
    override def fromBytes(buf:ByteBuf) {
        id = buf.readInt();
    }

    override def toBytes(buf:ByteBuf) {
        buf.writeInt(id);
    }
}