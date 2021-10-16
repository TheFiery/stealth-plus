package fr.TheFiery.stealthplus.item;

import cpw.mods.fml.common.registry.GameRegistry;
import fr.TheFiery.stealthplus.recipe.BackpackRecipe;
import net.minecraft.item.Item;

/**
 * @author SCAREX
 * 
 */
public class StealthplModItems
{
	public static Item ITEM_BACKPACK;

	public static final void registerItems() {
		ITEM_BACKPACK = new ItemBackPack();
	}

	public static final void registerItemsCrafts() {
		GameRegistry.addRecipe(new BackpackRecipe());
	}
}
