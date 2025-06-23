/*
information
(i1) - File Class - https://www.w3schools.com/java/java_files.asp  - (CRUD file)

*/

import java.io.File; //import the File class

public class Main {
    public static void main(String[] args) {
        //set the output SSD external Path
        String outputPath = "E:/A-Phone/Amir's S24 Ultra/Internal storage/AllPhoVid"; //java didnt support "/" hence we use "\"

        //creating File object for the folder
        File allPhoVidFolder = new File(outputPath);  //Class Object = new Class()

        if(!allPhoVidFolder.exists()){  //checking if the path exists
            boolean created = allPhoVidFolder.mkdirs();  //mkdirs() - creates a directory  | allPhoVidFolder.mkdirs(); will perform and the result success or fail will store @ created
            if(created){
                System.out.println("Folder AllPhoVid created successfully on SSD");
            } else {
                System.out.println("Failed to create folder, check ssd connection or permission");
            }
        } else {
            System.out.println("Folder already exist " + outputPath);
        }
    }
}
