package com.example.risknarrative.api.company.search;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.LowerCamelCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(LowerCamelCaseStrategy.class)
public record CompanySearch(String companyNumber, String companyName) {
}
