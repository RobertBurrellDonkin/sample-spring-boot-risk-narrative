package com.example.risknarrative.services;

import com.example.risknarrative.domain.Company;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Service
public class CompanyService {
    private final TruProxyWebClient truProxyWebClient;

    public CompanyService(TruProxyWebClient truProxyWebClient) {
        this.truProxyWebClient = truProxyWebClient;
    }

    public List<Company> search(String apiKey, String companyNumber, String companyName, boolean activeOnly) {
        final var searchTerm = isBlank(companyNumber) ? companyName : companyNumber;
        return searchForCompanies(apiKey, activeOnly, searchTerm);
    }

    private List<Company> searchForCompanies(String apiKey, boolean activeOnly, String searchTerm) {
        return truProxyWebClient.getCompanies(apiKey, searchTerm)
                .stream()
                .filter(activeOnly ? company -> "active".equals(company.getCompanyStatus()) : __ -> true)
                .peek(company -> enrichWithOfficers(apiKey, company))
                .toList();
    }

    private void enrichWithOfficers(String apiKey, Company company) {
        company.setOfficers(
                truProxyWebClient.getOfficers(apiKey, company.getCompanyNumber())
                        .stream()
                        .filter(officer -> isBlank(officer.getResignedOn()))
                        .toList());
    }
}
