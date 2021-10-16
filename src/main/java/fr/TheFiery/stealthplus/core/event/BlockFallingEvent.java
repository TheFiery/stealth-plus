package fr.TheFiery.stealthplus.core.event;

import cpw.mods.fml.common.eventhandler.Cancelable;
import cpw.mods.fml.common.eventhandler.Event;
import net.minecraft.world.World;

/**
 * @author SCAREX
 *
 */
@Cancelable
public class BlockFallingEvent extends Event
{
	public final World world;
	public final int x;
	public final int y;
	public final int z;

	public BlockFallingEvent(World world, int x, int y, int z) {
		super();
		this.world = world;
		this.x = x;
		this.y = y;
		this.z = z;
	}
}
