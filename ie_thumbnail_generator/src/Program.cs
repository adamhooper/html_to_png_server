using System;
using System.IO;
using System.Windows.Forms;

namespace WebPageThumbnailGenerator
{
    static class Program
    {
        [STAThread]
        static void Main(string[] args)
        {
            if (args.Length != 2) {
                System.Console.WriteLine("usage: thumbnail-generator.exe [URL] [filename]");
                return;
            }

            Application.EnableVisualStyles();
            Application.SetCompatibleTextRenderingDefault(false);

            Application.Run(
                new BrowserForm(new Uri(args[0]), new FileInfo(@args[1])));
        }
    }
}
