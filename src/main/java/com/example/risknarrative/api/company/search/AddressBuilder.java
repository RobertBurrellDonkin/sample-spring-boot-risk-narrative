package com.example.risknarrative.api.company.search;

public class AddressBuilder {

    private String locality;
    private String postalCode;
    private String premises;
    private String addressLine1;
    private String country;

    public static AddressBuilder anAddress() {
        return new AddressBuilder();
    }

    public AddressBuilder withCountry(String country) {
        this.country = country;
        return this;
    }

    public AddressBuilder withAddressLine(String addressLine) {
        this.addressLine1 = addressLine;
        return this;
    }

    public AddressBuilder withPremises(String premises) {
        this.premises = premises;
        return this;
    }

    public AddressBuilder withPostalCode(String postalCode) {
        this.postalCode = postalCode;
        return this;
    }

    public AddressBuilder withLocality(String locality) {
        this.locality = locality;
        return this;
    }

    public Address build() {
        return new Address(
                locality,
                postalCode,
                premises,
                addressLine1,
                country);
    }
}
