package net.rest.customerdemo.service;

import net.rest.customerdemo.model.Customer;
import java.util.List;

public interface CustomerService {
    Customer getById(Long id);
    Customer create(Customer customer);
    Customer update(Long id, Customer customer);
    void delete(Long id);
    List<Customer> getAll();
}