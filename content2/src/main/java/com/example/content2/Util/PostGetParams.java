package com.example.content2.Util;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

public class PostGetParams {
    private static  HashMap bodyParams;
    private static  HashMap urlParams;

    public static Object getParameter(Object aim){
        if(bodyParams!=null)
            return bodyParams.get(aim);
        return urlParams.get(aim);
    }
    public static void clearParams(){
        bodyParams=null;
        urlParams=null;
    }

    public static void setBodyParams(HashMap bodyParams) {
        PostGetParams.bodyParams = bodyParams;
    }

    public static void setUrlParams(HashMap urlParams) {
        PostGetParams.urlParams = urlParams;
    }
    public static HashMap getNotNullHashmap(){
        if (bodyParams!=null){
            return bodyParams;
        }
        return urlParams;
    }
}
