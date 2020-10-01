package filewebapp.models;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DirectoryContentModel {
    public final String dateGenerated;
    public final String path;
    public final String previousPath;
    public final List<FileModel> files;

    public DirectoryContentModel(Path directoryPath) throws IOException {
        dateGenerated = new SimpleDateFormat("MM.dd.yyyy HH:mm:ss").format(new Date());
        path = directoryPath.toString();
        if (directoryPath.getParent() == null)
            previousPath = null;
        else
            previousPath = directoryPath.getParent().toString();
        files = getFiles(directoryPath);
    }

    private List<FileModel> getFiles(Path directoryPath) throws IOException {
        List<FileModel> files = new ArrayList<>();
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(directoryPath))
        {
            for (Path path : directoryStream) {
                File file = path.toFile().getCanonicalFile();
                if (file.isHidden() || isJunction(path))
                    continue;
                files.add(new FileModel(file));
            }
        }
        return files;
    }

    //https://stackoverflow.com/questions/13733275/determine-whether-a-file-is-a-junction-in-windows-or-not/29699473
    //определяет, является ли файл символической ссылкой Junction
    //а другие красивые способы определить символическую ссылку с Junction не работают
    //toRealPath getCanonicalPath и getAbsolutePath тоже не работают
    private boolean isJunction(Path p) {
        boolean isJunction = false;
        try {
            isJunction = (p.compareTo(p.toRealPath()) != 0);
        } catch (IOException e) {
            e.printStackTrace(); // TODO: handleMeProperly
        }
        return isJunction;
    }
}
