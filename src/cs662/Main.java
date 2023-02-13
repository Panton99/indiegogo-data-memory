package cs662;

/**
 * Jisoo Lee 02/05/2023
 *
 * Class: Main
 * Description:
 * Runs all the methods for making directory, merging, and searching keyword.
 */
public class Main {

    public static void main(String[] args) {
        Input i = new Input();
        UserView u = new UserView();

        //make Directory
        u.makeDirectory();
        //ask if user transferred the files into the directory
        i.setFileTransfer(u.askFileTransfer());
        //check the files and merge the files into one file
        u.checkFileTransfer(i.getFileTransfer());
        //asks user about the keyword
        u.askKeyword();
    }
}
