package com.example.risknarrative.services;

import com.example.risknarrative.domain.Company;
import com.example.risknarrative.responsitories.CompanyRepository;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Service
public class CompanyService {
    private final TruProxyWebClient truProxyWebClient;
    private final CompanyRepository companyRepository;

    public CompanyService(TruProxyWebClient truProxyWebClient, CompanyRepository companyRepository) {
        this.truProxyWebClient = truProxyWebClient;
        this.companyRepository = companyRepository;
    }

    public List<Company> search(String apiKey, String companyNumber, String companyName, boolean activeOnly) {
        if (isBlank(companyNumber)) {
            return searchForCompanies(apiKey, activeOnly, companyName);
        } else {
            return companyRepository.findById(companyNumber).map(List::of)
                    .orElseGet(
                            () -> {
                                final var companies = searchForCompanies(apiKey, activeOnly, companyNumber);
                                companyRepository.saveAll(companies);
                                return companies;
                            });
        }
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
