/*
 * GNU LESSER GENERAL PUBLIC LICENSE Copyright (C) 2006 The XAMJ Project This
 * library is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version. This library is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser
 * General Public License for more details. You should have received a copy of
 * the GNU Lesser General Public License along with this library; if not, write
 * to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston,
 * MA 02110-1301 USA Contact info: lobochief@users.sourceforge.net
 */
package org.lobobrowser.html.layout;

import org.lobobrowser.html.UserAgentContext;
import org.lobobrowser.html.domimpl.HTMLElementImpl;
import org.lobobrowser.html.renderer.MarkupLayout;
import org.lobobrowser.html.renderer.RBlockViewport;

/**
 * The Class NoScriptLayout.
 */
public class NoScriptLayout implements MarkupLayout {

    /*
     * (non-Javadoc)
     * @see
     * org.lobobrowser.html.renderer.MarkupLayout#layoutMarkup(org.lobobrowser.html
     * .renderer.RBlockViewport, org.lobobrowser.html.domimpl.HTMLElementImpl)
     */
    @Override
    public void layoutMarkup(RBlockViewport bodyLayout,
            HTMLElementImpl markupElement) {
        UserAgentContext ucontext = bodyLayout.getUserAgentContext();
        if (!ucontext.isScriptingEnabled()) {
            bodyLayout.layoutMarkup(markupElement);
        } else {
            // NOP
        }
    }
}
