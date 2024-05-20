package com.example.risknarrative;

import com.example.risknarrative.services.TruProxyWebClient;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.MappingBuilder;
import com.github.tomakehurst.wiremock.matching.RequestPatternBuilder;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.web.servlet.MockMvc;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Fail.fail;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;
import static org.skyscreamer.jsonassert.JSONCompareMode.STRICT;
import static org.springframework.cloud.contract.wiremock.WireMockSpring.options;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWireMock(port = 0)
@AutoConfigureMockMvc
public class EndToEndTest {

    public static final String TRU_PROXY_API = "/TruProxyAPI/rest/Companies/v1/";
    public static final String TRU_PROXY_SEARCH_PATH = TRU_PROXY_API + "Search";
    public static final String TRU_PROXY_OFFICERS_PATH = TRU_PROXY_API + "Officers";
    public static final String X_API_KEY = "x-api-key";

    public static final String COMPANY_SEARCH_URL = "/api/company/search";
    public static WireMockServer wiremock = new WireMockServer(options().dynamicPort());

    @Autowired
    TruProxyWebClient truProxyWebClient;

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


    @BeforeEach
    void setUpMocks() {
        truProxyWebClient.setBaseUrl(wiremock.baseUrl());
    }


    @Test
    void searchWithCompanyNumberAndCompanyName(@Autowired MockMvc mvc) throws Exception {
        var query = """
                {
                    "companyName" : "BBC LIMITED",
                    "companyNumber" : "06500244"
                }
                """;

        var companyNumber = "06500244";
        givenTruProxySearchWilLReturn(
                companyNumber,
                """
                         {
                           "page_number": 1,
                           "kind": "search#companies",
                           "total_results": 20,
                           "items": [
                               {
                                   "company_status": "active",
                                   "address_snippet": "Boswell Cottage Main Street, North Leverton, Retford, England, DN22 0AD",
                                   "date_of_creation": "2008-02-11",
                                   "matches": {
                                       "title": [
                                           1,
                                           3
                                       ]
                                   },
                                   "description": "06500244 - Incorporated on 11 February 2008",
                                   "links": {
                                       "self": "/company/06500244"
                                   },
                                   "company_number": "06500244",
                                   "title": "BBC LIMITED",
                                   "company_type": "ltd",
                                   "address": {
                                       "premises": "Boswell Cottage Main Street",
                                       "postal_code": "DN22 0AD",
                                       "country": "England",
                                       "locality": "Retford",
                                       "address_line_1": "North Leverton"
                                   },
                                   "kind": "searchresults#company",
                                   "description_identifier": [
                                       "incorporated-on"
                                   ]
                               }]
                         }
                        """);


        givenTruProxyGetOfficersWillReturn(
                companyNumber,
                """
                                {
                                    "etag": "6dd2261e61776d79c2c50685145fac364e75e24e",
                                    "links": {
                                        "self": "/company/10241297/officers"
                                    },
                                    "kind": "officer-list",
                                    "items_per_page": 35,
                                    "items": [
                                        {
                                            "address": {
                                                "premises": "The Leeming Building",
                                                "postal_code": "LS2 7JF",
                                                "country": "England",
                                                "locality": "Leeds",
                                                "address_line_1": "Vicar Lane"
                                            },
                                            "name": "ANTLES, Kerri",
                                            "appointed_on": "2017-04-01",
                                            "officer_role": "director",
                                            "links": {
                                                "officer": {
                                                    "appointments": "/officers/4R8_9bZ44w0_cRlrxoC-wRwaMiE/appointments"
                                                }
                                            },
                                            "date_of_birth": {
                                                "month": 6,
                                                "year": 1969
                                            },
                                            "occupation": "Finance And Accounting",
                                            "country_of_residence": "United States",
                                            "nationality": "American"
                                        }]
                                  }
                        """);


        var apiKey = "some-api-key";
        var actualSearchResults = searchAll(mvc, apiKey, query);

        assertEquals("""
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
                                            "name": "ANTLES, Kerri",
                                            "officer_role": "director",
                                            "appointed_on": "2017-04-01",
                                            "address": {
                                                "premises": "The Leeming Building",
                                                "locality": "Leeds",
                                                "address_line_1": "Vicar Lane",
                                                "country": "England",
                                                "postal_code": "LS2 7JF"
                                            }
                                        }
                                    ]
                                }
                            ]
                        }
                        """,
                actualSearchResults,
                STRICT);

        verify(getRequestedFor(urlPathEqualTo(TRU_PROXY_SEARCH_PATH))
                .withQueryParam("Query", equalTo(companyNumber))
                .withHeader("Accept", equalTo("application/json"))
                .withHeader(X_API_KEY, equalTo(apiKey)));
        verify(getRequestedFor(urlPathEqualTo(TRU_PROXY_OFFICERS_PATH))
                .withQueryParam("CompanyNumber", equalTo(companyNumber))
                .withHeader("Accept", equalTo("application/json"))
                .withHeader(X_API_KEY, equalTo(apiKey)));

    }

