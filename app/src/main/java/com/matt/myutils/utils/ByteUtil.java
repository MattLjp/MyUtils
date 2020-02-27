package com.matt.myutils.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * @author matt.Ljp
 * @time 2020/2/27 15:27
 * @description byte数据处理工具类
 */
public class ByteUtil {

    /**
     * 十六进制字符串转byte[]
     *
     * @param hex 十六进制字符串
     * @return byte[]
     */
    public static byte[] hexStr2Byte(String hex) {
        if (hex == null) {
            return new byte[]{};
        }

        // 奇数位补0
        if (hex.length() % 2 != 0) {
            hex = "0" + hex;
        }

        int length = hex.length();
        ByteBuffer buffer = ByteBuffer.allocate(length / 2);
        for (int i = 0; i < length; i++) {
            String hexStr = hex.charAt(i) + "";
            i++;
            hexStr += hex.charAt(i);
            byte b = (byte) Integer.parseInt(hexStr, 16);
            buffer.put(b);
        }
        return buffer.array();
    }

    /**
     * byte[]转十六进制字符串
     *
     * @param buffer byte[]
     * @return 十六进制字符串
     */
    public static String byte2hex(byte[] buffer) {
        String h = "";
        if (buffer == null) {
            return "null";
        }

        for (int i = 0; i < buffer.length; i++) {
            String temp = Integer.toHexString(buffer[i] & 0xFF);
            if (temp.length() == 1) {
                temp = "0" + temp;
            }
            h = h + " " + temp;
        }

        return h;
    }

    /**
     * byte[]转十六进制字符串
     *
     * @param array
     * @param offset
     * @param length
     * @return
     */
    public static String byteArrayToHexString(byte[] array, int offset, int length) {
        if (array == null) {
            return "";
        }
        StringBuffer buffer = new StringBuffer();
        for (int i = offset; i < offset + length; i++) {
            buffer.append(byteToHex(array[offset]));
        }
        return buffer.toString();
    }


    /**
     * byte转十六进制字符
     *
     * @param b byte
     * @return 十六进制字符
     */
    public static String byteToHex(byte b) {
        String hex = Integer.toHexString(b & 0xFF);
        if (hex.length() == 1) {
            hex = '0' + hex;
        }
        return hex.toUpperCase(Locale.getDefault());
    }

    /**
     * 有符号byte[]转无符号int[]
     *
     * @param array
     * @return
     */
    public static int[] byteArrayToUintArray(byte[] array) {
        int[] result = new int[array.length];
        for (int a = 0; a < array.length; a++) {
            result[a] = array[a] & 0xff;
        }
        return result;
    }

    /**
     * 无符号int[]转有符号byte[]
     *
     * @param array
     * @return
     */
    public static byte[] uintArrayToByteArray(int[] array) {
        byte[] result = new byte[array.length];
        for (int a = 0; a < array.length; a++) {
            result[a] = (byte) array[a];
        }
        return result;
    }


    /**
     * 将byte转换为一个长度为8的byte数组，数组每个值代表bit
     */
    public static byte[] getBooleanArray(byte b) {
        byte[] array = new byte[8];
        for (int i = 0; i < 8; i++) {
            array[i] = (byte) (b & 1);
            b = (byte) (b >> 1);
        }
        return array;
    }

    /**
     * 将一个长度为8的boolean数组（每bit代表一个boolean值）转换为byte
     * 2014-7-4 下午5:28:28
     *
     * @param array
     * @return
     */
    public static byte getByte(boolean[] array) {
        if (array != null && array.length > 0) {
            byte b = 0;
            for (int i = 0; i <= 7; i++) {
                if (array[i]) {
                    int nn = (1 << (7 - i));
                    b += nn;
                }
            }
            return b;
        }
        return 0;
    }

    /**
     * 把byte转为字符串的bit
     */
    public static String byteToBit(byte b) {
        return ""
                + (byte) ((b >> 7) & 0x1) + (byte) ((b >> 6) & 0x1)
                + (byte) ((b >> 5) & 0x1) + (byte) ((b >> 4) & 0x1)
                + (byte) ((b >> 3) & 0x1) + (byte) ((b >> 2) & 0x1)
                + (byte) ((b >> 1) & 0x1) + (byte) ((b >> 0) & 0x1);
    }


    /**
     * byte数组转换成long
     *
     * @param data
     * @return
     * @throws IOException
     */
    public static long byteArrayToLong(byte[] data) throws IOException {
        ByteArrayInputStream bai = new ByteArrayInputStream(data);
        DataInputStream dis = new DataInputStream(bai);
        return dis.readLong();
    }

