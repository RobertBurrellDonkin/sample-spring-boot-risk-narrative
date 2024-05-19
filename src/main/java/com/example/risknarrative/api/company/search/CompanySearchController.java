package com.example.risknarrative.api.company.search;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CompanySearchController {

    private final CompanySearchService companySearchService;

    public CompanySearchController(CompanySearchService companySearchService) {
        this.companySearchService = companySearchService;
    }

    @PostMapping("/api/company/search")
    public SearchResults search(@RequestBody CompanySearch companySearch) {
        final var items = companySearchService.searchByNumber(companySearch.companyNumber());
        return new SearchResults(items.size(), items);
    }
}
