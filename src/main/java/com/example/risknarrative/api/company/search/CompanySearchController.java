package com.example.risknarrative.api.company.search;

import com.example.risknarrative.services.company.CompanyService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CompanySearchController {

    private final CompanyService companySearchService;

    public CompanySearchController(CompanyService companyService) {
        this.companySearchService = companyService;
    }

    @PostMapping("/api/company/search")
    public CompanySearchResults search(
            @RequestHeader("x-api-key") String apiKey,
            @RequestBody CompanySearch companySearch) {
        final var items = companySearchService.recordsByCompanyNumber(apiKey, companySearch.companyNumber());
        return new CompanySearchResults(items.size(), items);
    }
}
