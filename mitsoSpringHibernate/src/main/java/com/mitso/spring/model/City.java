package com.mitso.spring.model; // объявляем пакет

import javax.persistence.*; // импортируем аннотации
import java.util.Set; // импортируем Set

/**
 * сущность города
 */
@Entity // указываем, что это сущность
@Table(name = "cities") // название таблицы
public class City {

    @Id // первичный ключ
    @GeneratedValue(strategy = GenerationType.IDENTITY) // автоинкремент
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    // связь с клиентами
    @OneToMany(mappedBy = "city", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Client> clients;

    // геттеры и сеттеры

    // конструкторы, если необходимо

    // toString(), equals() и hashCode(), если необходимо
}