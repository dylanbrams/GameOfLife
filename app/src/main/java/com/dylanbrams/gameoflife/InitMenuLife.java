package com.dylanbrams.gameoflife;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.dylanbrams.gameoflife.LifeComponents.LifeMatrix;

public class InitMenuLife extends AppCompatActivity {

    private static final boolean AUTO_HIDE = true;
    private View mContentView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init_menu_life);

    }

    public void openGameOfLife(View View)
    {
        Intent intent = new Intent(InitMenuLife.this, LifeActivity.class);
        startActivity(intent);
    }
}
