package com.shaic.arch.table;

import java.io.Serializable;

public interface Sizeable  extends Serializable{

    /**
     * @deprecated As of 7.0, use {@link Unit#PIXELS} instead Â  Â 
     */
    @Deprecated
    public static final Unit UNITS_PIXELS = Unit.PIXELS;

    /**
     * @deprecated As of 7.0, use {@link Unit#POINTS} instead Â  Â 
     */
    @Deprecated
    public static final Unit UNITS_POINTS = Unit.POINTS;

    /**
     * @deprecated As of 7.0, use {@link Unit#PICAS} instead Â  Â 
     */
    @Deprecated
    public static final Unit UNITS_PICAS = Unit.PICAS;

    /**
     * @deprecated As of 7.0, use {@link Unit#EM} instead Â  Â 
     */
    @Deprecated
    public static final Unit UNITS_EM = Unit.EM;

    /**
     * @deprecated As of 7.0, use {@link Unit#EX} instead Â  Â 
     */
    @Deprecated
    public static final Unit UNITS_EX = Unit.EX;

    /**
     * @deprecated As of 7.0, use {@link Unit#MM} instead Â  Â 
     */
    @Deprecated
    public static final Unit UNITS_MM = Unit.MM;

    /**
     * @deprecated As of 7.0, use {@link Unit#CM} instead Â  Â 
     */
    @Deprecated
    public static final Unit UNITS_CM = Unit.CM;

    /**
     * @deprecated As of 7.0, use {@link Unit#INCH} instead Â  Â 
     */
    @Deprecated
    public static final Unit UNITS_INCH = Unit.INCH;

    /**
     * @deprecated As of 7.0, use {@link Unit#PERCENTAGE} instead Â  Â 
     */
    @Deprecated
    public static final Unit UNITS_PERCENTAGE = Unit.PERCENTAGE;

    /**
     * @deprecated As of 7.3, use instead {@link #setSizeUndefined()},
     *             {@link #setHeightUndefined()} and
     *             {@link #setWidthUndefined()}
     */
    @Deprecated
    public static final float SIZE_UNDEFINED = -1;

    public enum Unit {
        /**
         * Unit code representing pixels.
         */
        PIXELS("px"),
        /**
         * Unit code representing points (1/72nd of an inch).
         */
        POINTS("pt"),
        /**
         * Unit code representing picas (12 points).
         */
        PICAS("pc"),
        /**
         * Unit code representing the font-size of the relevant font.
         */
        EM("em"),
        /**
         * Unit code representing the font-size of the root font.
         */
        REM("rem"),
        /**
         * Unit code representing the x-height of the relevant font.
         */
        EX("ex"),
        /**
         * Unit code representing millimeters.
         */
        MM("mm"),
        /**
         * Unit code representing centimeters.
         */
        CM("cm"),
        /**
         * Unit code representing inches.
         */
        INCH("in"),
        /**
         * Unit code representing in percentage of the containing element
         * defined by terminal.
         */
        PERCENTAGE("%");

        private final String symbol;

        private Unit(String symbol) {
            this.symbol = symbol;
        }

        public String getSymbol() {
            return symbol;
        }

        @Override
        public String toString() {
            return symbol;
        }

        public static Unit getUnitFromSymbol(String symbol) {
            if (symbol == null) {
                return Unit.PIXELS; // Defaults to pixels
            }
            for (Unit unit : Unit.values()) {
                if (symbol.equals(unit.getSymbol())) {
                    return unit;
                }
            }
            return Unit.PIXELS; // Defaults to pixels
        }
    }

    /**
     * Gets the width of the object. Negative number implies unspecified size
     * (terminal is free to set the size).
     *
     * @return width of the object in units specified by widthUnits property.
     */
    public float getWidth();

    /**
     * Gets the height of the object. Negative number implies unspecified size
     * (terminal is free to set the size).
     *
     * @return height of the object in units specified by heightUnits property.
     */
    public float getHeight();

    /**
     * Gets the width property units.
     *
     * @return units used in width property.
     */
    public Unit getWidthUnits();

    /**
     * Gets the height property units.
     *
     * @return units used in height property.
     */
    public Unit getHeightUnits();

    /**
     * Sets the height of the component using String presentation.
     *
     * String presentation is similar to what is used in Cascading Style Sheets.
     * Size can be length or percentage of available size.
     *
     * The empty string ("") or null will unset the height and set the units to
     * pixels.
     *
     * See
     * <a href="http://www.w3.org/TR/REC-CSS2/syndata.html#value-def-length">CSS
     * specification</a> for more details.
     *
     * @param height
     *            in CSS style string representation
     */
    public void setHeight(String height);

    /**
     * Sets the width of the object. Negative number implies unspecified size
     * (terminal is free to set the size).
     *
     * @param width
     *            the width of the object.
     * @param unit
     *            the unit used for the width.
     */
    public void setWidth(float width, Unit unit);

    /**
     * Sets the height of the object. Negative number implies unspecified size
     * (terminal is free to set the size).
     *
     * @param height
     *            the height of the object.
     * @param unit
     *            the unit used for the width.
     */
    public void setHeight(float height, Unit unit);

    /**
     * Sets the width of the component using String presentation.
     *
     * String presentation is similar to what is used in Cascading Style Sheets.
     * Size can be length or percentage of available size.
     *
     * The empty string ("") or null will unset the width and set the units to
     * pixels.
     *
     * See
     * <a href="http://www.w3.org/TR/REC-CSS2/syndata.html#value-def-length">CSS
     * specification</a> for more details.
     *
     * @param width
     *            in CSS style string representation, null or empty string to
     *            reset
     */
    public void setWidth(String width);

    /**
     * Sets the size to 100% x 100%.
     */
    public void setSizeFull();

    /**
     * Clears any size settings.
     */
    public void setSizeUndefined();

    /**
     * Clears any defined width.
     *
     * @since 7.3
     */
    public void setWidthUndefined();

    /**
     * Clears any defined height.
     *
     * @since 7.3
     */
    public void setHeightUndefined();

}
