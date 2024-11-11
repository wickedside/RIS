package com.mitso.spring.calculator; // объявляем пакет

import org.springframework.stereotype.Component; // импортируем аннотацию

/**
 * реализация интерфейса PercentCalculator
 */
@Component("percentCalculator") // аннотация для сканирования компонентов
public class PercentCalculatorImpl implements PercentCalculator {

    /**
     * реализация метода calculatePercent
     *
     * @param number основное число
     * @param percent процент, который нужно вычислить
     * @return результат вычисления процента
     */
    @Override
    public double calculatePercent(double number, double percent) {
        return (number * percent) / 100.0; // вычисляем процент
    }
}