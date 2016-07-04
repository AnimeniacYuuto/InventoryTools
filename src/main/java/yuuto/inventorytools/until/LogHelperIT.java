package yuuto.inventorytools.until;

import org.apache.logging.log4j.Level;

import cpw.mods.fml.common.FMLLog;

public class LogHelperIT {
	public static boolean debug = false;
	
	public static void All(Object obj){
		LogSpecial(Level.ALL, obj);
	}
	public static void Debug(Object obj){
		if(debug) {
			Info(obj);
			LogSpecial(Level.DEBUG, obj);
		}
	}
	public static void Error(Object obj){
		LogSpecial(Level.ERROR, obj);
	}
	public static void Error(Object obj, Throwable t){
		LogSpecial(Level.ERROR, obj, t);
	}
	public static void Fatal(Object obj){
		LogSpecial(Level.FATAL, obj);
	}
	public static void Info(Object obj){
		LogSpecial(Level.INFO, obj);
	}
	public static void Off(Object obj){
		LogSpecial(Level.OFF, obj);
	}
	public static void Trace(Object obj){
		LogSpecial(Level.TRACE, obj);
	}
	public static void Warning(Object obj){
		LogSpecial(Level.WARN, obj);
	}
	public static void LogSpecial(Level logLevel, Object obj){
		FMLLog.log("Inventory Tools", logLevel, String.valueOf(obj));
	}
	public static void LogSpecial(Level logLevel, Object obj, Throwable t){
  		FMLLog.log("Inventory Tools", logLevel, t, String.valueOf(obj));
  	}

}