    @Test
    void searchWithOnlyCompanyNumber(@Autowired MockMvc mvc) throws Exception {
        var query = """
                {
                    "companyNumber" : "06500244"
                }
                """;

        var companyNumber = "06500244";
        givenTruProxySearchWilLReturn(
                companyNumber,
                """
                         {
                           "page_number": 1,
                           "kind": "search#companies",
                           "total_results": 20,
                           "items": [
                               {
                                   "company_status": "active",
                                   "address_snippet": "Boswell Cottage Main Street, North Leverton, Retford, England, DN22 0AD",
                                   "date_of_creation": "2008-02-11",
                                   "matches": {
                                       "title": [
                                           1,
                                           3
                                       ]
                                   },
                                   "description": "06500244 - Incorporated on 11 February 2008",
                                   "links": {
                                       "self": "/company/06500244"
                                   },
                                   "company_number": "06500244",
                                   "title": "BBC LIMITED",
                                   "company_type": "ltd",
                                   "address": {
                                       "premises": "Boswell Cottage Main Street",
                                       "postal_code": "DN22 0AD",
                                       "country": "England",
                                       "locality": "Retford",
                                       "address_line_1": "North Leverton"
                                   },
                                   "kind": "searchresults#company",
                                   "description_identifier": [
                                       "incorporated-on"
                                   ]
                               }]
                         }
                        """);


        givenTruProxyGetOfficersWillReturn(
                companyNumber,
                """
                                {
                                    "etag": "6dd2261e61776d79c2c50685145fac364e75e24e",
                                    "links": {
                                        "self": "/company/10241297/officers"
                                    },
                                    "kind": "officer-list",
                                    "items_per_page": 35,
                                    "items": [
                                        {
                                            "address": {
                                                "premises": "The Leeming Building",
                                                "postal_code": "LS2 7JF",
                                                "country": "England",
                                                "locality": "Leeds",
                                                "address_line_1": "Vicar Lane"
                                            },
                                            "name": "ANTLES, Kerri",
                                            "appointed_on": "2017-04-01",
                                            "officer_role": "director",
                                            "links": {
                                                "officer": {
                                                    "appointments": "/officers/4R8_9bZ44w0_cRlrxoC-wRwaMiE/appointments"
                                                }
                                            },
                                            "date_of_birth": {
                                                "month": 6,
                                                "year": 1969
                                            },
                                            "occupation": "Finance And Accounting",
                                            "country_of_residence": "United States",
                                            "nationality": "American"
                                        }]
                                  }
                        """);


        var apiKey = "some-api-key";
        var actualSearchResults = searchAll(mvc, apiKey, query);

        assertEquals("""
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
                                            "name": "ANTLES, Kerri",
                                            "officer_role": "director",
                                            "appointed_on": "2017-04-01",
                                            "address": {
                                                "premises": "The Leeming Building",
                                                "locality": "Leeds",
                                                "address_line_1": "Vicar Lane",
                                                "country": "England",
                                                "postal_code": "LS2 7JF"
                                            }
                                        }
                                    ]
                                }
                            ]
                        }
                        """,
                actualSearchResults,
                STRICT);

        verify(getRequestedFor(urlPathEqualTo(TRU_PROXY_SEARCH_PATH))
                .withQueryParam("Query", equalTo(companyNumber))
                .withHeader("Accept", equalTo("application/json"))
                .withHeader(X_API_KEY, equalTo(apiKey)));
        verify(getRequestedFor(urlPathEqualTo(TRU_PROXY_OFFICERS_PATH))
                .withQueryParam("CompanyNumber", equalTo(companyNumber))
                .withHeader("Accept", equalTo("application/json"))
                .withHeader(X_API_KEY, equalTo(apiKey)));

    }

