package com.sjtuserfid.callnum;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;


public class ConfigureRule {

    private HashMap<String,Integer> classRule=new HashMap<String,Integer>();
    private HashMap<String,String>  smallclassRule=new HashMap<String, String>();
    private HashMap<String,Integer> bookorderRule=new HashMap<String, Integer>();
    private HashMap<String,Integer> chineseVersionRule=new HashMap<String, Integer>();

    public static void readRuleFile(){

        //配置文件路径
        String filePath="../sortrule.json";
        try {
            FileInputStream fileInputStream = new FileInputStream(filePath);

        }catch (FileNotFoundException e){
            e.printStackTrace();
        }

        //配置GSON流读取
        //JsonReader

    }


    public ConfigureRule(){

        //默认的映射规则
        //大类
        for (int i=0;i<(1+'T'-'A');i+=1){
            String key=String.valueOf((char)('A'+i));
            classRule.put(key,i+1);
        }
        classRule.put("TB",21);
        classRule.put("TE",22);
        classRule.put("TF",23);
        classRule.put("TG",24);
        classRule.put("TH",25);
        classRule.put("TJ",26);
        classRule.put("TK",27);
        classRule.put("TL",28);
        classRule.put("TM",29);
        classRule.put("TN",30);
        classRule.put("TP",31);
        classRule.put("TQ",32);
        classRule.put("TS",33);
        classRule.put("TU",34);
        classRule.put("TV",35);

        classRule.put("U",36);
        classRule.put("V",37);
        classRule.put("X",38);
        classRule.put("Z",39);

        //小类特殊字符
        smallclassRule.put("-","%");
        smallclassRule.put("=","&");
        smallclassRule.put(":","'");
        smallclassRule.put("#","(");
        smallclassRule.put("+",")");

        //种次号，A对应100，B对应200
        for (int i=0;i<('Z'+1-'A');i+=1){
            String key=String.valueOf((char)('A'+i));
            bookorderRule.put(key,(i+1)*100);
        }

        //中文版本号，一二三四五六。。。
        chineseVersionRule.put("一",1);
        chineseVersionRule.put("二",2);
        chineseVersionRule.put("三",3);
        chineseVersionRule.put("四",4);
        chineseVersionRule.put("五",5);
        chineseVersionRule.put("六",6);
        chineseVersionRule.put("七",7);
        chineseVersionRule.put("八",8);
        chineseVersionRule.put("九",9);
        chineseVersionRule.put("十",10);
        chineseVersionRule.put("十一",11);
        chineseVersionRule.put("十二",12);
        chineseVersionRule.put("十三",13);
        chineseVersionRule.put("十四",14);
        chineseVersionRule.put("十五",15);
        chineseVersionRule.put("十六",16);
        chineseVersionRule.put("十七",17);
        chineseVersionRule.put("十八",18);
        chineseVersionRule.put("十九",19);
        chineseVersionRule.put("二十",20);
    }

    //返回大类顺序号
    public Integer classOrder(String classS){
        if (classRule.containsKey(classS)){
            return classRule.get(classS);
        }
        else {
            return -1;
        }
    }

    public Integer getCategory(String bookS){
        if (bookorderRule.containsKey(bookS)){
            return bookorderRule.get(bookS);
        }
        else{
            return -1;
        }
    }

    //替换小类中特殊字符
    public String serializedSmallClass(String sclassS){
        String serializedclass="";
        for (int i=0;i<sclassS.length();i+=1){
            String subchar=String.valueOf(sclassS.charAt(i));
            if (smallclassRule.containsKey(subchar)){
                serializedclass+=smallclassRule.get(subchar);
            }
            else{
                serializedclass+=subchar;
            }
        }
        return serializedclass;
    }

    public Integer getChineseVersion(String versionS){
        if (chineseVersionRule.containsKey(versionS)){
            return chineseVersionRule.get(versionS);
        }
        else{
            return -1;
        }
    }

}
