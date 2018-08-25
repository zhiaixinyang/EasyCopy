package com.mdove.easycopy.utils;

import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateTimeUtil {
	private static final String DATE_FORMAT = "yyyy-MM-dd";
	private static final String DATE_FORMAT_E_Y_M_D_H_M = "yyyy-MM-dd hh:mm";
	private static final String DATE_FORMAT_C_Y_M_D_H_M = "yyyy年MM月dd日 hh:mm";

	public static String getTodayDate(){
		return getTodayDate(DATE_FORMAT);
	}
	
	public static String getTodayDate(String dateFormat){
		return Conversion.date2Str(new Date(), dateFormat);
	}

	public static String getChineseTime(long time){
		return Conversion.date2Str(time, DATE_FORMAT_C_Y_M_D_H_M);
	}
	
	public static String getYesterdayDate(){
		return getCertainDate(-1);
	}
	
	public static String getTomorrowDate(){
		return getCertainDate(1);
	}
	
	public static String getCertainDate(int daysAfterToday){
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, daysAfterToday);
		return Conversion.date2Str(cal.getTime(), DATE_FORMAT);
	}

	public static int getCurrentYear(){
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.YEAR);
	}

	public static List<String> getYearList(int startOffset, int endOffset){
		int currentYear = getCurrentYear();
		int startYear = currentYear - startOffset;
		int endYear = currentYear + endOffset;
		ArrayList<String> yearList = new ArrayList<String>();
		for(int i = startYear; i <= endYear; i++){
			yearList.add(i + "");
		}
		return yearList;
	}

	/**
	 * 返回1970/01/01 00:00:00开始到指定日期00:00:00的毫秒数
	 * @param date
	 * @return 1970/01/01 00:00:00开始到指定日期00:00:00的毫秒数
	 */
	public static long getMillisecondsUntilCertainDate(Date date){
		return getMillisecondsUntilCertainDate(date, 0);
	}

	/**
	 * 返回1970/01/01 00:00:00开始到指定日期00:00:00的毫秒数
	 * @param date
	 * @param dayOffset 日期offset
	 * @return 1970/01/01 00:00:00开始到指定日期00:00:00的毫秒数
	 */
	public static long getMillisecondsUntilCertainDate(Date date, int dayOffset){
		Calendar calendar = new GregorianCalendar(date.getYear() + 1900, date.getMonth(), date.getDate());
		if(dayOffset != 0){
			calendar.add(Calendar.DATE, dayOffset);
		}

		return calendar.getTimeInMillis();
	}

	public static Calendar getStartTimeOfThisMonth() {
		Calendar now = Calendar.getInstance();
		now.set(now.get(Calendar.YEAR), now.get(Calendar.MONTH), 1, 0, 0, 0);
		return now;
	}

	public static Calendar getStartTimeOfLastMonth() {
		Calendar now = Calendar.getInstance();
		now.add(Calendar.MONTH, -1);
		now.set(now.get(Calendar.YEAR), now.get(Calendar.MONTH), 1, 0, 0, 0);
		return now;
	}

	public static Calendar getEndTimeOfThisMonth() {
		Calendar now = Calendar.getInstance();
		now.set(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.getActualMaximum(Calendar.DATE), 23, 59, 59);
		return now;
	}

	/**
	 * 计算指定的日期到今天相差的天数，如果为未来的日期则结果为负
	 * @param date
	 * @return
     */
	public static int getDaysUntilNow(String date) {
		int betweenDays = 0;
		try {
			Date dateObj;
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			dateObj = sdf.parse(date);
			Calendar cal = Calendar.getInstance();
			long timeNow = cal.getTimeInMillis();
			cal.setTime(dateObj);
			long targetTime = cal.getTimeInMillis();
			betweenDays = (int) ((timeNow - targetTime) / (1000 * 3600 * 24));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return betweenDays;
	}

	public static String[] getBeginAndEndMonthInYear(int addYear) {
		try {
			String[] date = new String[2];
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.YEAR, addYear);
			cal.set(Calendar.MONTH, Calendar.JANUARY);
			date[0] = sdf.format(cal.getTime());
			cal.set(Calendar.MONTH, Calendar.DECEMBER);
			date[1] = sdf.format(cal.getTime());
			return date;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 给定日期转化为指定格式的日期字符串
	 * @param date
	 * @param format
     * @return
     */
	public static String getDateFormatString(String date, String format) {
		try {
			if (TextUtils.isEmpty(date) || TextUtils.isEmpty(format)) {
				return null;
			}
			SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
			SimpleDateFormat sdfNew = new SimpleDateFormat(format);
			Date dateObj;
			try {
				dateObj = sdf.parse(date);
				if (dateObj != null) {
					String targetDate = sdfNew.format(dateObj);
					return targetDate;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}catch (Exception e){

		}
		return "";
	}

	/**
	 * 获取当前时间字符串
	 * @param format
	 * @return
	 */
	public static String getCurrentDateTime(String format) {
		String dateString;
		long current = System.currentTimeMillis();
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			Date date = new Date();
			date.setTime(current);
			dateString = sdf.format(date);
		} catch (Exception e) {
			dateString = "";
		}
		return dateString;
	}

	/**
	 * 返回两个时间间隔
	 * @param start
	 * @param end
	 * @return
	 */
	public static long getTimeSpace(String start, String end) {
		long space = 0;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date dateStart = sdf.parse(start);
			Date dateEnd = sdf.parse(end);
			long startLong = dateStart.getTime();
			long endLong = dateEnd.getTime();
			if (endLong > startLong) {
				return endLong - startLong;
			}
		}  catch (Exception e) {
		}
		return space;
	}

	/**
	 * 判断日期格式:yyyy-mm-dd
	 *
	 * @param sDate
	 * @return
	 */
	public static boolean isValidDate(String sDate) {
		String datePattern1 = "\\d{4}-\\d{2}-\\d{2}";
		String datePattern2 = "^((\\d{2}(([02468][048])|([13579][26]))"
				+ "[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|"
				+ "(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?"
				+ "((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?("
				+ "(((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?"
				+ "((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))";
		if ((sDate != null)) {
			Pattern pattern = Pattern.compile(datePattern1);
			Matcher match = pattern.matcher(sDate);
			if (match.matches()) {
				pattern = Pattern.compile(datePattern2);
				match = pattern.matcher(sDate);
				return match.matches();
			} else {
				return false;
			}
		}
		return false;
	}

    public static boolean isToday(Date dateTime) {
		if (dateTime == null) {
			return false;
		}
		String dateStr = Conversion.date2Str(dateTime, DATE_FORMAT);
		return getTodayDate().equals(dateStr);
    }
}
