package com.example.risknarrative.api.company.search;

import com.example.risknarrative.services.company.CompanyService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import static org.apache.commons.lang3.StringUtils.isBlank;

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
        final var companyRecords = companySearchService.search(
                apiKey,
                isBlank(companySearch.companyNumber()) ? companySearch.companyName() : companySearch.companyNumber());
        return new CompanySearchResults(companyRecords.size(), companyRecords);
    }
}
