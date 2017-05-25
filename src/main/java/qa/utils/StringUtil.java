package qa.utils;

import qa.exception.RunException;

import java.io.File;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.util.Random;

/**
 * Created by ${chris.li} on ${2015-10-30}.
 */
public class StringUtil {

    public static final String formatStr = "0.00##########"; //小数点后保留两位

    /**
     * 格式化数字  number*multiplyNum后取小数点后scale位，并以bigDecimalRoud方式进位，最后以format格式化
     * @param number  例：1.25
     * @param multiplyNum 例：1000（可以为空）
     * @param scale 例：2（可以为空）
     * @param roundingMode 例：RoundingMode.HALF_UP（scale必须存在;可以为空）
     * @param format 例：0.##（可以为空）
     * @return String
     */
    public static String strNumberFormat(String number,String multiplyNum, Integer scale, RoundingMode roundingMode, String format){
        String returnNum = number;
        if(StringUtil.isEmpty(number)) {
            return returnNum;
        }
        try {
            BigDecimal bigDecimal = new BigDecimal(number.trim());
            if (!StringUtil.isEmpty(multiplyNum)) {
                bigDecimal = bigDecimal.multiply(new BigDecimal(multiplyNum));
            }
            if (scale!=null && roundingMode!=null) {
                bigDecimal = bigDecimal.setScale(scale, roundingMode);
            }else if(scale!=null) {
                bigDecimal = bigDecimal.setScale(scale);
            }
            returnNum = bigDecimal.toString();
            if (!StringUtil.isEmpty(format)) {
                DecimalFormat d = new DecimalFormat(format);
                returnNum = d.format(bigDecimal);
            }
            return returnNum;
        }catch (Exception e) {
            return returnNum;
        }
    }

    /**
     * 判断字符串是否为空
     * @param str 字符串
     * @return true||false
     */
    public static boolean isEmpty(String str) {
        if (str==null || str.isEmpty() || str.trim().isEmpty()){
            return true;
        }else {
            return  false;
        }
    }

    /**
     * 判断是否为数字
     * @param str
     * @return
     */
    public static boolean isNumber(String str) {
        if(StringUtil.isEmpty(str)) {
            return false;
        }
        try {
            BigDecimal bigDecimal = new BigDecimal(str.trim());
            if(bigDecimal!=null) {
                return true;
            }
            return false;
        }catch (Exception e) {
            return false;
        }
    }

