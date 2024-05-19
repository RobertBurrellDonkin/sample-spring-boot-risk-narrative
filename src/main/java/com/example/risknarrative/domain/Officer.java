package com.example.risknarrative.domain;

public record Officer(
        String name,
        String officerRole,
        String appointedOn,
        Address address) {
}
