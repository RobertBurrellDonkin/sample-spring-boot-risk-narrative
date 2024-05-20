package com.example.risknarrative.responsitories;

import com.example.risknarrative.domain.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, String> {

}
