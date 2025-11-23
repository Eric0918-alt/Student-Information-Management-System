package com.studentmanagement.util;

import java.util.Scanner;

public class InputUtil {
    private static Scanner scanner = new Scanner(System.in);

    public static String getString(String prompt) {
        String input;
        while (true) {
            System.out.println(prompt);
            input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                break;
            } else {
                System.out.println("输入不能为空，请重新输入！");
            }
        }
        return input;
    }

    public static int getInt(String prompt) {
        while (true) {
            try {
                System.out.println(prompt);
                String input = scanner.nextLine().trim();
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("输入格式错误，请输入一个整数！");
            }
        }
    }

    public static String getPassword(String prompt) {
        return getString(prompt);
    }
}
