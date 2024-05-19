package com.example.risknarrative.api.company.search;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import static com.example.risknarrative.api.company.search.AddressBuilder.anAddress;
import static com.example.risknarrative.api.company.search.ItemBuilder.anItem;
import static com.example.risknarrative.api.company.search.OfficerBuilder.anOfficer;
import static com.example.risknarrative.api.company.search.SearchResultsTestBuilder.searchResults;
import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class CompanySearchJsonTests {

    @Nested
    class CompanySearchTests {
        @Autowired
        private JacksonTester<CompanySearch> json;

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

            //TODO: perm out
        }
    }


    @Nested
    class SearchResultsTests {
        @Autowired
        private JacksonTester<SearchResults> json;

        @Nested
        class SerializationTests {
            @Test
            void allFields() throws Exception {
                var searchResults =
                        searchResults()
                                .withItems(
                                        anItem()
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
                assertThat(json.write(searchResults)).isEqualToJson(expectedJson);
            }

            //TODO: perm out
        }
    }
}