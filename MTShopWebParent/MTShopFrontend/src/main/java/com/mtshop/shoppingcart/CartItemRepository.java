package com.mtshop.shoppingcart;

import com.mtshop.common.entity.CartItem;
import com.mtshop.common.entity.Customer;
import com.mtshop.common.entity.product.Product;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CartItemRepository extends CrudRepository<CartItem, Integer> {

	List<CartItem> findByCustomer(Customer customer);
	
	CartItem findByCustomerAndProduct(Customer customer, Product product);
	
	@Modifying
	@Query("UPDATE CartItem c SET c.quantity = ?1 WHERE c.customer.id = ?2 AND c.product.id = ?3")
	void updateQuantity(Integer quantity, Integer customerId, Integer productId);

	@Modifying
	void deleteByCustomerAndProduct(Integer customerId, Integer productId);
}