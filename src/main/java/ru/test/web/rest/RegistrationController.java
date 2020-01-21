package ru.test.web.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.test.service.UserService;
import ru.test.web.dto.UserDto;

@RestController
@RequestMapping("/registration")
public class RegistrationController {

    @Autowired
    private UserService userService;

    @PostMapping
    public void register(@RequestBody UserDto userDto) {
        System.out.println(userDto);
        userService.createUser(userDto);
    }
}
