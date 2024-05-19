package com.example.risknarrative.api.company.search;

import java.util.List;

public record Item(
        String companyNumber,
        String companyType,
        String title,
        String companyStatus,
        String dateOfCreation,
        Address address,
        List<Officer> officers
) {
}
