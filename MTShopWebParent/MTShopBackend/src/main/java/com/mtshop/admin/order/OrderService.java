package com.mtshop.admin.order;

import com.mtshop.common.entity.order.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class OrderService {
    public static final int ORDERS_PER_PAGE = 5;

    @Autowired
    private OrderRepository repo;

    public Page<Order> listByPage(int pageNum, String sortField, String sortType, String keyword) {
        Sort sort = null;

        if ("destination".equals(sortField)) {
            sort = Sort.by("country").and(Sort.by("state")).and(Sort.by("city"));
        } else {
            sort = Sort.by(sortField);
        }

        sort = sortType.equals("asc") ? sort.ascending() : sort.descending();

        Pageable pageable = PageRequest.of(pageNum - 1, ORDERS_PER_PAGE, sort);

        if (keyword != null)
            return repo.findAll(keyword, pageable);

        return repo.findAll("", pageable);
    }

    public Order get(Integer id) throws OrderNotFoundException {
        try {
            return repo.findById(id).get();
        } catch (NoSuchElementException ex) {
            throw new OrderNotFoundException("Could not find any orders with ID " + id);
        }
    }

    public void delete(Integer id) throws OrderNotFoundException {
        Long count = repo.countById(id);
        if (count == null || count == 0) {
            throw new OrderNotFoundException("Could not find any orders with ID " + id);
        }

        repo.deleteById(id);
    }
}
