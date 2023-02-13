package cs662;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.*;

/**
 * Jisoo Lee 02/05/2023
 *
 * Class: FileProcess
 * Description:
 * FileProcess class have 4 public methods; checkHeaderCols(), mergeFiles(), searchKeyword(), and printCSVHistory().
 * And 3 private methods; writeSearchHistoryToFile(), readSearchHistoryFromFile(), and searchHistory().
 * These methods check the header column of each data text file and check if the header matches or not.
 * Then merge all the files into one file called mergedFile.csv.
 * When searching the user's input (keyword), used the Hashset for checking if there are any duplicates and show the result with funds_raised_percent and close_date.
 */
public class FileProcess {
    MakeFileDir m = new MakeFileDir();
    private static final String OUTPUT_FILE = "mergedFile.csv";
    private static final String DATA_STORED_FILE = "searchHistory_data.csv";
    private static final String FUNDS_RAISED_PERCENT = "funds_raised_percent";
    private static final String CLOSE_DATE = "close_date";
    //Only splits a comma that is followed by an even number of quotes (not comma within a quotes, in Urls, and inside the square brackets).
    private static final String SPLIT_PATTERN_CSV = ",(?=([^\"]\"[^\"]\")[^\"](https://[^,]+)?$)";
    private Integer frequency = 0;
    private String timestamp = "";
    private String dir = m.getDir();
    private String[] headerCols;
    private int fundsRaisedPercentIdx = 0;
    private int closeDateIdx = 0;
    private File dirList = new File(dir);
    //Mac OS keep making invisible OS file, need to delete them
    private final String[] files = dirList.list((dir, fileName) -> !fileName.equals(".DS_Store"));
    private final Set<String> foundSearchedRows = new HashSet<>();
    private  Map<String, Integer> searchHistoryList = new HashMap<>();
    private  List<String[]> historyDataList = new ArrayList<>();
    private boolean isFirstLine = true;

