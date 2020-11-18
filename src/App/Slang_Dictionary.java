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
        if(Files.exists(Paths.get("CurrentData.txt"))){
            this.readFile("CurrentData.txt");
        }
        else{
            this.readFile("slang.txt");
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
        ArrayList<String> res = dict.get(slang);
        System.out.println("The value is: " + res); 
        updateHistory(slang, res);
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
        updateHistory(meaning, res);
        System.out.print(res);
    }


    private void updateHistory(String keyword, ArrayList<String> search_result){
        String line = keyword;
        if (search_result.size() > 0){
            line += ": ";
            line += search_result.get(0);
            for(int i = 1; i < search_result.size(); i++){
                line = line + ", " + search_result.get(i);
            }
        }
        try(FileWriter fw = new FileWriter("History.txt", true)) {
            line += "\n";
            fw.write(line);
            fw.close();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public void getSearchHistory(){
        if(Files.exists(Paths.get("History.txt"))){
            try(BufferedReader bw = new BufferedReader(new FileReader(new File("History.txt")))) {
                String line;
                while((line = bw.readLine()) != null){
                    System.out.println(line);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else{
            System.out.println("Can't find History.txt");
        }
    }

}
