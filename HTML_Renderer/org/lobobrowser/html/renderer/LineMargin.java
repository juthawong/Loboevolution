/*
 * GNU LESSER GENERAL PUBLIC LICENSE Copyright (C) 2006 The Lobo Project.
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
package org.lobobrowser.html.renderer;

/**
 * The Class LineMargin.
 */
class LineMargin {

    /** The clear x. */
    private final int clearX;

    /** The clear y. */
    private final int clearY;

    /** The next. */
    private final LineMargin next;

    /**
     * Instantiates a new line margin.
     *
     * @param next
     *            the next
     * @param cleary
     *            the cleary
     * @param totalXOffset
     *            the total x offset
     */
    public LineMargin(LineMargin next, int cleary, int totalXOffset) {
        super();
        this.next = next;
        this.clearY = cleary;
        this.clearX = totalXOffset;
    }

    /**
     * Gets the clear y.
     *
     * @return the clear y
     */
    public int getClearY() {
        return clearY;
    }

    /**
     * Gets the next.
     *
     * @return the next
     */
    public LineMargin getNext() {
        return next;
    }

    /**
     * Gets the offset.
     *
     * @return the offset
     */
    public int getOffset() {
        return clearX;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object other) {
        if (!(other instanceof LineMargin)) {
            return false;
        }
        LineMargin olm = (LineMargin) other;
        return (olm.clearX == this.clearX) && (olm.clearY == this.clearY)
                && org.lobobrowser.util.Objects.equals(olm.next, this.next);
    }

    /**
     * Translated.
     *
     * @param yoffset
     *            the yoffset
     * @param xoffset
     *            the xoffset
     * @return the line margin
     */
    public LineMargin translated(int yoffset, int xoffset) {
        int newClearY = this.clearY - yoffset;
        int newOffsetX = this.clearX - xoffset;
        if (newOffsetX < 0) {
            newOffsetX = 0;
        }
        LineMargin oldNext = this.next;
        LineMargin newNext = oldNext == null ? null : oldNext.translated(
                yoffset, xoffset);
        return new LineMargin(newNext, newClearY, newOffsetX);
    }
}
