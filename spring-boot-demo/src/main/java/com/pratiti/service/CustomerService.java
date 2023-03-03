package com.pratiti.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pratiti.Exception.CustomerServiceException;
import com.pratiti.entity.Customer;
import com.pratiti.entity.Customer.Status;
import com.pratiti.model.LoginData;
import com.pratiti.repository.AddressRepository;
import com.pratiti.repository.CustomerRepository;

@Service
public class CustomerService {
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private AddressRepository addressRepository;
	
	public int register(Customer customer) {
		if(!customerRepository.existsByEmail(customer.getEmail())) {
			customer.setStatus(Status.INACTIVE);
			customer.getAddress();
		    customerRepository.save(customer);
		    return customer.getId();
     	} 
		else
			throw new CustomerServiceException("Customer already registered!!");		
    }
	
	public void activate(int id) {
		Optional<Customer> customer = customerRepository.findById(id);
		if(customer.isPresent()) {
			Customer customerData = customer.get();
			if(customerData.getStatus() !=Status.LOCKED) {
				customerData.setStatus(Status.ACTIVE);
				customerRepository.save(customerData);
			}
			else
			    throw new CustomerServiceException ("Account is Locked please contact Admin");
		}
		else	
		    throw new CustomerServiceException("Account not exist");
		
	}
	public Customer login(String email, String password) {
		Optional<Customer>customer = customerRepository.findByEmailAndPassword(email, password);
			if(customer.isPresent()) {
				Customer customerData =customer.get();
				if(customerData.getStatus()==Status.ACTIVE)
					return customerData;
				else
					throw new CustomerServiceException("cannot login account is inactive");
			}
			else
				throw new CustomerServiceException("cannot login invalid email/password!1");
		
	}
}
