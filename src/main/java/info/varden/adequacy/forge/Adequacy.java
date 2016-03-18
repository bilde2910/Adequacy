package info.varden.adequacy.forge;

import net.minecraftforge.fml.common.Mod;

/**
 * Adequacy mod file for Forge.
 */
@Mod(modid = Adequacy.MODID, version = Adequacy.VERSION, name = Adequacy.NAME)
public class Adequacy {
    public static final String MODID = "adequacy";
    public static final String VERSION = "1.0";
    public static final String NAME = "Adequacy";
    public static final String MCVERSION = "1.9";
    
    /**
     * Logs information to the console.
     * @param entry The line to be output.
     */
    protected static void logInfo(String entry) {
    	System.out.println("[Adequacy] " + entry);
    }
}
