package com.example.risknarrative.domain;

public class OfficerBuilder {


    private String name;
    private String officerRole;
    private String appointedOn;
    private AddressBuilder address;

    public static OfficerBuilder anOfficer() {
        return new OfficerBuilder();
    }

    public OfficerBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public OfficerBuilder withOfficerRole(String officerRole) {
        this.officerRole = officerRole;
        return this;
    }

    public OfficerBuilder withAppointedOn(String appointedOn) {
        this.appointedOn = appointedOn;
        return this;
    }

    public OfficerBuilder withAddress(AddressBuilder address) {
        this.address = address;
        return this;
    }

    public Officer build() {
        return new Officer(name, officerRole, appointedOn, address.build());
    }
}
