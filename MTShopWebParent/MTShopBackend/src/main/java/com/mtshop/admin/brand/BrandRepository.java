package com.mtshop.admin.brand;

import com.mtshop.common.entity.Brand;
import com.mtshop.common.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface BrandRepository extends PagingAndSortingRepository<Brand, Integer> {
    Long countById(Integer id);
//    @Query("UPDATE Category c SET c.enabled = ?2 WHERE c.id = ?1")
//    @Modifying
//    void updateEnabledStatus(Integer id, boolean enabled);
//
//    List<Category> findByParentIsNull(Sort sort);
//
//    Page<Category> findByParentIsNull(Pageable pageable);
//
//    Page<Category> findByName(Pageable pageable, String keyword);
//
//    Category findByName(String name);
//
//    Category findByAlias(String alias);
//
}
