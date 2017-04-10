package com.ixigo.utils;

import com.google.gson.Gson;

/**
 * Created by dixant on 23/03/17.
 */
public class JsonUtils {
    private static Gson gson = new Gson();

    public static String toJson(Object src) {
        return gson.toJson(src);
    }

    public static <V> V fromJson(String json, Class<V> clazz) {
        return gson.fromJson(json, clazz);
    }
}
