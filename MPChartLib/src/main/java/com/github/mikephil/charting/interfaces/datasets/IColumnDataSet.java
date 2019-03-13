package com.github.mikephil.charting.interfaces.datasets;

import android.graphics.Paint;

import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;

import java.util.List;

/**
 * Created by Dipali Shah on 13/3/19
 */
public interface IColumnDataSet extends ILineScatterCandleRadarDataSet<CandleEntry> {

    /**
     * Returns the space that is left out on the left and right side of each
     * candle.
     *
     * @return
     */
    float getBarSpace();

    /**
     * Returns whether the candle bars should show?
     * When false, only "ticks" will show
     * <p>
     * - default: true
     *
     * @return
     */
    boolean getShowCandleBar();

    /**
     * Returns the width of the candle-shadow-line in pixels.
     *
     * @return
     */
    float getShadowWidth();

    /**
     * Returns shadow color for all entries
     *
     * @return
     */
    int getShadowColor();

    /**
     * Returns the neutral color (for open == close)
     *
     * @return
     */
    int getColumnColor();

    /**
     * Returns the increasing color (for open < close).
     *
     * @return
     */
    int getBreathingColor();

    /**
     * Returns paint style when open < close
     *
     * @return
     */
    Paint.Style getColumnPaintStyle();

    /**
     * Returns paint style when open > close
     *
     * @return
     */
    Paint.Style getBreathingPaintStyle();

    /**
     * Is the shadow color same as the candle color?
     *
     * @return
     */
    boolean getShadowColorSameAsCandle();

    List<Entry> getBreathingEntries();

}
