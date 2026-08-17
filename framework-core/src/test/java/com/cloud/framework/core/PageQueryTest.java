package com.cloud.framework.core;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;

class PageQueryTest {

    @Test
    void shouldUseOneBasedPageNoAndCalculateOffset() {
        PageQuery pageQuery = PageQuery.of(2, 10);

        assertThat(pageQuery.pageNo()).isEqualTo(2);
        assertThat(pageQuery.zeroBasedPageNo()).isEqualTo(1);
        assertThat(pageQuery.pageSize()).isEqualTo(10);
        assertThat(pageQuery.offset()).isEqualTo(10L);
    }

    @Test
    void shouldUseDefaultsOnlyForMissingValues() {
        PageQuery pageQuery = PageQuery.of(null, null);

        assertThat(pageQuery).isEqualTo(PageQuery.defaults());
    }

    @Test
    void shouldRejectInvalidPageNo() {
        assertThatThrownBy(() -> PageQuery.of(0, 20))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("pageNo must be positive");
    }

    @Test
    void shouldRejectPageSizeAboveLimit() {
        assertThatThrownBy(() -> PageQuery.of(1, 201))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("pageSize must be between 1 and 200");
    }

    @Test
    void shouldCreateFromPaginationRequest() {
        PageQueryRequest request = new PageQueryRequest(3, 5);

        assertThat(PageQuery.from(request)).isEqualTo(new PageQuery(3, 5));
    }

    @Test
    void shouldValidatePaginationRequestContract() {
        Validator validator = Validation.byDefaultProvider()
                .configure()
                .messageInterpolator(new ParameterMessageInterpolator())
                .buildValidatorFactory()
                .getValidator();

        assertThat(validator.validate(new PageQueryRequest(0, 201)))
                .extracting(violation -> violation.getPropertyPath().toString())
                .containsExactlyInAnyOrder("pageNo", "pageSize");
    }
}
