package com.example.risknarrative.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.WRITE_ONLY;

public class Officer {
    private String name;
    private  String officerRole;
    private  String appointedOn;
    @JsonProperty(access = WRITE_ONLY)
    private  String resignedOn;
    private  Address address;

    public Officer(
            String name,
            String officerRole,
            String appointedOn,
            @JsonProperty(access = WRITE_ONLY) String resignedOn,
            Address address) {
        this.name = name;
        this.officerRole = officerRole;
        this.appointedOn = appointedOn;
        this.resignedOn = resignedOn;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOfficerRole() {
        return officerRole;
    }

    public void setOfficerRole(String officerRole) {
        this.officerRole = officerRole;
    }

    public String getAppointedOn() {
        return appointedOn;
    }

    public void setAppointedOn(String appointedOn) {
        this.appointedOn = appointedOn;
    }

    public String getResignedOn() {
        return resignedOn;
    }

    public void setResignedOn(String resignedOn) {
        this.resignedOn = resignedOn;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Officer[" +
                "name=" + name + ", " +
                "officerRole=" + officerRole + ", " +
                "appointedOn=" + appointedOn + ", " +
                "resignedOn=" + resignedOn + ", " +
                "address=" + address + ']';
    }

}
