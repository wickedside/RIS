package com.mitso.spring.config; // объявляем пакет

import org.springframework.context.annotation.Bean; // импортируем аннотацию
import org.springframework.context.annotation.ComponentScan; // импортируем аннотацию
import org.springframework.context.annotation.Configuration; // импортируем аннотацию
import org.springframework.orm.hibernate5.HibernateTransactionManager; // импортируем менеджер транзакций
import org.springframework.orm.hibernate5.LocalSessionFactoryBean; // импортируем фабрику сессий
import org.springframework.transaction.annotation.EnableTransactionManagement; // импортируем аннотацию

import javax.sql.DataSource; // импортируем DataSource
import org.springframework.jdbc.datasource.DriverManagerDataSource; // импортируем драйвер

import java.util.Properties; // импортируем Properties

/**
 * конфигурационный класс для spring и hibernate
 */
@Configuration // указываем, что это конфигурационный класс
@ComponentScan(basePackages = "com.mitso.spring") // сканируем пакеты
@EnableTransactionManagement // включаем управление транзакциями
public class AppConfig {

    /**
     * бин для подключения к базе данных
     */
    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver"); // класс драйвера
        dataSource.setUrl("jdbc:mysql://localhost:3306/mitso_db?useSSL=false&serverTimezone=UTC"); // url базы данных
        dataSource.setUsername("root"); // имя пользователя
        dataSource.setPassword("your_password"); // пароль
        return dataSource;
    }

    /**
     * бин для фабрики сессий hibernate
     */
    @Bean
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource()); // устанавливаем источник данных
        sessionFactory.setPackagesToScan("com.mitso.spring.model"); // пакеты для сканирования
        sessionFactory.setHibernateProperties(hibernateProperties()); // свойства hibernate
        return sessionFactory;
    }

    /**
     * настройки hibernate
     */
    private Properties hibernateProperties() {
        Properties properties = new Properties();
        properties.put("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect"); // диалект базы данных
        properties.put("hibernate.show_sql", "true"); // показывать sql-запросы
        properties.put("hibernate.hbm2ddl.auto", "update"); // автоматическое обновление схемы
        return properties;
    }

    /**
     * бин для менеджера транзакций
     */
    @Bean
    public HibernateTransactionManager transactionManager() {
        HibernateTransactionManager txManager = new HibernateTransactionManager();
        txManager.setSessionFactory(sessionFactory().getObject()); // устанавливаем фабрику сессий
        return txManager;
    }
}
