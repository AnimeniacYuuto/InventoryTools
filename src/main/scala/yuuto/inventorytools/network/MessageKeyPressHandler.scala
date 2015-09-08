package yuuto.inventorytools.network

import cpw.mods.fml.common.network.simpleimpl.{MessageContext, IMessage, IMessageHandler}
import net.minecraft.item.ItemStack
import yuuto.inventorytools.items.ItemDolly
import yuuto.inventorytools.proxy.ProxyCommon
import yuuto.inventorytools.until.ITKeyBinds

/**
 * Created by Yuuto on 9/8/2015.
 */
class MessageKeyPressHandler  extends IMessageHandler[MessageKeyPress, IMessage] {

  override def onMessage(message:MessageKeyPress, ctx:MessageContext):IMessage={
    val key:ITKeyBinds=ITKeyBinds.values()(message.id);
    key match {
      case ITKeyBinds.DOLLY_MODE=>switchDollyMode(ctx);
      case k=>;
    }
    null;
  }
  def switchDollyMode(ctx:MessageContext){
    val item:ItemStack=ctx.getServerHandler.playerEntity.inventory.getCurrentItem;
    if(item == null)
      return;
    if(item.getItem!= ProxyCommon.itemDolly && item.getItem!= ProxyCommon.itemDollyAdvanced)
      return;
    if(item.getItemDamage != 1 && item.getItemDamage != 2)
      return;
    item.getItem.asInstanceOf[ItemDolly].switchMode(item, ctx.getServerHandler.playerEntity, ctx.getServerHandler.playerEntity.getEntityWorld);
  }
}
