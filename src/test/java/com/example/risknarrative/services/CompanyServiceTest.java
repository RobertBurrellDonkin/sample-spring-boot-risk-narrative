package com.example.risknarrative.services;

import com.example.risknarrative.domain.Company;
import com.example.risknarrative.responsitories.CompanyRepository;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.CountMatchingStrategy;
import com.github.tomakehurst.wiremock.client.MappingBuilder;
import com.github.tomakehurst.wiremock.matching.RequestPatternBuilder;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.cloud.contract.wiremock.WireMockSpring.options;

@SpringBootTest
@AutoConfigureWireMock(port = 0)
class CompanyServiceTest {

    public static final String TRU_PROXY_API = "/TruProxyAPI/rest/Companies/v1/";
    public static final String TRU_PROXY_SEARCH_PATH = TRU_PROXY_API + "Search";
    public static final String TRU_PROXY_OFFICERS_PATH = TRU_PROXY_API + "Officers";
    public static final String X_API_KEY = "x-api-key";
    public static WireMockServer wiremock = new WireMockServer(options().dynamicPort());
    @Autowired
    CompanyService subjectUnderTest;
    @Autowired
    TruProxyWebClient truProxyWebClient;
    @Autowired
    CompanyRepository companyRepository;

    @BeforeAll
    static void setupClass() {
        wiremock.start();
    }

    @AfterAll
    static void clean() {
        wiremock.shutdown();
    }

    private static void verify(RequestPatternBuilder requestedFor) {
        wiremock.verify(requestedFor);
    }

    private static void verify(CountMatchingStrategy countMatchingStrategy, RequestPatternBuilder requestedFor) {
        wiremock.verify(countMatchingStrategy, requestedFor);
    }

    @AfterEach
    void after() {
        wiremock.resetAll();
        companyRepository.deleteAll();
    }

    @BeforeEach
    void setUpBaseUrl() {
        truProxyWebClient.setBaseUrl(wiremock.baseUrl());
    }

    private void stubFor(MappingBuilder mappingBuilder) {
        wiremock.stubFor(mappingBuilder);
    }

