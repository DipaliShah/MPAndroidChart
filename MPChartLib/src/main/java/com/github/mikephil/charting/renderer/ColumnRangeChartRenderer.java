package com.github.mikephil.charting.renderer;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.os.Build;

import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.ColumnData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.dataprovider.ColumnDataProvider;
import com.github.mikephil.charting.interfaces.datasets.IColumnDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointD;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.List;

public class ColumnRangeChartRenderer extends LineScatterCandleRadarRenderer {

    private final float barRadius = 25F;
    protected ColumnDataProvider mChart;
    private float[] mBodyBuffers = new float[4];

    public ColumnRangeChartRenderer(ColumnDataProvider chart, ChartAnimator animator,
                                    ViewPortHandler viewPortHandler) {
        super(animator, viewPortHandler);
        mChart = chart;
    }

    @Override
    public void initBuffers() {

    }

    @Override
    public void drawData(Canvas c) {

        ColumnData candleData = mChart.getColumnData();

        for (IColumnDataSet set : candleData.getDataSets()) {

            if (set.isVisible()) {
                drawDataSet(c, set);
                drawStoppedBreathing(c, set);
            }
        }


    }

    private void drawStoppedBreathing(Canvas c, IColumnDataSet dataSet) {

        Transformer trans = mChart.getTransformer(dataSet.getAxisDependency());

        Paint breathingPaint = new Paint();
        breathingPaint.setColor(dataSet.getBreathingColor());
        breathingPaint.setStyle(dataSet.getBreathingPaintStyle());
        breathingPaint.setStrokeWidth(dataSet.getBreathingWidth());

        Path path = new Path();
        List<Entry> mBreathingEntries = dataSet.getBreathingEntries();

        for (Entry entry : mBreathingEntries) {

            MPPointD pix = trans.getPixelForValues(entry.getX(), entry.getY());

            MPPointD pixLeft = trans.getPixelForValues(entry.getX() - 0.5F, entry.getY());
            MPPointD pixRight = trans.getPixelForValues(entry.getX() + 0.5F, entry.getY());

            //Horizontal top line
            path.reset();
            path.moveTo((float) pixLeft.x, mViewPortHandler.contentTop());
            path.lineTo((float) pixRight.x, mViewPortHandler.contentTop());
            c.drawPath(path, breathingPaint);

            //Vertical line
            path.reset();
            path.moveTo((float) pix.x, mViewPortHandler.contentTop());
            path.lineTo((float) pix.x, mViewPortHandler.contentBottom());
            c.drawPath(path, breathingPaint);

            //Horizontal bottom line
            path.reset();
            path.moveTo((float) pixLeft.x, mViewPortHandler.contentBottom());
            path.lineTo((float) pixRight.x, mViewPortHandler.contentBottom());
            c.drawPath(path, breathingPaint);


        }

    }

