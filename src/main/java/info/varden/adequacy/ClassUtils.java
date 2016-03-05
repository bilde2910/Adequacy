package info.varden.adequacy;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Random;

import javax.xml.bind.DatatypeConverter;

/**
 * Utilities for class manipulation.
 */
public final class ClassUtils {
	/**
	 * Highest major version of class files known to work in this environment.
	 */
	private static int highestMajor = Integer.MIN_VALUE;
	/**
	 * Highest minor version of class files known to work in this environment.
	 */
	private static int highestMinor = Integer.MIN_VALUE;
	/**
	 * Last new tested major class file version.
	 */
	private static int lastMajor = Integer.MIN_VALUE;
	/**
	 * Last new tested minor class file version.
	 */
	private static int lastMinor = Integer.MIN_VALUE;
	/**
	 * Local instance of the byte array class loader.
	 */
	private static final ByteArrayClassLoader BACL = new ByteArrayClassLoader();
	/**
	 * Class file magic header.
	 */
	public static final int CLASS_MAGIC = 0xCAFEBABE;
	
	/**
	 * Generates a dummy class file for the specified version of Java.
	 * @param name The name of the class file
	 * @param major The major version of the class file
	 * @param minor The minor version of the class file
	 * @return A byte array containing the binary class file
	 */
	public static byte[] generateDummyClassForVersion(String name, int major, int minor) {
		String javaName = name + ".java";
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		try {
			// public class <NAME> {}
			
			// Magic header
			dos.write(DatatypeConverter.parseHexBinary(
			
					  "CAFEBABE"
			));
			dos.writeShort(minor);
			dos.writeShort(major);
			dos.write(DatatypeConverter.parseHexBinary(
			
					  "000D0A0003000A07000B07000C010006"
					+ "3C696E69743E01000328295601000443"
					+ "6F646501000F4C696E654E756D626572"
					+ "5461626C6501000A536F757263654669"
					+ "6C650100"
			));
			dos.write(new byte[] {(byte) javaName.length()});
			dos.write(javaName.getBytes());
			dos.write(DatatypeConverter.parseHexBinary(
			
					  "0C000400050100"
			));
			dos.write(new byte[] {(byte) name.length()});
			dos.write(name.getBytes());
			dos.write(DatatypeConverter.parseHexBinary(
			
					  "0100106A6176612F6C616E672F4F626A"
					+ "65637400210002000300000000000100"
					+ "0100040005000100060000001D000100"
					+ "01000000052AB70001B1000000010007"
					+ "00000006000100000001000100080000"
					+ "00020009"
			));
			dos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		byte[] classData = baos.toByteArray();
		try {
			dos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return classData;
	}
	
	/**
	 * Checks whether the current Java runtime is capable of loading class files of a given version by
	 * generating a class file of that version and then trying to define it in the class loader.
	 * @param major The major version of the class file
	 * @param minor The minor version of the class file
	 * @return True if loading is supported, false otherwise
	 */
	public static boolean checkVersionCompatibility(int major, int minor) {
		// There's no point in checking class files of older versions than the latest version confirmed
		// to work because Java is backwards compatible with old class files.
		if (major < highestMajor || (major == highestMajor && minor <= highestMinor)) {
			return true;
		}
		lastMajor = major;
		lastMinor = minor;
		// Generate a dummy name for the class file
		Random random = new Random();
		String className = "C" + Math.abs(major) + "V" + Math.abs(minor) + "R" + random.nextInt(32768);
		byte[] classData = generateDummyClassForVersion(className, major, minor);
		try {
			// If this succeeds, the version is supported
			BACL.findClass(className, classData);
		} catch (UnsupportedClassVersionError ex) {
			// Not supported by the class loader
			return false;
		}
		highestMajor = major;
		highestMinor = minor;
		return true;
	}
	
	/**
	 * Gets the highest class file version known to work with this runtime.
	 * @return Class file version as a String
	 */
	public static String getHighestConfirmedVersion() {
		return highestMajor + "." + highestMinor;
	}
	
	/**
	 * Gets the last new version tested by {@link ClassUtils}.
	 * @return Class file version as a String
	 */
	public static String getLastNewTestedVersion() {
		return lastMajor + "." + lastMinor;
	}
	
	/**
	 * Custom class loader that defines classes from their binary representation in memory
	 */
	private static class ByteArrayClassLoader extends ClassLoader {
		/**
		 * Attempts to define the given class data.
		 * @param name The name of the class
		 * @param clazz The class' binary data
		 * @return A {@link Class} representation
		 * @throws UnsupportedClassVersionError Thrown if the Java runtime does not
		 * support the version of the class file
		 */
		public Class<?> findClass(String name, byte[] clazz) throws UnsupportedClassVersionError {
			try {
				return defineClass(name, clazz, 0, clazz.length);
			} catch (Exception ex) {
				ex.printStackTrace();
				throw new UnsupportedClassVersionError();
			}
		}
	}
	
	/**
	 * Not instantiable
	 */
	private ClassUtils() {}
}
