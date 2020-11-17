package src.fileHandler;

import java.io.*;
import java.util.ArrayList;

public class fileHandler {

    public static void inputFileHandler(String Infile, String Outfile){
        try(BufferedReader br = new BufferedReader(new FileReader(new File(Infile)));
            BufferedWriter bw = new BufferedWriter(new FileWriter(new File(Outfile)))) {

            String lineIn = br.readLine();

            while ((lineIn = br.readLine()) != null){
                String[] preprocess = lineIn.split("`");
                String slang = preprocess[0];
                String[] meaning = preprocess[1].split("\\| ");
                for(String temp : meaning){
                    bw.write(slang + "|" + temp + "\n");
                }
            }

            br.close();
            bw.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        inputFileHandler("slang.txt", "Preprocessed.txt");
    }
}
