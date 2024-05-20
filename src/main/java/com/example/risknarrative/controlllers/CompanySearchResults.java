package com.example.risknarrative.controlllers;

import com.example.risknarrative.domain.Company;

import java.util.List;

public record CompanySearchResults(int total_results, List<Company> items) {
}
