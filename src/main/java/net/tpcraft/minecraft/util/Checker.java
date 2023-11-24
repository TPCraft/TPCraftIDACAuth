package net.tpcraft.minecraft.util;

public class Checker {
    public static boolean stringIsNullOrEmpty(String... values) {
        for (String value : values) {
            if (value == null || value.isEmpty()) {
                return true;
            }
        }
        return false;
    }
}
