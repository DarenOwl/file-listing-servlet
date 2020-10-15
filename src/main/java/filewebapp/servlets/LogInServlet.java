package filewebapp.servlets;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;
import java.security.spec.KeySpec;
import java.util.Arrays;


@WebServlet("/login")
public class LogInServlet extends HttpServlet {

    @Override
    public void init(ServletConfig config) throws ServletException {

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        if (session.getAttribute("user") != null)
            response.sendRedirect("/files");
        else
            request.getRequestDispatcher("views/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String login = request.getParameter("login");
        String password = request.getParameter("password");

        if (login == null || login.equals("") || password == null || password.equals(""))
            return;

        try (BufferedReader passwordReader = new BufferedReader(new FileReader(new File("C:/users/Julie/" + login + "/password.txt")))){
            if (passwordIsOk(password,passwordReader.readLine())) {
                HttpSession session = request.getSession();
                session.setAttribute("user", login);

                response.sendRedirect("/files");
                return;
            }
        }

        request.setAttribute("errors", "неверный логин или пароль");
        request.getRequestDispatcher("views/login.jsp").forward(request, response);
    }

    private boolean passwordIsOk(String password, String storedHash) {
        String hash = null;
        KeySpec spec = new PBEKeySpec(password.toCharArray(),new byte[1], 65536, 128);
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            hash = Arrays.toString(factory.generateSecret(spec).getEncoded());
        } catch (Exception e) {
            e.printStackTrace();
        }

        assert hash != null;
        return hash.equals(storedHash);
    }
}