    @SuppressWarnings("ResourceAsColor")
    protected void drawDataSet(Canvas c, IColumnDataSet dataSet) {

        Transformer trans = mChart.getTransformer(dataSet.getAxisDependency());

        float phaseY = mAnimator.getPhaseY();
        float barSpaceHalf = dataSet.getBarSpace() / 2F;
        boolean showCandleBar = dataSet.getShowCandleBar();

        mXBounds.set(mChart, dataSet);

        mRenderPaint.setStrokeWidth(dataSet.getShadowWidth());

        // draw the body
        for (int j = mXBounds.min; j <= mXBounds.range + mXBounds.min; j++) {

            // get the entry
            CandleEntry e = dataSet.getEntryForIndex(j);

            if (e == null)
                continue;

            final float xPos = e.getX();

            final float open = e.getOpen();
            final float close = e.getClose();
            final float high = e.getHigh();
            final float low = e.getLow();

            // calculate the body

            mBodyBuffers[0] = xPos - barSpaceHalf;
            mBodyBuffers[1] = close * phaseY;
            mBodyBuffers[2] = (xPos + barSpaceHalf);
            mBodyBuffers[3] = open * phaseY;

            trans.pointValuesToPixel(mBodyBuffers);

            if (dataSet.getColumnColor() == ColorTemplate.COLOR_NONE) {
                mRenderPaint.setColor(dataSet.getColor(j));
            } else {
                mRenderPaint.setColor(dataSet.getColumnColor());
            }

            mRenderPaint.setStyle(dataSet.getColumnPaintStyle());
            // draw body differently for increasing and decreasing entry
            if (open > close) { // decreasing

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    c.drawRoundRect(
                            mBodyBuffers[0], mBodyBuffers[3],
                            mBodyBuffers[2], mBodyBuffers[1], barRadius, barRadius,
                            mRenderPaint);
                } else
                    c.drawRect(
                            mBodyBuffers[0], mBodyBuffers[3],
                            mBodyBuffers[2], mBodyBuffers[1],
                            mRenderPaint);

            } else if (open < close) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    c.drawRoundRect(
                            mBodyBuffers[0], mBodyBuffers[1],
                            mBodyBuffers[2], mBodyBuffers[3], barRadius, barRadius,
                            mRenderPaint);
                } else
                    c.drawRect(
                            mBodyBuffers[0], mBodyBuffers[1],
                            mBodyBuffers[2], mBodyBuffers[3],
                            mRenderPaint);
            }

            // draw the shadows
            mRenderPaint.setStrokeWidth(dataSet.getShadowWidth());
            mRenderPaint.setColor(dataSet.getShadowColor());
            mRenderPaint.setStyle(Paint.Style.STROKE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                c.drawRoundRect(mBodyBuffers[0], mBodyBuffers[3],
                        mBodyBuffers[2], mBodyBuffers[1], barRadius, barRadius, mRenderPaint);
            } else {
                c.drawRect(mBodyBuffers[0], mBodyBuffers[3],
                        mBodyBuffers[2], mBodyBuffers[1], mRenderPaint);
            }

        }
    }

    @Override
    public void drawValues(Canvas c) {

        // if values are drawn
        if (isDrawingValuesAllowed(mChart)) {

            List<IColumnDataSet> dataSets = mChart.getColumnData().getDataSets();

            for (int i = 0; i < dataSets.size(); i++) {

                IColumnDataSet dataSet = dataSets.get(i);

                if (!shouldDrawValues(dataSet) || dataSet.getEntryCount() < 1)
                    continue;

                // apply the text-styling defined by the DataSet
                applyValueTextStyle(dataSet);

                Transformer trans = mChart.getTransformer(dataSet.getAxisDependency());

                mXBounds.set(mChart, dataSet);

                float[] positions = trans.generateTransformedValuesColumn(
                        dataSet, mAnimator.getPhaseX(), mAnimator.getPhaseY(), mXBounds.min, mXBounds.max);

                float yOffset = Utils.convertDpToPixel(5f);

                ValueFormatter formatter = dataSet.getValueFormatter();

                MPPointF iconsOffset = MPPointF.getInstance(dataSet.getIconsOffset());
                iconsOffset.x = Utils.convertDpToPixel(iconsOffset.x);
                iconsOffset.y = Utils.convertDpToPixel(iconsOffset.y);

                for (int j = 0; j < positions.length; j += 2) {

                    float x = positions[j];
                    float y = positions[j + 1];

                    if (!mViewPortHandler.isInBoundsRight(x))
                        break;

                    if (!mViewPortHandler.isInBoundsLeft(x) || !mViewPortHandler.isInBoundsY(y))
                        continue;

                    CandleEntry entry = dataSet.getEntryForIndex(j / 2 + mXBounds.min);

                    if (dataSet.isDrawValuesEnabled()) {
                        drawValue(c, formatter.getCandleLabel(entry), x, y - yOffset, dataSet.getValueTextColor(j / 2));
                    }

                    if (entry.getIcon() != null && dataSet.isDrawIconsEnabled()) {

                        Drawable icon = entry.getIcon();

                        Utils.drawImage(
                                c,
                                icon,
                                (int) (x + iconsOffset.x),
                                (int) (y + iconsOffset.y),
                                icon.getIntrinsicWidth(),
                                icon.getIntrinsicHeight());
                    }
                }

                MPPointF.recycleInstance(iconsOffset);
            }
        }
    }

    @Override
    public void drawValue(Canvas c, String valueText, float x, float y, int color) {
        mValuePaint.setColor(color);
        c.drawText(valueText, x, y, mValuePaint);
    }

    @Override
    public void drawExtras(Canvas c) {
    }

    @Override
    public void drawHighlighted(Canvas c, Highlight[] indices) {

        ColumnData candleData = mChart.getColumnData();

        for (Highlight high : indices) {

            IColumnDataSet set = candleData.getDataSetByIndex(high.getDataSetIndex());

            if (set == null || !set.isHighlightEnabled())
                continue;

            CandleEntry e = set.getEntryForXValue(high.getX(), high.getY());

            if (!isInBoundsX(e, set))
                continue;

            float lowValue = e.getLow() * mAnimator.getPhaseY();
            float highValue = e.getHigh() * mAnimator.getPhaseY();
            float y = (lowValue + highValue) / 2f;

            MPPointD pix = mChart.getTransformer(set.getAxisDependency()).getPixelForValues(e.getX(), y);

            high.setDraw((float) pix.x, (float) pix.y);

            // draw the lines
            drawHighlightLines(c, (float) pix.x, (float) pix.y, set);
        }
    }
}