    /**
     * 判断num1是否大于num2
     * @param num1 字符串1
     * @param num2 字符串2
     * @return true||false
     */
    public static boolean biggerThan(String num1, String num2) {
        try {
            String regexStr = "^(\\d{1,3}(,\\d\\d\\d)*(\\.\\d+)?)$";
            if(!StringUtil.isEmpty(num1) && RegExp.matchRegExp(num1, regexStr) && num1.contains(",")) {
                num1 = num1.replace(",", "");
            }
            if(!StringUtil.isEmpty(num2) && RegExp.matchRegExp(num2, regexStr) && num2.contains(",")) {
                num1 = num1.replace(",", "");
            }
            if(new BigDecimal(num1).compareTo(new BigDecimal(num2))>0) {
                return true;
            }else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 判断num1是否大于num2
     * @param num1 字符串1
     * @param num2 字符串2
     * @return true||false
     */
    public static boolean equalValue(String num1, String num2) {
        try {
            String regexStr = "^(\\d{1,3}(,\\d\\d\\d)*(\\.\\d+)?)$";
            if(!StringUtil.isEmpty(num1) && RegExp.matchRegExp(num1, regexStr) && num1.contains(",")) {
                num1 = num1.replace(",", "");
            }
            if(!StringUtil.isEmpty(num2) && RegExp.matchRegExp(num2, regexStr) && num2.contains(",")) {
                num1 = num1.replace(",", "");
            }

            if(new BigDecimal(num1).compareTo(new BigDecimal(num2))==0) {
                return true;
            }else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 格式化数字(保留有效位)  number*multiplyNum后取小数点后scale位，并以roundingMode方式进位，最后以format格式化
     * 特别说明：如果number*multiplyNum后结果为0.02356，那么取值进2位四舍五入后值为0.024
     * @param number  例：1.25
     * @param multiplyNum 例：1000（可以为空）
     * @param scale 例：2（可以为空）
     * @param roundingMode 例：RoundingMode.HALF_UP（scale必须存在;可以为空）
     * @param format 例：0.##（可以为空）
     * @return String
     */
    public static String strNumberValidFormat(String number,String multiplyNum, Integer scale, RoundingMode roundingMode, String format) {
        String returnNum = number;
        if(StringUtil.isEmpty(number)) {
            return returnNum;
        }
        try {
            BigDecimal bigDecimal = new BigDecimal(number.trim());
            if (!StringUtil.isEmpty(multiplyNum)) {
                bigDecimal = bigDecimal.multiply(new BigDecimal(multiplyNum));
            }
            if(scale!=null) {
                if(bigDecimal.compareTo(BigDecimal.ONE)<0 && bigDecimal.compareTo(BigDecimal.ZERO)>0) {
                    bigDecimal = StringUtil.toFormatValidNum(bigDecimal, scale, roundingMode);
                }else {
                    if (roundingMode!=null) {
                        bigDecimal = bigDecimal.setScale(scale, roundingMode);
                    }else{
                        bigDecimal = bigDecimal.setScale(scale);
                    }
                }
            }
            returnNum = bigDecimal.toString();
            if (!StringUtil.isEmpty(format)) {
                DecimalFormat d = new DecimalFormat(format);
                returnNum = d.format(bigDecimal);
            }
            return returnNum;
        }catch (Exception e) {
            return returnNum;
        }
    }

    /**
     * 返回对象的有效数字
     * @param obj BigDecimal
     * @param length 长度
     * @return BigDecimal
     */
    public static BigDecimal toFormatValidNum(BigDecimal obj, int length, RoundingMode roundingMode) {
        try {
            if(obj!=null && length>0) {
                BigDecimal divisor = BigDecimal.ONE;
                MathContext mc = new MathContext(length);
                if(roundingMode!=null) {
                    mc = new MathContext(length, roundingMode);
                }
                return obj.divide(divisor, mc);
            }
            return new BigDecimal("0");
        }catch (Exception e) {
            return new BigDecimal("0");
        }
    }

    /**
     * 两数相除
     * @param num1 数字1
     * @param num2 数字2
     * @param scale scale of the {@code BigDecimal} quotient to be returned.
     * @param roundingMode rounding mode to apply.
     * @return BigDecimal
     */
    public static BigDecimal divide(String num1, String num2, int scale, RoundingMode roundingMode) {
        try {
            BigDecimal b1 = new BigDecimal(num1);
            BigDecimal b2 = new BigDecimal(num2);
            return  b1.divide(b2, scale, roundingMode);
        } catch (Exception e) {
            return  new BigDecimal("0");
        }
    }

    /**
     * 两数相除减
     * @param num1 数字1
     * @param num2 数字2
     * @return BigDecimal
     */
    public static BigDecimal subtract(String num1, String num2) {
        try {
            if(StringUtil.isEmpty(num1) || StringUtil.isEmpty(num2)) {
                return  new BigDecimal("0");
            }
            BigDecimal b1 = new BigDecimal(num1);
            BigDecimal b2 = new BigDecimal(num2);
            return  b1.subtract(b2);
        } catch (Exception e) {
            return  new BigDecimal("0");
        }
    }

    public static Object isNumericAndTransformDouble(Object obj){
        String str = String.valueOf(obj);
        if (RegExp.isNumeric(str)){
            Double d = Double.parseDouble(str);
            return d;
        }else {
            return obj;
        }
    }


    public static String trim(String str) {
        if(str==null) {
            return null;
        }else {
            return str.trim();
        }
    }

    public static boolean matchValues(String expectedValue, String actValue) {
        if((expectedValue==null && actValue==null)
                || ("".equals(StringUtil.trim(expectedValue)) && "".equals(StringUtil.trim(actValue)))
                || (!StringUtil.isEmpty(expectedValue) && !StringUtil.isEmpty(actValue)
                    && StringUtil.trim(expectedValue).equals(StringUtil.trim(actValue))) ) {
            //获取页面的数据和数据库比较,相同则成功
            return true;
        }
        return false;
    }

    public static Integer strToInt(String str) {
        try {
            return new Integer(str);
        } catch (Exception e) {
            return null;
        }
    }

    public static String objectToString(Object obj) {
        try {
            if(obj==null) {
                return  null;
            }
            if(obj instanceof Integer) {
                return new BigDecimal((Integer)obj).toString();
            }else if(obj instanceof Float) {
                return new BigDecimal((Float)obj).toString();
            }else if(obj instanceof Double) {
                if(StringUtil.equalValue(String.valueOf(obj), new BigDecimal((Double)obj).toString())) {
                    return new BigDecimal((Double)obj).toString();
                }else {
                    return String.valueOf(obj);
                }
            }else {
                return obj.toString();
            }
        } catch (Exception e) {
            return obj.toString();
        }
    }

    public static String trimAllSpace(String str) {
        if(str==null) {
            return str;
        }else {
            return str.replace(" ", "").replace("\n", "").replace("\r", "");
        }
    }

    public static String addZeroToIntStr(Integer num, int zeroNum){
        if (num == null){
            return null;
        }else if(zeroNum<=0){
            return num+"";
        }else {
            String numStr = num + "";
            int numLength = numStr.length();
            if (zeroNum > numLength){
                for(int i=0; i<(zeroNum-numLength); i++){
                    numStr = "0" + numStr;
                }
            }
            return numStr;
        }
    }

    public static String getFileSha1(File file) {
        if (!file.isFile()) {
            return null;
        }
        MessageDigest digest = null;
        FileInputStream in = null;
        byte buffer[] = new byte[8192];
        int len;
        try {
            digest =MessageDigest.getInstance("SHA-1");
            in = new FileInputStream(file);
            while ((len = in.read(buffer)) != -1) {
                digest.update(buffer, 0, len);
            }
            BigInteger bigInt = new BigInteger(1, digest.digest());
            return bigInt.toString(16);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                in.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static String urlEncoder(String str, String enc) throws RunException {
        try {
            return URLEncoder.encode(str,enc);
        } catch (UnsupportedEncodingException e) {
            throw new RunException(e);
        }
    }
    public static String urlEncoderUTF8(String str) throws RunException {
        try {
            return URLEncoder.encode(str,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RunException(e);
        }
    }
    /**
     * URL 解码
     */
    public static String getURLDecoderString(String str) {
        String result = "";
        if (null == str) {
            return "";
        }
        try {
            result = java.net.URLDecoder.decode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }
    /*
     * 返回长度为【strLength】的随机数，在前面补0
     */
    public static String getFixLenthString(int strLength) {

        Random rm = new Random();

        // 获得随机数
        double pross = (1 + rm.nextDouble()) * Math.pow(10, strLength);

        // 将获得的获得随机数转化为字符串
        String fixLenthString = new BigDecimal(pross).toPlainString();

        // 返回固定的长度的随机数
        return fixLenthString.substring(1, strLength + 1);
    }

    public static synchronized String createFixLenthString(int length) {
        String str = "";
        String value = "1";
        for (int i=0;i<length;i++){
            str += value;
        }
        return str;
    }

    public static  void  main(String[] args) throws RunException {
//        Object a = isNumericAndTransformDouble("2");
//        String a = createFixLenthString(4500);
//        System.out.println(a);

//        System.out.println(urlEncoder("{\"data\":{\"applyCode\":\"AO20170330000001\", \"loanAmount\":2000.44, \"loanDate\":1459845047000, \"peroidValue\":12, \"instalmentPlanId\":\"402881e55914ca00015914e3e0ee000a\"}}"));
//        System.out.println(true&&true);
//        System.out.println("dd".replace("123", "456"));
//        Object obj = 123456789.12d;
//        System.out.println((String)obj);

//        String result = "{...}data[0].pr[0].value{%}";
//        System.out.println(result.replaceFirst("^\\{(.*)\\}$", ""));
//        System.out.println(RegExp.matchRegExp(result, "^\\{(.*)\\}$"));

//        System.out.println(new BigDecimal(102.81d).toString());
//        System.out.println(new BigDecimal(102d).toString());
//        System.out.println(String.valueOf(102.81d));
//        System.out.println(String.valueOf(102d));
//        System.out.println(StringUtil.equalValue(String.valueOf(102.81d), new BigDecimal((Double)102.81d).toString()));

//        try {
//            String aa = DigestUtils.md5Hex(new FileInputStream(new File("E:\\envisionTest\\Apollo-OS\\compare\\HistoricalAlarm_20160323171015.csv")));
//            String bb = DigestUtils.md5Hex(new FileInputStream(new File("E:\\envisionTest\\Apollo-OS\\compare\\HistoricalAlarm_20160323171953.csv")));
//            String cc = DigestUtils.md5Hex(new FileInputStream(new File("E:\\envisionTest\\Apollo-OS\\compare\\HistoricalAlarm_20160328150016.csv")));
//            System.out.println(aa);
//            System.out.println(bb);
//            System.out.println(cc);
//            System.out.println(aa.equals(bb));
//            System.out.println(aa.equals(cc));
//
//            String ss1 = getFileSha1(new File("E:\\envisionTest\\Apollo-OS\\compare\\HistoricalAlarm_20160323171015.csv"));
//            String ss2 = getFileSha1(new File("E:\\envisionTest\\Apollo-OS\\compare\\HistoricalAlarm_20160323171953.csv"));
//            String ss22 = getFileSha1(new File("E:\\envisionTest\\Apollo-OS\\compare\\HistoricalAlarm_20160328150016.csv"));
//            System.out.println(ss1);
//            System.out.println(ss2);
//            System.out.println(ss22);
//            System.out.println(ss1.equals(ss2));
//            System.out.println(ss1.equals(ss22));
//
//            String aa1 = DigestUtils.md5Hex(new FileInputStream(new File("E:\\envisionTest\\Apollo-OS\\compare\\HistoricalAlarm_20160323175050.xls")));
//            String bb1 = DigestUtils.md5Hex(new FileInputStream(new File("E:\\envisionTest\\Apollo-OS\\compare\\HistoricalAlarm_20160323175247.xls")));
//            String cc1 = DigestUtils.md5Hex(new FileInputStream(new File("E:\\envisionTest\\Apollo-OS\\compare\\HistoricalAlarm_20160328150352.xls")));
//            System.out.println(aa1);
//            System.out.println(bb1);
//            System.out.println(cc1);
//            System.out.println(aa1.equals(bb1));
//            System.out.println(aa1.equals(cc1));
//
//            String ss3 = getFileSha1(new File("E:\\envisionTest\\Apollo-OS\\compare\\HistoricalAlarm_20160323175050.xls"));
//            String ss4 = getFileSha1(new File("E:\\envisionTest\\Apollo-OS\\compare\\HistoricalAlarm_20160323175247.xls"));
//            String ss44 = getFileSha1(new File("E:\\envisionTest\\Apollo-OS\\compare\\HistoricalAlarm_20160328150352.xls"));
//            System.out.println(ss3);
//            System.out.println(ss4);
//            System.out.println(ss44);
//            System.out.println(ss3.equals(ss4));
//            System.out.println(ss3.equals(ss44));
//
//            String aa2 = DigestUtils.md5Hex(new FileInputStream(new File("E:\\envisionTest\\Apollo-OS\\compare\\HistoricalAlarm_20160323175124.pdf")));
//            String bb2 = DigestUtils.md5Hex(new FileInputStream(new File("E:\\envisionTest\\Apollo-OS\\compare\\HistoricalAlarm_20160323175137.pdf")));
//            System.out.println(aa2);
//            System.out.println(bb2);
//            System.out.println(aa2.equals(bb2));
//
//            String ss5 = getFileSha1(new File("E:\\envisionTest\\Apollo-OS\\compare\\HistoricalAlarm_20160323175124.pdf"));
//            String ss6 = getFileSha1(new File("E:\\envisionTest\\Apollo-OS\\compare\\HistoricalAlarm_20160323175137.pdf"));
//            System.out.println(ss5);
//            System.out.println(ss6);
//            System.out.println(ss5.equals(ss6));
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }


}
