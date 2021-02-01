import exception.MovieReviewException;
import helper.CommonUtils;

import java.util.Scanner;

/**
 * this is entry class
 * to initialize the application with dummy data do -1
 * else provide other options
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
                movieReviewSystem.selectChoice(choice);
            }
            catch (MovieReviewException exc){
                CommonUtils.printSeperationLine();
                System.out.println(exc.getMessage());
                CommonUtils.printSeperationLine();
            }
        }
    }

}
