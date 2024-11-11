package com.example.calculator; // объявляем пакет, в котором находится интерфейс

import java.rmi.Remote; // импортируем интерфейс Remote из RMI
import java.rmi.RemoteException; // импортируем класс RemoteException для обработки удаленных ошибок

/**
 * удаленный интерфейс для вычисления НОК двух чисел
 */
public interface Calculator extends Remote { // интерфейс должен расширять Remote
    /**
     * метод для вычисления наименьшего общего кратного (НОК) двух чисел
     *
     * @param a первое число
     * @param b второе число
     * @return НОК чисел a и b
     * @throws RemoteException если происходит ошибка удаленного вызова
     */
    int getLCM(int a, int b) throws RemoteException; // объявляем удаленный метод
}
