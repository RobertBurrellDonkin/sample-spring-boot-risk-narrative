package com.example.risknarrative.services;

import com.example.risknarrative.domain.Company;
import com.example.risknarrative.domain.Officer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Component
public class TruProxyWebClient {
    private final WebClient.Builder webClientBuilder;

    @Value("${truproxy.base.url}")
    private String baseUrl;

    public TruProxyWebClient(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    private WebClient webClient() {
        return webClientBuilder
                .baseUrl(baseUrl + "/TruProxyAPI/rest/Companies/v1/")
                .defaultHeader("Accept", APPLICATION_JSON_VALUE)
                .build();
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public List<Company> getCompanies(String apiKey, String searchTerm) {
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
                .block()
                .items();
    }

    public List<Officer> getOfficers(String apiKey, String number) {
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
                .block()
                .items();
    }

    public record CompanyResults(List<Company> items) {
    }

    public record OfficerResults(List<Officer> items) {
    }
}
