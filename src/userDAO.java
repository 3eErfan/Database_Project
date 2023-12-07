import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.PreparedStatement;
//import java.sql.Connection;
//import java.sql.PreparedStatement;
import java.sql.ResultSet;
//import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Servlet implementation class Connect
 */
@WebServlet("/userDAO")
public class userDAO {
	private static final long serialVersionUID = 1L;
	private Connection connect = null;
	private Statement statement = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;
	private static final Set<String> ALLOWED_COLUMN_NAMES = new HashSet<>(Arrays.asList(
		    "email",
		    "firstName",
		    "lastName",
		    "password",
		    "adress_street_num",
		    "adress_street",
		    "adress_city" ,
		    "adress_state",
		    "adress_zip_code",
		    "phonenumber",
		    "credit_no",
		    "credit_cvv",
		    "credit_year",
		    "credit_month"
		));

		private String getSafeColumnName(String columnName) {
		    if (ALLOWED_COLUMN_NAMES.contains(columnName)) {
		        return columnName;
		    } else {
		        throw new IllegalArgumentException("Invalid column name: " + columnName);
		    }
		}
	
	
	public userDAO() {
	}

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	protected void connect_func() throws SQLException {
		// uses default connection to the database
		if (connect == null || connect.isClosed()) {
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
			} catch (ClassNotFoundException e) {
				throw new SQLException(e);
			}
			connect = (Connection) DriverManager.getConnection(
					"jdbc:mysql://127.0.0.1:3306/testdb?allowPublicKeyRetrieval=true&useSSL=false&user=John&password=pass1234");
			System.out.println(connect);
		}
	}

	public boolean database_login(String email, String password) throws SQLException {
		try {
			connect_func("root", "pass1234");
			String sql = "select * from user where email = ?";
			preparedStatement = connect.prepareStatement(sql);
			preparedStatement.setString(1, email);
			ResultSet rs = preparedStatement.executeQuery();
			return rs.next();
		} catch (SQLException e) {
			System.out.println("failed login");
			return false;
		}
	}

	// connect to the database
	public void connect_func(String username, String password) throws SQLException {
		if (connect == null || connect.isClosed()) {
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
			} catch (ClassNotFoundException e) {
				throw new SQLException(e);
			}
			connect = (Connection) DriverManager.getConnection(
					"jdbc:mysql://127.0.0.1:3306/userdb?" + "useSSL=false&user=" + username + "&password=" + password);
			System.out.println(connect);
		}
	}

	protected void disconnect() throws SQLException {
		if (connect != null && !connect.isClosed()) {
			connect.close();
		}
	}

	public boolean isValid(String email, String password) throws SQLException {
		String sql = "SELECT * FROM User";
		connect_func();
		statement = (Statement) connect.createStatement();
		ResultSet resultSet = statement.executeQuery(sql);

		resultSet.last();

		int setSize = resultSet.getRow();
		resultSet.beforeFirst();

		for (int i = 0; i < setSize; i++) {
			resultSet.next();
			if (resultSet.getString("email").equals(email) && resultSet.getString("password").equals(password)) {
				return true;
			}
		}
		return false;
	}

	public int get_user_id(String email) throws SQLException {
		String sql = "SELECT user_id FROM User WHERE email = ?";
		connect_func();
		preparedStatement = (PreparedStatement) connect.prepareStatement(sql);
		preparedStatement.setString(1, email);
		ResultSet resultSet = preparedStatement.executeQuery();

		resultSet.first();
		int id = resultSet.getInt(1);
		System.out.println("id = " + id);

		return id;
	}

    
	
	public boolean checkEmail(String email) throws SQLException {
		boolean checks = false;
		String sql = "SELECT * FROM User WHERE email = ?";
		connect_func();
		preparedStatement = (PreparedStatement) connect.prepareStatement(sql);
		preparedStatement.setString(1, email);
		ResultSet resultSet = preparedStatement.executeQuery();

		if (resultSet.next()) {
			checks = true;
			System.out.println("Error: Username is taken");
		}

		return checks;
	}

	public boolean checkAddress(String adress_street_num, String adress_street, String adress_city, String adress_state,
			String adress_zip_code) throws SQLException {
		boolean checks = false;
		String sql = "SELECT * FROM User WHERE" + " adress_street_num = '" + adress_street_num + "'"
				+ " and adress_street = '" + adress_street + "'" + " and adress_city = '" + adress_city + "'"
				+ " and adress_state = '" + adress_state + "'" + " and adress_zip_code = '" + adress_zip_code + "'";

		connect_func();
		preparedStatement = (PreparedStatement) connect.prepareStatement(sql);
		ResultSet resultSet = preparedStatement.executeQuery();

		if (resultSet.next()) {
			checks = true;
			System.out.println("Error: Duplicate address");
		}

		return checks;
	}

	public void insert(user users) throws SQLException {
		connect_func();
		String sql = "insert into User(email, firstName, lastName, password, adress_street_num, adress_street, adress_city, adress_state, adress_zip_code, phonenumber,credit_no,credit_cvv,credit_year,credit_month) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ? ,? ,?,?,?)";
		preparedStatement = (PreparedStatement) connect.prepareStatement(sql);
		preparedStatement.setString(1, users.getEmail());
		preparedStatement.setString(2, users.getFirstName());
		preparedStatement.setString(3, users.getLastName());
		preparedStatement.setString(4, users.getPassword());
		preparedStatement.setString(5, users.getAdress_street_num());
		preparedStatement.setString(6, users.getAdress_street());
		preparedStatement.setString(7, users.getAdress_city());
		preparedStatement.setString(8, users.getAdress_state());
		preparedStatement.setString(9, users.getAdress_zip_code());
		preparedStatement.setString(10, users.getphone_number());
		preparedStatement.setString(11, users.getcredit_card_no());
		preparedStatement.setInt(12, users.getcredit_card_cvv());
		preparedStatement.setInt(13, users.getcredit_card_ex_year());
		preparedStatement.setInt(14, users.getcredit_card_ex_month());

		preparedStatement.executeUpdate();
		preparedStatement.close();
	}

	public boolean is_admin(int user_id) {
		if (user_id > 1)
			return false;
		return true;

	}

	public void insertNewMsg(int request_id, int user_id, String date, String topic, String msg) throws SQLException {
		connect_func();
		
		String sql = "insert into Chats(msg_date, msg_topic, msg_content, who_sent, request_id) values (?,?,?,?,?)";
		preparedStatement = (PreparedStatement) connect.prepareStatement(sql);
		preparedStatement.setString(1, date);
		preparedStatement.setString(2, topic);
		preparedStatement.setString(3, msg);
		if (is_admin(user_id)) {
			preparedStatement.setString(4, "admin");
		} else {
			preparedStatement.setString(4, "user");
		}
		preparedStatement.setInt(5, request_id);

		preparedStatement.executeUpdate();
		preparedStatement.close();
	}

	public void insertNewTree(String photo_1, String photo_2, String photo_3, int size, int height,
			String location, String note, int request_id) throws SQLException {
		connect_func();
		String sql = "insert into  Trees(photo_1, photo_2, photo_3, size, height, location, note, cost, cut, request_id) values (?,?,?,?,?,?,?,?,?,?)";
		preparedStatement = (PreparedStatement) connect.prepareStatement(sql);

		preparedStatement.setString(1, photo_1);
		preparedStatement.setString(2, photo_2);
		preparedStatement.setString(3, photo_3);
		preparedStatement.setInt(4, size);
		preparedStatement.setInt(5, height);
		preparedStatement.setString(6, location);
		preparedStatement.setString(7, note);
		preparedStatement.setString(8, "0");
		preparedStatement.setString(9, "no");
		preparedStatement.setInt(10, request_id);

		preparedStatement.executeUpdate();
		preparedStatement.close();

		System.out.println("tree added");
	}

	public int get_request_last_id() throws SQLException { // To DO: return last id !!!
		String sql = "SELECT request_id FROM requests WHERE request_id = (SELECT MAX(request_id) FROM requests)";
		connect_func();
		statement = (Statement) connect.createStatement();
		ResultSet resultSet = statement.executeQuery(sql);
		resultSet.first();
		System.out.println("Last request id:");
		System.out.println(resultSet.getInt(1));

		return resultSet.getInt(1);
	}

	public String get_request_status(int request_id) throws SQLException { 
	    String sql = "SELECT status FROM requests WHERE request_id = ?";
	    connect_func();
	    PreparedStatement preparedStatement = connect.prepareStatement(sql);
	    preparedStatement.setInt(1, request_id);
	    ResultSet resultSet = preparedStatement.executeQuery();

	    // Move the cursor to the first row
	    if (resultSet.first()) {
	        String status = resultSet.getString(1);
	        System.out.println("request status is: " + status);
	        return status;
	    } else {
	        // Handle the case where no rows are returned
	        System.out.println("No request found with ID: " + request_id);
	        return null; // Or handle it differently based on your application logic
	    }
	}
	
	public ResultSet get_tree_id(int request_id) throws SQLException { 
	    String sql = "select tree_id from trees where request_id = ?";
	    connect_func(); 
	    preparedStatement = connect.prepareStatement(sql);
	    preparedStatement.setInt(1, request_id);
	    ResultSet resultSet = preparedStatement.executeQuery();
	    return resultSet;	
	}
	
	
	public String get_user_info(int user_id, String info) throws SQLException { 
	    // Safely constructing SQL query
	    String safeInfo = getSafeColumnName(info); // Ensure 'info' is a valid column name
	    String sql = "select " + safeInfo + " from user where user_id = ?";
	    
	    connect_func(); 
	    preparedStatement = connect.prepareStatement(sql);
	    preparedStatement.setInt(1, user_id);
	    ResultSet resultSet = preparedStatement.executeQuery();
	    
	    // Move the cursor to the first row
	    if (resultSet.next()) {
	        String status = resultSet.getString(safeInfo); // Using column name directly
	        System.out.println(info + " is: " + status);
	        return status;
	    } else {
	        // Handle the case where no rows are returned
	        System.out.println("No User found with ID: " + user_id);
	        return null; // Or handle it differently based on your application logic
	    }
	}

	public String generate_MaxHeight_Tree_HTMLtable() throws SQLException {
	    List<Integer> treeIds = new ArrayList<>();
	    // Updated SQL query to include the condition where 'cut' is 'yes'
	    String sql = "SELECT tree_id FROM Trees WHERE cut = 'yes' AND height = (SELECT MAX(height) FROM Trees WHERE cut = 'yes')";
	    connect_func();
	    try (PreparedStatement preparedStatement = connect.prepareStatement(sql);
	         ResultSet resultSet = preparedStatement.executeQuery()) {

	        while (resultSet.next()) {
	            int treeId = resultSet.getInt("tree_id");
	            treeIds.add(treeId);
	        }
	    }

	    StringBuilder htmlTable = new StringBuilder();

	    htmlTable.append("<table border='1'>"); // Start of the table, add more styling if needed
	    htmlTable.append("<tr><th>Tree ID</th></tr>"); // Table header

	    for (Integer ID : treeIds) {
	        htmlTable.append("<tr>"); // Start of row
	        htmlTable.append("<td>").append(ID).append("</td>"); // Tree ID column
	        htmlTable.append("</tr>"); // End of row
	    }

	    htmlTable.append("</table>"); // End of table

	    return htmlTable.toString();
	}
		
	public void change_request_status(int requestId, String newStatus) throws SQLException {
		String sql = "UPDATE Requests SET status = ? WHERE request_id = ?;";
		connect_func();
		try (PreparedStatement pstmt = connect.prepareStatement(sql)) {

			// Set the parameters for the prepared statement.
			pstmt.setString(1, newStatus);
			pstmt.setInt(2, requestId);

			// Execute the update.
			int affectedRows = pstmt.executeUpdate();

			if (affectedRows > 0) {
				System.out.println("Request status updated successfully.");
			} else {
				System.out.println("Request ID not found.");
			}

		} catch (SQLException e) {
			System.out.println("Error updating request status: " + e.getMessage());
		}
	}
	
	public void update_request_bill(int requestId, int total) throws SQLException {
		String sql = "UPDATE Requests SET bill = ? WHERE request_id = ?;";
		connect_func();
		try (PreparedStatement pstmt = connect.prepareStatement(sql)) {

			// Set the parameters for the prepared statement.
			pstmt.setInt(1, total);
			pstmt.setInt(2, requestId);

			// Execute the update.
			int affectedRows = pstmt.executeUpdate();

			if (affectedRows > 0) {
				System.out.println("Request bill updated successfully.");
			} else {
				System.out.println("Request ID not found.");
			}

		} catch (SQLException e) {
			System.out.println("Error updating request bill: " + e.getMessage());
		}
	}
	
	public void update_request_paid(int requestId, int amount) throws SQLException {
		String sql = "UPDATE Requests SET paid = ? WHERE request_id = ?;";
		connect_func();
		try (PreparedStatement pstmt = connect.prepareStatement(sql)) {

			// Set the parameters for the prepared statement.
			pstmt.setInt(1, amount);
			pstmt.setInt(2, requestId);

			// Execute the update.
			int affectedRows = pstmt.executeUpdate();

			if (affectedRows > 0) {
				System.out.println("Request paid updated successfully.");
			} else {
				System.out.println("Request ID not found.");
			}

		} catch (SQLException e) {
			System.out.println("Error updating request paid: " + e.getMessage());
		}
	}
	
	public int update_tree_cost(HttpServletRequest request, int requestID) throws SQLException {
		connect_func();
		resultSet = get_tree_id(requestID);
		int i = 1;
		while (resultSet.next()) {
				String newCost = request.getParameter("tree_"+i+"_cost");	
				i++;
		
				String sql = "UPDATE Trees SET cost = ? WHERE tree_id = ?;";
				try (PreparedStatement pstmt = connect.prepareStatement(sql)) {
		
					// Set the parameters for the prepared statement.
					pstmt.setString(1, newCost);
					pstmt.setInt(2, resultSet.getInt(1));
		
					// Execute the update.
					int affectedRows = pstmt.executeUpdate();
		
					if (affectedRows > 0) {
						System.out.println("Tree cut cost updated successfully.");
					} else {
						System.out.println("Tree ID not found.");
					}
		
				} catch (SQLException e) {
					System.out.println("Error updating tree cost: " + e.getMessage());
				}
		}
		return i-1;
	}
	
	public void mark_trees_as_cut(HttpServletRequest request, int requestID) throws SQLException {
	    connect_func();
	    resultSet = get_tree_id(requestID);
	    int i = 1;
	    while (resultSet.next()) {
	        String sql = "UPDATE Trees SET cut = 'yes' WHERE tree_id = ?;";
	        try (PreparedStatement pstmt = connect.prepareStatement(sql)) {

	            // Set the parameter for the prepared statement.
	            pstmt.setInt(1, resultSet.getInt(1));

	            // Execute the update.
	            int affectedRows = pstmt.executeUpdate();

	            if (affectedRows > 0) {
	                System.out.println("Tree cut status updated successfully for tree " + i + ".");
	            } else {
	                System.out.println("Tree ID not found for tree " + i + ".");
	            }

	            i++;
	        } catch (SQLException e) {
	            System.out.println("Error updating tree cut status: " + e.getMessage());
	        }
	    }
	}
	
	public void insertNewRequest(String request_date, int user_id, int treeCount) throws SQLException {
		connect_func();
		String sql = "insert into   Requests(request_date, status, tree_n, bill, paid, user_id) values (?,?,?,?,?,?)";
		preparedStatement = (PreparedStatement) connect.prepareStatement(sql);

		preparedStatement.setString(1, request_date);
		preparedStatement.setString(2, "open");
		preparedStatement.setInt(3, treeCount);
		preparedStatement.setInt(4, 0);
		preparedStatement.setInt(5, 0);
		preparedStatement.setInt(6, user_id);

		preparedStatement.executeUpdate();
		preparedStatement.close();

	}

	public static String resultSetToHTMLTable(ResultSet resultSet) throws SQLException {
		StringBuilder htmlTable = new StringBuilder();

		// Start the HTML table
		htmlTable.append("<table border='1'>");

		// Get the metadata of the ResultSet to determine the number of columns
		int columnCount = resultSet.getMetaData().getColumnCount();

		// Create the table header row
		htmlTable.append("<tr>");
		for (int i = 1; i <= columnCount; i++) {
			htmlTable.append("<th>").append(resultSet.getMetaData().getColumnName(i)).append("</th>");
		}
		htmlTable.append("</tr>");

		// Iterate through the ResultSet and create table rows
		while (resultSet.next()) {
			htmlTable.append("<tr>");
			for (int i = 1; i <= columnCount; i++) {
				String cellContent = resultSet.getString(i);

				if (cellContent != null && cellContent.endsWith(".jpg")) {
					// If the cell content is a reference to a JPG image, create an img tag
					String imgUrl = "/resources/" + cellContent;
					htmlTable.append("<td><img src=\"").append(imgUrl)
							.append("\" alt=\"Image\" style=\"max-width:100px; max-height:100px;\"></td>");
				} else {
					// For regular text content, just append it within the table data tags
					htmlTable.append("<td>").append(cellContent).append("</td>");
				}
			}
			htmlTable.append("</tr>");
		}

		// End the HTML table
		htmlTable.append("</table>");

		return htmlTable.toString();
	}

	public String generateHTMLRequestTable(ResultSet resultSet, boolean isUser) throws SQLException {
	    
		System.out.println("overdue:");
		List<Integer> overdue = ChatDelay.getUnpaidBills(connect);
		StringBuilder htmlTable = new StringBuilder();

	    try {
	        // Start building the HTML table
	        htmlTable.append("<table border=\"1\">\n")
	                 .append("  <thead>\n")
	                 .append("    <tr>\n")
	                 .append("      <th>Date</th>\n"); // Header for Date

	        // Conditional column based on 'isUser'
	        if (isUser) {
	            htmlTable.append("      <th>Status</th>\n"); // Header for Status
	        } else {
	            htmlTable.append("      <th>Tree Count</th>\n"); // Header for Tree Count
	        }

	        // Headers for Request ID and Open Balance
	        htmlTable.append("      <th>Request ID</th>\n")
	                 .append("      <th>Open Balance</th>\n") // Added a new header for Open Balance
	                 .append("    </tr>\n")
	                 .append("  </thead>\n")
	                 .append("  <tbody>\n");

	        // Iterate through each result in the ResultSet
	        while (resultSet.next()) {
	            String requestDate = resultSet.getString("request_date");
	            String status = resultSet.getString("status");
	            int tree_n = resultSet.getInt("tree_n");
	            String requestId = resultSet.getString("request_id");
	            int openBalance = resultSet.getInt("bill")-resultSet.getInt("paid"); // Fetch open balance

	            // Check if the requestId is in the overdue list
	            String rowStyle = overdue.contains(Integer.valueOf(requestId)) ? " style=\"background-color: red;\"" : 
	                             ((tree_n == 1 && status.equals("accepted") && !isUser) ? " style=\"background-color: #90EE90;\"" : "");


	            // Constructing each row of the table
	            htmlTable.append("    <tr").append(rowStyle).append(">\n")
	                     .append("      <td><a href=\"/database/requestDetails?requestId=").append(requestId)
	                     .append("\" class=\"date-link\" data-request-id=\"").append(requestId)
	                     .append("\">").append(requestDate).append("</a></td>\n");

	            // Conditionally adding status or tree count
	            if (isUser) {
	                htmlTable.append("      <td>").append(status).append("</td>\n");
	            } else {
	                htmlTable.append("      <td>").append(tree_n).append("</td>\n");
	            }

	            // Adding Request ID and Open Balance
	            htmlTable.append("      <td>").append(requestId).append("</td>\n")
	                     .append("      <td>$").append(openBalance).append("</td>\n")
	                     .append("    </tr>\n");
	        }

	        // Add 'Start New Request' link for users
	        if (isUser) {
	            htmlTable.append("    <tr>\n")
	                     .append("      <td colspan=\"5\"><a href=\"/database/newRequest\">Start New Request</a></td>\n") // Adjusted colspan to 5
	                     .append("    </tr>\n");
	        }

	        // Finish the HTML table
	        htmlTable.append("  </tbody>\n")
	                 .append("</table>\n");

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return htmlTable.toString();
	}

//	public static String generateChatHTMLTable(ResultSet resultSet) throws SQLException {
//		StringBuilder htmlTable = new StringBuilder();
//		try {
//			htmlTable.append("<table border=\"1\">\n")
//					.append("  <thead>\n")
//					.append("    <tr>\n")
//					.append("      <th>Date</th>\n")
//					.append("      <th>Topic</th>\n")
//					.append("      <th>Message Content</th>\n")
//					.append("      <th>Sender</th>\n")
//					.append("    </tr>\n")
//					.append("  </thead>\n")
//					.append("  <tbody>\n");
//
//			while (resultSet.next()) {
//				String msgDate = resultSet.getString("msg_date");
//				String msgTopic = resultSet.getString("msg_topic");
//				String msgContent = resultSet.getString("msg_content");
//				String whoSent = resultSet.getString("who_sent");
//
//				htmlTable.append("    <tr>\n")
//						.append("      <td>")
//						.append(msgDate)
//						.append("</td>\n")
//						.append("      <td>")
//						.append(msgTopic)
//						.append("</td>\n")
//						.append("      <td>")
//						.append(msgContent)
//						.append("</td>\n")
//						.append("      <td>")
//						.append(whoSent)
//						.append("</td>\n")
//						.append("    </tr>\n");
//			}
//
//			htmlTable.append("  </tbody>\n")
//					 .append("</table>\n");
//
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return htmlTable.toString();
//	}
	
	public static String generateChatHTMLTable(ResultSet resultSet, boolean adminView) throws SQLException {
	    StringBuilder htmlTable = new StringBuilder();

	    try {
	        // Start of the HTML table
	        htmlTable.append("<table border=\"1\" style=\"width:50%;\">\n");

	        while (resultSet.next()) {
	            String msgDate = resultSet.getString("msg_date");
	            String msgTopic = resultSet.getString("msg_topic");
	            String msgContent = resultSet.getString("msg_content");
	            String whoSent = resultSet.getString("who_sent");

	            // Determine the column placement based on the sender
	            boolean adminMsg = whoSent.equals("admin");

	            // Constructing the table row based on the sender
	            htmlTable.append("    <tr>\n");

	            if (adminMsg && adminView) {
	                htmlTable.append("      <td style=\"width:50%;\"></td>\n");
	            }
	            if (!adminMsg && !adminView) {
	                htmlTable.append("      <td style=\"width:50%;\"></td>\n");
	            }

	            // Column with the message
	            htmlTable.append("      <td style=\"width:50%; text-align:")
	                     .append("left")
	                     .append("; vertical-align:top;\">\n")
	                     .append("        <strong>").append(msgTopic).append("</strong><br/>\n")
	                     .append("        ").append(msgContent).append("<br/>\n")
	                     .append("        <small><i>").append(whoSent).append(" ").append(msgDate).append("</i></small>\n")
	                     .append("      </td>\n");

	            if (!adminMsg && adminView) {
	                htmlTable.append("      <td style=\"width:50%;\"></td>\n");
	            }
	            if (adminMsg && !adminView) {
	                htmlTable.append("      <td style=\"width:50%;\"></td>\n");
	            }

	            htmlTable.append("    </tr>\n");
	        }

	        // End of the HTML table
	        htmlTable.append("</table>\n");

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return htmlTable.toString();
	}
	
	public String msgBox(int request_id, int userId) throws SQLException {
		StringBuilder htmlTable = new StringBuilder();
		String stat = get_request_status(request_id);

		if (!is_admin(userId)) {

			htmlTable.append(" <h2>Send a New Message</h2>\n"
					+ " <form id=\"messageForm\" action=\"newMsg\" method=\"post\">\n"
					+ " <table border=\"1\" cellpadding=\"5\">" + " <tr>" + " <th>Topic: </th>\n"
					+ " <td><input type=\"text\" name=\"topic\" id=\"topicInput\" required></td>\n" + " </tr>" + " <tr>"
					+ " <th>Message: </th>\n"
					+ " <td><textarea name=\"msg\" id=\"messageInput\" rows=\"4\" cols=\"50\" required></textarea></td>\n"
					+ " </tr>" 
					+ " <tr>" 
					+ " <td colspan=\"2\" style=\"text-align: center;\">"
					+ " <input type=\"submit\" value=\"Send\" onclick=\"document.getElementById('messageForm').action='newMsg';\" />\n");
			if (stat.equals("open"))
				htmlTable.append(
						" <input type=\"button\" value=\"Cancel Request\" onclick=\"document.getElementById('topicInput').disabled=true; document.getElementById('messageInput').disabled=true; document.getElementById('messageForm').action='prepareReject'; document.getElementById('messageForm').submit();\" />\n");
			if (stat.equals("quote")) {
				htmlTable.append(
						" <input type=\"button\" value=\"Reject and Cancel Request\" onclick=\"document.getElementById('topicInput').disabled=true; document.getElementById('messageInput').disabled=true; document.getElementById('messageForm').action='prepareReject'; document.getElementById('messageForm').submit();\" />\n");
				htmlTable.append(
						" <input type=\"button\" value=\"Accept Order\" onclick=\"document.getElementById('topicInput').disabled=true; document.getElementById('messageInput').disabled=true; document.getElementById('messageForm').action='acceptRequest'; document.getElementById('messageForm').submit();\" />\n");
			}
			if (stat.equals("bill")) {
				htmlTable.append(
						" <input type=\"button\" value=\"Make a Payment\" onclick=\"document.getElementById('topicInput').disabled=true; document.getElementById('messageInput').disabled=true; document.getElementById('messageForm').action='preparePayment'; document.getElementById('messageForm').submit();\" />\n");
				}
			

			htmlTable.append(" </td>\n" + " </tr>" + " </table>" + " </form>");

		} else { //admin:

			htmlTable.append(" <h2>Send a New Message</h2>\n"
					+ " <form id=\"messageForm\" action=\"newMsg\" method=\"post\">\n"
					+ " <table border=\"1\" cellpadding=\"5\">" + " <tr>" + " <th>Topic: </th>\n"
					+ " <td><input type=\"text\" name=\"topic\" id=\"topicInput\" required></td>\n" + " </tr>" + " <tr>"
					+ " <th>Message: </th>\n"
					+ " <td><textarea name=\"msg\" id=\"messageInput\" rows=\"4\" cols=\"50\" required></textarea></td>\n"
					+ " </tr>" + " <tr>" + " <td colspan=\"2\" style=\"text-align: center;\">"
					+ " <input type=\"submit\" value=\"Send\" onclick=\"document.getElementById('messageForm').action='newMsg';\" />\n");
			if (stat.equals("open")) {
//				htmlTable.append(" <input type=\"submit\" value=\"Send as Quote\" onclick=\"document.getElementById('messageForm').action='sendQuote';\" />\n");
				htmlTable.append(" <input type=\"button\" value=\"Prepare Quote\" onclick=\"document.getElementById('topicInput').disabled=true; document.getElementById('messageInput').disabled=true; document.getElementById('messageForm').action='prepareQuote'; document.getElementById('messageForm').submit();\" />\n");
				htmlTable.append(" <input type=\"button\" value=\"Reject Request\" onclick=\"document.getElementById('topicInput').disabled=true; document.getElementById('messageInput').disabled=true; document.getElementById('messageForm').action='prepareReject'; document.getElementById('messageForm').submit();\" />\n");
				}
			if (stat.equals("accepted")) {
//				htmlTable.append(" <input type=\"submit\" value=\"Send as Bill\" onclick=\"document.getElementById('messageForm').action='sendBill';\" />\n");
				htmlTable.append(" <input type=\"button\" value=\"Prepare Bill\" onclick=\"document.getElementById('topicInput').disabled=true; document.getElementById('messageInput').disabled=true; document.getElementById('messageForm').action='prepareBill'; document.getElementById('messageForm').submit();\" />\n");
					
			}
				
			htmlTable.append(" </td>\n");
			htmlTable.append(" </tr>");
			htmlTable.append(" </table>");
			htmlTable.append(" </form>");
		}

		return htmlTable.toString();
	}
	
    public static int getTreeCountByRequestId(Connection connection, int requestId) throws SQLException {

        String query = "SELECT tree_n FROM Requests where request_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, requestId);
            try (ResultSet resultSet = pstmt.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("tree_n");
                } else {
                    return -1; // No matching record found
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }
    public int get_bill_RequestId( int requestId) throws SQLException {
    	connect_func(); 
        String query = "SELECT bill FROM Requests where request_id = ?";
        try (PreparedStatement pstmt = connect.prepareStatement(query)) {
            pstmt.setInt(1, requestId);
            try (ResultSet resultSet = pstmt.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("bill");
                } else {
                    return -1; // No matching record found
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }
    public int get_paid_RequestId( int requestId) throws SQLException {
    	connect_func(); 
        String query = "SELECT paid FROM Requests where request_id = ?";
        try (PreparedStatement pstmt = connect.prepareStatement(query)) {
            pstmt.setInt(1, requestId);
            try (ResultSet resultSet = pstmt.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("paid");
                } else {
                    return -1; // No matching record found
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }
	
	public String prepareQuote(int request_id) throws SQLException {
		connect_func();
		int tree_n = getTreeCountByRequestId(connect, request_id);
		System.out.println("tree count >>>>>>> "+ tree_n);
		
		StringBuilder htmlTable = new StringBuilder();
		htmlTable.append(" <h2>Prepare Quote</h2>\n")
		 .append("<div align=\"center\">\n")
		 .append("<form action=\"sendQuote\" method=\"POST\">\n")
		 .append("<table border=\"1\" cellpadding=\"5\">\n");
		
		for (int i=1;i<=tree_n;i++)
		htmlTable.append("   <tr>\n"
		 		+ "                    <th>tree "+ i +": </th>\n"
		 		+ "                    <td><input type=\"number \" name=\"tree_"+i+"_cost\" required></td>\n"
		 		+ "   </tr>");
		
		htmlTable.append("<th>Note: </th>\n"
						+ " <td><textarea name=\"note\" id=\"messageInput\" rows=\"4\" cols=\"50\" required></textarea></td>\n"
						+ " </tr>");
		
		htmlTable.append("                <tr>\n"
		 		+ "                    <td colspan=\"2\" align=\"center\">\n"
		 		+ "                        <input type=\"submit\" value=\"Send Quote\"/>\n"
		 		+ "                    </td>\n"
		 		+ "                </tr>")
		 .append("</table>\n")
		 .append("</form>\n")
		 .append("</div>\n");
					
		
		disconnect();
		return htmlTable.toString();
	}
	
	public String prepareBill(int request_id) throws SQLException {
		connect_func();
		int tree_n = getTreeCountByRequestId(connect, request_id);
		System.out.println("tree count >>>>>>> "+ tree_n);
		
		StringBuilder htmlTable = new StringBuilder();
		htmlTable.append(" <h2>Finalize Cost</h2>\n")
		 .append("<div align=\"center\">\n")
		 .append("<form action=\"sendBill\" method=\"POST\">\n")
		 .append("<table border=\"1\" cellpadding=\"5\">\n");
		
		for (int i=1;i<=tree_n;i++)
		htmlTable.append("   <tr>\n"
		 		+ "                    <th>tree "+ i +": </th>\n"
		 		+ "                    <td><input type=\"number \" name=\"tree_"+i+"_cost\" required></td>\n"
		 		+ "   </tr>");
		
		htmlTable.append("   <tr>\n"
		 		+ "                    <th>other costs: </th>\n"
		 		+ "                    <td><input type=\"number \" name=\"other_costs\" required></td>\n"
		 		+ " </tr>\n"
				+ " <th>Note: </th>\n"
				+ " <td><textarea name=\"note\" id=\"messageInput\" rows=\"4\" cols=\"50\" required></textarea></td>\n"
				+ " </tr>");
		
		htmlTable.append("                <tr>\n"
		 		+ "                    <td colspan=\"2\" align=\"center\">\n"
		 		+ "                        <input type=\"submit\" value=\"Send Bill\"/>\n"
		 		+ "                    </td>\n"
		 		+ "                </tr>")
		 .append("</table>\n")
		 .append("</form>\n")
		 .append("</div>\n");
					
		
		disconnect();
		return htmlTable.toString();
	}
	
	public String preparePayment(int request_id, int user_id) throws SQLException {
	    // Fields to retrieve
	    String[] paymentInfoFields = { 
	    	"firstName","lastName",
	        "credit_no", "credit_cvv", "credit_year", "credit_month",
	        "adress_street", "adress_city", "adress_state", "adress_zip_code"
	    };
	    StringBuilder htmlTable = new StringBuilder();
	    htmlTable.append(" <h3>Make a payment at your convenience</h3>\n");
	    // Start HTML table
	    htmlTable.append("<table>\n");

	    // Iterate through each field and add it to the table
	    for (String field : paymentInfoFields) {
	        String value = get_user_info(user_id, field); // Retrieve the value for each field

	        // Check if the value is null or empty and handle it appropriately
	        value = (value == null || value.isEmpty()) ? "Not Available" : value;

	        // Append row to the HTML table
	        htmlTable.append("<tr>\n")
	                 .append("<td>").append(field).append("</td>\n") // Field name
	                 .append("<td><input type='text' name='").append(field).append("' value='").append(value).append("' /></td>\n") // Input field pre-filled with the value
	                 .append("</tr>\n");
	    }

	    // Special handling for payment_amount
	    int paymentAmount = get_bill_RequestId(request_id) - get_paid_RequestId(request_id); 
	    htmlTable.append("<tr>\n")
	             .append("<td>payment_amount</td>\n")
	             .append("<td><input type='text' name='payment_amount' value='").append(paymentAmount).append("' /></td>\n")
	             .append("</tr>\n");

	    // End HTML table
	    htmlTable.append("</table>\n");

	    // Create form with the table and submit button
	    StringBuilder htmlForm = new StringBuilder();
	    htmlForm.append("<form action='payBill' method='POST'>\n") 
	            .append(htmlTable)
	            .append("<input type='submit' value='Submit Payment Info'>\n")
	            .append("</form>\n");

	    return htmlForm.toString();
	}
	
	public String prepareReject(int userID) throws SQLException {
		connect_func();
		
		StringBuilder htmlTable = new StringBuilder();
		htmlTable.append(" <h2>Prepare Quote</h2>\n")
		 .append("<div align=\"center\">\n")
		 .append("<form action=\"rejectRequest\" method=\"POST\">\n")
		 .append("<table border=\"1\" cellpadding=\"5\">\n");
		
		
		htmlTable.append("<th>Reason: </th>\n"
						+ " <td><textarea name=\"note\" id=\"messageInput\" rows=\"4\" cols=\"50\" required></textarea></td>\n"
						+ " </tr>");
		
		htmlTable.append("                <tr>\n"
		 		+ "                    <td colspan=\"2\" align=\"center\">\n");
		
		if (is_admin(userID))
			htmlTable.append("                        <input type=\"submit\" value=\"Reject Request\"/>\n");
		else
			htmlTable.append("                        <input type=\"submit\" value=\"Cancel Request\"/>\n");
		
		htmlTable.append("                    </td>\n"
		 		+ "                </tr>")
		 .append("</table>\n")
		 .append("</form>\n")
		 .append("</div>\n");
					
		
		disconnect();
		return htmlTable.toString();
	}
	
	
	public static String generateTreeHTMLTable(ResultSet resultSet) throws SQLException {
		StringBuilder htmlTable = new StringBuilder();
		try {
			htmlTable.append("<h2>Trees Info</h2>\n")
	    			.append("<div align=\"center\">")
					.append("<table border=\"1\">\n")
					.append("  <thead>\n")
					.append("    <tr>\n")
					.append("      <th>Image1</th>\n")
					.append("      <th>Image2</th>\n")
					.append("      <th>Image3</th>\n")
					.append("      <th>Size</th>\n")
					.append("      <th>Height</th>\n")
					.append("      <th>Location</th>\n")
					.append("      <th>Note</th>\n")
					.append("      <th>Cost</th>\n")
					.append("    </tr>\n")
					.append("  </thead>\n")
					.append("  <tbody>\n");

			while (resultSet.next()) {
				String photo1 = resultSet.getString("photo_1");
				String photo2 = resultSet.getString("photo_2");
				String photo3 = resultSet.getString("photo_3");
				int size = resultSet.getInt("size");
				int height = resultSet.getInt("height");
				String location = resultSet.getString("location");
				String note = resultSet.getString("note");
				String cost = resultSet.getString("cost");

				htmlTable.append("    <tr>\n")
						.append("      <td><img src=\"")
						.append(photo1)
						.append("\" alt=\"Tree Image\" width=\"100\"/></td>\n")
						.append("      <td><img src=\"")
						.append(photo2)
						.append("\" alt=\"Tree Image\" width=\"100\"/></td>\n")
						.append("      <td><img src=\"")
						.append(photo3)
						.append("\" alt=\"Tree Image\" width=\"100\"/></td>\n")
						.append("      <td>")
						.append(size)
						.append("</td>\n")
						.append("      <td>")
						.append(height)
						.append("</td>\n")
						.append("      <td>")
						.append(location)
						.append("</td>\n")
						.append("      <td>")
						.append(note)
						.append("</td>\n")
						.append("      <td>")
						.append(cost)
						.append("</td>\n")
						.append("    </tr>\n");
			}

			htmlTable.append("  </tbody>\n")
					 .append("</table>\n")
					 .append("</div>");

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return htmlTable.toString();
	}
	

	
	public String listBigClients() throws SQLException {
		connect_func();
		List<TreeSumByUser.UserTreeCount> TreeCounts = TreeSumByUser.getTreeCountsPerUser(connect);
//		TreeCounts.forEach(System.out::println);
		disconnect();
		return TreeSumByUser.convertToHtmlTable(TreeCounts);
	}
	public String listGoodClients() throws SQLException {
		connect_func();
		List<ChatDelay> delays = ChatDelay.calculateDelays(connect);
		delays.forEach(System.out::println);
		disconnect();
		return ChatDelay.convertToHtmlTable(delays);
	}
	public String listProspectiveClients() throws SQLException {
		connect_func();
		List<Integer> Prospectives = ChatDelay.getUsersWithUnacceptedQuotes(connect);
		Prospectives.forEach(System.out::println);
		disconnect();
		return ChatDelay.convertProspectivesToHtmlTable(Prospectives);
	}
	public String listEasyClients() throws SQLException {
		connect_func();
		List<Integer> easy = ChatDelay.getEasyClients(connect);
		easy.forEach(System.out::println);
		disconnect();
		return ChatDelay.convertProspectivesToHtmlTable(easy);
	}
	
	public String listAllTable(String tableName) throws SQLException {
		String htmlTable = "";
		String sql = "SELECT * FROM " + tableName;
		connect_func();
		statement = (Statement) connect.createStatement();
		ResultSet resultSet = statement.executeQuery(sql);
		try {
			htmlTable = resultSetToHTMLTable(resultSet);
			System.out.println("htmlTable generated");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return htmlTable;
	}

	public String listUserTable(int user_id) throws SQLException {
		String htmlTable = "";
		String sql = "SELECT * FROM requests WHERE user_id = " + user_id;
		connect_func();
		statement = (Statement) connect.createStatement();
		ResultSet resultSet = statement.executeQuery(sql);
		try {
			htmlTable = generateHTMLRequestTable(resultSet, true);
			System.out.println("htmlTable generated");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return htmlTable;
	}

	public String listAdminTable(String status) throws SQLException {
		String htmlTable = "";
		String sql = "SELECT * FROM requests WHERE status = \"" + status + "\"";
		connect_func();
		statement = (Statement) connect.createStatement();
		ResultSet resultSet = statement.executeQuery(sql);
		try {
			htmlTable = generateHTMLRequestTable(resultSet, false);
			System.out.println("htmlTable generated");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return htmlTable;
	}

	public String listChatTable(int request_id, int current_user_id) throws SQLException {
	    String htmlTable = "";
	    String sql = "SELECT * FROM Chats WHERE request_id = ?";
	    connect_func();
	    try (PreparedStatement preparedStatement = connect.prepareStatement(sql)) {
	        // Set the value of the placeholder
	        preparedStatement.setInt(1, request_id);
	        // Execute the query
	        try (ResultSet resultSet = preparedStatement.executeQuery()) {
	            htmlTable = generateChatHTMLTable(resultSet, is_admin(current_user_id));
	            System.out.println("htmlTable generated");
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return htmlTable;
	}

	public String listTreeTable(int request_id) throws SQLException {
	    String htmlTable = "";
	    String sql = "SELECT * FROM Trees WHERE request_id = ?";
	    connect_func();
	    try (PreparedStatement preparedStatement = connect.prepareStatement(sql)) {
	        // Set the value of the placeholder
	        preparedStatement.setInt(1, request_id);
	        
	        // Execute the query and generate the HTML table
	        try (ResultSet resultSet = preparedStatement.executeQuery()) {
	            htmlTable = generateTreeHTMLTable(resultSet);
	            System.out.println("htmlTable generated");
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return htmlTable;
	}
	

	public String listUserInfo(int request_id) throws SQLException {
	    StringBuilder htmlTable = new StringBuilder();
	    String sql = "SELECT user_id FROM requests WHERE request_id = ?";
	    connect_func();
	    
	    try (PreparedStatement preparedStatement = connect.prepareStatement(sql)) {
	        // Set the value of the placeholder
	        preparedStatement.setInt(1, request_id);
	        
	        // Execute the query
	        try (ResultSet resultSet = preparedStatement.executeQuery()) {
	            if (resultSet.first()) {
	                int user_id = resultSet.getInt(1);

	                // Retrieve user information
	                String firstName = get_user_info(user_id, "firstName");
	                String lastName = get_user_info(user_id, "lastName");
	                String address = get_user_info(user_id, "adress_street") + ", "
	                               + get_user_info(user_id, "adress_city") + ", "
	                               + get_user_info(user_id, "adress_state") + " "
	                               + get_user_info(user_id, "adress_zip_code");
	                String phoneNumber = get_user_info(user_id, "phonenumber");

	                // Construct HTML table
	                htmlTable.append("<h2>User Info</h2>")
	                	     .append("<table border=\"1\">\n")
	                         .append("<tr><th>Name</th><td>").append(firstName).append(" ").append(lastName).append("</td></tr>\n")
	                         .append("<tr><th>Address</th><td>").append(address).append("</td></tr>\n")
	                         .append("<tr><th>Phone Number</th><td>").append(phoneNumber).append("</td></tr>\n")
	                         .append("</table>\n");
	            } else {
	                htmlTable.append("<p>No user found for the given request ID.</p>");
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return htmlTable.toString();
	}
	
	
	public void init() throws SQLException, FileNotFoundException, IOException {
		connect_func();
		statement = (Statement) connect.createStatement();

		String[] INITIAL = { "drop database if exists testdb; ", "create database testdb; ", "use testdb; ",
				"drop table if exists User; ",
				("CREATE TABLE if not exists User( "
						+ "user_id int primary key Auto_Increment," + "email VARCHAR(50) unique, "
						+ "firstName VARCHAR(10) NOT NULL, " + "lastName VARCHAR(10) NOT NULL, "
						+ "password VARCHAR(20) NOT NULL, " + "adress_street_num VARCHAR(4) , "
						+ "adress_street VARCHAR(30) , " + "adress_city VARCHAR(20)," + "adress_state VARCHAR(2),"
						+ "adress_zip_code VARCHAR(5)," + "phonenumber VARCHAR(10)," + "credit_no VARCHAR(19),"
						+ "credit_cvv int," + "credit_year int," + "credit_month int" + "); "),
				"drop table if exists Requests; ",
				("CREATE TABLE if not exists Requests( " 
						+ "request_id int primary key Auto_Increment, "
						+ "request_date datetime not null," 
						+ "status varchar(50)," 
						+ "tree_n int,"
						+ "bill int,"
						+ "paid int," 
						+ "user_id int, foreign key (user_id) references user(user_id)); "),
				"drop table if exists Chats; ",
				("CREATE TABLE if not exists Chats( " + "msg_id int primary key Auto_Increment, " + "msg_date datetime not null,"
						+ "msg_topic varchar(100)," + "msg_content varchar(300)," + "who_sent varchar(50),"
						+ "request_id int, foreign key (request_id) references requests(request_id)); "),
				("CREATE TABLE if not exists Trees( " 
						+ "tree_id int primary key Auto_Increment, " 
						+ "photo_1 varchar(100)," 
						+ "photo_2 varchar(100)," 
						+ "photo_3 varchar(100),"
						+ "size int," 
						+ "height int," 
						+ "location varchar(50)," 
						+ "note varchar(300),"
						+ "cost varchar(10)," 
						+ "cut varchar(5)," 
						+ "request_id int, foreign key (request_id) references requests(request_id)); ") };

		String[] USERS = {
				("insert into User(email, firstName, lastName, password, adress_street_num, adress_street, adress_city, adress_state, adress_zip_code, phonenumber,credit_no,credit_cvv,credit_year,credit_month)"
						+ "values ('admin', 'David', 'Smith','123',  '0000', 'Default', 'Default', '0', '00000','3133987613', '4532-4220-6922-9909', 654 , 27, 4),"
						+ "('root', 'default', 'default','pass1234',  '0000', 'Default', 'Default', '0', '00000','3133987613', '4532-4220-6922-9909', 654 , 27, 4),"
						+ "('don@gmail.com', 'Don', 'Cummings','don123',  '1000', 'hi street', 'mama', 'MO', '12345','1000', '4929-3813-3266-4295', 654 , 27, 4 ),"
						+ "('Margarita@gmail.com', 'Margarita', 'Lawson','Margarita123',  '1234', 'ivan street', 'tata','CO','12561','1000', '4916-4811-5814-8111' , 654 , 27, 4),"
						+ "('jo@gmail.com', 'Jo', 'Brady','jo1234',  '3214','marko street', 'brat', 'DU', '54321','1000', '4916-4034-9269-8783', 654 , 27, 4 ),"
						+ "('wallace@gmail.com', 'Wallace', 'Moore','wallace1234',  '4500', 'frey street', 'sestra', 'MI', '48202','1000' , '5299-1561-5689-1938', 654 , 27, 4),"
						+ "('amelia@gmail.com', 'Amelia', 'Phillips','amelia1234',  '1245', 'm8s street', 'baka', 'IL', '48000','1000', '5293-8502-0071-3058' , 654 , 27, 4),"
						+ "('sophie@gmail.com', 'Sophie', 'Pierce','sophie1234',  '2468', 'yolos street', 'ides', 'CM', '24680','1000', '5548-0246-6336-5664', 654 , 27, 4 ),"
						+ "('angelo@gmail.com', 'Angelo', 'Francis','angelo1234',  '4680', 'egypt street', 'lolas', 'DT', '13579','1000' , '4539-5385-7425-5825', 654 , 27, 4),"
						+ "('rudy@gmail.com', 'Rudy', 'Smith','rudy1234',  '1234', 'sign street', 'samo ne tu','MH', '09876','1000' , '4916-9766-5240-6147', 654 , 27, 4),"
						+ "('jeannette@gmail.com', 'Jeannette ', 'Stone','jeannette1234',  '0981', 'snoop street', 'kojik', 'HW', '87654','1000' , '4556-0072-1294-7415', 654 , 27, 4),"
						+ "('susie@gmail.com', 'Susie ', 'Guzman', 'susie1234',  '1234', 'whatever street', 'detroit', 'MI', '48202','1000', '5370-4638-8881-3020', 654 , 27, 4 );") };

		String[] REQUESTS = { 
			    ("insert into Requests(request_date, status, tree_n, bill, paid, user_id)"
			    + "values ( '2023-06-12 12:06:54' , 'accepted'  ,1 ,0 	,0 		, 3),"
			    + "       ( '2023-06-12 12:08:29' , 'Rejected'  ,2 ,0 	,0 		, 4),"
			    + "       ( '2023-06-12 12:09:37' , 'paid'      ,3 ,250 ,250 	, 5),"
			    + "       ( '2023-06-12 12:10:55' , 'Cancelled' ,1 ,0 	,0 		, 6),"
			    + "       ( '2023-06-12 12:11:28' , 'bill'      ,3 ,0 	,0 		, 6),"
			    + "       ( '2023-06-12 12:12:09' , 'bill'      ,2 ,80  ,50 	, 7),"
			    + "       ( '2023-06-12 12:12:09' , 'accepted'  ,2 ,0   ,0 		, 8),"
			    + "       ( '2023-06-02 12:12:09' , 'bill'      ,3 ,180 ,100	, 9),"
			    + "       ( '2023-06-12 12:12:09' , 'quote'     ,2 ,0   ,0 		, 10)"
			    + ";")
			};

			String[] Chats = { 
			    ("insert into Chats(msg_date, msg_topic, msg_content, who_sent, request_id)"
			    + "values ('2023-07-12 09:06:54', 'Beginning', 	'The request Started with above information', 				'user'   ,1),"
			    + "       ('2023-07-12 09:07:54', 'Beginning', 	'The request Started with above information', 				'user'   ,2),"
			    + "       ('2023-07-12 09:08:54', 'Beginning', 	'The request Started with above information', 				'user'   ,3),"
			    + "       ('2023-07-12 09:09:54', 'Beginning', 	'The request Started with above information', 				'user'   ,4),"
			    + "       ('2023-07-12 09:10:54', 'Beginning', 	'The request Started with above information', 				'user'   ,5),"
			    + "       ('2023-07-12 09:11:54', 'Beginning', 	'The request Started with above information', 				'user'   ,6),"
			    + "       ('2023-07-12 10:06:54', 'Quote', 	   	'Cost is roughly $100 for tree_1 Note: The actual cost may be higher depending on the bee hive situation.', 				'admin'  ,1),"
			    + "       ('2023-07-12 10:26:54', 'Quote', 	   	'Cost is roughly $50 for tree_1 $75 for tree_2 $120 for tree_3 Note: The actual cost may differ depending on the situation.'        , 			'admin'  ,3),"
			    + "       ('2023-07-12 12:06:54', 'Acceptance', 'User accepted the request', 				'user'   ,3),"
			    + "       ('2023-08-12 12:06:01', 'Bill', 		'Final Cost is $30 for tree_1 $80 for tree_2 $130 for tree_3 and $10for other costs. Total= $250 Due in one week. Note: Thanks for your business.'        , 			'admin'  ,3),"
			    + "       ('2023-08-12 20:06:00', 'payment', 	'User paid $150 Still $100 remains.', 				'user'   ,3),"
			    + "       ('2023-08-12 21:06:00', 'payment', 	'User paid $50 Still $50 remains.'        , 			'user'   ,3),"
			    + "       ('2023-08-12 22:06:00', 'Paid', 		'User paid the full price', 				'user'	 ,3),"
			    + "       ('2023-08-12 23:06:00', 'Rejected', 	'sorry it is too hard for me.', 				'admin'	 ,2),"
			    + "       ('2023-09-12 22:06:00', 'Quote', 		'Cost is roughly $100 for tree_1 Note: tax is not included', 				'admin'	 ,4),"
			    + "       ('2023-09-13 22:06:00', 'Cancelled', 	'It is too late I did it myself', 				'user'	 ,4),"
			    + "('2023-07-12 14:19:42', 'Quote', 'Cost is roughly $50 for tree_1 $50 for tree_2 $200 for tree_3 Note: Some other minor costs may apply', 'admin', 5),"
			    + "('2023-07-12 14:21:38', 'Ask for Discount', 'I will accept the quote for $80', 'user', 1),"
			    + "('2023-07-12 14:22:44', 'Discount', 'Okay, that is fine.', 'admin', 1),"
			    + "('2023-07-12 14:23:09', 'Acceptance', 'User accepted the request', 'user', 1),"
			    + "('2023-07-12 14:24:20', 'Quote', 'Cost is roughly $50 for tree_1 $30 for tree_2 Note: No more cost will apply.', 'admin', 6),"
			    + "('2023-07-12 14:24:58', 'Acceptance', 'User accepted the request', 'user', 6),"
			    + "('2023-07-12 14:26:14', 'Bill', 'Final Cost is $50 for tree_1 $30 for tree_2 and $0for other costs. Total= $80 Due in one week. Note: Thank You.', 'admin', 6),"
			    + "('2023-07-12 14:27:05', 'payment', 'User paid $50 Still $30 remains.', 'user', 6),"
			    + "('2023-07-12 14:36:57', 'Negotiation', 'Why the third one cost this high?', 'user', 5),"
			    + "('2023-07-12 14:37:29', 'Asnwer', 'Because it is too high', 'admin', 5),"
			    + "('2023-07-12 14:37:49', 'Acceptance', 'User accepted the request', 'user', 5),"
			    + "('2023-07-12 17:06:47', 'Beginning', 'The request Started with above information', 'user', 7),"
			    + "('2023-07-12 17:07:33', 'Beginning', 'The request Started with above information', 'user', 8),"
			    + "('2023-07-12 17:08:53', 'Quote', 'Cost is roughly $75 for tree_1 $45 for tree_2 Note: up to $20 may be applied for other costs.', 'admin', 7),"
			    + "('2023-07-02 17:10:23', 'Quote', 'Cost is roughly $130 for tree_1 $25 for tree_2 $50 for tree_3 Note: Nothing more', 'admin', 8),"
			    + "('2023-07-12 17:12:03', 'Ask for discount', 'Please give me a discount', 'user', 7),"
			    + "('2023-07-12 17:13:15', 'Discount', 'I accept if you do it for $160 in total', 'user', 8),"
			    + "('2023-07-12 17:14:15', 'Agree', 'Okay I will do the job for $100 nothing less', 'admin', 7),"
			    + "('2023-07-12 17:14:46', 'Discount', 'At least $180', 'admin', 8),"
			    + "('2023-07-12 17:14:54', 'Acceptance', 'User accepted the request', 'user', 8),"
			    + "('2023-07-12 17:15:03', 'Acceptance', 'User accepted the request', 'user', 7),"
			    + "('2023-07-12 17:16:27', 'Bill', 'Final Cost is $50 for tree_1 $50 for tree_2 $200 for tree_3 and $10for other costs. Total= $310 Due in one week. Note: A $10 is for tax', 'admin', 5),"
			    + "('2023-07-02 17:18:11', 'Bill', 'Final Cost is $120 for tree_1 $10 for tree_2 $40 for tree_3 and $10for other costs. Total= $180 Due in one week. Note: The $10 is for tax.', 'admin', 8),"
			    + "('2023-07-06 17:19:26', 'payment', 'User paid $100 Still $80 remains.', 'user', 8),"
			    + "('2023-07-12 17:20:19', 'Beginning', 'The request Started with above information', 'user', 9),"
			    + "('2023-07-12 17:21:22', 'Quote', 'Cost is roughly $250 for tree_1 $100 for tree_2 Note: It is the final price', 'admin', 9)"
			    + ";" ) };


		
		String[] Trees = { ("insert into Trees(photo_1, photo_2, photo_3, size, height, location, note, cost, cut, request_id)"
				+ "values"
				+ "('pic3.jpg','pic1.jpg','pic2.jpg',7, 15,'Far away from building','There is a bee hive on the tree',100,'no',1),"
				+ "('pic3.jpg','pic1.jpg','pic2.jpg',4, 10,'Near parking','some note...',0,'no',2),"
				+ "('pic3.jpg','pic1.jpg','pic2.jpg',5, 10,'Near House','some note...',0,'no',2),"
				+ "('pic3.jpg','pic1.jpg','pic2.jpg',4, 10,'far away','some note...',30,'yes',3),"
				+ "('pic3.jpg','pic1.jpg','pic2.jpg',6, 15,'10 feet far','some note...',80,'yes',3),"
				+ "('pic3.jpg','pic1.jpg','pic2.jpg',7, 20,'10 feet far','some note...',130,'yes',3),"
				+ "('pic3.jpg','pic1.jpg','pic2.jpg',5, 10,'10 feet far','some note...',100,'no',4),"
				+ "('pic3.jpg','pic1.jpg','pic2.jpg',5, 10,'10 feet far','some note...',50,'yes',5),"
				+ "('pic3.jpg','pic1.jpg','pic2.jpg',5, 10,'10 feet far','some note...',50,'yes',5),"
				+ "('pic3.jpg','pic1.jpg','pic2.jpg',7, 20,'10 feet far','some note...',200,'yes',5),"
				+ "('pic3.jpg','pic1.jpg','pic2.jpg',4, 4,'10 feet far','some note...',50,'yes',6),"
				+ "('pic3.jpg','pic1.jpg','pic2.jpg',3, 4,'10 feet far','some note...',30,'yes',6),"
				+ "('pic3.jpg','pic1.jpg','pic2.jpg',14, 15,'10 feet far','some note...',75,'no',7),"
				+ "('pic3.jpg','pic1.jpg','pic2.jpg',3, 12,'10 feet far','some note...',45,'no',7),"
				+ "('pic3.jpg','pic1.jpg','pic2.jpg',6, 17,'10 feet far','some note...',120,'yes',8),"
				+ "('pic3.jpg','pic1.jpg','pic2.jpg',2, 9,'10 feet far','some note...',10,'yes',8),"
				+ "('pic3.jpg','pic1.jpg','pic2.jpg',4, 12,'10 feet far','some note...',40,'yes',8),"
				+ "('pic3.jpg','pic1.jpg','pic2.jpg',8, 25,'10 feet far','some note...',250,'no',9),"
				+ "('pic3.jpg','pic1.jpg','pic2.jpg',5, 15,'10 feet far','some note...',100,'no',9)"
				+ ";") };

		// for loop to put these in database
		for (int i = 0; i < INITIAL.length; i++)
			statement.execute(INITIAL[i]);
		for (int i = 0; i < USERS.length; i++)
			statement.execute(USERS[i]);
		for (int i = 0; i < REQUESTS.length; i++)
			statement.execute(REQUESTS[i]);
		for (int i = 0; i < Chats.length; i++)
			statement.execute(Chats[i]);
		for (int i = 0; i < Trees.length; i++)
			statement.execute(Trees[i]);
		disconnect();
	}

}
