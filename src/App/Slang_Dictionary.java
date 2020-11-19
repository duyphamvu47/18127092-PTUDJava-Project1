package src.App;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.io.*;
import java.nio.file.*;

public class Slang_Dictionary {
    HashMap<String, ArrayList<String>> dict = null;
    private static final String CURRENTDATA = "CurrentData.txt";
    private static final String HISTORY = "History.txt";

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
            readFile("slang.txt");
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

        if(dict.containsKey(slang)){
            ArrayList<String> definition = dict.get(slang);
            System.out.println("The value is: " + definition); 
            updateHistory(slang, definition);
        }
        else{
            System.out.println("Can't found " + slang); 
        }

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
        System.out.print(res + "\n");
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


    public void editSlang(String slang, String definition, Scanner sc){
        String option = "";
        if (dict.containsKey(slang)){



            while(!option.equals("Yes") && !option.equals("No")){
                System.out.print("Overwrite slang? Yes/No: ");
                option = sc.nextLine();
            }
            
            addSlang(slang, definition, option);
        }
        else{
            System.out.println("Cant found slang " + slang);
            while(!option.equals("Yes") && !option.equals("No")){
                System.out.print("Create a new slang word? Yes/No: ");
                option = sc.nextLine();
            }
            if (option.equals("Yes")){
                addSlang(slang, definition, "");
            }
        }
    }


    private void updateHistory(String keyword, ArrayList<String> search_result){
        String line = keyword;
        if (!search_result.isEmpty()){
            line += ": ";
            line += search_result.get(0);
            for(int i = 1; i < search_result.size(); i++){
                line = line + ", " + search_result.get(i);
            }
        }
        try(FileWriter fw = new FileWriter(HISTORY, true)) {
            line += "\n";
            fw.write(line);
            fw.close();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public void getSearchHistory(){
        if(Files.exists(Paths.get(HISTORY))){
            try(BufferedReader bw = new BufferedReader(new FileReader(new File(HISTORY)))) {
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



    public void removeSlang(String slang, Scanner sc){
        if(dict.containsKey(slang)){
            String option = "";
            System.out.println("Slang word found");
            while(!option.equals("Yes") && !option.equals("No")){
                System.out.print("Your sure want to delete this slang word? Yes/No: ");
                option = sc.nextLine();
            }
            if (option.equals("Yes")){
                System.out.println("Slang word with key:" + slang + " , has been removed");
                dict.remove(slang);
                this.writeFile();
            }
        }
    }


    public void resetData(){
        System.out.println("Reseting");
        dict.clear();
        this.readFile("slang.txt");
        this.writeFile();
        System.out.println("Reset completed");
    }

}
