package cs662;

import java.io.File;
/**
 * Jisoo Lee 02/05/2023
 *
 * Class: MakeFileDir
 * Description:
 * This MakeFileDir class is about making the file directory.
 * Setting the folder, makeDir() method is for making the folder and shows the path.
 *
 */
public class MakeFileDir {
    private String dir = "./indiegogo-dataset-files";
    private File folder = new File(dir);

    /* getDir()
     * input : none
     * output : none
     * return : dir
     * */
    public String getDir() {
        return dir;
    }

    /* setDir()
     * input : String dir
     * output : set the dir
     * return : none
     * */
    public void setDir(String dir) {
        this.dir = dir;
    }

    /* getFolder()
     * input : none
     * output : none
     * return : File folder
     * */
    public File getFolder() {
        return folder;
    }

    /* setFolder()
     * input : File folder
     * output : set the folder value
     * return : none
     * */
    public void setFolder(File folder) {
        this.folder = folder;
    }

    /* makeDir()
     * input : none
     * output : create new folder and print out the folder path
     * return : none
     * */
    public void makeDir() {
        try {
            //check whether folder exists or not
            if (!folder.exists()) {
                //if there is no folder then make new directory folder
                if (folder.mkdir()) {
                    System.out.println("Folder is created!");
                    System.out.println("Folder name : " + folder.getName() + "\nFolder path : [" + folder.getAbsolutePath() + "]");
                    System.out.println("Please put your files into the folder that you want to merge.");
                } else {
                    System.out.println("Failed to create a folder.");
                }
            } else {
                System.out.println("Folder is already created!");
                System.out.println("Folder name : " + folder.getName() + "\nFolder path : [" + folder.getAbsolutePath() +"]");
                System.out.println("Please put your files into the folder that you want to merge.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
