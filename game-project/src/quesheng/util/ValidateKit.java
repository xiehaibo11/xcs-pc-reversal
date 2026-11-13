/*
 * Decompiled with CFR 0.152.
 */
package quesheng.util;

import java.util.Collection;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ValidateKit {
    public static final boolean isNull(Object[] objs) {
        return objs == null || objs.length == 0;
    }

    public static final boolean isNull(Integer integer) {
        return integer == null || integer == 0;
    }

    public static final boolean isNull(Collection collection) {
        return collection == null || collection.isEmpty();
    }

    public static final boolean isNull(Map map) {
        return map == null || map.isEmpty();
    }

    public static final boolean isNull(String str) {
        return str == null || "".equals(str.trim()) || "null".equals(str.toLowerCase());
    }

    public static final boolean isNull(Long longs) {
        return longs == null || longs == 0L;
    }

    public static final boolean isNotNull(Long longs) {
        return !ValidateKit.isNull(longs);
    }

    public static final boolean isNotNull(String str) {
        return !ValidateKit.isNull(str);
    }

    public static final boolean isNotNull(Collection collection) {
        return !ValidateKit.isNull(collection);
    }

    public static final boolean isNotNull(Map map) {
        return !ValidateKit.isNull(map);
    }

    public static final boolean isNotNull(Integer integer) {
        return !ValidateKit.isNull(integer);
    }

    public static final boolean isNotNull(Object[] objs) {
        return !ValidateKit.isNull(objs);
    }

    public static final boolean isInteger(String str) {
        return str.matches("[0-9]+");
    }

    public static final boolean isIntEqZero(int num) {
        return num == 0;
    }

    public static final boolean isIntGtZero(int num) {
        return num > 0;
    }

    public static final boolean isIntGteZero(int num) {
        return num >= 0;
    }

    public static final boolean isFloatEqZero(float num) {
        return num == 0.0f;
    }

    public static final boolean isFloatGtZero(float num) {
        return num > 0.0f;
    }

    public static final boolean isFloatGteZero(float num) {
        return num >= 0.0f;
    }

    public static String stringFilter(String text) {
        String regExpr = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~\uff01@#\uffe5%\u2026\u2026&*\uff08\uff09\u2014\u2014+|{}\u3010\u3011\u2018\uff1b\uff1a\u201d\u201c\u2019\u3002\uff0c\u3001\uff1f]";
        Pattern p = Pattern.compile(regExpr);
        Matcher m = p.matcher(text);
        return m.replaceAll("").trim();
    }

    public static String htmlFilter(String inputString) {
        String htmlStr = inputString;
        String textStr = "";
        try {
            String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>";
            String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>";
            String regEx_html = "<[^>]+>";
            String patternStr = "\\s+";
            Pattern p_script = Pattern.compile(regEx_script, 2);
            Matcher m_script = p_script.matcher(htmlStr);
            htmlStr = m_script.replaceAll("");
            Pattern p_style = Pattern.compile(regEx_style, 2);
            Matcher m_style = p_style.matcher(htmlStr);
            htmlStr = m_style.replaceAll("");
            Pattern p_html = Pattern.compile(regEx_html, 2);
            Matcher m_html = p_html.matcher(htmlStr);
            htmlStr = m_html.replaceAll("");
            Pattern p_ba = Pattern.compile(patternStr, 2);
            Matcher m_ba = p_ba.matcher(htmlStr);
            textStr = htmlStr = m_ba.replaceAll("");
        }
        catch (Exception e) {
            System.err.println("Html2Text: " + e.getMessage());
        }
        return textStr;
    }
}

