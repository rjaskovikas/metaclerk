package org.rola.metaclerk.test;

public class StringUtils {

    public static int getWordRepeatCount(String text, String word) {
        int idx = 0, cnt = 0;
        while ((idx = text.indexOf(word, idx)) >= 0) {
            cnt++; idx++;
        }
        return cnt;
    }
}
