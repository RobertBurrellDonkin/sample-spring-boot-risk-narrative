package com.example.risknarrative.api.company.search;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.*;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Fail.fail;
import static org.springframework.cloud.contract.wiremock.WireMockSpring.options;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWireMock(port = 0)
@AutoConfigureMockMvc
public class EndToEndTest {

    public static final String COMPANY_SEARCH_URL = "/api/company/search";
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
    void searchWithCompanyNumberAndCompanyName(@Autowired MockMvc mvc) throws Exception {
        var expectedSearchResults = """
                {
                    "total_results": 1,
                    "items": [
                        {
                            "company_number": "06500244",
                            "company_type": "ltd",
                            "title": "BBC LIMITED",
                            "company_status": "active",
                            "date_of_creation": "2008-02-11",
                            "address": {
                                "locality": "Retford",
                                "postal_code": "DN22 0AD",
                                "premises": "Boswell Cottage Main Street",
                                "address_line_1": "North Leverton",
                                "country": "England"
                            },
                            "officers": [
                                {
                                    "name": "BOXALL, Sarah Victoria",
                                    "officer_role": "secretary",
                                    "appointed_on": "2008-02-11",
                                    "address": {
                                        "premises": "5",
                                        "locality": "London",
                                        "address_line_1": "Cranford Close",
                                        "country": "England",
                                        "postal_code": "SW20 0DP"
                                    }
                                }
                            ]
                        }
                    ]
                }
                """;
        var actualSearchResults = mvc.perform(
                post(COMPANY_SEARCH_URL)
                        .contentType(APPLICATION_JSON) //TODO: Other Content Types
                        .content("""
                                {
                                    "companyName" : "BBC LIMITED",
                                    "companyNumber" : "06500244"
                                }
                                """))
                .andExpectAll(
                        status().isOk(),
                        content()
                                .contentType(APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();

        JSONAssert.assertEquals(expectedSearchResults, actualSearchResults, JSONCompareMode.STRICT);
    }

    @Test
    @Disabled
    void searchWithOnlyCompanyNumber(@Autowired MockMvc mvc) throws Exception {
        fail("TODO");
    }

    @Test
    @Disabled
    void searchWithOnlyCompanyName(@Autowired MockMvc mvc) throws Exception {
        fail("TODO");
    }

    @Test
    @Disabled
    void searchWithCompanyNumberShouldIgnoreCompanyName(@Autowired MockMvc mvc) throws Exception {
        fail("TODO");
    }

    @Test
    @Disabled
    void searchWithoutBodyShouldBeABadRequest(@Autowired MockMvc mvc) throws Exception {
        fail("TODO");
    }

    @Test
    @Disabled
    void searchWithoutAnApiKeyShouldBeForbidden(@Autowired MockMvc mvc) throws Exception {
        fail("TODO");
    }

    @Test
    @Disabled
    void searchWithForbiddenApiKeyShouldBeForbidden(@Autowired MockMvc mvc) throws Exception {
        fail("TODO");
    }

    @Test
    @Disabled
    void searchWithCompanyNumberNotFoundShouldBeNotFound(@Autowired MockMvc mvc) throws Exception {
        fail("TODO");
    }

    // TODO: Check Sanitise Query String
    // Should be a POST, not a GET
}

