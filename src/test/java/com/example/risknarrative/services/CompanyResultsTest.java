package com.example.risknarrative.services;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import static com.example.risknarrative.builders.AddressBuilder.anAddress;
import static com.example.risknarrative.builders.CompanyBuilder.aCompanyRecord;
import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class CompanyResultsTest {

    @Autowired
    private JacksonTester<CompanyResults> json;

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
                            aCompanyRecord()
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
