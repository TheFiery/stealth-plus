package fr.TheFiery.stealthplus.recipe;

import fr.TheFiery.stealthplus.item.StealthplModItems;
import net.minecraft.block.BlockColored;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

/**
 * @author SCAREX
 *
 */
public class BackpackRecipe implements IRecipe
{
	/**
	 * The craft matrix
	 */
	public static final Item[][] matrix = new Item[][] {
			new Item[] { Items.paper,
					Items.paper,
					Items.paper },
			new Item[] { Items.paper,
					Items.dye,
					Items.paper },
			new Item[] { Items.paper,
					Items.paper,
					Items.paper } };

	/**
	 * This method is used to check if the matrix corresponds to your craft
	 * 
	 * @param inv
	 *            The crafting table's inventory
	 * @param world
	 *            The world
	 * @return true if the matrix corresponds
	 */
	@Override
	public boolean matches(InventoryCrafting inv, World world) {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (!BackpackRecipe.corresponds(BackpackRecipe.matrix[i][j], inv.getStackInSlot(i * 3 + j))) return false;
			}
		}
		return true;
	}

	/**
	 * This method is used to get the result from the craft relatively to the
	 * given inventory
	 * 
	 * @param inv
	 *            The crafting table's inventory
	 * @return The result from the craft
	 */
	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv) {
		ItemStack stack = new ItemStack(StealthplModItems.ITEM_BACKPACK);
		if (!stack.hasTagCompound()) stack.setTagCompound(new NBTTagCompound());
		stack.getTagCompound().setInteger("Color", BlockColored.func_150032_b(inv.getStackInSlot(4).getItemDamage()));
		return stack;
	}

	/**
	 * This method should return the size used for the craft
	 * 
	 * @return The requested size
	 */
	@Override
	public int getRecipeSize() {
		return 9;
	}

	/**
	 * @return An ItemStack to identify the craft
	 */
	@Override
	public ItemStack getRecipeOutput() {
		return new ItemStack(StealthplModItems.ITEM_BACKPACK);
	}

	/**
	 * @param item
	 *            The item from your matrix
	 * @param stack
	 *            The ItemStack from the crafting inventory
	 * @return true if the 2 objects correspond
	 */
	public static boolean corresponds(Item item, ItemStack stack) {
		if (stack == null && item == null) return true;
		if (stack == null && item != null) return false;
		if (stack != null && item == null) return false;
		if (stack.getItem() != item) return false;
		return true;
	}
}
