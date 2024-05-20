package com.example.risknarrative.controlllers;

import com.example.risknarrative.domain.CompanyBuilder;

import java.util.ArrayList;
import java.util.List;

public class SearchResultsTestBuilder {

    public static SearchResultsTestBuilder searchResults() {
        return new SearchResultsTestBuilder();
    }

    private List<CompanyBuilder> itemBuilders = new ArrayList<>();

    public SearchResultsTestBuilder withItems(CompanyBuilder... itemBuilders) {
        this.itemBuilders = List.of(itemBuilders);
        return this;
    }

    public CompanySearchResults build() {
        return new CompanySearchResults(itemBuilders.size(), itemBuilders.stream().map(CompanyBuilder::build).toList());
    }
}
