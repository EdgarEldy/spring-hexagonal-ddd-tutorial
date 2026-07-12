package com.edgareldy.infrastructure.in.web.dto.common;

import java.math.BigDecimal;

/**
 * Wire representation of the domain's {@code Money} Value Object: amount and currency travel
 * together as a nested JSON object, never flattened into separate scalar fields.
 * <p>
 * Created by edgar.muhamyangabo on 7/12/26
 * Author : edgar.muhamyangabo
 * Date : 7/12/26
 * Project : spring-hexagonal-ddd-tutorial
 */
public record MoneyDto(BigDecimal amount, String currency) {
}
