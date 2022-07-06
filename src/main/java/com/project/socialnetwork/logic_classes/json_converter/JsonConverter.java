package com.project.socialnetwork.logic_classes.json_converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.logging.Level;
import java.util.logging.Logger;

public class JsonConverter {
    private static final Logger logger = Logger.getLogger(JsonConverter.class.getName());

    public static <T> T convertJsonToObject(String json){
        T obj = null;
        ObjectMapper objectMapper = new ObjectMapper();
        TypeReference<T> objType = new TypeReference<>(){};
        try{
            obj = objectMapper.readValue(json,objType);
        }catch (JsonProcessingException exception){
            logger.log(Level.WARNING,"could not convert json to object",exception);
        }
        return obj;
    }

    public static <T> String convertObjectToJson(T obj){
        String json = null;
        ObjectMapper objectMapper = new ObjectMapper();
        try{
            json = objectMapper.writeValueAsString(obj);
        }catch(JsonProcessingException exception){
            logger.log(Level.WARNING,"could not convert object to json",exception);
        }
        return json;
    }
}
