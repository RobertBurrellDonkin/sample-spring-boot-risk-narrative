package com.example.risknarrative.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.WRITE_ONLY;

public record Officer(
        String name,
        String officerRole,
        String appointedOn,
        @JsonProperty(access = WRITE_ONLY) String resignedOn,
        Address address) {
}
