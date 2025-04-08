package com.shousi.web.utils;

import java.awt.*;

/**
 * 工具类：计算颜色相似度
 */
public class ColorSimilarUtils {

    private ColorSimilarUtils() {
        // 工具类不需要实例化
    }

    /**
     * 计算两个颜色的相似度
     *
     * @param color1 第一个颜色
     * @param color2 第二个颜色
     * @return 相似度（越小越相似）
     */
    public static double calculateSimilarity(Color color1, Color color2) {
        int r1 = color1.getRed();
        int g1 = color1.getGreen();
        int b1 = color1.getBlue();

        int r2 = color2.getRed();
        int g2 = color2.getGreen();
        int b2 = color2.getBlue();

        // 计算欧氏距离
        double distance = Math.sqrt(Math.pow(r1 - r2, 2) + Math.pow(g1 - g2, 2) + Math.pow(b1 - b2, 2));

        // 计算相似度
        return distance / Math.sqrt(3 * Math.pow(255, 2));
    }

    public static String normalizeHexColor(String hexColor) {
        // 去除可能存在的0x前缀
        String input = hexColor.startsWith("0x") ? hexColor.substring(2) : hexColor;
        int length = input.length();
        int index = 0;
        StringBuilder expanded = new StringBuilder();
        if (length == 3) {
            return "0x000000";
        }
        // 处理三个颜色分量
        for (int i = 0; i < 3; i++) {
            char current = input.charAt(index);
            if (current == '0') {
                // 当前分量是00的情况
                expanded.append("00");
                index++;
            } else {
                // 正常分量处理（可能包含补零）
                if (index + 1 < length) {
                    expanded.append(current).append(input.charAt(index + 1));
                    index += 2;
                } else {
                    // 最后一个字符单独处理，补零
                    expanded.append(current).append('0');
                    index += 2;
                }
            }
        }
        return "0x" + expanded.toString();
    }

    /**
     * 根据十六进制颜色代码计算相似度
     *
     * @param hexColor1 第一个颜色的十六进制代码（如 0xFF0000）
     * @param hexColor2 第二个颜色的十六进制代码（如 0xFE0101）
     * @return 相似度（0到1之间，1为完全相同）
     */
    public static double calculateSimilarity(String hexColor1, String hexColor2) {
        Color color1 = Color.decode(hexColor1);
        Color color2 = Color.decode(hexColor2);
        return calculateSimilarity(color1, color2);
    }

    // 示例代码
    public static void main(String[] args) {
        // 测试用例
        System.out.println(normalizeHexColor("000"));     // 0x000000
        System.out.println(normalizeHexColor("0a00"));    // 0x00a000
        System.out.println(normalizeHexColor("a0b40"));   // 0xa0b400
        System.out.println(normalizeHexColor("0ab0"));    // 0x00ab00
        System.out.println(normalizeHexColor("00ab"));   // 0x0000ab
        System.out.println(normalizeHexColor("0ab00"));  // 0x00ab00
    }
}
