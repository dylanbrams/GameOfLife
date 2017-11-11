package com.dylanbrams.gameoflife;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class InitMenuActivity extends AppCompatActivity {

    private static final boolean AUTO_HIDE = true;
    private View mContentView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init_menu_life);

    }

    public void openGameOfLife(View View)
    {
        EditText mEdit = (EditText)findViewById(R.id.etRandomSeed);
        Integer intRandInput = 0;
        try{
            intRandInput = Integer.parseInt( mEdit.getText().toString());}
        catch (Exception ex) {
            intRandInput = 0;
        }

        Intent intent = new Intent(InitMenuActivity.this, LifeActivity.class);

        intent.putExtra("RANDOM_SEED", intRandInput);
        startActivity(intent);
    }
}
