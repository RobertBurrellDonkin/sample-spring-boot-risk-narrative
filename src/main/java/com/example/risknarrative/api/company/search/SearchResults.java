package com.example.risknarrative.api.company.search;

import java.util.List;

public record SearchResults(int total_results, List<Item> items) {
}