    /**
     * long转换成byte数组
     *
     * @param l
     * @return
     * @throws IOException
     */
    public static byte[] longToByteArray(long l) throws IOException {
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bao);
        dos.writeLong(l);
        byte[] buf = bao.toByteArray();
        return buf;
    }


    /**
     * 将int数值转换为占四个字节的byte数组，本方法适用于(低位在前，高位在后)的顺序。 和bytesToInt（）配套使用
     *
     * @param value 要转换的int值
     * @return byte数组
     */
    public static byte[] intToBytes(int value) {
        byte[] src = new byte[4];
        src[3] = (byte) ((value >> 24) & 0xFF);
        src[2] = (byte) ((value >> 16) & 0xFF);
        src[1] = (byte) ((value >> 8) & 0xFF);
        src[0] = (byte) (value & 0xFF);
        return src;
    }

    /**
     * 将int数值转换为占两个字节的byte数组，本方法适用于(低位在前，高位在后)的顺序。
     *
     * @param value 要转换的int值
     * @return byte数组
     */
    public static byte[] int2ToBytes(int value) {
        byte[] src = new byte[2];
        src[1] = (byte) ((value >> 8) & 0xFF);
        src[0] = (byte) (value & 0xFF);
        return src;
    }


    /**
     * 将int数值转换为占四个字节的byte数组，本方法适用于(高位在前，低位在后)的顺序。  和bytesToInt2（）配套使用
     */
    public static byte[] intToBytes2(int value) {
        byte[] src = new byte[4];
        src[0] = (byte) ((value >> 24) & 0xFF);
        src[1] = (byte) ((value >> 16) & 0xFF);
        src[2] = (byte) ((value >> 8) & 0xFF);
        src[3] = (byte) (value & 0xFF);
        return src;
    }


    /**
     * byte数组中取int数值，本方法适用于(低位在前，高位在后)的顺序，和和intToBytes（）配套使用
     *
     * @param src    byte数组
     * @param offset 从数组的第offset位开始
     * @return int数值
     */
    public static int bytesToInt(byte[] src, int offset) {
        int value;
        value = (int) ((src[offset] & 0xFF)
                | ((src[offset + 1] & 0xFF) << 8)
                | ((src[offset + 2] & 0xFF) << 16)
                | ((src[offset + 3] & 0xFF) << 24));
        return value;
    }

    /**
     * 2字节byte[]转int数值，本方法适用于(低位在前，高位在后)的顺序
     *
     * @param src    byte数组
     * @param offset 从数组的第offset位开始
     * @return int数值
     */
    public static int bytes2ToInt(byte[] src, int offset) {
        int value;
        value = (int) ((src[offset] & 0xFF)
                | ((src[offset + 1] & 0xFF) << 8));
        return value;
    }

    /**
     * byte数组中取int数值，本方法适用于(低位在后，高位在前)的顺序。和intToBytes2（）配套使用
     */
    public static int bytesToInt2(byte[] src, int offset) {
        int value;
        value = (int) (((src[offset] & 0xFF) << 24)
                | ((src[offset + 1] & 0xFF) << 16)
                | ((src[offset + 2] & 0xFF) << 8)
                | (src[offset + 3] & 0xFF));
        return value;
    }

    /**
     * 指定编码，字符串转byte[]
     * @param str
     * @return
     */
    public static byte[] stringToBytes(String str) {
        try {
            // 使用指定的字符集将此字符串编码为byte序列并存到一个byte数组中
            return str.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 指定编码，byte转字符串
     * @param bs
     * @return
     */
    public static String bytesToString(byte[] bs) {
        try {
            // 通过指定的字符集解码指定的byte数组并构造一个新的字符串
            return new String(bs, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * byte转字符串
     *
     * @param array  byte[]
     * @param offset 开始偏移量
     * @param length 长度
     * @return
     */
    public static String bytesToString(byte[] array, int offset, int length) {
        if (array == null) {
            return "";
        } else {
            byte[] bytes = new byte[length];
            System.arraycopy(array, offset, bytes, 0, length);
            return new String(bytes);
        }
    }

    /**
     * 拆分byte数组
     *
     * @param superbyte
     * @param size
     * @return
     */
    public static List<byte[]> SplitList(byte[] superbyte, int offset, int size) {
        List<byte[]> result = new ArrayList<>();
        int length = superbyte.length - offset;
        int count = length / size;
        int r = length % size;
        byte[] newbyte;
        for (int i = 0; i < count; i++) {
            newbyte = new byte[size];
            System.arraycopy(superbyte, i * size + offset, newbyte, 0, size);
            result.add(newbyte);
        }

        if (r != 0) {
            byte[] newbyte2 = new byte[r];
            System.arraycopy(superbyte, count * size + offset, newbyte2, 0, r);
            result.add(newbyte2);
        }
        return result;
    }

}