package com.gx.morgan.customchart;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.gx.morgan.chartlib.utils.ViewUtil;
import com.gx.morgan.chartlib.view.BaseCoordinateView;
import com.gx.morgan.chartlib.view.LineView;
import com.gx.morgan.chartlib.view.StatisticsBar;
import com.gx.morgan.customchart.view.SlidingTab;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();
//        initStatisticBar();
//        initLineView();
        initViews();
    }

    private void initViews() {

        ViewPager viewPager=findViewById(R.id.viewPager);

        LayoutInflater layoutInflater = getLayoutInflater();
        StatisticsBar statisticsBar= (StatisticsBar) layoutInflater.inflate(R.layout.layout_statisticbar,null);
        setStatisticBarData(statisticsBar);
        LineView lineView= (LineView) layoutInflater.inflate(R.layout.layout_lineview,null);
        setLineViewData(lineView);


        List<View> views=new ArrayList<>(2);
        views.add(statisticsBar);
        views.add(lineView);

        List<String> titles=new ArrayList<>(2);
        titles.add("柱状图");
        titles.add("折线图");

        ViewPagerAdapter pagerAdapter=new ViewPagerAdapter(views,titles);
        viewPager.setAdapter(pagerAdapter);

        SlidingTab tabs=findViewById(R.id.tabs);
        tabs.setViewPager(viewPager);
        tabs.setShouldExpand(true);
        tabs.setTextSize((int) ViewUtil.dp2px(this,16));
        tabs.setTextColorResource(android.R.color.black);
        tabs.setSelectedTextColorResource(R.color.colorPrimary);
    }
    private static class ViewPagerAdapter extends PagerAdapter {

        private List<View> views;
        private List<String> titles;
        public  ViewPagerAdapter( List<View> views,List<String> titles){
            this.views=views;
            this.titles=titles;
        }

        @Override
        public int getCount() {
            return views.size();
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            View view = views.get(position);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            View view = views.get(position);
            container.removeView(view);
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view==object;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }


    private void setLineViewData(LineView lineView) {
        //x轴数据
        int xDataSize = 8;
        List<Integer> xCoodinateDatas = new ArrayList<>(xDataSize);
        for (int i = 0; i < xDataSize; i++) {
            xCoodinateDatas.add(i + 1);
        }


        //y轴数据
        int yDataSize = 7;
        List<Integer> yCoodinateDatas = new ArrayList<>(yDataSize);
        for (int i = 0; i < yDataSize; i++) {
            yCoodinateDatas.add(8 * i);
        }


        //柱状图数据
        int contentDataSize = 8;
        List<StatisticsBar.ContentData> contentDatas = new ArrayList<>(contentDataSize);
        int[] datas = new int[]{24, 48, 40, 8, 0, 16, 32, 48};
        for (int i = 0; i < contentDataSize; i++) {
            BaseCoordinateView.ContentData contentData = new BaseCoordinateView.ContentData(i + 1, datas[i]);
            contentDatas.add(contentData);
        }


        lineView.setFullData(xCoodinateDatas, yCoodinateDatas, contentDatas);
    }

    private void setStatisticBarData(StatisticsBar statisticsBar) {
        //x轴数据
        int xDataSize = 8;
        List<Integer> xCoodinateDatas = new ArrayList<>(xDataSize);
        for (int i = 0; i < xDataSize; i++) {
            xCoodinateDatas.add(i + 1);
        }


        //y轴数据
        int yDataSize = 7;
        List<Integer> yCoodinateDatas = new ArrayList<>(yDataSize);
        for (int i = 0; i < yDataSize; i++) {
            yCoodinateDatas.add(8 * i);
        }


        //柱状图数据
        int contentDataSize = 8;
        List<StatisticsBar.ContentData> contentDatas = new ArrayList<>(contentDataSize);
        int[] datas = new int[]{24, 48, 40, 8, 0, 16, 32, 48};
        for (int i = 0; i < contentDataSize; i++) {
            BaseCoordinateView.ContentData contentData = new BaseCoordinateView.ContentData(i + 1, datas[i]);
            contentDatas.add(contentData);
        }

        statisticsBar.setFullData(xCoodinateDatas, yCoodinateDatas, contentDatas);
    }
//    private void initLineView() {
//        LineView lineView = findViewById(R.id.lineView);
//        //x轴数据
//        int xDataSize = 8;
//        List<Integer> xCoodinateDatas = new ArrayList<>(xDataSize);
//        for (int i = 0; i < xDataSize; i++) {
//            xCoodinateDatas.add(i + 1);
//        }
//
//
//        //y轴数据
//        int yDataSize = 7;
//        List<Integer> yCoodinateDatas = new ArrayList<>(yDataSize);
//        for (int i = 0; i < yDataSize; i++) {
//            yCoodinateDatas.add(8 * i);
//        }
//
//
//        //柱状图数据
//        int contentDataSize = 8;
//        List<StatisticsBar.ContentData> contentDatas = new ArrayList<>(contentDataSize);
//        int[] datas = new int[]{24, 48, 40, 8, 0, 16, 32, 48};
//        for (int i = 0; i < contentDataSize; i++) {
//            BaseCoordinateView.ContentData contentData = new BaseCoordinateView.ContentData(i + 1, datas[i]);
//            contentDatas.add(contentData);
//        }
//
//
//        lineView.setFullData(xCoodinateDatas, yCoodinateDatas, contentDatas);
//    }
//
//    private void initStatisticBar() {
//        StatisticsBar statisticsBar = findViewById(R.id.statisticsBar);
//        //x轴数据
//        int xDataSize = 8;
//        List<Integer> xCoodinateDatas = new ArrayList<>(xDataSize);
//        for (int i = 0; i < xDataSize; i++) {
//            xCoodinateDatas.add(i + 1);
//        }
//
//
//        //y轴数据
//        int yDataSize = 7;
//        List<Integer> yCoodinateDatas = new ArrayList<>(yDataSize);
//        for (int i = 0; i < yDataSize; i++) {
//            yCoodinateDatas.add(8 * i);
//        }
//
//
//        //柱状图数据
//        int contentDataSize = 8;
//        List<StatisticsBar.ContentData> contentDatas = new ArrayList<>(contentDataSize);
//        int[] datas = new int[]{24, 48, 40, 8, 0, 16, 32, 48};
//        for (int i = 0; i < contentDataSize; i++) {
//            BaseCoordinateView.ContentData contentData = new BaseCoordinateView.ContentData(i + 1, datas[i]);
//            contentDatas.add(contentData);
//        }
//
//        statisticsBar.setFullData(xCoodinateDatas, yCoodinateDatas, contentDatas);
//    }
}
