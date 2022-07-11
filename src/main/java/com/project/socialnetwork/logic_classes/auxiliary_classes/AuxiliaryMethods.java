package com.project.socialnetwork.logic_classes.auxiliary_classes;

import javax.servlet.http.HttpServletRequest;

public class AuxiliaryMethods {

    public static String createRedirectionToPreviousPage(HttpServletRequest request){
        String refer = request.getHeader("Referer");
        return String.format("redirect:%s",refer);
    }


    public static boolean areSameTypes(Object obj1,Object obj2){
        return obj1.getClass().equals(obj2.getClass());
    }

}
