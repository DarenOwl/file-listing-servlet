package filewebapp.servlets;

import filewebapp.models.DirectoryContentModel;
import filewebapp.services.CatalogService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import java.io.*;
import java.net.CookieManager;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@WebServlet("/files")
public class CatalogServlet extends HttpServlet {
    private final int STREAM_BUFFER_SIZE = 255;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        CatalogService.ROOT_PATH = System.getProperty("os.name").toLowerCase().startsWith("win") ? "C:/users/" : "/home/";

        if (Files.notExists(Paths.get(CatalogService.getPathPrefix()))) {
            throw new ServletException("Servlet can't access configured Home Directory: " + CatalogService.getPathPrefix());
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        if (session.getAttribute("user") == null) {
            request.getRequestDispatcher("views/login.jsp").forward(request, response);
            return;
        }

        String username = (String) session.getAttribute("user");

        String requestPath = request.getParameter("path");
        if (requestPath == null || requestPath.isEmpty())
            requestPath = CatalogService.getPathPrefix();

        Path path = Paths.get(requestPath);
        if (!path.startsWith(CatalogService.getPathPrefix() + username)) {
            response.sendRedirect("/files?path=" + CatalogService.getPathPrefix() + username);
            return;
        }

        File file = path.toFile();
        if (!file.exists()) {
            session.removeAttribute("user");
            request.getRequestDispatcher("views/login.jsp").forward(request, response);
        }
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
        request.getRequestDispatcher("views/files.jsp").forward(request, response);
    }
}
