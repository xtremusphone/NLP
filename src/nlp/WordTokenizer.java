package nlp;

import java.util.ArrayList;
import java.util.List;

public class WordTokenizer {

    private List<String> compound;
    private String in;
    
    public WordTokenizer(){
        compound = new ArrayList();
    }
    
    public WordTokenizer(String input){
        in = input;
        compound = new ArrayList();
    }
    
public List<String> tokenizer(){
        if(in == null) return null;
        
        return compound;
    }
    
    public List<String> tokenizer(String input){
        if(input.equals("") || input.equals(" ")) return null;
        
        //greedy split
        String[] words = input.split(" ");
        
        for(int i = 0; i < words.length;i++){
            String original = words[i];
            
            //check if it is word starting or ends with quotation
            if(original.charAt(0) == '"' && original.charAt(original.length() - 1) == '"'){
                compound.add("\"");
                compound.add(original.substring(1,original.length() - 2));
                compound.add("\"");
                continue;
            }
            else if(original.charAt(0) == '"'){
                compound.add("\"");
                compound.add(original.substring(1,original.length() - 1));
                continue;
            }
            else if(original.charAt(original.length() - 1) == '"'){
                compound.add(original.substring(0,original.length() - 2));
                compound.add("\"");
            }
            
            //check if word is surrounded with bracket
            if(original.charAt(0) == '(' && original.charAt(original.length() - 1) == ')'){
                compound.add("(");
                compound.add(original.substring(1,original.length() - 1));
                compound.add(")");
                continue;
            }
            else if(original.charAt(0) == '('){
                compound.add("(");
                compound.add(original.substring(1,original.length() - 1));
                continue;
            }
            else if(original.charAt(original.length() - 1) == ')'){
                compound.add(original.substring(0,original.length() - 2));
                compound.add(")");
                continue;
            }
            
            //check if it is an acronym, words that actually ends with period or words that is actually at end of the sentence
            if(Character.isAlphabetic(original.charAt(0)) && original.length() > 1 && original.charAt(1) == '.'){
                int last_index = original.lastIndexOf(".");
                if(original.charAt(last_index - 1) == '.'){
                    compound.add(original.substring(0,last_index));
                    compound.add(".");
                    continue;
                }
                else{
                    compound.add(original.substring(0,last_index + 1));
                    for(int j = last_index + 1; j < original.length();j++){
                        compound.add(original.charAt(j) + "");
                    }
                    continue;
                }
            }
            else if(original.charAt(original.length() - 1) == '.'){
               if(i - 1 >= 0){
                   compound.add(original.substring(0,original.length() - 1));
                   compound.add(".");
                   continue;
               }
               else{
                   compound.add(original);
                   continue;
               }
            }
            
            if(Character.isLetterOrDigit(original.charAt(original.length() - 1)) == false){
                if(original.length() <= 2){
                    compound.add(original.charAt(0) + "");
                    compound.add(original.charAt(1) + "");
                    continue;
                }
                else{
                    compound.add(original.substring(0,original.length() - 1));
                    compound.add(original.charAt(original.length() - 1) + "");
                    continue;
                }
            }
            
            //check if clitic or not
            if(original.contains("'") && original.length() > 1 && original.indexOf("'") > 0){
                
            }
            
            compound.add(original);
        }
      
        return compound;
    }
    
}