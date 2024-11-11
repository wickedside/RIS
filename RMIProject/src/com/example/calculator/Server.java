package com.example.calculator; // объявляем пакет, в котором находится сервер

import java.rmi.registry.Registry; // импортируем класс Registry для работы с реестром RMI
import java.rmi.registry.LocateRegistry; // импортируем LocateRegistry для поиска или создания реестра
import java.rmi.RemoteException; // импортируем RemoteException для обработки удаленных ошибок
import java.rmi.server.UnicastRemoteObject; // импортируем UnicastRemoteObject для экспорта удаленных объектов

/**
 * реализация сервера, предоставляющего удаленный метод для вычисления НОК
 */
public class Server implements Calculator { // класс сервер реализует интерфейс Calculator

    // конструктор сервера
    public Server() {} // пустой конструктор

    /**
     * реализация метода getLCM для вычисления НОК двух чисел
     */
    @Override
    public int getLCM(int a, int b) throws RemoteException { // переопределяем метод из интерфейса
        return lcm(a, b); // вызываем приватный метод для вычисления НОК
    }

    /**
     * метод для вычисления НОК двух чисел
     */
    private int lcm(int a, int b) { // приватный метод для вычисления НОК
        return (a * b) / gcd(a, b); // формула НОК через НОД
    }

    /**
     * метод для вычисления наибольшего общего делителя (НОД) двух чисел
     */
    private int gcd(int a, int b) { // приватный метод для вычисления НОД
        if (b == 0) return a; // базовый случай для рекурсии
        return gcd(b, a % b); // рекурсивный вызов с остатком от деления
    }

    public static void main(String[] args) { // главный метод сервера
        try {
            // создаем экземпляр сервера
            Server obj = new Server(); // создаем объект сервера

            // экспортируем удаленный объект
            Calculator stub = (Calculator) UnicastRemoteObject.exportObject(obj, 0); // экспортируем объект и получаем стаб

            // создаем или получаем реестр RMI на порту 1099
            Registry registry = LocateRegistry.createRegistry(1099); // создаем реестр на порту 1099

            // привязываем удаленный объект к имени "Calculator"
            registry.bind("Calculator", stub); // связываем стаб с именем в реестре

            System.out.println("Сервер готов."); // выводим сообщение о готовности сервера
        } catch (Exception e) { // ловим все исключения
            System.err.println("Ошибка сервера: " + e.toString()); // выводим ошибку сервера
            e.printStackTrace(); // печатаем стек трейс для отладки
        }
    }
}
