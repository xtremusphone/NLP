package nlp;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class AutoCorrect {
    
    //Using Norvig Spell Checker Method
    
    /*
    1. Norvig Spell Checker method may not be that great due to limitation on the number of 
    edit distance which people rarely does type within 1 - 2 edit distance.
    2. Changes in algorithm might occur or maybe a hybrid of bruteforce with statistical method
    might be used together.
    */
    
    private String DEFAULT_DICTIONARY_PATH = "Dictionary.txt";
    
    private String wordErr;
    private ArrayList<String> bagOfWord;
    
    public AutoCorrect(){
        bagOfWord = new ArrayList<>();
        checkDictionaryFile();
        System.out.println(bagOfWord.size());
    }
    
    private boolean checkDictionaryFile(){
        File dict = new File(DEFAULT_DICTIONARY_PATH);
        if(dict.exists()){
            try{
                Scanner scn = new Scanner(dict);
                while(scn.hasNextLine()){
                    String tmp = scn.nextLine();
                    bagOfWord.add(tmp);
                }
            }
            catch(IOException e){
                System.out.println(e);
            }
            return true;
        }
        else
            return false;
    }
    
    public String spellChecker(String input){
        if(bagOfWord.contains(input.toLowerCase()))
            return input;
        else{
            for(String x: editDistance1(input)){
                if(bagOfWord.contains(x.toLowerCase())){
                    return x;
                }
                    
            }

            for(String x:editDistance2(input)){
                if(bagOfWord.contains(x.toLowerCase()))
                    return x;
            }
        }
        return null;
    }
    
    private ArrayList<String> editDistance1(String input){
        ArrayList<String> candidate = new ArrayList<>();
        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        
        //1. replacing a letter
        for(int i = 0;i < input.length();i++){
            for(int j = 0;j < alphabet.length();j++){
                candidate.add(input.substring(0,i) + alphabet.charAt(j) + input.substring(i + 1,input.length()));
            }
        }
        
        //2. removing a letter
        for(int i = 0;i < input.length();i++){
            candidate.add(input.substring(0,i) + input.substring(i + 1,input.length()));
        }
        
        //3. inserting a letter
        for(int i = 0;i <= input.length();i++){
            for(int j = 0;j < alphabet.length();j++){
                candidate.add(input.substring(0,i) + alphabet.charAt(j) +input.substring(i,input.length()));
            }
        }
        
        //4. swapping letter
        for(int i =0; i < input.length() - 1;i++){
            candidate.add(input.substring(0,i) + input.charAt(i + 1) + input.charAt(i) + input.substring(i + 2,input.length()));
        }
        
        return candidate;
    }
    
    public ArrayList<String> editDistance2(String input){
        ArrayList<String> tmp = new ArrayList<>();
        for(String x:editDistance1(input)){
            for(String y:editDistance1(x)){
                tmp.add(y);
            }
        }
        return tmp;
    }
}
