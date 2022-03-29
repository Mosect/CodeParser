package com.mosect.parser4java.core.util;

import java.math.BigDecimal;
import java.math.MathContext;

public final class NumberUtils {

    private NumberUtils() {
    }

    /**
     * 解析十六进制小数字符串<br>
     * 示例：0x1.123p-12<br>
     * 注意：不包含前缀0x
     *
     * @param str 十六进制小数
     * @return 十六进制小数
     */
    public static Number parseHexDecimal32(String str) {
        return parseHexDecimal(str, 8, 23);
    }

    /**
     * 解析十六进制小数字符串<br>
     * 示例：0x1.123p-12<br>
     * 注意：不包含前缀0x
     *
     * @param str 十六进制小数
     * @return 十六进制小数
     */
    public static Number parseHexDecimal64(String str) {
        return parseHexDecimal(str, 11, 52);
    }

    private static Number parseHexDecimal(String str, int exponentSize, int significandSize) {
        int dotIndex = str.indexOf('.');
        int powerIndex = str.indexOf('p');
        if (powerIndex < 0) {
            throw new NumberFormatException("Missing power char");
        }
        if (str.length() - powerIndex <= 1) {
            throw new NumberFormatException("Missing power value");
        }

        int significandNum1;
        long significandNum2;
        int significandCharCount;
        if (dotIndex < 0) {
            // 没有小数点
            if (powerIndex > 0) {
                significandNum1 = Integer.parseInt(str.substring(0, powerIndex), 16);
            } else {
                significandNum1 = 0;
            }
            significandNum2 = 0;
            significandCharCount = 0;
        } else {
            // 含有小数点
            if (dotIndex > 0) {
                significandNum1 = Integer.parseInt(str.substring(0, dotIndex), 16);
            } else {
                significandNum1 = 0;
            }
            if (powerIndex - dotIndex > 1) {
                String str2 = str.substring(dotIndex + 1, powerIndex);
                significandNum2 = Long.parseLong(str2, 16);
                significandCharCount = str2.length();
            } else {
                significandNum2 = 0;
                significandCharCount = 0;
            }
        }
        int powerNum = Integer.parseInt(str.substring(powerIndex + 1));
        // 最大次幂
        int maxPower = ~(Integer.MIN_VALUE >> (Integer.SIZE - exponentSize)) + 1;
        // 最小次幂
        int minPower = -(maxPower - 1);
        if (powerNum < minPower || powerNum > maxPower) {
            throw new NumberFormatException(String.format("Power value over [%s,%s]", minPower, maxPower));
        }

        boolean bit32 = exponentSize + significandSize < 32; // 是否为32位

        if (powerNum == maxPower) {
            // 最大次幂，特殊值
            if (significandNum2 == 0) {
                // 无穷
                if (bit32) {
                    return Float.POSITIVE_INFINITY;
                }
                return Double.POSITIVE_INFINITY;
            } else {
                // 非数值
                if (bit32) {
                    return Float.NaN;
                }
                return Double.NaN;
            }
        }

        if (significandNum1 == 0 && significandNum2 == 0) {
            if (bit32) {
                return 0f;
            }
            return 0d;
        }

        int significandOffset = 1 - significandNum1;
        // 0x1.?p?
        if (bit32) {
            int exponentValue = powerNum - minPower;
            int significandValue = ((int) significandNum2) << (significandSize - significandCharCount * 4);
            int value = (exponentValue << significandSize) | significandValue;
            float fv = Float.intBitsToFloat(value);
            if (significandOffset != 0) {
                return new BigDecimal(significandOffset, MathContext.DECIMAL32)
                        .multiply(new BigDecimal(2, MathContext.DECIMAL32).pow(powerNum, MathContext.DECIMAL32))
                        .add(new BigDecimal(fv));
            }
            return fv;
        } else {
            long exponentValue = powerNum - minPower;
            long significandValue = significandNum2 << (significandSize - significandCharCount * 4);
            long value = (exponentValue << significandSize) | significandValue;
            double dv = Double.longBitsToDouble(value);
            if (significandOffset != 0) {
                return new BigDecimal(significandOffset, MathContext.DECIMAL64)
                        .multiply(new BigDecimal(2, MathContext.DECIMAL64).pow(powerNum, MathContext.DECIMAL64))
                        .add(new BigDecimal(dv));
            }
            return dv;
        }
    }

    /**
     * 解析十六进制小数字符串，包含前缀0x
     *
     * @param str 字符串
     * @return 数值
     */
    public static Number parseHexDecimalWithFull32(String str) {
        if (str.startsWith("0x")) {
            return parseHexDecimal32(str.substring(2));
        }
        throw new NumberFormatException("For input: " + str);
    }

    /**
     * 解析十六进制小数字符串，包含前缀0x
     *
     * @param str 字符串
     * @return 数值
     */
    public static Number parseHexDecimalWithFull64(String str) {
        if (str.startsWith("0x")) {
            return parseHexDecimal64(str.substring(2));
        }
        throw new NumberFormatException("For input: " + str);
    }
}
