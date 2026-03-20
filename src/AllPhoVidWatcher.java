import java.io.File;
import java.io.IOException;
import java.nio.file.*;

public class AllPhoVidWatcher {
    public static void main(String[] args) throws IOException, InterruptedException {
        if (args.length < 1) {
            System.err.println("Usage: java AllPhoVidWatcher <AllPhoVid_folder_path>");
            return;
        }

        Path watchDir = Paths.get(args[0]);
        File allPhoVidFolder = watchDir.toFile();
        if (!allPhoVidFolder.exists() || !allPhoVidFolder.isDirectory()) {
            System.err.println("Invalid folder: " + args[0]);
            return;
        }

        System.out.println("🔍 Watching for deletions in: " + watchDir);

        // 1) Create the WatchService
        WatchService watcher = FileSystems.getDefault().newWatchService();
        watchDir.register(watcher, StandardWatchEventKinds.ENTRY_DELETE);

        // 2) Loop forever, waiting for delete events
        while (true) {
            WatchKey key = watcher.take(); // blocks until an event occurs
            for (WatchEvent<?> event : key.pollEvents()) {
                if (event.kind() == StandardWatchEventKinds.ENTRY_DELETE) {
                    String fileName = event.context().toString();
                    System.out.println("🗑️ Detected delete: " + fileName);
                    try {
                        // 3) Sync-delete the original
                        DeleteSync.deleteOriginalAndSyncIndex(allPhoVidFolder, fileName);
                    } catch (Exception e) {
                        System.err.println("Error syncing delete for: " + fileName);
                        e.printStackTrace();
                    }
                }
            }
            // reset the key to receive further events
            boolean valid = key.reset();
            if (!valid) break; // folder no longer accessible
        }
    }
}
