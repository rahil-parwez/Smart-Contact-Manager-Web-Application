package com.rahil.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.rahil.entities.ApplicationUser;
import com.rahil.entities.ContactDetails;
import com.rahil.helper.Message;
import com.rahil.repository.ApplicationUserRepository;
import com.rahil.repository.ContactRepository;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	ApplicationUserRepository userRepository;
	
	@Autowired
	ContactRepository contactRepository;
	
	@ModelAttribute
	public void addCommanData(Model model, Principal principal) {
		String username = principal.getName();
		System.out.println(username);
		
		ApplicationUser user = userRepository.getUserByUserName(username);
		
		System.out.println("USER: "+user);
		
		model.addAttribute("user", user);
	}
	
	@GetMapping("/index")
	public String dashboard(Model model, Principal principal) {
		model.addAttribute("title", "user dashboard");
		return "normal/user_dashboard";
	}
	
//	open add contact form handler
	@GetMapping("/add-contact")
	public String openAddContactForm(Model model) {
		
		model.addAttribute("title", "add contact");
		model.addAttribute("contact", new ContactDetails());
		return "normal/add_contact_form";
	}
	
	@PostMapping("/process-contact")
	public String processContact(@ModelAttribute ContactDetails contactDetails, @RequestParam("profileImage") MultipartFile file, Principal principal, HttpSession httpSession) {
		
		try {
			String name = principal.getName();
			ApplicationUser user = userRepository.getUserByUserName(name);
			
			
			if (file.isEmpty()) {
				
				contactDetails.setImage("contact.png");
				
			}else {
				contactDetails.setImage(file.getOriginalFilename());
				File savefile = new ClassPathResource("static/img").getFile();
				Path path = Paths.get(savefile.getAbsolutePath()+File.separator+file.getOriginalFilename());
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				
				System.out.println("image is uploaded...");
				
				httpSession.setAttribute("message", new Message("Your contact is added successfully!", "success"));
				
			}
			
			contactDetails.setApplicationUser(user);
			user.getContact().add(contactDetails);
			
			userRepository.save(user);
		
		}catch (Exception e) {
			httpSession.setAttribute("message", new Message("Somthing went wrong. Please try again!", "danger"));
			e.printStackTrace();
		}
		
		
		return "normal/add_contact_form";
	}
	
	
	
	//Show contact handler
	//show pages = 5[n]
	//current page = 0[page]
	@GetMapping("/show-contact/{page}")
	public String showContacts(@PathVariable("page") Integer page, Model model, Principal principal) {
		
		model.addAttribute("title", "show contact");
		
		String username = principal.getName();
		
		ApplicationUser user = userRepository.getUserByUserName(username);
		
		Pageable pageable = PageRequest.of(page, 5);
		
		Page<ContactDetails> contacts = contactRepository.findContactsByUser(user.getId(), pageable);
		
		model.addAttribute("contacts", contacts);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", contacts.getTotalPages());
		
		return "normal/show_contact";
	}
	
	
//	showing specific contact
	@GetMapping("/contact/{id}")
	public String showContactDetail(@PathVariable("id") Integer id,  Model model, Principal principal) {
		model.addAttribute("title", "show user contact");
		
		ContactDetails contactDetails = null;
		Optional<ContactDetails> byId = null;
		
		try {
			byId = contactRepository.findById(id);
			contactDetails = byId.get();
			
//			if (contactDetails==null) {
//				return "normal/contact_detail";
//			}
		
		}catch (Exception e) {
			return "normal/contact_detail";
		}
		
		
		String username = principal.getName();
		ApplicationUser user = userRepository.getUserByUserName(username);
		
		if (user.getId()==contactDetails.getApplicationUser().getId()) {
			model.addAttribute("userContact", contactDetails);
		}
		
		return "normal/contact_detail";
	}
	
	
	
}
