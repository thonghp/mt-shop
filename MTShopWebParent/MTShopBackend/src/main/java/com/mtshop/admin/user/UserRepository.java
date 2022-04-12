package com.mtshop.admin.user;

import com.mtshop.common.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/*
 * @Repository Mark a Class As a Repository layer (Data Access layer), the class provides the mechanism for storage,
 * retrieval, search, update and delete operation on objects.
 * @Repository has been marked with @Component
 * @Repository converts database exceptions to Spring-based unchecked exceptions
 */

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
    @Query("SELECT u FROM User u WHERE u.email= :email")
    public User getUserByEmail(@Param("email") String email);
}
