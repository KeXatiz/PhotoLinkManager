///*
//information
//(i1) - File Class - https://www.w3schools.com/java/java_files.asp  - (CRUD file)
//(i2) - Scanner - https://www.w3schools.com/java/java_user_input.asp
//(i3) - Constructor Overloading - https://www.geeksforgeeks.org/java/constructor-overloading-java/
//(i4) - Array - https://www.w3schools.com/java/java_arrays.asp
//(i5) - Recurssion - //https://www.w3schools.com/java/java_recursion.asp
//(i6) - File>.listFiles() - https://www.geeksforgeeks.org/java/file-listfiles-method-in-java-with-examples/


import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Main {
    public static void main(String[] args) throws IOException { //throw IOException - so that we dont have to wrap every file operation in a try/catch







        Scanner scanner = new Scanner(System.in);

        // 1) Get and validate root folder
        File inputRoot;
        while (true) {
            System.out.print("Enter root folder path: ");
            inputRoot = new File(scanner.nextLine().trim());  //nextLine() - read user input | trim()- whenever you need to drop unwanted spaces or control characters at the edges of your text "  Hello  "
            if (!inputRoot.exists() || !inputRoot.isDirectory()) { // !isDirectory()	Make sure the path is really a folder
                System.out.println("❌ Invalid path. Try again.\n");
            } else {
                System.out.println("✅ Scanning from: " + inputRoot.getAbsolutePath()); //getAbsolutePath() show the complete location
                break;
            }
        }

        // 2) Prepare AllPhoVid output folder
        File allPhoVid = inputRoot.getName().equalsIgnoreCase("AllPhoVid") ? inputRoot : new File(inputRoot, "AllPhoVid"); //rootFolder.getName() -  last folder name in the path   |  .equalsIgnoreCase("AllPhoVid") - checks if the folder name is "AllPhoVid", ignoring upper/lower case | If the folder the user typed is not already named AllPhoVid, then we’ll add /AllPhoVid at the end
        if (!allPhoVid.exists() && !allPhoVid.mkdirs()) {
            System.err.println("❌ Cannot create output folder: " + allPhoVid);
            return;
        }

        // 3) Extensions to match
        String[] exts = {".jpg", ".jpeg", ".png", ".heif", ".mov", ".mp4", ".gif", ".webp"};

        // 4) Map for index.json: copyName → originalAbsolutePath
        Map<String,String> indexMap = new LinkedHashMap<>();

        // 5) Scan & copy
        System.out.println("\n🔍 Scanning & copying media...");
        copyMediaRecursively(inputRoot, allPhoVid, exts, indexMap);

        // 6) Write index.json
        Path indexFile = Paths.get(allPhoVid.getAbsolutePath(), "index.json");
        try (var writer = Files.newBufferedWriter(indexFile)) {
            new GsonBuilder().setPrettyPrinting().create().toJson(indexMap, writer);
            System.out.println("\n📑 index.json created with " + indexMap.size() + " entries");
        }

        // ─── 7) DELETE-SYNC PROMPT ───────────────────────────
        System.out.println("\n✅ All files copied into: "
                + allPhoVid.getAbsolutePath());
        System.out.print("Enter filenames to delete (separated by space or comma), or blank to exit: ");
        String line = scanner.nextLine().trim();

// if they entered nothing, exit
        if (!line.isEmpty()) {
            // split on commas or whitespace
            String[] names = line.split("[,\\s]+");
            for (String toDelete : names) {
                File copyFile = new File(allPhoVid, toDelete);
                if (copyFile.exists() && copyFile.delete()) {
                    System.out.println("🗑️ Deleted copy: " + toDelete);
                    // sync delete of original
                    DeleteSync.deleteOriginalAndSyncIndex(allPhoVid, toDelete);
                } else {
                    System.out.println("⚠️ Could not delete copy: " + toDelete);
                }
            }
        }
        scanner.close();
    }

    private static void copyMediaRecursively(
            File dir, //input root
            File outputDir, //allphovid folder
            String[] extensions,
            Map<String,String> indexMap
    ) throws IOException {
        File[] entries = dir.listFiles();
        if (entries == null) return;

        for (File entry : entries) {
            // Skip the output folder itself
            if (entry.equals(outputDir)) continue;

            if (entry.isDirectory()) {
                copyMediaRecursively(entry, outputDir, extensions, indexMap);
            } else {
                String name = entry.getName().toLowerCase();
                for (String ext : extensions) {
                    if (name.endsWith(ext)) {
                        // Copy file
                        Path src = entry.toPath();
                        // Ensure unique filename in AllPhoVid
                        Path dest = uniqueDestination(outputDir.toPath(), entry.getName());
                        Files.copy(src, dest, StandardCopyOption.COPY_ATTRIBUTES);
                        System.out.println("📋 Copied: " + entry.getName());

                        // Record in index
                        indexMap.put(dest.getFileName().toString(), src.toAbsolutePath().toString());
                        break;
                    }
                }
            }
        }
    }

    /** If a file named 'foo.jpg' already exists in the output folder,
     *  will return 'foo(1).jpg', 'foo(2).jpg', etc. */
    private static Path uniqueDestination(Path folder, String fileName) {
        Path dest = folder.resolve(fileName);
        if (!Files.exists(dest)) return dest;

        String base = fileName, ext = "";
        int dot = fileName.lastIndexOf('.');
        if (dot > 0) {
            base = fileName.substring(0, dot);
            ext = fileName.substring(dot);
        }

        int count = 1;
        while (true) {
            Path candidate = folder.resolve(base + "(" + count + ")" + ext);
            if (!Files.exists(candidate)) return candidate;
            count++;
        }
    }

}
