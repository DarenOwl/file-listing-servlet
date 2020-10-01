package filewebapp.servlets;

import filewebapp.models.DirectoryContentModel;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

@WebServlet("/files")
public class MainServlet extends HttpServlet {
    private final int STREAM_BUFFER_SIZE = 255;
    private String HOME_PATH = null;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        HOME_PATH = System.getProperty("os.name").toLowerCase().startsWith("win") ? "C:/" : "/";
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //response.setContentType("text/html");

        String requestPath = request.getParameter("path");
        if (requestPath == null || requestPath.isEmpty())
            requestPath = HOME_PATH;

        Path path = Paths.get(requestPath);
        File file = path.toFile();
        if (!file.exists())
            response.sendRedirect(request.getServletPath());
        else if (file.isFile()) {
            download(request, response, file);
        } else {
            showDirectoryContent(request, response, path);
        }
    }

    private void download(HttpServletRequest request, HttpServletResponse response, File file) throws IOException {
        response.setContentType("application/octet-stream");
        response.setHeader("Content-disposition", "attachment; filename=" + file.getName());

        try(InputStream in = new FileInputStream(file);
            OutputStream out = response.getOutputStream()) {

            byte[] buffer = new byte[STREAM_BUFFER_SIZE];

            int bytesReadCount;
            while ((bytesReadCount = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesReadCount);
            }
        } catch (Exception ignored){

        }
    }

    private void showDirectoryContent(HttpServletRequest request, HttpServletResponse response, Path path) throws IOException, ServletException {
        request.setAttribute("directoryContent", new DirectoryContentModel(path));
        request.getRequestDispatcher("files.jsp").forward(request, response);
    }
}
