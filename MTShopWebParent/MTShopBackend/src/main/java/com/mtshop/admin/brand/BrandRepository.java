package com.mtshop.admin.brand;

import com.mtshop.common.entity.Brand;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface BrandRepository extends PagingAndSortingRepository<Brand, Integer> {
    Long countById(Integer id);

    Brand findByName(String name);
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
//
//    Category findByAlias(String alias);
//
}
