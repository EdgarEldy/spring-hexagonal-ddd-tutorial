package com.edgareldy.domain.model.customer;

import com.edgareldy.domain.model.shared.Email;

import java.util.Objects;

/**
 * Entity representing a customer. Identity is carried by {@code id}, not by field values.
 * <p>
 * Created by edgar.muhamyangabo on 7/11/26
 * Author : edgar.muhamyangabo
 * Date : 7/11/26
 * Project : spring-hexagonal-ddd-tutorial
 */
public final class Customer {

    private final Long id;
    private final String firstName;
    private final String lastName;
    private final String telephone;
    private final Email email;
    private final String address;

    private Customer(Long id, String firstName, String lastName, String telephone, Email email, String address) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.telephone = telephone;
        this.email = email;
        this.address = address;
    }

    public static Customer create(String firstName, String lastName, String telephone, Email email,
            String address) {
        return new Customer(null, requireNotBlank(firstName, "firstName"), requireNotBlank(lastName, "lastName"),
                requireNotBlank(telephone, "telephone"), Objects.requireNonNull(email, "email must not be null"),
                requireNotBlank(address, "address"));
    }

    public static Customer reconstitute(Long id, String firstName, String lastName, String telephone, Email email,
            String address) {
        Objects.requireNonNull(id, "id must not be null");
        return new Customer(id, requireNotBlank(firstName, "firstName"), requireNotBlank(lastName, "lastName"),
                requireNotBlank(telephone, "telephone"), Objects.requireNonNull(email, "email must not be null"),
                requireNotBlank(address, "address"));
    }

    private static String requireNotBlank(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " must not be blank");
        }
        return value;
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getTelephone() {
        return telephone;
    }

    public Email getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Customer other)) {
            return false;
        }
        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
