package pxl.kwops.domain.utils;

public final class StringUtils {
    private StringUtils() {
    }

    public static boolean isNullOrEmpty(String string) {
        return string == null || string.isEmpty();
    }
}
