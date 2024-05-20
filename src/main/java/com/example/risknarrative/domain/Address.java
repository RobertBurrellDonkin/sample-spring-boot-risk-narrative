package com.example.risknarrative.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "ADDRESSES")
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return Objects.equals(id, address.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
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
