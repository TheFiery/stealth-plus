package fr.TheFiery.stealthplus.client;

import java.awt.Desktop;
import java.net.URI;

import cpw.mods.fml.common.ObfuscationReflectionHelper;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import fr.TheFiery.stealthplus.CommonProxy;
import fr.TheFiery.stealthplus.StealthplMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreenResourcePacks;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent.ActionPerformedEvent;
import net.minecraftforge.client.event.GuiScreenEvent.InitGuiEvent;
import net.minecraftforge.common.MinecraftForge;

/**
 * @author SCAREX
 * 
 */
public class ClientProxy extends CommonProxy
{
	public void register() {
		// registering the PROXY to use events from MinecraftForge
		MinecraftForge.EVENT_BUS.register(new ClientHandlers());

		StealthplMod.LOGGER.info(Minecraft.getMinecraft().getSystemTime());
		StealthplMod.LOGGER.info(Minecraft.getMinecraft());
		StealthplMod.LOGGER.info(Minecraft.isRunningOnMac);
	}
	
	public static class ClientHandlers
	{
		/**
		 * This function is called when the init method of a GuiScreen is called and
		 * modify the current buttons and the splash text
		 */
		@SubscribeEvent
		public void onInitGuiEvent(InitGuiEvent.Post event) {
			if (event.gui instanceof GuiMainMenu) {
				for (Object b : event.buttonList) {
					if (((GuiButton) b).id == 14) {
						((GuiButton) b).visible = false;
					}
				}
				int i = event.gui.height / 4 + 48;
				event.buttonList.add(new GuiButton(30, event.gui.width / 2 - 100, i + 24 * 2, 100, 20, "Discord") {
					@Override
					public void mouseReleased(int x, int y) {
						if (Desktop.isDesktopSupported()) {
							try {
								Desktop.getDesktop().browse(new URI("ts3server://ts.minecraftforgefrance.fr?port=9987"));
							} catch (Exception e) {
								StealthplMod.LOGGER.warn("Couldn't open teamspeak", e);
							}
						}
					}
				});

				ObfuscationReflectionHelper.setPrivateValue(GuiMainMenu.class, (GuiMainMenu) event.gui, "Merci d'avoir télécharger stealth-plus", "splashText", "field_73975_c");
			}
		}

		/**
		 * This event is called when an action is performed <br />
		 * This method avoid opening the resource packs GUI
		 */
		@SubscribeEvent
		public void onActionPerformed(ActionPerformedEvent.Pre event) {
			if (event.gui instanceof GuiOptions && event.button.id == 105) event.setCanceled(true);
		}

		/**
		 * This event is called when a GUI is opened <br />
		 * This method avoid opening the resource packs GUI
		 */
		@SubscribeEvent
		public void onGuiOpen(GuiOpenEvent event) {
			if (event.gui instanceof GuiScreenResourcePacks) event.setCanceled(true);
		}
	}
}
