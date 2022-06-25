package com.mtshop.admin.product;

import com.mtshop.admin.brand.BrandNotFoundException;
import com.mtshop.admin.brand.BrandRepository;
import com.mtshop.common.entity.Brand;
import com.mtshop.common.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class ProductService {

//    public static final int BRAND_PER_PAGE = 5;
//
//    @Autowired
//    private BrandRepository brandRepo;

    @Autowired
    private ProductRepository productRepo;

    public List<Product> listAll() {
        return (List<Product>) productRepo.findAll();
    }


//    public Page<Brand> listByPage(int pageNum, String sortField, String sortType, String keyword) {
//        Sort sort = Sort.by(sortField);
//
//        sort = sortType.equals("asc") ? sort.ascending() : sort.descending();
//
//        Pageable pageable = PageRequest.of(pageNum - 1, BRAND_PER_PAGE, sort);
//
//        if (keyword != null)
//            return brandRepo.findAll(pageable, keyword);
//
//        return brandRepo.findAll(pageable, "");
//    }

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

//    public Brand get(Integer id) throws BrandNotFoundException {
//        try {
//            return brandRepo.findById(id).get();
//        } catch (NoSuchElementException ex) {
//            throw new BrandNotFoundException("Could not find any brand with ID " + id);
//        }
//    }
//
//    public void delete(Integer id) throws BrandNotFoundException {
//        Long countById = brandRepo.countById(id);
//
//        if (countById == null || countById == 0) {
//            throw new BrandNotFoundException("Could not find any brand with ID: " + id);
//        }
//
//        brandRepo.deleteById(id);
//    }
//
//    public String checkUnique(Integer id, String name) {
//        boolean isCreatingNew = (id == null || id == 0);
//
//        Brand brandByName = brandRepo.findByName(name);
//
//        if (isCreatingNew) {
//            if (brandByName != null)
//                return "Duplicate";
//        } else {
//            if (brandByName != null && brandByName.getId() != id)
//                return "Duplicate";
//        }
//
//        return "OK";
//    }
//
//    private SortedSet<Brand> sortSubCategories(Set<Brand> children) {
//        return sortSubCategories(children, "asc");
//    }
//
//    private SortedSet<Brand> sortSubCategories(Set<Brand> children, String sortDir) {
//        SortedSet<Brand> sortedSet = new TreeSet<>(new Comparator<Brand>() {
//            @Override
//            public int compare(Brand o1, Brand o2) {
//                if (sortDir.equals("asc")) {
//                    return o1.getName().compareTo(o2.getName());
//                } else {
//                    return o2.getName().compareTo(o1.getName());
//                }
//            }
//        });
//
//        sortedSet.addAll(children);
//
//        return sortedSet;
//    }


//    public void updateBrandEnabledStatus(Integer id, boolean enabled) {
//        categoryRepo.updateEnabledStatus(id, enabled);
//    }

//    public Category getByEmail(String email) {
//        return categoryRepo.findByEmail(email);
//    }
}