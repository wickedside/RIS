using System;
using System.Windows.Forms;

namespace CandyClient
{
    static class Program
    {
        /// <summary>
        /// ������� ����� ����� ��� ����������.
        /// </summary>
        [STAThread]
        static void Main()
        {
            Application.EnableVisualStyles();
            Application.SetCompatibleTextRenderingDefault(false);

            // ������ �������� ����� �������
            Application.Run(new ClientForm());
        }
    }
}
