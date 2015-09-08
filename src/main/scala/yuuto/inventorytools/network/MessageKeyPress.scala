package yuuto.inventorytools.network

import cpw.mods.fml.common.network.simpleimpl.IMessage
import io.netty.buffer.ByteBuf
import yuuto.inventorytools.until.ITKeyBinds

/**
 * Created by Yuuto on 9/8/2015.
 */
class MessageKeyPress(var id:Int, var dim:Int, var x:Int, var y:Int, var z:Int) extends IMessage{

  def this()=this(0,0,0,0,0);
  def this(key:ITKeyBinds, dim:Int, x:Int, y:Int, z:Int)=this(key.ordinal(), dim, x, y, z);

  override def fromBytes(buf:ByteBuf) {
    id = buf.readInt();
    x = buf.readInt();
    y = buf.readInt();
    z = buf.readInt();
    dim = buf.readInt();
  }

  override def toBytes(buf:ByteBuf) {
    buf.writeInt(id);
    buf.writeInt(x);
    buf.writeInt(y);
    buf.writeInt(z);
    buf.writeInt(dim);
  }
}
