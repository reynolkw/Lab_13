import javax.swing.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

import static java.nio.file.StandardOpenOption.CREATE;

public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        ArrayList<String> myList = new ArrayList<>();

        ArrayList<String> MENU = new ArrayList<>();
        MENU.add("O - Open a list file from disk");
        MENU.add("S - Save the current file to disk");
        MENU.add("A - Add an item to the list");
        MENU.add("D – Delete an item from the list");
        MENU.add("C - Remove all elements from the current list");
        MENU.add("V – View the list");
        MENU.add("Q – Quit the program");

        String MENU_HEADER = "---- List Maker Menu ----";
        String LIST_HEADER = "----- My To Do List -----";

        boolean isDirty = false;
        String activeListName = "";

        // Constantly loops until the user quits the program
        do {
            // Get menu choice from the user
            displayMenu(MENU, MENU_HEADER);
            String cmd = SafeInput.getRegExString(in,"Enter your menu choice","[OoSsAaDdCcVvQq]").toUpperCase();

            switch (cmd){
                // Open a list file from disk
                case("O"):
                    // prevents data loss from unsaved lists in memory
                    if (isDirty) {
                        System.out.println("Warning! Unsaved list items will be lost.");
                        boolean saveList = SafeInput.getYNConfirm(in, "Would you like to save?");
                        if (saveList) {
                            // save list
                            saveListFile(myList, activeListName, in);
                            // reset changes flag
                            isDirty = false;
                        }
                        // discard list
                    }
                    // get file selection from user
                    System.out.println("Check JFileChooser dialog for file selection");
                    Path filePath = getListFilePath();
                    // load list into memory
                    activeListName = removeExtension(filePath.getFileName().toString());
                    myList = openListFile(filePath);
                    // display list to user
                    displayArrayList(myList, LIST_HEADER);
                    break;
                // save the current list to a file
                case("S"):
                    // only allows the list to be saved if changes have been made
                    if (isDirty) {
                        // save list
                        saveListFile(myList, activeListName, in);
                        // reset changes flag
                        isDirty = false;
                    }
                    break;
                // Add a list item
                case("A"):
                    // prompt user for list item
                    System.out.print("Enter the list item: ");
                    String listItem = in.nextLine();
                    // add list item to the list
                    myList.add(listItem);
                    // update changes flag to require save warnings
                    isDirty = true;
                    // display list with added list item
                    displayArrayList(myList, LIST_HEADER);
                    break;
                // Delete a list item
                case("D"):
                    // cannot delete if there are no list items
                    if (!myList.isEmpty()){
                        // prompt user for list item index to delete
                        int delIndex = SafeInput.getRangedInt(in, "Enter the index of the list item to delete",1, myList.size()) - 1;
                        // remove list item
                        myList.remove(delIndex);
                        // update changes flag to require save warnings
                        isDirty = true;
                        // display list without deleted list item
                        displayArrayList(myList, LIST_HEADER);
                    } else System.out.print("No items to delete\n\n");
                    break;
                // Clear all list items from the active list
                case("C"):
                    // get user confirmation
                    boolean confirm = SafeInput.getYNConfirm(in,"Are you sure you want to clear all items?");
                    if (confirm) {
                        // clear the list
                        myList.clear();
                        // update changes flag to require save warnings
                        isDirty = true;
                    }
                    break;
                // Print the list
                case("V"):
                    displayArrayList(myList, LIST_HEADER);
                    break;
                // Quit the program
                case("Q"):
                    // allows user to decide whether to save changes to disk
                    if (isDirty) {
                        System.out.println("Warning! Unsaved list items will be lost.");
                        boolean saveList = SafeInput.getYNConfirm(in, "Would you like to save?");
                        if (saveList)
                            saveListFile(myList, activeListName, in);
                    }
                    // user confirms exit
                    boolean shouldExit = SafeInput.getYNConfirm(in,"Are you sure you want to quit?");
                    if (shouldExit)
                        System.exit(0);
            }
        } while(true);
    }
    /**
     * prints out to console string menu items
     * @param MENU list of menu items, each menu item is one element in the ArrayList
     * @param MENU_HEADER the text header above the menu items
     */
    private static void displayMenu(ArrayList<String> MENU, String MENU_HEADER){
        System.out.println(MENU_HEADER);
        MENU.forEach(System.out::println);
        System.out.println();
    }
    /**
     * prints out to console a numbered list of ArrayList elements with a header
     * @param arrayList ArrayList of elements to be logged to the console
     * @param LIST_HEADER The text header above the numbered list of elements
     */
    private static void displayArrayList(ArrayList<String> arrayList, String LIST_HEADER){
        System.out.println();
        System.out.println(LIST_HEADER);
        for (int i = 0; i < arrayList.size(); i++){
            System.out.printf(" %2d. %s\n", i + 1, arrayList.get(i));
        }
        System.out.println();
    }
    /**
     * uses JFileChooser dialog to get user file selection
     * @return Path of the file the user wishes to load
     */
    private static Path getListFilePath() {
        // target JFileChooser to program directory
        JFileChooser dialog = new JFileChooser();
        File dialogTarget = new File(System.getProperty("user.dir"));
        dialog.setCurrentDirectory(dialogTarget);

        // open JFileChooser and return user selected Path
        File selectedFile = null;
        if (dialog.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            selectedFile = dialog.getSelectedFile();
        }
        assert selectedFile != null;
        return selectedFile.toPath();
    }
    /**
     * returns myList from provided text file
     * @param selectedFilePath Path object for saved myList file
     * @return myList ArrayList from the provided text file
     */
    private static ArrayList<String> openListFile(Path selectedFilePath) {
        // init return ArrayList
        ArrayList<String> myList = new ArrayList<>();
        try {
            // boilerplate file input
            InputStream in = new BufferedInputStream(Files.newInputStream(selectedFilePath, CREATE));
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            // read each line into a new ArrayList element
            while (reader.ready()){
                String record = reader.readLine();
                myList.add(record);
            }
            // close file and clear buffer
            reader.close();
        // boilerplate error handling from support videos
        } catch (FileNotFoundException e){
            System.out.println("ERROR: File not found.");
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
        return myList;
    }
    /**
     * saves myList ArrayList into a text file in the project directory
     * @param myList active myList ArrayList to be saved
     * @param activeListName the String name of the list file
     * @param in standard scanner in for user input
     */
    private static void saveListFile(ArrayList<String> myList, String activeListName, Scanner in) {
        // target JFileChooser to program directory
        File userDir = new File(System.getProperty("user.dir"));
        // get list file name from user
        Path file;
        boolean isComplete = false;
        // no list name means the list has not been loaded from disk and should be saved new
        if (activeListName.isEmpty()) {
            do {
                // prompt user for file name
                System.out.print("Enter the list filename: ");
                String fileName = in.nextLine();
                // create path object for file
                file = Paths.get(userDir.getPath() + "\\lists\\" + fileName + ".txt");
                // prevent overwriting existing files
                if (Files.exists(file)) {
                    System.out.println("A file with the file name already exists. Enter another file name.");
                } else {
                    isComplete = true;
                }
            } while (!isComplete);
        } else {
            file = Paths.get(userDir.getPath() + "\\lists\\" + activeListName + ".txt");
            // clears the contents of the original file to prepare for saving the list changes
            try {
                // Use the Files.delete method to delete the file
                Files.delete(file);
            } catch (Exception e) {
                // Handle exceptions, such as file not found or permission issues
                System.err.println("Failed to delete the file: " + e.getMessage());
            }
        }
        // write the file
        try {
            // boilerplate file output init
            OutputStream out = new BufferedOutputStream(Files.newOutputStream(file, CREATE));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));
            // each ArrayList item is written onto a newline in the text file
            for (String listItem : myList) {
                writer.write(listItem, 0, listItem.length());
                writer.newLine();
            }
            writer.close();
        // boilerplate error handling from support videos
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * removes the file extension from a String file name
     * @param fileNameWithExtension provided full file name
     * @return base file name
     */
    private static String removeExtension(String fileNameWithExtension) {
        int lastDotIndex = fileNameWithExtension.lastIndexOf('.');
        if (lastDotIndex > 0) {
            return fileNameWithExtension.substring(0, lastDotIndex);
        } else {
            // If there is no dot, or it's the first character, return the original string
            return fileNameWithExtension;
        }
    }
}