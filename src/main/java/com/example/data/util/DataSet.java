package com.example.data.util;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DataSet{
    String value;
    String time;

    public DataSet(String value, String time) {
        this.value = value;
        this.time = time;
    }
}
