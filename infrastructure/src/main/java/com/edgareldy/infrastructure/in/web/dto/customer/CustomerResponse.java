package com.edgareldy.infrastructure.in.web.dto.customer;

/**
 * Response body for a customer.
 * <p>
 * Created by edgar.muhamyangabo on 7/12/26
 * Author : edgar.muhamyangabo
 * Date : 7/12/26
 * Project : spring-hexagonal-ddd-tutorial
 */
public record CustomerResponse(Long id, String firstName, String lastName, String telephone, String email,
        String address) {
}
