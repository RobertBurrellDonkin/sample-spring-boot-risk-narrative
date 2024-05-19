package com.example.risknarrative.api.company.search;

public record Officer(
        String name,
        String officerRole,
        String appointedOn,
        Address address) {
}
