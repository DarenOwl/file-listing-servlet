package filewebapp.models;

import java.io.File;
import java.text.SimpleDateFormat;

public class FileModel {
    public final boolean isDirectory;
    public final String path;
    public final String name;
    public final long sizeInBytes;
    public final String lastModifiedDate;

    public FileModel(File file) {
        this.isDirectory = file.isDirectory();
        this.path = file.getPath();
        this.name = file.getName();
        this.sizeInBytes = file.length();
        this.lastModifiedDate = new SimpleDateFormat("MM.dd.yyyy HH:mm:ss").format(file.lastModified());
    }
}
