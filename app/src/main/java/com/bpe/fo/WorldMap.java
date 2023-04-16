package com.bpe.fo;

public class WorldMap {
    String Name;
    String Key;
    void NameKeySeparator(String Data){
        Name = "";
        Key = "";
        int delimiter = 0;
        for(int i=0;i<Data.length();i++){
            if(Data.charAt(i)==' ' && Data.charAt(i+1)=='-' && Data.charAt(i+2)==' '){
                delimiter = 1;
                i++;i++;
                continue;
            }
            else{
                if(delimiter == 0){
                    Name+=Data.charAt(i);
                }
                else{
                    Key+=Data.charAt(i);
                }
            }
        }
    }
    String getName(){
        return Name;
    }
    String getKey(){
        return Key;
    }
    void setKey(String k){
        Key = k;
    }
}
