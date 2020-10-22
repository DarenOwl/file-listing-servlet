package filewebapp.servlets;

import filewebapp.services.UsersService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;


@WebServlet("/login")
public class LogInServlet extends HttpServlet {
    private UsersService usersService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        usersService = new UsersService();
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

        if (usersService.login(login, password)) {
            HttpSession session = request.getSession();
            session.setAttribute("user", login);
            response.sendRedirect("/files");
            return;
        }

        request.setAttribute("errors", "неверный логин или пароль");
        request.getRequestDispatcher("views/login.jsp").forward(request, response);
    }
}
