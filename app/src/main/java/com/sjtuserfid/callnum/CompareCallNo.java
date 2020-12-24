package com.sjtuserfid.callnum;

public class CompareCallNo {

    private static CallNum CallNumA,CallNumB;
    private static boolean inited=false;
    private static ConfigureRule sortrule=new ConfigureRule();

    private static String errorinfo="";
    private static String resultinfo="";



    //比较两个索书号，-1小于，1大于
    //通过输入两个索书号进行初始化并比较
    public static int Compare(String CallNoA,String CallNoB){
        initCallNum(CallNoA,CallNoB);
        return Compare();
    }

    //直接使用初始化过的CallNumA,CallNumB进行比较
    public static int Compare(){
        errorinfo="";
        resultinfo="";
        //初始化索书号
        if (! inited){
            System.out.println("未初始化，请使用initCallNum初始化或直接调用Compare(String A,String B)");
            return 0;
        }

        //比较索书号主流程部分
        //先比较语言
        String languageA=CallNumA.language;
        String languageB=CallNumB.language;
        //语言不同
        if(!languageA.equals(languageB)){
            int languageorder=-1;
            if(sortrule.getlanguageOrder(languageA)<=sortrule.getlanguageOrder(languageB)){
                languageorder=-1;
            }
            else{
                languageorder=1;
            }
            resultinfo+="不同语言书次号";
            return languageorder;
        }


        //比较分类号
        int classorder=compareClassN();
        if (classorder<=-1){
            return -1;
        }
        else if (classorder>=1){
            return 1;
        }

        //比较分类号出现错误
        if (!errorinfo.equals("")){
            System.out.println(errorinfo);
            return 0;
        }
        else{
            //debug info
            //System.out.println("equal class number");
        }

        //若类号相等比较书次号
        int bookorder=compareBookN();
        if (bookorder<0){
            return -1;
        }
        else if (bookorder>0){
            return 1;
        }

        int extraorder=compareExtraN();
        if (extraorder<=0){
            return -1;
        }
        else{
            return 1;
        }

    }

        //初始化索书号数据结构，初步解析索书号字符串
    private static void initCallNum(String CallNoA,String CallNoB){
        CallNumA=new CallNum(CallNoA);
        CallNumB=new CallNum(CallNoB);
        inited=true;
    }


    //比较类号，相等返回0，A小于B返回-1，A大于B返回1
    private static int compareClassN(){
        int bigClassA=parseBigClass(CallNumA);
        int bigClassB=parseBigClass(CallNumB);
        if (bigClassA==-2){
            System.out.println("无法识别的大类号");
        }
        else if (bigClassA<bigClassB){
            return -1;
        }
        else if (bigClassA>bigClassB){
            return 1;
        }
        else if (bigClassA==bigClassB){
            //return 0;
            return CallNumA.getSmallClass().compareTo(CallNumB.getSmallClass());
        }
        return 0;
    }

    //解析类号，返回大类号对应顺序号，同时记录小类号
    private static int parseBigClass(CallNum callNum){
        String ClassStr=callNum.classNumber;
        int offset;
        int classNum=-1;
        String substrA;
        for (offset=0;offset<ClassStr.length();offset+=1){
            substrA=ClassStr.substring(0,offset+1);
            if (sortrule.classOrder(substrA)>-1){
                callNum.setBigClass(substrA);
                classNum=sortrule.classOrder(substrA);
            }
            //第一个字非大类号，说明格式有误
            else if(offset==0){
                //System.out.println("illegal class Number:"+ClassStr);
                return -2;
            }
            else{
                break;
            }
        }

        //记录小类号
        if (offset<ClassStr.length()) {
            callNum.setSmallClass(ClassStr.substring(offset));
        }
        else{
            callNum.setSmallClass("");
        }
        return classNum;
    }

