package com.example.risknarrative.services.company;

import com.example.risknarrative.domain.CompanyRecords;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

import static com.example.risknarrative.domain.CompanyRecordsBuilder.aCompanyRecord;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Service
public class CompanyService {
    private final WebClient.Builder webClientBuilder;

    @Value("${truproxy.base.url}")
    private String truProxyBaseUrl;

    public CompanyService(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    public List<CompanyRecords> recordsByCompanyNumber(String apiKey, String number) {
        //TODO
        return getCompanyResults(apiKey, number)
                .items()
                .stream()
                .map(company ->
                        aCompanyRecord()
                                .withCompany(company)
                                .withOfficers(getOfficers(apiKey, company.companyNumber()).items()).build())
                .toList();
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
