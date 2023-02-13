package cs662;

import sun.misc.Signal;
import sun.misc.SignalHandler;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.Scanner;

/**
 * Jisoo Lee 02/05/2023
 *
 * Class: UserView
 * Description:
 * This UserView class is dealing with the user's inputs.
 * In makeDirectory(), making the directory folder.
 * By using askFileTransfer() method, asks if the user put all the files are in the directory folder.
 * In checkFileTransfer() method, it checks the user input (y/n).
 * Asks the user what keyword want to search by askKeyword() method.
 *
 */
public class UserView {
    FileProcess fp = new FileProcess();
    MakeFileDir m = new MakeFileDir();

    /* makeDirectory()
     * input : none
     * output : runs makeDir() in MakeFileDir class which is making the directory.
     * return : none
     * */
    public void makeDirectory() {
        //Make new folder directory
        m.makeDir();
    }

    /* askFileTransfer()
     * input : none
     * output : none
     * return : user's input
     * */
    public String askFileTransfer() {
        Scanner scan = new Scanner(new BufferedReader(new InputStreamReader(System.in)));
        System.out.println("Did you transferred all your files into the folder?  (type : Y/N)");
        return scan.nextLine();
    }

    /* checkFileTransfer()
     * input : String answer
     * output : validate the answer String to go to the next step.
     * return : none
     * */
    public void checkFileTransfer(String answer) {
        if (answer.equalsIgnoreCase("N")) {
            System.out.println("Please put your files into the folder");
            System.exit(0);
        } else if(answer.equalsIgnoreCase("Y")) {
            File f = new File(m.getDir());
            //if the folder is empty
            if (f.listFiles().length == 0) {
                System.out.println("Folder is empty. Please put your files into the folder.");
                System.exit(0);
            }
            //check the header column of the files that are going to be merged
            fp.checkHeaderCols();
            //merge into one file
            fp.mergeFiles();
        } else {
            System.out.println("Please enter Y or N.");
            System.exit(0);
        }
    }

    /* askKeyword()
     * input : none
     * output : none
     * return : keyword user's input
     * */
    public void askKeyword(){
        //Used signal handler for ending the program (not to interrupt the keyword searching)
        SignalHandler signalHandler = new SignalHandler() {
            @Override
            public void handle(Signal signal) {
                //when the user type the signal
                System.out.println("Program exit signal received. Bye!");
                System.exit(0);
            }
        };

        //Set the interrupt signal (CTRL + C) --> In Mac OS, stop running is the interrupt signal
        Signal.handle(new Signal("INT"), signalHandler);
        Scanner scan = new Scanner(System.in);
        //user's input will be val
        String val;
        while (true) {
            System.out.println("\nType the keyword that you want to search :   (exit : click stop run or Ctrl + c) (history: type 'csvhis')");
            val = scan.nextLine();
            if (val.equalsIgnoreCase("csvhis")) { //if user enters the 'csvhis' - show the list of search history csv file
                fp.printCSVHistory();
            } else {
                fp.searchKeyword(val); //search the value that user typed in

            }
        }
    }

}
