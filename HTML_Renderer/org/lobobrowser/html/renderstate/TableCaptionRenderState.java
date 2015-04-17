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

import org.lobobrowser.html.domimpl.HTMLElementImpl;
import org.w3c.dom.Node;
import org.w3c.dom.css.CSS2Properties;

/**
 * The Class TableCaptionRenderState.
 */
public class TableCaptionRenderState extends DisplayRenderState {

    /**
     * Instantiates a new table caption render state.
     *
     * @param prevRenderState
     *            the prev render state
     * @param element
     *            the element
     */
    public TableCaptionRenderState(RenderState prevRenderState,
            HTMLElementImpl element) {
        super(prevRenderState, element, RenderState.DISPLAY_TABLE_CAPTION);
    }

    /** The align x percent. */
    private int alignXPercent = -1;

    /*
     * (non-Javadoc)
     * @see
     * org.lobobrowser.html.renderstate.StyleSheetRenderState#getAlignXPercent()
     */
    @Override
    public int getAlignXPercent() {
        int axp = this.alignXPercent;
        if (axp != -1) {
            return axp;
        }
        axp = getAlignXPercent(this.element);
        this.alignXPercent = axp;
        return axp;
    }

    /**
     * Gets the align x percent.
     *
     * @param htmlElement
     *            the html element
     * @return the align x percent
     */
    public int getAlignXPercent(HTMLElementImpl htmlElement) {
        int axp = 50; // caption text is default in middle of caption
        String textAlign = null;
        CSS2Properties props = htmlElement.getCurrentStyle();
        if (props != null) {
            textAlign = props.getTextAlign();
        }

        if ("center".equalsIgnoreCase(textAlign)) {
            axp = 50;
        } else if ("left".equalsIgnoreCase(textAlign)) {
            axp = 0;
        } else if ("right".equalsIgnoreCase(textAlign)) {
            axp = 100;
        } else if ("inherit".equalsIgnoreCase(textAlign)) {
            Node parent = htmlElement.getParentNode();
            if (parent instanceof HTMLElementImpl) {
                return getAlignXPercent((HTMLElementImpl) parent);
            }
        }
        return axp;
    }
}
