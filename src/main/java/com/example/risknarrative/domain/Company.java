package com.example.risknarrative.domain;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Company {
    @Id
    private String companyNumber;
    @Column
    private String companyType;
    @Column
    private String title;
    @Column
    private String companyStatus;
    @Column
    private String dateOfCreation;
    @JoinColumn()
    @OneToOne(cascade = CascadeType.ALL)
    private Address address;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn()
    private List<Officer> officers = new ArrayList<>();

    public Company() {
    }

    public Company(String companyNumber) {
        this.companyNumber = companyNumber;
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
        Company that = (Company) o;
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
