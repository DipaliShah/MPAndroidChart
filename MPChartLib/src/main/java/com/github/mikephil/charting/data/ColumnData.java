package com.github.mikephil.charting.data;

import com.github.mikephil.charting.interfaces.datasets.IColumnDataSet;

import java.util.List;

/**
 * Created by Dipali Shah on 13/3/19
 */
public class ColumnData extends BarLineScatterCandleBubbleData<IColumnDataSet> {

    public ColumnData() {
        super();
    }

    public ColumnData(List<IColumnDataSet> dataSets) {
        super(dataSets);
    }

    public ColumnData(IColumnDataSet... dataSets) {
        super(dataSets);
    }
}

