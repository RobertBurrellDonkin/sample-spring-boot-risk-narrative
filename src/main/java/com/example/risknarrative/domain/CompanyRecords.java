package com.example.risknarrative.domain;

import java.util.List;
import java.util.Objects;

public class CompanyRecords {
    private String companyNumber;
    private String companyType;
    private String title;
    private String companyStatus;
    private String dateOfCreation;
    private Address address;
    private List<Officer> officers;

    public CompanyRecords(
            String companyNumber,
            String companyType,
            String title,
            String companyStatus,
            String dateOfCreation,
            Address address,
            List<Officer> officers
    ) {
        this.companyNumber = companyNumber;
        this.companyType = companyType;
        this.title = title;
        this.companyStatus = companyStatus;
        this.dateOfCreation = dateOfCreation;
        this.address = address;
        this.officers = officers;
    }

    public String getCompanyNumber() {
        return companyNumber;
    }

    public void setCompanyNumber(String companyNumber) {
        this.companyNumber = companyNumber;
    }

    public String getCompanyType() {
        return companyType;
    }

    public void setCompanyType(String companyType) {
        this.companyType = companyType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCompanyStatus() {
        return companyStatus;
    }

    public void setCompanyStatus(String companyStatus) {
        this.companyStatus = companyStatus;
    }

    public String getDateOfCreation() {
        return dateOfCreation;
    }

    public void setDateOfCreation(String dateOfCreation) {
        this.dateOfCreation = dateOfCreation;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public List<Officer> getOfficers() {
        return officers;
    }

    public void setOfficers(List<Officer> officers) {
        this.officers = officers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompanyRecords that = (CompanyRecords) o;
        return Objects.equals(companyNumber, that.companyNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(companyNumber);
    }

    @Override
    public String toString() {
        return "CompanyRecords[" +
                "companyNumber=" + companyNumber + ", " +
                "companyType=" + companyType + ", " +
                "title=" + title + ", " +
                "companyStatus=" + companyStatus + ", " +
                "dateOfCreation=" + dateOfCreation + ", " +
                "address=" + address + ", " +
                "officers=" + officers + ']';
    }

}
