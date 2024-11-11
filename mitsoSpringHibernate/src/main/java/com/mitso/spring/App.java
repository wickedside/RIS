package com.mitso.spring; // объявляем пакет

import com.mitso.spring.config.AppConfig; // импортируем конфигурационный класс
import com.mitso.spring.model.Client; // импортируем модель
import com.mitso.spring.service.ClientService; // импортируем сервис
import com.mitso.spring.util.JsonUtil; // импортируем утилиту
import com.mitso.spring.util.XmlUtil; // импортируем утилиту
import org.springframework.context.annotation.AnnotationConfigApplicationContext; // импортируем класс для конфигурации

import java.util.Date; // импортируем Date
import java.util.List; // импортируем List

/**
 * основной класс приложения
 */
public class App {
    public static void main(String[] args) {
        // создаём контекст spring
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class); // создаём контекст

        // получаем сервис
        ClientService clientService = context.getBean(ClientService.class); // получаем сервис

        // получаем утилиты
        XmlUtil xmlUtil = context.getBean(XmlUtil.class); // получаем XML утилиту
        JsonUtil jsonUtil = context.getBean(JsonUtil.class); // получаем JSON утилиту

        // создаём нового клиента
        Client client = new Client(); // создаём клиента
        client.setSurname("иванов"); // устанавливаем фамилию
        client.setName("иван"); // устанавливаем имя
        client.setPatronymic("иванович"); // устанавливаем отчество
        client.setDateOfBirth(new Date()); // устанавливаем дату рождения
        client.setGender(true); // устанавливаем пол
        client.setPassportSeries("AB"); // устанавливаем серию паспорта
        client.setPassportNumber("123456"); // устанавливаем номер паспорта
        client.setIdentificationNumber("987654321"); // устанавливаем идентификационный номер
        // установи другие поля по необходимости

        // сохраняем клиента
        clientService.saveOrUpdate(client); // сохраняем клиента

        // получаем всех клиентов
        List<Client> clients = clientService.findAll(); // получаем список клиентов
        System.out.println("список клиентов:");
        clients.forEach(System.out::println); // выводим клиентов

        // маршалим в XML
        xmlUtil.marshal(clients, "clients_output.xml"); // маршалим в XML

        // маршалим в JSON
        jsonUtil.toJson(clients, "clients_output.json"); // маршалим в JSON

        // закрываем контекст
        context.close(); // закрываем контекст
    }
}
