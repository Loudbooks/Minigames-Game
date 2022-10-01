package com.loudbook.minestom.api.util;

import java.util.List;
import java.util.Random;

public class GeneralUtils {
    public static String randomString(List<String> list){
        Random rand = new Random();
        return list.get(rand.nextInt(list.size()));
    }
}
