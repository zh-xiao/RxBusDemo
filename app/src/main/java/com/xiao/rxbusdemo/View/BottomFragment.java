package com.xiao.rxbusdemo.View;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.xiao.rxbusdemo.R;
import com.xiao.rxbusdemo.utils.Bus;

import io.reactivex.functions.Consumer;

/**
 * Created by zhangxiao on 2018/5/22 0022.
 */
public class BottomFragment extends Fragment implements View.OnClickListener{

    private Button mSubscribeIncreaseOne;
    private Button mSubscribeIncreaseAuto;

    public BottomFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bottom, container, false);
        mSubscribeIncreaseOne = view.findViewById(R.id.subscribe_increase_one);
        mSubscribeIncreaseAuto = view.findViewById(R.id.subscribe_increase_auto);
//        mSubscribeIncreaseOne.setOnClickListener(this);
        mSubscribeIncreaseOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bus.subscribe("increase_one", new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        mSubscribeIncreaseOne.setText(integer + "");
                    }
                });
                mSubscribeIncreaseOne.setEnabled(false);
                Toast.makeText(getContext(), "订阅+1事件成功", Toast.LENGTH_SHORT).show();
            }
        });
        mSubscribeIncreaseAuto.setOnClickListener(this);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Bus.clear();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.subscribe_increase_one:
                Bus.subscribe("increase_one", new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        mSubscribeIncreaseOne.setText(integer + "");
                    }
                });
                mSubscribeIncreaseOne.setEnabled(false);
                Toast.makeText(getContext(), "订阅+1事件成功", Toast.LENGTH_SHORT).show();
                break;
            case R.id.subscribe_increase_auto:
                Bus.subscribe("increase_auto", new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        mSubscribeIncreaseAuto.setText(integer+"");
                    }
                });
                mSubscribeIncreaseAuto.setEnabled(false);
                Toast.makeText(getContext(), "订阅自增事件成功", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
