package com.example.sendmoneyapi.repositories;

import java.util.List;

import com.example.sendmoneyapi.models.Customer;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "customers", path = "customers")
public interface CustomerRepository extends MongoRepository<Customer, String> {
    List<Customer> findByCustomerPhonenumber(@Param("phonenumber") String phonenumber);

    List<Customer> findAll();

    default List<Customer> fetchCustomers() {
        return findAll();
    }

}
