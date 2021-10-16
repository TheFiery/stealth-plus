package fr.TheFiery.stealthplus.core;

import net.minecraft.world.World;

/**
 * @author SCAREX
 *
 */
public interface ICanFall
{
	public boolean canBlockFall(World world, int x, int y, int z);
}