    /* checkHeaderCols()
     * input : none
     * output : create the headerCols array with header column values.
     * return : none
     * */
    public void checkHeaderCols() {
        //check the header column of first one in the files list
        try (BufferedReader br = new BufferedReader(new FileReader(dir + "/" + files[0]))) {
            String line = br.readLine();
            //get the header column list for reference
            if (line != null) {
                //Only splits a comma that is followed by an even number of quotes (not comma within a quotes)
                headerCols = line.split(SPLIT_PATTERN_CSV);
            }
            //print out the list of files for merging
            System.out.print("Lists of files for merging : ");
            for (String f : files) {
                System.out.print(f + " ");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* mergeFiles()
     * input : none
     * output : merging the files in the folder into one file.
     * return : none
     * */
    public void mergeFiles() {
        File f = new File(dir);
        //Get all the path of files in the directory that are made
        try (FileWriter fw = new FileWriter(OUTPUT_FILE)) {
            boolean isHeaderColWritten = false;
            for (File file : f.listFiles()) {
                //Check if the file is csv file
                if (!file.getName().endsWith(".csv")) {
                    continue;
                }
                try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                    String line = br.readLine();
                    //Only splits a comma that is followed by an even number of quotes (not comma within a quotes)
                    String[] header = line.split(SPLIT_PATTERN_CSV);
                    //check the header if it matches or not
                    if (!Arrays.equals(header, headerCols)) {
                        System.out.println("\nError: header columns do not match in " + file.getName());
                        System.exit(0);
                    }
                    //If the header is not written, then write the line in the file
                    if (!isHeaderColWritten) {
                        fw.write(line + "\n");
                        isHeaderColWritten = true;
                    }
                    //Get all the lines from the text into one file
                    while ((line = br.readLine()) != null) {
                        fw.write(line + "\n");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    /* searchKeyword()
     * input : String val (user's input)
     * output : search the val in the merged file
     * return : none
     * */
    public void searchKeyword(String val) {
        try (BufferedReader br = new BufferedReader(new FileReader(OUTPUT_FILE))){
            //When the user close the program and reruns the program
            if (searchHistoryList.isEmpty()) {
                readSearchHistoryFromFile();
            }

            //Checking "funds_raised_percent" index and "close_date" index column
            for (int i = 0; i < headerCols.length; i++) {
                if (headerCols[i].equalsIgnoreCase(FUNDS_RAISED_PERCENT)) {
                    fundsRaisedPercentIdx = i;
                }
                if (headerCols[i].equalsIgnoreCase(CLOSE_DATE)) {
                    closeDateIdx = i;
                }
            }
            //Read the merged file by line
            String line;
            String[] values;
            while ((line = br.readLine()) != null) {
                //checking every line with regex. Check if there are new lines inside the quotes
                if (line.endsWith("\"")) { //when ends with the quote char
                    //when there is no problem, just split with new line
                    values = line.split(SPLIT_PATTERN_CSV);
                } else {
                    //when there is a new line in between the quotes in the row (ex. "hello \n world" , "hello" ...)
                    StringBuilder sb = new StringBuilder(line);
                    //find the line where does not ends with the quote char
                    while (!(line = br.readLine()).endsWith("\"")) {
                        sb.append("\n").append(line); //append it to make the line complete
                    }
                    sb.append("\n").append(line);
                    //split the complete line with regex
                    values = sb.toString().split(SPLIT_PATTERN_CSV);
                }
                //ignoring the first row of CSV (header column)
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                for (String value : values) {
                    //Searching the keyword and check if the contents are same (no duplicates) --> HashSet
                    if (value.contains(val) && !foundSearchedRows.contains(line)) {
                        System.out.println("Match found: " + value + ", funds_raised_percent: " + values[fundsRaisedPercentIdx] + ", close_date: " + values[closeDateIdx]);
                        foundSearchedRows.add(line); //if it is not a duplicate then add to the list
                        break;
                    }
                }
            }
            //if there are no match in the csv file
            if (foundSearchedRows.isEmpty()) {
                System.out.println("There is no match with " + val);
            }
            searchHistory(val);
            br.close();
            writeSearchHistoryToFile(historyDataList);
            foundSearchedRows.clear(); //clear the hashSet for looping the user's input
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* writeSearchHistoryToFile()
     * input : List containing the term, the timestamp, and the frequency
     * output : write the list into a file (searchHistory.csv)
     * return : none
     * */
    private void writeSearchHistoryToFile(List<String[]> list) {
        //write the data into a file
        try (FileWriter fileWriter = new FileWriter(DATA_STORED_FILE)) { //write into csv file
            //append header when file is empty
            if (Files.size(Paths.get(DATA_STORED_FILE)) == 0) {
                //put the headers
                fileWriter.append("Search terms,Timestamp,Frequency\n");
            }
            try {
                //write all the data that user searched into the file
                for (int i = 0; i < list.size(); i++) {
                    fileWriter.append(list.get(i)[0] + "," + list.get(i)[1]+ "," + list.get(i)[2] + "\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /* readSearchHistoryFromFile()
     * input : List containing the term, the timestamp, and the frequency
     * output : write the list into a file
     * return : none
     * */
    private void readSearchHistoryFromFile() {
        //reading existing data file
        try (BufferedReader bf = new BufferedReader(new FileReader(DATA_STORED_FILE))) {
            bf.readLine(); //skip the header
            String line;
            //Get the existing data file for a new run
            while ((line = bf.readLine()) != null) {
                String[] values = line.split(",");
                String term = values[0];
                String timestampVal = values[1];
                String frequencyVal = values[2];
                //put the history data into a list
                String[] data = {term, timestampVal, frequencyVal};
                historyDataList.add(data);
            }
            //update the history data into the HashMap (get the frequency data of the term)
            for (String[] list : historyDataList) {
                if (searchHistoryList.containsKey(list[0])) {
                    searchHistoryList.put(list[0], Math.max(Integer.parseInt(list[2]), searchHistoryList.get(list[0])));
                } else {
                    searchHistoryList.put(list[0], Integer.parseInt(list[2]));
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /* searchHistory()
     * input : String searchKeyword
     * output : write the keyword(term), timestamp, and the cumulative frequency value of what user searched
     * return : none
     * */
    private void searchHistory(String searchKeyword) {
        //get the frequency value from the HashMap, if there is non set as default 1
        frequency = searchHistoryList.getOrDefault(searchKeyword, 1);
        //check if the searchKeyword is already searched or not
        if (searchHistoryList.containsKey(searchKeyword)) {
            searchHistoryList.put(searchKeyword, frequency++); //if the keyword is in the HashMap, increment the value
        }
        searchHistoryList.put(searchKeyword, frequency); //put the keyword and the frequency into the HashMap
        timestamp = String.valueOf(Instant.now());
        System.out.println("\n\tSearch terms: [" + searchKeyword + "],  Timestamp: [" + timestamp + "], cumulative frequency: [" + frequency + "]");
        String[] data = {searchKeyword, timestamp, String.valueOf(frequency)};
        historyDataList.add(data); //put keyWord, timestamp, and the frequency everytime the user searches
    }

    /* printCSVHistory()
     * input : List containing the term, the timestamp, and the frequency
     * output : write the list into a file
     * return : none
     * */
    public void printCSVHistory() {
        //reading existing data file
        try (BufferedReader bf = new BufferedReader(new FileReader(DATA_STORED_FILE))) {
            bf.readLine(); //skip the header
            String line;
            int idx = 0;
            System.out.println("\t---Search History---");
            //Get the existing data file for a new run
            while ((line = bf.readLine()) != null) {
                idx++; //set the row number
                String[] values = line.split(",");
                String term = values[0];
                String timestampVal = values[1];
                String frequencyVal = values[2];
                //print all the history that are stored in the file
                System.out.println("\t"+idx + ") term: "+ term + ", timestamp: " + timestampVal + ", cumulative frequency: " + frequencyVal);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
