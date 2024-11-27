using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Net;
using System.Net.Sockets;
using System.Text;
using System.Threading;

namespace CandyServer
{
    public class Server
    {
        private TcpListener server;
        private bool isRunning;
        private Thread serverThread;
        private string dataFilePath = "server_data.txt";
        private readonly object lockObject = new object();

        public void Start()
        {
            isRunning = true;
            serverThread = new Thread(RunServer);
            serverThread.Start();
            Console.WriteLine("Сервер запущен.");
        }

        public void Stop()
        {
            isRunning = false;
            server?.Stop();
            serverThread?.Join();
        }

        private void RunServer()
        {
            try
            {
                server = new TcpListener(IPAddress.Any, 5000);
                server.Start();

                while (isRunning)
                {
                    TcpClient client = server.AcceptTcpClient();
                    ThreadPool.QueueUserWorkItem(HandleClient, client);
                }
            }
            catch (SocketException ex)
            {
                if (isRunning)
                {
                    Console.WriteLine($"Ошибка сервера: {ex.Message}");
                }
            }
        }

        private void HandleClient(object clientObj)
        {
            using (TcpClient client = (TcpClient)clientObj)
            using (NetworkStream stream = client.GetStream())
            using (StreamReader reader = new StreamReader(stream, Encoding.UTF8))
            using (StreamWriter writer = new StreamWriter(stream, Encoding.UTF8) { AutoFlush = true })
            {
                try
                {
                    string request;
                    while ((request = reader.ReadLine()) != null)
                    {
                        Console.WriteLine($"Получен запрос: {request}");
                        string response = ProcessRequest(request);
                        writer.WriteLine(response);
                        Console.WriteLine($"Отправлен ответ: {response}");
                    }
                }
                catch (Exception ex)
                {
                    Console.WriteLine($"Ошибка клиента: {ex.Message}");
                }
            }
        }

        private string ProcessRequest(string request)
        {
            string[] parts = request.Split('|');
            string command = parts[0];

            switch (command)
            {
                case "ADD":
                    if (parts.Length == 4)
                        return AddRecord(parts[1], int.Parse(parts[2]), decimal.Parse(parts[3]));
                    else
                        return "ERROR|Неверный формат команды ADD";
                case "EDIT":
                    if (parts.Length == 5)
                        return EditRecord(int.Parse(parts[1]), parts[2], int.Parse(parts[3]), decimal.Parse(parts[4]));
                    else
                        return "ERROR|Неверный формат команды EDIT";
                case "DELETE":
                    if (parts.Length == 2)
                        return DeleteRecord(int.Parse(parts[1]));
                    else
                        return "ERROR|Неверный формат команды DELETE";
                case "VIEW":
                    return ViewRecords();
                case "SEARCH":
                    if (parts.Length == 2)
                        return SearchRecords(parts[1]);
                    else
                        return "ERROR|Неверный формат команды SEARCH";
                default:
                    return "ERROR|Неизвестная команда";
            }
        }

        private string AddRecord(string productName, int quantity, decimal price)
        {
            try
            {
                int newId = 1; // Объявляем newId до блока lock
                lock (lockObject)
                {
                    var records = new List<SaleRecord>();

                    if (File.Exists(dataFilePath))
                    {
                        records = LoadData();
                        if (records.Count > 0)
                            newId = records.Max(r => r.Id) + 1;
                    }

                    var newRecord = new SaleRecord
                    {
                        Id = newId,
                        ProductName = productName,
                        Quantity = quantity,
                        Price = price
                    };

                    records.Add(newRecord);
                    SaveData(records);
                }

                return $"OK|Запись добавлена с ID {newId}";
            }
            catch (Exception ex)
            {
                return $"ERROR|{ex.Message}";
            }
        }

        private string EditRecord(int id, string productName, int quantity, decimal price)
        {
            try
            {
                lock (lockObject)
                {
                    var records = LoadData();
                    var record = records.FirstOrDefault(r => r.Id == id);
                    if (record != null)
                    {
                        record.ProductName = productName;
                        record.Quantity = quantity;
                        record.Price = price;
                        SaveData(records);
                        return "OK|Запись обновлена";
                    }
                    else
                    {
                        return "ERROR|Запись не найдена";
                    }
                }
            }
            catch (Exception ex)
            {
                return $"ERROR|{ex.Message}";
            }
        }

        private string DeleteRecord(int id)
        {
            try
            {
                lock (lockObject)
                {
                    var records = LoadData();
                    var record = records.FirstOrDefault(r => r.Id == id);
                    if (record != null)
                    {
                        records.Remove(record);
                        SaveData(records);
                        return "OK|Запись удалена";
                    }
                    else
                    {
                        return "ERROR|Запись не найдена";
                    }
                }
            }
            catch (Exception ex)
            {
                return $"ERROR|{ex.Message}";
            }
        }

        private string ViewRecords()
        {
            try
            {
                var records = LoadData();
                StringBuilder sb = new StringBuilder();
                foreach (var record in records)
                {
                    sb.Append($"{record.Id};{record.ProductName};{record.Quantity};{record.Price}#");
                }
                return $"OK|{sb.ToString()}";
            }
            catch (Exception ex)
            {
                return $"ERROR|{ex.Message}";
            }
        }

        private string SearchRecords(string searchTerm)
        {
            try
            {
                var records = LoadData();
                var results = records.Where(r => r.ProductName.IndexOf(searchTerm, StringComparison.OrdinalIgnoreCase) >= 0).ToList();

                if (results.Count > 0)
                {
                    StringBuilder sb = new StringBuilder();
                    foreach (var record in results)
                    {
                        sb.Append($"{record.Id};{record.ProductName};{record.Quantity};{record.Price}#");
                    }
                    return $"OK|{sb.ToString()}";
                }
                else
                {
                    return "OK|";
                }
            }
            catch (Exception ex)
            {
                return $"ERROR|{ex.Message}";
            }
        }

        private List<SaleRecord> LoadData()
        {
            var records = new List<SaleRecord>();
            if (File.Exists(dataFilePath))
            {
                var lines = File.ReadAllLines(dataFilePath);
                foreach (var line in lines)
                {
                    var parts = line.Split(';');
                    if (parts.Length == 4)
                    {
                        records.Add(new SaleRecord
                        {
                            Id = int.Parse(parts[0]),
                            ProductName = parts[1],
                            Quantity = int.Parse(parts[2]),
                            Price = decimal.Parse(parts[3])
                        });
                    }
                }
            }
            return records;
        }

        private void SaveData(List<SaleRecord> records)
        {
            var lines = records.Select(r => $"{r.Id};{r.ProductName};{r.Quantity};{r.Price}");
            File.WriteAllLines(dataFilePath, lines);
        }
    }
}