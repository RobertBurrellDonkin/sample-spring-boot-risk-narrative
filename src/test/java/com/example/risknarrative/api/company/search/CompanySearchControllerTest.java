package com.example.risknarrative.api.company.search;

import com.example.risknarrative.services.company.CompanyService;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.example.risknarrative.domain.AddressBuilder.anAddress;
import static com.example.risknarrative.domain.CompanyRecordsBuilder.aCompanyRecord;
import static com.example.risknarrative.domain.OfficerBuilder.anOfficer;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CompanySearchController.class)
class CompanySearchControllerTest {

    @MockBean
    private CompanyService companySearchService;

    @Test
    void searchWithCompanyNumberAndCompanyName(@Autowired MockMvc mvc) throws Exception {

        given(companySearchService.recordsByCompanyNumber("some-api-key", "some-company-number")).willReturn(
                List.of(
                        aCompanyRecord()
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
                                ).build(),
                        aCompanyRecord()
                                .withCompanyNumber("another-company-number")
                                .withCompanyType("another-company-type")
                                .withTitle("another-title")
                                .withCompanyStatus("another-company-status")
                                .withDateOfCreation("another-date-of-creation")
                                .withAddress(
                                        anAddress()
                                                .withLocality("another-company-locality")
                                                .withPostalCode("another-company-postal-code")
                                                .withPremises("another-company-premises")
                                                .withAddressLine("another-company-address-line1")
                                                .withCountry("another-company-country")
                                )
                                .withOfficers(
                                        anOfficer()
                                                .withName("another-officer-name")
                                                .withOfficerRole("another-officer-officerRole")
                                                .withAppointedOn("another-officer-appointed-on")
                                                .withAddress(
                                                        anAddress()
                                                                .withLocality("another-officer-locality")
                                                                .withPostalCode("another-officer-postal-code")
                                                                .withPremises("another-officer-premises")
                                                                .withAddressLine("another-officer-address-line1")
                                                                .withCountry("another-officer-country")
                                                )
                                ).build()
                )
        );

        var expectedSearchResults = """
                {
                  "total_results": 2,
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
                    },
                    {
                      "company_number": "another-company-number",
                      "company_type": "another-company-type",
                      "title": "another-title",
                      "company_status": "another-company-status",
                      "date_of_creation": "another-date-of-creation",
                      "address": {
                        "locality": "another-company-locality",
                        "postal_code": "another-company-postal-code",
                        "premises": "another-company-premises",
                        "address_line_1": "another-company-address-line1",
                        "country": "another-company-country"
                      },
                      "officers": [
                        {
                          "name": "another-officer-name",
                          "officer_role": "another-officer-officerRole",
                          "appointed_on": "another-officer-appointed-on",
                          "address": {
                            "premises": "another-officer-premises",
                            "locality": "another-officer-locality",
                            "address_line_1": "another-officer-address-line1",
                            "country": "another-officer-country",
                            "postal_code": "another-officer-postal-code"
                          }
                        }
                      ]
                    }
                  ]
                }
                """;

        var actualSearchResults = mvc.perform(
                        post("/api/company/search")
                                .header("x-api-key", "some-api-key")
                                .contentType(APPLICATION_JSON) //TODO: Other Content Types
                                .content("""
                                        {
                                            "companyName" : "some-company-name",
                                            "companyNumber" : "some-company-number"
                                        }
                                        """))
                .andExpectAll(
                        status().isOk(),
                        content()
                                .contentType(APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(companySearchService).recordsByCompanyNumber("some-api-key", "some-company-number");

        JSONAssert.assertEquals(expectedSearchResults, actualSearchResults, JSONCompareMode.STRICT);
    }
}