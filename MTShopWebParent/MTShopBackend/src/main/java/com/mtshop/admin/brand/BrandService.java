package com.mtshop.admin.brand;

import com.mtshop.common.entity.Brand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class BrandService {

    public static final int ROOT_CATEGORIES_PER_PAGE = 3;

    @Autowired
    private BrandRepository brandRepo;


    public List<Brand> listByPage(String sortType, int pageNum, BrandPageInfo pageInfo, String keyword) {
//        Sort sort = Sort.by("name");
//
//        if (sortType.equals("asc")) {
//            sort = sort.ascending();
//        } else {
//            sort = sort.descending();
//        }
//
//        Pageable pageable = PageRequest.of(pageNum - 1, ROOT_CATEGORIES_PER_PAGE, sort);
//
//        Page<Brand> pageCategories = null;
//        if (keyword != null && !keyword.isEmpty()) {
//            pageCategories = brandRepo.findByName(pageable, keyword);
//        } else {
//            pageCategories = brandRepo.findByParentIsNull(pageable);
//        }
//
//        List<Brand> rootCategories = pageCategories.getContent();
//
//        pageInfo.setTotalPages(pageCategories.getTotalPages());
//        pageInfo.setTotalElements(pageCategories.getTotalElements());
//
//        if (keyword != null && !keyword.isEmpty()) {
//            List<Brand> searchResult = pageCategories.getContent();
//            for (Brand category : searchResult) {
//                category.setHasChildren(category.getChildren().size() > 0);
//            }
//
//            return searchResult;
//        } else {
//            return listHierarchicalCategories(rootCategories, sortType);
//        }

        return (List<Brand>) brandRepo.findAll();
    }

    public Brand save(Brand category) {
        return brandRepo.save(category);
    }

    public Brand get(Integer id) throws BrandNotFoundException {
        try {
            return brandRepo.findById(id).get();
        } catch (NoSuchElementException ex) {
            throw new BrandNotFoundException("Could not find any brand with ID " + id);
        }
    }

    public void delete(Integer id) throws BrandNotFoundException {
        Long countById = brandRepo.countById(id);

        if (countById == null || countById == 0) {
            throw new BrandNotFoundException("Could not find any brand with ID: " + id);
        }

        brandRepo.deleteById(id);
    }

    public String checkUnique(Integer id, String name) {
        boolean isCreatingNew = (id == null || id == 0);

        Brand brandByName = brandRepo.findByName(name);

        if (isCreatingNew) {
            if (brandByName != null)
                return "Duplicate";
        } else {
            if (brandByName != null && brandByName.getId() != id)
                return "Duplicate";
        }

        return "OK";
    }
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
