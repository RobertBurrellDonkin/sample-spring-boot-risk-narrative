package com.example.risknarrative.api.company.search;

import java.util.ArrayList;
import java.util.List;

import static com.example.risknarrative.api.company.search.AddressBuilder.anAddress;

public class ItemBuilder {

    private String companyNumber;
    private String companyType;
    private String title;
    private String companyStatus;
    private String dateOfCreation;
    private AddressBuilder address = anAddress();
    private List<OfficerBuilder> officers = new ArrayList<>();

    public static ItemBuilder anItem() {
        return new ItemBuilder();
    }

    public ItemBuilder withOfficers(OfficerBuilder... officers) {
        this.officers = List.of(officers);
        return this;
    }

    public ItemBuilder  withAddress(AddressBuilder address) {
        this.address = address;
        return this;
    }

    public ItemBuilder withDateOfCreation(String dateOfCreation) {
        this.dateOfCreation = dateOfCreation;
        return this;
    }

    public ItemBuilder withCompanyStatus(String companyStatus) {
        this.companyStatus = companyStatus;
        return this;
    }

    public ItemBuilder withTitle(String title) {
        this.title = title;
        return this;
    }

    public ItemBuilder withCompanyType(String companyType) {
        this.companyType = companyType;
        return this;
    }

    public ItemBuilder withCompanyNumber(String companyNumber) {
        this.companyNumber = companyNumber;
        return this;
    }

    public Item build() {
        return new Item(
                companyNumber,
                companyType,
                title,
                companyStatus,
                dateOfCreation,
                address.build(),
                officers.stream().map(OfficerBuilder::build).toList());
    }
}
