package com.mitso.spring.model; // объявляем пакет

import javax.persistence.*; // импортируем аннотации
import java.util.Date; // импортируем Date

/**
 * сущность клиента
 */
@Entity // указываем, что это сущность
@Table(name = "clients") // название таблицы
public class Client {

    @Id // первичный ключ
    @GeneratedValue(strategy = GenerationType.IDENTITY) // автоинкремент
    private Long id;

    @Column(nullable = false) // обязательное поле
    private String surname;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String patronymic;

    @Temporal(TemporalType.DATE)
    @Column(name = "date_of_birth", nullable = false)
    private Date dateOfBirth;

    @Column(nullable = false)
    private Boolean gender;

    @Column(name = "passport_series", nullable = false)
    private String passportSeries;

    @Column(name = "passport_number", nullable = false)
    private String passportNumber;

    @Column(name = "identification_number")
    private String identificationNumber;

    @ManyToOne // связь с городом
    @JoinColumn(name = "city_id", nullable = false)
    private City city;

    @Column(name = "actual_address", nullable = false)
    private String actualAddress;

    @Column(name = "home_phone")
    private String homePhone;

    @Column(name = "mobile_phone")
    private String mobilePhone;

    @Column(name = "registration_address", nullable = false)
    private String registrationAddress;

    @ManyToOne // связь с гражданством
    @JoinColumn(name = "citizenship_id", nullable = false)
    private Citizenship citizenship;

    // геттеры и сеттеры

    // конструкторы, если необходимо

    // toString(), equals() и hashCode(), если необходимо
}