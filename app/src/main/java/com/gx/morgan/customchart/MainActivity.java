package com.gx.morgan.customchart;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.gx.morgan.chartlib.view.BaseCoordinateView;
import com.gx.morgan.chartlib.view.LineView;
import com.gx.morgan.chartlib.view.StatisticsBar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        initStatisticBar();
        initLineView();
    }

    private void initLineView() {
        LineView lineView = findViewById(R.id.lineView);
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

    private void initStatisticBar() {
        StatisticsBar statisticsBar = findViewById(R.id.statisticsBar);
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
}
