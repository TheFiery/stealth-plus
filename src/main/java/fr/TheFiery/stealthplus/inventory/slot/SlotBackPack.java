package fr.TheFiery.stealthplus.inventory.slot;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import fr.TheFiery.stealthplus.item.ItemBackPack;

/**
 * @author SCAREX
 * 
 */
public class SlotBackPack extends Slot
{
	public SlotBackPack(IInventory inv, int index, int x, int y) {
		super(inv, index, x, y);
	}

	/**
	 * Method used to prevent backpack-ception (backpacks inside backpacks)
	 */
	@Override
	public boolean isItemValid(ItemStack stack) {
		return !(stack.getItem() instanceof ItemBackPack);
	}
}
