package com.github.oahnus.rpcdemo.api;

/**
 * Created by oahnus on 2019/7/15
 * 11:04.
 */
public class ByteUtil {
    public static byte[] concat(byte[] a, byte[] b) {
        if (a == null && b == null) {
            return null;
        }
        if (a == null || b == null) {
            return a == null ? b : a;
        }
        byte[] result = new byte[a.length + b.length];
        System.arraycopy(a, 0, result, 0, a.length);
        System.arraycopy(b, 0, result, a.length, b.length);
        return result;
    }

    public static void main(String... args) {
        byte[] b1 = "Hello".getBytes();
        byte[] b2 = "world".getBytes();

        byte[] b3 = concat(b1, b2);
        System.out.println(new String(b3));
    }
}
