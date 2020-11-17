package src.App;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.io.*;
import java.nio.file.*;

public class Slang_Dictionary {
    HashMap<String, ArrayList<String>> dict = null;


    public Slang_Dictionary(){
        this.dict = new HashMap<>();
        this.readFile("slang.txt");
    }


    private void readData(){
        System.out.println("Loading data");
        if(Files.exists(Paths.get("CurrentData.txt"))) { 
            readFile("CurrentData.txt");
        }
        else{
            System.out.println("Can't found CurrentData.txt");
            readFile("Proprocessed.txt");
        }

    }


    private void readFile(String fileName){
        try(BufferedReader br1 = new BufferedReader(new FileReader(new File(fileName)))) {
            System.out.println("Read data from " + fileName);
            String line = br1.readLine();
            while((line = br1.readLine()) != null){
                String[] preprocess = line.split("`");
                String slang = preprocess[0];
                String[] meaning_arr = preprocess[1].split("\\|");
                for(int i = 0; i < meaning_arr.length; i++){
                    meaning_arr[i] = meaning_arr[i].strip();
                }
                ArrayList<String> meaning = new ArrayList<String>(Arrays.asList(meaning_arr));
                dict.put(slang, meaning);
            }
            br1.close();
            System.out.println("Load data complete!!!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void findMeaing(String slang){
        System.out.println("Searching for " + slang); 
        System.out.println("The value is: " + dict.get(slang)); 
    }

    public void findSlang(String meaning){
        List<Entry<String, ArrayList<String>>> found = this.dict
                .entrySet()
                .stream()
                .filter(entry -> entry.getValue().toString().contains(meaning))
                .collect(Collectors.toList());
        
        System.out.println("Searching for slang with definition: " + meaning); 
        Iterator i = found.iterator();
        ArrayList<String> res = new ArrayList<>();
        while(i.hasNext()){
            Object next = i.next();
            String[] temp = next.toString().split("\\=");
            res.add(temp[0]);
        }
        System.out.print(res);
    }

}
