package com.example.risknarrative.controlllers;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.LowerCamelCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(LowerCamelCaseStrategy.class)
public record CompanySearchRequest(String companyNumber, String companyName) {
}
