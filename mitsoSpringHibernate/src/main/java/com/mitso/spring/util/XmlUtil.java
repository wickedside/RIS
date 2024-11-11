package com.mitso.spring.util; // объявляем пакет

import com.mitso.spring.model.generated.Clients; // импортируем сгенерированные классы
import com.mitso.spring.model.Client; // импортируем модель
import org.springframework.stereotype.Component; // импортируем аннотацию

import javax.xml.bind.JAXBContext; // импортируем JAXBContext
import javax.xml.bind.JAXBException; // импортируем исключение
import javax.xml.bind.Marshaller; // импортируем Marshaller
import javax.xml.bind.Unmarshaller; // импортируем Unmarshaller
import java.io.File; // импортируем File
import java.util.List; // импортируем List

/**
 * утилитный класс для работы с XML
 */
@Component // указываем, что это бин
public class XmlUtil {

    /**
     * маршалинг объектов в XML
     */
    public void marshal(List<Client> clients, String filePath) {
        try {
            JAXBContext context = JAXBContext.newInstance(Clients.class); // создаём контекст
            Marshaller marshaller = context.createMarshaller(); // создаём маршаллер
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true); // форматированный вывод

            Clients clientsWrapper = new Clients(); // создаём обёртку
            clientsWrapper.setClients(clients); // устанавливаем список клиентов

            marshaller.marshal(clientsWrapper, new File(filePath)); // маршалим в файл
            System.out.println("данные успешно записаны в XML файл");
        } catch (JAXBException e) { // ловим исключение
            e.printStackTrace(); // печатаем стек трейс
        }
    }

    /**
     * анмаршалинг XML в объекты
     */
    public List<Client> unmarshal(String filePath) {
        try {
            JAXBContext context = JAXBContext.newInstance(Clients.class); // создаём контекст
            Unmarshaller unmarshaller = context.createUnmarshaller(); // создаём анмаршаллер

            Clients clientsWrapper = (Clients) unmarshaller.unmarshal(new File(filePath)); // анмаршалим из файла
            System.out.println("данные успешно прочитаны из XML файла");
            return clientsWrapper.getClients(); // возвращаем список клиентов
        } catch (JAXBException e) { // ловим исключение
            e.printStackTrace(); // печатаем стек трейс
            return null; // возвращаем null в случае ошибки
        }
    }
}
