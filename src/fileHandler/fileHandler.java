package src.fileHandler;

import java.io.*;
import java.util.ArrayList;

public class fileHandler {
    public static void read(String fileName){
        try(BufferedReader br = new BufferedReader(new FileReader(new File(fileName)))) {
            String line = br.readLine();
            // ArrayList<String, String> a = new ArrayList<String, String>();
            while ((line = br.readLine()) != null){
                String[] preprocess = line.split("`");
                String slang = preprocess[0];
                String[] meaning = preprocess[1].split("\\| ");
                for(String temp : meaning){
                    System.out.println(slang + ", " + temp);
                }
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        read("input.txt");
    }
}
