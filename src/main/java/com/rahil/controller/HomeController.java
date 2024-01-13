package com.rahil.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.rahil.entities.ApplicationUser;
import com.rahil.helper.Message;
import com.rahil.repository.ApplicationUserRepository;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class HomeController {
	
	@Autowired
	private ApplicationUserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@GetMapping("/")
	public String home(Model model) {
		model.addAttribute("title", "Home - Smart Contact Manager");
		
		return "home";
	}
	
	@GetMapping("/about")
	public String about(Model model) {
		model.addAttribute("title", "About - Smart Contact Manager");
		
		return "about";
	}
	
	@GetMapping("/signup")
	public String signup(Model model) {
		model.addAttribute("title", "Register - Smart Contact Manager");
		model.addAttribute("user", new ApplicationUser());
		return "signup";
	}
	
	@PostMapping("/do_register")
	public String userRegister(@Valid @ModelAttribute("user") ApplicationUser user,BindingResult result, @RequestParam(value = "agreement", defaultValue = "false") boolean agreement, Model model,HttpSession session) {
		
		try {
			
//			if (!agreement) {
//				System.out.println("You dont have the term condition");
//				throw new Exception("You dont have the term condition");
//			}
			
			if (result.hasErrors()) {
				model.addAttribute("user", user);
				return "signup";
			}
			
			user.setRole("ROLE_USER");
			user.setEnable(true);
			user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
			
			System.out.println(agreement);
			System.out.println(user);
			
			ApplicationUser applicationUser =  userRepository.save(user);
			
			model.addAttribute("user", new ApplicationUser());
			session.setAttribute("message", new Message("Successfully Register!!! ", "alert-success"));
			return "signup";
			
		} catch (Exception e) {
		
			e.printStackTrace();
			model.addAttribute("user", user);
			session.setAttribute("message", new Message("Something went wrong !!! "+e.getMessage(), "alert-danger"));
			
			return "signup";
		}
		
	}
	
	@GetMapping("/login")
	public String customLogin(Model model) {
		model.addAttribute("title", "Login page");
		return "login";
	}
	
}
