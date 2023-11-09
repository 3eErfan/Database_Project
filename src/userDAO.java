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
import java.util.List;
/**
 * Servlet implementation class Connect
 */
@WebServlet("/userDAO")
public class userDAO 
{
	private static final long serialVersionUID = 1L;
	private Connection connect = null;
	private Statement statement = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;
	
	public userDAO(){}
	
	/** 
	 * @see HttpServlet#HttpServlet()
     */
    protected void connect_func() throws SQLException {
    	//uses default connection to the database
        if (connect == null || connect.isClosed()) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                throw new SQLException(e);
            }
            connect = (Connection) DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/testdb?allowPublicKeyRetrieval=true&useSSL=false&user=John&password=pass1234");
            System.out.println(connect);
        }
    }
    
    public boolean database_login(String email, String password) throws SQLException{
    	try {
    		connect_func("root","pass1234");
    		String sql = "select * from user where email = ?";
    		preparedStatement = connect.prepareStatement(sql);
    		preparedStatement.setString(1, email);
    		ResultSet rs = preparedStatement.executeQuery();
    		return rs.next();
    	}
    	catch(SQLException e) {
    		System.out.println("failed login");
    		return false;
    	}
    }
	//connect to the database 
    public void connect_func(String username, String password) throws SQLException {
        if (connect == null || connect.isClosed()) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                throw new SQLException(e);
            }
            connect = (Connection) DriverManager
  			      .getConnection("jdbc:mysql://127.0.0.1:3306/userdb?"
  			          + "useSSL=false&user=" + username + "&password=" + password);
            System.out.println(connect);
        }
    }
    
   
    protected void disconnect() throws SQLException {
        if (connect != null && !connect.isClosed()) {
        	connect.close();
        }
    }
    
    
    public boolean isValid(String email, String password) throws SQLException
    {
    	String sql = "SELECT * FROM User";
    	connect_func();
    	statement = (Statement) connect.createStatement();
    	ResultSet resultSet = statement.executeQuery(sql);
    	
    	resultSet.last();
    	
    	int setSize = resultSet.getRow();
    	resultSet.beforeFirst();
    	
    	for(int i = 0; i < setSize; i++)
    	{
    		resultSet.next();
    		if(resultSet.getString("email").equals(email) && resultSet.getString("password").equals(password)) {
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
        System.out.println("id = "+id);
        
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
    
    public boolean checkAddress(String adress_street_num, String adress_street, String adress_city,
	        					String adress_state,String adress_zip_code) throws SQLException {
    	boolean checks = false;
    	String sql = "SELECT * FROM User WHERE" + 
    				 " adress_street_num = '"+ adress_street_num+ "'"+
    				 " and adress_street = '" + adress_street+ "'"+
    			     " and adress_city = '"+ adress_city+ "'"+
    				 " and adress_state = '" + adress_state+ "'"+
    			     " and adress_zip_code = '" + adress_zip_code + "'";
    	
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
    	connect_func("root","pass1234");         
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
    
    public void insertNewMsg(int request_id, int user_id, String topic, String msg) throws SQLException {
    	connect_func("root","pass1234");         
		String sql = "insert into Chats(msg_topic, msg_content, who_sent, request_id) values (?,?,?,?)";
		preparedStatement = (PreparedStatement) connect.prepareStatement(sql);
			preparedStatement.setString(1, topic);
			preparedStatement.setString(2, msg);
			if (is_admin(user_id)) {
				preparedStatement.setString(3, "admin");
			}else {
				preparedStatement.setString(3, "user");
			}
			preparedStatement.setInt(4, request_id);
			
		preparedStatement.executeUpdate();
        preparedStatement.close();
    }
    
    public void insertNewTree(String photo_1, String photo_2, String photo_3, String size, String hight, String location, String note, int request_id) throws SQLException {
    	connect_func("root","pass1234");         
		String sql = "insert into  Trees(photo_1, photo_2, photo_3, size, hight, location, note, request_id) values (?,?,?,?,?,?,?,?)";
		preparedStatement = (PreparedStatement) connect.prepareStatement(sql);

		preparedStatement.setString(1, photo_1);
		preparedStatement.setString(2, photo_2);
		preparedStatement.setString(3, photo_3);
		preparedStatement.setString(4, size);
		preparedStatement.setString(5, hight);
		preparedStatement.setString(6, location);
		preparedStatement.setString(7, note);
		preparedStatement.setInt(8, request_id);
		
		preparedStatement.executeUpdate();
        preparedStatement.close();
        
        System.out.println("tree added" );
    }    
    
    public int get_request_last_id() throws SQLException { // To DO: return last id !!!
        String sql = "SELECT request_id FROM requests WHERE request_id = (SELECT MAX(request_id) FROM requests)" ;      
        connect_func();      
        statement = (Statement) connect.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        resultSet.first();
        System.out.println("Last request id:" );
        System.out.println(resultSet.getInt(1) );
    	
        return resultSet.getInt(1);
    }

    public String get_request_status(int request_id) throws SQLException { // To DO: return last id !!!
        String sql = "SELECT status FROM requests WHERE request_id =" + request_id ;      
        connect_func();      
        statement = (Statement) connect.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        resultSet.first();
        System.out.println("request status is: " );
        System.out.println(resultSet.getString(1) );
    	
        return resultSet.getString(1);
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
    
    
    public void insertNewRequest(String request_date,int user_id) throws SQLException {
    	connect_func("root","pass1234");         
		String sql = "insert into   Requests(request_date, status, user_id) values (?,?,?)";
		preparedStatement = (PreparedStatement) connect.prepareStatement(sql);

		preparedStatement.setString(1, request_date);
		preparedStatement.setString(2, "open");
		preparedStatement.setInt(3, user_id);

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
            htmlTable.append("<th>").append( resultSet.getMetaData().getColumnName(i)).append("</th>");
        }
        htmlTable.append("</tr>");

        // Iterate through the ResultSet and create table rows
        while (resultSet.next()) {
            htmlTable.append("<tr>");
            for (int i = 1; i <= columnCount; i++) {
                String cellContent = resultSet.getString(i);

                if (cellContent != null && cellContent.endsWith(".jpg")) {
                    // If the cell content is a reference to a JPG image, create an img tag
                    String imgUrl =  "/resources/" + cellContent;
                    htmlTable.append("<td><img src=\"").append(imgUrl).append("\" alt=\"Image\" style=\"max-width:100px; max-height:100px;\"></td>");
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
    
    public static String generateHTMLRequestTable(ResultSet resultSet, boolean isUser) throws SQLException {
        StringBuilder htmlTable = new StringBuilder();
//        String userId = "";
        try {
            htmlTable.append("<table border=\"1\">\n")
                     .append("  <thead>\n")
                     .append("    <tr>\n")
                     .append("      <th>Request Date</th>\n")
                     .append("      <th>Status</th>\n")
                     .append("      <th>Request ID</th>\n") // Added a new header for Request ID
                     .append("    </tr>\n")
                     .append("  </thead>\n")
                     .append("  <tbody>\n");

            while (resultSet.next()) {
                String requestDate = resultSet.getString("request_date");
                String status = resultSet.getString("status");
//                userId = resultSet.getString("user_id");
                String requestId = resultSet.getString("request_id"); // Fetch request_id

                htmlTable.append("    <tr>\n")
                .append("      <td><a href=\"/database/requestDetails?requestId=")
                .append(requestId)  // Use fetched request_id 
                .append("\" class=\"date-link\" data-request-id=\"")
                .append(requestId) // Use fetched request_id here as well
                .append("\">")
                .append(requestDate)
                .append("</a></td>\n")
                .append("      <td>").append(status).append("</td>\n")
                .append("      <td>").append(requestId).append("</td>\n") // Added a new cell for Request ID
                .append("    </tr>\n");
            }
            
            if (isUser) {
            // Add the "Start New Request" row
            htmlTable.append("    <tr>\n")
                     .append("      <td colspan=\"4\"><a href=\"/database/newRequest")
                     .append("\">Start New Request</a></td>\n")
                     .append("    </tr>\n");
            }
            
            htmlTable.append("  </tbody>\n")
                     .append("</table>\n");
            
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return htmlTable.toString();
    }
    
    public static String generateChatHTMLTable(ResultSet resultSet) throws SQLException {
        StringBuilder htmlTable = new StringBuilder();
        try {
            htmlTable.append("<table border=\"1\">\n")
                     .append("  <thead>\n")
                     .append("    <tr>\n")
                     .append("      <th>Topic</th>\n")
                     .append("      <th>Message Content</th>\n")
                     .append("      <th>Sender</th>\n")
                     .append("    </tr>\n")
                     .append("  </thead>\n")
                     .append("  <tbody>\n");

            while (resultSet.next()) {
                String msgTopic = resultSet.getString("msg_topic");
                String msgContent = resultSet.getString("msg_content");
                String whoSent = resultSet.getString("who_sent");

                htmlTable.append("    <tr>\n")
                         .append("      <td>").append(msgTopic).append("</td>\n")
                         .append("      <td>").append(msgContent).append("</td>\n")
                         .append("      <td>").append(whoSent).append("</td>\n")
                         .append("    </tr>\n");
            }

            htmlTable.append("  </tbody>\n")
                     .append("</table>\n");
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return htmlTable.toString();
    }
    
    public String msgBox( int request_id, int userId) throws SQLException {
    	StringBuilder htmlTable = new StringBuilder();
    	String stat = get_request_status(request_id);
    	
    	if (!is_admin(userId)) {
    	
    		htmlTable.append(" <h2>Send a New Message</h2>\n"
    			    + " <form id=\"messageForm\" action=\"newMsg\" method=\"post\">\n"
    			    + " <table border=\"1\" cellpadding=\"5\">"
    			    + " <tr>"
    			    + " <th>Topic: </th>\n"
    			    + " <td><input type=\"text\" name=\"topic\" id=\"topicInput\" required></td>\n"
    			    + " </tr>"
    			    + " <tr>"
    			    + " <th>Message: </th>\n"
    			    + " <td><textarea name=\"msg\" id=\"messageInput\" rows=\"4\" cols=\"50\" required></textarea></td>\n"
    			    + " </tr>"
    			    + " <tr>"
    			    + " <td colspan=\"2\" style=\"text-align: center;\">"
    			    + " <input type=\"submit\" value=\"Send\" onclick=\"document.getElementById('messageForm').action='newMsg';\" />\n");
		    if (stat.equals("open"))
		    	htmlTable.append( " <input type=\"button\" value=\"Cancel Request\" onclick=\"document.getElementById('topicInput').disabled=true; document.getElementById('messageInput').disabled=true; document.getElementById('messageForm').action='rejectRequest'; document.getElementById('messageForm').submit();\" />\n");
		    if (stat.equals("quote")) {
		    	htmlTable.append( " <input type=\"button\" value=\"Reject and Cancel Request\" onclick=\"document.getElementById('topicInput').disabled=true; document.getElementById('messageInput').disabled=true; document.getElementById('messageForm').action='rejectRequest'; document.getElementById('messageForm').submit();\" />\n");
		    	htmlTable.append( " <input type=\"button\" value=\"Accept Order\" onclick=\"document.getElementById('topicInput').disabled=true; document.getElementById('messageInput').disabled=true; document.getElementById('messageForm').action='acceptRequest'; document.getElementById('messageForm').submit();\" />\n");
			    }
		    
		    htmlTable.append( " </td>\n"
    			    + " </tr>"
    			    + " </table>"
    			    + " </form>");
    		
    	} else {
    	
    		htmlTable.append(" <h2>Send a New Message</h2>\n"
    			    + " <form id=\"messageForm\" action=\"newMsg\" method=\"post\">\n"
    			    + " <table border=\"1\" cellpadding=\"5\">"
    			    + " <tr>"
    			    + " <th>Topic: </th>\n"
    			    + " <td><input type=\"text\" name=\"topic\" id=\"topicInput\" required></td>\n"
    			    + " </tr>"
    			    + " <tr>"
    			    + " <th>Message: </th>\n"
    			    + " <td><textarea name=\"msg\" id=\"messageInput\" rows=\"4\" cols=\"50\" required></textarea></td>\n"
    			    + " </tr>"
    			    + " <tr>"
    			    + " <td colspan=\"2\" style=\"text-align: center;\">"
    			    + " <input type=\"submit\" value=\"Send\" onclick=\"document.getElementById('messageForm').action='newMsg';\" />\n"
    			    + " <input type=\"submit\" value=\"Send as Quote\" onclick=\"document.getElementById('messageForm').action='sendQuote';\" />\n"
    			    + " <input type=\"button\" value=\"Reject Request\" onclick=\"document.getElementById('topicInput').disabled=true; document.getElementById('messageInput').disabled=true; document.getElementById('messageForm').action='rejectRequest'; document.getElementById('messageForm').submit();\" />\n"
    			    + " </td>\n"
    			    + " </tr>"
    			    + " </table>"
    			    + " </form>");
    	}
    	
    	return htmlTable.toString();
    }
    
  
    public static String generateTreeHTMLTable(ResultSet resultSet) throws SQLException {
        StringBuilder htmlTable = new StringBuilder();
        try {
            htmlTable.append("<table border=\"1\">\n")
                     .append("  <thead>\n")
                     .append("    <tr>\n")
                     .append("      <th>Image1</th>\n")
                     .append("      <th>Image2</th>\n")
                     .append("      <th>Image3</th>\n")
                     .append("      <th>Size</th>\n")
                     .append("      <th>Height</th>\n")
                     .append("      <th>Location</th>\n")
                     .append("      <th>Note</th>\n")
                     .append("    </tr>\n")
                     .append("  </thead>\n")
                     .append("  <tbody>\n");

            while (resultSet.next()) {
                String photo1 = resultSet.getString("photo_1");
                String photo2 = resultSet.getString("photo_2");
                String photo3 = resultSet.getString("photo_3");
                String size = resultSet.getString("size");
                String height = resultSet.getString("hight");
                String location = resultSet.getString("location");
                String note = resultSet.getString("note");

                htmlTable.append("    <tr>\n")
                .append("      <td><img src=\"").append(photo1).append("\" alt=\"Tree Image\" width=\"100\"/></td>\n")
                .append("      <td><img src=\"").append(photo2).append("\" alt=\"Tree Image\" width=\"100\"/></td>\n")
                .append("      <td><img src=\"").append(photo3).append("\" alt=\"Tree Image\" width=\"100\"/></td>\n")
                         .append("      <td>").append(size).append("</td>\n")
                         .append("      <td>").append(height).append("</td>\n")
                         .append("      <td>").append(location).append("</td>\n")
                         .append("      <td>").append(note).append("</td>\n")
                         .append("    </tr>\n");
            }

            htmlTable.append("  </tbody>\n")
                     .append("</table>\n");
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return htmlTable.toString();
    }
    
    public String listAllTable(String tableName)throws SQLException {
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
    
    public String listUserTable(int user_id)throws SQLException {
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
    
    public String listAdminTable(String status)throws SQLException {
    	String htmlTable = "";
    	String sql = "SELECT * FROM requests WHERE status = \"" + status +"\"";  
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
 
   
    public String listChatTable(int request_id)throws SQLException {
    	String htmlTable = "";
    	String sql = "SELECT * FROM Chats WHERE request_id = " + request_id;  
        connect_func();      
        statement = (Statement) connect.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
    	try {
            htmlTable = generateChatHTMLTable(resultSet);
            System.out.println("htmlTable generated");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    	return htmlTable;
    }
    
    public String listTreeTable(int request_id)throws SQLException {
    	String htmlTable = "";
    	String sql = "SELECT * FROM Trees WHERE request_id = " + request_id;  
        connect_func();      
        statement = (Statement) connect.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
    	try {
            htmlTable = generateTreeHTMLTable(resultSet);
            System.out.println("htmlTable generated");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    	return htmlTable;
    }

    public void init() throws SQLException, FileNotFoundException, IOException{
    	connect_func();
        statement =  (Statement) connect.createStatement();
        
      String[] INITIAL = {"drop database if exists testdb; ",
      "create database testdb; ",
      "use testdb; ",
      "drop table if exists User; ",
      ("CREATE TABLE if not exists User( " +
          "user_id int primary key Auto_Increment,"+
          "email VARCHAR(50) unique, " + 
          "firstName VARCHAR(10) NOT NULL, " +
          "lastName VARCHAR(10) NOT NULL, " +
          "password VARCHAR(20) NOT NULL, " +
          "adress_street_num VARCHAR(4) , "+ 
          "adress_street VARCHAR(30) , "+ 
          "adress_city VARCHAR(20)," + 
          "adress_state VARCHAR(2),"+ 
          "adress_zip_code VARCHAR(5),"+ 
          "phonenumber VARCHAR(10),"+ 
          "credit_no VARCHAR(19),"+
          "credit_cvv int,"+ 
          "credit_year int,"+  
          "credit_month int"+
          "); "),
      "drop table if exists Requests; ",
      ("CREATE TABLE if not exists Requests( " +
          "request_id int primary key Auto_Increment, " + 
          "request_date date not null,"+
          "status varchar(50),"+
          "user_id int, foreign key (user_id) references user(user_id)); "),
      "drop table if exists Chats; ",
      ("CREATE TABLE if not exists Chats( " +
              "msg_id int primary key Auto_Increment, " + 
              "msg_topic varchar(100),"+
              "msg_content varchar(300),"+
              "who_sent varchar(50),"+
              "request_id int, foreign key (request_id) references requests(request_id)); "),
      ("CREATE TABLE if not exists Trees( " +
              "tree_id int primary key Auto_Increment, " + 
              "photo_1 varchar(100),"+
              "photo_2 varchar(100),"+
              "photo_3 varchar(100),"+
              "size varchar(50),"+
              "hight varchar(50),"+
              "location varchar(50),"+
              "note varchar(300),"+
              "request_id int, foreign key (request_id) references requests(request_id)); ")
      };
      
      
      
	  String[] USERS = {("insert into User(email, firstName, lastName, password, adress_street_num, adress_street, adress_city, adress_state, adress_zip_code, phonenumber,credit_no,credit_cvv,credit_year,credit_month)"+
	  "values ('admin', 'David', 'Smith','admin',  '0000', 'Default', 'Default', '0', '00000','3133987613', '4532-4220-6922-9909', 654 , 27, 4),"+
	       "('don@gmail.com', 'Don', 'Cummings','don123',  '1000', 'hi street', 'mama', 'MO', '12345','1000', '4929-3813-3266-4295', 654 , 27, 4 ),"+
	        "('margarita@gmail.com', 'Margarita', 'Lawson','margarita1234',  '1234', 'ivan street', 'tata','CO','12561','1000', '4916-4811-5814-8111' , 654 , 27, 4),"+
	       "('jo@gmail.com', 'Jo', 'Brady','jo1234',  '3214','marko street', 'brat', 'DU', '54321','1000', '4916-4034-9269-8783', 654 , 27, 4 ),"+
	       "('wallace@gmail.com', 'Wallace', 'Moore','wallace1234',  '4500', 'frey street', 'sestra', 'MI', '48202','1000' , '5299-1561-5689-1938', 654 , 27, 4),"+
	       "('amelia@gmail.com', 'Amelia', 'Phillips','amelia1234',  '1245', 'm8s street', 'baka', 'IL', '48000','1000', '5293-8502-0071-3058' , 654 , 27, 4),"+
	      "('sophie@gmail.com', 'Sophie', 'Pierce','sophie1234',  '2468', 'yolos street', 'ides', 'CM', '24680','1000', '5548-0246-6336-5664', 654 , 27, 4 ),"+
	      "('angelo@gmail.com', 'Angelo', 'Francis','angelo1234',  '4680', 'egypt street', 'lolas', 'DT', '13579','1000' , '4539-5385-7425-5825', 654 , 27, 4),"+
	      "('rudy@gmail.com', 'Rudy', 'Smith','rudy1234',  '1234', 'sign street', 'samo ne tu','MH', '09876','1000' , '4916-9766-5240-6147', 654 , 27, 4),"+
	      "('jeannette@gmail.com', 'Jeannette ', 'Stone','jeannette1234',  '0981', 'snoop street', 'kojik', 'HW', '87654','1000' , '4556-0072-1294-7415', 654 , 27, 4),"+
	      "('root', 'default', 'default','pass1234',  '0000', 'Default', 'Default', '0', '00000','3133987613', '4532-4220-6922-9909', 654 , 27, 4),"+
		  "('susie@gmail.com', 'Susie ', 'Guzman', 'susie1234',  '1234', 'whatever street', 'detroit', 'MI', '48202','1000', '5370-4638-8881-3020', 654 , 27, 4 );")
      };
        
	  String[] REQUESTS = {("insert into Requests(request_date, status, user_id)"+
	   "values ( '2021-06-14' , 'open' 	, 1),"+
			  "( '2021-06-14' , 'open'	, 3),"+
			  "( '2021-06-14' , 'rejected', 3),"+
			  "( '2021-06-14' , 'rejected', 3),"+
			  "( '2021-06-14' , 'quote'	, 4),"+
			  "( '2021-06-14' , 'quote'	, 5),"+
			  "( '2021-06-14' , 'accepted', 6),"+
			  "( '2021-06-14' , 'done'	, 7),"+
			  "( '2021-06-14' , 'done'	, 8);"
			  )};
	  
	  String[] Chats = {("insert into Chats(msg_topic, msg_content, who_sent, request_id)"+
			"values ('some new topic', 'Hi, here is my request. Please consider it ASAP', 'user' 	,1),"+
			       "('some new topic', 'Hi, here is my request. Please consider it ASAP', 'user' 	,2),"+
			       "('some new topic', 'Hi, here is my request. Please consider it ASAP', 'user' 	,3),"+
			       "('some new topic', 'sorry I cannot do it.'							, 'admin' 	,3),"+
			       "('some new topic', 'Hi, here is my request. Please consider it ASAP', 'user' 	,4),"+
			       "('some new topic', 'sorry I cannot do it.'							, 'admin' 	,4),"+
			       "('some new topic', 'Hi, here is my request. Please consider it ASAP', 'user' 	,5),"+
			       "('some new topic', 'sure, it will cost you $250. Is that OK?'		, 'admin' 	,5),"+
			       "('some new topic', 'Hi, here is my request. Please consider it ASAP', 'user' 	,6),"+
			       "('some new topic', 'sure, it will cost you $250. Is that OK?'		, 'admin' 	,6),"+
			       "('some new topic', 'Hi, here is my request. Please consider it ASAP', 'user' 	,7),"+
			       "('some new topic', 'sure, it will cost you $250. Is that OK?'		, 'admin' 	,7),"+
			       "('some new topic', 'Sounds good Let\\'s close the deal.'				, 'user' 	,7),"+
			       "('some new topic', 'Hi, here is my request. Please consider it ASAP', 'user' 	,8),"+
			       "('some new topic', 'sure, it will cost you $250. Is that OK?'		, 'admin' 	,8),"+
			       "('some new topic', 'Sounds good Let us close the deal.'				, 'user' 	,8);"
			  )};
	  
	  String[] Trees = {("insert into Trees(photo_1, photo_2, photo_3, size, hight, location, note, request_id)"+
				"values ('pic1.jpg','pic2.jpg','pic3.jpg','4 inch','10 feet','45 feet far','some note...',1),"+
				       "('pic3.jpg','pic1.jpg','pic2.jpg','4 inch','10 feet','10 feet far','some note...',2),"+
				       "('pic3.jpg','pic1.jpg','pic2.jpg','4 inch','10 feet','10 feet far','some note...',3),"+
				       "('pic3.jpg','pic1.jpg','pic2.jpg','4 inch','10 feet','10 feet far','some note...',4),"+
				       "('pic3.jpg','pic1.jpg','pic2.jpg','4 inch','10 feet','10 feet far','some note...',5),"+
				       "('pic3.jpg','pic1.jpg','pic2.jpg','4 inch','10 feet','10 feet far','some note...',6),"+
				       "('pic3.jpg','pic1.jpg','pic2.jpg','4 inch','10 feet','10 feet far','some note...',7),"+
				       "('pic3.jpg','pic1.jpg','pic2.jpg','4 inch','10 feet','10 feet far','some note...',8);"
				  )};
	  
        //for loop to put these in database
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






























