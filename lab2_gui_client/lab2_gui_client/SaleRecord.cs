namespace CandyClient
{
    public class SaleRecord
    {
        public int Id { get; set; } // Уникальный идентификатор записи
        public string ProductName { get; set; } // Название продукта
        public int Quantity { get; set; } // Количество
        public decimal Price { get; set; } // Цена
    }
}