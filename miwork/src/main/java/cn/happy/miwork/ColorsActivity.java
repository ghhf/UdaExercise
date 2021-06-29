package cn.happy.miwork;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class ColorsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container,new ColorsFragment())
                .commit();
    }

}