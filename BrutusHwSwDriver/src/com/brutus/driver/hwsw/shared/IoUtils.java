package com.brutus.driver.hwsw.shared;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class IoUtils {

    private static final int BUFFER_SIZE = 8192;

    public static long copy(InputStream is, OutputStream os) {
        byte[] buf = new byte[BUFFER_SIZE];
        long total = 0;
        int len = 0;
        try {
            while (-1 != (len = is.read(buf))) {
                os.write(buf, 0, len);
                total += len;
            }
        } catch (IOException ioe) {
            throw new RuntimeException("error reading stream", ioe);
        }
        return total;
    }

}
