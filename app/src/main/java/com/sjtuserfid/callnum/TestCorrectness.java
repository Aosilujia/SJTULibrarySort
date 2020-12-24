package com.sjtuserfid.callnum;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestCorrectness{

    public static List<List<String>> readCSV(String filePath, boolean hasTitle){
        List<List<String>> data=new ArrayList<>();
        String line=null;
        try {
            //BufferedReader bufferedReader=new BufferedReader(new FileReader(filePath));
            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(new FileInputStream(filePath),"GBK"));
            if (hasTitle){
                //第一行信息，为标题信息
                line = bufferedReader.readLine();
                String[] items=line.split(",");
                data.add(Arrays.asList(items));
                System.out.println("标题行："+line);
            }

            int i=0;
            while((line=bufferedReader.readLine())!=null){
                i++;
                //数据行
                String[] items=line.split(",");
                data.add(Arrays.asList(items));
                //System.out.println("第"+i+"行："+line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }



    private static void writeRow(List<String> row, BufferedWriter csvWriter) throws IOException {
        int i = 0;
        for (String data : row) {
            csvWriter.write(data);
            if (i != row.size() - 1) {
                csvWriter.write(",");
            }
            i++;
        }
    }

    public static void main(String[] args) throws IOException {

        List<List<String>> booklist = readCSV(".\\app\\src\\main\\java\\com\\sjtuserfid\\callnum\\B200中文外借索书号列表.csv", true);
        int datanumber=booklist.size();
        int beginpoint=1;
        int i=beginpoint;

        List<List<String>> resultlist=new ArrayList<>();
        for (int o=0;o<datanumber;o++){
            resultlist.add(new ArrayList<String>());
        }

        int correctnumber=0;
        while (i<datanumber){

            //元素的最终位置
            int finalposition=beginpoint;
            String BookN1=booklist.get(i).get(0);
            for(int j=beginpoint;j<datanumber;j++){
                if (i==j){
                    continue;
                }

                String BookN2=booklist.get(j).get(0);
                int compresult=CompareCallNo.Compare(BookN1,BookN2);

                //遍历之后的书，找到一本小的就把位置置后一个
                if (compresult==1) {
                    finalposition += 1;
                }
                else if (compresult==0){
                    //
                    System.out.println(BookN1);
                    System.out.println(BookN2);
                    i=datanumber;
                }
            }

            //把内容写到结果的列表里面对应位置

            //while (resultlist.get(finalposition).size()!=0){
            //    finalposition+=1;
            //}
            resultlist.set(finalposition,booklist.get(i));

            i+=1;
            System.out.println("目前比较到第"+i+"个");
            System.out.println(BookN1+":"+finalposition);
        }


        File csvOutputFile = new File("B200chineseresult.csv");
        BufferedWriter csvFileOutputStream = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(csvOutputFile), "GBK"), 1024);
        for (List<String> exportDatum : resultlist) {
            writeRow(exportDatum, csvFileOutputStream);
            csvFileOutputStream.newLine();
        }

        csvFileOutputStream.flush();
        csvFileOutputStream.close();

    }

}




