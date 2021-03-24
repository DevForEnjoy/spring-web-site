package com.example.demo.repos;

import org.springframework.data.repository.CrudRepository;

import com.example.demo.repos.Customer;

public interface CustomerRepository extends CrudRepository<Customer, Integer> {

   // Customer findCustomerById(Integer id);
}