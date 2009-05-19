namespace WebPageThumbnailGenerator
{
    partial class BrowserForm
    {
        /// <summary>
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code

        /// <summary>
        /// Required method for Designer support - do not modify
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            this._webBrowser = new System.Windows.Forms.WebBrowser();
            this.SuspendLayout();
            // 
            // _webBrowser
            // 
            this._webBrowser.ScriptErrorsSuppressed = true;
            this._webBrowser.ScrollBarsEnabled = false;
            this._webBrowser.Size = new System.Drawing.Size(800, 600);
            this._webBrowser.DocumentCompleted += new System.Windows.Forms.WebBrowserDocumentCompletedEventHandler(this.OnDocumentCompleted);
            // 
            // BrowserForm
            // 
            this.ClientSize = new System.Drawing.Size(800, 600);
            this.Controls.Add(this._webBrowser);
            this.ShowInTaskbar = false;
            this.WindowState = System.Windows.Forms.FormWindowState.Minimized;
            this.ResumeLayout(false);

        }

        #endregion

        private System.Windows.Forms.WebBrowser _webBrowser;
    }
}