    @Test
    public void activeOfficersOnly() {
        stubFor(get(urlPathEqualTo(TRU_PROXY_SEARCH_PATH))
                .withQueryParam("Query", equalTo("some-company-name"))
                .willReturn(
                        aResponse()
                                .withHeader("Content-Type", "application/json")
                                .withBody(
                                        """
                                                 {
                                                    "page_number": 1,
                                                    "kind": "search#companies",
                                                    "total_results": 20,
                                                    "items": [
                                                        {
                                                            "company_status": "some-company-status",
                                                            "address_snippet": "Boswell Cottage Main Street, North Leverton, Retford, England, DN22 0AD",
                                                            "date_of_creation": "some-date-of-creation",
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
                                                            "company_number": "some-company-number",
                                                            "title": "some-title",
                                                            "company_type": "some-company-type",
                                                            "address": {
                                                                "premises": "some-company-premises",
                                                                "postal_code": "some-company-postal-code",
                                                                "country": "some-company-country",
                                                                "locality": "some-company-locality",
                                                                "address_line_1": "some-company-address-line"
                                                            },
                                                            "kind": "searchresults#company",
                                                            "description_identifier": [
                                                                "incorporated-on"
                                                            ]
                                                        }]
                                                  }
                                                """
                                )));

        stubFor(get(urlPathEqualTo(TRU_PROXY_OFFICERS_PATH))
                .withQueryParam("CompanyNumber", equalTo("some-company-number"))
                .willReturn(
                        aResponse()
                                .withHeader("Content-Type", "application/json")
                                .withBody(
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
                                                                   "premises": "some-officer-premises",
                                                                   "postal_code": "some-officer-postal-code",
                                                                   "country": "some-officer-country",
                                                                   "locality": "some-officer-locality",
                                                                   "address_line_1": "some-officer-address-line"
                                                               },
                                                               "name": "some-officer-name",
                                                               "appointed_on": "some-officer-appointed-on",
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
                                                               "occupation": "Finance And Accounting",
                                                               "country_of_residence": "United States",
                                                               "nationality": "American"
                                                           },
                                                           {
                                                               "address": {
                                                                   "premises": "another-officer-premises",
                                                                   "postal_code": "another-officer-postal-code",
                                                                   "country": "another-officer-country",
                                                                   "locality": "another-officer-locality",
                                                                   "address_line_1": "another-officer-address-line"
                                                               },
                                                               "name": "another-officer-name",
                                                               "appointed_on": "another-appointed-on",
                                                               "resigned_on": "another-resigned-on",
                                                               "officer_role": "another-officer-role",
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
                                                """
                                )));

        var companies = subjectUnderTest.search("some-api-key", null, "some-company-name", false);

        verify(getRequestedFor(urlPathEqualTo(TRU_PROXY_SEARCH_PATH))
                .withQueryParam("Query", equalTo("some-company-name"))
                .withHeader("Accept", equalTo("application/json"))
                .withHeader(X_API_KEY, equalTo("some-api-key")));
        verify(getRequestedFor(urlPathEqualTo(TRU_PROXY_OFFICERS_PATH))
                .withQueryParam("CompanyNumber", equalTo("some-company-number"))
                .withHeader("Accept", equalTo("application/json"))
                .withHeader(X_API_KEY, equalTo("some-api-key")));

        assertStandardResultsFor(companies, "some-company-status");
    }

    @Test
    public void byCompanyNumberAndCompanyName() {
        stubFor(get(urlPathEqualTo(TRU_PROXY_SEARCH_PATH))
                .withQueryParam("Query", equalTo("some-company-number"))
                .willReturn(
                        aResponse()
                                .withHeader("Content-Type", "application/json")
                                .withBody(
                                        """
                                                 {
                                                    "page_number": 1,
                                                    "kind": "search#companies",
                                                    "total_results": 20,
                                                    "items": [
                                                        {
                                                            "company_status": "some-company-status",
                                                            "address_snippet": "Boswell Cottage Main Street, North Leverton, Retford, England, DN22 0AD",
                                                            "date_of_creation": "some-date-of-creation",
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
                                                            "company_number": "some-company-number",
                                                            "title": "some-title",
                                                            "company_type": "some-company-type",
                                                            "address": {
                                                                "premises": "some-company-premises",
                                                                "postal_code": "some-company-postal-code",
                                                                "country": "some-company-country",
                                                                "locality": "some-company-locality",
                                                                "address_line_1": "some-company-address-line"
                                                            },
                                                            "kind": "searchresults#company",
                                                            "description_identifier": [
                                                                "incorporated-on"
                                                            ]
                                                        }]
                                                  }
                                                """
                                )));

        stubFor(get(urlPathEqualTo(TRU_PROXY_OFFICERS_PATH))
                .withQueryParam("CompanyNumber", equalTo("some-company-number"))
                .willReturn(
                        aResponse()
                                .withHeader("Content-Type", "application/json")
                                .withBody(
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
                                                                   "premises": "some-officer-premises",
                                                                   "postal_code": "some-officer-postal-code",
                                                                   "country": "some-officer-country",
                                                                   "locality": "some-officer-locality",
                                                                   "address_line_1": "some-officer-address-line"
                                                               },
                                                               "name": "some-officer-name",
                                                               "appointed_on": "some-officer-appointed-on",
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
                                                               "occupation": "Finance And Accounting",
                                                               "country_of_residence": "United States",
                                                               "nationality": "American"
                                                           }]
                                                     }
                                                """
                                )));

        var companies = subjectUnderTest.search("some-api-key", "some-company-number", "some-company-name", false);

        verify(getRequestedFor(urlPathEqualTo(TRU_PROXY_SEARCH_PATH))
                .withQueryParam("Query", equalTo("some-company-number"))
                .withHeader("Accept", equalTo("application/json"))
                .withHeader(X_API_KEY, equalTo("some-api-key")));
        verify(getRequestedFor(urlPathEqualTo(TRU_PROXY_OFFICERS_PATH))
                .withQueryParam("CompanyNumber", equalTo("some-company-number"))
                .withHeader("Accept", equalTo("application/json"))
                .withHeader(X_API_KEY, equalTo("some-api-key")));

