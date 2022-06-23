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
//
//    private List<Brand> listHierarchicalCategories(List<Brand> rootCategories, String sortDir) {
//        List<Brand> hierarchicalCategories = new ArrayList<>();
//
//        for (Brand rootCategory : rootCategories) {
//            hierarchicalCategories.add(Brand.copyFull(rootBrand));
//
//            Set<Brand> children = sortSubCategories(rootBrand.getChildren(), sortDir);
//
//            for (Brand subBrand : children) {
//                String name = "--" + subBrand.getName();
//                hierarchicalCategories.add(Brand.copyFull(subBrand, name));
//
//                listSubHierachicalCategories(hierarchicalCategories, subBrand, 1, sortDir);
//            }
//        }
//
//        return hierarchicalCategories;
//    }
//
//    private void listSubHierachicalCategories(List<Brand> hierarchicalCategories, Brand parent, int subLevel,
//                                              String sortDir) {
//        Set<Brand> children = sortSubCategories(parent.getChildren(), sortDir);
//        int newSubLevel = subLevel + 1;
//
//        for (Brand subBrand : children) {
//            String name = "";
//
//            for (int i = 0; i < newSubLevel; i++) {
//                name += "--";
//            }
//
//            name += subBrand.getName();
//
//            hierarchicalCategories.add(Brand.copyFull(subBrand, name));
//
//            listSubHierachicalCategories(hierarchicalCategories, subBrand, newSubLevel, sortDir);
//        }
//    }
//
//    public List<Brand> listCategoriesUsedInForm() {
//        List<Brand> categoriesUsedInForm = new ArrayList<>();
//
//        Iterable<Brand> categoriesInDB = brandRepo.findByParentIsNull(Sort.by("name").ascending());
//
//        for (Brand category : categoriesInDB) {
//            if (category.getParent() == null) {
//                categoriesUsedInForm.add(Brand.copyIdAndName(category));
//
//                Set<Brand> children = sortSubCategories(category.getChildren());
//
//                for (Brand subBrand : children) {
//                    String name = "--" + subBrand.getName();
//
//                    categoriesUsedInForm.add(Brand.copyIdAndName(subBrand.getId(), name));
//
//                    listSubCategoriesUsedInForm(categoriesUsedInForm, subBrand, 1);
//                }
//            }
//        }
//        return categoriesUsedInForm;
//    }
//
//    private void listSubCategoriesUsedInForm(List<Brand> categoriesUsedInForm, Brand parent, int subLevel) {
//        int newSubLevel = subLevel + 1;
//        Set<Brand> children = sortSubCategories(parent.getChildren());
//
//        for (Brand subBrand : children) {
//            String name = "";
//
//            for (int i = 0; i < newSubLevel; i++) {
//                name += "--";
//            }
//
//            name += subBrand.getName();
//
//            categoriesUsedInForm.add(Brand.copyIdAndName(subBrand.getId(), name));
//
//            listSubCategoriesUsedInForm(categoriesUsedInForm, subBrand, newSubLevel);
//        }
//    }

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

//    public String checkUnique(Integer id, String name, String alias) {
//        boolean isCreatingNew = (id == null || id == 0);
//
//        Brand categoryByName = brandRepo.findByName(name);
//
//        if (isCreatingNew) {
//            if (categoryByName != null) {
//                return "DuplicateName";
//            } else {
//                Brand categoryByAlias = brandRepo.findByAlias(alias);
//                if (categoryByAlias != null) return "DuplicateAlias";
//            }
//        } else {
//            if (categoryByName != null && categoryByName.getId() != id) return "DuplicateName";
//
//            Brand categoryByAlias = brandRepo.findByAlias(alias);
//            if (categoryByAlias != null && categoryByAlias.getId() != id) return "DuplicateAlias";
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
