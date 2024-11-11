package com.mitso.spring; // объявляем пакет

import com.mitso.spring.calculator.PercentCalculator; // импортируем интерфейс
import org.springframework.context.ApplicationContext; // импортируем контекст приложения
import org.springframework.context.support.ClassPathXmlApplicationContext; // импортируем класс для XML конфигурации
import org.springframework.context.annotation.AnnotationConfigApplicationContext; // импортируем класс для аннотационной конфигурации

/**
 * основной класс приложения
 */
public class App {
    public static void main(String[] args) {
        // использование XML конфигурации
        ApplicationContext xmlContext = new ClassPathXmlApplicationContext("applicationContext.xml"); // создаем контекст из XML
        PercentCalculator calculatorXML = (PercentCalculator) xmlContext.getBean("percentCalculatorXML"); // получаем бин
        double resultXML = calculatorXML.calculatePercent(200, 15); // вычисляем 15% от 200
        System.out.println("Результат с использованием XML: 15% от 200 = " + resultXML); // выводим результат

        // использование аннотаций
        ApplicationContext annotationContext = new AnnotationConfigApplicationContext("com.mitso.spring.calculator"); // создаем контекст с аннотациями
        PercentCalculator calculatorAnnotation = (PercentCalculator) annotationContext.getBean("percentCalculator"); // получаем бин
        double resultAnnotation = calculatorAnnotation.calculatePercent(500, 20); // вычисляем 20% от 500
        System.out.println("Результат с использованием аннотаций: 20% от 500 = " + resultAnnotation); // выводим результат

        // закрываем контексты, если это необходимо
        ((ClassPathXmlApplicationContext) xmlContext).close();
        ((AnnotationConfigApplicationContext) annotationContext).close();
    }
}