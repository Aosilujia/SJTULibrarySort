package com.sjtuserfid.callnum;

public class CallNum {


    //返回结果字符串
    String parseresult;
    //语种
    String language="Default";

    //分类号相关
    //全分类号
    String classNumber;
    //大小类号
    private String bigClass;
    private String smallClass="";

    //书次号相关
    //全书次号
    String bookNumber;
    //中文种次号，分正副
    private int bookNum_chinese;
    private int subBookNum_chinese;
    //英文著者号，分作者号和书名缩略字母和版本号
    private String author_english="";
    private String bookName_english="";
    private int versionNum_english;

    //辅助区分号
    String extraNumber;
    //中文版本号
    private int versionNum_Chinese;
    //卷次号
    private int sectionNumMain;
    private int sectionNumSub;
    //年代号
    private String yearNum;
    public CallNum(){
        System.out.println("Call Num need string");
    }

    //根据字符串分析索书号
    public CallNum(String inputS){

        //去除两端空格
        String S=inputS.trim();

        //System.out.println("init class:CallNum(string)");//debug
        /**语种判断，遍历查找语种分隔符
         *此处需要可配置更多语种*/
        int i=0;
        for (; i<S.length(); i++){
            char ch=S.charAt(i);
            if ('/'==ch){
                language="chinese";
                break;
            }
            else if ('#'==ch){
                //判断c#的特殊情况
                if (i<S.length() && S.charAt(i+1)=='-'){
                    continue;
                }
                language="japanese";
                break;
            }
            else if (' '==ch){
                language="english";
                break;
            }
        }

        //分割分类号
        if (i==S.length()){
            parseresult="No such language";
            return;
        }
        else if (i+1==S.length()){
            parseresult="No bookNumber";
            return;
        }
        else {
            classNumber=S.substring(0,i);
        }

        int bookNumberOffset=i+1;

        //遍历查找书次号与辅助号之间的空格
        //可考虑改写成String.indexof
        for (i+=1;i<S.length();i++){
            char ch=S.charAt(i);
            if (' '==ch) {
                break;
            }
        }

        //无空格，即无辅助号，存bookNumber
        if (i==S.length()){
            bookNumber=S.substring(bookNumberOffset);
            extraNumber="";
        }
        //有空格，即有辅助号，存bookNumber,extraNumber
        else{
            bookNumber=S.substring(bookNumberOffset,i);
            extraNumber=S.substring(i+1);
        }

        parseresult="normal";
    }


    //构造函数列表

    public void setBigClass(String S){
        this.bigClass=S;
    }

    public void setSmallClass(String S){
        this.smallClass=S;
    }

    public String getSmallClass(){
        return smallClass;
    }

    public void setBookNum_chinese(int i){
        this.bookNum_chinese=i;
    }

    public int getBookNum_chinese() {
        return bookNum_chinese;
    }

    public void setSubBookNum_chinese(int subBookNum_chinese) {
        this.subBookNum_chinese = subBookNum_chinese;
    }

    public int getSubBookNum_chinese() {
        return subBookNum_chinese;
    }

    public void setAuthor_english(String author_english) {
        this.author_english = author_english;
    }
    public String getAuthor_english() {
        return author_english;
    }

    public void setBookName_english(String bookName_english) {
        this.bookName_english = bookName_english;
    }

    public String getBookName_english() {
        return bookName_english;
    }

    public void setVersionNum_english(int versionNum_english) {
        this.versionNum_english = versionNum_english;
    }

    public int getVersionNum_english() {
        return versionNum_english;
    }

    public void setVersionNum_Chinese(int versionNum_Chinese) {
        this.versionNum_Chinese = versionNum_Chinese;
    }

    public int getVersionNum_Chinese() {
        return versionNum_Chinese;
    }

    public void setSectionNumMain(int sectionNumMain) {
        this.sectionNumMain = sectionNumMain;
    }

    public int getSectionNumMain() {
        return sectionNumMain;
    }

    public void setSectionNumSub(int sectionNumSub) {
        this.sectionNumSub = sectionNumSub;
    }

    public int getSectionNumSub() {
        return sectionNumSub;
    }

    public void setYearNum(String yearNum) {
        this.yearNum = yearNum;
    }

    public String getYearNum() {
        return yearNum;
    }

    //功能型函数
    public String Status(){
        return parseresult;
    }

    public void PrintLanguage(){
        System.out.println(language);
    }

}