    @Test
    void searchWithOnlyCompanyName(@Autowired MockMvc mvc) throws Exception {
        var query = """
                {
                    "companyName" : "BBC LIMITED"
                }
                """;

        var companyNumber = "06500244";
        var companyName = "BBC LIMITED";
        givenTruProxySearchWilLReturn(
                companyName,
                """
                         {
                           "page_number": 1,
                           "kind": "search#companies",
                           "total_results": 20,
                           "items": [
                               {
                                   "company_status": "active",
                                   "address_snippet": "Boswell Cottage Main Street, North Leverton, Retford, England, DN22 0AD",
                                   "date_of_creation": "2008-02-11",
                                   "matches": {
                                       "title": [
                                           1,
                                           3
                                       ]
                                   },
                                   "description": "06500244 - Incorporated on 11 February 2008",
                                   "links": {
                                       "self": "/company/06500244"
                                   },
                                   "company_number": "06500244",
                                   "title": "BBC LIMITED",
                                   "company_type": "ltd",
                                   "address": {
                                       "premises": "Boswell Cottage Main Street",
                                       "postal_code": "DN22 0AD",
                                       "country": "England",
                                       "locality": "Retford",
                                       "address_line_1": "North Leverton"
                                   },
                                   "kind": "searchresults#company",
                                   "description_identifier": [
                                       "incorporated-on"
                                   ]
                               }]
                         }
                        """);


        givenTruProxyGetOfficersWillReturn(
                companyNumber,
                """
                                {
                                    "etag": "6dd2261e61776d79c2c50685145fac364e75e24e",
                                    "links": {
                                        "self": "/company/10241297/officers"
                                    },
                                    "kind": "officer-list",
                                    "items_per_page": 35,
                                    "items": [
                                        {
                                            "address": {
                                                "premises": "The Leeming Building",
                                                "postal_code": "LS2 7JF",
                                                "country": "England",
                                                "locality": "Leeds",
                                                "address_line_1": "Vicar Lane"
                                            },
                                            "name": "ANTLES, Kerri",
                                            "appointed_on": "2017-04-01",
                                            "officer_role": "director",
                                            "links": {
                                                "officer": {
                                                    "appointments": "/officers/4R8_9bZ44w0_cRlrxoC-wRwaMiE/appointments"
                                                }
                                            },
                                            "date_of_birth": {
                                                "month": 6,
                                                "year": 1969
                                            },
                                            "occupation": "Finance And Accounting",
                                            "country_of_residence": "United States",
                                            "nationality": "American"
                                        }]
                                  }
                        """);


        var apiKey = "some-api-key";
        var actualSearchResults = searchAll(mvc, apiKey, query);

        assertEquals("""
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
                                            "name": "ANTLES, Kerri",
                                            "officer_role": "director",
                                            "appointed_on": "2017-04-01",
                                            "address": {
                                                "premises": "The Leeming Building",
                                                "locality": "Leeds",
                                                "address_line_1": "Vicar Lane",
                                                "country": "England",
                                                "postal_code": "LS2 7JF"
                                            }
                                        }
                                    ]
                                }
                            ]
                        }
                        """,
                actualSearchResults,
                STRICT);

        verify(getRequestedFor(urlPathEqualTo(TRU_PROXY_SEARCH_PATH))
                .withQueryParam("Query", equalTo(companyName))
                .withHeader("Accept", equalTo("application/json"))
                .withHeader(X_API_KEY, equalTo(apiKey)));
        verify(getRequestedFor(urlPathEqualTo(TRU_PROXY_OFFICERS_PATH))
                .withQueryParam("CompanyNumber", equalTo(companyNumber))
                .withHeader("Accept", equalTo("application/json"))
                .withHeader(X_API_KEY, equalTo(apiKey)));
    }

