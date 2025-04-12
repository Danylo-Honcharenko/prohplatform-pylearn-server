package org.ua.fkrkm.progplatform.utils;

import java.util.UUID;

/**
 * Клас зберігання UUID помилки
 */
public class Msid {
    // UUID помилки
    private static String msidV;

    public static void set(UUID msid) {
        msidV = String.valueOf(msid);
    }

    public static void set(String msid) {
        msidV = msid;
    }

    public static String get() {
        return msidV;
    }
}