    //比较书次号，多种语言有不同规则，返回大于0表示大于，小于0表示小于，0为相等
    private static int compareBookN(){
        String languageA=CallNumA.language;
        String languageB=CallNumB.language;
        //语言不同
        if(!languageA.equals(languageB)){
            int languageorder=-1;
            if(sortrule.getlanguageOrder(languageA)<=sortrule.getlanguageOrder(languageB)){
                languageorder=-1;
            }
            else{
                languageorder=1;
            }
            //errorinfo=CallNumA.bookNumber+","+CallNumB.bookNumber+"不同语言书次号";
            return languageorder;
        }

        //中文书次号分析，字母（可无）+数字+（-数字）（可无）；另外日文书次号暂时
        if (languageA.equals("chinese") || languageA.equals("japanese")) {
            resultinfo+="比较中文书次号 ";
            //字符串处理
            parseCategoryBookN(CallNumA);
            parseCategoryBookN(CallNumB);
            //主要书次号比较，
            if (CallNumA.getBookNum_chinese() != CallNumB.getBookNum_chinese()) {
                resultinfo+="主书号不同";
                return CallNumA.getBookNum_chinese() <= CallNumB.getBookNum_chinese() ? -1 : 1;

            }


            //主书次号相同比较横杠后副号码
            if (CallNumA.getSubBookNum_chinese()!=CallNumB.getSubBookNum_chinese()){
                resultinfo+="副书号不同";
                return CallNumA.getSubBookNum_chinese()<= CallNumB.getSubBookNum_chinese() ? -1:1;
            }

            //副书号相同，比较潜在版本号
            if (CallNumA.getVersionNum_Chinese()!=CallNumB.getVersionNum_Chinese()){
                return CallNumA.getVersionNum_Chinese()<=CallNumB.getVersionNum_Chinese()?-1:1;
            }

        }
        //英文书次号
        else if (languageA.equals("english")){
            resultinfo+="比较英文著者号 ";
            //
            parseAuthorBookN(CallNumA);
            parseAuthorBookN(CallNumB);
            //著者号比较
            if (!CallNumA.getAuthor_english().equals(CallNumB.getAuthor_english())){
                //字符串比较
                resultinfo+="著者号不同";
                return CallNumA.getAuthor_english().compareTo(CallNumB.getAuthor_english());
            }

            //著者号相同，比较书名缩略字母
            if (!CallNumA.getBookName_english().equals(CallNumB.getBookName_english())){
                resultinfo+="书名缩略不同";
                return CallNumA.getBookName_english().compareTo(CallNumB.getBookName_english());
            }

            //书名首字母相同，比较版本号
            return CallNumA.getVersionNum_english()<=CallNumB.getVersionNum_english() ? -1 : 1;

        }

        //未处理的语言返回字符串比较结果
        else{
            return CallNumA.bookNumber.compareTo(CallNumB.bookNumber);
        }

        return 0;
    }

    //处理书类号
    private static int parseCategoryBookN(CallNum callNum){
        int i=0;
        int categoryNum=0;
        String bookN=callNum.bookNumber;
        //字母号，A代表100，以此类推
        int AlgeCato=sortrule.getCategory(String.valueOf(bookN.charAt(0)));
        if (AlgeCato==-1){
            //没有对应的字母
            //do nothing
        }
        else{
            //有对应的字母，获取字母对应的数字
            categoryNum+=AlgeCato;
            i+=1;
        }

        int index_number=i;

        //遍历字母之后的数字，检测到横杠说明有副书号
        boolean subnumflag=false;
        for (;i<bookN.length();i+=1){
            char ch=bookN.charAt(i);
            if (ch=='-'){
                subnumflag=true;
                break;
            }
            //处理异常情况：非数字
            else if (ch<'0' || ch> '9'){
                break;
            }
        }
        //把数字加到字母代表的数上，生成处理过的书号
        if (i<=bookN.length()) {
            categoryNum += Integer.parseInt(bookN.substring(index_number, i));
        }
        //存储书号
        callNum.setBookNum_chinese(categoryNum);

        //有横杠，进行横杠后副书号处理
        int index_subnumber=i+1;
        if (subnumflag && i+1<bookN.length()){
            i+=1;
            for (;i<bookN.length();i+=1){
                char ch=bookN.charAt(i);
                //处理异常情况：非数字
                if (ch<'0' || ch> '9'){
                    break;
                }
            }
            callNum.setSubBookNum_chinese(Integer.parseInt(bookN.substring(index_subnumber,i)));
        }

        //中文版本号直接排在书次号后面，如T2/B12-1二，在此处理中文版本号
        //warning 由于中文字符的问题，目前的处理方法可能存在一些问题，但是考虑实际使用影响不大
        int versionnumber=sortrule.getChineseVersion(bookN.substring(bookN.length()-1));
        if (versionnumber!=-1){
            int twobyteversion=sortrule.getChineseVersion(bookN.substring(bookN.length()-2));
            if (twobyteversion!=-1){
                callNum.setVersionNum_Chinese(twobyteversion);
            }
            else{
                callNum.setVersionNum_Chinese(versionnumber);
            }
        }

        return categoryNum;
    }

