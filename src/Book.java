// Abstract class of the books
public abstract class Book {
    // Common variables for the books
	private String isbn;
    private String title;
    private String author;
    
    // Variables for database
    protected static final String jdbcUrl = "jdbc:mysql://localhost:3306/library";
    protected static final String username = "root";
    protected static final String password = "";
    
    // Constructor
    public Book(String isbn, String title, String author) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
    }
    
    // Getters
    public String getIsbn() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }
    
    // Setters
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
    
    // Abstract method for add the book data to database
    protected abstract String addToDatabase();
}