package com.langlearnquiz.backend.controllers;

import com.langlearnquiz.backend.dto.UserDTO;
import com.langlearnquiz.backend.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {
    Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    UserService userService;

    @GetMapping("/user/{id}")
    public UserDTO getUser(@PathVariable("id") long id){
        return UserDTO.fromDAO(userService.getUserByChatId(id));
    }

    @PostMapping("/user")
    public void saveUser(@RequestBody UserDTO user) {
        log.info(user.toString());
        userService.saveUser(user.convertToDAO());
    }
}
