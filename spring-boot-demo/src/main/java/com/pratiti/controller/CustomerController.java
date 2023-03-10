package com.pratiti.controller;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pratiti.Exception.CustomerServiceException;
import com.pratiti.entity.Customer;
import com.pratiti.model.ActivationStatus;
import com.pratiti.model.CustomerData;
import com.pratiti.model.LoginData;
import com.pratiti.model.LoginStatus;
import com.pratiti.model.RegistrationStatus;
import com.pratiti.service.CustomerService;

@RestController
@CrossOrigin
public class CustomerController {
	
	@Autowired
	private CustomerService customerService;
	
	@PostMapping(path = "/register", consumes ="multipart/form-data")
	public  RegistrationStatus register( CustomerData customerData) {
		//System.out.println(customerData.getProfilePic().getOriginalFilename());
		RegistrationStatus status = new RegistrationStatus();
		try {
			//copying the uploaded image in folder
			String uploadDir ="C:\\Users\\anu03\\OneDrive\\Documents\\upload\\";
			
			InputStream f1 = customerData.getProfilePic().getInputStream();
			FileOutputStream f2 = new FileOutputStream(uploadDir + customerData.getProfilePic().getOriginalFilename());
			FileCopyUtils.copy(f1,f2);
			
			Customer customer = new Customer();
			BeanUtils.copyProperties(customerData, customer);
			customer.setProfilePic(customerData.getProfilePic().getOriginalFilename());
			int id = customerService.register(customer);
			
			status.setStatus(true);
			status.setMessageIfAny("Customer registered succesfully!!");
			status.setRegisteredCustomerId(customer.getId());
			
		}
		catch (IOException | CustomerServiceException e) {
			status.setStatus(false);
			status.setMessageIfAny(e.getMessage());
		}
		return status;
	}     
	
	@GetMapping("/activate-account")
	public ActivationStatus activate(@RequestParam("id")int id) {
		ActivationStatus status = new ActivationStatus();
		try {
			customerService.activate(id);
			status.setStatus(true);
			status.setMessageIfAny("Activation successfully");
		}
		catch (CustomerServiceException e) {
			status.setStatus(false);
			status.setMessageIfAny(e.getMessage());
			
		}
		return status;
		
	}
	@PostMapping ("/login")
	public LoginStatus login(@RequestBody LoginData loginData){
		LoginStatus status = new LoginStatus();
		try {
			Customer customer = customerService.login(loginData.getEmail(),loginData.getPassword());
			status.setStatus(true);
			status.setId(customer.getId());
			status.setName(customer.getName());
		}
		catch (CustomerServiceException e) {
			status.setStatus(false);
			status.setMessageIfAny(e.getMessage());
		}
		return status;
	}
}








//@PostMapping("/register")
//public  RegistrationStatus register(@RequestBody Customer customer) {
	//RegistrationStatus status = new RegistrationStatus();
	//try {
		//customerService.register(customer);
		
		//status.setStatus(true);
		//status.setMessageIfAny("Customer registered successfully!!"); before image cod
		//status.setRegisteredCustomerId(customer.getId());
		
	//}
//	catch (CustomerServiceException e) {
	//	status.setStatus(false);
		//status.setMessageIfAny(e.getMessage());
	//}
	//return status;
//}