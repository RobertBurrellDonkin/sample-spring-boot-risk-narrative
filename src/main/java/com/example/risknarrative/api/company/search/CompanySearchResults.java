package com.example.risknarrative.api.company.search;

import com.example.risknarrative.domain.CompanyRecords;

import java.util.List;

public record CompanySearchResults(int total_results, List<CompanyRecords> items) {
}
