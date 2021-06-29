package cn.happy.miwork;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Array 数组  长度不可变，不是类, 不能通过方法修改或引用元素，可以存储基本数据类型和对象
 * ArrayList 数组列表  长度可变，是类， 可以通过方法修改或引用元素， 只能存储对象类型
 *
 * AudioManager https://developer.android.google.cn/guide/topics/media-apps/audio-focus?hl=zh-cn
 */
public class NumbersActivity extends AppCompatActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container,new NumbersFragment())
                .commit();
    }
}