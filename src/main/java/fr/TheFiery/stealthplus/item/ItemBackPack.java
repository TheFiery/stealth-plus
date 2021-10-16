package fr.TheFiery.stealthplus.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.world.World;

import java.util.List;

import cpw.mods.fml.common.registry.GameRegistry;
import fr.TheFiery.stealthplus.StealthplMod;

/**
 * @author SCAREX
 * 
 */
public class ItemBackPack extends Item
{
	public static final String NAME = "backpack";

	public ItemBackPack() {
		this.setUnlocalizedName(StealthplMod.MODID + "_" + NAME);
		this.setTextureName(StealthplMod.MODID + ":" + NAME);
		this.setCreativeTab(CreativeTabs.tabTools);
		this.maxStackSize = 1;

		this.register();
	}

	/**
	 * Used to add the item to the game registry
	 */
	public final void register() {
		GameRegistry.registerItem(this, NAME);
	}

	/**
	 * Used to open the gui
	 */
	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		player.openGui(StealthplMod.INSTANCE, 0, world, (int) player.posX, (int) player.posY, (int) player.posZ);
		return stack;
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean advanced) {
		if (stack.getTagCompound() != null && stack.getTagCompound().hasKey("Color")) list.add("Color : " + stack.getTagCompound().getInteger("Color"));
	}
}
