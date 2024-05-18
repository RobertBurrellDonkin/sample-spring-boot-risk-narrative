package com.example.risknarrative;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.cloud.contract.wiremock.WireMockSpring;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.cloud.contract.wiremock.WireMockSpring.options;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWireMock(port = 0)
@AutoConfigureMockMvc
public class IntegrationTests {

    public static final String COMPANY_SEARCH_URL = "/";
    public static WireMockServer wiremock = new WireMockServer(options().dynamicPort());

    @BeforeAll
    static void setupClass() {
        wiremock.start();
    }

    @AfterEach
    void after() {
        wiremock.resetAll();
    }

    @AfterAll
    static void clean() {
        wiremock.shutdown();
    }

    @Test
    @Disabled
    void searchWithOnlyCompanyName(@Autowired MockMvc mvc) throws Exception {
        mvc.perform(post(COMPANY_SEARCH_URL))
                .andExpect(status().isOk());
    }

    @Test
    @Disabled
    void searchWithOnlyCompanyNumber(@Autowired MockMvc mvc) throws Exception {
        mvc.perform(post(COMPANY_SEARCH_URL))
                .andExpect(status().isOk());
    }

    @Test
    @Disabled
    void searchWithCompanyNumberAndCompanyName(@Autowired MockMvc mvc) throws Exception {
        mvc.perform(post(COMPANY_SEARCH_URL))
                .andExpect(status().isOk());
    }

    @Test
    @Disabled
    void searchWithCompanyNumberShouldIgnoreCompanyName(@Autowired MockMvc mvc) throws Exception {
        mvc.perform(post(COMPANY_SEARCH_URL))
                .andExpect(status().isOk());
    }

    @Test
    @Disabled
    void searchWithoutBodyShouldBeABadRequest(@Autowired MockMvc mvc) throws Exception {
        mvc.perform(post(COMPANY_SEARCH_URL))
                .andExpect(status().isBadRequest());
    }

    @Test
    void searchWithoutAnApiKeyShouldBeForbidden(@Autowired MockMvc mvc) throws Exception {
        mvc.perform(post(COMPANY_SEARCH_URL))
                .andExpect(status().isForbidden());
    }

    @Test
    @Disabled
    void searchWithForbiddenApiKeyShouldBeForbidden(@Autowired MockMvc mvc) throws Exception {
        mvc.perform(post(COMPANY_SEARCH_URL))
                .andExpect(status().isForbidden());
    }

    @Test
    @Disabled
    void searchWithCompanyNumberNotFoundShouldBeNotFound(@Autowired MockMvc mvc) throws Exception {
        mvc.perform(post(COMPANY_SEARCH_URL))
                .andExpect(status().isNotFound());
    }

    // TODO: Check Sanitise Query String
    // Should be a POST, not a GET
}

