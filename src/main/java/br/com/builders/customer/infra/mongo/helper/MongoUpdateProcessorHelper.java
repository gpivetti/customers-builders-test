package br.com.builders.customer.infra.mongo.helper;

import br.com.builders.customer.main.exceptions.AppErrorException;
import org.springframework.data.mongodb.core.query.Update;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class MongoUpdateProcessorHelper {
    public static <T> Update buildUpdateByObject(T object) throws AppErrorException {
        if (object == null) {
            throw new AppErrorException("Error on build update: object is null");
        }
        try {
            Map<String, Object> mapAttributes = convertObjectFieldsToMap(object);
            if (mapAttributes.isEmpty()) return null;
            Update update = new Update();
            mapAttributes.forEach(update::set);
            return update;
        } catch (IllegalAccessException ex) {
            throw new AppErrorException(ex);
        }
    }

    private static <T> Map<String, Object> convertObjectFieldsToMap(T object) throws IllegalAccessException {
        Map<String, Object> mapEntity = new HashMap<>();
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field: fields) {
            if (!field.getName().equals("id") && !field.getName().equals("_id")) {
                field.setAccessible(true);
                if (field.get(object) != null) {
                    mapEntity.put(field.getName(), field.get(object));
                }
            }
        }
        return mapEntity;
    }
}
