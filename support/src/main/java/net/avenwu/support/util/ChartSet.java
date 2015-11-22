package net.avenwu.support.util;

import android.util.Log;

/**
 * Created by chaobin on 11/22/15.
 */
public class ChartSet {

    static final String TAG = ChartSet.class.getCanonicalName();
    final static char ANSI_START = '!';
    final static char ANSI_END = '~';

    final static char UNICODE_START = '！';
    final static char UNICODE_END = '～';

    final static long DIFF = UNICODE_START - ANSI_START;

    public static String convertDBCS(CharSequence text) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            Log.e(TAG, "" + c);
            if (isCharNeedConvert(c)) {
                c = convert(c);
                Log.e(TAG, "translated:" + c);
            }
            builder.append(c);
        }
        return builder.toString();
    }

    public static boolean isCharNeedConvert(char c) {
        return c >= UNICODE_START && c <= UNICODE_END;
    }

    public static char convert(char c) {
        return (char) (c - DIFF);
    }
}
