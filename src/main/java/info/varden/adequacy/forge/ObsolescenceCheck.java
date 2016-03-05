package info.varden.adequacy.forge;

import info.varden.adequacy.ClassUtils;

import java.io.DataInputStream;
import java.io.File;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import net.minecraftforge.fml.relauncher.CoreModManager;

/**
 * Checks for Java obsolesence.
 */
public final class ObsolescenceCheck {
	private static boolean checked = false;
	private static boolean result = false;
	
	/**
	 * Check whether any of the installed mods require a Java version more recent than the one installed.
	 * @return True if Java is outdated, false otherwise
	 */
	public static boolean check() {
		if (!checked) {
			checked = true;
			try {
				// The Minecraft directory is stored in the CoreModManager which is initialized on startup,
				// but the field is private, so we must fetch its value via reflection
				Field f = CoreModManager.class.getDeclaredField("mcDir");
				f.setAccessible(true);
				File mcDir = (File) f.get(null);
				File dir1 = new File(mcDir, "mods");
				File dir2 = new File(dir1, Adequacy.MCVERSION);
				FilenameFilter ff = new FilenameFilter() {

					@Override
					public boolean accept(File dir, String name) {
						return name.endsWith(".jar") || name.endsWith(".jar.zip") || name.endsWith(".litemod");
					}
					
				};
				if (dir1.exists()) {
					Adequacy.logInfo("Checking for mods in " + dir1.getAbsolutePath());
					File[] modFiles1 = dir1.listFiles(ff);
					for (File mod : modFiles1) {
						if (check(mod)) {
							result = true;
							return result;
						}
					}
					if (dir2.exists()) {
						Adequacy.logInfo("Checking for mods in " + dir2.getAbsolutePath());
						File[] modFiles2 = dir2.listFiles(ff);
						for (File mod : modFiles2) {
							if (check(mod)) {
								result = true;
								return result;
							}
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	/**
	 * Checks whether the given jar file contains any class files requiring a newer version of Java.
	 * @param modArchive A reference to the mod's location in the file system
	 * @return True if Java is outdated, false otherwise
	 */
	private static boolean check(File modArchive) {
		try {
			ZipFile zf = new ZipFile(modArchive);
			Enumeration<? extends ZipEntry> entries = zf.entries();
			while (entries.hasMoreElements()) {
				ZipEntry e = entries.nextElement();
				if (e.getName().toLowerCase().endsWith(".class")) {
					InputStream is = zf.getInputStream(e);
					DataInputStream dis = new DataInputStream(is);
					if (dis.readInt() == ClassUtils.CLASS_MAGIC) {
						int minor = dis.readUnsignedShort();
						int major = dis.readUnsignedShort();
						dis.close();
						if (!ClassUtils.checkVersionCompatibility(major, minor)) {
							zf.close();
							return true;
						}
					}
				}
			}
			zf.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Not instantiable
	 */
	private ObsolescenceCheck() {}
}
