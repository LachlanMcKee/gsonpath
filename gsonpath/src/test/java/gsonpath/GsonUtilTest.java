package gsonpath;

import com.google.gson.stream.JsonReader;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import static gsonpath.GsonUtil.isValidValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

public class GsonUtilTest {

    private static final String NON_NUMBER = "non-number";
    private static final String NUMBER = "number";
    private static final String NULL_NUMBER = "null-number";

    private static final byte[] testData =
            ("{\"" + NON_NUMBER + "\":\"\",\"" +
                    NUMBER + "\":5,\"" +
                    NULL_NUMBER + "\":null}").getBytes();

    private JsonReader in;

    @Before
    public void setUp() throws IOException {
        in = new JsonReader(new InputStreamReader(new ByteArrayInputStream(testData)));
        if (!isValidValue(in)) {
            fail();
        }
    }

    @Test
    public void testGetIntegerSafely() {
        try {
            in.beginObject();
            while (in.hasNext()) {
                switch (in.nextName()) {
                    case NON_NUMBER:
                        assertNull(GsonUtil.getIntegerSafely(in));
                        break;
                    case NUMBER:
                        assertEquals(Integer.valueOf(5), GsonUtil.getIntegerSafely(in));
                        break;
                    case NULL_NUMBER:
                        assertNull(GsonUtil.getIntegerSafely(in));
                        break;
                    default:
                        in.skipValue();
                        break;

                }
            }
            in.endObject();
        }
        catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testGetLongSafely() {
        try {
            in.beginObject();
            while (in.hasNext()) {
                switch (in.nextName()) {
                    case NON_NUMBER:
                        assertNull(GsonUtil.getLongSafely(in));
                        break;
                    case NUMBER:
                        assertEquals(Long.valueOf(5), GsonUtil.getLongSafely(in));
                        break;
                    case NULL_NUMBER:
                        assertNull(GsonUtil.getLongSafely(in));
                        break;
                    default:
                        in.skipValue();
                        break;

                }
            }
            in.endObject();
        }
        catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testGetDoubleSafely() {
        try {
            in.beginObject();
            while (in.hasNext()) {
                switch (in.nextName()) {
                    case NON_NUMBER:
                        assertNull(GsonUtil.getDoubleSafely(in));
                        break;
                    case NUMBER:
                        assertEquals(Double.valueOf(5), GsonUtil.getDoubleSafely(in));
                        break;
                    case NULL_NUMBER:
                        assertNull(GsonUtil.getDoubleSafely(in));
                        break;
                    default:
                        in.skipValue();
                        break;

                }
            }
            in.endObject();
        }
        catch (Exception e) {
            fail();
        }
    }
}