    //处理著者号
    private static int parseAuthorBookN(CallNum callNum) {
        int i;
        int versionnumber;
        //完整书次号
        String bookN=callNum.bookNumber;

        //前四位表示著者
        String authorString;
        if (bookN.length()>=4) {
            authorString = bookN.substring(0, 4);
        }
        else{
            authorString=bookN;
        }
        /* 遍历查找点号，加入到著者号以方便比较大小s */
        i = 4;
        for (;i<bookN.length();i+=1){
            char ch=bookN.charAt(i);
            if (ch=='.'){
                authorString+=ch;
            }
            else{
                break;
            }
        }
        callNum.setAuthor_english(authorString);


        //遍历处理书名缩略字母
        String bookName_short="";
        String versionNum_S="";
        int bookNamefinishflag=0;

        for (;i<bookN.length();i+=1){
            char ch= bookN.charAt(i);
            //数字是版本号
            if (ch>='0' && ch<='9'){
                bookNamefinishflag=1;
                versionNum_S+=ch;
            }
            //字母是书名缩略
            else{
                //异常情况
                if (bookNamefinishflag==1){
                    //error
                    System.out.println("---------error from bookName_short-------");
                }
                bookName_short+=ch;
            }
        }

        if (versionNum_S.equals("")){
            versionNum_S="0";
        }
        versionnumber=Integer.parseInt(versionNum_S);
        callNum.setBookName_english(bookName_short);
        callNum.setVersionNum_english(versionnumber);

        return versionnumber;
    }

    //辅助号比较
    private static int compareExtraN(){
        parseExtraN(CallNumA);
        parseExtraN(CallNumB);

        /*
        总流程：
        先比较卷册号，再比较年代号
        */

        if (CallNumA.getSectionNumMain()!=CallNumB.getSectionNumMain() ){
            return CallNumA.getSectionNumMain()<=CallNumB.getSectionNumMain()?-1:1;
        }

        if (CallNumA.getSectionNumSub()!=CallNumB.getSectionNumSub()){
            return CallNumA.getSectionNumSub()<=CallNumB.getSectionNumSub()?-1:1;
        }

        if (!CallNumA.getYearNum().equals(CallNumB.getYearNum())){
            return CallNumA.getYearNum().compareTo(CallNumB.getYearNum());
        }

        return 0;
    }

