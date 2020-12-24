package com.sjtuserfid.sjtulibrarysort;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.sjtuserfid.callnum.CompareCallNo;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private SensorActivity sensorActivity;
    private Button comparebutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        comparebutton= findViewById(R.id.button_compare);
        comparebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonCompare();
            }
            });
    }


    public void buttonCompare(){

        //读取两个索书号输入框数据
        String callno1= ((EditText)findViewById(R.id.editText)).getText().toString();
        String callno2= ((EditText)findViewById(R.id.editText2)).getText().toString();
        //debug callno
        System.out.println("##Two callnos are"+callno1+","+callno2+"\n");

        TextView resultview=findViewById(R.id.resultView);

        //判断数据空
        if (callno1==null || callno2==null){
            resultview.setText("输入不能为空");
            return;
        }

        /**调用比较索书号的库*/
        //初始化CompareCallNo 类
        CompareCallNo Compare=new CompareCallNo();
        //调用Compare函数直接比较两个字符串
        int compareResult=Compare.Compare(callno1,callno2);
        System.out.println("libsort result:"+compareResult); //debug
        String resultContent;
        //根据比较结果输出
        if (compareResult==-1 || compareResult==0){
            resultContent="比较结果:\n"+callno1+'\n'+callno2;
        }
        else{
            resultContent="比较结果:\n"+callno2+'\n'+callno1;
        }


        resultview.setText(resultContent);

    }
}
