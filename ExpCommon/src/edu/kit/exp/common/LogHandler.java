package edu.kit.exp.common;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JOptionPane;

public class LogHandler {

    private static SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.TIME_STAMP_FORMAT);

    public static void popupException(Exception e) {
        JOptionPane.showMessageDialog(null, e.getMessage());
        if (Constants.isSystemDebugMode()) {
            print(e.toString(), "Exception");
            e.printStackTrace();
        }
    }

    public static void popupException(Exception e, String message) {
        JOptionPane.showMessageDialog(null, message);
        if (Constants.isSystemDebugMode()) {
            print(e.toString() + ": " + message, "Exception");
            e.printStackTrace();
        }
    }

    public static void printException(Exception e) {
        if (Constants.isSystemDebugMode()) {
            print(e.toString(), "Exception");
            e.printStackTrace();
        }
    }

    public static void printException(Exception e, String message) {
        if (Constants.isSystemDebugMode()) {
            print(e.toString() + ": " + message, "Exception");
            e.printStackTrace();
        }
    }

    public static void printInfo(String message) {
        if (Constants.isSystemDebugMode()) {
            print(message, "Info");
        }
    }

    private static void print(String message, String priority) {
        String threadName = Thread.currentThread().toString();
        StackTraceElement element = Thread.currentThread().getStackTrace()[3];
        String className = element.getClassName();
        String methodName = element.getMethodName();
        System.out.println(dateFormat.format(new Date()) + " " + priority + " [" + threadName + "] - " + className + "." + methodName + " - " + message);
    }
}
