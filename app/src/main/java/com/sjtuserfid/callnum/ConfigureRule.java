package com.sjtuserfid.callnum;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;


public class ConfigureRule {

    private HashMap<String,Integer> languageRule=new HashMap<>();
    private HashMap<String,Integer> classRule= new HashMap<>();
    private HashMap<String,String>  smallclassRule= new HashMap<>();
    private HashMap<String,Integer> bookorderRule=new HashMap<>();
    private HashMap<String,Integer> chineseVersionRule=new HashMap<>();
    private HashMap<String,Integer> sectionRomanNumber=new HashMap<>();

    public static void readRuleFile(){
        //还没有完成的，预留给改配置功能的函数
        //配置文件路径
        String filePath="../sortrule.json";
        try {
            FileInputStream fileInputStream = new FileInputStream(filePath);

        }catch (FileNotFoundException e){
            e.printStackTrace();
        }


    }


    public ConfigureRule(){

        //默认的映射规则

        //语言
        languageRule.put("chinese",1);
        languageRule.put("english",2);
        languageRule.put("japanese",3);


        //大类
        for (int i=0;i<(1+'T'-'A');i+=1){
            String key=String.valueOf((char)('A'+i));
            classRule.put(key,i+1);
        }
        classRule.put("TB",21);
        classRule.put("TD",22);
        classRule.put("TE",23);
        classRule.put("TF",24);
        classRule.put("TG",25);
        classRule.put("TH",26);
        classRule.put("TJ",27);
        classRule.put("TK",28);
        classRule.put("TL",29);
        classRule.put("TM",30);
        classRule.put("TN",31);
        classRule.put("TP",32);
        classRule.put("TQ",33);
        classRule.put("TS",34);
        classRule.put("TU",35);
        classRule.put("TV",36);

        classRule.put("U",37);
        classRule.put("V",38);
        classRule.put("X",39);
        classRule.put("Z",40);

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
            //小写字母
            key=String.valueOf((char)('a'+i));
            bookorderRule.put(key,(i+1)*100);
        }

        /*中文版本号，一二三四五六。。。
        */
        //utf-8
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


        //罗马数字卷册号
        sectionRomanNumber.put("I",1);
        sectionRomanNumber.put("II",2);
        sectionRomanNumber.put("III",3);
        sectionRomanNumber.put("IV",4);
        sectionRomanNumber.put("V",5);
        sectionRomanNumber.put("VI",6);
        sectionRomanNumber.put("VII",7);
        sectionRomanNumber.put("VIII",8);
        sectionRomanNumber.put("IX",9);
        sectionRomanNumber.put("X",10);

    }

    //返回语言顺序
    public Integer getlanguageOrder(String languageS){
        if (languageRule.containsKey(languageS)){
            return languageRule.get(languageS);
        }
        else {
            return -1;
        }
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

    //返回种次号字母对应的数字
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

    //中文版本号转为数字
    public Integer getChineseVersion(String versionS){
        if (chineseVersionRule.containsKey(versionS)){
            return chineseVersionRule.get(versionS);
        }
        else{
            return -1;
        }
    }

    public Integer getSectionRomanNumber(String sectionS) {
        if (sectionRomanNumber.containsKey(sectionS)){
            return sectionRomanNumber.get(sectionS);
        }
        else{
            return -1;
        }
    }
}
