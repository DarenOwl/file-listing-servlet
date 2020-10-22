package filewebapp.servlets;

import filewebapp.services.CatalogService;
import filewebapp.services.UsersService;

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

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private UsersService usersService;

    @Override
    public void init(ServletConfig config) {
        usersService = new UsersService();
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
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        if (login == null || login.equals("") || password == null || password.equals(""))
            return;

        Path userHomePath = Paths.get(CatalogService.getPathPrefix() + login);

        if (usersService.loginRegistered(login) || Files.exists(userHomePath)) {
            request.setAttribute("errors", "пользователь с таким логином уже существует");
            request.getRequestDispatcher("views/register.jsp").forward(request, response);
            return;
        }

        if (usersService.emailRegistered(email)) {
            request.setAttribute("errors", "пользователь с таким email уже существует");
            request.getRequestDispatcher("views/register.jsp").forward(request, response);
            return;
        }

        if (usersService.register(login, email, password)) {
            Files.createDirectory(userHomePath);

            HttpSession session = request.getSession();
            session.setAttribute("user", login);
            response.sendRedirect("/files");
        } else {
            request.setAttribute("errors", "при регистрации возникла ошибка");
            request.getRequestDispatcher("views/register.jsp").forward(request, response);
        }
    }
}
