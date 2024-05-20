package com.example.risknarrative.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Address {
    @Id
    @GeneratedValue
    @JsonIgnore
    private Long id;
    @Column
    private String locality;
    @Column
    private String postalCode;
    @Column
    private String premises;
    @Column
    @JsonProperty("address_line_1")
    private String addressLine;
    @Column
    private String country;

    public Address(){}

    public Address(
            String locality,
            String postalCode,
            String premises,
            @JsonProperty("address_line_1") String addressLine,
            String country) {
        this.locality = locality;
        this.postalCode = postalCode;
        this.premises = premises;
        this.addressLine = addressLine;
        this.country = country;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getPremises() {
        return premises;
    }

    public void setPremises(String premises) {
        this.premises = premises;
    }

    @JsonProperty("address_line_1")
    public String getAddressLine() {
        return addressLine;
    }

    public void setAddressLine(String addressLine) {
        this.addressLine = addressLine;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return "Address[" +
                "locality=" + locality + ", " +
                "postalCode=" + postalCode + ", " +
                "premises=" + premises + ", " +
                "addressLine=" + addressLine + ", " +
                "country=" + country + ']';
    }

}
