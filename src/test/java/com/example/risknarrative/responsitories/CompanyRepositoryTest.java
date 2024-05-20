package com.example.risknarrative.responsitories;

import com.example.risknarrative.domain.Address;
import com.example.risknarrative.domain.Company;
import com.example.risknarrative.domain.Officer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest()
public class CompanyRepositoryTest {
    @Autowired private CompanyRepository companyRepository;
    private Company testCompany;

    @BeforeEach
    public void setUp() {
        final var address = new Address();
        address.setAddressLine("some-address-line");
        address.setCountry("some-country");
        address.setLocality("some-locality");
        address.setPostalCode("some-postal-code");
        address.setPremises("some-premises");

        testCompany = new Company("some-company-number");
        testCompany.setCompanyStatus("some-company-status");
        testCompany.setCompanyType("some-company-type");
        testCompany.setDateOfCreation("some-date-of-creation");
        testCompany.setCompanyType("some-company-type");

        testCompany.setAddress(address);

        final var officer = new Officer();
        officer.setName("some-officer-name");
        officer.setAppointedOn("some-appointed-on");
        officer.setOfficerRole("some-officer-role");
        testCompany.getOfficers().clear();
        testCompany.getOfficers().add(officer);

        companyRepository.save(testCompany);
    }

    @AfterEach
    public void tearDown() {
        companyRepository.deleteAll();
    }

    @Test
    public void findByCompanyNumber() {
        final var savedCompany = companyRepository.findById(testCompany.getCompanyNumber()).orElseThrow();

        assertThat(savedCompany.getCompanyStatus()).isEqualTo("some-company-status");
        assertThat(savedCompany.getCompanyType()).isEqualTo("some-company-type");
        assertThat(savedCompany.getDateOfCreation()).isEqualTo("some-date-of-creation");
        assertThat(savedCompany.getCompanyNumber()).isEqualTo("some-company-number");

        final var savedAddress = savedCompany.getAddress();
        assertThat(savedAddress.getAddressLine()).isEqualTo("some-address-line");
        assertThat(savedAddress.getCountry()).isEqualTo("some-country");
        assertThat(savedAddress.getLocality()).isEqualTo("some-locality");
        assertThat(savedAddress.getPostalCode()).isEqualTo("some-postal-code");
        assertThat(savedAddress.getPremises()).isEqualTo("some-premises");

        assertThat(savedCompany.getOfficers()).hasSize(1);

        final var officer = savedCompany.getOfficers().get(0);
        assertThat(officer.getName()).isEqualTo("some-officer-name");
        assertThat(officer.getAppointedOn()).isEqualTo("some-appointed-on");
        assertThat(officer.getOfficerRole()).isEqualTo("some-officer-role");
    }

    @Test
    public void save() {
        final var savedCompany = companyRepository.findById(testCompany.getCompanyNumber()).orElseThrow();
        savedCompany.setCompanyStatus("another-company-status");
        savedCompany.setTitle("another-title");
        savedCompany.setCompanyType("another-company-type");
        savedCompany.setDateOfCreation("another-date-of-creation");

        Address anotherAddress = new Address();
        anotherAddress.setAddressLine("another-address-line");
        anotherAddress.setCountry("another-country");
        anotherAddress.setLocality("another-locality");
        anotherAddress.setPostalCode("another-postal-code");
        anotherAddress.setPremises("another-premises");
        savedCompany.setAddress(anotherAddress);

        final var anotherOfficer = new Officer();
        anotherOfficer.setName("another-officer-name");
        anotherOfficer.setAppointedOn("another-appointed-on");
        anotherOfficer.setOfficerRole("another-officer-role");
        savedCompany.getOfficers().clear();
        savedCompany.getOfficers().add(anotherOfficer);

        final var updatedCompany = companyRepository.save(savedCompany);
        assertThat(updatedCompany.getCompanyStatus()).isEqualTo("another-company-status");
        assertThat(updatedCompany.getCompanyType()).isEqualTo("another-company-type");
        assertThat(updatedCompany.getDateOfCreation()).isEqualTo("another-date-of-creation");
        assertThat(updatedCompany.getCompanyNumber()).isEqualTo("some-company-number");

        Address updatedAddress = updatedCompany.getAddress();
        assertThat(updatedAddress.getAddressLine()).isEqualTo("another-address-line");
        assertThat(updatedAddress.getCountry()).isEqualTo("another-country");
        assertThat(updatedAddress.getLocality()).isEqualTo("another-locality");
        assertThat(updatedAddress.getPostalCode()).isEqualTo("another-postal-code");
        assertThat(updatedAddress.getPremises()).isEqualTo("another-premises");

        assertThat(savedCompany.getOfficers()).hasSize(1);

        final var officer = savedCompany.getOfficers().get(0);
        assertThat(officer.getName()).isEqualTo("another-officer-name");
        assertThat(officer.getAppointedOn()).isEqualTo("another-appointed-on");
        assertThat(officer.getOfficerRole()).isEqualTo("another-officer-role");
    }
}
