package com.avenwu.deepinandroid;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.util.Log;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    final static char ASSIC_START = '!';
    final static char ASSIC_END = '~';

    final static char UNICODE_START = '！';
    final static char UNICODE_END = '～';

    final static long DIFF = UNICODE_START - ASSIC_START;

    public void testEncodeFormat() {
        String res = "，！@#%&*（）？；";
        String des = ",!@#%&*()?;";
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < res.length(); i++) {
            char c = res.charAt(i);
            Log.e("TEST_UNICODE", "" + c);
            if (isEXPANDUNICODE(c)) {
                c = unicode2Assic(c);
                Log.e("TEST_UNICODE", "translated:" + c);
            }
            builder.append(c);
        }
        assertEquals(des, builder.toString());
    }

    boolean isBasicASSIC(char c) {
        return c >= ASSIC_START && c <= ASSIC_END;
    }

    boolean isEXPANDUNICODE(char c) {
        return c >= UNICODE_START && c <= UNICODE_END;
    }

    char unicode2Assic(char c) {
        return (char) (c - DIFF);
    }
}