package com.example.risknarrative.builders;

import com.example.risknarrative.controlllers.CompanySearchResults;

import java.util.ArrayList;
import java.util.List;

public class CompanySearchResultsBuilder {

    public static CompanySearchResultsBuilder searchResults() {
        return new CompanySearchResultsBuilder();
    }

    private List<CompanyBuilder> itemBuilders = new ArrayList<>();

    public CompanySearchResultsBuilder withItems(CompanyBuilder... itemBuilders) {
        this.itemBuilders = List.of(itemBuilders);
        return this;
    }

    public CompanySearchResults build() {
        return new CompanySearchResults(itemBuilders.size(), itemBuilders.stream().map(CompanyBuilder::build).toList());
    }
}