    @Test
    void searchWithActiveOnly(@Autowired MockMvc mvc) throws Exception {
        var query = """
                {
                    "companyName" : "BBC LIMITED"
                }
                """;

        var companyNumber = "06500244";
        var companyName = "BBC LIMITED";
        givenTruProxySearchWilLReturn(
                companyName,
                """
                         {
                           "page_number": 1,
                           "kind": "search#companies",
                           "total_results": 2,
                           "items": [
                                {
                                   "company_status": "inactive",
                                   "address_snippet": "some-address-snippet",
                                   "date_of_creation": "some-date-of-creation",
                                   "matches": {
                                       "title": [
                                           1,
                                           3
                                       ]
                                   },
                                   "description": "some-description",
                                   "links": {
                                       "self": "/company/06500244"
                                   },
                                   "company_number": "some-company-number",
                                   "title": "BBC LIMITED",
                                   "company_type": "some-company-type",
                                   "address": {
                                       "premises": "some-address",
                                       "postal_code": "some-postal-code",
                                       "country": "some-country",
                                       "locality": "some-locality",
                                       "address_line_1": "some-address-line-1"
                                   },
                                   "kind": "searchresults#company",
                                   "description_identifier": [
                                       "incorporated-on"
                                   ]
                               },
                               {
                                   "company_status": "active",
                                   "address_snippet": "Boswell Cottage Main Street, North Leverton, Retford, England, DN22 0AD",
                                   "date_of_creation": "2008-02-11",
                                   "matches": {
                                       "title": [
                                           1,
                                           3
                                       ]
                                   },
                                   "description": "06500244 - Incorporated on 11 February 2008",
                                   "links": {
                                       "self": "/company/06500244"
                                   },
                                   "company_number": "06500244",
                                   "title": "BBC LIMITED",
                                   "company_type": "ltd",
                                   "address": {
                                       "premises": "Boswell Cottage Main Street",
                                       "postal_code": "DN22 0AD",
                                       "country": "England",
                                       "locality": "Retford",
                                       "address_line_1": "North Leverton"
                                   },
                                   "kind": "searchresults#company",
                                   "description_identifier": [
                                       "incorporated-on"
                                   ]
                               }
                               ]
                         }
                        """);


        givenTruProxyGetOfficersWillReturn(
                companyNumber,
                """
                                {
                                    "etag": "6dd2261e61776d79c2c50685145fac364e75e24e",
                                    "links": {
                                        "self": "/company/10241297/officers"
                                    },
                                    "kind": "officer-list",
                                    "items_per_page": 35,
                                    "items": [
                                        {
                                            "address": {
                                                "premises": "The Leeming Building",
                                                "postal_code": "LS2 7JF",
                                                "country": "England",
                                                "locality": "Leeds",
                                                "address_line_1": "Vicar Lane"
                                            },
                                            "name": "ANTLES, Kerri",
                                            "appointed_on": "2017-04-01",
                                            "officer_role": "director",
                                            "links": {
                                                "officer": {
                                                    "appointments": "/officers/4R8_9bZ44w0_cRlrxoC-wRwaMiE/appointments"
                                                }
                                            },
                                            "date_of_birth": {
                                                "month": 6,
                                                "year": 1969
                                            },
                                            "occupation": "Finance And Accounting",
                                            "country_of_residence": "United States",
                                            "nationality": "American"
                                        }]
                                  }
                        """);


        var apiKey = "some-api-key";
        var actualSearchResults = searchActiveOnly(mvc, apiKey, query);

        assertEquals("""
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
                                            "name": "ANTLES, Kerri",
                                            "officer_role": "director",
                                            "appointed_on": "2017-04-01",
                                            "address": {
                                                "premises": "The Leeming Building",
                                                "locality": "Leeds",
                                                "address_line_1": "Vicar Lane",
                                                "country": "England",
                                                "postal_code": "LS2 7JF"
                                            }
                                        }
                                    ]
                                }
                            ]
                        }
                        """,
                actualSearchResults,
                STRICT);

        verify(getRequestedFor(urlPathEqualTo(TRU_PROXY_SEARCH_PATH))
                .withQueryParam("Query", equalTo(companyName))
                .withHeader("Accept", equalTo("application/json"))
                .withHeader(X_API_KEY, equalTo(apiKey)));
        verify(getRequestedFor(urlPathEqualTo(TRU_PROXY_OFFICERS_PATH))
                .withQueryParam("CompanyNumber", equalTo(companyNumber))
                .withHeader("Accept", equalTo("application/json"))
                .withHeader(X_API_KEY, equalTo(apiKey)));
    }

