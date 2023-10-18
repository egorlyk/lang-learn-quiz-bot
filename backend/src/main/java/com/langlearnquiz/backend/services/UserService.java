package com.langlearnquiz.backend.services;

import com.langlearnquiz.backend.dao.UserDAO;
import com.langlearnquiz.backend.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    UserRepository userRepository;

    public void saveUser(UserDAO userDAO) {
        log.info("USER DAO: " + userDAO);
        userRepository.save(userDAO);
    }

    public UserDAO getUserByChatId(long chatId) {
        return userRepository.findById(chatId);
    }

}
