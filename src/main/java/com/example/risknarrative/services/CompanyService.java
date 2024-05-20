package com.example.risknarrative.services;

import com.example.risknarrative.domain.Company;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Service
public class CompanyService {
    private final WebClient.Builder webClientBuilder;

    @Value("${truproxy.base.url}")
    private String truProxyBaseUrl;

    public CompanyService(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    public List<Company> search(String apiKey, String companyNumber, String companyName, boolean activeOnly) {
        final var searchTerm = isBlank(companyNumber) ? companyName : companyNumber;
        return getCompanyResults(apiKey, searchTerm)
                .items()
                .stream()
                .filter(activeOnly ? company -> "active".equals(company.getCompanyStatus()) : __ -> true)
                .peek(company -> enrichWithOfficers(apiKey, company))
                .toList();
    }

    private void enrichWithOfficers(String apiKey, Company company) {
        company.setOfficers(
                getOfficers(apiKey, company.getCompanyNumber()).items()
                        .stream()
                        .filter(officer -> isBlank(officer.resignedOn()))
                        .toList());
    }

    private CompanyResults getCompanyResults(String apiKey, String searchTerm) {
        return webClient()
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/Search")
                        .queryParam("Query", searchTerm)
                        .build())
                .header("x-api-key", apiKey)
                .retrieve()
                .bodyToMono(CompanyResults.class)
                .log()
                .block();
    }

    private OfficerResults getOfficers(String apiKey, String number) {
        return webClient()
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/Officers")
                        .queryParam("CompanyNumber", number)
                        .build())
                .header("x-api-key", apiKey)
                .retrieve()
                .bodyToMono(OfficerResults.class)
                .log()
                .block();
    }

    private WebClient webClient() {
        return webClientBuilder
                .baseUrl(truProxyBaseUrl + "/TruProxyAPI/rest/Companies/v1/")
                .defaultHeader("Accept", APPLICATION_JSON_VALUE)
                .build();
    }

    public void setTruProxyBaseUrl(String truProxyBaseUrl) {
        this.truProxyBaseUrl = truProxyBaseUrl;
    }

}
