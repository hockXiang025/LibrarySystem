import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

// Inheritance class of the book that have been returned
public class ReturnedBook extends Book {
    // Variables for borrower id, borrow date and return date and late fee
	private String borrowerId;
	private String borrowDate;
	private String returnDate;
	private double fees;
	
	// Constructor
    public ReturnedBook(String isbn) {
        super(isbn, "", "");
    }
    
    // Getters
    public String getBorrowerId() { 
    	return borrowerId; 
    }
    
    public String getBorrowDate() { 
    	return borrowDate; 
    }
    
    public String getReturnDate() { 
    	return returnDate; 
    }
    
    public String getFees() {
    	return "Late Fee: RM " + String.format("%.2f", fees);
    }

    // Setters
    public void setBorrowerId(String borrowerId) { 
    	this.borrowerId = borrowerId; 
    }
    
    public void setBorrowDate(String borrowDate) { 
    	this.borrowDate = borrowDate; 
    }
    
    public void setReturnDate(String returnDate) { 
    	this.returnDate = returnDate; 
    }
    
    public void setFees(double fees) {
    	this.fees = fees;
    }

    // Method for calculate the late fee for return book
    public void calculateLateFee() {
        if (borrowDate == null || returnDate == null) {
            setFees(0.0);
        }

        try {
            // Convert to LocalDate
            LocalDate borrow = LocalDate.parse(borrowDate);
            LocalDate returned = LocalDate.parse(returnDate);

            // Assume loan period = 14 days
            LocalDate dueDate = borrow.plusDays(14);

            if (returned.isAfter(dueDate)) {
                long daysLate = ChronoUnit.DAYS.between(dueDate, returned);

                // RM5 per 10 days late
                long feeUnits = (daysLate + 9) / 10;  // increase every 10 days
                setFees(feeUnits * 5.0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        setFees(0.0);
    }
    
    // Override method for add the book and borrower data to returnedBook table in database (return & create)
    @Override
    protected String addToDatabase() {
        Connection connection = null;
        PreparedStatement selectStatement = null;
        PreparedStatement insertStatement = null;
        PreparedStatement deleteStatement = null;
        PreparedStatement updateStatement = null;
        ResultSet rs = null;

        try {
            connection = DriverManager.getConnection(jdbcUrl, username, password);
            
            // Get the book data in the borrowedBook table in database
            String selectSql = "SELECT * FROM borrowedBook WHERE isbn = ?";
            selectStatement = connection.prepareStatement(selectSql);
            selectStatement.setString(1, getIsbn().toUpperCase());
            rs = selectStatement.executeQuery();

            if (!rs.next()) {
                return "Not_Borrowed";
            }

            String borrowerId = rs.getString("user_id");
            String borrowDate = rs.getString("borrow_date");

            rs.close();
            selectStatement.close();
            
            // Add the data of book that have been returned in the returnedBook table in database
            String insertSql = "INSERT INTO returnedBook (user_id, isbn, borrow_date, return_date) VALUES (?, ?, ?, NOW())";
            insertStatement = connection.prepareStatement(insertSql);
            insertStatement.setString(1, borrowerId);
            insertStatement.setString(2, getIsbn());
            insertStatement.setString(3, borrowDate);
            insertStatement.executeUpdate();
            
            // Delete the data of book that have been returned from the returnedBook table in database 
            String deleteSql = "DELETE FROM borrowedBook WHERE isbn = ?";
            deleteStatement = connection.prepareStatement(deleteSql);
            deleteStatement.setString(1, getIsbn());
            deleteStatement.executeUpdate();
            
            // Update status into available in the returnBook table in database
            String updateSql = "UPDATE book SET status = 'available' WHERE isbn = ?";
            updateStatement = connection.prepareStatement(updateSql);
            updateStatement.setString(1, getIsbn());
            updateStatement.executeUpdate();

            return "Success";

        } catch (SQLException e) {
            e.printStackTrace();
            return "Error";
        } finally {
            try {
                if (rs != null) rs.close();
                if (selectStatement != null) selectStatement.close();
                if (insertStatement != null) insertStatement.close();
                if (deleteStatement != null) deleteStatement.close();
                if (updateStatement != null) updateStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    // Method for retrieve borrower and book data from returnBook table in database (read)
    protected static List<ReturnedBook> History(String user_id){
    	List<ReturnedBook> returnedBooks = new ArrayList<>();
    	Connection connection = null;
        PreparedStatement preparedStatement = null;
    	ResultSet resultSet = null;
    	
        try{
            connection = DriverManager.getConnection(jdbcUrl, username, password);
            String sql = "SELECT * FROM `returnedBook` WHERE `user_id` = ?";
	        preparedStatement = connection.prepareStatement(sql);
	        preparedStatement.setString(1, user_id.toUpperCase());
	        resultSet = preparedStatement.executeQuery();
	        
            while(resultSet.next()){
                String id = resultSet.getString("user_id");
                String isbn = resultSet.getString("isbn");
                String borrow_date = resultSet.getString("borrow_date");
                String return_date = resultSet.getString("return_date");
                
                ReturnedBook rb = new ReturnedBook(isbn);
                
                rb.setBorrowerId(id);
                rb.setBorrowDate(borrow_date);
                rb.setReturnDate(return_date);
                returnedBooks.add(rb);
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
        return returnedBooks;
	}
}
