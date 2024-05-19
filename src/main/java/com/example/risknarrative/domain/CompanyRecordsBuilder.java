package com.example.risknarrative.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class CompanyRecordsBuilder {

    private String companyNumber;
    private String companyType;
    private String title;
    private String companyStatus;
    private String dateOfCreation;
    private Address address;
    private List<Officer> officers = new ArrayList<>();

    public static CompanyRecordsBuilder aCompanyRecord() {
        return new CompanyRecordsBuilder();
    }

    public CompanyRecordsBuilder withOfficers(OfficerBuilder... officers) {
        this.officers = Stream.of(officers).map(OfficerBuilder::build).toList();
        return this;
    }

    public CompanyRecordsBuilder withOfficers(List<Officer> officers) {
        this.officers = officers;
        return this;
    }

    public CompanyRecordsBuilder withCompany(Company company) {
        return this
                .withCompanyStatus(company.companyStatus())
                .withCompanyNumber(company.companyNumber())
                .withCompanyType(company.companyType())
                .withTitle(company.title())
                .withDateOfCreation(company.dateOfCreation())
                .withAddress(company.address());
    }

    public CompanyRecordsBuilder withAddress(Address address) {
        this.address = address;
        return this;
    }

    public CompanyRecordsBuilder withAddress(AddressBuilder address) {
        return this.withAddress(address.build());
    }

    public CompanyRecordsBuilder withDateOfCreation(String dateOfCreation) {
        this.dateOfCreation = dateOfCreation;
        return this;
    }

    public CompanyRecordsBuilder withCompanyStatus(String companyStatus) {
        this.companyStatus = companyStatus;
        return this;
    }

    public CompanyRecordsBuilder withTitle(String title) {
        this.title = title;
        return this;
    }

    public CompanyRecordsBuilder withCompanyType(String companyType) {
        this.companyType = companyType;
        return this;
    }

    public CompanyRecordsBuilder withCompanyNumber(String companyNumber) {
        this.companyNumber = companyNumber;
        return this;
    }

    public CompanyRecords build() {
        return new CompanyRecords(
                companyNumber,
                companyType,
                title,
                companyStatus,
                dateOfCreation,
                address,
                officers);
    }
}
