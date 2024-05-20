package com.example.risknarrative.services;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

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
