package com.example.risknarrative.builders;

import com.example.risknarrative.controlllers.CompanySearchController;

import java.util.ArrayList;
import java.util.List;

public class CompanySearchResultsBuilder {

    private List<CompanyBuilder> itemBuilders = new ArrayList<>();

    public static CompanySearchResultsBuilder searchResults() {
        return new CompanySearchResultsBuilder();
    }

    public CompanySearchResultsBuilder withItems(CompanyBuilder... itemBuilders) {
        this.itemBuilders = List.of(itemBuilders);
        return this;
    }

    public CompanySearchController.CompanySearchResults build() {
        return new CompanySearchController.CompanySearchResults(itemBuilders.size(), itemBuilders.stream().map(CompanyBuilder::build).toList());
    }
}
