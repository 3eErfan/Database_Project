import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.time.Duration;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ChatDelay {
	private static PreparedStatement preparedStatement = null;
	private final int requestId;
	private final int userId;
    private final float delayHours;

    public ChatDelay(int requestId, float delayHours, int userID) {
        this.requestId = requestId;
        this.delayHours = delayHours;
        this.userId = userID;
    }
    
    public int getUserId() {
        return userId;
    }
    public int getRequestId() {
        return requestId;
    }

    public float getDelayHours() {
        return delayHours;
    }

    @Override
    public String toString() {
        return "Request ID: " + requestId + ", Delay: " + delayHours + " hours";
    }
    
    public static int getUserIdByRequestId(Connection connection, int requestId) throws SQLException {
    	
        String query = "SELECT user_id FROM Requests WHERE request_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, requestId);
            try (ResultSet resultSet = pstmt.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("user_id");
                } else {
                    return -1; // No matching record found
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }
    
//    // Method to calculate delays
//    public static List<ChatDelay> calculateDelays(Connection connection) throws SQLException {
//        List<ChatDelay> delays = new ArrayList<>();
//
//        String query = "SELECT request_id, msg_date, msg_topic FROM Chats WHERE msg_topic IN ('Paid', 'Bill') ORDER BY request_id, msg_date";
//        try (Statement statement = connection.createStatement();
//             ResultSet resultSet = statement.executeQuery(query)) {
//
//            Integer lastRequestId = null;
//            LocalDateTime lastDateTime = null;
//
//            while (resultSet.next()) {
//                int currentRequestId = resultSet.getInt("request_id");
//                int currentUserId = getUserIdByRequestId(connection, currentRequestId);
//                LocalDateTime currentDateTime = LocalDateTime.parse(resultSet.getString("msg_date"), DateTimeFormatter.ofPattern("yyyy-dd-MM HH:mm:ss"));
//                String currentTopic = resultSet.getString("msg_topic");
//
//                if (lastRequestId != null && lastRequestId.equals(currentRequestId) && "Paid".equals(currentTopic) && lastDateTime != null) {
//                    float delay = (float) (Duration.between(lastDateTime, currentDateTime).toSeconds()/3600.0);
//                    delays.add(new ChatDelay(currentRequestId, delay, currentUserId));
//                    lastRequestId = null; // Reset for the next pair
//                } else if ("Bill".equals(currentTopic)) {
//                    lastRequestId = currentRequestId;
//                    lastDateTime = currentDateTime;
//                }
//            }
//        }
//
//        return delays;
//    }
    public static List<ChatDelay> calculateDelays(Connection connection) throws SQLException {
        List<ChatDelay> delays = new ArrayList<>();
        LocalDateTime currentTime = LocalDateTime.now();

        String query = "SELECT request_id, msg_date, msg_topic FROM Chats WHERE msg_topic IN ('Paid', 'Bill') ORDER BY request_id, msg_date";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            Integer lastRequestId = null;
            LocalDateTime lastDateTime = null;

            while (resultSet.next()) {
                int currentRequestId = resultSet.getInt("request_id");
                int currentUserId = getUserIdByRequestId(connection, currentRequestId);
                LocalDateTime currentDateTime = LocalDateTime.parse(resultSet.getString("msg_date"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                String currentTopic = resultSet.getString("msg_topic");

                if (lastRequestId != null && lastRequestId.equals(currentRequestId)) {
                    if ("Paid".equals(currentTopic) && lastDateTime != null) {
                        // Calculate delay between 'Bill' and 'Paid'
                        float delay = (float) (Duration.between(lastDateTime, currentDateTime).toSeconds() / 3600.0);
                        delays.add(new ChatDelay(currentRequestId, delay, currentUserId));
                        lastRequestId = null; // Reset for the next pair
                    }
                } else if ("Bill".equals(currentTopic)) {
                    lastRequestId = currentRequestId;
                    lastDateTime = currentDateTime;
                }
            }

            // Check for unpaid bills
            if (lastRequestId != null && lastDateTime != null) {
                // Calculate delay from the last bill to the current time
                float delay = (float) (Duration.between(lastDateTime, currentTime).toSeconds() / 3600.0);
                delays.add(new ChatDelay(lastRequestId, delay, getUserIdByRequestId(connection, lastRequestId)));
            }
        }

        return delays;
    }
    
    
    public static List<Integer> getUnpaidBills(Connection connection) throws SQLException {
        List<Integer> unpaidBills = new ArrayList<>();
        Map<Integer, LocalDateTime> lastBillTimeMap = new HashMap<>();
        // Query to get 'Bill' and 'Paid' topics sorted by request_id and msg_date
        String query = "SELECT request_id, msg_date, msg_topic FROM Chats WHERE msg_topic IN ('Paid', 'Bill') ORDER BY request_id, msg_date";
        
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            
            
            while (resultSet.next()) {
                int currentRequestId = resultSet.getInt("request_id");
                LocalDateTime currentDateTime = LocalDateTime.parse(resultSet.getString("msg_date"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                String currentTopic = resultSet.getString("msg_topic");

                if ("Bill".equals(currentTopic)) {
                    // Store the time of the 'Bill' message
                    lastBillTimeMap.put(currentRequestId, currentDateTime);
                } else if ("Paid".equals(currentTopic)) {
                    // Remove the request_id from map if 'Paid' topic is encountered
                    lastBillTimeMap.remove(currentRequestId);
                }
            }
        }

        LocalDateTime currentTime = LocalDateTime.now();
        for (Map.Entry<Integer, LocalDateTime> entry : lastBillTimeMap.entrySet()) {
//        	System.out.println(">>>>>>>  " + entry.getKey() + "  >>>  " + Duration.between(entry.getValue(), currentTime).toHours());
            if (Duration.between(entry.getValue(), currentTime).toHours() > 24*7) { // 24*7 = a week
                unpaidBills.add(entry.getKey());
            }
        }

        return unpaidBills;
    }
    
    
    // Method to get user IDs of easy clients, avoiding duplicates
    public static List<Integer> getEasyClients(Connection connection) throws SQLException {
        List<Integer> easyClientUserIds = new ArrayList<>();
        Set<Integer> addedUserIds = new HashSet<>(); // Set to track added user IDs

        String query = "SELECT c1.request_id, c1.who_sent FROM Chats c1 " +
                       "INNER JOIN Chats c2 ON c1.request_id = c2.request_id AND c2.msg_topic = 'Acceptance' " +
                       "WHERE c1.msg_topic = 'Quote' AND c1.msg_id = c2.msg_id - 1";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                int requestId = resultSet.getInt("request_id");
                int userId = getUserIdByRequestId(connection, requestId);

                // Check if userId has already been added
                if (!addedUserIds.contains(userId)) {
                    easyClientUserIds.add(userId);
                    addedUserIds.add(userId); // Add to set to track
                }
            }
        }

        return easyClientUserIds;
    }
    
    public static List<Integer> getUsersWithUnacceptedQuotes(Connection connection) throws SQLException {
        Set<Integer> quotedRequests = new HashSet<>();
        Set<Integer> acceptedRequests = new HashSet<>();

        String query = "SELECT Chats.request_id, Chats.msg_topic, Requests.status " +
                       "FROM Chats " +
                       "JOIN Requests ON Chats.request_id = Requests.request_id " +
                       "WHERE Chats.msg_topic IN ('Acceptance', 'Quote') " +
                       "AND Requests.status = 'quote' " +
                       "ORDER BY Chats.request_id";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                int currentRequestId = resultSet.getInt("request_id");
                String currentTopic = resultSet.getString("msg_topic");

                if ("Quote".equals(currentTopic)) {
                    quotedRequests.add(currentRequestId);
                } else if ("Acceptance".equals(currentTopic)) {
                    acceptedRequests.add(currentRequestId);
                }
            }
        }

        // Remove all request IDs that have acceptance
        quotedRequests.removeAll(acceptedRequests);

        // Fetch corresponding user IDs
        List<Integer> userIdsWithUnacceptedQuotes = new ArrayList<>();
        for (Integer requestId : quotedRequests) {
            int userId = getUserIdByRequestId(connection, requestId);
            userIdsWithUnacceptedQuotes.add(userId);
        }

        return userIdsWithUnacceptedQuotes;
    }
    
    
    
    public static List<UserAverageDelay> calculateAverageDelays(List<ChatDelay> chatDelays) {
        Map<Integer, Float> totalDelays = new HashMap<>();
        Map<Integer, Integer> delayCounts = new HashMap<>();

        // Accumulate delays and counts for each user
        for (ChatDelay delay : chatDelays) {
            int userId = delay.getUserId();
            float currentDelay = delay.getDelayHours();

            totalDelays.put(userId, totalDelays.getOrDefault(userId, 0f) + currentDelay);
            delayCounts.put(userId, delayCounts.getOrDefault(userId, 0) + 1);
        }

        List<UserAverageDelay> averages = new ArrayList<>();
        // Calculate average for each user and add to the result list
        for (Map.Entry<Integer, Float> entry : totalDelays.entrySet()) {
            int userId = entry.getKey();
            float averageDelay = entry.getValue() / delayCounts.get(userId);
            averages.add(new UserAverageDelay(userId, averageDelay));
        }

        return UserAverageDelay.sortChatDelaysByDescendingDelay(averages);
    }

    // Inner class to hold user ID and average delay
    public static class UserAverageDelay {
        private final int userId;
        private final float averageDelay;

        public UserAverageDelay(int userId, float averageDelay) {
            this.userId = userId;
            this.averageDelay = averageDelay;
        }

        public int getUserId() {
            return userId;
        }

        public float getAverageDelay() {
            return averageDelay;
        }

        @Override
        public String toString() {
            return "UserID: " + userId + ", Average Delay: " + averageDelay + " hours";
        }
        
        // Function to sort the list in descending order by delay
        private static List<UserAverageDelay> sortChatDelaysByDescendingDelay(List<UserAverageDelay> chatDelays) {
            Collections.sort(chatDelays, new Comparator<UserAverageDelay>() {
                @Override
                public int compare(UserAverageDelay cd1, UserAverageDelay cd2) {
                    // For descending order, compare second object with first
                    return Long.compare( (long)(cd2.getAverageDelay()*3600), (long)(cd1.getAverageDelay()*3600));
                }
            });
            return chatDelays;
        }
    }
    
    
    
    

    
    
    // Method to convert list to HTML table
    public static String convertGoodToHtmlTable(List<ChatDelay> Delays) {
    	List<UserAverageDelay> chatDelays = calculateAverageDelays(Delays);
        StringBuilder htmlTable = new StringBuilder();

        htmlTable.append("<table border='1'>"); // Start of the table, add more styling if needed
        htmlTable.append("<tr><th>User ID</th><th>Payment Delay (hours)</th></tr>"); // Table header

        for (UserAverageDelay chatDelay : chatDelays) {
        	if (chatDelay.getAverageDelay() < 24.0) {
		            htmlTable.append("<tr>"); // Start of row
		            htmlTable.append("<td>").append(chatDelay.getUserId()).append("</td>"); // User ID column
		            htmlTable.append("<td>").append(String.format("%.1f", chatDelay.getAverageDelay())).append("</td>"); // Delay column
		            htmlTable.append("</tr>"); // End of row
        		}
        	}

        htmlTable.append("</table>"); // End of table

        return htmlTable.toString();
    }
    
    // Method to convert list to HTML table
    public static String convertBadToHtmlTable(List<ChatDelay> Delays) {
    	List<UserAverageDelay> chatDelays = calculateAverageDelays(Delays);
        StringBuilder htmlTable = new StringBuilder();

        htmlTable.append("<table border='1'>"); // Start of the table, add more styling if needed
        htmlTable.append("<tr><th>User ID</th><th>Payment Delay (Days)</th></tr>"); // Table header

        for (UserAverageDelay chatDelay : chatDelays) {
        	if (chatDelay.getAverageDelay() > 7*24.0) {
		            htmlTable.append("<tr>"); // Start of row
		            htmlTable.append("<td>").append(chatDelay.getUserId()).append("</td>"); // User ID column
		            htmlTable.append("<td>").append(String.format("%.1f", chatDelay.getAverageDelay() / 24.0)).append("</td>"); // Delay column
		            htmlTable.append("</tr>"); // End of row
        		}
        	}

        htmlTable.append("</table>"); // End of table

        return htmlTable.toString();
    }

}
