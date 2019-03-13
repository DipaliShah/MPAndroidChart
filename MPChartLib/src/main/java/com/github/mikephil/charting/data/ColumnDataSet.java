package com.github.mikephil.charting.data;

import android.graphics.Paint;

import com.github.mikephil.charting.interfaces.datasets.IColumnDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * DataSet for the CandleStickChart.
 *
 * @author Philipp Jahoda
 */
public class ColumnDataSet extends LineScatterCandleRadarDataSet<CandleEntry> implements IColumnDataSet {

    /**
     * paint style when open < close
     * increasing candlesticks are traditionally hollow
     */
    private Paint.Style mColumnPaintStyle = Paint.Style.FILL;
    /**
     * paint style when open > close
     * descreasing candlesticks are traditionally filled
     */
    private Paint.Style mBreathingPaintStyle = Paint.Style.STROKE;

    /**
     * color for open == close
     */
    private int mBreathingColor = ColorTemplate.COLOR_SKIP;
    /**
     * color for open < close
     */
    private int mColumnColor = ColorTemplate.COLOR_SKIP;
    /**
     * shadow line color, set -1 for backward compatibility and uses default
     * color
     */
    private int mShadowColor = ColorTemplate.COLOR_SKIP;
    private List<Entry> mBreathingEntries;
    /**
     * the width of the shadow of the candle
     */
    private float mShadowWidth = 3f;
    /**
     * should the candle bars show?
     * when false, only "ticks" will show
     * <p/>
     * - default: true
     */
    private boolean mShowCandleBar = true;
    /**
     * the space between the candle entries, default 0.1f (10%)
     */
    private float mBarSpace = 0.1f;
    /**
     * use candle color for the shadow
     */
    private boolean mShadowColorSameAsCandle = false;

    public ColumnDataSet(List<CandleEntry> yVals, String label) {
        super(yVals, label);
    }

    @Override
    public DataSet<CandleEntry> copy() {
        List<CandleEntry> entries = new ArrayList<>();
        for (int i = 0; i < mValues.size(); i++) {
            entries.add(mValues.get(i).copy());
        }
        ColumnDataSet copied = new ColumnDataSet(entries, getLabel());
        copy(copied);
        return copied;
    }

    protected void copy(ColumnDataSet candleDataSet) {
        super.copy(candleDataSet);
        candleDataSet.mShadowWidth = mShadowWidth;
        candleDataSet.mShowCandleBar = mShowCandleBar;
        candleDataSet.mBarSpace = mBarSpace;
        candleDataSet.mShadowColorSameAsCandle = mShadowColorSameAsCandle;
        candleDataSet.mHighLightColor = mHighLightColor;
        candleDataSet.mColumnColor = mColumnColor;
        candleDataSet.mBreathingColor = mBreathingColor;
        candleDataSet.mBreathingPaintStyle = mBreathingPaintStyle;
        candleDataSet.mColumnPaintStyle = mColumnPaintStyle;
        candleDataSet.mBreathingEntries = mBreathingEntries;
        candleDataSet.mShadowColor = mShadowColor;
    }

    @Override
    protected void calcMinMax(CandleEntry e) {

        if (e.getLow() < mYMin)
            mYMin = e.getLow();

        if (e.getHigh() > mYMax)
            mYMax = e.getHigh();

        calcMinMaxX(e);
    }

    @Override
    protected void calcMinMaxY(CandleEntry e) {

        if (e.getHigh() < mYMin)
            mYMin = e.getHigh();

        if (e.getHigh() > mYMax)
            mYMax = e.getHigh();

        if (e.getLow() < mYMin)
            mYMin = e.getLow();

        if (e.getLow() > mYMax)
            mYMax = e.getLow();
    }

    @Override
    public float getBarSpace() {
        return mBarSpace;
    }

    /**
     *
     * @param space Sets the space that is left out on the left and right side of each
     *      * candle, default 0.1f (10%), max 0.45f, min 0f
     */
    public void setBarSpace(float space) {
        if (space < 0f)
            space = 0f;
        mBarSpace = space;
    }

    @Override
    public float getShadowWidth() {
        return mShadowWidth;
    }

    /**
     *
     * @param width Sets the width of the candle-shadow-line in pixels. Default 3f.
     */
    public void setShadowWidth(float width) {
        mShadowWidth = Utils.convertDpToPixel(width);
    }

    @Override
    public boolean getShowCandleBar() {
        return mShowCandleBar;
    }

    /**
     * Sets whether the candle bars should show?
     *
     * @param showCandleBar
     */
    public void setShowCandleBar(boolean showCandleBar) {
        mShowCandleBar = showCandleBar;
    }

    /**
     * BELOW THIS COLOR HANDLING
     */


    @Override
    public int getShadowColor() {
        return mShadowColor;
    }

    /**
     * Sets shadow color for all entries
     *
     * @param shadowColor
     */
    public void setShadowColor(int shadowColor) {
        this.mShadowColor = shadowColor;
    }

    @Override
    public int getColumnColor() {
        return mColumnColor;
    }

    public void setColumnColor(int mColumnColor) {
        this.mColumnColor = mColumnColor;
    }

    @Override
    public int getBreathingColor() {
        return mBreathingColor;
    }

    /**
     * It is necessary to implement ColorsList class that will encapsulate
     * colors list functionality, because It's wrong to copy paste setColor,
     * addColor, ... resetColors for each time when we want to add a coloring
     * options for one of objects
     *
     * @author Mesrop
     */

    public void setBreathingColor(int mBreathingColor) {
        this.mBreathingColor = mBreathingColor;
    }

    @Override
    public Paint.Style getColumnPaintStyle() {
        return mColumnPaintStyle;
    }

    public void setColumnPaintStyle(Paint.Style mColumnPaintStyle) {
        this.mColumnPaintStyle = mColumnPaintStyle;
    }

    @Override
    public Paint.Style getBreathingPaintStyle() {
        return mBreathingPaintStyle;
    }

    public void setBreathingPaintStyle(Paint.Style mBreathingPaintStyle) {
        this.mBreathingPaintStyle = mBreathingPaintStyle;
    }

    @Override
    public boolean getShadowColorSameAsCandle() {
        return mShadowColorSameAsCandle;
    }

    /**
     * Sets shadow color to be the same color as the candle color
     *
     * @param shadowColorSameAsCandle
     */
    public void setShadowColorSameAsCandle(boolean shadowColorSameAsCandle) {
        this.mShadowColorSameAsCandle = shadowColorSameAsCandle;
    }

    @Override
    public List<Entry> getBreathingEntries() {
        return mBreathingEntries;
    }

    public void setBreathingEntries(List<Entry> mBreathingEntries) {
        this.mBreathingEntries = mBreathingEntries;
    }
}
