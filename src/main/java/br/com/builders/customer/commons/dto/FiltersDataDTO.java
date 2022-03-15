package br.com.builders.customer.commons.dto;

import br.com.builders.customer.main.exceptions.AppErrorException;
import br.com.builders.customer.main.exceptions.InvalidParameterException;
import lombok.Data;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class FiltersDataDTO<T> {
    private Class<T> filterClass;
    private List<FieldsDataDTO> fields;

    private FiltersDataDTO() {}

    private FiltersDataDTO(Class<T> filterClass, List<FieldsDataDTO> filterFields) {
        this.filterClass = filterClass;
        this.fields = filterFields;
    }

    public static FiltersDataDTO<Void> fromFields(List<FieldsDataDTO> fields) {
        return new FiltersDataDTO<>(null, fields);
    }

    public static <S> FiltersDataDTO<S> fromClassFields(List<FieldsDataDTO> fields, Class<S> source)
            throws AppErrorException, InvalidParameterException {
        try {
            List<Field> classFields = Arrays.asList(source.getDeclaredFields());
            fields = fields.stream().peek(filter -> {
                Field field = findFieldOnClassFields(classFields, filter.getField());
                filter.setField(field.getName());
                if (filter.getValue() instanceof String) {
                    filter.setValue(normalizeFieldValue(field, (String) filter.getValue()));
                }
            }).collect(Collectors.toList());
            return new FiltersDataDTO<>(source, fields);
        } catch (InvalidParameterException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new AppErrorException("Error processing filter ["+  ex.getMessage() + "]");
        }
    }

    private static Field findFieldOnClassFields(List<Field> classFields, String fieldName) {
        return classFields.stream()
                .filter(currentField -> currentField.getName().equals(fieldName))
                .findFirst()
                .orElseThrow(() -> new InvalidParameterException("Field " + fieldName + " not allowed for filter"));
    }

    private static Object normalizeFieldValue(Field field, String filterValue) {
        try {
            Object value;
            String fieldType = field.getType().getSimpleName().toLowerCase();
            switch(fieldType) {
                case "date":
                case "localdate":
                case "localdatetime":
                case "zoneddatetime":
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
                    value = filterValue;
                    break;
                default:
                    throw new InvalidParameterException("Type " + fieldType + " not allowed to filter " + field);
            }
            return value;
        } catch (InvalidParameterException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new InvalidParameterException("Invalid pattern for field " + field);
        }
    }

    private static LocalDate convertStringToDate(String dateString) throws ParseException {
        return LocalDate.parse(dateString, DateTimeFormatter.ofPattern("yyyy-MMM-dd"));
    }
}
