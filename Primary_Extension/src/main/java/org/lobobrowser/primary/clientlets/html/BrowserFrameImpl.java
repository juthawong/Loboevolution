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
/*
 * Created on Feb 5, 2006
 */
package org.lobobrowser.primary.clientlets.html;

import java.awt.Component;
import java.awt.Insets;
import java.net.URL;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.lobobrowser.html.BrowserFrame;
import org.lobobrowser.html.HtmlRendererContext;
import org.lobobrowser.ua.NavigatorFrame;
import org.lobobrowser.ua.RequestType;
import org.lobobrowser.ua.TargetType;

/**
 * The Class BrowserFrameImpl.
 */
public class BrowserFrameImpl implements BrowserFrame {

	/** The Constant logger. */
	private static final Logger logger = LogManager.getLogger(BrowserFrameImpl.class);

	/** The frame. */
	private final NavigatorFrame frame;

	/** The rcontext. */
	private final HtmlRendererContextImpl rcontext;

	/**
	 * Instantiates a new browser frame impl.
	 *
	 * @param frame
	 *            the frame
	 * @param parentrcontext
	 *            the parentrcontext
	 */
	public BrowserFrameImpl(NavigatorFrame frame, HtmlRendererContext parentrcontext) {
		if (logger.isInfoEnabled()) {
			logger.info("BrowserFrameImpl(): frame=" + frame + ",parentrcontext=" + parentrcontext);
		}
		this.frame = frame;
		this.rcontext = HtmlRendererContextImpl.getHtmlRendererContext(frame);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.lobobrowser.html.BrowserFrame#getHtmlRendererContext()
	 */
	@Override
	public HtmlRendererContext getHtmlRendererContext() {
		return this.rcontext;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.lobobrowser.html.BrowserFrame#getComponent()
	 */
	@Override
	public Component getComponent() {
		return this.frame.getComponent();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.lobobrowser.html.BrowserFrame#getContentDocument()
	 */
	@Override
	public org.w3c.dom.Document getContentDocument() {
		return this.rcontext.getContentDocument();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.lobobrowser.html.BrowserFrame#loadURL(java.net.URL)
	 */
	@Override
	public void loadURL(URL url) {
		if (logger.isInfoEnabled()) {
			logger.info("loadURL(): frame=" + frame + ",url=" + url);
		}
		this.frame.navigate(url, "GET", null, TargetType.SELF, RequestType.FRAME);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.lobobrowser.html.BrowserFrame#setDefaultMarginInsets(java.awt.Insets)
	 */
	@Override
	public void setDefaultMarginInsets(Insets insets) {
		this.frame.setProperty("defaultMarginInsets", insets);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.lobobrowser.html.BrowserFrame#setDefaultOverflowX(int)
	 */
	@Override
	public void setDefaultOverflowX(int overflowX) {
		this.frame.setProperty("defaultOverflowX", overflowX);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.lobobrowser.html.BrowserFrame#setDefaultOverflowY(int)
	 */
	@Override
	public void setDefaultOverflowY(int overflowY) {
		this.frame.setProperty("defaultOverflowY", overflowY);
	}
}
