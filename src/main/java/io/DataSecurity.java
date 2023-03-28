package io;

public final class DataSecurity {
    public static boolean ENABLED = true;

    public static String decrypt(String s) {
        if (ENABLED) {
            final char[] arr = s.toCharArray();
            for (int i = 0; i < arr.length; i++) {
                arr[i] = --arr[i];
            }
            return String.valueOf(arr);
        } else {
            return s;
        }
    }

    public static String encrypt(String s) {
        if (ENABLED) {
            final char[] arr = s.toCharArray();
            for (int i = 0; i < arr.length; i++) {
                arr[i] = ++arr[i];
            }
            return String.valueOf(arr);
        } else {
            return s;
        }
    }
}
