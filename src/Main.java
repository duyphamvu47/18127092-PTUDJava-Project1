package src;
import java.util.*;
import src.App.*;

public class Main {
    public static void main(String[] args) {
        Slang_Dictionary dict = new Slang_Dictionary();
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter slang: ");
        String keyword = sc.nextLine();
        dict.findMeaing(keyword);
        dict.getSearchHistory();
    }
}
