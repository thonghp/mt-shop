package com.mtshop.admin.customer;

import com.mtshop.admin.setting.country.CountryRepository;
import com.mtshop.common.entity.Customer;
import com.mtshop.common.exception.CustomerNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.NoSuchElementException;

@Service
@Transactional
public class CustomerService {
    public static final int CUSTOMERS_PER_PAGE = 5;

    @Autowired
    private CustomerRepository customerRepo;
    @Autowired
    private CountryRepository countryRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public Page<Customer> listByPage(int pageNum, String sortField, String sortType, String keyword) {
        Sort sort = Sort.by(sortField);
        sort = sortType.equals("desc") ? sort.descending() : sort.ascending();

        Pageable pageable = PageRequest.of(pageNum - 1, CUSTOMERS_PER_PAGE, sort);

        if (keyword != null)
            return customerRepo.findAll(keyword, pageable);

        return customerRepo.findAll(pageable);
    }

    public void updateCustomerEnabledStatus(Integer id, boolean enabled) {
        customerRepo.updateEnabledStatus(id, enabled);
    }

    public Customer get(Integer id) throws CustomerNotFoundException {
        try {
            return customerRepo.findById(id).get();
        } catch (NoSuchElementException ex) {
            throw new CustomerNotFoundException("Could not find any customers with ID " + id);
        }
    }

    public void delete(Integer id) throws CustomerNotFoundException {
        Long count = customerRepo.countById(id);
        if (count == null || count == 0) {
            throw new CustomerNotFoundException("Could not find any customers with ID " + id);
        }

        customerRepo.deleteById(id);
    }
}
