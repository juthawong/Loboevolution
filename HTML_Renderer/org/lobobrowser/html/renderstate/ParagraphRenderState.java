/*
 * GNU LESSER GENERAL PUBLIC LICENSE Copyright (C) 2006 The XAMJ Project.
 * Copyright (C) 2014 - 2015 Lobo Evolution This library is free software; you
 * can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version. This
 * library is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details. You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA
 * Contact info: lobochief@users.sourceforge.net; ivan.difrancesco@yahoo.it
 */
package org.lobobrowser.html.renderstate;

import java.awt.FontMetrics;

import org.lobobrowser.html.domimpl.HTMLElementImpl;
import org.lobobrowser.html.style.HtmlInsets;

/**
 * The Class ParagraphRenderState.
 */
public class ParagraphRenderState extends AbstractMarginRenderState {

    /**
     * Instantiates a new paragraph render state.
     *
     * @param prevRenderState
     *            the prev render state
     * @param element
     *            the element
     */
    public ParagraphRenderState(RenderState prevRenderState,
            HTMLElementImpl element) {
        super(prevRenderState, element);
    }

    /*
     * (non-Javadoc)
     * @see
     * org.lobobrowser.html.renderstate.AbstractMarginRenderState#getDefaultMarginInsets
     * ()
     */
    @Override
    protected HtmlInsets getDefaultMarginInsets() {
        HtmlInsets insets = new HtmlInsets();
        RenderState prevRS = this.getPreviousRenderState();
        FontMetrics fm = prevRS == null ? this.getFontMetrics() : prevRS
                .getFontMetrics();
        insets.top = fm.getHeight();
        insets.bottom = fm.getHeight();
        insets.topType = HtmlInsets.TYPE_PIXELS;
        insets.bottomType = HtmlInsets.TYPE_PIXELS;
        return insets;
    }

}
