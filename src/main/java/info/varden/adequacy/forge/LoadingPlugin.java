package info.varden.adequacy.forge;

import info.varden.adequacy.ClassUtils;
import info.varden.adequacy.gui.InadequacyGui;

import java.awt.GraphicsEnvironment;
import java.lang.reflect.Method;
import java.util.Map;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

/**
 * Coremod file for Adequacy.
 */
public class LoadingPlugin implements IFMLLoadingPlugin {

	/**
	 * Checks whether the game will try to load mods which require an newer version of Java.
	 */
	@Override
	public String[] getASMTransformerClass() {
		if (ObsolescenceCheck.check()) {
			System.err.println("\n\n@#@#@ OUTDATED JAVA RUNTIME!! @#@#@\n"
					+ "One or more mods require a Java runtime newer than the one installed! Please update Java before re-launching.\n"
					+ "Required version: " + ClassUtils.getLastNewTestedVersion());
			
			if (GraphicsEnvironment.isHeadless()) {
				Method m;
				try {
					m = Class.forName("java.lang.Shutdown").getDeclaredMethod("exit", int.class);
					m.setAccessible(true);
					m.invoke(null, -286);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				InadequacyGui g = new InadequacyGui() {
					/**
					 * Forcibly shuts down the game.
					 */
					@Override
					public void shutdownGame() {
						// I don't care what you think about this, because the game will crash if this isn't done.
						// Brutal, I know, but what has to be done, has to be done.
						Method m;
						try {
							m = Class.forName("java.lang.Shutdown").getDeclaredMethod("exit", int.class);
							m.setAccessible(true);
							m.invoke(null, -286);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				};
			}
		}
		return new String[0];
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
	public void injectData(Map<String, Object> data) {
	}

	@Override
	public String getAccessTransformerClass() {
		return null;
	}

}
