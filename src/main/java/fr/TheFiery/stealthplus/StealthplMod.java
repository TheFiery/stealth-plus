package fr.TheFiery.stealthplus;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import fr.TheFiery.stealthplus.block.StealthplModBlocks;
import fr.TheFiery.stealthplus.handler.BlockCanFallHandler;
import fr.TheFiery.stealthplus.item.StealthplModItems;
import fr.TheFiery.stealthplus.neat.HealthBarRenderer;
import fr.TheFiery.stealthplus.neat.NeatConfig;
import net.minecraftforge.common.MinecraftForge;

/**
 * @author SCAREX
 * 
 */
@Mod(modid = StealthplMod.MODID, name = StealthplMod.NAME, version = StealthplMod.VERSION)
public class StealthplMod
{
	public static final String MODID = "StealthplMod";
	public static final String NAME = "TheFiery_ Stealth+";
	public static final String VERSION = "1.0.4";
	/**
	 * Logger used for debug
	 */
	public static final Logger LOGGER = LogManager.getLogger(NAME);

	@Mod.Instance(MODID)
	public static StealthplMod INSTANCE;

	@SidedProxy(clientSide = "fr.TheFiery.stealthplus.client.ClientProxy", serverSide = "fr.TheFiery.stealthplus.CommonProxy")
	public static CommonProxy PROXY;

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		StealthplModItems.registerItems();
		StealthplModBlocks.registerBlocks();
		NeatConfig.init(event.getSuggestedConfigurationFile());
	    MinecraftForge.EVENT_BUS.register(new HealthBarRenderer());

		PROXY.register();
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		StealthplModItems.registerItemsCrafts();
		StealthplModBlocks.registerBlocksCrafts();

		// Registering the gui handler
		NetworkRegistry.INSTANCE.registerGuiHandler(INSTANCE, PROXY);
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		multiregister(new BlockCanFallHandler());
	}

	public static void multiregister(Object o) {
		MinecraftForge.EVENT_BUS.register(o);
	}
	
	
}
