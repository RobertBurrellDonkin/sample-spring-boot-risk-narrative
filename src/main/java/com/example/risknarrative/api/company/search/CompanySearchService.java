package com.example.risknarrative.api.company.search;

import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.Collections.emptyList;

@Service
public class CompanySearchService {
    public List<Item> searchByNumber(String number) {
        return emptyList();
    }
}