    //解析单个辅助号
    private static int parseExtraN(CallNum callNum){
        String extraNumberString=callNum.extraNumber;

        //辅助号分为卷册标识符（例 V.3）和年代号(例 2011 or 2011-2020)，空格分隔
        String yearNumberS="";
        String sectionIdS="";

        extraNumberString.trim();
        //查找卷册标识符V.
        int sectionIdIndex=extraNumberString.indexOf("V.");

        if (extraNumberString.indexOf("v.")!=-1){
            sectionIdIndex=extraNumberString.indexOf("v.");
        }

        /*
        分割部分
        -比较粗略的检测策略：标识符不在第一位说明前面是年代号
        */
        if (sectionIdIndex==0){
            //第一位是V
            int spaceindex=extraNumberString.indexOf(" ");
            if(spaceindex!=-1) {
                //有空格，空格前是卷册，空格后是年代
                yearNumberS = extraNumberString.substring(spaceindex);
                sectionIdS = extraNumberString.substring(0, spaceindex);
            }
            else{
                //无空格，只有卷册
                sectionIdS = extraNumberString;
            }
        }
        else if(sectionIdIndex==-1){
            //没有卷册，都是年代
            yearNumberS=extraNumberString;
        }
        else{
            //V.前是年代，后是卷册
            yearNumberS=extraNumberString.substring(0,sectionIdIndex);
            sectionIdS=extraNumberString.substring(sectionIdIndex);
        }

        //处理卷册号，需要将字符串转为int才能正确比较
        if (!sectionIdS.equals("")) {
            sectionIdS.trim();
            sectionIdS = sectionIdS.replaceAll("V.", "");
            sectionIdS = sectionIdS.replaceAll("v.", "");
            int dashIndex = sectionIdS.indexOf("/");
            if (dashIndex == -1) {
                //没有斜杠
                try {
                    int mainsectionId = Integer.parseInt(sectionIdS);
                    callNum.setSectionNumMain(mainsectionId);
                } catch (NumberFormatException e) {
                    int mainSectionNum=sortrule.getSectionRomanNumber(sectionIdS);
                    if (mainSectionNum==-1){
                        //负一是没有记录的卷册号规则，所以认为这本书的次序很靠后
                        mainSectionNum=100;
                    }
                    callNum.setSectionNumMain(mainSectionNum);
                }
            } else if (dashIndex + 1 < sectionIdS.length()) {
                callNum.setSectionNumMain(Integer.parseInt(sectionIdS.substring(0, dashIndex)));
                try {
                    //将分割开的string卷册号转为int数字
                    callNum.setSectionNumSub(Integer.parseInt(sectionIdS.substring(dashIndex + 1)));
                } catch (NumberFormatException e) {
                    //上面一句语句报错说明卷册号可能用的是大写字母
                    int subSectionNum=sortrule.getSectionRomanNumber(sectionIdS.substring(dashIndex+1));
                    if (subSectionNum==-1){
                        //负一是没有记录的卷册号规则，所以认为这本书的次序很靠后
                        subSectionNum=100;
                    }
                    callNum.setSectionNumSub(subSectionNum);
                }
            }
        }
        /*
        处理年代号
        warning: 目前认为都是四位数，可以直接比较字符串
        */
        callNum.setYearNum(yearNumberS);


        return 0;
    }

    public static String getResultinfo() {
        return resultinfo;
    }

    //构造函数，基本不作用
    public CompareCallNo(){
      //do nothing
    }

    public CompareCallNo(String CallNoA, String CallNoB){
        initCallNum(CallNoA,CallNoB);
    }


    /**此java文件的main函数
     * 用来单独测试相关代码的对错
     * */
    public static void main(String[] args){



        System.out.println("main func");
        CallNum testnum=new CallNum("TB383 N186L RP.");

        ConfigureRule testrule=new ConfigureRule();
        //parseExtraN(CallNumA);

        //testnum.PrintLanguage();
        /*System.out.println(testnum.Status());
        System.out.println(testnum.classNumber);
        System.out.println(testnum.bookNumber);
        System.out.println(testnum.extraNumber);*/

        //System.out.println(testrule.serializedSmallClass("aaaaaa-=:#+bbbbb"));



        //System.out.println(parseBigClass(testnum));
        System.out.println(Compare("TK01#15","TK01-05 M718M V.2 2018"));
        System.out.println(getResultinfo());
        //System.out.println(Compare("TB383 N186..n 2014","TB383 N186..n2 2010"));
        //System.out.println(Compare("O441.4#52","O441.4#6"));

        //System.out.println(compareClassN());
    }

}
