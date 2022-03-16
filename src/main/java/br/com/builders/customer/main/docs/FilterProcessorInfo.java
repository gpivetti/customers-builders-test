package br.com.builders.customer.main.docs;

public class FilterProcessorInfo {
    public static final String DESCRIPTION = "Customer filter criteria in the format: field(:op):value, for " +
            " op = [eq, ne, like, lt, lte, gt, gte]. (Default \"op\" is \"eq\")";

    public static final String EXAMPLE = "filter=id:123&filter=name:like:John" +
            "&filter=document:eq:325637289834&birthdate:lte:2020-01-01";
}
