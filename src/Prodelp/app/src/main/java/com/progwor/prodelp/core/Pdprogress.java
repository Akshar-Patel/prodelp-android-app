package com.progwor.prodelp.core;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ValueFormatter;
import com.progwor.prodelp.R;
import com.progwor.prodelp.data.ProdelpContract;
import com.progwor.prodelp.ui.main.MainActivity;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

public class Pdprogress {

    //Projection columns for query
    public static final String[] COLS = {
            ProdelpContract.PdprogressPdactivityEntry._ID,
            ProdelpContract.PdprogressPdactivityEntry.RATE_COL,
            ProdelpContract.PdprogressPdactivityEntry.DATE_COL
    };

    LineChart chart;
    Cursor cursor;

    //Returns the cursor
    public Cursor getCursor(Context context) {
        Cursor cursor = context.getContentResolver()
                .query(ProdelpContract.PdprogressPdactivityEntry.CONTENT_URI, COLS, null, null,
                        null);
        cursor.moveToFirst();
        return cursor;
    }


    public View load(final Context context, LayoutInflater layoutInflater,
                     ViewGroup container) {

        final View pdprogressView = layoutInflater.inflate(R.layout.pdprogress_fragment, container,
                false);

        cursor = getCursor(context);

        /*ArrayList<String> xVals = new ArrayList<String>();
        ArrayList<Entry> pointvals = new ArrayList<Entry>();

        for (int i = 0; i < cursor.getCount(); i++) {
            xVals.add(cursor.getString(ProdelpContract.PdprogressPdactivityEntry.DATE_COL_INDEX));
            pointvals.add(new Entry((float)(
                    cursor.getInt(ProdelpContract.PdprogressPdactivityEntry.RATE_COL_INDEX)), i));
            cursor.moveToNext();
        }
        //cursor.close();
        chart = (LineChart) pdprogressView.findViewById(R.id.chart);
        chart.setNoDataTextDescription("You need to create routine and activities.");
        chart.setTouchEnabled(true);
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);

        chart.getAxisRight().setDrawAxisLine(false);
        chart.getAxisRight().setDrawLabels(false);

        YAxis yAxis = chart.getAxisLeft();
        yAxis.setTextSize(13f); // set the textsize
        yAxis.setValueFormatter(new MyValueFormatter());

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(13f);
        xAxis.setDrawAxisLine(true);
        xAxis.setAvoidFirstLastClipping(true);

        LineDataSet setComp1 = new LineDataSet(pointvals, "");
        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();

        setComp1.setColor(context.getResources().getColor(R.color.accent_dark));
        setComp1.setCircleColor(context.getResources().getColor(R.color.accent_dark));
        setComp1.setCircleColorHole(context.getResources().getColor(R.color.accent_dark));

        dataSets.add(setComp1);

        LineData data = new LineData(xVals, dataSets);
        data.setValueFormatter(new MyValueFormatter());
        data.setValueTextSize(10f);

        chart.setDescription("");
        chart.setData(data);
        chart.setVisibleXRange(8);

        Legend l = chart.getLegend();
        l.setFormSize(0);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                scroll();
            }
        }, 10);*/


         final String[] days = new String[]{"Mon", "Tue", "Wen", "Thu", "Fri", "Sat", "Sun",};

        List<AxisValue> axisValues = new ArrayList<AxisValue>();
        List<PointValue> values = new ArrayList<PointValue>();

        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++) {
            axisValues.add(new AxisValue(i).setLabel(
                    cursor.getString(ProdelpContract.PdprogressPdactivityEntry.DATE_COL_INDEX)));
            values.add(new PointValue(i,cursor.getInt(ProdelpContract.PdprogressPdactivityEntry.RATE_COL_INDEX)));
            cursor.moveToNext();
        }
        //In most cased you can call data model methods in builder-pattern-like manner.
        Line line = new Line(values).setColor(context.getResources().getColor(R.color.accent)).setCubic(true);
        line.setHasLabels(true);

        List<Line> lines = new ArrayList<Line>();
        lines.add(line);

        final LineChartData data1 = new LineChartData();
        data1.setLines(lines);

        final LineChartView chart = (LineChartView) pdprogressView.findViewById(R.id.charttest);
        chart.setInteractive(true);
             chart.setZoomType(ZoomType.HORIZONTAL_AND_VERTICAL);

             chart.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        chart.setLineChartData(data1);


        Axis axisX = new Axis();
        Axis axisY = new Axis().setHasLines(true);

        data1.setAxisXBottom(new Axis(axisValues).setHasLines(true));
        data1.getAxisXBottom().setTextColor(context.getResources().getColor(R.color.primary_text_dark));
        data1.setAxisYLeft(axisY);
        data1.getAxisYLeft().setTextColor(context.getResources().getColor(R.color.primary_text_dark));
        data1.setBaseValue(Float.NEGATIVE_INFINITY);

        final Viewport v = new Viewport(chart.getMaximumViewport());
        v.bottom = -5;
        v.top = 115;
        v.left = -0.5f;
        v.right = cursor.getCount()-0.5f;
        // You have to set max and current viewports separately.
        chart.setMaximumViewport(v);

        // I changing current viewport with animation in this case.
        v.left = cursor.getCount()-1;
        v.right = 4;
        chart.setCurrentViewportWithAnimation(v);
        chart.setMaxZoom(2f);

        Button pdactivityClearButton = (Button)pdprogressView.findViewById(R.id.pdprogress_clear_button);
        pdactivityClearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.getContentResolver().delete(ProdelpContract.PdprogressPdactivityEntry.CONTENT_URI,null,null);
               // String[] drawerItems = context.getResources().getStringArray(R.array.drawer_list);
               // ((ActionBarActivity) getActivity()).getSupportActionBar().setTitle(drawerItems[Prodelp.PDPROGRESS]);
                ((MainActivity) context).mDrawerFragment.selectItem(Prodelp.PDPROGRESS);
            };
        });

        Pdgoal pdgoal = new Pdgoal();
        Cursor cursor = pdgoal.getCursorByCompletedDate(context);

        String[] from = new String[]{ProdelpContract.PdgoalEntry.NAME_COL,
                ProdelpContract.PdgoalEntry.COMPLETED_DATE_COL};

        // create an array of the display item we want to bind our data to
        int[] to = new int[]{R.id.pdprogress_pdgoal_listview_item_name_textview,
                R.id.pdprogress_pdgoal_listview_item_date_textview};

        final SimpleCursorAdapter mAdapter = new SimpleCursorAdapter(context,
                R.layout.pdprogress_listview_item, cursor, from, to, 0);

        final ListView listView = (ListView) pdprogressView.findViewById(
                R.id.pdprogress_pdgoal_listview);
        listView.setAdapter(mAdapter);

        return pdprogressView;
    }

    public void scroll() {
        chart.moveViewToX(cursor.getCount() - 8);
    }

    public class MyValueFormatter implements ValueFormatter {

        @Override
        public String getFormattedValue(float value) {
            return "" + ((int) value) + "%";
        }
    }
}
