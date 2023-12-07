import java.io.IOException;
import java.io.PrintWriter;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.catalina.startup.UserDatabase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.sql.PreparedStatement;

public class ControlServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private userDAO userDAO = new userDAO();
	private String currentUser;
	private int currentUser_id;
	private int currentRequest_id;
	
	private HttpSession session = null;

	public ControlServlet() {

	}

	public void init() {
		userDAO = new userDAO();
		currentUser = "";
		currentUser_id = -1;
		currentRequest_id = -1;
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
			case "/activityPage":
				backToActivity(request, response);
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
        		logout(request,response);
				break;
			case "/requestDetails":
				currentRequest_id = Integer.valueOf( request.getParameter("requestId") );
				requestDetails(request, response, "");
				break;
			case "/newMsg":
				newMsg(request, response, "normal msg");
				break;
			case "/newRequest":
				newRequest(request, response);
				break;
			case "/submitTreeInfo":
				newTree(request, response);
				break;
			case "/rejectRequest":
				rejectRequest(request, response, "");
				break;
			case "/acceptRequest":
				acceptRequest(request, response, "");
				break;
			case "/prepareQuote":
				requestDetails(request, response, "Prepare Quote");
				break;
			case "/prepareBill":
				requestDetails(request, response, "Prepare Bill");
				break;
			case "/preparePayment":
				requestDetails(request, response, "Prepare Payment");
				break;
			case "/prepareReject":
				requestDetails(request, response, "Prepare Reject");
				break;
			case "/sendQuote":
				quote(request, response, "");
				break;
			case "/sendBill":
				bill(request, response, "");
				break;
			case "/payBill":
				pay(request, response, "");
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

		request.setAttribute("requests", "\n<h3>Work Order  (Quote Accepted)</h3>\n"
							+ userDAO.listAdminTable("accepted")
							+"\n<h3>Waiting for Client To Accept the Quote</h3>\n" 
							+ userDAO.listAdminTable("Quote")
							+"<h3>Open requests</h3>" 
							+ userDAO.listAdminTable("Open")
							+"<h3>Done requests (open payment)</h3>" 
							+ userDAO.listAdminTable("Bill")
							+"<h3>Done requests (Paid)</h3>" 
							+ userDAO.listAdminTable("paid")
							+"<h3>Rejected requests</h3>" 
							+ userDAO.listAdminTable("Rejected")
							+"<h3>Cancelled requests</h3>" 
							+ userDAO.listAdminTable("Cancelled")
		);
		
		request.setAttribute("Big", userDAO.listBigClients());
		request.setAttribute("Easy", userDAO.listEasyClients());
		request.setAttribute("Good", userDAO.listGoodClients());
		request.setAttribute("Prospective", userDAO.listProspectiveClients());
		request.setAttribute("Highest Tree", userDAO.generate_MaxHeight_Tree_HTMLtable());
		
		
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
		} else if (email.equals("admin") && password.equals("123")) {
			currentUser = email;
			currentUser_id = userDAO.get_user_id(email);
			System.out.println("Login Successful! Redirecting to admin");
			session = request.getSession();
			session.setAttribute("username", email);
			adminPage(request, response, "");
		} else if (userDAO.isValid(email, password)) {
			System.out.println("Login Successful! Redirecting >>>>>>>>>>>>>");
			currentUser = email;
			currentUser_id = userDAO.get_user_id(email);
			
			request.setAttribute("histoty of requests", userDAO.listUserTable(currentUser_id));
			request.getRequestDispatcher("activitypage.jsp").forward(request, response);
			
		} else {
			request.setAttribute("loginStr", "Login Failed: Please check your credentials.");
			request.getRequestDispatcher("login.jsp").forward(request, response);
		}
	}
	
	protected void backToActivity(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, SQLException {
		
		if (userDAO.is_admin(currentUser_id)) {
			adminPage(request, response, "");
		}else {
			request.setAttribute("histoty of requests", userDAO.listUserTable(currentUser_id));
			request.getRequestDispatcher("activitypage.jsp").forward(request, response);
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
	
	private void requestDetails(HttpServletRequest request, HttpServletResponse response, String view) 
		throws ServletException, IOException, SQLException {
		
		if (userDAO.is_admin(currentUser_id))
			request.setAttribute("trees info", userDAO.listUserInfo(currentRequest_id)
											  +userDAO.listTreeTable(currentRequest_id));
		else
			request.setAttribute("trees info", userDAO.listTreeTable(currentRequest_id));
		
		request.setAttribute("History of Chats", userDAO.listChatTable(currentRequest_id, currentUser_id));
        
        if (view.equals("Prepare Quote")) {
        	request.setAttribute("msgBox", userDAO.prepareQuote(currentRequest_id));
        }else if (view.equals("Prepare Bill")){
        	request.setAttribute("msgBox", userDAO.prepareBill(currentRequest_id));
        }else if (view.equals("Prepare Payment")){
        	request.setAttribute("msgBox", userDAO.preparePayment(currentRequest_id, currentUser_id));
        }else if (view.equals("Prepare Reject")){
        	request.setAttribute("msgBox", userDAO.prepareReject(currentUser_id));
        }else {
	        String stat = userDAO.get_request_status(currentRequest_id);
	        if (stat.equals("rejected"))  
	        	request.setAttribute("msgBox", " Chat is Closed");
	        else if (stat.equals("bill"))
	        	request.setAttribute("msgBox", " Job is Done\n"+userDAO.msgBox(currentRequest_id, currentUser_id));
	        else if (stat.equals("done"))
	        	request.setAttribute("msgBox", userDAO.msgBox(currentRequest_id, currentUser_id));
	        else if (stat.equals("accepted"))
	        	request.setAttribute("msgBox", " Quote is accepted\n"+userDAO.msgBox(currentRequest_id, currentUser_id));
	        else 
	        	request.setAttribute("msgBox", userDAO.msgBox(currentRequest_id, currentUser_id));
        }
        
        
        request.getRequestDispatcher("requestDetails.jsp").forward(request, response);
	}
	
	private void newMsg(HttpServletRequest request, HttpServletResponse response, String status)
			throws ServletException, IOException, SQLException {
        
		String timeStamp = new SimpleDateFormat("yyyy-dd-MM HH:mm:ss").format(Calendar.getInstance().getTime());
        
//		int requestId = Integer.valueOf( request.getParameter("requestId") );
		String msg = request.getParameter("msg");
		String topic = "";
		if (status.equals("Quote") || status.equals("Bill"))
			topic = status;
		else
			topic = request.getParameter("topic");

		userDAO.insertNewMsg(currentRequest_id, currentUser_id,timeStamp, topic, msg);

		
		
		if (status.equals("normal msg"))
		requestDetails(request, response, "");
	}
	
	private void newRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, SQLException {
		
        request.getRequestDispatcher("newRequest.jsp").forward(request, response);
	}

	private void newTree(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, SQLException {
		       
        String timeStamp = new SimpleDateFormat("yyyy-dd-MM HH:mm:ss").format(Calendar.getInstance().getTime());
        
        String treeCountString = request.getParameter("treeCount");

		// Convert the string value to an integer if you need to perform numerical operations
		int treeCount = 0;
		if (treeCountString != null && !treeCountString.isEmpty()) {
		    try {
		        treeCount = Integer.parseInt(treeCountString);
		    } catch (NumberFormatException e) {
		        // Handle the exception if the string does not contain a parsable integer
		        e.printStackTrace();
		    }
		}
        
        
        userDAO.insertNewRequest(timeStamp, currentUser_id, treeCount);
        int request_id = userDAO.get_request_last_id();
        
        
        userDAO.insertNewMsg(request_id, currentUser_id, timeStamp, "Beginning", "The request Started with above information");
        
		
		
		for (int i=1;i<=treeCount;i++) {
			String p1 = request.getParameter("tree"+i+"_photo_1");
			String p2 = request.getParameter("tree"+i+"_photo_2");
			String p3 = request.getParameter("tree"+i+"_photo_3");
			String size = request.getParameter("tree"+i+"_size");
			String height = request.getParameter("tree"+i+"_height");
			String location = request.getParameter("tree"+i+"_location");
			String note = request.getParameter("tree"+i+"_note");
			
		    int sizeInt, heightInt;

		    try {
		        sizeInt = Integer.valueOf(size);
		    } catch (NumberFormatException e) {
		        sizeInt = 0; // Set to zero if there is an error in conversion
		    }

		    try {
		        heightInt = Integer.valueOf(height);
		    } catch (NumberFormatException e) {
		        heightInt = 0; // Set to zero if there is an error in conversion
		    }
			
			userDAO.insertNewTree(p1, p2, p3, sizeInt,heightInt, location, note, request_id);
		}
		
		
		request.setAttribute("histoty of requests", userDAO.listUserTable(currentUser_id));
		request.getRequestDispatcher("activitypage.jsp").forward(request, response);
		
	}
	
	private void rejectRequest(HttpServletRequest request, HttpServletResponse response, String view) 
			throws ServletException, IOException, SQLException {
			String timeStamp = new SimpleDateFormat("yyyy-dd-MM HH:mm:ss").format(Calendar.getInstance().getTime());
			String msg = request.getParameter("note");
			String status = "";
			
			if (userDAO.is_admin(currentUser_id))
				status = "Rejected";
			else
				status = "Cancelled";
			
			userDAO.insertNewMsg(currentRequest_id, currentUser_id, timeStamp, status, msg);
			userDAO.change_request_status(currentRequest_id, status);
	        requestDetails(request, response, view);
		}
	private void acceptRequest(HttpServletRequest request, HttpServletResponse response, String view) 
			throws ServletException, IOException, SQLException {
			
		String timeStamp = new SimpleDateFormat("yyyy-dd-MM HH:mm:ss").format(Calendar.getInstance().getTime());
        
	         
			String msg = "User accepted the request";
			String topic = "Acceptance";
			
			userDAO.insertNewMsg(currentRequest_id, currentUser_id,timeStamp, topic, msg);
			userDAO.change_request_status(currentRequest_id, "accepted");
	        requestDetails(request, response, view);
		}

	
	private void quote(HttpServletRequest request, HttpServletResponse response, String view) 
			throws ServletException, IOException, SQLException {
			String timeStamp = new SimpleDateFormat("yyyy-dd-MM HH:mm:ss").format(Calendar.getInstance().getTime());
			
			int tree_n = userDAO.update_tree_cost(request, currentRequest_id);
			String msg = "Cost is roughly";
			for (int i=1;i<=tree_n;i++) {
				String cost = request.getParameter("tree_"+i+"_cost");
				msg += " $"+cost+" for tree_"+i;
			}
			msg += " Note: " + request.getParameter("note");
			
			userDAO.insertNewMsg(currentRequest_id, currentUser_id, timeStamp, "Quote", msg);
	        userDAO.change_request_status(currentRequest_id, "quote");
	        
	         
	        requestDetails(request, response, view);
	}
	

	private void bill(HttpServletRequest request, HttpServletResponse response, String view) 
			throws ServletException, IOException, SQLException {
			String timeStamp = new SimpleDateFormat("yyyy-dd-MM HH:mm:ss").format(Calendar.getInstance().getTime());
			
			int tree_n = userDAO.update_tree_cost(request, currentRequest_id);
			int sum = 0;
			String msg = "Final Cost is";
			for (int i=1;i<=tree_n;i++) {
				String cost = request.getParameter("tree_"+i+"_cost");
				msg += " $"+cost+" for tree_"+i;
				sum += Integer.valueOf(cost);
			}
			String otherCosts = request.getParameter("other_costs");
			msg += " and $"+otherCosts+"for other costs.\n";
			sum += Integer.valueOf(otherCosts);
			msg += "Total= $"+sum;
			msg += " Due in one week. Note: " + request.getParameter("note");
			
			userDAO.insertNewMsg(currentRequest_id, currentUser_id, timeStamp, "Bill", msg);
	        userDAO.change_request_status(currentRequest_id, "bill");
	        userDAO.mark_trees_as_cut(request, currentRequest_id);
	        userDAO.update_request_bill(currentRequest_id, sum);
	        requestDetails(request, response, view);
	}
	private void pay(HttpServletRequest request, HttpServletResponse response, String view) 
			throws ServletException, IOException, SQLException {
		String msg = "";
		String topic = "";
		String timeStamp = new SimpleDateFormat("yyyy-dd-MM HH:mm:ss").format(Calendar.getInstance().getTime());
		int amount = Integer.valueOf(request.getParameter("payment_amount"));
		System.out.println("amount: "+amount+ "  id: "+currentRequest_id);
		
		int bill = userDAO.get_bill_RequestId(currentRequest_id);
		int remaining = bill - userDAO.get_paid_RequestId(currentRequest_id) - amount;
		if (remaining<=0) {
			amount += remaining;
			remaining = 0;
			userDAO.insertNewMsg(currentRequest_id, currentUser_id,timeStamp, "Paid", "User paid the full price");
		 	userDAO.change_request_status(currentRequest_id, "paid");
		}else {
			msg = "User paid $"+amount+ " Still $"+remaining+ " remains.";
			topic = "payment";
			userDAO.insertNewMsg(currentRequest_id, currentUser_id,timeStamp, topic, msg);
		}
		
		userDAO.update_request_paid(currentRequest_id, bill - remaining);		
		requestDetails(request, response, view);
	}
}






















