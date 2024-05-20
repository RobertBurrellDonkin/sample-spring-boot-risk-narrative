package com.example.risknarrative.services;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.MappingBuilder;
import com.github.tomakehurst.wiremock.matching.RequestPatternBuilder;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;

import static com.example.risknarrative.builders.AddressBuilder.anAddress;
import static com.example.risknarrative.builders.CompanyBuilder.aCompany;
import static com.example.risknarrative.builders.OfficerBuilder.anOfficer;
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

    @AfterEach
    void after() {
        wiremock.resetAll();
    }

    @BeforeEach
    void setUpBaseUrl() {
        truProxyWebClient.setBaseUrl(wiremock.baseUrl());
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
                                                               "appointed_on": "some-appointed-on",
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

        var companyRecords = subjectUnderTest.search("some-api-key", null, "some-company-name", false);

        verify(getRequestedFor(urlPathEqualTo(TRU_PROXY_SEARCH_PATH))
                .withQueryParam("Query", equalTo("some-company-name"))
                .withHeader("Accept", equalTo("application/json"))
                .withHeader(X_API_KEY, equalTo("some-api-key")));
        verify(getRequestedFor(urlPathEqualTo(TRU_PROXY_OFFICERS_PATH))
                .withQueryParam("CompanyNumber", equalTo("some-company-number"))
                .withHeader("Accept", equalTo("application/json"))
                .withHeader(X_API_KEY, equalTo("some-api-key")));

        assertThat(companyRecords).containsExactly(
                aCompany()
                        .withCompanyNumber("some-company-number")
                        .withCompanyType("some-company-type")
                        .withCompanyStatus("some-company-status")
                        .withTitle("some-title")
                        .withDateOfCreation("some-date-of-creation")
                        .withAddress(
                                anAddress()
                                        .withAddressLine("some-company-address-line")
                                        .withPostalCode("some-company-postal-code")
                                        .withLocality("some-company-locality")
                                        .withCountry("some-company-country")
                                        .withPremises("some-company-premises")
                        )
                        .withOfficers(
                                anOfficer()
                                        .withName("some-officer-name")
                                        .withOfficerRole("some-officer-role")
                                        .withAppointedOn("some-appointed-on")
                                        .withAddress(
                                                anAddress()
                                                        .withAddressLine("some-officer-address-line")
                                                        .withPostalCode("some-officer-postal-code")
                                                        .withLocality("some-officer-locality")
                                                        .withCountry("some-officer-country")
                                                        .withPremises("some-officer-premises")))
                        .build()
        );
    }

    private void stubFor(MappingBuilder mappingBuilder) {
        wiremock.stubFor(mappingBuilder);
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
                                                               "appointed_on": "some-appointed-on",
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

        var companyRecords = subjectUnderTest.search("some-api-key", "some-company-number", null, false);

        verify(getRequestedFor(urlPathEqualTo(TRU_PROXY_SEARCH_PATH))
                .withQueryParam("Query", equalTo("some-company-number"))
                .withHeader("Accept", equalTo("application/json"))
                .withHeader(X_API_KEY, equalTo("some-api-key")));
        verify(getRequestedFor(urlPathEqualTo(TRU_PROXY_OFFICERS_PATH))
                .withQueryParam("CompanyNumber", equalTo("some-company-number"))
                .withHeader("Accept", equalTo("application/json"))
                .withHeader(X_API_KEY, equalTo("some-api-key")));

        assertThat(companyRecords).containsExactly(
                aCompany()
                        .withCompanyNumber("some-company-number")
                        .withCompanyType("some-company-type")
                        .withCompanyStatus("some-company-status")
                        .withTitle("some-title")
                        .withDateOfCreation("some-date-of-creation")
                        .withAddress(
                                anAddress()
                                        .withAddressLine("some-company-address-line")
                                        .withPostalCode("some-company-postal-code")
                                        .withLocality("some-company-locality")
                                        .withCountry("some-company-country")
                                        .withPremises("some-company-premises")
                        )
                        .withOfficers(
                                anOfficer()
                                        .withName("some-officer-name")
                                        .withOfficerRole("some-officer-role")
                                        .withAppointedOn("some-appointed-on")
                                        .withAddress(
                                                anAddress()
                                                        .withAddressLine("some-officer-address-line")
                                                        .withPostalCode("some-officer-postal-code")
                                                        .withLocality("some-officer-locality")
                                                        .withCountry("some-officer-country")
                                                        .withPremises("some-officer-premises")))
                        .build()
        );
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
                                                               "appointed_on": "some-appointed-on",
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

        var companyRecords = subjectUnderTest.search("some-api-key", null, "some-company-name", false);

        verify(getRequestedFor(urlPathEqualTo(TRU_PROXY_SEARCH_PATH))
                .withQueryParam("Query", equalTo("some-company-name"))
                .withHeader("Accept", equalTo("application/json"))
                .withHeader(X_API_KEY, equalTo("some-api-key")));
        verify(getRequestedFor(urlPathEqualTo(TRU_PROXY_OFFICERS_PATH))
                .withQueryParam("CompanyNumber", equalTo("some-company-number"))
                .withHeader("Accept", equalTo("application/json"))
                .withHeader(X_API_KEY, equalTo("some-api-key")));

        assertThat(companyRecords).containsExactly(
                aCompany()
                        .withCompanyNumber("some-company-number")
                        .withCompanyType("some-company-type")
                        .withCompanyStatus("some-company-status")
                        .withTitle("some-title")
                        .withDateOfCreation("some-date-of-creation")
                        .withAddress(
                                anAddress()
                                        .withAddressLine("some-company-address-line")
                                        .withPostalCode("some-company-postal-code")
                                        .withLocality("some-company-locality")
                                        .withCountry("some-company-country")
                                        .withPremises("some-company-premises")
                        )
                        .withOfficers(
                                anOfficer()
                                        .withName("some-officer-name")
                                        .withOfficerRole("some-officer-role")
                                        .withAppointedOn("some-appointed-on")
                                        .withAddress(
                                                anAddress()
                                                        .withAddressLine("some-officer-address-line")
                                                        .withPostalCode("some-officer-postal-code")
                                                        .withLocality("some-officer-locality")
                                                        .withCountry("some-officer-country")
                                                        .withPremises("some-officer-premises")))
                        .build()
        );
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
                                                               "appointed_on": "some-appointed-on",
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

        var companyRecords = subjectUnderTest.search("some-api-key", null, "some-company-name", true);

        verify(getRequestedFor(urlPathEqualTo(TRU_PROXY_SEARCH_PATH))
                .withQueryParam("Query", equalTo("some-company-name"))
                .withHeader("Accept", equalTo("application/json"))
                .withHeader(X_API_KEY, equalTo("some-api-key")));
        verify(getRequestedFor(urlPathEqualTo(TRU_PROXY_OFFICERS_PATH))
                .withQueryParam("CompanyNumber", equalTo("some-company-number"))
                .withHeader("Accept", equalTo("application/json"))
                .withHeader(X_API_KEY, equalTo("some-api-key")));

        assertThat(companyRecords).containsExactly(
                aCompany()
                        .withCompanyNumber("some-company-number")
                        .withCompanyType("some-company-type")
                        .withCompanyStatus("active")
                        .withTitle("some-title")
                        .withDateOfCreation("some-date-of-creation")
                        .withAddress(
                                anAddress()
                                        .withAddressLine("some-company-address-line")
                                        .withPostalCode("some-company-postal-code")
                                        .withLocality("some-company-locality")
                                        .withCountry("some-company-country")
                                        .withPremises("some-company-premises")
                        )
                        .withOfficers(
                                anOfficer()
                                        .withName("some-officer-name")
                                        .withOfficerRole("some-officer-role")
                                        .withAppointedOn("some-appointed-on")
                                        .withAddress(
                                                anAddress()
                                                        .withAddressLine("some-officer-address-line")
                                                        .withPostalCode("some-officer-postal-code")
                                                        .withLocality("some-officer-locality")
                                                        .withCountry("some-officer-country")
                                                        .withPremises("some-officer-premises")))
                        .build()
        );
    }
}