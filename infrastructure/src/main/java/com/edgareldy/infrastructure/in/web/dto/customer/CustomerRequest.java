package com.edgareldy.infrastructure.in.web.dto.customer;

/**
 * Request body of {@code POST /api/v1/customers}.
 * <p>
 * Created by edgar.muhamyangabo on 7/12/26
 * Author : edgar.muhamyangabo
 * Date : 7/12/26
 * Project : spring-hexagonal-ddd-tutorial
 */
public record CustomerRequest(String firstName, String lastName, String telephone, String email, String address) {
}
