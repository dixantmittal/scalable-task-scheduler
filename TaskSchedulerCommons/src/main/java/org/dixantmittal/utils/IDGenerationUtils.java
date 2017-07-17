package org.dixantmittal.utils;

import java.util.UUID;

/**
 * Created by dixant on 28/03/17.
 */
public class IDGenerationUtils {
    public static String generateRandomUUID(String uniqueIdentifier) {
        return uniqueIdentifier + UUID.randomUUID().toString();
    }
}
