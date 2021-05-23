package cn.happy.miwork;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Interface List是个接口，所有的方法都没有实现
 * Abstract Class AbstractList是个抽象类，实现了部分方法
 * Concrete Class ArrayList是具体类，具有状态和所有方法的实现
 *
 * Java泛型，E，是个占位符 类型参数 代表实际创建的ArrayList的实例。可用特定数据类型来替换E,E 元素的简写。
 * E 可被替换为任何非原始类型数据。
 *
 * Async Callback 异步回调， 让应用中的组件执行某个操作，当完成式通知回来，应用其他操作不被影响。
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView NumbersTV,ColorsTV,FamilyTV,PhrasesTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NumbersTV = findViewById(R.id.numbers);
        ColorsTV = findViewById(R.id.colors);
        FamilyTV = findViewById(R.id.family);
        PhrasesTV = findViewById(R.id.phrases);

        NumbersTV.setOnClickListener(this);
        ColorsTV.setOnClickListener(this);
        FamilyTV.setOnClickListener(this);
        PhrasesTV.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.numbers:
                Intent numbersIntent = new Intent(MainActivity.this,NumbersActivity.class);
                startActivity(numbersIntent);
                break;
            case R.id.colors:
                Intent colorsIntent = new Intent(MainActivity.this,ColorsActivity.class);
                startActivity(colorsIntent);
                break;
            case R.id.family:
                Intent familyIntent = new Intent(MainActivity.this,FamilyActivity.class);
                startActivity(familyIntent);
                break;
            case R.id.phrases:
                Intent phrasesIntent = new Intent(MainActivity.this,PhrasesActivity.class);
                startActivity(phrasesIntent);
                break;
        }
    }
}
