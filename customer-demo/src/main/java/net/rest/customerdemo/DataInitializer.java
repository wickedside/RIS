package net.rest.customerdemo;

import net.rest.customerdemo.model.Customer;
import net.rest.customerdemo.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Arrays;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private CustomerService customerService;

    @Override
    public void run(String... args) throws Exception {
        // Клиент 1
        Customer customer1 = new Customer();
        customer1.setFirstName("Иван");
        customer1.setLastName("Иванов");
        customer1.setMiddleName("Иванович");
        customer1.setBirthDate(LocalDate.of(1990, 1, 1));
        customer1.setGender(true);
        customer1.setPassportSeries("AB");
        customer1.setPassportNumber("1234567");
        customer1.setIdentityNumber("1234567A123PB1");
        customer1.setCity("Минск");
        customer1.setActualAddress("ул. Ленина, д.1");
        customer1.setHomePhone("+375 (17) 123-45-67");
        customer1.setMobilePhone("+375 (29) 123-45-67");
        customer1.setRegistrationAddress("ул. Ленина, д.1");
        customer1.setCitizenship("Республика Беларусь");
        customerService.create(customer1);

        // Клиент 2
        Customer customer2 = new Customer();
        customer2.setFirstName("Мария");
        customer2.setLastName("Петрова");
        customer2.setMiddleName("Сергеевна");
        customer2.setBirthDate(LocalDate.of(1985, 5, 15));
        customer2.setGender(false);
        customer2.setPassportSeries("BM");
        customer2.setPassportNumber("7654321");
        customer2.setIdentityNumber("7654321B321PB2");
        customer2.setCity("Гомель");
        customer2.setActualAddress("пр. Победы, д.10");
        customer2.setHomePhone(null);
        customer2.setMobilePhone("+375 (29) 765-43-21");
        customer2.setRegistrationAddress("пр. Победы, д.10");
        customer2.setCitizenship("Республика Беларусь");
        customerService.create(customer2);

        // Клиент 3
        Customer customer3 = new Customer();
        customer3.setFirstName("Александр");
        customer3.setLastName("Сидоров");
        customer3.setMiddleName("Петрович");
        customer3.setBirthDate(LocalDate.of(1992, 7, 21));
        customer3.setGender(true);
        customer3.setPassportSeries("KH");
        customer3.setPassportNumber("2345678");
        customer3.setIdentityNumber("2345678C456PB3");
        customer3.setCity("Брест");
        customer3.setActualAddress("ул. Советская, д.20");
        customer3.setHomePhone("+375 (16) 765-43-21");
        customer3.setMobilePhone("+375 (33) 111-22-33");
        customer3.setRegistrationAddress("ул. Советская, д.20");
        customer3.setCitizenship("Республика Беларусь");
        customerService.create(customer3);

        // Клиент 4
        Customer customer4 = new Customer();
        customer4.setFirstName("Елена");
        customer4.setLastName("Кузнецова");
        customer4.setMiddleName("Андреевна");
        customer4.setBirthDate(LocalDate.of(1988, 3, 30));
        customer4.setGender(false);
        customer4.setPassportSeries("MC");
        customer4.setPassportNumber("3456789");
        customer4.setIdentityNumber("3456789D789PB4");
        customer4.setCity("Витебск");
        customer4.setActualAddress("ул. Пушкина, д.5");
        customer4.setHomePhone("+375 (21) 123-45-67");
        customer4.setMobilePhone("+375 (44) 333-44-55");
        customer4.setRegistrationAddress("ул. Пушкина, д.5");
        customer4.setCitizenship("Республика Беларусь");
        customerService.create(customer4);

        // Клиент 5 (ваш клиент)
        Customer customer5 = new Customer();
        customer5.setFirstName("Даниил");
        customer5.setLastName("Кублицкий");
        customer5.setMiddleName("Евгеньевич");
        customer5.setBirthDate(LocalDate.of(2004, 4, 15));
        customer5.setGender(true);
        customer5.setPassportSeries("1111");
        customer5.setPassportNumber("2222 333333");
        customer5.setIdentityNumber("333444555");
        customer5.setCity("Минск");
        customer5.setActualAddress("ул. Каменногорская, д.100");
        customer5.setHomePhone("+375-44-763-1883");
        customer5.setMobilePhone("+375-44-763-1883");
        customer5.setRegistrationAddress("ул. Каменногорская, д.100");
        customer5.setCitizenship("Республика Беларусь");
        customerService.create(customer5);
    }
}
