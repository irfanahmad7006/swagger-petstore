package io.swagger.petstore.qaUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class FakeData {

    public static int getRandomDigit() {
//        int x = 0;
//        for (int i = 1; i <= 10; i++) {
//            x = 10 + (int) (Math.random() * 999);
//
//        }
//        return x;
        Random rn = new Random();
        int answer = rn.nextInt(500) + 1;
        return answer;

    }


    public static HashMap<Object, Object> generateDataFromHashMap(Map<String, String> createStoreMap) {
        HashMap<Object, Object> updatedDataMap = new HashMap<Object, Object>();
        for (String key : createStoreMap.keySet()) {

            if (createStoreMap.get(key).equalsIgnoreCase("GenerateNumber")) {
                String digit = String.valueOf(getRandomDigit());
                updatedDataMap.put(key, digit);
            } else if (createStoreMap.get(key).equalsIgnoreCase("CurrentDate")) {
                DateTimeFormatter dtf = DateTimeFormatter.ISO_DATE_TIME;
                LocalDateTime now = LocalDateTime.now();
                String formattedDateTime = now.format(dtf);
                updatedDataMap.put(key, formattedDateTime);
            } else if (createStoreMap.get(key).equalsIgnoreCase("GenerateString")) {
                String digit = String.valueOf(getRandomDigit());
                updatedDataMap.put(key, "Test" + digit);
            } else {
                updatedDataMap.put(key, createStoreMap.get(key));
            }

        }
        return updatedDataMap;
    }

}
