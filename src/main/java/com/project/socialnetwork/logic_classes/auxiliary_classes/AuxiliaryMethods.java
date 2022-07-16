package com.project.socialnetwork.logic_classes.auxiliary_classes;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

public class AuxiliaryMethods {

    public static String createRedirectionToPreviousPage(HttpServletRequest request){
        String refer = request.getHeader("Referer");
        return String.format("redirect:%s",refer);
    }


    public static boolean areSameTypes(Object obj1,Object obj2){
        return obj1.getClass().equals(obj2.getClass());
    }

    public static boolean isMultipartFilesArrayNotEmpty(MultipartFile[] multipartFiles){
        return multipartFiles.length > 0 && !multipartFiles[0].isEmpty();
    }

}
