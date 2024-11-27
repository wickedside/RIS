using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Sockets;
using System.Text;
using System.Windows.Forms;

namespace CandyClient
{
    public class ClientForm : Form
    {
        private TextBox textBoxProductName;
        private TextBox textBoxQuantity;
        private TextBox textBoxPrice;
        private Button buttonAdd;
        private Button buttonEdit;
        private Button buttonDelete;
        private Button buttonSearch;
        private ListView listViewRecords;
        private Label labelProductName;
        private Label labelQuantity;
        private Label labelPrice;

        TcpClient client;
        StreamReader reader;
        StreamWriter writer;
        string serverAddress = "127.0.0.1";
        int port = 5000;

        public ClientForm()
        {
            // Настройка формы
            this.Text = "Клиентское приложение конфетной фабрики";
            this.Width = 487;
            this.Height = 600;

            // Создание элементов управления
            InitializeComponents();

            // Подключение к серверу
            ConnectToServer();

            // Загрузка данных при запуске приложения
            RequestAndUpdateData();
        }

        private void InitializeComponents()
        {
            // Метки
            labelProductName = new Label();
            labelProductName.Text = "Название продукта:";
            labelProductName.Top = 10;
            labelProductName.Left = 10;
            labelProductName.Width = 120;
            this.Controls.Add(labelProductName);

            labelQuantity = new Label();
            labelQuantity.Text = "Количество:";
            labelQuantity.Top = labelProductName.Bottom + 10;
            labelQuantity.Left = 10;
            labelQuantity.Width = 120;
            this.Controls.Add(labelQuantity);

            labelPrice = new Label();
            labelPrice.Text = "Цена:";
            labelPrice.Top = labelQuantity.Bottom + 10;
            labelPrice.Left = 10;
            labelPrice.Width = 120;
            this.Controls.Add(labelPrice);

            // Текстовые поля
            textBoxProductName = new TextBox();
            textBoxProductName.Top = 10;
            textBoxProductName.Left = labelProductName.Right + 10;
            textBoxProductName.Width = 200;
            this.Controls.Add(textBoxProductName);

            textBoxQuantity = new TextBox();
            textBoxQuantity.Top = textBoxProductName.Bottom + 10;
            textBoxQuantity.Left = labelQuantity.Right + 10;
            textBoxQuantity.Width = 200;
            this.Controls.Add(textBoxQuantity);

            textBoxPrice = new TextBox();
            textBoxPrice.Top = textBoxQuantity.Bottom + 10;
            textBoxPrice.Left = labelPrice.Right + 10;
            textBoxPrice.Width = 200;
            this.Controls.Add(textBoxPrice);

            // Кнопки
            buttonAdd = new Button();
            buttonAdd.Text = "Добавить";
            buttonAdd.Top = textBoxPrice.Bottom + 20;
            buttonAdd.Left = 10;
            buttonAdd.Click += ButtonAdd_Click;
            this.Controls.Add(buttonAdd);

            buttonEdit = new Button();
            buttonEdit.Text = "Изменить";
            buttonEdit.Top = buttonAdd.Top;
            buttonEdit.Left = buttonAdd.Right + 10;
            buttonEdit.Click += ButtonEdit_Click;
            this.Controls.Add(buttonEdit);

            buttonDelete = new Button();
            buttonDelete.Text = "Удалить";
            buttonDelete.Top = buttonAdd.Top;
            buttonDelete.Left = buttonEdit.Right + 10;
            buttonDelete.Click += ButtonDelete_Click;
            this.Controls.Add(buttonDelete);

            buttonSearch = new Button();
            buttonSearch.Text = "Поиск";
            buttonSearch.Top = buttonAdd.Top;
            buttonSearch.Left = buttonDelete.Right + 10;
            buttonSearch.Click += ButtonSearch_Click;
            this.Controls.Add(buttonSearch);

            // Список записей
            listViewRecords = new ListView();
            listViewRecords.Top = buttonAdd.Bottom + 20;
            listViewRecords.Left = 10;
            listViewRecords.Width = this.ClientSize.Width - 20;
            listViewRecords.Height = this.ClientSize.Height - listViewRecords.Top - 10;
            listViewRecords.Anchor = AnchorStyles.Top | AnchorStyles.Bottom | AnchorStyles.Left | AnchorStyles.Right;
            listViewRecords.View = View.Details;
            listViewRecords.FullRowSelect = true;
            listViewRecords.Columns.Add("ID", 50);
            listViewRecords.Columns.Add("Название продукта", 200);
            listViewRecords.Columns.Add("Количество", 100);
            listViewRecords.Columns.Add("Цена", 100);
            listViewRecords.SelectedIndexChanged += ListViewRecords_SelectedIndexChanged;
            this.Controls.Add(listViewRecords);
        }

        // Подключение к серверу
        private void ConnectToServer()
        {
            try
            {
                client = new TcpClient(serverAddress, port);
                NetworkStream stream = client.GetStream();
                reader = new StreamReader(stream, Encoding.UTF8);
                writer = new StreamWriter(stream, Encoding.UTF8) { AutoFlush = true };
                Log("Подключено к серверу.");
            }
            catch (Exception ex)
            {
                Log($"Ошибка подключения: {ex.Message}");
                MessageBox.Show($"Ошибка подключения к серверу: {ex.Message}");
            }
        }

        // Метод для запроса данных с сервера и обновления списка
        private void RequestAndUpdateData()
        {
            string request = "VIEW|";
            string response = SendRequest(request);
            ProcessResponse(response);
        }

