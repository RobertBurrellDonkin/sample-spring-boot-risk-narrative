package com.example.risknarrative.domain;

public record Company(
        String companyNumber,
        String companyType,
        String title,
        String companyStatus,
        String dateOfCreation,
        Address address
) {
}
