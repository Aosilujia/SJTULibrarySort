package com.sjtuserfid.sjtulibrarysort;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.sjtuserfid.callnum.CompareCallNo;

public class MainActivity extends AppCompatActivity {
    private SensorActivity sensorActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.out.println("this is 0");
    }





    public void buttonCompare(View view){

        //读取两个索书号输入框数据
        String callno1= ((EditText)findViewById(R.id.editText)).getText().toString();
        String callno2= ((EditText)findViewById(R.id.editText2)).getText().toString();
        //debug callno
        System.out.println("##Two callnos are"+callno1+","+callno2+"\n");

        TextView resultview=findViewById(R.id.resultView);

        //比较索书号
        if (callno1==null || callno2==null){
            resultview.setText("输入不能为空");
            return;
        }
        CompareCallNo Compare=new CompareCallNo();
        int compareResult=Compare.Compare(callno1,callno2);
        System.out.println("libsort result:"+compareResult);
        String resultContent;
        //根据比较结果输出
        if (compareResult==-1 || compareResult==0){
            resultContent=callno1+'\n'+callno2;
        }
        else{
            resultContent=callno2+'\n'+callno1;
        }


        resultview.setText(resultContent);

    }
}
