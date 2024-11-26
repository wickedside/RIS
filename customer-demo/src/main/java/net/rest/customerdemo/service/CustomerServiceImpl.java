package net.rest.customerdemo.service;

import net.rest.customerdemo.model.Customer;
import net.rest.customerdemo.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public Customer getById(Long id) {
        Optional<Customer> optional = customerRepository.findById(id);
        return optional.orElse(null);
    }

    @Override
    public Customer create(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public Customer update(Long id, Customer customer) {
        Optional<Customer> existingOpt = customerRepository.findById(id);
        if (existingOpt.isPresent()) {
            Customer existing = existingOpt.get();
            // Обновляем поля
            existing.setFirstName(customer.getFirstName());
            existing.setLastName(customer.getLastName());
            existing.setMiddleName(customer.getMiddleName());
            existing.setBirthDate(customer.getBirthDate());
            existing.setGender(customer.getGender());
            existing.setPassportSeries(customer.getPassportSeries());
            existing.setPassportNumber(customer.getPassportNumber());
            existing.setIdentityNumber(customer.getIdentityNumber());
            existing.setCity(customer.getCity());
            existing.setActualAddress(customer.getActualAddress());
            existing.setHomePhone(customer.getHomePhone());
            existing.setMobilePhone(customer.getMobilePhone());
            existing.setRegistrationAddress(customer.getRegistrationAddress());
            existing.setCitizenship(customer.getCitizenship());
            return customerRepository.save(existing);
        } else {
            return null;
        }
    }

    @Override
    public void delete(Long id) {
        customerRepository.deleteById(id);
    }

    @Override
    public List<Customer> getAll() {
        return customerRepository.findAll();
    }
}