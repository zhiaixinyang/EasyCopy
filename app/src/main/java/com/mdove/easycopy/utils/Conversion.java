package com.mdove.easycopy.utils;

import android.text.TextUtils;

import com.baidu.ocr.sdk.utils.LogUtil;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Conversion {
    private static final String CLASS_TAG = Conversion.class.getSimpleName();

    public static String bool2Str(boolean b) {
        return Boolean.toString(b);
    }

    public static boolean str2Bool(String str) {
        try {
            if (!TextUtils.isEmpty(str)) {
                return Boolean.valueOf(str);
            }
        } catch (Exception ex) {
        }

        return false;
    }

    public static String int2Str(int num) {
        return int2Str(num, "0");
    }

    public static String int2Str(int num, String emptyVal) {
        return num != 0 ? Integer.toString(num) : emptyVal;
    }

    public static int str2Int(String str) {
        try {
            if (!TextUtils.isEmpty(str)) {
                return Integer.valueOf(str);
            }
        } catch (Exception ex) {
        }

        return 0;
    }

    public static String long2Str(long num) {
        return long2Str(num, "0");
    }

    public static String long2Str(long num, String emptyVal) {
        return num != 0 ? Long.toString(num) : emptyVal;
    }

    public static long str2Long(String str) {
        try {
            if (!TextUtils.isEmpty(str)) {
                return Long.valueOf(str);
            }
        } catch (Exception ex) {
            LogUtil.d(CLASS_TAG + "-str2Long", ex.getLocalizedMessage());
        }

        return 0;
    }

    public static String float2Str(float num) {
        return float2Str(num, "0");
    }

    public static String float2Str(float num, String emptyVal) {
        return num != 0 ? Float.toString(num) : emptyVal;
    }

    public static float str2Float(String str) {
        try {
            if (!TextUtils.isEmpty(str)) {
                return Float.valueOf(str);
            }
        } catch (Exception ex) {
        }

        return 0;
    }

    public static String double2Str(double num) {
        return double2Str(num, "0");
    }

    public static String double2Str(double num, String emptyVal) {
        return num != 0 ? Double.toString(num) : emptyVal;
    }

    public static double str2Double(String str) {
        try {
            if (!TextUtils.isEmpty(str)) {
                return Double.valueOf(str);
            }
        } catch (Exception ex) {
        }
        return 0.0;
    }

    /**
     * double设置保留小数位数（向下取整，正数、负数均趋于0）
     *
     * @param value 要设置小数位数的小数
     * @param scale 保留的小数位数
     * @return
     */
    public static double scaleDouble(double value, int scale) {
        try {
            BigDecimal decimal = new BigDecimal(value);
            return decimal.setScale(scale, BigDecimal.ROUND_DOWN).doubleValue();
        } catch (Exception ex) {
        }

        return value;
    }

    /**
     * float类型的小数设置保留小数位数（向下取整，正数、负数均趋于0）
     *
     * @param value 要设置小数位数的小数
     * @param scale 保留的小数位数
     * @return
     */
    public static float scaleFloat(float value, int scale) {
        try {
            BigDecimal decimal = new BigDecimal(value);
            return decimal.setScale(scale, BigDecimal.ROUND_DOWN).floatValue();
        } catch (Exception ex) {
        }

        return value;
    }

    public static String date2Str(Date date) {
        return date2Str(date, null);
    }

    public static String date2Str(Date date, String format) {
        if (date == null) {
            return "";
        }
        format = TextUtils.isEmpty(format) ? "yyyy-MM-dd HH:mm:ss" : format;
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String dateStr = "";
        try {
            dateStr = sdf.format(date);
        } catch (Exception ex) {
        }

        return dateStr;
    }

    public static String date2Str(long date, String format) {
        if (date < 0) {
            return "";
        }
        format = TextUtils.isEmpty(format) ? "yyyy-MM-dd HH:mm:ss" : format;
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String dateStr = "";
        try {
            dateStr = sdf.format(date);
        } catch (Exception ex) {
        }

        return dateStr;
    }


    public static Date str2Date(String dateStr) {
        return str2Date(dateStr, null);
    }

    public static String str2Str(String data, String formatStr) {
        String result = "";
        try {
            if (!TextUtils.isEmpty(data)) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                SimpleDateFormat format2 = new SimpleDateFormat(formatStr);
                Date parse = format.parse(data, new ParsePosition(0));
                result = format2.format(parse);
            }
            return result;
        } catch (Exception e) {
            result = data;
        }
        return result;
    }


    public static Date str2Date(String dateStr, String format) {
        if (TextUtils.isEmpty(dateStr)) {
            return null;
        }
        format = TextUtils.isEmpty(format) ? "yyyy-MM-dd HH:mm:ss" : format;
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = sdf.parse(dateStr);
        } catch (Exception ex) {
        }

        return date;
    }

    public static long str2TimeStamp(String dateStr) {
        return str2TimeStamp(dateStr, null);
    }

    public static long str2TimeStamp(String dateStr, String format) {
        if (TextUtils.isEmpty(dateStr)) {
            return 0;
        }

        format = TextUtils.isEmpty(format) ? "yyyy-MM-dd HH:mm:ss" : format;
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        long timestamp = 0;
        try {
            Date date = sdf.parse(dateStr);
            timestamp = date.getTime();
        } catch (Exception ex) {
            LogUtil.d(CLASS_TAG + "-str2TimeStamp", ex.getLocalizedMessage());
        }

        return timestamp;
    }

    public static String timeStamp2DateStr(long timeStamp) {
        return timeStamp2DateStr(timeStamp, null);
    }

    public static String timeStamp2DateStr(long timeStamp, String dateFormat) {
        if (dateFormat == null) {
            dateFormat = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        String dateStr = "";
        try {
            Date date = new Date(timeStamp);
            dateStr = sdf.format(date);
        } catch (Exception ex) {
            LogUtil.d(CLASS_TAG + "-timeStamp2DateStr", ex.getLocalizedMessage());
        }

        return dateStr;
    }

    public static String formatFloatString(String valueString) {
        DecimalFormat decimalFormat = new DecimalFormat("0.00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
        String p = decimalFormat.format(str2Float(valueString));
        return p;
    }

    public static String getPartialPhone(String phone, String defaultText) {
        defaultText = (defaultText == null) ? "点击查看" : defaultText;

        if (TextUtils.isEmpty(phone)) {
            return defaultText;
        }
        int phoneLength = phone.length();
        if (phoneLength >= 7) {
            String begin = phone.substring(0, 3);
            String end = phone.substring(phoneLength - 4, phoneLength);
            return begin + "****" + end;
        }
        return defaultText;
    }


    /***
     * 时间计算
     * @param original  原始时间,格式必须是yyyy-MM-dd HH:mm:ss
     * @param value     加减的时间
     * @param outFormat 输出格式
     * @return
     */
    public static String calculatingTime(String original, long value, String outFormat) {
        try {
            Date date = Conversion.str2Date(original);
            return calculatingTime(date, value, outFormat);
        } catch (Exception e) {
            e.printStackTrace();
            return original;
        }
    }

    /***
     * 时间计算
     * @param original  原始时间
     * @param value     加减的时间
     * @param outFormat 输出格式
     * @return
     */
    public static String calculatingTime(Date original, long value, String outFormat) {
        try {
            long reslutValue = original.getTime() + value;
            Date data = new Date(reslutValue);
            String endTime = Conversion.date2Str(data, outFormat);
            return endTime;
        } catch (Exception e) {
            e.printStackTrace();
            return date2Str(original);
        }
    }

    /**
     * 格式化 float 保留两位小数
     *
     * @param f
     * @return
     */
    public static String formatFloatTwoDecimalPlaces(float f) {
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(f);
    }
}
