package com.example.t_prc_prototype_01;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.google.zxing.common.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class SendListAdapter extends ArrayAdapter<Send_Item> {

    private LayoutInflater layoutInflater;

    private Send_Item data;

    // ビューホルダー
    static class ViewHolder {
        TextView item_01;
        TextView item_02;
        TextView item_03;
        TextView item_04;
        TextView item_05;
        BarChart CObj;

    }



    // コンストラクタ
    public SendListAdapter(Context context, int textViewResourceId, List<Send_Item> object) {
        super(context, textViewResourceId, object);
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

 /*
  * positionごとのViewのタイプを返す
  * (non-Javadoc)
  * @see android.widget.BaseAdapter#getItemViewType(int)
  */

 /*
     @Override
     public int getItemViewType(int position) {
         // position ごとに適切な値を取得
        return listArray.get(position).getRowType();


     }

  */


     @Override
     public View getView(final int position, View convertView,final ViewGroup parent) {

        // 指定行のデータを取得
         data = (Send_Item) getItem(position);

         ViewHolder holder;

        // null の　場合のみ　再作成
         if(null == convertView) {
             convertView = layoutInflater.inflate(R.layout.row_listview, null);
         }

         //----------------------- 行のデータを項目へ設定 ---------------------

         holder = new ViewHolder();

         //------ 商品名
         holder.item_01 = (TextView) convertView.findViewById(R.id.list_left_item_01_num);
         //------ 最終工程
         holder.item_02 = (TextView) convertView.findViewById(R.id.list_left_item_02_num);
         //------ 完成数量
         holder.item_03 = (TextView) convertView.findViewById(R.id.list_left_item_03_num);
         //------ 目標数量
         holder.item_04 = (TextView) convertView.findViewById(R.id.list_left_item_04_num);
         //------ 作業時間
         holder.item_05 = (TextView) convertView.findViewById(R.id.right_time_text);

         //------ グラフ　チャート
         holder.CObj = (BarChart) convertView.findViewById(R.id.chart);

         //--------------------------------------------------- ゲッター セット

         //------ 商品名
         holder.item_01.setText(String.valueOf(data.getHinmoku_name()));
         //------ 最終工程
         holder.item_02.setText(String.valueOf(data.getSaishyuu_k_check()));
         //------ 完成数量 （良品数）
         holder.item_03.setText(String.valueOf(data.getRyouhinn_num()));
         //------ 予定数量
         holder.item_04.setText(String.valueOf(data.getYotei_num()));
         //------- 作業時間
         holder.item_05.setText(String.valueOf(data.getSagyou_time()));


         //------ グラフ　表示-----------------------------------------------

         //-------グラフ　パラメーター　設定 -------------------------------
      //   BarChart CObj = (BarChart) convertView.findViewById(R.id.chart);
         holder.CObj.getAxisLeft().setAxisMaxValue(10000f);
         holder.CObj.getAxisLeft().setAxisMinValue(1000f);
         holder.CObj.getAxisLeft().setStartAtZero(false);

         // ラベルの文字の大きさ変更
         holder.CObj.getXAxis().setTextSize(5f);

         /*------------------ グラフの値設定 -----------------*/
         // Y軸（左）
         YAxis left = holder.CObj.getAxisLeft();
         // 最小値
         left.setAxisMinimum(0);
         //---------------------- 最大値

         // ------ エディットテキスト 03 入力値を設定

         // 作業時間　　Integer パース
         if(holder.item_05.length() != 0) {
             String tmp = holder.item_05.getText().toString();

             // null だった時の　エラー処理
             if(tmp != null) {
                 System.out.println(tmp);

                 int graf_max_nul = 0;
                 left.setAxisMaximum(graf_max_nul);

             } else {

                 int graf_max_in = Integer.parseInt(holder.item_05.getText().toString());
                 left.setAxisMaximum(graf_max_in);

             }

         } else {
             int graf_max_nul = 0;
             left.setAxisMaximum(graf_max_nul);
         }

      // left.setAxisMaximum(100);
         //
         left.setLabelCount(8);
         //
         left.setDrawTopYLabelEntry(true);

         // 整数表示に
         left.setValueFormatter(new IAxisValueFormatter() {
             @Override
             public String getFormattedValue(float value, AxisBase axis) {
                 return "" + (int)value + " " +
                         "分";
             }
         }); // ----- setValueFormatter END

         //------------ Y軸(右) ------------
         YAxis right = holder.CObj.getAxisRight();
         right.setDrawLabels(false);
         right.setDrawGridLines(false);
         right.setDrawZeroLine(true);
         right.setDrawTopYLabelEntry(true);

         //----------- X 軸
         XAxis xAxis = holder.CObj.getXAxis();

         /*------------------ データ　ラベル 設定 -----------------*/
         // X軸に表示する Label の　リスト　最初の　原点は　""
      //   final String[] labels = {"", "テスト01"};
         final String[] labels = {"テスト01"};
         xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));

         // ボトム
         XAxis bottomAxis = holder.CObj.getXAxis();
         bottomAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
         bottomAxis.setDrawLabels(true);
         // グリッドの線　（網、網）
         bottomAxis.setDrawGridLines(false);
         bottomAxis.setDrawAxisLine(true);


         // グラフ上の表示 設定
         holder.CObj.setDrawValueAboveBar(true);
         holder.CObj.getDescription().setEnabled(false);
         holder.CObj.setClickable(false);

         // 凡例
         holder.CObj.getLegend().setEnabled(true);
         holder.CObj.setScaleEnabled(false);

         // アニメーション
         holder.CObj.animateY(1200, Easing.EasingOption.Linear);
         holder.CObj.animateX(1200,Easing.EasingOption.Linear);

         //-------- 設定追加 -------
         holder.CObj.setDoubleTapToZoomEnabled(true);
         holder.CObj.setPinchZoom(true);

         //-----------------------------------------------------------------
         //-------グラフ　パラメーター　設定 END -------------------------------
         //-----------------------------------------------------------------

         List<BarEntry> entry = new ArrayList<>();

         //************* グラフに表示する　値を挿入する。
         if(holder.item_03.length() != 0) {

             //------ 完成数量 （良品数）
             int fraf_num_in = Integer.parseInt(holder.item_03.getText().toString());
             entry.add(new BarEntry(1, fraf_num_in));
         } else {
             int fraf_num_nul = 0;
             entry.add(new BarEntry(1,fraf_num_nul));
         }
         // ------ エディットテキスト 02 入力値を設定

         List<IBarDataSet> bars = new ArrayList<>();
         // 表示させるデータ
         BarDataSet dataSet = new BarDataSet(entry, "作業時間");
         dataSet.setColor(Color.BLUE);
         dataSet.setValueTextSize(10);

         bars.add(dataSet);

         BarData data_b = new BarData(bars);
         holder.CObj.setData(data_b);

         return convertView;
    }

}
