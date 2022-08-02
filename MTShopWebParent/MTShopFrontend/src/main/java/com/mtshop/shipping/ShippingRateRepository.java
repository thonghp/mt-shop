package com.mtshop.shipping;

import com.mtshop.common.entity.Country;
import com.mtshop.common.entity.ShippingRate;
import org.springframework.data.repository.CrudRepository;

public interface ShippingRateRepository extends CrudRepository<ShippingRate, Integer> {
	
	ShippingRate findByCountryAndState(Country country, String state);
}
