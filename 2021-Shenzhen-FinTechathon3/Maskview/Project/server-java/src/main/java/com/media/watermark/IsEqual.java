package com.media.watermark;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class IsEqual {

    public String  isequal(ArrayList<String> robustWaterMarkList){
        //int ans=0;
        String code="";
            char[] codenum=new char[22];
            for (int i=0;i<22;++i){
                int[] a=new int[]{0,0,0,0,0,0,0,0,0,0,0};
                for(int j=0;j<robustWaterMarkList.size();++j){
                    a[robustWaterMarkList.get(j).charAt(i)-'0']++;
                }
                int max=0;
                //int size=a[0];
                for(int j=1;j<10;++j){
                    if(a[j]>a[j-1]){
                        max=j;
                    }
                }
                codenum[i]=(char)(max+'0');
            }
            code=String.valueOf(codenum);
            //code=codenum.toString();
        return code;
    }
}
