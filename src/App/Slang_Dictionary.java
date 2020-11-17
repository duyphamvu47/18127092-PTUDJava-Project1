package src.App;
import java.util.*;
import java.io.*;
import src.fileHandler.*;
import java.nio.file.*;

public class Slang_Dictionary {
    HashMap<String, String> dict = null;


    public Slang_Dictionary(){
        this.dict = new HashMap<String, String>();
        this.readData();
    }


    private void readData(){
        String preprocessed_file = "Preprocessed.txt";
        System.out.println("Loading data");
        if(Files.exists(Paths.get("CurrentData.txt"))) { 
            readFile("CurrentData.txt");
        }
        else if ((Files.exists(Paths.get(preprocessed_file)))){
            System.out.println("Can't found CurrentData.txt");
            readFile(preprocessed_file);
        }
        else{
            fileHandler.inputFileHandler("slang.txt", "Preprocessed.txt");
            System.out.println("Can't found CurrentData.txt and Preprocess.txt");
            readFile("Proprocessed.txt");
        }

    }


    private void readFile(String fileName){
        try(BufferedReader br1 = new BufferedReader(new FileReader(new File(fileName)))) {
            System.out.println("Read data from " + fileName);
            String line;
            while((line = br1.readLine()) != null){
                String[] temp = line.split("\\|");
                dict.put(temp[0], temp[1]);
            }
            br1.close();
            System.out.println("Load data complete!!!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
