package com.mtshop.admin.product;

import com.mtshop.common.entity.product.Product;
import com.mtshop.common.exception.ProductNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.NoSuchElementException;

@Service
@Transactional
public class ProductService {

    public static final int PRODUCTS_PER_PAGE = 5;

    @Autowired
    private ProductRepository productRepo;

    public Page<Product> listByPage(int pageNum, String sortField, String sortType, String keyword, Integer categoryId) {
        Sort sort = Sort.by(sortField);

        sort = sortType.equals("asc") ? sort.ascending() : sort.descending();

        Pageable pageable = PageRequest.of(pageNum - 1, PRODUCTS_PER_PAGE, sort);

        if (keyword != null && !keyword.isEmpty()) {
            if (categoryId != null && categoryId > 0) {
                String categoryIdMatch = "-" + String.valueOf(categoryId) + "-";
                return productRepo.searchInCategory(categoryId, categoryIdMatch, keyword, pageable);
            }

            return productRepo.findAll(keyword, pageable);
        }

        if (categoryId != null && categoryId > 0) {
            String categoryIdMatch = "-" + String.valueOf(categoryId) + "-";
            return productRepo.findAllInCategory(categoryId, categoryIdMatch, pageable);
        }

        return productRepo.findAll(pageable);
    }

    public Product save(Product product) {
        if (product.getId() == null)
            product.setCreatedTime(new Date());

        if (product.getAlias() == null || product.getAlias().isEmpty()) {
            String defaultAlias = product.getName().replaceAll(" ", "-");
            product.setAlias(defaultAlias);
        } else {
            product.setAlias(product.getAlias().replaceAll(" ", "-"));
        }

        product.setUpdatedTime(new Date());

        return productRepo.save(product);
    }

    public void saveProductPrice(Product productInForm) {
        Product productInDb = productRepo.findById(productInForm.getId()).get();
        productInDb.setCost(productInForm.getCost());
        productInDb.setPrice(productInForm.getPrice());
        productInDb.setDiscountPercent(productInForm.getDiscountPercent());

        productRepo.save(productInDb);
    }

    public Product get(Integer id) throws ProductNotFoundException {
        try {
            return productRepo.findById(id).get();
        } catch (NoSuchElementException ex) {
            throw new ProductNotFoundException("Could not find any product with ID " + id);
        }
    }

    public void delete(Integer id) throws ProductNotFoundException {
        Long countById = productRepo.countById(id);

        if (countById == null || countById == 0) {
            throw new ProductNotFoundException("Could not find any product with ID: " + id);
        }

        productRepo.deleteById(id);
    }

    public String checkUnique(Integer id, String name) {
        boolean isCreatingNew = (id == null || id == 0);

        Product productByName = productRepo.findByName(name);

        if (isCreatingNew) {
            if (productByName != null)
                return "Duplicate";
        } else {
            if (productByName != null && productByName.getId() != id)
                return "Duplicate";
        }

        return "OK";
    }

    public void updateProductEnabledStatus(Integer id, boolean enabled) {
        productRepo.updateEnabledStatus(id, enabled);
    }
}
