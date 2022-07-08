package com.example.t_prc_prototype_01;

import static com.example.t_prc_prototype_01.Worker_TEST_01.getInstance;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class SendWorker extends Worker {

    public SendWorker(Context context, WorkerParameters params) {
        super(context, params);

        //  worker_test_01 = (Worker_TEST_01) this.getApplicationContext();
    }

    /**
     * doWork 内の　処理を実行
     */
    @Override
    public Result doWork() {

        int count = 1;

        //************ 送信　処理　***************
        getInstance().Task_schedule_Send_CSV();
        Log.d("TAG", "Tossy Worker 起動中　ゴーゴーゴー");
        System.out.println("＊＊＊＊＊＊　定期実行タスク　＊＊＊＊＊＊ 成功：：：" + "回数" + count);
        count++;
        return Result.success();
    }

    /*
    public interface Listener {

    }

     */
}
