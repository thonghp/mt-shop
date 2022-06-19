package com.mtshop.admin.category;

import com.mtshop.common.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class CategoryService {

    public static final int CATEGORY_PER_PAGE = 5;

    @Autowired
    private CategoryRepository categoryRepo;

    public List<Category> listAll() {
        return (List<Category>) categoryRepo.findAll();
    }

    public List<Category> listCategoriesUsedInForm() {
        List<Category> categoriesUsedInForm = new ArrayList<>();

        Iterable<Category> categoriesInDB = categoryRepo.findAll();

        for (Category category : categoriesInDB) {
            if (category.getParent() == null) {
                categoriesUsedInForm.add(Category.copyIdAndName(category));

                Set<Category> children = category.getChildren();

                for (Category subCategory : children) {
                    String name = "--" + subCategory.getName();

                    categoriesUsedInForm.add(Category.copyIdAndName(subCategory.getId(), name));

                    listChildren(categoriesUsedInForm, subCategory, 1);
                }
            }
        }
        return categoriesUsedInForm;
    }

    private void listChildren(List<Category> categoriesUsedInForm, Category parent, int subLevel) {
        int newSubLevel = subLevel + 1;
        Set<Category> children = parent.getChildren();

        for (Category subCategory : children) {
            String name = "";

            for (int i = 0; i < newSubLevel; i++) {
                name += "--";
            }

            name += subCategory.getName();

            categoriesUsedInForm.add(Category.copyIdAndName(subCategory.getId(), name));

            listChildren(categoriesUsedInForm, subCategory, newSubLevel);
        }
    }

    public Page<Category> listByPage(int pageNum, String sortField, String sortType, String keyword) {
        Sort sort = Sort.by(sortField);
        sort = sortType.equals("desc") ? sort.descending() : sort.ascending();

        Pageable pageable = PageRequest.of(pageNum - 1, CATEGORY_PER_PAGE, sort);

        if (keyword != null)
            return categoryRepo.findAll(keyword, pageable);

        return categoryRepo.findAll(pageable);
    }

    public Category save(Category category) {
        return categoryRepo.save(category);
    }

    public boolean isNameUnique(Integer id, String email) {
//        Category CategoryByEmail = categoryRepo.findByEmail(email);
//
//        if (CategoryByEmail == null) return true;
//
//        boolean isCreatingNew = (id == null);
//
//        if (isCreatingNew) {
//            if (CategoryByEmail != null) return false;
//        } else {
//            if (CategoryByEmail.getId() != id) return false;
//        }

        return true;
    }

//    public Category get(Integer id) throws CategoryNotFoundException {
//        try {
//            return categoryRepo.findById(id).get();
//        } catch (NoSuchElementException ex) {
//            throw new CategoryNotFoundException("Could not find any Category with ID" + id);
//        }
//    }

//    public void delete(Integer id) throws CategoryNotFoundException {
//        Long countById = categoryRepo.countById(id);
//
//        if (countById == null || countById == 0) {
//            throw new CategoryNotFoundException("Could not find any Category with ID" + id);
//        }
//
//        categoryRepo.deleteById(id);
//    }

    public void updateCategoryEnabledStatus(Integer id, boolean enabled) {
        categoryRepo.updateEnabledStatus(id, enabled);
    }

//    public Category getByEmail(String email) {
//        return categoryRepo.findByEmail(email);
//    }
}
