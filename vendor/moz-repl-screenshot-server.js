defineInteractor('screenshot', {
    handleInput: function(repl, input) {
        // Given an HTTP _request_, return an array containing the verb,
        // the path, and protocol version, e.g. ['GET', '/foo/bar', 'HTTP/1.1']
        //
        // Careful, the implementation is as naive as it can get!

        function parseHTTPRequest(request) {
            return request
                .split(/[\r\n]+/)[0] // get first line
                .split(/\s+/); // split it
        }

        // Strip leading and trailing whitespace
        var input = input.replace(/^\s+|\s+$/g, '');

        var [verb, path, protocolVersion] = parseHTTPRequest(input);
        if(verb != 'GET')
            return;

        var browserWindow = Cc['@mozilla.org/appshell/window-mediator;1']
            .getService(Ci.nsIWindowMediator)
            .getMostRecentWindow('navigator:browser');
        var tabbrowser = browserWindow.getBrowser();

        var canvas = browserWindow.document.createElementNS('http://www.w3.org/1999/xhtml', 'canvas');
        var tab = tabbrowser.addTab();
        var browser = tabbrowser.getBrowserForTab(tab); // tab.linkedBrowser ?

        browser.addEventListener('load', function() {
						var timer = Cc['@mozilla.org/timer;1'].createInstance(Ci.nsITimer);
						timer.initWithCallback({notify: function() {
							var win = browser.contentWindow;
							var width = win.document.width;
							var height = win.document.height;
							var computedStyle = win.document.defaultView.getComputedStyle(win.document.body, '');
							var marginLeft = parseInt(computedStyle.getPropertyValue('margin-left'), 10);
							var marginTop = parseInt(computedStyle.getPropertyValue('margin-top'), 10);
							canvas.width = width;
							canvas.height = height;
							var ctx = canvas.getContext('2d');
							ctx.clearRect(0, 0, canvas.width, canvas.height);
							ctx.save();
							ctx.scale(1.0, 1.0);
							ctx.drawWindow(win, marginLeft, marginTop, width, height, 'rgb(255,255,255)');
							ctx.restore();

							repl.onOutput('HTTP/1.1 200 OK\r\n' +
														'Content-Type: image/png\r\n' +
														'\r\n' +
														atob(canvas
																 .toDataURL('image/png', '')
																 .split(',')[1]))

							tabbrowser.removeTab(tab);
							repl.quit();
						}}, 0, Ci.nsITimer.TYPE_ONE_SHOT);
        }, true);

        var url = decodeURIComponent(path.match(/\/screenshot\/(.*$)/)[1]); // "/screenshot/http://www.google.com" -> "http://www.google.com" 
        browser.loadURI(url);
    },

    onStart: function(repl) {},

    onStop: function(repl) {},

    getPrompt: function(repl) {},
});
