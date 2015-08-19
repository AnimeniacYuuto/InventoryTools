/**
 * @author Yuuto
 */
package yuuto.inventorytools.config

import cpw.mods.fml.common.event.FMLPostInitializationEvent
import cpw.mods.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.common.config.Configuration
import yuuto.inventorytools.api.dolly.DollyHandlerRegistry
import yuuto.inventorytools.api.toolbox.ToolBoxRegistry

object ConfigurationIT {
  var maxTools:Int=18;
  var blockListAll:Array[String]=null;
  var blockListNormal:Array[String]=null;
  var toolWhiteList:Array[String]=null;
  var toolBlackList:Array[String]=null;
  
  def preInit(event:FMLPreInitializationEvent){
    val config=new Configuration(event.getSuggestedConfigurationFile());
    config.load();
    maxTools = config.getInt("MaxTools", "ToolBox", maxTools, 1, 18, "The number of tools that can be stored in a toolbox");
    toolWhiteList=config.getStringList("WhiteList", "ToolBox", Array(), "A list of items that can be put in a toolbox as tools Formats: ItemName, ModName:ItemName, ModName:ItemName:Meta");
    toolBlackList=config.getStringList("BlackList", "ToolBox", Array(), "A list of items that CANNOT be put in a toolbox as tools Formats: ItemName, ModName:ItemName, ModName:ItemName:Meta");
    blockListAll=config.getStringList("BlackListAll", "Dolly", Array(), "A list of blocks that cannot be picked up by any dolly Formats: ItemName, ModName:ItemName, ModName:ItemName:Meta");
    blockListNormal=config.getStringList("BlackListNormal", "Dolly", Array("mob_spawner"), "A lits of blocks that cannot be picked up by normal dollies Formats: ItemName, ModName:ItemName, ModName:ItemName:Meta");
    config.save();
  }
  def postInit(event:FMLPostInitializationEvent){
    if(blockListAll != null){
      for(s<-blockListAll){
        DollyHandlerRegistry.addToBlackListAll(s);
      }
      blockListAll=null;
    }
    if(blockListNormal != null){
      for(s<-blockListNormal){
        DollyHandlerRegistry.addToBlackListNormal(s);
      }
      blockListNormal=null;
    }
    if(toolWhiteList != null){
      for(s<-toolWhiteList){
        ToolBoxRegistry.addToBlackList(s);
      }
      toolWhiteList=null;
    }
    if(toolBlackList != null){
      for(s<-toolBlackList){
        ToolBoxRegistry.addToWhiteList(s);
      }
      toolBlackList=null;
    }
  }
}