import java.io.IOException;
import java.io.PrintWriter;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.PreparedStatement;

public class ControlServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private userDAO userDAO = new userDAO();
	private String currentUser;
	private HttpSession session = null;

	public ControlServlet() {

	}

	public void init() {
		userDAO = new userDAO();
		currentUser = "";
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getServletPath();
		System.out.println(action);

		try {
			switch (action) {
			case "/login":
				login(request, response);
				break;
			case "/register":
				register(request, response);
				break;
			case "/initialize":
				userDAO.init();
				System.out.println("Database successfully initialized!");
				rootPage(request, response, "");
				break;
			case "/root":
				rootPage(request, response, "");
				break;
			case "/admin":
				adminPage(request, response, "");
				break;
			case "/logout":
//        		logout(request,response);
				break;
			}
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	private void rootPage(HttpServletRequest request, HttpServletResponse response, String view)
			throws ServletException, IOException, SQLException {
		System.out.println("root view");
		request.setAttribute("list of users", userDAO.listAllTable("user"));
		request.setAttribute("list of requests", userDAO.listAllTable("requests"));
		request.setAttribute("list of trees", userDAO.listAllTable("trees"));
		request.setAttribute("History of Chats", userDAO.listAllTable("chats"));

		request.getRequestDispatcher("rootView.jsp").forward(request, response);
	}

	private void adminPage(HttpServletRequest request, HttpServletResponse response, String view)
			throws ServletException, IOException, SQLException {
		System.out.println("admin view");

		request.getRequestDispatcher("adminView.jsp").forward(request, response);
	}

	protected void login(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, SQLException {
		String email = request.getParameter("email");
		String password = request.getParameter("password");

		if (email.equals("root") && password.equals("pass1234")) {
			System.out.println("Login Successful! Redirecting to root");
			session = request.getSession();
			session.setAttribute("username", email);
			rootPage(request, response, "");
		} else if (email.equals("admin") && password.equals("admin")) {
			System.out.println("Login Successful! Redirecting to admin");
			session = request.getSession();
			session.setAttribute("username", email);
			adminPage(request, response, "");
		} else if (userDAO.isValid(email, password)) {
			currentUser = email;
			System.out.println("Login Successful! Redirecting");
			request.getRequestDispatcher("activitypage.jsp").forward(request, response);

		} else {
			request.setAttribute("loginStr", "Login Failed: Please check your credentials.");
			request.getRequestDispatcher("login.jsp").forward(request, response);
		}
	}

	private void register(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, SQLException {
		String email = request.getParameter("email");
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String password = request.getParameter("password");
		String adress_street_num = request.getParameter("adress_street_num");
		String adress_street = request.getParameter("adress_street");
		String adress_city = request.getParameter("adress_city");
		String adress_state = request.getParameter("adress_state");
		String adress_zip_code = request.getParameter("adress_zip_code");

		String phone_number = request.getParameter("phone_number");
		String credit_card_no = request.getParameter("credit_card_no");
		int credit_card_cvv = Integer.valueOf(request.getParameter("credit_card_cvv"));
		int credit_card_ex_year = Integer.valueOf(request.getParameter("credit_card_ex_year"));
		int credit_card_ex_month = Integer.valueOf(request.getParameter("credit_card_ex_month"));

		String confirm = request.getParameter("confirmation");

		if (password.equals(confirm)) {
			if (!userDAO.checkEmail(email)) {
				if (!userDAO.checkAddress(adress_street_num, adress_street, adress_city, adress_state,
						adress_zip_code)) {
					System.out.println("Registration Successful! Added to database");
					user users = new user(email, firstName, lastName, password, adress_street_num, adress_street,
							adress_city, adress_state, adress_zip_code, phone_number, credit_card_no, credit_card_cvv,
							credit_card_ex_year, credit_card_ex_month);
					userDAO.insert(users);
					response.sendRedirect("login.jsp");
				} else {
					request.setAttribute("errorThree", "Registration failed: Duplicate Address.");
					request.getRequestDispatcher("register.jsp").forward(request, response);
				}

			} else {
				request.setAttribute("errorOne", "Registration failed: Username taken, please enter a new username.");
				request.getRequestDispatcher("register.jsp").forward(request, response);
			}
		} else {
			System.out.println("Password and Password Confirmation do not match");
			request.setAttribute("errorTwo", "Registration failed: Password and Password Confirmation do not match.");
			request.getRequestDispatcher("register.jsp").forward(request, response);
		}
	}

	private void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
		currentUser = "";
		response.sendRedirect("login.jsp");
	}

}
