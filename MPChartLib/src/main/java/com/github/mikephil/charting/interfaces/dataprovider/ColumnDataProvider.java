package com.github.mikephil.charting.interfaces.dataprovider;

import com.github.mikephil.charting.data.ColumnData;

/**
 * Created by Dipali Shah on 13/3/19
 */
public interface ColumnDataProvider extends BarLineScatterCandleBubbleDataProvider {

    ColumnData getColumnData();
}
