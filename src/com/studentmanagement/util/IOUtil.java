package com.studentmanagement.util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class IOUtil {
    public static List<String> readFile(String filePath) {
        List<String> lines = new ArrayList<>();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(filePath));
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    lines.add(line);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("文件 " + filePath + " 不存在，首次运行将自动创建。");
        } catch (IOException e) {
            System.out.println("文件读取失败：" + e.getMessage());
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    System.out.println("文件流关闭失败：" + e.getMessage());
                }
            }
        }
        return lines;
    }

    public static void writeFile(String filePath, List<String> lines) {
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(filePath));
            for (String line : lines) {
                bw.write(line);
                bw.newLine();
            }
            System.out.println("文件写入成功！");
        } catch (IOException e) {
            System.out.println("文件写入失败：" + e.getMessage());
        } finally {
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                    System.out.println("文件流关闭失败：" + e.getMessage());
                }
            }
        }
    }
}