        assertStandardResultsFor(companies, "some-company-status");
    }

    @Test
    public void byCompanyNumber() {
        stubFor(get(urlPathEqualTo(TRU_PROXY_SEARCH_PATH))
                .withQueryParam("Query", equalTo("some-company-number"))
                .willReturn(
                        aResponse()
                                .withHeader("Content-Type", "application/json")
                                .withBody(
                                        """
                                                 {
                                                    "page_number": 1,
                                                    "kind": "search#companies",
                                                    "total_results": 20,
                                                    "items": [
                                                        {
                                                            "company_status": "some-company-status",
                                                            "address_snippet": "Boswell Cottage Main Street, North Leverton, Retford, England, DN22 0AD",
                                                            "date_of_creation": "some-date-of-creation",
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
                                                            "company_number": "some-company-number",
                                                            "title": "some-title",
                                                            "company_type": "some-company-type",
                                                            "address": {
                                                                "premises": "some-company-premises",
                                                                "postal_code": "some-company-postal-code",
                                                                "country": "some-company-country",
                                                                "locality": "some-company-locality",
                                                                "address_line_1": "some-company-address-line"
                                                            },
                                                            "kind": "searchresults#company",
                                                            "description_identifier": [
                                                                "incorporated-on"
                                                            ]
                                                        }]
                                                  }
                                                """
                                )));

        stubFor(get(urlPathEqualTo(TRU_PROXY_OFFICERS_PATH))
                .withQueryParam("CompanyNumber", equalTo("some-company-number"))
                .willReturn(
                        aResponse()
                                .withHeader("Content-Type", "application/json")
                                .withBody(
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
                                                                   "premises": "some-officer-premises",
                                                                   "postal_code": "some-officer-postal-code",
                                                                   "country": "some-officer-country",
                                                                   "locality": "some-officer-locality",
                                                                   "address_line_1": "some-officer-address-line"
                                                               },
                                                               "name": "some-officer-name",
                                                               "appointed_on": "some-officer-appointed-on",
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
                                                               "occupation": "Finance And Accounting",
                                                               "country_of_residence": "United States",
                                                               "nationality": "American"
                                                           }]
                                                     }
                                                """
                                )));

        var companies = subjectUnderTest.search("some-api-key", "some-company-number", null, false);

        verify(getRequestedFor(urlPathEqualTo(TRU_PROXY_SEARCH_PATH))
                .withQueryParam("Query", equalTo("some-company-number"))
                .withHeader("Accept", equalTo("application/json"))
                .withHeader(X_API_KEY, equalTo("some-api-key")));
        verify(getRequestedFor(urlPathEqualTo(TRU_PROXY_OFFICERS_PATH))
                .withQueryParam("CompanyNumber", equalTo("some-company-number"))
                .withHeader("Accept", equalTo("application/json"))
                .withHeader(X_API_KEY, equalTo("some-api-key")));

        assertStandardResultsFor(companies, "some-company-status");
    }

    @Test
    public void byCompanyName() {
        stubFor(get(urlPathEqualTo(TRU_PROXY_SEARCH_PATH))
                .withQueryParam("Query", equalTo("some-company-name"))
                .willReturn(
                        aResponse()
                                .withHeader("Content-Type", "application/json")
                                .withBody(
                                        """
                                                 {
                                                    "page_number": 1,
                                                    "kind": "search#companies",
                                                    "total_results": 20,
                                                    "items": [
                                                        {
                                                            "company_status": "some-company-status",
                                                            "address_snippet": "Boswell Cottage Main Street, North Leverton, Retford, England, DN22 0AD",
                                                            "date_of_creation": "some-date-of-creation",
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
                                                            "company_number": "some-company-number",
                                                            "title": "some-title",
                                                            "company_type": "some-company-type",
                                                            "address": {
                                                                "premises": "some-company-premises",
                                                                "postal_code": "some-company-postal-code",
                                                                "country": "some-company-country",
                                                                "locality": "some-company-locality",
                                                                "address_line_1": "some-company-address-line"
                                                            },
                                                            "kind": "searchresults#company",
                                                            "description_identifier": [
                                                                "incorporated-on"
                                                            ]
                                                        }]
                                                  }
                                                """
                                )));

        stubFor(get(urlPathEqualTo(TRU_PROXY_OFFICERS_PATH))
                .withQueryParam("CompanyNumber", equalTo("some-company-number"))
                .willReturn(
                        aResponse()
                                .withHeader("Content-Type", "application/json")
                                .withBody(
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
                                                                   "premises": "some-officer-premises",
                                                                   "postal_code": "some-officer-postal-code",
                                                                   "country": "some-officer-country",
                                                                   "locality": "some-officer-locality",
                                                                   "address_line_1": "some-officer-address-line"
                                                               },
                                                               "name": "some-officer-name",
                                                               "appointed_on": "some-officer-appointed-on",
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
                                                               "occupation": "Finance And Accounting",
                                                               "country_of_residence": "United States",
                                                               "nationality": "American"
                                                           }]
                                                     }
                                                """
                                )));

        var companies = subjectUnderTest.search("some-api-key", null, "some-company-name", false);

        verify(getRequestedFor(urlPathEqualTo(TRU_PROXY_SEARCH_PATH))
                .withQueryParam("Query", equalTo("some-company-name"))
                .withHeader("Accept", equalTo("application/json"))
                .withHeader(X_API_KEY, equalTo("some-api-key")));
        verify(getRequestedFor(urlPathEqualTo(TRU_PROXY_OFFICERS_PATH))
                .withQueryParam("CompanyNumber", equalTo("some-company-number"))
                .withHeader("Accept", equalTo("application/json"))
                .withHeader(X_API_KEY, equalTo("some-api-key")));

        assertStandardResultsFor(companies, "some-company-status");
    }

    @Test
    public void activeOnly() {
        stubFor(get(urlPathEqualTo(TRU_PROXY_SEARCH_PATH))
                .withQueryParam("Query", equalTo("some-company-name"))
                .willReturn(
                        aResponse()
                                .withHeader("Content-Type", "application/json")
                                .withBody(
                                        """
                                                 {
                                                    "page_number": 1,
                                                    "kind": "search#companies",
                                                    "total_results": 20,
                                                    "items": [
                                                    {
                                                            "company_status": "another-company-status",
                                                            "address_snippet": "another-address-snippet",
                                                            "date_of_creation": "another-date-of-creation",
                                                            "matches": {
                                                                "title": [
                                                                    1,
                                                                    3
                                                                ]
                                                            },
                                                            "description": "another-description",
                                                            "links": {
                                                                "self": "/company/06500244"
                                                            },
                                                            "company_number": "another-company-number",
                                                            "title": "another-title",
                                                            "company_type": "another-company-type",
                                                            "address": {
                                                                "premises": "another-company-premises",
                                                                "postal_code": "another-company-postal-code",
                                                                "country": "another-company-country",
                                                                "locality": "another-company-locality",
                                                                "address_line_1": "another-company-address-line"
                                                            },
                                                            "kind": "searchresults#company",
                                                            "description_identifier": [
                                                                "incorporated-on"
                                                            ]
                                                        },
                                                        {
                                                            "company_status": "active",
                                                            "address_snippet": "Boswell Cottage Main Street, North Leverton, Retford, England, DN22 0AD",
                                                            "date_of_creation": "some-date-of-creation",
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
                                                            "company_number": "some-company-number",
                                                            "title": "some-title",
                                                            "company_type": "some-company-type",
                                                            "address": {
                                                                "premises": "some-company-premises",
                                                                "postal_code": "some-company-postal-code",
                                                                "country": "some-company-country",
                                                                "locality": "some-company-locality",
                                                                "address_line_1": "some-company-address-line"
                                                            },
                                                            "kind": "searchresults#company",
                                                            "description_identifier": [
                                                                "incorporated-on"
                                                            ]
                                                        }]
                                                  }
                                                """
                                )));

        stubFor(get(urlPathEqualTo(TRU_PROXY_OFFICERS_PATH))
                .withQueryParam("CompanyNumber", equalTo("some-company-number"))
                .willReturn(
                        aResponse()
                                .withHeader("Content-Type", "application/json")
                                .withBody(
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
                                                                   "premises": "some-officer-premises",
                                                                   "postal_code": "some-officer-postal-code",
                                                                   "country": "some-officer-country",
                                                                   "locality": "some-officer-locality",
                                                                   "address_line_1": "some-officer-address-line"
                                                               },
                                                               "name": "some-officer-name",
                                                               "appointed_on": "some-officer-appointed-on",
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
                                                               "occupation": "Finance And Accounting",
                                                               "country_of_residence": "United States",
                                                               "nationality": "American"
                                                           }]
                                                     }
                                                """
                                )));

        var companies = subjectUnderTest.search("some-api-key", null, "some-company-name", true);

        verify(getRequestedFor(urlPathEqualTo(TRU_PROXY_SEARCH_PATH))
                .withQueryParam("Query", equalTo("some-company-name"))
                .withHeader("Accept", equalTo("application/json"))
                .withHeader(X_API_KEY, equalTo("some-api-key")));
        verify(getRequestedFor(urlPathEqualTo(TRU_PROXY_OFFICERS_PATH))
                .withQueryParam("CompanyNumber", equalTo("some-company-number"))
                .withHeader("Accept", equalTo("application/json"))
                .withHeader(X_API_KEY, equalTo("some-api-key")));

        assertStandardResultsFor(companies, "active");
    }

    @Test
    public void cacheByCompanyNumber() {
        stubFor(get(urlPathEqualTo(TRU_PROXY_SEARCH_PATH))
                .withQueryParam("Query", equalTo("some-company-number"))
                .willReturn(
                        aResponse()
                                .withHeader("Content-Type", "application/json")
                                .withBody(
                                        """
                                                 {
                                                    "page_number": 1,
                                                    "kind": "search#companies",
                                                    "total_results": 20,
                                                    "items": [
                                                        {
                                                            "company_status": "some-company-status",
                                                            "address_snippet": "Boswell Cottage Main Street, North Leverton, Retford, England, DN22 0AD",
                                                            "date_of_creation": "some-date-of-creation",
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
                                                            "company_number": "some-company-number",
                                                            "title": "some-title",
                                                            "company_type": "some-company-type",
                                                            "address": {
                                                                "premises": "some-company-premises",
                                                                "postal_code": "some-company-postal-code",
                                                                "country": "some-company-country",
                                                                "locality": "some-company-locality",
                                                                "address_line_1": "some-company-address-line"
                                                            },
                                                            "kind": "searchresults#company",
                                                            "description_identifier": [
                                                                "incorporated-on"
                                                            ]
                                                        }]
                                                  }
                                                """
                                )));

        stubFor(get(urlPathEqualTo(TRU_PROXY_OFFICERS_PATH))
                .withQueryParam("CompanyNumber", equalTo("some-company-number"))
                .willReturn(
                        aResponse()
                                .withHeader("Content-Type", "application/json")
                                .withBody(
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
                                                                   "premises": "some-officer-premises",
                                                                   "postal_code": "some-officer-postal-code",
                                                                   "country": "some-officer-country",
                                                                   "locality": "some-officer-locality",
                                                                   "address_line_1": "some-officer-address-line"
                                                               },
                                                               "name": "some-officer-name",
                                                               "appointed_on": "some-officer-appointed-on",
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
                                                               "occupation": "Finance And Accounting",
                                                               "country_of_residence": "United States",
                                                               "nationality": "American"
                                                           }]
                                                     }
                                                """
                                )));

        assertStandardResultsFor(subjectUnderTest.search("some-api-key", "some-company-number", null, false), "some-company-status");
        assertStandardResultsFor(subjectUnderTest.search("some-api-key", "some-company-number", null, false), "some-company-status");
        assertStandardResultsFor(subjectUnderTest.search("some-api-key", "some-company-number", null, false), "some-company-status");

        verify(exactly(1), getRequestedFor(urlPathEqualTo(TRU_PROXY_SEARCH_PATH))
                .withQueryParam("Query", equalTo("some-company-number"))
                .withHeader("Accept", equalTo("application/json"))
                .withHeader(X_API_KEY, equalTo("some-api-key")));
        verify(exactly(1), getRequestedFor(urlPathEqualTo(TRU_PROXY_OFFICERS_PATH))
                .withQueryParam("CompanyNumber", equalTo("some-company-number"))
                .withHeader("Accept", equalTo("application/json"))
                .withHeader(X_API_KEY, equalTo("some-api-key")));

    }

    private static void assertStandardResultsFor(List<Company> companies, String expected) {
        assertThat(companies).hasSize(1);
        final var company = companies.get(0);
        assertThat(company.getCompanyNumber()).isEqualTo("some-company-number");
        assertThat(company.getCompanyType()).isEqualTo("some-company-type");
        assertThat(company.getCompanyStatus()).isEqualTo(expected);
        assertThat(company.getTitle()).isEqualTo("some-title");
        assertThat(company.getDateOfCreation()).isEqualTo("some-date-of-creation");

        final var address = company.getAddress();
        assertThat(address.getPostalCode()).isEqualTo("some-company-postal-code");
        assertThat(address.getCountry()).isEqualTo("some-company-country");
        assertThat(address.getLocality()).isEqualTo("some-company-locality");
        assertThat(address.getPremises()).isEqualTo("some-company-premises");
        assertThat(address.getAddressLine()).isEqualTo("some-company-address-line");

        assertThat(company.getOfficers()).hasSize(1);
        final var officer = company.getOfficers().get(0);
        assertThat(officer.getName()).isEqualTo("some-officer-name");
        assertThat(officer.getOfficerRole()).isEqualTo("some-officer-role");
        assertThat(officer.getAppointedOn()).isEqualTo("some-officer-appointed-on");

        final var officerAddress = officer.getAddress();
        assertThat(officerAddress.getPostalCode()).isEqualTo("some-officer-postal-code");
        assertThat(officerAddress.getCountry()).isEqualTo("some-officer-country");
        assertThat(officerAddress.getLocality()).isEqualTo("some-officer-locality");
        assertThat(officerAddress.getPremises()).isEqualTo("some-officer-premises");
        assertThat(officerAddress.getAddressLine()).isEqualTo("some-officer-address-line");
    }

}