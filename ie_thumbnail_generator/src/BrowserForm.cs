using System;
using System.Drawing;
using System.Drawing.Drawing2D;
using System.Drawing.Imaging;
using System.IO;
using System.Runtime.InteropServices;
using Microsoft.VisualStudio.OLE.Interop;
using System.Windows.Forms;

namespace WebPageThumbnailGenerator
{
    public partial class BrowserForm : Form
    {
        #region fields

        private FileInfo _file = null;

        #endregion

        #region constructors

        public BrowserForm(Uri url, FileInfo file)
        {
            InitializeComponent();

            if (url == null) throw new ArgumentNullException();
            if (!url.IsAbsoluteUri) throw new ArgumentOutOfRangeException();

            if (file == null) throw new ArgumentNullException();

            _webBrowser.Width = 1024;
            _webBrowser.Height = 768;

            _webBrowser.Navigate(url.OriginalString);

            _file = file;
        }

        #endregion

        #region private methods

        private void SaveThumbnail(Image image)
        {
            if (image != null)
            {
                image.Save(_file.FullName, ImageFormat.Png);
            }
        }

        #endregion

        #region private event handlers

        private void OnDocumentCompleted(object sender, WebBrowserDocumentCompletedEventArgs e)
        {
            WebBrowser browser = (sender as WebBrowser);
            
            if (browser != null)
            {
                mshtml.IHTMLDocument2 document = (browser.Document.DomDocument as mshtml.IHTMLDocument2);
                if (document != null)
                {
                    int width = browser.Document.Body.ScrollRectangle.Width;
                    int height = browser.Document.Body.ScrollRectangle.Height;
                    browser.Width = width;
                    browser.Height = height;

                    mshtml.IHTMLElement element = (document.body as mshtml.IHTMLElement);
                    if (element != null)
                    {

                        IViewObject viewObject = (document as IViewObject);
                        if (viewObject != null) {
                            using (Graphics graphics = this.CreateGraphics())
                            {
                                IntPtr hdcDestination = graphics.GetHdc();
                                IntPtr hdcMemory = GDI32.CreateCompatibleDC(hdcDestination);
                                IntPtr bitmap = GDI32.CreateCompatibleBitmap(hdcDestination, width, height);

                                IntPtr hOld = (IntPtr)GDI32.SelectObject(hdcMemory, bitmap);

                                RECTL rect = new RECTL();
                                rect.left = 0;
                                rect.top = 0;
                                rect.right = width;
                                rect.bottom = height;
                                viewObject.Draw(
                                        DVASPECT.DVASPECT_CONTENT,
                                        -1,
                                        IntPtr.Zero,
                                        IntPtr.Zero,
                                        IntPtr.Zero,
                                        hdcMemory,
                                        ref rect,
                                        ref rect,
                                        IntPtr.Zero,
                                        (uint) 0);

                                GDI32.SelectObject(hdcMemory, hOld);
                                GDI32.DeleteDC(hdcMemory);
                                graphics.ReleaseHdc(hdcDestination);

                                SaveThumbnail(Image.FromHbitmap(bitmap));
                            }
                        }
                    }
                }
            }

            this.Close();
        }

        #endregion
    }
}
