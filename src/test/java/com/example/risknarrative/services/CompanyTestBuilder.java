package com.example.risknarrative.services;

import com.example.risknarrative.domain.AddressBuilder;
import com.example.risknarrative.domain.Company;

import static com.example.risknarrative.domain.AddressBuilder.anAddress;

public class CompanyTestBuilder {

    private String companyNumber;
    private String companyType;
    private String title;
    private String companyStatus;
    private String dateOfCreation;
    private AddressBuilder address = anAddress();

    public static CompanyTestBuilder aCompany() {
        return new CompanyTestBuilder();
    }

    public CompanyTestBuilder withAddress(AddressBuilder address) {
        this.address = address;
        return this;
    }

    public CompanyTestBuilder withDateOfCreation(String dateOfCreation) {
        this.dateOfCreation = dateOfCreation;
        return this;
    }

    public CompanyTestBuilder withCompanyStatus(String companyStatus) {
        this.companyStatus = companyStatus;
        return this;
    }

    public CompanyTestBuilder withTitle(String title) {
        this.title = title;
        return this;
    }

    public CompanyTestBuilder withCompanyType(String companyType) {
        this.companyType = companyType;
        return this;
    }

    public CompanyTestBuilder withCompanyNumber(String companyNumber) {
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
                address.build());
    }
}
