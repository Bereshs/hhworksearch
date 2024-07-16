package ru.bereshs.hhworksearch.hhapiclient;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class HhLocalDateTime {

    private HhLocalDateTime(){};
    public static LocalDateTime decodeData(String data) {
        String pattern="EEE, dd MMM yyyy HH:mm:ss z";
        String pattern1="EEE, dd-MMM-yyyy HH:mm:ss z";

        if(data.contains("-")){
            pattern=pattern1;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern, Locale.ENGLISH);
        return LocalDateTime.parse(data, formatter);
    }

    public static LocalDateTime decodeLocalData(String data) {
        String resultData = data.substring(0, data.indexOf("+"));
        return LocalDateTime.parse(resultData);
    }

}
