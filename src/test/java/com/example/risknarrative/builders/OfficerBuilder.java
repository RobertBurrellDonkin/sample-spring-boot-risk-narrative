package com.example.risknarrative.builders;

import com.example.risknarrative.domain.Officer;

public class OfficerBuilder {

    private String name;
    private String officerRole;
    private String appointedOn;
    private AddressBuilder address;
    private String resignedOn;

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

    public OfficerBuilder withResignedOn(String resignedOn) {
        this.resignedOn = resignedOn;
        return this;
    }

    public OfficerBuilder withAddress(AddressBuilder address) {
        this.address = address;
        return this;
    }

    public Officer build() {
        final Officer officer = new Officer();
        officer.setName(name);
        officer.setOfficerRole(officerRole);
        officer.setAppointedOn(appointedOn);
        officer.setResignedOn(resignedOn);
        officer.setAddress(address.build());
        return officer;
    }
}
