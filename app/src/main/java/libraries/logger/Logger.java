package libraries.logger;

import android.util.Log;

public class Logger {

    public static void trace(String message) {
        Log.i("LOG", message);
    }

    public static void traceHttp(String message) {
        Log.i("LOG_HTTP", message);
    }

    public static void traceM(String message) {
        Log.i("LOG_METHODS", message);
    }
}
