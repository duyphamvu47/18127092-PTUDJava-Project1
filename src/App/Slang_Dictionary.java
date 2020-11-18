package src.App;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.io.*;
import java.nio.file.*;

public class Slang_Dictionary {
    HashMap<String, ArrayList<String>> dict = null;
    private static final String CURRENTDATA = "CurrentData.txt";

    public Slang_Dictionary(){
        this.dict = new HashMap<>();
        this.readData();
    }


    private void readData(){
        System.out.println("Loading data");
        if(Files.exists(Paths.get(CURRENTDATA))) { 
            readFile(CURRENTDATA);
        }
        else{
            System.out.println("Can't found CurrentData.txt");
            readFile("Proprocessed.txt");
        }

    }


    private void readFile(String fileName){
        try(BufferedReader br1 = new BufferedReader(new FileReader(new File(fileName)))) {
            System.out.println("Reading data from " + fileName);
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



    private void writeFile(){
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(new File(CURRENTDATA))))  {
            System.out.println("Writing data to CurrentData.txt");
            String line = "Slang dictionary \n";
            bw.write(line);
            String[] key = dict.keySet().toArray(new String[dict.size()]);
            for (int i = 0; i < key.length; i++){
                line = key[i] + "`";
                ArrayList<String> definition = dict.get(key[i]);
                line += definition.get(0);
                if(definition.size() > 1){
                    for(int j = 1; j < definition.size(); j++){
                        line += "| " + definition.get(j);
                    }
                }
                line += "\n";
                bw.write(line);
            }
            bw.close();
        } catch (Exception e) {
            //TODO: handle exception
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


    public void addSlang(String slang, String definition, String overwrite){

        if ((overwrite.equals("Yes") && dict.containsKey(slang)) || !dict.containsKey(slang)){
            ArrayList<String> value = new ArrayList<>();
            value.add(definition);
            dict.put(slang, value);
        }
        else if(overwrite.equals("No") && dict.containsKey(slang)){
            ArrayList<String> value = dict.get(slang);
            value.add(definition);
            dict.put(slang, value);
        }
        this.writeFile();


    }

}
