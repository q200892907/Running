package com.jvtd.running_sdk.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/10/16.
 * 正则工具
 */
public class JvtdMatchUtils
{

  /**
   * 正则：手机号（精确）
   * <p>移动：134(0-8)、135、136、137、138、139、147、150、151、152、157、158、159、178、182、183、184、187、188</p>
   * <p>联通：130、131、132、145、155、156、171、175、176、185、186</p>
   * <p>电信：133、153、173、177、180、181、189</p>
   * <p>新增: 166及199的正则适配</p>
   * <p>新增： 141,144,146,148,149,162,165,167,172,191,198</p>
   * <p>全球星：1349</p>
   * <p>虚拟运营商：170</p>
   */
  private static final String REGEX_MOBILE_EXACT = "^((13[0-9])|(14[1,4-9])|(15[0-3,5-9])|(16[2,5-7])|(17[0-3,5-8])|(18[0-9])|(19[1,8-9]))\\d{8}$";

  /**
   * 正则：电话号码
   */
  private static final String REGEX_TEL = "^($$[0]{1}\\d{2,3}-)|([(]?[（]?([0]{1}\\d{2,3})?[)]?[）]?[-]?)\\d{7,8}$";

  /**
   * 正则：邮箱
   */
  private static final String REGEX_EMAIL = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";

  /**
   * 正则：用户名，取值范围为a-z,A-Z,0-9,"_",汉字，不能以"_"结尾,用户名必须是2-20位
   */
  private static final String REGEX_USERNAME = "^[\\w\\u4e00-\\u9fa5]{2,20}(?<!_)$";

  /**
   * 密码正则 6-16 位字母加数字组合
   */
  private static final String LOGIN_PASSWORD = "^(?=.*\\d)(?=.*[a-zA-Z]).{6,16}$";
//  private static final String LOGIN_PASSWORD = "^([A-Z]|[a-z]|[0-9]){6,16}$";


  /**
   * 正则：QQ号
   */
  private static final String REGEX_TENCENT_QQ = "[1-9][0-9]{4,10}";

  /**
   * 正则：中国邮政编码
   */
  private static final String REGEX_ZIP_CODE = "[1-9]\\d{5}(?!\\d)";

  /**
   * 正则：网址
   */
  public static final String REGEX_HTTP = "(?i)\\b((?:[a-z][\\w-]+:(?:/{1,3}|[a-z0-9%])" +
      "|www\\d{0,3}[.]|[a-z0-9.\\-]+[.][a-z]{2,4}/)(?:[^\\s()<>]+|\\(([^\\s()<>]+|(\\([^\\s()" +
      "<>]+\\)))*\\))+(?:\\(([^\\s()<>]+|(\\([^\\s()<>]+\\)))*\\)|[^\\s`!()\\[\\]{};:\\'\".," +
      "<>?«»“”‘’]))";

  /**
   * 正则：中文真实姓名
   */
  private static final String REGEX_TURENAME = "^[\\u4e00-\\u9fa5]+$";
  private static final String REGEX_TURENAME_FEW = "^[\\u4e00-\\u9fa5]+[·•][\\u4e00-\\u9fa5]+$";//少数民族

  private JvtdMatchUtils()
  {
  }


  //根据此方法进行拓展
  private static boolean isMatch(final String regex, final CharSequence input)
  {
    return input != null && input.length() > 0 && Pattern.matches(regex, input);
  }

  /**
   * 验证手机号（精确）
   *
   * @param input 待验证文本
   * @return {@code true}: 匹配<br>{@code false}: 不匹配
   */
  public static boolean isMobile(final CharSequence input)
  {
    return isMatch(REGEX_MOBILE_EXACT, input);
  }

  /**
   * 验证电话号码
   *
   * @param input 待验证文本
   * @return {@code true}: 匹配<br>{@code false}: 不匹配
   */
  public static boolean isTel(final CharSequence input)
  {
    return isMatch(REGEX_TEL, input);
  }

  /**
   * 验证邮箱
   *
   * @param input 待验证文本
   * @return {@code true}: 匹配<br>{@code false}: 不匹配
   */
  public static boolean isEmail(final CharSequence input)
  {
    return isMatch(REGEX_EMAIL, input);
  }

