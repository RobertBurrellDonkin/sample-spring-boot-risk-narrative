package com.example.risknarrative.controlllers;

import com.example.risknarrative.services.CompanyService;
import org.springframework.web.bind.annotation.*;

import static org.apache.commons.lang3.StringUtils.isBlank;

@RestController
public class CompanySearchController {

    private final CompanyService companyService;

    public CompanySearchController(final CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping("/api/company/search")
    public CompanySearchResults search(
            @RequestHeader("x-api-key") String apiKey,
            @RequestParam(name = "Active", required = false) boolean activeOnly,
            @RequestBody CompanySearchRequest companySearch) {
        final var companyRecords = companyService.search(
                apiKey,
                isBlank(companySearch.companyNumber()) ? companySearch.companyName() : companySearch.companyNumber(), activeOnly);
        return new CompanySearchResults(companyRecords.size(), companyRecords);
    }
}
