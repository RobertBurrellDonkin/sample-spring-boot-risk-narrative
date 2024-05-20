package com.example.risknarrative;

import com.example.risknarrative.controlllers.CompanySearchController;
import com.example.risknarrative.services.TruProxyWebClient;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import static com.example.risknarrative.builders.AddressBuilder.anAddress;
import static com.example.risknarrative.builders.CompanyBuilder.aCompany;
import static com.example.risknarrative.builders.CompanySearchResultsBuilder.searchResults;
import static com.example.risknarrative.builders.OfficerBuilder.anOfficer;
import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class JsonTests {

    @Nested
    class CompanySearchTests {
        @Autowired
        private JacksonTester<CompanySearchController.CompanySearchRequest> json;

        @Nested
        class DeserializationTests {
            @Test
            void allFields() throws Exception {
                var content = """
                        {
                            "companyName" : "some-company-name",
                            "companyNumber" : "some-company-number"
                        }
                        """;
                assertThat(json.parseObject(content).companyName()).isEqualTo("some-company-name");
                assertThat(json.parseObject(content).companyNumber()).isEqualTo("some-company-number");
            }
        }
    }

    @Nested
    class CompanySearchResultsTests {
        @Autowired
        private JacksonTester<CompanySearchController.CompanySearchResults> json;

        @Nested
        class SerializationTests {
            @Test
            void allFields() throws Exception {
                var searchResults =
                        searchResults()
                                .withItems(
                                        aCompany()
                                                .withCompanyNumber("some-company-number")
                                                .withCompanyType("some-company-type")
                                                .withTitle("some-title")
                                                .withCompanyStatus("some-company-status")
                                                .withDateOfCreation("some-date-of-creation")
                                                .withAddress(
                                                        anAddress()
                                                                .withLocality("some-company-locality")
                                                                .withPostalCode("some-company-postal-code")
                                                                .withPremises("some-company-premises")
                                                                .withAddressLine("some-company-address-line1")
                                                                .withCountry("some-company-country")
                                                )
                                                .withOfficers(
                                                        anOfficer()
                                                                .withName("some-officer-name")
                                                                .withOfficerRole("some-officer-officerRole")
                                                                .withAppointedOn("some-officer-appointed-on")
                                                                .withResignedOn("some-officer-resigned-on")
                                                                .withAddress(
                                                                        anAddress()
                                                                                .withLocality("some-officer-locality")
                                                                                .withPostalCode("some-officer-postal-code")
                                                                                .withPremises("some-officer-premises")
                                                                                .withAddressLine("some-officer-address-line1")
                                                                                .withCountry("some-officer-country")
                                                                )
                                                )

                                )
                                .build();


                var expectedJson = """
                        {
                            "total_results": 1,
                            "items": [
                                {
                                    "company_number": "some-company-number",
                                    "company_type": "some-company-type",
                                    "title": "some-title",
                                    "company_status": "some-company-status",
                                    "date_of_creation": "some-date-of-creation",
                                    "address": {
                                        "locality": "some-company-locality",
                                        "postal_code": "some-company-postal-code",
                                        "premises": "some-company-premises",
                                        "address_line_1": "some-company-address-line1",
                                        "country": "some-company-country"
                                    },
                                    "officers": [
                                        {
                                            "name": "some-officer-name",
                                            "officer_role": "some-officer-officerRole",
                                            "appointed_on": "some-officer-appointed-on",
                                            "address": {
                                                "premises": "some-officer-premises",
                                                "locality": "some-officer-locality",
                                                "address_line_1": "some-officer-address-line1",
                                                "country": "some-officer-country",
                                                "postal_code": "some-officer-postal-code"
                                            }
                                        }
                                    ]
                                }
                            ]
                        }
                        """;
                assertThat(json.write(searchResults)).isStrictlyEqualToJson(expectedJson);
            }
        }
    }

    @Nested
    class OfficerResults {
        @Autowired
        private JacksonTester<TruProxyWebClient.OfficerResults> json;

        @Nested
        class Deserialization {
            @Test
            void allFields() throws Exception {
                var content = """
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
                                        "address_line_1": "some-address-line"
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
                                    "occupation": "Finance And Accounting",
                                    "country_of_residence": "United States",
                                    "nationality": "American"
                                }]
                          }
                        """;

                final var items = json.parseObject(content).items();
                assertThat(items).hasSize(1);

                final var officer = items.get(0);
                assertThat(officer).isNotNull();
                assertThat(officer.getName()).isEqualTo("some-name");
                assertThat(officer.getAppointedOn()).isEqualTo("some-appointed-on");
                assertThat(officer.getResignedOn()).isEqualTo("some-resigned-on");
                assertThat(officer.getOfficerRole()).isEqualTo("some-officer-role");

                final var address = officer.getAddress();
                assertThat(address).isNotNull();
                assertThat(address.getPostalCode()).isEqualTo("some-postal-code");
                assertThat(address.getCountry()).isEqualTo("some-country");
                assertThat(address.getLocality()).isEqualTo("some-locality");
                assertThat(address.getPremises()).isEqualTo("some-premises");
                assertThat(address.getAddressLine()).isEqualTo("some-address-line");

            }
        }
    }

    @Nested
    class CompanyResults {

        @Autowired
        private JacksonTester<TruProxyWebClient.CompanyResults> json;

        @Nested
        class Deserialization {
            @Test
            void allFields() throws Exception {
                var content = """
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
                                   "title": "some-company-title",
                                   "company_type": "some-company-type",
                                   "address": {
                                       "premises": "some-premises",
                                       "postal_code": "some-postal-code",
                                       "country": "some-country",
                                       "locality": "some-locality",
                                       "address_line_1": "some-address-line"
                                   },
                                   "kind": "searchresults#company",
                                   "description_identifier": [
                                       "incorporated-on"
                                   ]
                               }]
                         }
                        """;

                assertThat(json.parseObject(content).items())
                        .containsExactly(
                                aCompany()
                                        .withCompanyStatus("some-company-status")
                                        .withCompanyNumber("some-company-number")
                                        .withDateOfCreation("some-date-of-creation")
                                        .withCompanyType("some-company-type")
                                        .withTitle("some-company-title")
                                        .withAddress(
                                                anAddress()
                                                        .withAddressLine("some-address-line")
                                                        .withPremises("some-premises")
                                                        .withCountry("some-country")
                                                        .withLocality("some-locality")
                                                        .withPostalCode("some-postal-code")
                                        )
                                        .build()
                        );
            }
        }
    }
}