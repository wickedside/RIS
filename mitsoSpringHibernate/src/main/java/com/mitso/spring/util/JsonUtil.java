package com.mitso.spring.util; // объявляем пакет

import com.google.gson.Gson; // импортируем Gson
import com.google.gson.GsonBuilder; // импортируем GsonBuilder
import com.mitso.spring.model.Client; // импортируем модель
import org.springframework.stereotype.Component; // импортируем аннотацию

import java.io.FileWriter; // импортируем FileWriter
import java.io.FileReader; // импортируем FileReader
import java.io.IOException; // импортируем исключение
import java.util.List; // импортируем List

/**
 * утилитный класс для работы с JSON
 */
@Component // указываем, что это бин
public class JsonUtil {

    private Gson gson = new GsonBuilder().setPrettyPrinting().create(); // создаём Gson с форматированием

    /**
     * сериализация объектов в JSON
     */
    public void toJson(List<Client> clients, String filePath) {
        try (FileWriter writer = new FileWriter(filePath)) { // создаём FileWriter
            gson.toJson(clients, writer); // сериализуем список клиентов
            System.out.println("данные успешно записаны в JSON файл");
        } catch (IOException e) { // ловим исключение
            e.printStackTrace(); // печатаем стек трейс
        }
    }

    /**
     * десериализация JSON в объекты
     */
    public List<Client> fromJson(String filePath, Class<List<Client>> clazz) {
        try (FileReader reader = new FileReader(filePath)) { // создаём FileReader
            List<Client> clients = gson.fromJson(reader, clazz); // десериализуем JSON
            System.out.println("данные успешно прочитаны из JSON файла");
            return clients; // возвращаем список клиентов
        } catch (IOException e) { // ловим исключение
            e.printStackTrace(); // печатаем стек трейс
            return null; // возвращаем null в случае ошибки
        }
    }
}
