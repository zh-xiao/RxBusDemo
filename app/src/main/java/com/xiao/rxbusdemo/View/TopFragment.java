package com.xiao.rxbusdemo.View;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.xiao.rxbusdemo.R;
import com.xiao.rxbusdemo.utils.Bus;


/**
 * Created by zhangxiao on 2018/5/22 0022.
 */
public class TopFragment extends Fragment implements View.OnClickListener {
    private Button mIncreaseOne;
    private Button mIncreaseAuto;
    private int mIncreaseOneNumber;
    private int mIncreaseAutoNumber;

    public TopFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_top, container, false);
        mIncreaseOne = view.findViewById(R.id.increase_one);
        mIncreaseAuto = view.findViewById(R.id.increase_auto);
//        mIncreaseOne.setOnClickListener(this);
        mIncreaseOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIncreaseOneNumber++;
                Bus.post("increase_one",mIncreaseOneNumber);    //发送+1事件
            }
        });
        mIncreaseAuto.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.increase_one:    //点击，发送一个整数，从1开始，每次比上一次的整数+1
                mIncreaseOneNumber++;
                Bus.post("increase_one",mIncreaseOneNumber);    //发送+1事件
                break;
            case R.id.increase_auto:    //点击，每个100毫秒发送一个整数，每个整数比之前+1
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (true){
                            mIncreaseAutoNumber++;
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            Bus.post("increase_auto",mIncreaseAutoNumber);    //发送自增事件
                        }
                    }
                }).start();
                mIncreaseAuto.setEnabled(false);
                break;
        }
    }

}
