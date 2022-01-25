package com.example.sendmoneyapi.repositories;

import java.util.List;

import com.example.sendmoneyapi.models.Payment;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "payments", path = "payments")
public interface PaymentRepository extends MongoRepository<Payment, Long> {

    List<Payment> findAll();

}
