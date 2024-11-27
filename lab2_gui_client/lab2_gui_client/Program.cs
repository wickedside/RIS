using System;
using System.Windows.Forms;

namespace CandyClient
{
    static class Program
    {
        /// <summary>
        /// Главная точка входа для приложения.
        /// </summary>
        [STAThread]
        static void Main()
        {
            Application.EnableVisualStyles();
            Application.SetCompatibleTextRenderingDefault(false);

            // Запуск основной формы клиента
            Application.Run(new ClientForm());
        }
    }
}