        // Обработчик кнопки "Добавить"
        private void ButtonAdd_Click(object sender, EventArgs e)
        {
            string productName = textBoxProductName.Text.Trim();
            if (string.IsNullOrEmpty(productName))
            {
                MessageBox.Show("Введите название продукта.");
                return;
            }

            if (!int.TryParse(textBoxQuantity.Text, out int quantity))
            {
                MessageBox.Show("Некорректное количество.");
                return;
            }
            if (!decimal.TryParse(textBoxPrice.Text, out decimal price))
            {
                MessageBox.Show("Некорректная цена.");
                return;
            }

            string request = $"ADD|{productName}|{quantity}|{price}";
            string response = SendRequest(request);
            ProcessResponse(response);

            // Обновляем список после добавления
            RequestAndUpdateData();
        }

        // Обработчик кнопки "Редактировать"
        private void ButtonEdit_Click(object sender, EventArgs e)
        {
            if (listViewRecords.SelectedItems.Count == 0)
            {
                MessageBox.Show("Выберите запись для редактирования.");
                return;
            }

            string id = listViewRecords.SelectedItems[0].SubItems[0].Text;
            string productName = textBoxProductName.Text.Trim();
            if (string.IsNullOrEmpty(productName))
            {
                MessageBox.Show("Введите название продукта.");
                return;
            }

            if (!int.TryParse(textBoxQuantity.Text, out int quantity))
            {
                MessageBox.Show("Некорректное количество.");
                return;
            }
            if (!decimal.TryParse(textBoxPrice.Text, out decimal price))
            {
                MessageBox.Show("Некорректная цена.");
                return;
            }

            string request = $"EDIT|{id}|{productName}|{quantity}|{price}";
            string response = SendRequest(request);
            ProcessResponse(response);

            // Обновляем список после редактирования
            RequestAndUpdateData();
        }

        // Обработчик кнопки "Удалить"
        private void ButtonDelete_Click(object sender, EventArgs e)
        {
            if (listViewRecords.SelectedItems.Count == 0)
            {
                MessageBox.Show("Выберите запись для удаления.");
                return;
            }

            string id = listViewRecords.SelectedItems[0].SubItems[0].Text;
            string request = $"DELETE|{id}";
            string response = SendRequest(request);
            ProcessResponse(response);

            // Обновляем список после удаления
            RequestAndUpdateData();
        }

        // Обработчик кнопки "Поиск"
        private void ButtonSearch_Click(object sender, EventArgs e)
        {
            string searchTerm = textBoxProductName.Text.Trim();
            if (string.IsNullOrEmpty(searchTerm))
            {
                MessageBox.Show("Введите название продукта для поиска.");
                return;
            }

            string request = $"SEARCH|{searchTerm}";
            string response = SendRequest(request);
            ProcessResponse(response);
        }

        // Обработчик выбора записи в списке
        private void ListViewRecords_SelectedIndexChanged(object sender, EventArgs e)
        {
            if (listViewRecords.SelectedItems.Count > 0)
            {
                var item = listViewRecords.SelectedItems[0];
                textBoxProductName.Text = item.SubItems[1].Text;
                textBoxQuantity.Text = item.SubItems[2].Text;
                textBoxPrice.Text = item.SubItems[3].Text;
            }
        }

        // Отправка запроса на сервер
        private string SendRequest(string request)
        {
            try
            {
                writer.WriteLine(request);
                string response = reader.ReadLine();
                return response;
            }
            catch (Exception ex)
            {
                return $"ERROR|{ex.Message}";
            }
        }

        // Обработка ответа от сервера
        private void ProcessResponse(string response)
        {
            // Разделяем ответ на статус и содержимое
            string[] parts = response.Split(new[] { '|' }, 2);
            string status = parts[0];

            if (status == "OK")
            {
                if (parts.Length > 1)
                {
                    string content = parts[1];

                    // Проверяем, содержит ли содержимое данные (наличие символа ';')
                    if (content.Contains(";"))
                    {
                        // Это данные, обновляем список записей
                        UpdateListView(content);
                    }
                    else
                    {
                        // Это сообщение от сервера
                        MessageBox.Show(content);
                    }
                }
                else
                {
                    // Нет содержимого, очищаем список
                    listViewRecords.Items.Clear();
                }
            }
            else if (status == "ERROR")
            {
                if (parts.Length > 1)
                {
                    MessageBox.Show($"Ошибка: {parts[1]}");
                }
                else
                {
                    MessageBox.Show("Неизвестная ошибка.");
                }
            }
        }

        // Обновление списка записей
        private void UpdateListView(string data)
        {
            listViewRecords.Items.Clear();
            var lines = data.Split(new[] { '#' }, StringSplitOptions.RemoveEmptyEntries);

            foreach (var line in lines)
            {
                var parts = line.Split(';');
                if (parts.Length == 4)
                {
                    var item = new ListViewItem(parts[0]); // ID
                    item.SubItems.Add(parts[1]); // Название продукта
                    item.SubItems.Add(parts[2]); // Количество
                    item.SubItems.Add(parts[3]); // Цена
                    listViewRecords.Items.Add(item);
                }
            }
        }

        // Вывод сообщений в консоль (для отладки)
        private void Log(string message)
        {
            Console.WriteLine(message);
        }

        // Закрытие соединения при закрытии формы
        protected override void OnFormClosing(FormClosingEventArgs e)
        {
            client?.Close();
            base.OnFormClosing(e);
        }
    }
}
