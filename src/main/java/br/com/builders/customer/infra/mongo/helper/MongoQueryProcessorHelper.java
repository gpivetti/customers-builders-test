package br.com.builders.customer.infra.mongo.helper;

import br.com.builders.customer.commons.dto.FilterDataDTO;
import br.com.builders.customer.commons.dto.PageDataDTO;
import br.com.builders.customer.commons.enums.FilterEnum;
import br.com.builders.customer.main.exceptions.InvalidParameterException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class MongoQueryProcessorHelper {
    private final Query query;

    public MongoQueryProcessorHelper() {
        this.query = new Query();
    }

    public Query getQueries() {
        return this.query;
    }

    public void setQueryByPages(PageDataDTO pageFilters) {
        if (pageFilters == null) return;
        this.query.with(PageRequest.of(pageFilters.getPage(), pageFilters.getSize()));
    }

    public void setQueryBySorting(Sort sorting, Map<String, String> mapFields) {
        if (sorting == null || sorting.isEmpty()) return;
        sorting.forEach(sort -> {
            String realFieldName = mapFields.get(sort.getProperty());
            if (StringUtils.isEmpty(realFieldName)) {
                throw new InvalidParameterException("Field " + sort.getProperty()+ " not allowed as database sorting");
            }
            this.query.with(Sort.by(sort.getDirection(), realFieldName));
        });
    }

    public void setQueryByFilters(List<FilterDataDTO> filters, Map<String, String> mapFields)
            throws InvalidParameterException {
        if (filters == null || filters.isEmpty()) return;
        if (mapFields == null || mapFields.isEmpty()) return;
        filters.forEach(filter -> {
            String realFieldName = mapFields.get(filter.getField());
            if (StringUtils.isEmpty(realFieldName)) {
                throw new InvalidParameterException("Field " + filter.getField() + " not allowed as database filter");
            }
            if (filter.getValue() != null) {
                this.query.addCriteria(
                        this.normalizeCriteria(realFieldName, filter.getFilter(), filter.getValue()));
            }
        });
    }

    private Criteria normalizeCriteria(String fieldName, FilterEnum fieldFilter, Object fieldValue) {
        Criteria newCriteria;
        switch (fieldFilter) {
            case NOT_EQUAL:
                newCriteria = Criteria.where(fieldName).ne(fieldValue);
                break;
            case LIKE:
                if (fieldValue instanceof Date) {
                    throw new InvalidParameterException("Invalid like operation for Date field " + fieldName);
                }
                newCriteria = Criteria.where(fieldName).regex(".*" + fieldValue + ".*", "i");
                break;
            case GREATER_THAN:
                newCriteria = Criteria.where(fieldName).gt(fieldValue);
                break;
            case GREATER_THEN_EQUALS:
                newCriteria = Criteria.where(fieldName).gte(fieldValue);
                break;
            case LESS_THAN:
                newCriteria = Criteria.where(fieldName).lt(fieldValue);
                break;
            case LESS_THEN_EQUAL:
                newCriteria = Criteria.where(fieldName).lte(fieldValue);
                break;
            case EQUAL:
            default:
                newCriteria = Criteria.where(fieldName).is(fieldValue);
                break;
        }
        return newCriteria;
    }
}
