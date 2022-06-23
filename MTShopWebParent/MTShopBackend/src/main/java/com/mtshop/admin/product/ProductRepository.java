package com.mtshop.admin.product;

import com.mtshop.common.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ProductRepository extends PagingAndSortingRepository<Product, Integer> {
//    Long countById(Integer id);

//    Brand findByName(String name);
//
//    @Query("SELECT b FROM Brand b WHERE b.name LIKE %?1%")
//    Page<Brand> findAll(Pageable pageable, String keyword);
}
