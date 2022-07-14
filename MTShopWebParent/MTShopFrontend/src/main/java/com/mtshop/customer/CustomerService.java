package com.mtshop.customer;

import com.mtshop.common.entity.Country;
import com.mtshop.common.entity.Customer;
import com.mtshop.setting.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CustomerService {

    @Autowired private CountryRepository countryRepo;
    @Autowired private CustomerRepository customerRepo;

    public List<Country> listAllCountries() {
        return countryRepo.findAllByOrderByNameAsc();
    }

    public boolean isEmailUnique(String email) {
        Customer customer = customerRepo.findByEmail(email);

        return customer == null;
    }
}
