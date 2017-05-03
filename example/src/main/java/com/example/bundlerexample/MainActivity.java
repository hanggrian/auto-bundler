package com.example.bundlerexample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.example.bundlerexample.model.User;

import org.parceler.Parcels;

import butterknife.BindView;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.toolbar_main) Toolbar toolbar;
    @BindView(R.id.button_main_example1) Button buttonExample1;
    @BindView(R.id.button_main_example2) Button buttonExample2;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(toolbar);
        buttonExample1.setOnClickListener(this);
        buttonExample2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_main_example1:
                startActivity(new Intent(this, Example1Activity.class)
                        .putExtra("hello", "asdhiuw")
                        .putExtra("world", "weviuw"));
                break;
            case R.id.button_main_example2:
                User user = new User();
                user.name = "Hendra";
                startActivity(new Intent(this, Example2Activity.class)
                        .putExtra("user", Parcels.wrap(user)));
                break;
        }
    }
}