package com.example.risknarrative.api.company.search;

import java.util.ArrayList;
import java.util.List;

public class SearchResultsTestBuilder {

    public static SearchResultsTestBuilder searchResults() {
        return new SearchResultsTestBuilder();
    }

    private List<ItemBuilder> itemBuilders = new ArrayList<>();

    public SearchResultsTestBuilder withItems(ItemBuilder... itemBuilders) {
        this.itemBuilders = List.of(itemBuilders);
        return this;
    }

    public SearchResults build() {
        return new SearchResults(itemBuilders.size(), itemBuilders.stream().map(ItemBuilder::build).toList());
    }
}
