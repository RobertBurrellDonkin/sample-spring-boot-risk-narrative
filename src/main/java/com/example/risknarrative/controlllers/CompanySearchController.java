package com.example.risknarrative.controlllers;

import com.example.risknarrative.domain.Company;
import com.example.risknarrative.services.CompanyService;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

        final var companyRecords =
                companyService.search(
                        apiKey,
                        companySearch.companyNumber(),
                        companySearch.companyName(),
                        activeOnly);
        return new CompanySearchResults(companyRecords.size(), companyRecords);
    }

    @JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
    public record CompanySearchRequest(String companyNumber, String companyName) {
    }

    public record CompanySearchResults(int total_results, List<Company> items) {
    }
}
