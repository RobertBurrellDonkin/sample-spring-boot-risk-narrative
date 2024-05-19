package com.example.risknarrative.domain;

import java.util.List;

public record CompanyRecords(
        String companyNumber,
        String companyType,
        String title,
        String companyStatus,
        String dateOfCreation,
        Address address,
        List<Officer> officers
) {
}
