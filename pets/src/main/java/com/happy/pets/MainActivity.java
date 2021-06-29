package com.happy.pets;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

/**
 * 计算机存储器类型
 * 1）临时存储
 * 2）永久存储
 *
 * Android 提供的数据持久型：
 * 1）Files
 * 2）SharedPreference
 * 3) SQLite Databases
 *
 * SQL
 * 所有 SQL 语句都以 ; 结束
 * SQL 关键字 不区分大小写
 *
 * sqlite3 数据库名     sqlite3 store
 * CREATE TABLE 表名      CREATE TABLE pets (_id INTEGER, name TEXT, breed TEXT, gender INTEGER,weight INTEGER);
 * INSERT into 表名 VALUES  INSERT INTO pets (_id, name, breed, gender, weight) VALUES (2, "Garfield", "Tabby", "Male", 8);
 * SELECT * FROM 表名 查询条件（可选）  WHERE _id=5 OR ORDER BY name ASC/DESC
 * UPDATE 表名 SET column = value WHERE _id == 2;
 * DELEte FROM 表名 查询条件（可选）WHERE,⚠️ 无查询条件 会删除表中的所有数据
 * .mode ascii/tabs/column
 * .header on
 *
 *
 * TABLE CONSTRAINTS
 *  PRIMARY KEY 主键，指相关列标的唯一标识，通常与 AUTO INCREMENT 一起使用
 *  AUTO INCREMENT， 每添加一行 自动生成新的唯一的ID
 *  NOT NULL
 *  DEFAULT <value></value> 没有值时 设置一个默认值
 *
 *  CREATE TABLE pets (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, breed TEXT, gender INTEGER,weight INTEGER);
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
