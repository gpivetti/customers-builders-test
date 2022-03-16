package br.com.builders.customer.commons.dto;

import br.com.builders.customer.main.exceptions.AppErrorException;
import br.com.builders.customer.main.exceptions.InvalidParameterException;
import lombok.Data;
import org.springframework.data.domain.Sort;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class FieldsDataDTO<T> {
    private Class<T> fieldsClass;
    private Sort sorting;
    private List<FilterDataDTO> filters;

    private FieldsDataDTO() {}

    private FieldsDataDTO(Class<T> filterClass, Sort sorting, List<FilterDataDTO> filterFields) {
        this.fieldsClass = filterClass;
        this.sorting = sorting;
        this.filters = filterFields;
    }

    public static <S> FieldsDataDTO<S> fromClass(List<FilterDataDTO> fields, Sort sorting, Class<S> sourceClass)
            throws AppErrorException, InvalidParameterException {
        try {
            List<Field> classFields = Arrays.asList(sourceClass.getDeclaredFields());
            validateSortingByClass(sorting, classFields);
            return new FieldsDataDTO<>(sourceClass, sorting, normalizeFields(fields, classFields));
        } catch (InvalidParameterException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new AppErrorException("Error processing filter ["+  ex.getMessage() + "]");
        }
    }

    private static List<FilterDataDTO> normalizeFields(List<FilterDataDTO> fields, List<Field> classFields) {
        if (fields == null || fields.isEmpty()) return fields;
        return fields.stream().peek(filter -> {
            Field field = findFieldOnClassFields(classFields, filter.getField());
            filter.setField(field.getName());
            if (filter.getValue() instanceof String) {
                filter.setValue(normalizeFieldValue(field, (String) filter.getValue()));
            }
        }).collect(Collectors.toList());
    }

    private static void validateSortingByClass(Sort sorting, List<Field> classFields) {
        if (sorting == null || sorting.isEmpty()) return;
        sorting.stream().forEach(sort -> {
            findFieldOnClassFields(classFields, sort.getProperty());
        });
    }

    private static Field findFieldOnClassFields(List<Field> classFields, String fieldName) {
        return classFields.stream()
                .filter(currentField -> currentField.getName().equals(fieldName))
                .findFirst()
                .orElseThrow(() -> new InvalidParameterException("Field " + fieldName + " not allowed for " +
                        "filter and sorting"));
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
