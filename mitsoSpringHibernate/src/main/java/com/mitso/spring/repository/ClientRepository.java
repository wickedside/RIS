package com.mitso.spring.repository; // объявляем пакет

import com.mitso.spring.model.Client; // импортируем модель
import org.hibernate.SessionFactory; // импортируем SessionFactory
import org.springframework.beans.factory.annotation.Autowired; // импортируем аннотацию
import org.springframework.stereotype.Repository; // импортируем аннотацию
import org.springframework.transaction.annotation.Transactional; // импортируем аннотацию

import java.util.List; // импортируем List

/**
 * репозиторий для работы с клиентами
 */
@Repository // указываем, что это репозиторий
public class ClientRepository {

    @Autowired // инжектим SessionFactory
    private SessionFactory sessionFactory;

    /**
     * сохраняет или обновляет клиента
     */
    @Transactional
    public void saveOrUpdate(Client client) {
        sessionFactory.getCurrentSession().saveOrUpdate(client); // сохраняем или обновляем клиента
    }

    /**
     * удаляет клиента
     */
    @Transactional
    public void delete(Client client) {
        sessionFactory.getCurrentSession().delete(client); // удаляем клиента
    }

    /**
     * находит клиента по id
     */
    @Transactional(readOnly = true)
    public Client findById(Long id) {
        return sessionFactory.getCurrentSession().get(Client.class, id); // получаем клиента по id
    }

    /**
     * находит всех клиентов
     */
    @Transactional(readOnly = true)
    public List<Client> findAll() {
        return sessionFactory.getCurrentSession().createQuery("from Client", Client.class).list(); // получаем всех клиентов
    }
}
