/*
    GNU GENERAL LICENSE
    Copyright (C) 2006 The Lobo Project. Copyright (C) 2014 - 2016 Lobo Evolution

    This program is free software; you can redistribute it and/or
    modify it under the terms of the GNU General Public
    License as published by the Free Software Foundation; either
    verion 3 of the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
    General License for more details.

    You should have received a copy of the GNU General Public
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
    

    Contact info: lobochief@users.sourceforge.net; ivan.difrancesco@yahoo.it
 */
package org.lobobrowser.cobra_testing;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;

import org.apache.logging.log4j.LogManager;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.lobobrowser.html.HtmlRendererContext;
import org.lobobrowser.html.gui.HtmlPanel;
import org.lobobrowser.html.parser.DocumentBuilderImpl;
import org.lobobrowser.html.parser.InputSourceImpl;
import org.lobobrowser.html.test.SimpleHtmlRendererContext;
import org.lobobrowser.html.test.SimpleUserAgentContext;
import org.lobobrowser.http.UserAgentContext;
import org.lobobrowser.w3c.html.HTMLElement;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

/**
 * The Class BarebonesTest.
 */
public class BarebonesTest {
	public static void main(String[] args) throws Exception {
		// Initialize logging so only Cobra.warns are seen.
		LogManager.getLogger("org.lobobrowser");

		// Open a connection on the URL we want to render first.
		String uri = "http://www.google.com";
		URL url = new URL(uri);
		URLConnection connection = url.openConnection();
		InputStream in = connection.getInputStream();

		// A Reader should be created with the correct charset,
		// which may be obtained from the Content-Type header
		// of an HTTP response.
		Reader reader = new InputStreamReader(in);

		// InputSourceImpl constructor with URI recommended
		// so the renderer can resolve page component URLs.
		InputSource is = new InputSourceImpl(reader, uri);
		HtmlPanel htmlPanel = new HtmlPanel();
		UserAgentContext ucontext = new LocalUserAgentContext();
		HtmlRendererContext rendererContext = new LocalHtmlRendererContext(htmlPanel, ucontext);

		// Set a preferred width for the HtmlPanel,
		// which will allow getPreferredSize() to
		// be calculated according to block content.
		// We do this here to illustrate the
		// feature, but is generally not
		// recommended for performance reasons.
		htmlPanel.setPreferredWidth(800);

		// Note: This example does not perform incremental
		// rendering while loading the initial document.
		DocumentBuilderImpl builder = new DocumentBuilderImpl(rendererContext.getUserAgentContext(), rendererContext);

		Document document = builder.parse(is);
		in.close();

		// Set the document in the HtmlPanel. This method
		// schedules the document to be rendered in the
		// GUI thread.
		htmlPanel.setDocument(document, rendererContext);

		// Create a JFrame and add the HtmlPanel to it.
		final JFrame frame = new JFrame();
		frame.getContentPane().add(htmlPanel);

		// We pack the JFrame to demonstrate the
		// validity of HtmlPanel's preferred size.
		// Normally you would want to set a specific
		// JFrame size instead.

		// pack() should be called in the GUI dispatch
		// thread since the document is scheduled to
		// be rendered in that thread, and is required
		// for the preferred size determination.
		if (SwingUtilities.isEventDispatchThread()) {
			frame.pack();
			frame.setVisible(true);
		} else {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					frame.pack();
					frame.setVisible(true);
				}
			});
		}
	}

	/**
	 * The Class LocalUserAgentContext.
	 */
	private static class LocalUserAgentContext extends SimpleUserAgentContext {
		// Override methods from SimpleUserAgentContext to
		// provide more accurate information about application.

		public LocalUserAgentContext() {
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.lobobrowser.html.test.SimpleUserAgentContext#getAppMinorVersion()
		 */
		@Override
		public String getAppMinorVersion() {
			return "0";
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.lobobrowser.html.test.SimpleUserAgentContext#getAppName()
		 */
		@Override
		public String getAppName() {
			return "BarebonesTest";
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.lobobrowser.html.test.SimpleUserAgentContext#getAppVersion()
		 */
		@Override
		public String getAppVersion() {
			return "1";
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.lobobrowser.html.test.SimpleUserAgentContext#getUserAgent()
		 */
		@Override
		public String getUserAgent() {
			return "Mozilla/4.0 (compatible;) CobraTest/1.0";
		}
	}

	/**
	 * The Class LocalHtmlRendererContext.
	 */
	private static class LocalHtmlRendererContext extends SimpleHtmlRendererContext {
		// Override methods from SimpleHtmlRendererContext
		// to provide browser functionality to the renderer.

		public LocalHtmlRendererContext(HtmlPanel contextComponent, UserAgentContext ucontext) {
			super(contextComponent, ucontext);
		}

		@Override
		public void linkClicked(HTMLElement linkNode, URL url, String target) {
			super.linkClicked(linkNode, url, target);
			// This may be removed:
			System.out.println("## Link clicked: " + linkNode);
		}

		@Override
		public HtmlRendererContext open(URL url, String windowName, String windowFeatures, boolean replace) {
			// This is called on window.open().
			HtmlPanel newPanel = new HtmlPanel();
			JFrame frame = new JFrame();
			frame.setSize(600, 400);
			frame.getContentPane().add(newPanel);
			HtmlRendererContext newCtx = new LocalHtmlRendererContext(newPanel, this.getUserAgentContext());
			newCtx.navigate(url, "_this");
			return newCtx;
		}
	}
}
