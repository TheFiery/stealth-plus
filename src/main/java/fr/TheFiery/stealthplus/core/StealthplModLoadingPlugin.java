package fr.TheFiery.stealthplus.core;

import java.util.List;
import java.util.Map;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import fr.TheFiery.stealthplus.StealthplMod;

/**
 * @author SCAREX
 *
 */
@IFMLLoadingPlugin.MCVersion("1.7.10")
public class StealthplModLoadingPlugin implements IFMLLoadingPlugin
{
	@Override
	public String[] getASMTransformerClass() {
		return new String[] {
				StealthplModClassTransformer.class.getName() };
	}

	@Override
	public String getModContainerClass() {
		return null;
	}

	@Override
	public String getSetupClass() {
		return null;
	}

	@Override
	public void injectData(Map<String, Object> data) {}

	@Override
	public String getAccessTransformerClass() {
		return null;
	}
}
