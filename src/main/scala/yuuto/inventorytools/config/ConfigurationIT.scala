/**
 * @author Yuuto
 */
package yuuto.inventorytools.config

import cpw.mods.fml.common.event.FMLPostInitializationEvent
import cpw.mods.fml.common.event.FMLPreInitializationEvent
import net.minecraft.item.{ItemStack, Item}
import net.minecraft.potion.PotionEffect
import net.minecraftforge.common.config.Configuration
import yuuto.inventorytools.api.dolly.DollyHandlerRegistry
import yuuto.inventorytools.api.toolbox.ToolBoxRegistry
import yuuto.inventorytools.until.LogHelperIT

object ConfigurationIT {
  var advEffects=true;
  var dollyEffects:Array[PotionEffect]=null;
  var dollyEffectsList:Array[String]=null;
  var maxTools:Int=18;
  var invertBlock:Boolean=false;
  var invertTool:Boolean=false;
  var invertMods:Boolean=false;
  var blockListAll:Array[String]=null;
  var blockListNormal:Array[String]=null;
  var blockListModAll:Array[String]=null;
  var blockListModNormal:Array[String]=null;
  var toolWhiteList:Array[String]=null;
  var toolBlackList:Array[String]=null;
  
  def preInit(event:FMLPreInitializationEvent){
    val config=new Configuration(event.getSuggestedConfigurationFile());
    config.load();
    LogHelperIT.debug= config.getBoolean("DebugLogs", "Debug", false, "Should debug lines be output to the console")
    maxTools = config.getInt("MaxTools", "ToolBox", maxTools, 1, 18, "The number of tools that can be stored in a toolbox");
    invertTool=config.getBoolean("WhiteListOnly", "ToolBox", false, "Should the tool box only accept White Listed items?");
    toolWhiteList=config.getStringList("WhiteList", "ToolBox", Array("LogisticsPipes:item.remoteOrdererItem", "LogisticsPipes:item.pipeController", "Railcraft:tool.whistle.tuner", "Enchiridion:items", "Steamcraft:wrench"), "A list of items that can be put in a toolbox as tools Formats: ItemName, ModName:ItemName, ModName:ItemName:Meta");
    toolBlackList=config.getStringList("BlackList", "ToolBox", Array(), "A list of items that CANNOT be put in a toolbox as tools Formats: ItemName, ModName:ItemName, ModName:ItemName:Meta");
    invertBlock =config.getBoolean("InvertBlockBlackList", "Dolly", false, "Use use the black list as a white list, only items listed will be picked up by the dolly");
    blockListAll=config.getStringList("BlackListAll", "Dolly", Array(), "A list of blocks that cannot be picked up by any dolly \n [Inverted]A list of blocks that can be picked up by any dolly \n Formats: ItemName, ModName:ItemName, ModName:ItemName:Meta");
    blockListNormal=config.getStringList("BlackListNormal", "Dolly", Array("mob_spawner"), "A lits of blocks that cannot be picked up by normal dollies \n [Inverted]A list of blocks that can only be picked up by Advanced Dollies \n Formats: ItemName, ModName:ItemName, ModName:ItemName:Meta");
    invertBlock =config.getBoolean("InvertModBlackList", "Dolly", false, "Use use the black list as a white list, only mods listed will be picked up by the dolly unless otherwise white listed");
    blockListModAll=config.getStringList("BlackListModsAll", "Dolly", Array(), "A list of mods who's blocks cannot be picked up by any dolly unless white listed \n [Inverted]A list of mods who's blocks can be picked up by any dolly unless black listed");
    blockListModNormal=config.getStringList("BlackListModsNormal", "Dolly", Array(), "A list of mods who's blocks cannot be picked up by any normal dollies unless white listed \n [Inverted]A list of mods who's blocks can be picked up by Advanced Dollies unless black listed");;
    advEffects=config.getBoolean("AdvancedDollyEffects", "Dolly", true, "Should the advanced dolly give side effects while holding cargo?");
    dollyEffectsList=config.getStringList("DollyPotionEffects", "Dolly", Array("2:2:200", "4:2:200", "18:2:200"), "What potion effects(by ID) should a full dolly add to the player? \n Format: PotionID:Strength:Durration");
    config.save();
  }
  def postInit(event:FMLPostInitializationEvent){
    if(dollyEffectsList==null){
      dollyEffects=new Array[PotionEffect](0);
    }else{
      dollyEffects = new Array[PotionEffect](dollyEffectsList.length);
      for(i<-0 until dollyEffectsList.length){
        val parts:Array[String] = dollyEffectsList(i).split(':');
        val potion:Int = parts(0).toInt;
        var str:Int = 2;
        var dur:Int = 600;
        if(parts.length > 1)
          str = parts(1).toInt;
        if(parts.length > 2)
          dur = parts(2).toInt;
        dollyEffects(i) = new PotionEffect(potion, dur, str);
      }
      dollyEffectsList = null;
    }
    if(invertBlock)
      DollyHandlerRegistry.InvertBlocks();
    if(invertMods)
      DollyHandlerRegistry.InvertMods();
    if(blockListAll != null){
      LogHelperIT.Debug("Loaded Dolly Black List with "+blockListAll.length+" entries");
      for(s<-blockListAll){
        DollyHandlerRegistry.addToBlackListAll(s);
      }
      blockListAll=null;
    }
    if(blockListNormal != null){
      LogHelperIT.Debug("Loaded Gold Dolly Black List with "+blockListNormal.length+" entries");
      for(s<-blockListNormal){
        DollyHandlerRegistry.addToBlackListNormal(s);
      }
      blockListNormal=null;
    }
    if(blockListModAll != null){
      for(s<-blockListModAll){
        DollyHandlerRegistry.addToBlackListModsAll(s);
      }
      blockListModAll = null;
    }
    if(blockListModNormal != null){
      for(s<-blockListModNormal){
        DollyHandlerRegistry.addToBlackListModsNormal(s);
      }
      blockListModNormal = null;
    }
    if(invertTool)
      ToolBoxRegistry.setWhiteListOnly();
    if(toolWhiteList != null){
      LogHelperIT.Debug("Loaded Tool White List with "+toolWhiteList.length+" entries");
      for(s<-toolWhiteList){
        ToolBoxRegistry.addToWhiteList(s);
      }
      toolWhiteList=null;
    }
    if(!invertTool && toolBlackList != null){
      LogHelperIT.Debug("Loaded Tool Black List with "+toolBlackList.length+" entries");
      for(s<-toolBlackList){
        ToolBoxRegistry.addToBlackList(s);
      }
      toolBlackList=null;
    }
  }
}