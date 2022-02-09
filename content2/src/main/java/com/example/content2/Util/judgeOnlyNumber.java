package com.example.content2.Util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class judgeOnlyNumber {
    static private Log log  = LogFactory.getLog("judgeOnlyNumber");
    static public boolean judgeOnlyNumber(String... s) {
        for (int j = 0; j < s.length; j++) {
            for (int i = 0; i < s[j].length(); i++) {
                char c = s[j].charAt(i);
                if ((c >= '0' && c <= '9') || c == '.')
                    continue;
                else{
                    log.info("没能通过数字验证 : "+s[j]);
                    return false;
                }
            }
        }
        return true;
    }
}
