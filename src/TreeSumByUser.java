import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;


public class TreeSumByUser {
    public static class UserTreeCount {
        private final int userId;
        private final int treeCount;

        public UserTreeCount(int userId, int treeCount) {
            this.userId = userId;
            this.treeCount = treeCount;
        }
        public int getUserId() {
            return userId;
        }

        public int getTreeCount() {
            return treeCount;
        }
        @Override
        public String toString() {
            return "UserID: " + userId + ", Trees: " + treeCount;
        }
    }
	
    public static List<UserTreeCount> getTreeCountsPerUser(Connection connection) 
            throws SQLException {
        List<UserTreeCount> userTreeCounts = new ArrayList<>();

        String query = "SELECT user_id, SUM(tree_n) AS total_trees FROM Requests GROUP BY user_id";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                int userId = resultSet.getInt("user_id");
                int totalTrees = resultSet.getInt("total_trees");
                userTreeCounts.add(new UserTreeCount(userId, totalTrees));
            }
        }

        return getUsersWithMaxTrees(userTreeCounts);
    }

    private static List<UserTreeCount> sortTreeList(List<UserTreeCount> userTreeCounts){
    	// Sorting the list in descending order by tree count
        Collections.sort(userTreeCounts, new Comparator<UserTreeCount>() {
            @Override
            public int compare(UserTreeCount o1, UserTreeCount o2) {
                return Integer.compare(o2.getTreeCount(), o1.getTreeCount());
            }
        });
        
        return userTreeCounts;
    }
    
    private static List<UserTreeCount> getUsersWithMaxTrees(List<UserTreeCount> userTreeCounts) {
    	List<UserTreeCount> sortedUserTreeCounts = sortTreeList(userTreeCounts);
        List<UserTreeCount> usersWithMaxTrees = new ArrayList<>();

        if (sortedUserTreeCounts.isEmpty()) {
            return usersWithMaxTrees; // Return an empty list if the input list is empty
        }

        int maxTrees = sortedUserTreeCounts.get(0).getTreeCount(); // Get the max number of trees from the first element

        for (UserTreeCount userTreeCount : sortedUserTreeCounts) {
            if (userTreeCount.getTreeCount() == maxTrees) {
                usersWithMaxTrees.add(userTreeCount);
            } else {
                break; // Break the loop as soon as a user with fewer trees is found
            }
        }

        return usersWithMaxTrees;
    }
    
    // Function to convert list to HTML table
    public static String convertToHtmlTable(List<UserTreeCount> userTreeCounts) {
        StringBuilder htmlTable = new StringBuilder();

        htmlTable.append("<table border='1'>"); // Start of the table, you can add more styling here
        htmlTable.append("<tr><th>User ID</th><th>Number of Trees</th></tr>"); // Table header

        for (UserTreeCount userTreeCount : userTreeCounts) {
            htmlTable.append("<tr>"); // Start of row
            htmlTable.append("<td>").append(userTreeCount.getUserId()).append("</td>"); // User ID column
            htmlTable.append("<td>").append(userTreeCount.getTreeCount()).append("</td>"); // Tree count column
            htmlTable.append("</tr>"); // End of row
        }

        htmlTable.append("</table>"); // End of table

        return htmlTable.toString();
    }
    
    
    
}


























