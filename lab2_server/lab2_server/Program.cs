using System;

namespace CandyServer
{
    class Program
    {
        static void Main(string[] args)
        {
            Console.Title = "Candy Server";
            Console.WriteLine("Сервер запускается...");

            Server server = new Server();
            server.Start();

            Console.WriteLine("Нажмите любую клавишу для остановки сервера...");
            Console.ReadKey();

            server.Stop();
            Console.WriteLine("Сервер остановлен.");
        }
    }
}