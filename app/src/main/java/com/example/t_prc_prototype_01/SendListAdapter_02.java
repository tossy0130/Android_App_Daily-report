package com.example.t_prc_prototype_01;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class SendListAdapter_02 extends ArrayAdapter<Send_Item> {

    private LayoutInflater layoutInflater;

    private Send_Item data;

    // ビューホルダー
    static class ViewHolder {
        TextView item_01;
        TextView item_02;
        TextView item_03;
        TextView item_04;
        TextView item_05;
    }

    // コンストラクタ
    public SendListAdapter_02(Context context, int textViewResourceId, List<Send_Item> object) {
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
    public View getView(final int position, View convertView, final ViewGroup parent) {

        // 指定行のデータを取得
        data = (Send_Item) getItem(position);

        ViewHolder holder;

        // null の　場合のみ　再作成
        if (null == convertView) {
            convertView = layoutInflater.inflate(R.layout.row_listview_02, null);
        }

        //----------------------- 行のデータを項目へ設定 ---------------------

        holder = new ViewHolder();

        //------ 作業番号
        holder.item_01 = (TextView) convertView.findViewById(R.id.list_item_001);
        //------ 商品名
        holder.item_02 = (TextView) convertView.findViewById(R.id.list_item_002);
        //------ 作業時間
        holder.item_03 = (TextView) convertView.findViewById(R.id.list_item_003);
        //------ 予定数量
        holder.item_04 = (TextView) convertView.findViewById(R.id.list_item_004);
        //------ 総生産数
        holder.item_05 = (TextView) convertView.findViewById(R.id.list_item_005);


        //--------------------------------------------------- ゲッター セット

        //------ 作業番号
        String tmp = String.valueOf(data.getSend_id());
        holder.item_01.setText("00-" + tmp);
        //------ 商品名
        holder.item_02.setText(String.valueOf(data.getHinmoku_name()));
        //------ 作業時間
        holder.item_03.setText(String.valueOf(data.getSagyou_time()));
        //------ 予定数量
        holder.item_04.setText(String.valueOf(data.getYotei_num()));
        //------- 総生産数
        holder.item_05.setText(String.valueOf(data.getSouseisan_num()));

        return convertView;

    }

}