    @Test
    @Disabled
    void searchWithoutBodyShouldBeABadRequest(@Autowired MockMvc mvc) throws Exception {
        mvc.perform(post(COMPANY_SEARCH_URL)
                        .contentType(APPLICATION_JSON)
                        .header(X_API_KEY, "some-api-key"))
                .andExpect(
                        status().isBadRequest());

    }

    @Test
    @Disabled
    void searchWithoutAnApiKeyShouldBeABadRequest(@Autowired MockMvc mvc) throws Exception {
        mvc.perform(post(COMPANY_SEARCH_URL)
                        .contentType(APPLICATION_JSON)
                        .header(X_API_KEY, "some-api-key"))
                .andExpect(
                        status().isBadRequest());
    }

    @Test
    @Disabled
    void searchWithForbiddenApiKeyShouldBeForbidden(@Autowired MockMvc mvc) throws Exception {
        fail("TODO");
    }

    @Test
    void searchShouldReturnActiveOfficersOnly(@Autowired MockMvc mvc) throws Exception {
        var query = """
                {
                    "companyName" : "BBC LIMITED",
                    "companyNumber" : "06500244"
                }
                """;

        var companyNumber = "06500244";
        givenTruProxySearchWilLReturn(
                companyNumber,
                """
                         {
                           "page_number": 1,
                           "kind": "search#companies",
                           "total_results": 20,
                           "items": [
                               {
                                   "company_status": "active",
                                   "address_snippet": "Boswell Cottage Main Street, North Leverton, Retford, England, DN22 0AD",
                                   "date_of_creation": "2008-02-11",
                                   "matches": {
                                       "title": [
                                           1,
                                           3
                                       ]
                                   },
                                   "description": "06500244 - Incorporated on 11 February 2008",
                                   "links": {
                                       "self": "/company/06500244"
                                   },
                                   "company_number": "06500244",
                                   "title": "BBC LIMITED",
                                   "company_type": "ltd",
                                   "address": {
                                       "premises": "Boswell Cottage Main Street",
                                       "postal_code": "DN22 0AD",
                                       "country": "England",
                                       "locality": "Retford",
                                       "address_line_1": "North Leverton"
                                   },
                                   "kind": "searchresults#company",
                                   "description_identifier": [
                                       "incorporated-on"
                                   ]
                               }]
                         }
                        """);


        givenTruProxyGetOfficersWillReturn(
                companyNumber,
                """
                                {
                                    "etag": "6dd2261e61776d79c2c50685145fac364e75e24e",
                                    "links": {
                                        "self": "/company/10241297/officers"
                                    },
                                    "kind": "officer-list",
                                    "items_per_page": 35,
                                    "items": [
                                        {
                                            "address": {
                                                "premises": "some-premises",
                                                "postal_code": "some-postal-code",
                                                "country": "some-country",
                                                "locality": "some-locality",
                                                "address_line_1": "some-address-line-1"
                                            },
                                            "name": "some-name",
                                            "appointed_on": "some-appointed-on",
                                            "resigned_on": "some-resigned-on",
                                            "officer_role": "some-officer-role",
                                            "links": {
                                                "officer": {
                                                    "appointments": "/officers/4R8_9bZ44w0_cRlrxoC-wRwaMiE/appointments"
                                                }
                                            },
                                            "date_of_birth": {
                                                "month": 6,
                                                "year": 1969
                                            },
                                            "occupation": "some-occupation",
                                            "country_of_residence": "some-country-of-residence",
                                            "nationality": "some-nationality"
                                        },
                                        {
                                            "address": {
                                                "premises": "The Leeming Building",
                                                "postal_code": "LS2 7JF",
                                                "country": "England",
                                                "locality": "Leeds",
                                                "address_line_1": "Vicar Lane"
                                            },
                                            "name": "ANTLES, Kerri",
                                            "appointed_on": "2017-04-01",
                                            "officer_role": "director",
                                            "links": {
                                                "officer": {
                                                    "appointments": "/officers/4R8_9bZ44w0_cRlrxoC-wRwaMiE/appointments"
                                                }
                                            },
                                            "date_of_birth": {
                                                "month": 6,
                                                "year": 1969
                                            },
                                            "occupation": "Finance And Accounting",
                                            "country_of_residence": "United States",
                                            "nationality": "American"
                                        }
                                        ]
                                  }
                        """);


        var apiKey = "some-api-key";
        var actualSearchResults = searchAll(mvc, apiKey, query);

        assertEquals("""
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
                                            "name": "ANTLES, Kerri",
                                            "officer_role": "director",
                                            "appointed_on": "2017-04-01",
                                            "address": {
                                                "premises": "The Leeming Building",
                                                "locality": "Leeds",
                                                "address_line_1": "Vicar Lane",
                                                "country": "England",
                                                "postal_code": "LS2 7JF"
                                            }
                                        }
                                    ]
                                }
                            ]
                        }
                        """,
                actualSearchResults,
                STRICT);

        verify(getRequestedFor(urlPathEqualTo(TRU_PROXY_SEARCH_PATH))
                .withQueryParam("Query", equalTo(companyNumber))
                .withHeader("Accept", equalTo("application/json"))
                .withHeader(X_API_KEY, equalTo(apiKey)));
        verify(getRequestedFor(urlPathEqualTo(TRU_PROXY_OFFICERS_PATH))
                .withQueryParam("CompanyNumber", equalTo(companyNumber))
                .withHeader("Accept", equalTo("application/json"))
                .withHeader(X_API_KEY, equalTo(apiKey)));

    }

