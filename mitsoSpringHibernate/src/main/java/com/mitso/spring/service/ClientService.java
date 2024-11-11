package com.mitso.spring.service; // объявляем пакет

import com.mitso.spring.model.Client; // импортируем модель
import com.mitso.spring.repository.ClientRepository; // импортируем репозиторий
import org.springframework.beans.factory.annotation.Autowired; // импортируем аннотацию
import org.springframework.stereotype.Service; // импортируем аннотацию

import java.util.List; // импортируем List

/**
 * сервис для работы с клиентами
 */
@Service // указываем, что это сервис
public class ClientService {

    @Autowired // инжектим репозиторий
    private ClientRepository clientRepository;

    /**
     * сохраняет или обновляет клиента
     */
    public void saveOrUpdate(Client client) {
        clientRepository.saveOrUpdate(client); // делегируем репозиторию
    }

    /**
     * удаляет клиента
     */
    public void delete(Client client) {
        clientRepository.delete(client); // делегируем репозиторию
    }

    /**
     * находит клиента по id
     */
    public Client findById(Long id) {
        return clientRepository.findById(id); // делегируем репозиторию
    }

    /**
     * находит всех клиентов
     */
    public List<Client> findAll() {
        return clientRepository.findAll(); // делегируем репозиторию
    }
}
