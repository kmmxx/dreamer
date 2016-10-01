package com.dreamer.tool.zip;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Class help to compress and uncompress the string with the gzip arithmetic.
 *
 * @Author Ding Ji
 * @Since 2013-5-17
 */
public class GzipUtil {

    private static final int IO_BUF_SIZE = 1024;

    public static byte[] compress(String str) {
        if (null == str || str.length() == 0) {
            return "".getBytes();
        }

        byte[] bytes = null;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = new ByteArrayInputStream(str.getBytes());
        GZIPOutputStream gzipOut = null;

        try {
            gzipOut = new GZIPOutputStream(out);
            int size = 0;
            byte[] buf = new byte[IO_BUF_SIZE];
            while ((size = in.read(buf)) > 0) {
                gzipOut.write(buf, 0, size);
                gzipOut.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
                gzipOut.close();
                out.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        bytes = out.toByteArray();
        return bytes;
    }

    public static String uncompress(byte[] bytes) {
        if (null == bytes || bytes.length == 0) {
            return "";
        }

        String result = "";
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        GZIPInputStream gzip = null;
        try {
            gzip = new GZIPInputStream(in);
            byte[] buf = new byte[IO_BUF_SIZE];
            int size = 0;
            while ((size = gzip.read(buf)) > 0) {
                out.write(buf, 0, size);
                out.flush();
            }
        } catch (IOException e) {
            LogUtil.e(e);
        } finally {
            try {
                in.close();
                gzip.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        result = out.toString();
        return result;
    }
}