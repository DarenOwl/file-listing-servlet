package filewebapp.servlets;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.spec.KeySpec;
import java.util.Arrays;


@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    @Override
    public void init(ServletConfig config) throws ServletException {

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        if (session.getAttribute("user") != null)
            response.sendRedirect("/");
        else
            request.getRequestDispatcher("views/register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String login = request.getParameter("login");
        String password = request.getParameter("password");

        if (login == null || login.equals("") || password == null || password.equals(""))
            return;

        Path path = Paths.get("C:/users/Julie/" + login);
        if (Files.exists(path)) {
            request.getRequestDispatcher("views/login.jsp").forward(request, response);
            return;
        }
        Files.createDirectory(path);

        try (FileWriter passwordWriter = new FileWriter(Files.createFile(path.resolve("password.txt")).toFile());){
            passwordWriter.write(getPasswordHash(password));
        }

        HttpSession session = request.getSession();
        session.setAttribute("user", login);

        response.sendRedirect("/files");
    }

    private String getPasswordHash(String password) {
        KeySpec spec = new PBEKeySpec(password.toCharArray(),new byte[1], 65536, 128);
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            return Arrays.toString(factory.generateSecret(spec).getEncoded());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
