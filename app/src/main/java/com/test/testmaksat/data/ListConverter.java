package com.test.testmaksat.data;

import android.arch.persistence.room.TypeConverter;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Created by Александр on 07.09.2018.
 */

public class ListConverter {
    @RequiresApi(api = Build.VERSION_CODES.N)
    @TypeConverter
    public String fromInList(List<String> inList) {
        return inList.stream().collect(Collectors.joining(","));
    }
    @TypeConverter
    public List<String> toInlist(String data){
        return Arrays.asList(data.split(","));
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @TypeConverter
    public String fromOutList(List<String> inList) {
        return inList.stream().collect(Collectors.joining(","));
    }
    @TypeConverter
    public List<String> toOutlist(String data){
        return Arrays.asList(data.split(","));
    }

}