  /**
   * 密码格式是否正确 6 - 20 位
   *
   * @param input 待验证文本
   * @return {@code true}:匹配<br>{@code false}:不匹配
   */
  public static boolean isPassword(final CharSequence input)
  {
    return isMatch(LOGIN_PASSWORD, input);
  }

  /**
   * QQ 长度是否正确
   * @param input QQ
   * @return {@code true}:匹配<br>{@code false}:不匹配
   */
  public static boolean isQQ(final CharSequence input)
  {
    return isMatch(REGEX_TENCENT_QQ,input);
  }

  /**
   * 邮政编码
   * @param input 邮政编码
   * @return {@code true}:匹配<br>{@code false}:不匹配
   */
  public static boolean isZipCode(final CharSequence input)
  {
    return isMatch(REGEX_ZIP_CODE,input);
  }


  /**
   * 判断是否为正确身份证
   */
  public static boolean isIdCard(String IDStr) {
    String Ai = "";

    if (null == IDStr || IDStr.trim().isEmpty()) {
      //"身份证号码长度应该为15位或18位。";
      return false;
    }

    // 判断号码的长度 15位或18位
    if (IDStr.length() != 15 && IDStr.length() != 18) {
      //"身份证号码长度应该为15位或18位。";
      return false;
    }

    // 18位身份证前17位位数字，如果是15位的身份证则所有号码都为数字
    if (IDStr.length() == 18) {
      Ai = IDStr.substring(0, 17);
    } else
    {
      Ai = IDStr.substring(0, 6) + "19" + IDStr.substring(6, 15);
    }
    if (!isNumeric(Ai)) {
      //"身份证15位号码都应为数字 ; 18位号码除最后一位外，都应为数字。";
      return false;
    }
    // 判断出生年月是否有效
    String strYear = Ai.substring(6, 10);// 年份
    String strMonth = Ai.substring(10, 12);// 月份
    String strDay = Ai.substring(12, 14);// 日期
    if (!isDate(strYear + "-" + strMonth + "-" + strDay)) {
      //"身份证出生日期无效。";
      return false;
    }
    GregorianCalendar gc = new GregorianCalendar();
    SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
    try {
      if ((gc.get(Calendar.YEAR) - Integer.parseInt(strYear)) > 150
              || (gc.getTime().getTime() - s.parse(strYear + "-" + strMonth + "-" + strDay).getTime()) < 0) {
        //"身份证生日不在有效范围。";
        return false;
      }
    } catch (java.text.ParseException | NumberFormatException e) {
      e.printStackTrace();
    }
    if (Integer.parseInt(strMonth) > 12 || Integer.parseInt(strMonth) == 0) {
      //"身份证月份无效";
      return false;
    }
    if (Integer.parseInt(strDay) > 31 || Integer.parseInt(strDay) == 0) {
      //"身份证日期无效";
      return false;
    }
    // 判断地区码是否有效
    Hashtable areacode = GetAreaCode();
    // 如果身份证前两位的地区码不在Hashtable，则地区码有误

    if (areacode.get(Ai.substring(0, 2)) == null) {
      return false;
    }

    return isVarifyCode(Ai,IDStr);
  }

  /*
   * 判断第18位校验码是否正确 第18位校验码的计算方式： 1. 对前17位数字本体码加权求和 公式为：S = Sum(Ai * Wi), i =
   * 0, ... , 16 其中Ai表示第i个位置上的身份证号码数字值，Wi表示第i位置上的加权因子，其各位对应的值依次为： 7 9 10 5 8 4
   * 2 1 6 3 7 9 10 5 8 4 2 2. 用11对计算结果取模 Y = mod(S, 11) 3. 根据模的值得到对应的校验码
   * 对应关系为： Y值： 0 1 2 3 4 5 6 7 8 9 10 校验码： 1 0 X 9 8 7 6 5 4 3 2
   */
  private static boolean isVarifyCode(String Ai, String IDStr) {
    String[] VarifyCode = {"1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2"};
    String[] Wi = {"7", "9", "10", "5", "8", "4", "2", "1", "6", "3", "7", "9", "10", "5", "8", "4", "2"};
    int sum = 0;
    for (int i = 0; i < 17; i++) {
      sum = sum + Integer.parseInt(String.valueOf(Ai.charAt(i))) * Integer.parseInt(Wi[i]);
    }
    int modValue = sum % 11;
    String strVerifyCode = VarifyCode[modValue];
    Ai = Ai + strVerifyCode;
    if (IDStr.length() == 18) {
      if (!Ai.equals(IDStr)) {
        return false;
      }
    }
    return true;
  }

