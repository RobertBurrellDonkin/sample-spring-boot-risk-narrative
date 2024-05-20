package com.example.risknarrative.controlllers;

import com.example.risknarrative.domain.CompanyRecordsBuilder;

import java.util.ArrayList;
import java.util.List;

public class SearchResultsTestBuilder {

    public static SearchResultsTestBuilder searchResults() {
        return new SearchResultsTestBuilder();
    }

    private List<CompanyRecordsBuilder> itemBuilders = new ArrayList<>();

    public SearchResultsTestBuilder withItems(CompanyRecordsBuilder... itemBuilders) {
        this.itemBuilders = List.of(itemBuilders);
        return this;
    }

    public CompanySearchResults build() {
        return new CompanySearchResults(itemBuilders.size(), itemBuilders.stream().map(CompanyRecordsBuilder::build).toList());
    }
}
