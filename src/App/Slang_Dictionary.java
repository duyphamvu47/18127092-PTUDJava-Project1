package src.App;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.io.*;
import java.lang.reflect.Array;
import java.nio.file.*;

public class Slang_Dictionary {
    //Data structure
    HashMap<String, ArrayList<String>> dict = null; //String: Slang, ArrayList: definition
    //Random for quiz
    Random random = null;

    private static final String CURRENTDATA = "CurrentData.txt";
    private static final String HISTORY = "History.txt";

    public Slang_Dictionary(){
        this.dict = new HashMap<>();
        random = new Random();
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


    private String toString(ArrayList<String> a){

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < a.size(); i++){
            sb.append(a.get(i));
            if (i != 0){
                sb.append(", ");
            }
        }
        return sb.toString();
    }


    public void findMeaing(String slang){
        System.out.println("Searching for " + slang); 

        if(dict.containsKey(slang)){
            ArrayList<String> definition = dict.get(slang);
            System.out.println("The value is: " + toString(definition)); 
            updateHistory(slang, definition);
        }
        else{
            System.out.println("Can't found " + slang); 
        }

    }

    public void findSlang(String meaning){
        System.out.println("Searching for slang with definition: " + meaning);
        // Tham khao tu stackoverflow
        List<Entry<String, ArrayList<String>>> found = this.dict
                .entrySet()
                .stream()
                .filter(entry -> entry.getValue().toString().contains(meaning))
                .collect(Collectors.toList());

        if(found.isEmpty()){
            System.out.println("Can't found slang with definition: " + meaning); 
            return;
        }

        Iterator i = found.iterator();
        ArrayList<String> res = new ArrayList<>();
        while(i.hasNext()){
            Object next = i.next();
            String[] temp = next.toString().split("\\=");
            res.add(temp[0]);
        }
        updateHistory(meaning, res);
        System.out.println("The slang(s) with the definition \"" + meaning + "\": " + toString(res));
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


    public void editSlang(String slang, Scanner sc){
        if (dict.containsKey(slang)){
            System.out.println("Slang word found");

            System.out.println("- 1.Edit slang");
            System.out.println("- 2.Edit definition");
            System.out.print("Your choice: ");

            int user_choice = 3;
            try {
                user_choice = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }

            if (user_choice == 1){
                System.out.print("Enter new slang: ");
                String new_slang = sc.nextLine();
                ArrayList<String> def = dict.get(slang);
                dict.remove(slang);
                dict.put(new_slang, def);
                this.writeFile();
            }

            else if(user_choice == 2){
                System.out.print("Enter new definition: ");
                String new_def = sc.nextLine();
                this.addSlang(slang, new_def, "Yes");
            }
        }
        else{
            String userString = "";
            while(!userString.equals("Yes") && !userString.equals("No")){
                System.out.println("Can't found slang: \"" + slang + "\"");
                System.out.print("Add a new slang word? Yes/No: ");
                userString = sc.nextLine();
            }

            if(userString.equals("Yes")){
                System.out.print("New definition: ");
                String definition = sc.nextLine();
                this.addSlang(slang, definition, "");
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
                System.out.println("Search history (keyword: result):");
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


    private String randomKey(){
        List<String> keySet = new ArrayList<String>(dict.keySet());
        return keySet.get(random.nextInt(keySet.size()));
    }


    public void randomSlang(){
        String key = randomKey();
        ArrayList<String> value = dict.get(key);
        String line = key;
        line += " - \"";
        line += value.get(0);
        for(int i = 1; i < value.size(); i++){
            line = line + ", " + value.get(i);
        }
        line += "\"";
        System.out.println("The random slang word: " + line);
    }



    private String randomDefinition(String slang){
        ArrayList<String> def =  dict.get(slang);
        return def.get(random.nextInt(def.size()));
    }


    public void quiz_Slang(Scanner sc){
        String[] def = new String[4];
        String slang = this.randomKey();
        int ans = random.nextInt(4);

        def[ans] = this.randomDefinition(slang);

        for(int i = 0; i < 4; i++){
            if(i != ans){
                def[i] = this.randomDefinition(this.randomKey());
            }
        }

        List<String> tempStrings = new ArrayList<String>(4);
        tempStrings.add("A");
        tempStrings.add("B");
        tempStrings.add("C");
        tempStrings.add("D");

        System.out.println("Which one of the below answer is the definition of \"" + slang + "\"?");
        for(int i = 0; i < 4; i++){
            System.out.println(tempStrings.get(i) + ". " + def[i]);
        }


        String temp = "";
        while(!tempStrings.contains(temp)){
            System.out.print("Your choice: ");
            temp = sc.nextLine();
        }

        int user_choice = 5;

        if(temp.equals("A")){
            user_choice = 0;
        }
        else if(temp.equals("B")){
            user_choice = 1;
        }
        else if(temp.equals("C")){
            user_choice = 2;
        }
        else if(temp.equals("D")){
            user_choice = 3;
        }

        if(user_choice == ans){
            System.out.println("CORRECT!");
        }
        else{
            System.out.println("INCORRECT!. The answer is: \"" + tempStrings.get(ans) + ". " +  def[ans] + "\"");
        }
    }


    public void quiz_Definition(Scanner sc){
        String temp = this.randomKey();
        String def = this.randomDefinition(temp);
        String[] slang = new String[4];
        int ans = random.nextInt(4);

        slang[ans] = temp;

        for(int i = 0; i < 4; i++){
            if(i != ans){
                slang[i] = this.randomKey();
            }
        }

        List<String> tempStrings = new ArrayList<String>(4);
        tempStrings.add("A");
        tempStrings.add("B");
        tempStrings.add("C");
        tempStrings.add("D");

        System.out.println("Which one of the below slang have the definition \"" + def + "\"?");
        for(int i = 0; i < 4; i++){
            System.out.println(tempStrings.get(i) + ". " + slang[i]);
        }


        temp = "";
        while(!tempStrings.contains(temp)){
            System.out.print("Your choice: ");
            temp = sc.nextLine();
        }

        int user_choice = 5;

        if(temp.equals("A")){
            user_choice = 0;
        }
        else if(temp.equals("B")){
            user_choice = 1;
        }
        else if(temp.equals("C")){
            user_choice = 2;
        }
        else if(temp.equals("D")){
            user_choice = 3;
        }

        if(user_choice == ans){
            System.out.println("CORRECT!");
        }
        else{
            System.out.println("INCORRECT!. The answer is: \"" + tempStrings.get(ans) + ". " + slang[ans] + "\"");
        }
    }



}
