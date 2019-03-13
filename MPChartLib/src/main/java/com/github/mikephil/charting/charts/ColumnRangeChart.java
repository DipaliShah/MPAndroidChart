package com.github.mikephil.charting.charts;

import android.content.Context;
import android.util.AttributeSet;

import com.github.mikephil.charting.data.ColumnData;
import com.github.mikephil.charting.interfaces.dataprovider.ColumnDataProvider;
import com.github.mikephil.charting.renderer.ColumnRangeChartRenderer;

/**
 * Financial chart type that draws candle-sticks (OHCL chart).
 *
 * @author Philipp Jahoda
 */
public class ColumnRangeChart extends BarLineChartBase<ColumnData> implements ColumnDataProvider {

    public ColumnRangeChart(Context context) {
        super(context);
    }

    public ColumnRangeChart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ColumnRangeChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void init() {
        super.init();

        mRenderer = new ColumnRangeChartRenderer(this, mAnimator, mViewPortHandler);

        getXAxis().setSpaceMin(0.5f);
        getXAxis().setSpaceMax(0.5f);
    }

    @Override
    public ColumnData getColumnData() {
        return mData;
    }
}
