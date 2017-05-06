package com.example.bundlerexample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.bundlerexample.model.User;
import com.example.bundlerexample.test.C;
import com.hendraanggrian.bundler.Bundler;

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

        if (true) {
            Log.d("ASD", Bundler.wrap(C.class, "Hello", "World", "!").toString());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_main_example1:
                startActivity(new Intent(this, Example1Activity.class)
                        .putExtras(Bundler.wrap(Example1Activity.class, true, false)));
                break;
            case R.id.button_main_example2:
                User user = new User();
                user.name = "Hendra";
                startActivity(new Intent(this, Example2Activity.class)
                        //.putExtra(E.Example2Activity.user, Parcels.wrap(user)));
                        .putExtras(Bundler.wrap(Example2Activity.class, user)));
                break;
        }
    }
}