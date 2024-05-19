package com.example.risknarrative.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Address(
        String locality,
        String postalCode,
        String premises,
        @JsonProperty("address_line_1") String addressLine,
        String country) {
}
