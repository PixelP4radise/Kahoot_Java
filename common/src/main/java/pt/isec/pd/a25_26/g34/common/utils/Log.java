package pt.isec.pd.a25_26.g34.common.utils;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Log {
    private static final String RESET = "\u001B[0m";
    private static final String RED = "\u001B[31m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BLUE = "\u001B[34m";
    private static final String PURPLE = "\u001B[35m";
    private static final String CYAN = "\u001B[36m";

    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm:ss");

    private Log() {
    }

    private static void print(String colorcode, String type, String tag, String message) {
        String time = LocalTime.now().format(TIME_FORMAT);

        System.out.printf("%s[%s] [%-5s] [%-10s] %s%s%n", colorcode, time, type, tag, message, RESET);
    }

    public static void info(String tag, String message) {
        print(BLUE, "INFO", tag, message);
    }

    public static void success(String tag, String message) {
        print(GREEN, "OK", tag, message);
    }

    public static void warn(String tag, String message) {
        print(YELLOW, "WARN", tag, message);
    }

    public static void error(String tag, String message) {
        print(RED, "ERROR", tag, message);
    }

    public static void debug(String tag, String message) {
        print(CYAN, "DEBUG", tag, message);
    }
}
