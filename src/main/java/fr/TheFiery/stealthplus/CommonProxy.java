package fr.TheFiery.stealthplus;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;
import fr.TheFiery.stealthplus.client.gui.inventory.GuiBackPack;
import fr.TheFiery.stealthplus.inventory.InventoryBackPack;
import fr.TheFiery.stealthplus.inventory.container.ContainerBackPack;
import fr.TheFiery.stealthplus.item.ItemBackPack;

/**
 * @author SCAREX
 * 
 */
public class CommonProxy implements IGuiHandler
{
	public void register() {}
	
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		switch (ID) {
		case 0:
			// The last parameter must be a multiple of 9 (e.g: 9, 18, 27, 54)
			// Condition to check if the player has the right item in hand
			if (player.getHeldItem() == null || !(player.getHeldItem().getItem() instanceof ItemBackPack)) return null;
			return new ContainerBackPack(player.inventory, new InventoryBackPack(player.getHeldItem(), 54));
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		switch (ID) {
		case 0:
			// The last parameter must be a multiple of 9 (e.g: 9, 18, 27, 54)
			// Condition to check if the player has the right item in hand
			if (player.getHeldItem() == null || !(player.getHeldItem().getItem() instanceof ItemBackPack)) return null;
			return new GuiBackPack(player.inventory, new InventoryBackPack(player.getHeldItem(), 54));
		}
		return null;
	}
}
