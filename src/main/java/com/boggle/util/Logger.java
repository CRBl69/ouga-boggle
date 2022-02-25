package com.boggle.util;

import java.util.HashMap;

public class Logger {
    public static final int INFO = 30;
    public static final int WARN = 20;
    public static final int ERROR = 10;
    private final String tag;
    private final int level;
    private static HashMap<String, Logger> loggers = new HashMap<>();
    private static int defaultLogLevel = INFO;

    public Logger(String tag, int level) {
        this.tag = tag;
        this.level = level;
    }

    public static Logger getLogger(String tag) {
        if (!loggers.containsKey(tag)) {
            loggers.put(tag, new Logger(tag, defaultLogLevel));
        }
        return loggers.get(tag);
    }

    public void error(String message) {
        if (level >= Logger.ERROR) {
            System.out.printf("[%s][ERORR]: %s\n", tag, message);
        }
    }

    public void warn(String message) {
        if (level >= Logger.WARN) {
            System.out.printf("[%s][WARN]: %s\n", tag, message);
        }
    }

    public void info(String message) {
        if (level >= Logger.INFO) {
            System.out.printf("[%s][INFO]: %s\n", tag, message);
        }
    }

    public static void setLogLevel(int level) {
        Logger.defaultLogLevel = level;
    }
}