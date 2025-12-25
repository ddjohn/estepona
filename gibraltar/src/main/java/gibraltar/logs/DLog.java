package gibraltar.logs;

public class DLog {
	public static String forTag(Class<?> clazz) {
		return "ADB." + clazz.getSimpleName();
	}
	
	public static void method(String tag, Object msg) {
		System.err.println(" D " + tag + ":" + msg);
	}
	
	public static void info(String tag, Object msg) {
		System.err.println(" I " + tag + ":" + msg);
	}

	public static void error(String tag, Object msg, Exception e) {
		System.err.println(" E " + tag + ":" + msg);
		e.printStackTrace();
	}
}
