# indiegogo-data-memory

CS622_Jisoo Lee_02.06.2023

1.Program name: indiegogo-data-memory

2.Purpose
  - Merging the data files into one file
  - Learning how to save the data (term, frequency, timestamp) in the memory and easily search the keywords
  - Prints out the searched result and saved data

3.Descriptions
  - Download some files from Indiegogo dataset (https://webrobots.io/indiegogo-dataset)
  - Make a directory : check if the directory folder exists
  - Merge files into one file (big file)
     1) Check the header column of the file : split a comma followed by a even numbers of quotes
    (checking if there are /n within the quotes)
     2) Get the files one by one and check the header column and merge
  - Search the keyword (user's input) and print it out. (No duplicates - used HashSet)
  - Show the saved memory (term, timestamp, frequency)

  4. Test Cases
```
  - Run/search keywords: watch(4), health(3), wearable(3), robot(6)
  - print the history
    example) this is the history file(csv)
    Search terms	Timestamp	Frequency
    robot	2023-02-03T15:46:44.430Z	1
    robot	2023-02-03T15:49:45.315Z	2
    robot	2023-02-03T15:50:23.434Z	3
    hello	2023-02-03T15:50:34.382Z	1
    hello	2023-02-03T15:50:44.758Z	2
    robot	2023-02-03T15:52:47.312Z	4
    wearable	2023-02-03T15:52:58.821Z	1
    fitness	2023-02-03T16:05:54.436Z	1
    watch	2023-02-06T03:43:03.120Z	1
    watch	2023-02-06T03:46:06.652Z	2
    y	2023-02-06T03:46:11.715Z	1
    health	2023-02-06T03:52:49.161Z	1
    watch	2023-02-06T20:32:04.467Z	3
    health	2023-02-06T20:32:31.576Z	2
    wearable	2023-02-06T20:32:48.210Z	2
    robot	2023-02-06T20:33:04.507Z	5
```
