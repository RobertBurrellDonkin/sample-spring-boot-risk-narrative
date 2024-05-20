package com.example.risknarrative.controlllers;

import com.example.risknarrative.domain.CompanyRecords;

import java.util.List;

public record CompanySearchResults(int total_results, List<CompanyRecords> items) {
}
