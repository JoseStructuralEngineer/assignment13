package com.coderscampus.assignment13.web;

import com.coderscampus.assignment13.domain.User;
import com.coderscampus.assignment13.service.AccountService;
import com.coderscampus.assignment13.service.AddressService;
import com.coderscampus.assignment13.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegistrationController {

    @Autowired
    private UserService userService;

    @Autowired
    private AddressService addressService;

    @Autowired
    private AccountService accountService;

    @GetMapping("/register")
    public String getCreateUser (ModelMap model) {

        model.put("user", new User());

        return "register";
    }

    @PostMapping("/register")
    public String postCreateUser (User user) {
        System.out.println(user);
        if (user.getUserId() == null) {
            accountService.saveAccountsForUser(user,"Checking Account" );
            accountService.saveAccountsForUser(user,"Savings Account" );
        }
        userService.saveUser(user);
        return "redirect:/register";
    }

}
