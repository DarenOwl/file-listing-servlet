package filewebapp.services;

public class CatalogService {
    public static String ROOT_PATH = "C:/";
    public static final String HOME_DIR = "Julie/filewebapp/";

    public static String getPathPrefix()
    {
        return  ROOT_PATH + HOME_DIR;
    }
}
