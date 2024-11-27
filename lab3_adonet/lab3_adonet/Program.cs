using System;
using System.Data;
using System.Data.SqlClient;

namespace CDSalesApp
{
    class Program
    {
        // Строка подключения к базе данных
        static string connectionString = @"Data Source=(LocalDB)\MSSQLLocalDB;AttachDbFilename=C:\Users\awok3\CDStore.mdf;Integrated Security=True";


        static void Main(string[] args)
        {
            int choice = 0;
            do
            {
                Console.WriteLine("=== Приложение учета продажи компакт-дисков ===");
                Console.WriteLine("1. Добавить компакт-диск");
                Console.WriteLine("2. Просмотреть список компакт-дисков");
                Console.WriteLine("3. Обновить информацию о компакт-диске");
                Console.WriteLine("4. Удалить компакт-диск");
                Console.WriteLine("5. Зарегистрировать продажу");
                Console.WriteLine("6. Просмотреть продажи");
                Console.WriteLine("7. Выход");
                Console.Write("Выберите действие: ");

                if (int.TryParse(Console.ReadLine(), out choice))
                {
                    Console.Clear();
                    switch (choice)
                    {
                        case 1:
                            AddCD();
                            break;
                        case 2:
                            ViewCDs();
                            break;
                        case 3:
                            UpdateCD();
                            break;
                        case 4:
                            DeleteCD();
                            break;
                        case 5:
                            AddSale();
                            break;
                        case 6:
                            ViewSales();
                            break;
                        case 7:
                            Console.WriteLine("Выход из приложения.");
                            break;
                        default:
                            Console.WriteLine("Неверный выбор. Попробуйте снова.");
                            break;
                    }
                }
                else
                {
                    Console.WriteLine("Введите числовое значение.");
                }

                Console.WriteLine("\nНажмите любую клавишу для продолжения...");
                Console.ReadKey();
                Console.Clear();

            } while (choice != 7);
        }

        // Метод для добавления нового компакт-диска
        static void AddCD()
        {
            Console.WriteLine("=== Добавление нового компакт-диска ===");

            Console.Write("Введите название альбома: ");
            string title = Console.ReadLine();

            Console.Write("Введите исполнителя: ");
            string artist = Console.ReadLine();

            Console.Write("Введите цену: ");
            decimal price;
            while (!decimal.TryParse(Console.ReadLine(), out price))
            {
                Console.Write("Введите корректное значение цены: ");
            }

            using (SqlConnection conn = new SqlConnection(connectionString))
            {
                string query = "INSERT INTO CDs (Title, Artist, Price) VALUES (@Title, @Artist, @Price)";
                SqlCommand cmd = new SqlCommand(query, conn);
                cmd.Parameters.AddWithValue("@Title", title);
                cmd.Parameters.AddWithValue("@Artist", artist);
                cmd.Parameters.AddWithValue("@Price", price);

                conn.Open();
                cmd.ExecuteNonQuery();
                conn.Close();
            }

            Console.WriteLine("Компакт-диск успешно добавлен.");
        }

        // Метод для просмотра списка компакт-дисков
        static void ViewCDs()
        {
            Console.WriteLine("=== Список компакт-дисков ===");

            using (SqlConnection conn = new SqlConnection(connectionString))
            {
                string query = "SELECT * FROM CDs";
                SqlCommand cmd = new SqlCommand(query, conn);

                conn.Open();
                SqlDataReader reader = cmd.ExecuteReader();

                Console.WriteLine("{0,-5} {1,-30} {2,-30} {3,10}", "ID", "Название", "Исполнитель", "Цена");

                while (reader.Read())
                {
                    Console.WriteLine("{0,-5} {1,-30} {2,-30} {3,10:C}", reader["CDId"], reader["Title"], reader["Artist"], reader["Price"]);
                }

                reader.Close();
                conn.Close();
            }
        }

