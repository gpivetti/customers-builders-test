package br.com.builders.customer.commons.dto;

import br.com.builders.customer.main.exceptions.InvalidParameterException;
import lombok.Data;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class FiltersDataDTO<T> {
    private Class<T> filterClass;
    private List<FiltersDataFieldsDTO> fields;

    private FiltersDataDTO() {}

    private FiltersDataDTO(Class<T> filterClass, List<FiltersDataFieldsDTO> filterFields) {
        this.filterClass = filterClass;
        this.fields = filterFields;
    }

    public static FiltersDataDTO<Void> of(List<FiltersDataFieldsDTO> fields) {
        return new FiltersDataDTO<>(null, fields);
    }

    public static <S> FiltersDataDTO<S> of(List<FiltersDataFieldsDTO> fields, Class<S> source)
            throws InvalidParameterException {
        try {
            List<Field> classFields = Arrays.asList(source.getDeclaredFields());
            return new FiltersDataDTO<>(
                    source,
                    fields.stream().peek(filter -> {
                        Field field = classFields.stream()
                                .filter(currentField -> currentField.getName().equals(filter.getField()))
                                .findFirst()
                                .orElse(null);
                        if (field == null) {
                            throw new InvalidParameterException("Field " + filter.getField() + " " +
                                    "not allowed for filter");
                        }
                        filter.setField(field.getName());
                        filter.setValue(mapFieldValue(field, (String) filter.getValue()));
                    }).collect(Collectors.toList()));
        } catch (InvalidParameterException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new InvalidParameterException("Error processing filter ["+  ex.getMessage() + "]");
        }
    }

    private static Object mapFieldValue(Field field, String filterValue) {
        try {
            Object value;
            switch(field.getType().getSimpleName().toLowerCase()) {
                case "date":
                    value = convertStringToDate(filterValue);
                    break;
                case "integer":
                case "int":
                    value = Integer.valueOf(filterValue);
                    break;
                case "double":
                case "float":
                    value = Double.valueOf(filterValue);
                    break;
                case "string":
                default:
                    value = filterValue;
                    break;
            }
            return value;
        } catch (Exception e) {
            throw new InvalidParameterException("Invalid pattern for field " + field);
        }
    }

    private static Date convertStringToDate(String dateString) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.parse(dateString);
    }
}
