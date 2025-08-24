import java.util.ArrayList;
import java.util.List;
import java.sql.*;

// Inheritance class of the book that exist in library 
public class ExistedBook extends Book{
    // Variable for book status, can be borrowed or not
    private String hasBorrowed;
    
    // Constructor
    protected ExistedBook(String isbn, String title, String author, String hasBorrowed) {
        super(isbn,title,author);
        this.hasBorrowed = hasBorrowed;
    }
    
    // Getter
    public String getHasBorrowed(){
        return hasBorrowed;
    }
    
    // Setter
    public void setHasBorrowed(String hasBorrowed){
        this.hasBorrowed = hasBorrowed; 
    }
    
    // Override method for add the book data to book table in database (create)
    @Override
    protected String addToDatabase() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
 
        try {
            connection = DriverManager.getConnection(jdbcUrl, username, password);
            
            // Check the isbn in the book table in database, it exist or not
            String checkSql = "SELECT isbn FROM book WHERE isbn = ?";
            preparedStatement = connection.prepareStatement(checkSql);
            preparedStatement.setString(1, getIsbn());
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return "Duplicate";
            }
            resultSet.close();
            preparedStatement.close();
            
            String sql = "INSERT INTO book (isbn, title, author, status) VALUES (?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, getIsbn());
            preparedStatement.setString(2, getTitle());
            preparedStatement.setString(3, getAuthor());
 			preparedStatement.setString(4, hasBorrowed);
 			
            if (preparedStatement.executeUpdate() > 0) {
            	return "Success";
            }
        } catch (SQLException e) {
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
    
    // Method for retrieve book data from book table in database (read)
    protected static List<ExistedBook> retrieveFromDatabase(){
    	List<ExistedBook> books = new ArrayList<>();
    	Connection connection = null;
        Statement statement = null;
    	ResultSet resultSet = null;
    	
        try{
            connection = DriverManager.getConnection(jdbcUrl, username, password);
            statement = connection.createStatement();
            String sql = "SELECT * FROM `book`";
            resultSet = statement.executeQuery(sql);

            while(resultSet.next()){
                String isbn = resultSet.getString("isbn");
                String title = resultSet.getString("title");
                String author = resultSet.getString("author");
                String status = resultSet.getString("status");
                books.add(new ExistedBook(isbn, title, author, status));
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
        return books;
	}
    
    // Method for update the book data to book table in database (update)
    protected String updateBook(String oldIsbn) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DriverManager.getConnection(jdbcUrl, username, password);
            
            // Check the isbn in the book table in database, it exist or not
            String checkSql = "SELECT isbn FROM book WHERE isbn = ?";
            preparedStatement = connection.prepareStatement(checkSql);
            preparedStatement.setString(1, getIsbn());
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return "Duplicate";
            }
            resultSet.close();
            preparedStatement.close();

            String sql = "UPDATE book SET isbn = ?, title = ?, author = ? WHERE isbn = ?";
            preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, getIsbn());
            preparedStatement.setString(2, getTitle());
            preparedStatement.setString(3, getAuthor());
            preparedStatement.setString(4, oldIsbn);

            if (preparedStatement.executeUpdate() > 0) {
            	return "Success";
            }
        } catch (SQLException e) {
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
    
    // Method for delete the book data from book table in database (delete)
    protected boolean deleteBook() {
	    Connection connection = null;
	    PreparedStatement preparedStatement = null;
	
	    try {
	        connection = DriverManager.getConnection(jdbcUrl, username, password);
	        String sql = "DELETE FROM book WHERE isbn = ?";
	        preparedStatement = connection.prepareStatement(sql);
	        preparedStatement.setString(1, getIsbn());
	        return preparedStatement.executeUpdate() > 0;
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            if (preparedStatement != null) preparedStatement.close();
	            if (connection != null) connection.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	    return false;
	}
    
    // Method for search the book data from book table in database (search)
    protected static List<ExistedBook> searchBook(String title){
    	List<ExistedBook> books = new ArrayList<>();
    	Connection connection = null;
        PreparedStatement preparedStatement = null;
    	ResultSet resultSet = null;
    	
        try{
            connection = DriverManager.getConnection(jdbcUrl, username, password);
            String sql = "SELECT * FROM `book` WHERE `title` LIKE ?";
	        preparedStatement = connection.prepareStatement(sql);
	        preparedStatement.setString(1, "%" + title + "%");
	        resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                String isbn = resultSet.getString("isbn");
                String book_title = resultSet.getString("title");
                String author = resultSet.getString("author");
                String status = resultSet.getString("status");
                books.add(new ExistedBook(isbn, book_title, author, status));
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
        return books;
	}
}
