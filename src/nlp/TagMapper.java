/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nlp;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

/**
 *
 * @author User
 */
public class TagMapper {
    
    private final String BROWN_MAPPING_PATH = "brown-uni.map";
    private final HashMap<String,String> mapper;
    
    public TagMapper(){
        mapper = new HashMap<>();
        loadMapper();
    }
  
    public HashMap<String,String> getConverted(HashMap<String,String> tagged){
        HashMap<String,String> temp = new HashMap<>();
        for(String x:tagged.keySet()){
            temp.put(x, mapper.get(tagged.get(x)));
        }
        return temp;
    }
    
    public String getConverted(String tag){
        return mapper.get(tag);
    }
    
    private void loadMapper(){
        try{
            Scanner scn = new Scanner(new File(BROWN_MAPPING_PATH));
            while(scn.hasNextLine()){
                mapper.put(scn.next().toLowerCase(), scn.next().toLowerCase());
            }
        }
        catch(IOException e){
            System.out.println(e);
        }
    }
}
