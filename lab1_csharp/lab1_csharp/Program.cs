using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;

namespace CandyFactorySales
{
    class Program
    {
        // Путь к файлу для хранения данных
        static string dataFilePath = "sales.txt";

        static void Main(string[] args)
        {
            // Список для хранения записей о продажах
            List<SaleRecord> sales = LoadData();

            while (true)
            {
                Console.WriteLine("\nМеню:");
                Console.WriteLine("1 - Добавить запись");
                Console.WriteLine("2 - Изменить запись");
                Console.WriteLine("3 - Удалить запись");
                Console.WriteLine("4 - Просмотреть все записи");
                Console.WriteLine("5 - Поиск записи");
                Console.WriteLine("6 - Сортировка записей");
                Console.WriteLine("7 - Выход");
                Console.Write("Выберите пункт меню: ");

                string choice = Console.ReadLine();

                switch (choice)
                {
                    case "1":
                        AddRecord(sales);
                        break;
                    case "2":
                        EditRecord(sales);
                        break;
                    case "3":
                        DeleteRecord(sales);
                        break;
                    case "4":
                        ViewRecords(sales);
                        break;
                    case "5":
                        SearchRecords(sales);
                        break;
                    case "6":
                        SortRecords(sales);
                        break;
                    case "7":
                        SaveData(sales);
                        return;
                    default:
                        Console.WriteLine("Неверный выбор. Попробуйте снова.");
                        break;
                }
            }
        }

        // Загрузка данных из файла
        static List<SaleRecord> LoadData()
        {
            var sales = new List<SaleRecord>();

            if (File.Exists(dataFilePath))
            {
                var lines = File.ReadAllLines(dataFilePath);
                foreach (var line in lines)
                {
                    var parts = line.Split(';');
                    if (parts.Length == 4)
                    {
                        sales.Add(new SaleRecord
                        {
                            Id = int.Parse(parts[0]),
                            ProductName = parts[1],
                            Quantity = int.Parse(parts[2]),
                            Price = decimal.Parse(parts[3])
                        });
                    }
                }
            }

            return sales;
        }

        // Сохранение данных в файл
        static void SaveData(List<SaleRecord> sales)
        {
            var lines = new List<string>();
            foreach (var sale in sales)
            {
                lines.Add($"{sale.Id};{sale.ProductName};{sale.Quantity};{sale.Price}");
            }
            File.WriteAllLines(dataFilePath, lines);
        }

        // Добавление новой записи
        static void AddRecord(List<SaleRecord> sales)
        {
            Console.Write("Введите название продукта: ");
            string productName = Console.ReadLine();

            Console.Write("Введите количество: ");
            int quantity = int.Parse(Console.ReadLine());

            Console.Write("Введите цену: ");
            decimal price = decimal.Parse(Console.ReadLine());

            int newId = sales.Count() > 0 ? sales.Max(s => s.Id) + 1 : 1;

            sales.Add(new SaleRecord
            {
                Id = newId,
                ProductName = productName,
                Quantity = quantity,
                Price = price
            });

            Console.WriteLine("Запись добавлена.");
        }

        // Изменение существующей записи
        static void EditRecord(List<SaleRecord> sales)
        {
            Console.Write("Введите ID записи для редактирования: ");
            int id = int.Parse(Console.ReadLine());

            var sale = sales.FirstOrDefault(s => s.Id == id);
            if (sale != null)
            {
                Console.Write("Введите новое название продукта (оставьте пустым для сохранения текущего): ");
                string productName = Console.ReadLine();
                if (!string.IsNullOrEmpty(productName))
                {
                    sale.ProductName = productName;
                }

                Console.Write("Введите новое количество (оставьте пустым для сохранения текущего): ");
                string quantityInput = Console.ReadLine();
                if (!string.IsNullOrEmpty(quantityInput))
                {
                    sale.Quantity = int.Parse(quantityInput);
                }

                Console.Write("Введите новую цену (оставьте пустым для сохранения текущей): ");
                string priceInput = Console.ReadLine();
                if (!string.IsNullOrEmpty(priceInput))
                {
                    sale.Price = decimal.Parse(priceInput);
                }

                Console.WriteLine("Запись обновлена.");
            }
            else
            {
                Console.WriteLine("Запись с таким ID не найдена.");
            }
        }

        // Удаление записи
        static void DeleteRecord(List<SaleRecord> sales)
        {
            Console.Write("Введите ID записи для удаления: ");
            int id = int.Parse(Console.ReadLine());

            var sale = sales.FirstOrDefault(s => s.Id == id);
            if (sale != null)
            {
                sales.Remove(sale);
                Console.WriteLine("Запись удалена.");
            }
            else
            {
                Console.WriteLine("Запись с таким ID не найдена.");
            }
        }

        // Просмотр всех записей
        static void ViewRecords(List<SaleRecord> sales)
        {
            if (sales.Count > 0)
            {
                Console.WriteLine("\nСписок записей:");
                foreach (var sale in sales)
                {
                    Console.WriteLine($"ID: {sale.Id}, Продукт: {sale.ProductName}, Количество: {sale.Quantity}, Цена: {sale.Price}");
                }
            }
            else
            {
                Console.WriteLine("Список записей пуст.");
            }
        }

        // Поиск записей
        static void SearchRecords(List<SaleRecord> sales)
        {
            Console.Write("Введите название продукта для поиска: ");
            string searchTerm = Console.ReadLine();

            var results = sales.Where(s => s.ProductName.IndexOf(searchTerm, StringComparison.OrdinalIgnoreCase) >= 0).ToList();

            if (results.Count > 0)
            {
                Console.WriteLine("\nНайденные записи:");
                foreach (var sale in results)
                {
                    Console.WriteLine($"ID: {sale.Id}, Продукт: {sale.ProductName}, Количество: {sale.Quantity}, Цена: {sale.Price}");
                }
            }
            else
            {
                Console.WriteLine("Совпадений не найдено.");
            }
        }

        // Сортировка записей
        static void SortRecords(List<SaleRecord> sales)
        {
            Console.WriteLine("Сортировать по:");
            Console.WriteLine("1 - Названию продукта");
            Console.WriteLine("2 - Количеству");
            Console.WriteLine("3 - Цене");
            Console.Write("Выберите вариант сортировки: ");

            string choice = Console.ReadLine();

            switch (choice)
            {
                case "1":
                    sales = sales.OrderBy(s => s.ProductName).ToList();
                    Console.WriteLine("Сортировка по названию продукта выполнена.");
                    break;
                case "2":
                    sales = sales.OrderBy(s => s.Quantity).ToList();
                    Console.WriteLine("Сортировка по количеству выполнена.");
                    break;
                case "3":
                    sales = sales.OrderBy(s => s.Price).ToList();
                    Console.WriteLine("Сортировка по цене выполнена.");
                    break;
                default:
                    Console.WriteLine("Неверный выбор.");
                    break;
            }

            // После сортировки отображаем записи
            ViewRecords(sales);
        }
    }

    // Класс для хранения данных о продаже
    class SaleRecord
    {
        public int Id { get; set; } // Уникальный идентификатор записи
        public string ProductName { get; set; } // Название продукта
        public int Quantity { get; set; } // Количество
        public decimal Price { get; set; } // Цена
    }
}
