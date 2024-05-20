package com.example.risknarrative.builders;

import com.example.risknarrative.domain.Address;
import com.example.risknarrative.domain.Company;
import com.example.risknarrative.domain.Officer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class CompanyBuilder {

    private String companyNumber;
    private String companyType;
    private String title;
    private String companyStatus;
    private String dateOfCreation;
    private Address address;
    private List<Officer> officers = new ArrayList<>();

    public static CompanyBuilder aCompanyRecord() {
        return new CompanyBuilder();
    }

    public CompanyBuilder withOfficers(OfficerBuilder... officers) {
        this.officers = Stream.of(officers).map(OfficerBuilder::build).toList();
        return this;
    }

    public CompanyBuilder withAddress(Address address) {
        this.address = address;
        return this;
    }

    public CompanyBuilder withAddress(AddressBuilder address) {
        return this.withAddress(address.build());
    }

    public CompanyBuilder withDateOfCreation(String dateOfCreation) {
        this.dateOfCreation = dateOfCreation;
        return this;
    }

    public CompanyBuilder withCompanyStatus(String companyStatus) {
        this.companyStatus = companyStatus;
        return this;
    }

    public CompanyBuilder withTitle(String title) {
        this.title = title;
        return this;
    }

    public CompanyBuilder withCompanyType(String companyType) {
        this.companyType = companyType;
        return this;
    }

    public CompanyBuilder withCompanyNumber(String companyNumber) {
        this.companyNumber = companyNumber;
        return this;
    }

    public Company build() {
        return new Company(
                companyNumber,
                companyType,
                title,
                companyStatus,
                dateOfCreation,
                address,
                officers);
    }
}
