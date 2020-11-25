package src;
import java.util.*;
import src.App.*;

public class Main {
    public static void main(String[] args) {
        Slang_Dictionary dict = new Slang_Dictionary();
        Scanner sc = new Scanner(System.in);
        String slang = "";
        String definition = "";
        int option = Integer.MAX_VALUE;
        do{
            printMenu();

            if(!sc.hasNextInt())
            {
                System.out.println("Exiting!!!");
                break;

            }
            else
            {
                try {
                    option = Integer.parseInt(sc.nextLine());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                switch(option){
                    case 1:
                        System.out.print("Enter slang: ");
                        slang = sc.nextLine();
                        dict.findMeaing(slang);
                        break;

                    case 2:
                        System.out.print("Enter definition: ");
                        definition = sc.nextLine();
                        dict.findSlang(definition);
                        break;

                    case 3:
                        dict.getSearchHistory();
                        break;

                    case 4:
                        System.out.print("Enter slang: ");
                        slang = sc.nextLine();
                        System.out.print("Enter definition: ");
                        definition = sc.nextLine();
                        String overwrite = "";

                        while(!overwrite.equals("Yes") && !overwrite.equals("No")){
                            System.out.print("Overwrite slang? Yes/No: ");
                            overwrite = sc.nextLine();
                        }
                        dict.addSlang(slang, definition, overwrite);
                        break;

                    case 5:
                        System.out.print("Enter slang: ");
                        slang = sc.nextLine();
                        System.out.print("Enter definition: ");
                        definition = sc.nextLine();
                        dict.editSlang(slang, definition, sc);
                        break;
                    
                    case 6:
                        System.out.print("Enter slang: ");
                        slang = sc.nextLine();
                        dict.removeSlang(slang, sc);
                        break;
                    
                    case 7:
                        String remove ="";
                        while(!remove.equals("Yes") && !remove.equals("No")){
                            System.out.println("You sure about this. Reset data will make all of your adjustment to the data to zero");
                            System.out.print("Process? Yes/No: ");
                            remove = sc.nextLine();
                        }

                        if(remove.equals("Yes")){
                            dict.resetData();
                        }
                        break;
                    
                    case 8:
                        dict.randomSlang();
                        break;

                    case 9:
                        dict.quiz_Slang(sc);
                        break;

                    case 10:
                        dict.quiz_Definition(sc);
                        break;

                    default:
                        System.out.println("Wrong input");
                        break;

                }
            }
        }while(true);
        sc.close();
    }

    public static void printMenu(){
        System.out.println("- 1.Find definition: ");
        System.out.println("- 2.Find slang: ");
        System.out.println("- 3.Show search history: ");
        System.out.println("- 4.Add a new slang word: ");
        System.out.println("- 5.Edit a slang word: ");
        System.out.println("- 6.Delete a slang word: ");
        System.out.println("- 7.Reset data: ");
        System.out.println("- 8.Random a slang word: ");
        System.out.println("- 9.Quiz about slang definition: ");
        System.out.println("- 10.Quiz about slang word: ");
        System.out.print("Exercise to be executed (to Exit don't type number): ");
    }
}
