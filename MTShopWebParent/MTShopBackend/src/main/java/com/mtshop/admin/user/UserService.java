package com.mtshop.admin.user;

import com.mtshop.common.entity.Role;
import com.mtshop.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/*
 * @Service marks this as service layer, applies only to class and is a special case of @Component
 * @Autowired when the application runs autowire will find the bean and automatically instantiate the object for the
 * current class to use
 */

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    public List<User> listAll() {
        return (List<User>) userRepository.findAll();
    }

    public List<Role> listRoles() {
        return (List<Role>) roleRepository.findAll();
    }

    public void save(User user) {
        userRepository.save(user);
    }
}