    private static void verify(RequestPatternBuilder requestedFor) {
        wiremock.verify(requestedFor);
    }

    private void stubFor(MappingBuilder mappingBuilder) {
        wiremock.stubFor(mappingBuilder);
    }

    private void givenTruProxyGetOfficersWillReturn(String companyNumber, String officers) {
        stubFor(get(urlPathEqualTo(TRU_PROXY_OFFICERS_PATH))
                .withQueryParam("CompanyNumber", equalTo(companyNumber))
                .willReturn(
                        aResponse()
                                .withHeader("Content-Type", "application/json")
                                .withBody(
                                        officers
                                )));
    }

    private void givenTruProxySearchWilLReturn(String searchTerm, String companyResults) {
        stubFor(get(urlPathEqualTo(TRU_PROXY_SEARCH_PATH))
                .withQueryParam("Query", equalTo(searchTerm))
                .willReturn(
                        aResponse()
                                .withHeader("Content-Type", "application/json")
                                .withBody(companyResults)));
    }

    private static String searchAll(MockMvc mvc, String apiKey, String query) throws Exception {
        return mvc.perform(
                        post(COMPANY_SEARCH_URL)
                                .contentType(APPLICATION_JSON)
                                .header(X_API_KEY, apiKey)
                                .content(query))
                .andExpectAll(
                        status().isOk(),
                        content()
                                .contentType(APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    private static String searchActiveOnly(MockMvc mvc, String apiKey, String query) throws Exception {
        return mvc.perform(
                        post(COMPANY_SEARCH_URL)
                                .contentType(APPLICATION_JSON)
                                .queryParam("Active", "true")
                                .header(X_API_KEY, apiKey)
                                .content(query))
                .andExpectAll(
                        status().isOk(),
                        content()
                                .contentType(APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();
    }
}