        // Метод для обновления информации о компакт-диске
        static void UpdateCD()
        {
            Console.WriteLine("=== Обновление информации о компакт-диске ===");

            Console.Write("Введите ID компакт-диска для обновления: ");
            int cdId;
            while (!int.TryParse(Console.ReadLine(), out cdId))
            {
                Console.Write("Введите корректный ID: ");
            }

            Console.Write("Введите новое название альбома: ");
            string title = Console.ReadLine();

            Console.Write("Введите нового исполнителя: ");
            string artist = Console.ReadLine();

            Console.Write("Введите новую цену: ");
            decimal price;
            while (!decimal.TryParse(Console.ReadLine(), out price))
            {
                Console.Write("Введите корректное значение цены: ");
            }

            using (SqlConnection conn = new SqlConnection(connectionString))
            {
                string query = "UPDATE CDs SET Title = @Title, Artist = @Artist, Price = @Price WHERE CDId = @CDId";
                SqlCommand cmd = new SqlCommand(query, conn);
                cmd.Parameters.AddWithValue("@Title", title);
                cmd.Parameters.AddWithValue("@Artist", artist);
                cmd.Parameters.AddWithValue("@Price", price);
                cmd.Parameters.AddWithValue("@CDId", cdId);

                conn.Open();
                int rowsAffected = cmd.ExecuteNonQuery();
                conn.Close();

                if (rowsAffected > 0)
                {
                    Console.WriteLine("Информация о компакт-диске обновлена.");
                }
                else
                {
                    Console.WriteLine("Компакт-диск с указанным ID не найден.");
                }
            }
        }

        // Метод для удаления компакт-диска
        static void DeleteCD()
        {
            Console.WriteLine("=== Удаление компакт-диска ===");

            Console.Write("Введите ID компакт-диска для удаления: ");
            int cdId;
            while (!int.TryParse(Console.ReadLine(), out cdId))
            {
                Console.Write("Введите корректный ID: ");
            }

            using (SqlConnection conn = new SqlConnection(connectionString))
            {
                string query = "DELETE FROM CDs WHERE CDId = @CDId";
                SqlCommand cmd = new SqlCommand(query, conn);
                cmd.Parameters.AddWithValue("@CDId", cdId);

                conn.Open();
                int rowsAffected = cmd.ExecuteNonQuery();
                conn.Close();

                if (rowsAffected > 0)
                {
                    Console.WriteLine("Компакт-диск удален.");
                }
                else
                {
                    Console.WriteLine("Компакт-диск с указанным ID не найден.");
                }
            }
        }

        // Метод для регистрации продажи
        static void AddSale()
        {
            Console.WriteLine("=== Регистрация продажи ===");

            Console.Write("Введите ID компакт-диска: ");
            int cdId;
            while (!int.TryParse(Console.ReadLine(), out cdId))
            {
                Console.Write("Введите корректный ID: ");
            }

            Console.Write("Введите количество: ");
            int quantity;
            while (!int.TryParse(Console.ReadLine(), out quantity))
            {
                Console.Write("Введите корректное количество: ");
            }

            using (SqlConnection conn = new SqlConnection(connectionString))
            {
                string query = "INSERT INTO Sales (CDId, Quantity, SaleDate) VALUES (@CDId, @Quantity, @SaleDate)";
                SqlCommand cmd = new SqlCommand(query, conn);
                cmd.Parameters.AddWithValue("@CDId", cdId);
                cmd.Parameters.AddWithValue("@Quantity", quantity);
                cmd.Parameters.AddWithValue("@SaleDate", DateTime.Now);

                conn.Open();
                cmd.ExecuteNonQuery();
                conn.Close();
            }

            Console.WriteLine("Продажа успешно зарегистрирована.");
        }

        // Метод для просмотра списка продаж
        static void ViewSales()
        {
            Console.WriteLine("=== Список продаж ===");

            using (SqlConnection conn = new SqlConnection(connectionString))
            {
                string query = @"SELECT Sales.SaleId, CDs.Title, CDs.Artist, Sales.Quantity, Sales.SaleDate
                                 FROM Sales
                                 INNER JOIN CDs ON Sales.CDId = CDs.CDId";
                SqlCommand cmd = new SqlCommand(query, conn);

                conn.Open();
                SqlDataReader reader = cmd.ExecuteReader();

                Console.WriteLine("{0,-10} {1,-30} {2,-30} {3,-10} {4,-20}", "ID продажи", "Название", "Исполнитель", "Количество", "Дата продажи");

                while (reader.Read())
                {
                    Console.WriteLine("{0,-10} {1,-30} {2,-30} {3,-10} {4,-20}", reader["SaleId"], reader["Title"], reader["Artist"], reader["Quantity"], reader["SaleDate"]);
                }

                reader.Close();
                conn.Close();
            }
        }
    }
}
