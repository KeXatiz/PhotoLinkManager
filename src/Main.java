/*
information
(i1) - File Class - https://www.w3schools.com/java/java_files.asp  - (CRUD file)
(i2) - Scanner - https://www.w3schools.com/java/java_user_input.asp
(i3) - Constructor Overloading - https://www.geeksforgeeks.org/java/constructor-overloading-java/
*/

import java.io.File;                                                                                                    //import the File class
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        //set the output SSD external Path
        //String outputPath = "E:/A-Phone/Amir's S24 Ultra/Internal storage/AllPhoVid"; //java didnt support "/" hence we use "\"

        boolean pathExist = false;
        File rootFolder = null;

        while (!pathExist){                                                                                             //loop if path is false
            Scanner scanner = new Scanner(System.in);                                                                   //create a scanner object
            System.out.print("Enter root folder path: ");                                                               // - it will promt folder already exist
            String inputPath = scanner.nextLine();                                                                      //read user input

            //File rootFolder = new File(inputPath);
            rootFolder = new File(inputPath);

            //check if the root exist
            if(!rootFolder.exists() || !rootFolder.isDirectory()){                                                      // !isDirectory()	Make sure the path is really a folder
                System.out.println("Invalid path. Please check the folder and try again. \n");
            } else {
                pathExist = true;
                System.out.println("Valid path: " + rootFolder.getAbsolutePath());                                      //getAbsolutePath() show the complete location
            }
        }

        //avoid C:/.../AllPhoVid/AllPhoVid
        if(!rootFolder.getName().equalsIgnoreCase("AllPhoVid")){                                             //rootFolder.getName() -  last folder name in the path   |  .equalsIgnoreCase("AllPhoVid") - checks if the folder name is "AllPhoVid", ignoring upper/lower case | If the folder the user typed is not already named AllPhoVid, then we’ll add /AllPhoVid at the end
            rootFolder = new File(rootFolder, "AllPhoVid");                                                        //Constructor Overload in the File class | File(File parent, String child)
        }


        if(!rootFolder.exists()){                                                                                       //checking if the path exists
            boolean created = rootFolder.mkdirs();                                                                      //mkdirs() - creates a directory  | allPhoVidFolder.mkdirs(); will perform and the result success or fail will store @ created
            System.out.println(created ? "Folder AllPhoVid created successfully on SSD" : "Failed to create folder, check ssd connection or permission");
        } else {
            System.out.println("Folder already exist");
        }
    }
}
