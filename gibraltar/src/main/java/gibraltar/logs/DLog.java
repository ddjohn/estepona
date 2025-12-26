package gibraltar.logs;

public class DLog {
	private static boolean debug = true;
	
	public static String getTimestamp() {
		if(debug)
			return "[" + System.currentTimeMillis() + "]";
		else
			return "";
	}
	
	public static String forTag(Class<?> clazz) {
		return "ADB." + clazz.getSimpleName();
	}
	
	public static void method(String tag, Object msg) {
		if(debug)
			System.err.println(getTimestamp() + " D " + tag + ":" + msg);
	}
	
	public static void debug(String tag, Object msg) {
		if(debug)
			System.err.println(getTimestamp() + " D " + tag + ":" + msg);
	}

	public static void info(String tag, Object msg) {
		System.err.println(getTimestamp() + " I " + tag + ":" + msg);
	}

	public static void error(String tag, Object msg, Exception e) {
		System.err.println(getTimestamp() + " E " + tag + ":" + msg);
		e.printStackTrace();
	}

	public static void init(boolean debug) {
		DLog.debug = debug;
	}
}
