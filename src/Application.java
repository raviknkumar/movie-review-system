import exception.MovieReviewException;
import helper.CommonUtils;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * this is entry class
 * to initialize the application with dummy data do -1
 * else provide other options
 * 0 to stop the application
 */
public class Application {

    public static void main(String[] args) {
        MovieReviewSystem movieReviewSystem = new MovieReviewSystem();
        Scanner sc=new Scanner(System.in);
        while (true){
            int choice;
            try {
                movieReviewSystem.displayChoices();
                choice = sc.nextInt();
                if(choice == 0)
                    break;
                movieReviewSystem.selectChoice(choice);
            }
            catch (MovieReviewException exc){
                CommonUtils.printSeperationLine();
                System.out.println(exc.getMessage());
                CommonUtils.printSeperationLine();
            }
            catch (InputMismatchException exc){
                CommonUtils.printSeperationLine();
                System.out.println("Error Reading Input:"+exc.getMessage());
                CommonUtils.printSeperationLine();
            } catch (Exception exc){
                CommonUtils.printSeperationLine();
                System.out.println("Unexpected Error occured:"+exc.getMessage());
                CommonUtils.printSeperationLine();
            }
        }
    }

}
