package src;
import java.util.*;
import src.fileHandler.fileHandler;
import src.App.*;

public class Main {
    public static void main(String[] args) {
        Slang_Dictionary dict = new Slang_Dictionary();
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter slang: ");
        String slang = sc.nextLine();
        System.out.print("Enter definition: ");
        String definition = sc.nextLine();
        String overwrite = "";
        while(!overwrite.equals("Yes") && !overwrite.equals("No")){
            System.out.print("Overwrite slang? Yes/No: ");
            overwrite = sc.nextLine();
        }
        dict.addSlang(slang, definition, overwrite);
        sc.close();
    }
}
