import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Map;

public class DeleteSync {

    // Deletes the original media file corresponding to the given copy name and then removes that mapping from index.json.
    public static void deleteOriginalAndSyncIndex(File allPhoVidFolder, String copyFileName) throws IOException {
        // 1) Load existing index.json
        Path indexPath = allPhoVidFolder.toPath().resolve("index.json");
        Type mapType = new TypeToken<Map<String,String>>(){}.getType();
        Map<String,String> indexMap;
        try (Reader reader = Files.newBufferedReader(indexPath)) {
            indexMap = new Gson().fromJson(reader, mapType);
        }

        // 2) Find and delete the original
        String originalPath = indexMap.get(copyFileName);
        if (originalPath != null) {
            File originalFile = new File(originalPath);
            if (originalFile.exists()) {
                boolean deleted = originalFile.delete();
                System.out.println(deleted
                        ? "🗑️ Deleted original: " + originalPath
                        : "⚠️ Failed to delete original: " + originalPath);
            } else {
                System.out.println("⚠️ Original not found: " + originalPath);
            }
            // 3) Remove from index and rewrite
            indexMap.remove(copyFileName);
            try (Writer writer = Files.newBufferedWriter(indexPath, StandardOpenOption.TRUNCATE_EXISTING)) {
                new GsonBuilder().setPrettyPrinting().create().toJson(indexMap, writer);
                System.out.println("📑 Updated index.json");
            }
        } else {
            System.out.println("❌ No mapping for copy: " + copyFileName);
        }
    }
}
