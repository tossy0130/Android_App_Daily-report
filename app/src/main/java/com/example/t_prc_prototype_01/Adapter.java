package com.example.t_prc_prototype_01;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {

    Context context;
    ArrayList send_num, send_file_name, send_time, send_Stats, send_count;

    Adapter(Context context,ArrayList send_num, ArrayList send_file_name, ArrayList send_time, ArrayList send_Stats,
            ArrayList send_count) {

        this.context = context;
        this.send_num = send_num;
        this.send_file_name = send_file_name;
        this.send_time = send_time;
        this.send_Stats = send_Stats;
        this.send_count = send_count;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int ViewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.row_recycle_01,parent,false);
        return new MyViewHolder(view);
    }

    //1行文データを取得して設定
    @Override
    public void onBindViewHolder(Adapter.MyViewHolder holder, int position) {

        // 送信番号
        holder.send_num_txt.setText(String.valueOf(send_num.get(position)));
        // ファイル名
        holder.send_file_name_txt.setText(String.valueOf(send_file_name.get(position)));
        // 送信時間
        holder.send_time_txt.setText(String.valueOf(send_time.get(position)));
        // 送信スタッツ
        holder.send_Stats_txt.setText(String.valueOf(send_Stats.get(position)));
        // ファイル数
        holder.send_count_txt.setText(String.valueOf(send_count.get(position)));

    }

    @Override
    public int getItemCount() {
        return send_file_name.size();
    }

    //ViewHolderはRecyclerViewのrowとの繋ぎ役
    public class MyViewHolder extends RecyclerView.ViewHolder {
        // テキストビュー
        TextView send_num_txt, send_file_name_txt, send_time_txt, send_Stats_txt, send_count_txt;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            // 送信番号
            send_num_txt = itemView.findViewById(R.id.send_num);
            // 送信ファイル 名
            send_file_name_txt = itemView.findViewById(R.id.send_file_name);
            // 送信時間
            send_time_txt = itemView.findViewById(R.id.send_time);
            // 送信スタッツ
            send_Stats_txt = itemView.findViewById(R.id.send_Stats);
            // ファイル数
            send_count_txt = itemView.findViewById(R.id.send_count);

        }
    }

}
