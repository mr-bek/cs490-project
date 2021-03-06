/*
 * Author: Yee Mon Zaw
 * Date: 01-May-2019
 * Class Name: HomeController
 * Package: mum.pmp.mstore.controller
 * Description: This is Home Controller for all login and home mapping.
 */

package mum.pmp.mstore.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import mum.pmp.mstore.config.security.MyAuthSuccessHandler;
import mum.pmp.mstore.domain.Product;
import mum.pmp.mstore.model.Profile;
import mum.pmp.mstore.service.ProductService;
import mum.pmp.mstore.service.security.ProfileService;

@Controller
public class HomeController {

	private Authentication auth;
	
	@Autowired
	private MyAuthSuccessHandler handler;
	
	@Autowired
	private ProfileService profileService;
	
	@Autowired
	private ProductService productService;

	@RequestMapping("/login")
	public String loginPage() {
		System.out.println("Login???");
		return "/secure/login";
	}

	@RequestMapping("/logout-success")
	public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			new SecurityContextLogoutHandler().logout(request, response, auth);
		}
		return "redirect:/login";
	}

	@GetMapping("/forgotpwd")
	public String forgotPwd()
	{
		System.out.println("click on forgot password link");
		return "password/enter_email";
	}
	
	private UserDetails secureOperations() {
		UserDetails user = null;
		System.out.println("auth:" + auth);
		auth = handler.getAuth();
		if (auth.getPrincipal() != null)
			user = (UserDetails) auth.getPrincipal();
		
		return user;
	}

	private String authorizedRole() {
		for (GrantedAuthority roles : auth.getAuthorities()) {
			String authorizedRole = roles.getAuthority();
			System.out.println(authorizedRole);
			return authorizedRole;
		}
		return null;
	}

	@GetMapping("/home")
	public String homepage(Model model)
	{
		model.addAttribute("products", productService.getAllProducts());
		model.addAttribute("product", new Product());
		return "/home/home";
	}
	
	@RequestMapping("/")
	public String home(Model model) {
		auth = SecurityContextHolder.getContext().getAuthentication();
		String redirectURL = "";
		if (auth != null && !auth.getPrincipal().equals("anonymousUser")) {
			UserDetails user = secureOperations();
			System.out.println("UserDetails: "+user);
			if (user != null) {
				model.addAttribute("userdetails", user);
				model.addAttribute("role", authorizedRole());
			}
			System.out.println("......User Role ......." +authorizedRole());
			// When user login set his status to 1 to enable him if he was previously disabled by himself.
			if(user != null && !authorizedRole().equals("ROLE_SUPER_ADMIN"))
			{
				Profile profile = profileService.findByEmail(user.getUsername());
				if(profile.getStatus() == 0)
				{
					profile.setStatus((byte)1);
					profileService.saveProfile(profile);
				}
			}
		}
		model.addAttribute("products", productService.getAllProducts());
		model.addAttribute("product", new Product());
		return "/home/home";
	}
	
	@RequestMapping("/profile")
	public String profilePage() {
		System.out.println("Here in profile.");
		// validate user role and route to different profile.
		String url = "";
		UserDetails user = secureOperations();
		if (user != null) {
			String role = authorizedRole();
			System.out.println("role" + role);
			switch (role) {
			case "ROLE_ADMIN":
				url = "redirect:/admin/update";
				break;
			case "ROLE_CUSTOMER":
				url = "redirect:/customer/update";
				break;
			case "ROLE_VENDOR":
				url = "redirect:/vendor/update";
				break;
			case "ROLE_SUPER_ADMIN":
				url = "redirect:/admin/update";
				break;
			}
		}
		return url;
	}
	
	//display all products in catalog format
	@GetMapping("/catalogs")
	public String getAllCatalogs(Model model) {
		model.addAttribute("products", productService.getAllProducts());
		return "/catalog/catalog";
	}
}
