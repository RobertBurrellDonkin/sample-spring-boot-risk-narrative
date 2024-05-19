package com.example.risknarrative.services.company;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import static com.example.risknarrative.domain.AddressBuilder.anAddress;
import static com.example.risknarrative.domain.OfficerBuilder.anOfficer;
import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class OfficerResultsTest {

    @Autowired
    private JacksonTester<OfficerResults> json;

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
                                "resigned_on": "2018-02-12",
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

            assertThat(json.parseObject(content).items())
                    .containsExactly(
                            anOfficer()
                                    .withName("some-name")
                                    .withOfficerRole("some-officer-role")
                                    .withAppointedOn("some-appointed-on")
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
