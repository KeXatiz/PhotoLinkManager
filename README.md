This application helps manage media files (images, videos, GIFs, etc.) from multiple folders by consolidating them into a single directory for easier access and organisation.

**exFAT Limitation**
When using an external SSD formatted with exFAT, symbolic links are not supported (unlike NTFS).
To handle this limitation:
- The application currently creates duplicate files instead of symbolic links
- This ensures compatibility across exFAT systems, while still allowing centralised file management

**Features**
- Recursively scans directories for media files
- Consolidates files into a single folder (AllPhoVid)
- Supports multiple file types (images, videos, GIFs, etc.)
- Handles file duplication and unique file naming
- Watches file changes and syncs deletions with the original location

**How to Use**
1. Run Main
Execute Main.java
Enter the root directory path when prompted
The application will:
- Scan all subfolders
- Create an AllPhoVid folder
- Copy all detected media files into this folder
- You can now manage your media files in one place instead of multiple directories.

2. Run AllPhoVidWatcher
Execute AllPhoVidWatcher.java
This will monitor the AllPhoVid folder
If a file is deleted from AllPhoVid:
The application will also delete the original file from its source path
Example: DCIM/Camera/abc.jpg

**Notes**
Designed for external SSD usage (especially exFAT format)
Current implementation uses duplication instead of symbolic links
Best suited for personal media organisation and cleanup workflows

**Tech Stack**
Java
File I/O / NIO
Recursive directory traversal
File watching (observer pattern)
