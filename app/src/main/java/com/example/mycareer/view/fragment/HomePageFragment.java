package com.example.mycareer.view.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.mycareer.R;
import com.example.mycareer.base.BaseFragment;
import com.example.mycareer.presenter.HomePageFragmentPresenter;
import com.example.mycareer.presenter.HomePageFragmentPresenterImpl;
import com.example.mycareer.utils.MyMarkerView;
import com.example.mycareer.view.HomePageFragmentView;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HomePageFragment extends BaseFragment implements HomePageFragmentView {
    private static final String TAG = HomePageFragment.class.getSimpleName();
    private HomePageFragmentPresenter homePageFragmentPresenter;

    private TextView textView_avgScore;
    private TextView textView_examPassed;

    private ProgressBar simpleProgressBarMin;
    private ProgressBar simpleProgressBarMax;
    private TextView textView_gradeProjectionMin;
    private TextView textView_gradeProjectionMax;

    private PieChart piechart_credits;
    private BarChart barchart_grade;
    private LineChart linechart_avg;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_homepage, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Homepage");

        initUI();

        homePageFragmentPresenter = new HomePageFragmentPresenterImpl();
        homePageFragmentPresenter.attachView(this);

        homePageFragmentPresenter.initCurrentAverage();
        homePageFragmentPresenter.initPassedExams();

        homePageFragmentPresenter.initMinDegree();
        homePageFragmentPresenter.initMaxDegree();

        homePageFragmentPresenter.initCfuDone();
        homePageFragmentPresenter.initGradeCount();
        homePageFragmentPresenter.initTimeLineAvg();

    }

    private void initUI(){
        textView_avgScore = getView().findViewById(R.id.textView_avgScore);
        textView_examPassed = getView().findViewById(R.id.textView_examPassed);
        simpleProgressBarMin = getView().findViewById(R.id.simpleProgressBarMin);
        simpleProgressBarMax = getView().findViewById(R.id.simpleProgressBarMax);
        textView_gradeProjectionMin = getView().findViewById(R.id.textView_gradeProjectionMin);
        textView_gradeProjectionMax = getView().findViewById(R.id.textView_gradeProjectionMax);
        piechart_credits = getView().findViewById(R.id.barchart_credits);
        barchart_grade = getView().findViewById(R.id.barchart_grade);
        linechart_avg = getView().findViewById(R.id.linechart_avg);
    }

    @Nullable
    @Override
    public Context getContext() {
        return getView().getContext();
    }

    @Override
    public void refreshData() {
        homePageFragmentPresenter.initCurrentAverage();
        homePageFragmentPresenter.initPassedExams();

        homePageFragmentPresenter.initMinDegree();
        homePageFragmentPresenter.initMaxDegree();

        homePageFragmentPresenter.initCfuDone();
        homePageFragmentPresenter.initGradeCount();
        homePageFragmentPresenter.initTimeLineAvg();
    }

    @Override
    public void setCurrentAverage(Double avg) {
        textView_avgScore.setText(avg.toString());
    }

    @Override
    public void setPassedExams(Integer number) {
        textView_examPassed.setText(number.toString());
    }

    @Override
    public void databaseError(String error) {
        Log.w(TAG, "Firebase Database Error: " + error);
        //Utils.showMessage(getContext(), "Firebase Database Error ! " + error);
    }

    @Override
    public void setMinDegree(Integer number) {
        simpleProgressBarMin.setProgress(number);
        textView_gradeProjectionMin.setText(String.format(Locale.getDefault(),"%d/110", number));
    }

    @Override
    public void setMaxDegree(Integer number) {
        simpleProgressBarMax.setProgress(number);
        textView_gradeProjectionMax.setText(String.format(Locale.getDefault(), "%d/110", number));
    }

    @Override
    public void setCfuDone(Float totalCfu, Float totalCfuDone) {
        float[] yData = {totalCfuDone, totalCfu-totalCfuDone};
        String[] xData = {"", ""};
        String centerText = String.format(Locale.getDefault(),"%.2f%%\n%d/%d\n%s",
                (totalCfuDone/totalCfu*100),
                Math.round(totalCfuDone),
                Math.round(totalCfu),
                "Earned Credits");

        piechart_credits.setHoleRadius(80f);
        piechart_credits.setTransparentCircleAlpha(0);
        piechart_credits.setCenterText(centerText);
        piechart_credits.setCenterTextSize(14);
        piechart_credits.setDescription(null);
        //piechart_credits.setCenterTextRadiusPercent((totalCfuDone/totalCfu*100f));

        ArrayList<PieEntry> yEntrys = new ArrayList<>();
        ArrayList<String> xEntrys = new ArrayList<>();
        for(int i = 0; i < yData.length; i++)
            yEntrys.add(new PieEntry(yData[i] , i));

        for(int i = 1; i < xData.length; i++)
            xEntrys.add(xData[i]);

        //create the data set
        PieDataSet pieDataSet = new PieDataSet(yEntrys, "");

        //add colors to dataset
        ArrayList<Integer> colors = new ArrayList<>();

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);
        pieDataSet.setColors(colors);

        //create pie data object
        PieData pieData = new PieData(pieDataSet);
        pieData.setValueTextSize(0f);

        piechart_credits.getLegend().setEnabled(false);
        piechart_credits.setData(pieData);
        piechart_credits.highlightValues(null);
        piechart_credits.invalidate();
        piechart_credits.animateXY(2000, 2000);
    }

    @Override
    public void setBarChartGrades(int[] gradesCount) {
        int[] yData = gradesCount;
        ArrayList<BarEntry> yEntrys = new ArrayList<>();

        for(int i = 0; i < yData.length; i++)
            if(yData[i] > 0)
                yEntrys.add(new BarEntry(i+18, yData[i]));

        BarDataSet bardataset = new BarDataSet(yEntrys, "Grade Frequency");

        //add colors to dataset
        ArrayList<Integer> colors = new ArrayList<>();

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);
        bardataset.setColors(colors);

        //create bar data object
        BarData barData = new BarData(bardataset);
        barData.setValueTextSize(0f);
        barData.setBarWidth(0.9f); // set custom bar width

        YAxis leftAxis = barchart_grade.getAxisLeft();
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setDrawGridLines(true);
        leftAxis.setGranularityEnabled(true);
        leftAxis.setGranularity(1f);

        YAxis rightAxis = barchart_grade.getAxisRight();
        rightAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        rightAxis.setDrawGridLines(true);
        rightAxis.setGranularityEnabled(true);
        rightAxis.setGranularity(1f);

        XAxis xAxis = barchart_grade.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.TOP);
        xAxis.setGranularity(1f);

        if(getView() != null) {
            MyMarkerView mv = new MyMarkerView(getContext());

            // Set the marker to the chart
            mv.setChartView(barchart_grade);
            barchart_grade.setMarker(mv);
        }

        barchart_grade.setFitBars(true); // make the x-axis fit exactly all bars
        barchart_grade.setDescription(null);
        barchart_grade.getLegend().setEnabled(false);
        barchart_grade.setData(barData);
        barchart_grade.setFitBars(true); // make the x-axis fit exactly all bars
        barchart_grade.highlightValues(null);
        barchart_grade.invalidate();
        barchart_grade.animateXY(2000, 2000);
    }

    @Override
    public void setLineChartAvg(float[] avgList, List<Long> dateList) {
        float[] yData = avgList;
        List<Long> dates = dateList;

        ArrayList<Entry> yEntrys = new ArrayList<>();

        for(int i = 0; i < yData.length; i++)
            yEntrys.add(new Entry(dates.get(i), yData[i]));

        LineDataSet set1 = new LineDataSet(yEntrys, "Average Trend");
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);
        //add colors to dataset
        ArrayList<Integer> colors = new ArrayList<>();
        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);
        set1.setColors(Color.rgb(42, 109, 130));
        set1.setLineWidth(1.5f);
        set1.setDrawCircles(false);
        set1.setDrawValues(false);
        set1.setFillAlpha(65);
        set1.setFillColor(Color.BLACK);
        set1.setDrawCircleHole(false);
        set1.setMode(LineDataSet.Mode.LINEAR);


        // create a data object with the data sets
        LineData data = new LineData(set1);
        data.setValueTextColor(Color.BLACK);
        data.setValueTextSize(9f);

        XAxis xAxis = linechart_avg.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.TOP);
        xAxis.setTextSize(10f);
        xAxis.setCenterAxisLabels(true);
        xAxis.setGranularity(1f * 24f); // one hour
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            private final SimpleDateFormat mFormat = new SimpleDateFormat("MMM-yy", Locale.ENGLISH);
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                long millis = (long) value;
                return mFormat.format(new Date(millis));
            }
        });

        YAxis leftAxis = linechart_avg.getAxisLeft();
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setDrawGridLines(true);
        leftAxis.setYOffset(-9f);

        YAxis rightAxis = linechart_avg.getAxisRight();
        rightAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        rightAxis.setDrawGridLines(true);
        rightAxis.setYOffset(-9f);

        if(getView() != null) {
            // create marker to display box when values are selected
            MyMarkerView mv = new MyMarkerView(getContext());

            // Set the marker to the chart
            mv.setChartView(linechart_avg);
            linechart_avg.setMarker(mv);
        }

        // set data
        linechart_avg.getLegend().setEnabled(false);
        linechart_avg.setDescription(null);
        linechart_avg.setData(data);

        List<ILineDataSet> sets = linechart_avg.getData().getDataSets();

        for (ILineDataSet iSet : sets) {

            LineDataSet set = (LineDataSet) iSet;
            if (set.isDrawCirclesEnabled())
                set.setDrawCircles(false);
            else
                set.setDrawCircles(true);
        }
        linechart_avg.setTouchEnabled(true);
        linechart_avg.animateXY(2000, 2000);
    }
}