  /**
   * 将所有地址编码保存在一个Hashtable中
   *
   * @return Hashtable 对象
   */
  private static Hashtable GetAreaCode() {
    Hashtable<String,String> hashtable = new Hashtable<>();
    hashtable.put("11", "北京");
    hashtable.put("12", "天津");
    hashtable.put("13", "河北");
    hashtable.put("14", "山西");
    hashtable.put("15", "内蒙古");
    hashtable.put("21", "辽宁");
    hashtable.put("22", "吉林");
    hashtable.put("23", "黑龙江");
    hashtable.put("31", "上海");
    hashtable.put("32", "江苏");
    hashtable.put("33", "浙江");
    hashtable.put("34", "安徽");
    hashtable.put("35", "福建");
    hashtable.put("36", "江西");
    hashtable.put("37", "山东");
    hashtable.put("41", "河南");
    hashtable.put("42", "湖北");
    hashtable.put("43", "湖南");
    hashtable.put("44", "广东");
    hashtable.put("45", "广西");
    hashtable.put("46", "海南");
    hashtable.put("50", "重庆");
    hashtable.put("51", "四川");
    hashtable.put("52", "贵州");
    hashtable.put("53", "云南");
    hashtable.put("54", "西藏");
    hashtable.put("61", "陕西");
    hashtable.put("62", "甘肃");
    hashtable.put("63", "青海");
    hashtable.put("64", "宁夏");
    hashtable.put("65", "新疆");
    hashtable.put("71", "台湾");
    hashtable.put("81", "香港");
    hashtable.put("82", "澳门");
    hashtable.put("91", "国外");
    return hashtable;
  }

  /**
   * 判断字符串是否为数字,0-9重复0次或者多次
   */
  private static boolean isNumeric(String strnum) {
    Pattern pattern = Pattern.compile("[0-9]*");
    Matcher isNum = pattern.matcher(strnum);
    return isNum.matches();
  }

  /**
   * 功能：判断字符串出生日期是否符合正则表达式：包括年月日，闰年、平年和每月31天、30天和闰月的28天或者29天
   */
  private static boolean isDate(String strDate) {
    Pattern pattern = Pattern.compile(
            "^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))?$");
    Matcher m = pattern.matcher(strDate);
    return m.matches();
  }

  /**
   * 判断是否为今天(效率比较高)
   * @param dayTime 时间戳
   * @return true今天 false不是
   */
  public static boolean isToday(long dayTime) {
    Calendar pre = Calendar.getInstance();
    Date predate = new Date(System.currentTimeMillis());
    pre.setTime(predate);

    Calendar cal = Calendar.getInstance();
    Date date = new Date(dayTime);
    cal.setTime(date);

    if (cal.get(Calendar.YEAR) == (pre.get(Calendar.YEAR))) {
      int diffDay = cal.get(Calendar.DAY_OF_YEAR)
              - pre.get(Calendar.DAY_OF_YEAR);
      if (diffDay == 0) {
        return true;
      }
    }
    return false;
  }
  
  /**
   * 用户名验证
   *
   * @author Chenlei
   * created at 2018/9/14
   **/
  public static boolean isUserName(final CharSequence input){
    return isMatch(REGEX_USERNAME,input);
  }

  /**
   * 真实姓名验证
   * @param input 真实姓名
   * @return {@code true}:匹配<br>{@code false}:不匹配
   */
  public static boolean isTureName(final CharSequence input){
    if (input.toString().contains("·") || input.toString().contains("•"))
      return  isMatch(REGEX_TURENAME_FEW,input);
    return isMatch(REGEX_TURENAME,input);
  }
}
