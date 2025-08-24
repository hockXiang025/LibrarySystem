import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// Inheritance class of the book that have been borrowed
public class BorrowedBook extends Book{
    // Variables for borrower id, borrow date and return date
    private String borrowerId;
    private String borrowDate;
    private String returnDate;
    	
    // Constructor
    protected BorrowedBook(String borrowerId, String isbn, String borrowDate, String returnDate){
        super(isbn,"","");
		this.borrowerId = borrowerId;
		this.borrowDate = borrowDate;
		this.returnDate = returnDate;
    }
    
    // Getters
    public String getBorrowerId(){
    	return borrowerId;
    }
    	
    public String getBorrowDate(){
    	return borrowDate;
    }
    	
    public String getReturnDate(){
    	return returnDate;
    }
    
    // Override method for add the book and borrower data to borrowedBook table in database (borrow & create)
    @Override
    protected String addToDatabase() {
	    Connection connection = null;
	    PreparedStatement preparedStatement = null;
	    ResultSet resultSet = null;
	    
	    try {
	        connection = DriverManager.getConnection(jdbcUrl, username, password);
	        
	        // Check the book in the book table in database can be borrowed or not
	        String checkStatusSql = "SELECT status FROM book WHERE isbn = ?";
	        preparedStatement = connection.prepareStatement(checkStatusSql);
	        preparedStatement.setString(1, getIsbn());
	        resultSet = preparedStatement.executeQuery();
	        
	        // Add the book and borrower data
	        if (resultSet.next()) {
	        	if ("available".equalsIgnoreCase(resultSet.getString("status"))) {
	                resultSet.close();
	                preparedStatement.close();
	                String insertSql = "INSERT INTO borrowedBook (user_id, isbn, borrow_date, return_date) VALUES (?, ?, ?, ?)";
	                preparedStatement = connection.prepareStatement(insertSql);
	                preparedStatement.setString(1, borrowerId);
	                preparedStatement.setString(2, getIsbn());
	                preparedStatement.setString(3, borrowDate);
	                preparedStatement.setString(4, returnDate);
	                preparedStatement.executeUpdate();

	                String updateStatusSql = "UPDATE book SET status = 'borrowed' WHERE isbn = ?";
	                preparedStatement = connection.prepareStatement(updateStatusSql);
	                preparedStatement.setString(1, getIsbn());
	                preparedStatement.executeUpdate();

	                return "Success";
	            } else {
	            	return "Not_Available";	            	
	            }
	        } else {
	        	return "No_Book";	        	
	        }
	    } catch (SQLException e) {	    	
	        e.printStackTrace();
	        return "Error";
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
    
    // Method for retrieve book and borrower data from borrowedBook table in database (read)
    protected static List<BorrowedBook> retrieveFromDatabase(){
    	List<BorrowedBook> borrowedBooks = new ArrayList<>();
    	Connection connection = null;
        Statement statement = null;
    	ResultSet resultSet = null;
    	
        try{
            connection = DriverManager.getConnection(jdbcUrl, username, password);
            statement = connection.createStatement();
            String sql = "SELECT * FROM `borrowedBook`";
            resultSet = statement.executeQuery(sql);

            while(resultSet.next()){
                String userID = resultSet.getString("user_id");
                String isbn = resultSet.getString("isbn");
                String borrow_date = resultSet.getString("borrow_date");
                String return_date = resultSet.getString("return_date");
                borrowedBooks.add(new BorrowedBook(userID, isbn, borrow_date, return_date));
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        finally{ 
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return borrowedBooks;
	}
    
    // Method for search the book and borrower data from borrowedBook table in database (search)
    protected static List<BorrowedBook> checkUser(String user_id){
    	List<BorrowedBook> borrowedBooks = new ArrayList<>();
    	Connection connection = null;
        PreparedStatement preparedStatement = null;
    	ResultSet resultSet = null;
    	
        try{
            connection = DriverManager.getConnection(jdbcUrl, username, password);
            String sql = "SELECT * FROM `borrowedBook` WHERE `user_id` = ?";
	        preparedStatement = connection.prepareStatement(sql);
	        preparedStatement.setString(1, user_id.toUpperCase());
	        resultSet = preparedStatement.executeQuery();
	        
            while(resultSet.next()){
                String id = resultSet.getString("user_id");
                String isbn = resultSet.getString("isbn");
                String borrow_date = resultSet.getString("borrow_date");
                String return_date = resultSet.getString("return_date");
                borrowedBooks.add(new BorrowedBook(id, isbn, borrow_date, return_date));
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        finally{ 
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return borrowedBooks;
	}
}