package com.coderscampus.assignment13.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import com.coderscampus.assignment13.domain.Account;
import com.coderscampus.assignment13.domain.Address;
import com.coderscampus.assignment13.service.AccountService;
import com.coderscampus.assignment13.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.coderscampus.assignment13.domain.User;
import com.coderscampus.assignment13.service.UserService;

@Controller
public class UserController {
	
	@Autowired
	private UserService userService;

	@Autowired
	private AddressService addressService;

	@Autowired
	private AccountService accountService;
	

	
	@GetMapping("/users")
	public String getAllUsers (ModelMap model) {
		Set<User> users = userService.findAll();
		
		model.put("users", users);
		if (users.size() == 1) {
			model.put("user", users.iterator().next());
		}
		
		return "users";
	}
	
	@GetMapping("/users/{userId}")
	public String getOneUser (ModelMap model, @PathVariable Long userId) {
		User user = userService.findById(userId);
		model.put("users", Arrays.asList(user));
		model.put("user", user);
		return "users";
	}
	
	@PostMapping("/users/{userId}")
	public String postOneUser (User user) {
		//The user came with an address but no accounts
		//Thus we need to update object
		//Note Address does not have userId or user reference

		Address address = user.getAddress();
		address.setUserId(user.getUserId());
		address.setUser(user);

		user.setAddress(address);
		User user0 = userService.findById(user.getUserId());
		user.setPassword(user0.getPassword());
		user.setAccounts(user0.getAccounts());
		userService.saveUser(user);
		addressService.saveAddress(user.getAddress());
		return "redirect:/users/"+user.getUserId();
	}
	
	@PostMapping("/users/{userId}/delete")
	public String deleteOneUser (@PathVariable Long userId) {
		userService.delete(userId);
		return "redirect:/users";
	}

	@GetMapping("/users/{userId}/accounts/{accountId}")
	public String getAccount (ModelMap model, @PathVariable Long userId, @PathVariable Long accountId) {
		Account account = accountService.findByAccountId(accountId);
		User user = userService.findById(userId);
		model.put("account", account);
		model.put("user", user);
		return "account";
	}

	@PostMapping("/users/{userId}/accounts")
	public String saveNewAccount (@PathVariable Long userId) {
		User user = userService.findById(userId);
		int numberOfAccounts = user.getAccounts().size();
		accountService.saveAccountsForUser(user,"Account #" + (numberOfAccounts+1));

		Long newAccountId = user.getAccounts().get(user.getAccounts().size()-1).getAccountId();
		return "redirect:/users/"+ userId + "/accounts/"+newAccountId;
	}

	@PostMapping("/users/{userId}/accounts/{accountId}")
	public String saveAccount (Account account,@PathVariable Long userId,@PathVariable Long accountId) {
		Account account0 = accountService.findByAccountId(accountId);
		account0.setAccountName(account.getAccountName());
		accountService.saveAccount(account0);

		return "redirect:/users/"+userId+"/accounts/"+accountId;
	}
}
