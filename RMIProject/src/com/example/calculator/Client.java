package com.example.calculator; // объявляем пакет, в котором находится клиент

import java.rmi.registry.LocateRegistry; // импортируем LocateRegistry для поиска реестра
import java.rmi.registry.Registry; // импортируем Registry для взаимодействия с реестром
import java.util.Scanner; // импортируем Scanner для ввода данных пользователем

/**
 * реализация клиента, который вызывает удаленный метод для вычисления НОК
 */
public class Client { // класс клиент
    private Client() {} // приватный конструктор, чтобы предотвратить создание экземпляра

    public static void main(String[] args) { // главный метод клиента
        String host = (args.length < 1) ? null : args[0]; // определяем хост, если он передан в аргументах
        try {
            // получаем реестр на указанном хосте (по умолчанию localhost)
            Registry registry = LocateRegistry.getRegistry(host); // получаем стаб реестра

            // получаем удаленный объект "Calculator" из реестра
            Calculator stub = (Calculator) registry.lookup("Calculator"); // ищем стаб по имени

            // создаем сканер для ввода чисел пользователем
            Scanner scanner = new Scanner(System.in); // инициализируем сканер

            System.out.print("Введите первое число: "); // просим ввести первое число
            int num1 = scanner.nextInt(); // считываем первое число

            System.out.print("Введите второе число: "); // просим ввести второе число
            int num2 = scanner.nextInt(); // считываем второе число

            // вызываем удаленный метод getLCM
            int lcm = stub.getLCM(num1, num2); // вызываем метод на сервере

            // выводим результат
            System.out.println("(НОК) чисел " + num1 + " и " + num2 + " равно " + lcm); // показываем результат

            scanner.close(); // закрываем сканер
        } catch (Exception e) { // ловим все исключения
            System.err.println("Исключение клиента: " + e.toString()); // выводим ошибку клиента
            e.printStackTrace(); // печатаем стек трейс для отладки
        }
    }
}