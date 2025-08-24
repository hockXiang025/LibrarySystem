import java.sql.*;

// User class
public class User {
    // Variables for user id and user name
    private String userId;
    private String userName;
    
    // Variables for database
    protected static final String jdbcUrl = "jdbc:mysql://localhost:3306/library";
    protected static final String username = "root";
    protected static final String password = "";

    // Constructor
    public User(String userId, String userName) {
        this.userId = userId;
        this.userName = userName;
    }

    // Getters
    public String getUserId() {
    	return userId; 
    }
    
    public String getUserName() {
    	return userName; 
    }
    
    // Method for count borrower number from user table in database to auto show the user id
    protected static int getUserCount() {
        int count = 0;
        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS total FROM users")) {
            
            if (rs.next()) {
                count = rs.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }
    
    // Method for add borrower data to user table in database (create)
    protected String addUser() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
 
        try {
            connection = DriverManager.getConnection(jdbcUrl, username, password);
            
            String sql = "INSERT INTO users (id,name) VALUES (?, ?)";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, getUserId());
            preparedStatement.setString(2, getUserName());
 			
            if (preparedStatement.executeUpdate() > 0) {
            	return "Success";
            }
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
            e.printStackTrace();
            return "Error";
        } finally {
            try {
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return "";
    }
    
    // Method for retrieve borrower data from user table in database (read)
    protected static User retrieveUserInfo(String user_id){
    	Connection connection = null;
        PreparedStatement preparedStatement = null;
    	ResultSet userResultSet = null;
    	String name = null;
    	
        try{
            connection = DriverManager.getConnection(jdbcUrl, username, password);
            String infoSql = "SELECT * FROM `users` WHERE `id` = ?";
	        preparedStatement = connection.prepareStatement(infoSql);
	        preparedStatement.setString(1, user_id.toUpperCase());
	        userResultSet = preparedStatement.executeQuery();
	        
	        if (userResultSet.next()) {
	            name = userResultSet.getString("name");
	        }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        finally{ 
            try {
                if (preparedStatement != null) preparedStatement.close();
                if (userResultSet != null) userResultSet.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return new User(user_id, name);
	}
    
    // Method for search borrower data from user table in database (search)
    protected static boolean checkUserExist(String userId) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection(jdbcUrl, username, password);
            String sql = "SELECT id FROM users WHERE id = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, userId.toUpperCase());

            resultSet = preparedStatement.executeQuery();

            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false; 
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
