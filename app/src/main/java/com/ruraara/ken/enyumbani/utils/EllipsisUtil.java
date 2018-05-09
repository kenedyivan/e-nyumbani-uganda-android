package com.ruraara.ken.enyumbani.utils;

/**
 * Created by ken on 12/13/17.
 */

public class EllipsisUtil {

    public static final String ELLIPSE = "...";

    public static String toEllipsis(String input, int maxCharacters, int charactersAfterEllipse) {
        if (input == null || input.length() < maxCharacters) {
            return input;
        }
        return input.substring(0, maxCharacters - charactersAfterEllipse)
                + ELLIPSE
                + input.substring(input.length() - charactersAfterEllipse);
    }
}
