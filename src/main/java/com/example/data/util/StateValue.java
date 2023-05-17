package com.example.data.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StateValue {
    public static Map<String, Object> state_value = new HashMap<>() {
        {
            List<String> temp = new ArrayList<>();
            temp.add("boolean");
            temp.add("string");
            temp.add("double");
            temp.add("int");

            for (int i=1; i < 13; i++) {
                // {Client1_string1:"111",}
                String client = "Client" + i + "_";
                for (String element : temp) {
                    for (int j=1; j< 11; j++) {
                        String clientLa = client + element + j;
                        if (element == "string") {
                            put(clientLa, "default");
                        } else {
                            put(clientLa, 1.0);
                        }
                    }
                }

            }
        }
    };
}
