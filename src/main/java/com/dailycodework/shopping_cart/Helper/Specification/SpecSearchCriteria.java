package com.dailycodework.shopping_cart.Helper.Specification;

import lombok.*;

import static com.dailycodework.shopping_cart.Helper.Specification.SearchOperation.OR_PREDICATE_FLAG;

@Getter
@Setter
//@AllArgsConstructor
//@NoArgsConstructor
public class SpecSearchCriteria {
    private String key;
    private SearchOperation operation;
    private Object value;
    private Boolean orPredicate;
    public SpecSearchCriteria(final String key, final SearchOperation operation, final Object value) {
        super();
        this.key = key;
        this.operation = operation;
        this.value = value;
    }

    public SpecSearchCriteria(final String orPredicate, final String key, final SearchOperation operation, final Object value) {
        super();
        this.orPredicate = orPredicate != null && orPredicate.equals(OR_PREDICATE_FLAG);
        this.key = key;
        this.operation = operation;
        this.value = value;
    }
    public SpecSearchCriteria(String key, String operation, String value, String prefix, String suffix) {
        SearchOperation oper = SearchOperation.getSimpleOperation(operation.charAt(0));
        //kiem tra xem co phai la orPredicate hay khong
        if (oper != null) {
            if (oper == SearchOperation.EQUALITY) {
                final boolean startWithAsterisk = prefix != null && prefix.contains(SearchOperation.ZERO_OR_MORE_REGEX);
                final boolean endWithAsterisk = suffix != null && suffix.contains(SearchOperation.ZERO_OR_MORE_REGEX);
                if (startWithAsterisk && endWithAsterisk) {
                    oper = SearchOperation.CONTAINS;
                } else if (startWithAsterisk) {
                    oper = SearchOperation.ENDS_WITH;
                } else if (endWithAsterisk) {
                    oper = SearchOperation.STARTS_WITH;
                }
            }
        }
        this.key = key;
        this.operation = oper;
        this.value = value;
    }
}
