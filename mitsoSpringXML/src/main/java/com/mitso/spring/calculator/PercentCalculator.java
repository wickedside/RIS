package com.mitso.spring.calculator; // объявляем пакет

/**
 * интерфейс для калькулятора процентов
 */
public interface PercentCalculator {
    /**
     * метод для расчета процента от числа
     *
     * @param number основное число
     * @param percent процент, который нужно вычислить
     * @return результат вычисления процента
     */
    double calculatePercent(double number, double percent); // объявляем метод
}