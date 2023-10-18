package com.langlearnquiz.backend.repositories;

import com.langlearnquiz.backend.dao.UserDAO;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserDAO, Long> {
    UserDAO findById(long id);
}
