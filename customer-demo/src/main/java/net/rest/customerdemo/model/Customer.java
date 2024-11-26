package net.rest.customerdemo.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "customers")
@Getter
@Setter
@ToString
public class Customer extends BaseEntity {

    @NotBlank(message = "Фамилия обязательна")
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @NotBlank(message = "Имя обязательно")
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotBlank(message = "Отчество обязательно")
    @Column(name = "middle_name", nullable = false)
    private String middleName;

    @NotNull(message = "Дата рождения обязательна")
    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @NotNull(message = "Пол обязателен")
    @Column(name = "gender", nullable = false)
    private Boolean gender; // true - мужской, false - женский

    @NotBlank(message = "Серия паспорта обязательна")
    @Pattern(regexp = "[A-Z]{2}", message = "Серия паспорта должна состоять из 2 заглавных букв")
    @Column(name = "passport_series", nullable = false)
    private String passportSeries;

    @NotBlank(message = "Номер паспорта обязателен")
    @Pattern(regexp = "\\d{7}", message = "Номер паспорта должен состоять из 7 цифр")
    @Column(name = "passport_number", nullable = false)
    private String passportNumber;

    @Pattern(regexp = "\\d{7}[A-Z]\\d{3}[A-Z]{2}\\d", message = "Идентификационный номер должен соответствовать формату РБ")
    @Column(name = "identity_number")
    private String identityNumber;

    @NotBlank(message = "Город проживания обязателен")
    @Column(name = "city", nullable = false)
    private String city;

    @NotBlank(message = "Адрес фактического проживания обязателен")
    @Column(name = "actual_address", nullable = false)
    private String actualAddress;

    @Pattern(regexp = "\\+375\\s\\(\\d{2}\\)\\s\\d{3}-\\d{2}-\\d{2}", message = "Телефон должен быть в формате '+375 (XX) XXX-XX-XX'")
    @Column(name = "home_phone")
    private String homePhone;

    @Pattern(regexp = "\\+375\\s\\(\\d{2}\\)\\s\\d{3}-\\d{2}-\\d{2}", message = "Телефон должен быть в формате '+375 (XX) XXX-XX-XX'")
    @Column(name = "mobile_phone")
    private String mobilePhone;

    @NotBlank(message = "Адрес прописки обязателен")
    @Column(name = "registration_address", nullable = false)
    private String registrationAddress;

    @NotBlank(message = "Гражданство обязательно")
    @Column(name = "citizenship", nullable = false)
    private String citizenship;
}