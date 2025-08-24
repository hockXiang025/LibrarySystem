import javafx.scene.control.cell.PropertyValueFactory;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import java.time.LocalDate;
import java.sql.*;

// Main class for library management system
public class App extends Application {
	
	// Variables for table shown and data of the book 
	private TableView<ExistedBook> bookTableView = new TableView<>();
	private TableView<ExistedBook> searchTableView = new TableView<>();
    private ObservableList<ExistedBook> bookData = FXCollections.observableArrayList();
    
    private TableView<BorrowedBook> borrowedBookTableView = new TableView<>();
    private ObservableList<BorrowedBook> borrowedBookData = FXCollections.observableArrayList();

    private TableView<ReturnedBook> returnedBookTableView = new TableView<>();
    private ObservableList<ReturnedBook> returnedBookData = FXCollections.observableArrayList();
    
    // Variable for avoiding back to main page
    private String ui = "";
    
    // Status labels 
    Label checkUserLabel = new Label("");
    Label checkUserHistoryLabel = new Label("");
    Label bookHint = new Label("");
    Label returnHint = new Label("");
	Label borrowHint = new Label("");
	Label registerHint = new Label("");
	
    public void start(Stage stage) {
    	
    	// ToolBar
        ToolBar toolBar = new ToolBar();
        Label title = new Label("  School Library Management System");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        title.setTextFill(Color.PURPLE);
        checkUserLabel.setTextFill(Color.BLUE);
        bookHint.setTextFill(Color.RED);
		returnHint.setTextFill(Color.RED);
		borrowHint.setTextFill(Color.RED);
		registerHint.setTextFill(Color.RED);
        Image logo = new Image("file:images/logo.png");
        ImageView logoView = new ImageView(logo);
        logoView.setFitHeight(50);
        logoView.setFitWidth(50);
        Pane spacer = new Pane();
        HBox.setHgrow(spacer, Priority.SOMETIMES);
        Button registerButton = new Button("Register");
        registerButton.setVisible(true);
        Button backButton = new Button("Back");
        backButton.setVisible(false);
        toolBar.getItems().addAll(logoView, title, spacer, registerButton, backButton);

		// Table for books that exist in library
        TableColumn<ExistedBook, String> isbnColumn = new TableColumn<>("ISBN");
        isbnColumn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        TableColumn<ExistedBook, String> titleColumn = new TableColumn<>("Title");
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        TableColumn<ExistedBook, String> authorColumn = new TableColumn<>("Author");
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        TableColumn<ExistedBook, String> statusColumn = new TableColumn<>("Status");
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("hasBorrowed"));
        bookTableView.getColumns().addAll(isbnColumn, titleColumn, authorColumn, statusColumn);
        bookTableView.setMaxWidth(400);
        bookTableView.setItems(bookData);
        
        // Search table for books that exist in library 
        TableColumn<ExistedBook, String> searchIsbnColumn = new TableColumn<>("ISBN");
		searchIsbnColumn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
		TableColumn<ExistedBook, String> searchTitleColumn = new TableColumn<>("Title");
		searchTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
		TableColumn<ExistedBook, String> searchAuthorColumn = new TableColumn<>("Author");
		searchAuthorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
		TableColumn<ExistedBook, String> searchStatusColumn = new TableColumn<>("Status");
		searchStatusColumn.setCellValueFactory(new PropertyValueFactory<>("hasBorrowed"));
		searchTableView.getColumns().addAll(searchIsbnColumn, searchTitleColumn, searchAuthorColumn, searchStatusColumn);
		searchTableView.setMaxWidth(420);
		searchTableView.setItems(bookData);
        
		// Table for recording the books that have been borrowed
        TableColumn<BorrowedBook, String> userColumn = new TableColumn<>("User ID");
        userColumn.setCellValueFactory(new PropertyValueFactory<>("borrowerId"));
        TableColumn<BorrowedBook, String> bookColumn = new TableColumn<>("ISBN");
        bookColumn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        TableColumn<BorrowedBook, String> borrowDateColumn = new TableColumn<>("Borrow Date");
        borrowDateColumn.setCellValueFactory(new PropertyValueFactory<>("borrowDate"));
        TableColumn<BorrowedBook, String> returnDateColumn = new TableColumn<>("Return Date");
        returnDateColumn.setCellValueFactory(new PropertyValueFactory<>("returnDate"));
        borrowedBookTableView.getColumns().addAll(userColumn, bookColumn, borrowDateColumn, returnDateColumn);
        borrowedBookTableView.setMaxWidth(350);
        borrowedBookTableView.setItems(borrowedBookData);
        borrowedBookTableView.setVisible(false);
        
        // Table for recording books that have been returned as history
        TableColumn<ReturnedBook, String> borrowerColumn = new TableColumn<>("User ID");
        borrowerColumn.setCellValueFactory(new PropertyValueFactory<>("borrowerId"));
        TableColumn<ReturnedBook, String> bookIsbnColumn = new TableColumn<>("ISBN");
        bookIsbnColumn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        TableColumn<ReturnedBook, String> borrowBookDateColumn = new TableColumn<>("Borrow Date");
        borrowBookDateColumn.setCellValueFactory(new PropertyValueFactory<>("borrowDate"));
        TableColumn<ReturnedBook, String> actualReturnDateColumn = new TableColumn<>("Return Date");
        actualReturnDateColumn.setCellValueFactory(new PropertyValueFactory<>("returnDate"));
        returnedBookTableView.getColumns().addAll(borrowerColumn, bookIsbnColumn, borrowBookDateColumn, actualReturnDateColumn);
        returnedBookTableView.setMaxWidth(350);
        returnedBookTableView.setItems(returnedBookData);
        returnedBookTableView.setVisible(false);
        
		// GUI for register borrower page
        GridPane registerPage = new GridPane();
        registerPage.setAlignment(Pos.CENTER);
        Label idLabel = new Label("User ID: ");
        TextField idTextField = new TextField();
        Label nameLabel = new Label("User name: ");
        TextField nameTextField = new TextField();
        registerPage.setHgap(10);
        registerPage.setVgap(30);
        Label registerTitle = new Label("       Register");
        registerTitle.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        registerTitle.setTextFill(Color.RED);
        registerPage.add(registerTitle, 0, 0, 2, 1);
        registerPage.add(idLabel, 0, 1);
        registerPage.add(idTextField, 1, 1);
        registerPage.add(nameLabel, 0, 2);
        registerPage.add(nameTextField, 1, 2);
        Button submitButton = new Button("Register");
        registerPage.add(submitButton, 1, 3);
        registerPage.add(registerHint, 0, 4, 8, 2);

		// GUI for manage book page
		VBox manageBookTable = new VBox();
		manageBookTable.setPadding(new Insets(20));
		manageBookTable.getChildren().add(bookTableView);
		GridPane manageBookControlPane = new GridPane();
		manageBookControlPane.setAlignment(Pos.CENTER);
		manageBookControlPane.setHgap(10);
		manageBookControlPane.setVgap(10);
		Label manageBookTitle = new Label("          Manage Book");
        manageBookTitle.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        manageBookTitle.setTextFill(Color.RED);
		Label ISBN = new Label("ISBN: ");
		Label bookTitle = new Label("Title: ");
		Label author = new Label("Author: ");
		TextField ISBNField = new TextField();
		TextField bookTitleField = new TextField();
		TextField authorField = new TextField();
		Button addButton = new Button("ADD");
		Button editButton = new Button("Edit");
		Button updateButton = new Button("Update");
		Button deleteButton = new Button("DELETE");
		manageBookControlPane.add(manageBookTitle, 0 , 0, 2, 1);
		manageBookControlPane.add(ISBN, 0, 1);
		manageBookControlPane.add(ISBNField, 1, 1);
		manageBookControlPane.add(bookTitle, 0, 2);
		manageBookControlPane.add(bookTitleField, 1, 2);
		manageBookControlPane.add(author, 0, 3);
		manageBookControlPane.add(authorField, 1, 3);
		manageBookControlPane.add(addButton, 0, 4);
		manageBookControlPane.add(editButton, 1, 4);
		manageBookControlPane.add(bookHint, 1, 5);
        
		// GUI for check borrower status page
		TextField searchUserField = new TextField();
        searchUserField.setPromptText("Search for user...");
        Button searchUserButton = new Button("Search");
        HBox searchUserBox = new HBox(10, searchUserField, searchUserButton);
        searchUserBox.setPadding(new Insets(10));
        searchUserBox.setAlignment(Pos.CENTER);
        Label checkUserTitle = new Label("Check User Status");
        checkUserTitle.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        checkUserTitle.setTextFill(Color.RED);
		VBox manageBorrowerPage = new VBox(10, checkUserTitle, searchUserBox, checkUserLabel, borrowedBookTableView);
		manageBorrowerPage.setAlignment(Pos.CENTER);
		manageBorrowerPage.setPadding(new Insets(20));		
		
		// GUI for search book page
        TextField searchBookField = new TextField();
        searchBookField.setPromptText("Search title...");
        Button searchBookButton = new Button("Search");
        HBox searchBox = new HBox(10, searchBookField, searchBookButton);
        searchBox.setPadding(new Insets(10));
        searchBox.setAlignment(Pos.CENTER);
        Label searchBookTitle = new Label("Search Book");
        searchBookTitle.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        searchBookTitle.setTextFill(Color.RED);
        VBox searchPage = new VBox(10, searchBookTitle, searchBox, searchTableView);
        searchPage.setAlignment(Pos.CENTER);
        searchPage.setPadding(new Insets(20));
        
        // GUI for return book page
        GridPane returnPage = new GridPane();
        returnPage.setAlignment(Pos.CENTER);
        Label isbnLabel = new Label("ISBN: ");
        TextField isbnTextField = new TextField();
        returnPage.setHgap(10);
        returnPage.setVgap(30);
        Label returnBookTitle = new Label("       Return Book");
        returnBookTitle.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        returnBookTitle.setTextFill(Color.RED);
        returnPage.add(returnBookTitle, 0, 0, 2, 1);
        returnPage.add(isbnLabel, 0, 1);
        returnPage.add(isbnTextField, 1, 1);
        Button searchReturnButton = new Button("Search");
        returnPage.add(searchReturnButton, 2, 1);
        Label feeLabel = new Label("");
        Button returnButton = new Button("Return");
        feeLabel.setVisible(false);
        returnButton.setVisible(false);
        returnPage.add(feeLabel, 1, 2);
		returnPage.add(returnButton, 1, 3);
		returnPage.add(returnHint, 0, 4, 8, 2);        
        
        // GUI for borrow book page
        GridPane borrowPage = new GridPane();
        borrowPage.setAlignment(Pos.CENTER);
        borrowPage.setHgap(10);
        borrowPage.setVgap(10);
        Label borrowTitle = new Label("          Borrow Book");
        borrowTitle.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        borrowTitle.setTextFill(Color.RED);
        Label userID = new Label("User ID: ");
        Label bookISBN = new Label("ISBN: ");
        Label borrowDate = new Label("Borrow Date: ");
        Label returnDate = new Label("Return Date: ");
        TextField idField = new TextField();
        TextField isbnField = new TextField();
        DatePicker borrowPicker = new DatePicker(); 
        DatePicker returnPicker = new DatePicker();
        Button borrowButton = new Button("Borrow");
        borrowPage.add(borrowTitle, 0, 0, 2, 1);
        borrowPage.add(userID, 0, 1);
        borrowPage.add(idField, 1, 1);
        borrowPage.add(bookISBN, 0, 2);
        borrowPage.add(isbnField, 1, 2);
        borrowPage.add(borrowDate, 0, 3);
        borrowPage.add(borrowPicker, 1, 3);
        borrowPage.add(returnDate, 0, 4);
        borrowPage.add(returnPicker, 1, 4);
        borrowPage.add(borrowButton, 1, 5);
        borrowPage.add(borrowHint, 1, 6);
        
        // GUI for check borrower history page
		TextField searchUserHistoryField = new TextField();
        searchUserHistoryField.setPromptText("Search for user...");
        Button searchUserHistoryButton = new Button("Search");
        HBox searchUserHistoryBox = new HBox(10, searchUserHistoryField, searchUserHistoryButton);
        searchUserHistoryBox.setPadding(new Insets(10));
        searchUserHistoryBox.setAlignment(Pos.CENTER);
        Label searchUserHistoryTitle = new Label("Check User History");
        searchUserHistoryTitle.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        searchUserHistoryTitle.setTextFill(Color.RED);
		VBox searchUserHistoryPage = new VBox(10, searchUserHistoryTitle, searchUserHistoryBox, checkUserHistoryLabel, returnedBookTableView);
		searchUserHistoryPage.setAlignment(Pos.CENTER);
		searchUserHistoryPage.setPadding(new Insets(20));
		
        // GUI for main menu
        GridPane mainMenu = new GridPane();
		mainMenu.setAlignment(Pos.CENTER);
		mainMenu.setHgap(20);
		mainMenu.setVgap(20);
		
		Image manageBookImage = new Image("file:images/manage.png");
        ImageView manageBookImageView = new ImageView(manageBookImage);
        manageBookImageView.setFitHeight(50);
        manageBookImageView.setFitWidth(50);
        Button manageBookPageButton = new Button("Manage Book", manageBookImageView);
        manageBookPageButton.setPrefSize(130, 130);
        manageBookPageButton.setContentDisplay(ContentDisplay.TOP);
        mainMenu.add(manageBookPageButton, 0, 0);
        
        Image checkUserImage = new Image("file:images/user.png");
        ImageView checkUserImageView = new ImageView(checkUserImage);
        checkUserImageView.setFitHeight(50);
        checkUserImageView.setFitWidth(50);
        Button checkUserPageButton = new Button("Check User Status", checkUserImageView);
        checkUserPageButton.setPrefSize(130, 130);
        checkUserPageButton.setContentDisplay(ContentDisplay.TOP);
        mainMenu.add(checkUserPageButton, 1, 0);
        
        Image searchImage = new Image("file:images/search.png");
        ImageView searchImageView = new ImageView(searchImage);
        searchImageView.setFitHeight(50);
        searchImageView.setFitWidth(50);
        Button searchBookPageButton = new Button("Search Book", searchImageView);
        searchBookPageButton.setPrefSize(130, 130);
        searchBookPageButton.setContentDisplay(ContentDisplay.TOP);
        mainMenu.add(searchBookPageButton, 2, 0);
        
        Image returnImage = new Image("file:images/return.png");
        ImageView returnImageView = new ImageView(returnImage);
        returnImageView.setFitHeight(50);
        returnImageView.setFitWidth(50);
        Button returnBookButton = new Button("Return Book", returnImageView);
        returnBookButton.setPrefSize(130, 130);
        returnBookButton.setContentDisplay(ContentDisplay.TOP);
        mainMenu.add(returnBookButton, 0, 1);
        
        Image borrowImage = new Image("file:images/borrow.png");
        ImageView borrowImageView = new ImageView(borrowImage);
        borrowImageView.setFitHeight(50);
        borrowImageView.setFitWidth(50);
        Button borrowBookButton = new Button("Borrow Book", borrowImageView);
        borrowBookButton.setPrefSize(130, 130);
        borrowBookButton.setContentDisplay(ContentDisplay.TOP);
        mainMenu.add(borrowBookButton, 1, 1);

        Image historyImage = new Image("file:images/file.png");
        ImageView historyImageView = new ImageView(historyImage);
        historyImageView.setFitHeight(50);
        historyImageView.setFitWidth(50);
        Button historyButton = new Button("User History", historyImageView);
        historyButton.setPrefSize(130, 130);
        historyButton.setContentDisplay(ContentDisplay.TOP);
        mainMenu.add(historyButton, 2, 1);
        
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(toolBar);
        borderPane.setCenter(mainMenu);

        // Borrow & return date in picker
        borrowPicker.setValue(LocalDate.now());
        returnPicker.setValue(LocalDate.now().plusDays(20));
        
        borrowPicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                returnPicker.setValue(newValue.plusDays(20));
            }
        });
        
        // Button
        // Register Page
        // Navigate to register page
        registerButton.setOnAction(e->{
        	borderPane.setCenter(registerPage);
        	registerButton.setVisible(false);
        	backButton.setVisible(true);
            int idNum = User.getUserCount()+1; 
        	idTextField.setText(String.format("U%04d", idNum));
        	borrowHint.setText("");
        });
        
        // Add user id and name into database
        submitButton.setOnAction(e->{
        	if (!User.checkUserExist(idTextField.getText().toUpperCase())) {
        		// Object for the user class
        		User newUser = new User(idTextField.getText().toUpperCase(),nameTextField.getText().toUpperCase());
        		switch (newUser.addUser()) {
	            	case "Success":
	            		registerHint.setTextFill(Color.GREEN);
	            		registerHint.setText("Success added");
                        int idNum = User.getUserCount()+1;
	            		idTextField.setText(String.format("U%04d", idNum));
	            		nameTextField.clear();
	            		break;
	            	case "Error":
	            		registerHint.setTextFill(Color.RED);
	            		registerHint.setText("Error occur");
	            		break;
	            	case "":
	            		registerHint.setTextFill(Color.RED);
	            		registerHint.setText("Fail registered");
	            		break;
        		} 
        	}else {
        		registerHint.setTextFill(Color.RED);
        		registerHint.setText("User existed");
        	}
        });
        
        // Back to the main page, if in edit book page back to add book page
        backButton.setOnAction(e->{
        	if (ui.equals("edit")) {
        		manageBookTitle.setText("          Manage Book");
            	manageBookControlPane.getChildren().remove(updateButton);
            	manageBookControlPane.getChildren().remove(deleteButton);
            	manageBookControlPane.add(addButton, 0, 4);
            	manageBookControlPane.add(editButton, 1, 4);
            	borderPane.setCenter(manageBookControlPane);
            	borderPane.setRight(manageBookTable);
            	backButton.setVisible(true);
            	bookData.setAll(ExistedBook.retrieveFromDatabase());
            	ui = "";
        	}else {
	        	borderPane.setRight(null);
	        	borderPane.setCenter(mainMenu);
	        	backButton.setVisible(false);
	        	borrowHint.setText("");
	        	returnHint.setText("");
                registerHint.setText("");
	        	checkUserLabel.setText("");	        	
	        	searchBookField.setText("");
	        	registerButton.setVisible(true);
	        	nameTextField.clear();
	        	searchUserField.clear();
	        	searchUserField.setPromptText("Search for user...");
	        	borrowedBookTableView.setVisible(false);
	        	searchBookField.clear();
	        	searchBookField.setPromptText("Search title...");
	        	isbnTextField.clear();
	        	feeLabel.setVisible(false);
	        	returnButton.setVisible(false);
	        	idField.clear();
	        	isbnField.clear();
	        	borrowPicker.setValue(LocalDate.now());
	        	returnPicker.setValue(LocalDate.now().plusDays(20));
	        	searchUserHistoryField.clear();
	        	searchUserHistoryField.setPromptText("Search for user...");
	        	returnedBookTableView.setVisible(false);
        	}
        	ISBNField.clear();
        	bookTitleField.clear();
        	authorField.clear();
        	bookHint.setText("");
        });
        
        // Book Management Page
        // Navigate to manage book page and read book data
        manageBookPageButton.setOnAction(e->{
        	borderPane.setCenter(manageBookControlPane);
        	borderPane.setRight(manageBookTable);
        	backButton.setVisible(true);
        	registerButton.setVisible(false);
        	bookData.setAll(ExistedBook.retrieveFromDatabase());
        });
        
        // Create new book
        addButton.setOnAction(e->{
        	if (!ISBNField.getText().isEmpty() && !bookTitleField.getText().isEmpty() && !authorField.getText().isEmpty()){
        		// Object for the existedBook class
                ExistedBook newBook = new ExistedBook(ISBNField.getText(), bookTitleField.getText(), authorField.getText(), "available");
                switch (newBook.addToDatabase()) {
                	case "Success":
                		bookHint.setTextFill(Color.GREEN);
                		bookHint.setText("Success added");
                		bookData.setAll(ExistedBook.retrieveFromDatabase());
                		ISBNField.clear();
                    	bookTitleField.clear();
                    	authorField.clear();
                		break;
                	case "Duplicate":
                		bookHint.setTextFill(Color.RED);
                		bookHint.setText("Same isbn");
                		break;
                	case "Error":
                		bookHint.setTextFill(Color.RED);
                		bookHint.setText("Error occur");
                		break;
                }
        	}
        });
        
        // Navigate to edit page
        editButton.setOnAction(e->{
        	manageBookTitle.setText("          Edit Book");
        	manageBookControlPane.getChildren().remove(addButton);
        	manageBookControlPane.getChildren().remove(editButton);
        	manageBookControlPane.add(updateButton, 0, 4);
        	manageBookControlPane.add(deleteButton, 1, 4);
        	borderPane.setCenter(manageBookControlPane);
        	borderPane.setRight(manageBookTable);
        	backButton.setVisible(true);
        	bookData.setAll(ExistedBook.retrieveFromDatabase());
        	ISBNField.clear();
        	bookTitleField.clear();
        	authorField.clear();
        	bookHint.setText("");
        	ui = "edit";
        });
        
        // Row selection listener for bookTableView
        bookTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
            	ISBNField.clear();
            	bookTitleField.clear();
            	authorField.clear();
                ISBNField.setText(newSelection.getIsbn());
                bookTitleField.setText(newSelection.getTitle());
                authorField.setText(newSelection.getAuthor());
            }
        });
        
        // Update book data
        updateButton.setOnAction(e -> {
        	// Object for the existedBook class
            ExistedBook selectedBook = bookTableView.getSelectionModel().getSelectedItem();
            
            if (selectedBook != null) {
            	String oldIsbn = selectedBook.getIsbn();
            	selectedBook.setIsbn(ISBNField.getText());
            	selectedBook.setTitle(bookTitleField.getText());
                selectedBook.setAuthor(authorField.getText());
                
                switch (selectedBook.updateBook(oldIsbn)) {
	                case "Success":
	                    bookHint.setTextFill(Color.GREEN);
	                    bookHint.setText("Success update");
	                    bookData.setAll(ExistedBook.retrieveFromDatabase());
	                    ISBNField.clear();
	                    bookTitleField.clear();
	                    authorField.clear();
	                    break;	
	                case "Duplicate":
	                    bookHint.setTextFill(Color.RED);
	                    bookHint.setText("Same isbn");
	                    bookData.setAll(ExistedBook.retrieveFromDatabase());
	                    ISBNField.clear();
	                    bookTitleField.clear();
	                    authorField.clear();
	                    break;	
	                case "Error":
	                    bookHint.setTextFill(Color.RED);
	                    bookHint.setText("Error occur");
	                    bookData.setAll(ExistedBook.retrieveFromDatabase());
	                    ISBNField.clear();
	                    bookTitleField.clear();
	                    authorField.clear();
	                    break;
	            }
            }
        });

        // Delete a book
        deleteButton.setOnAction(e->{
        	// Object for the existedBook class
        	ExistedBook selectedBook = bookTableView.getSelectionModel().getSelectedItem();
            
            if (selectedBook != null) {
            	if (selectedBook.deleteBook()) {
            		bookData.setAll(ExistedBook.retrieveFromDatabase());
            		ISBNField.clear();
                	bookTitleField.clear();
                	authorField.clear();
                	bookHint.setTextFill(Color.GREEN);
            		bookHint.setText("Success delete");
            	}
            }
        });        
        
        // Navigate to check borrower status page
        checkUserPageButton.setOnAction(e->{
        	borderPane.setCenter(manageBorrowerPage);
        	backButton.setVisible(true);
        	registerButton.setVisible(false);
        	borrowedBookData.setAll(BorrowedBook.retrieveFromDatabase());
        });
        
        // Search the borrower status
        searchUserButton.setOnAction(e->{
        	// Object for the user class
        	User info = User.retrieveUserInfo(searchUserField.getText().toUpperCase());
            checkUserLabel.setText("Name:   " + info.getUserName() + "                   User ID:   " + info.getUserId().toUpperCase());
            borrowedBookData.setAll(BorrowedBook.checkUser(searchUserField.getText().toUpperCase()));
            borrowedBookTableView.setVisible(true);
        });
        
        // Navigate to search book page
        searchBookPageButton.setOnAction(e->{
        	borderPane.setCenter(searchPage);
        	backButton.setVisible(true);
        	registerButton.setVisible(false);
        	bookData.setAll(ExistedBook.retrieveFromDatabase());
        });
        
        // Search the book
        searchBookButton.setOnAction(e->{
        	bookData.setAll(ExistedBook.searchBook(searchBookField.getText()));
        });
        
        // Navigate to return book page
        returnBookButton.setOnAction(e->{
        	borderPane.setCenter(returnPage);
        	backButton.setVisible(true);
        	registerButton.setVisible(false);
        });
        
        //  Show late fee for return
        searchReturnButton.setOnAction(e->{
        	// Object for the returnedBook class
        	ReturnedBook returnedBook = new ReturnedBook(isbnTextField.getText());
        	feeLabel.setText(returnedBook.getFees());
        	feeLabel.setVisible(true);
        	returnButton.setVisible(true);
        });
        
        // Return a book
        returnButton.setOnAction(e->{
        	// Object for the returnedBook class
        	ReturnedBook returnedBook = new ReturnedBook(isbnTextField.getText());
        		switch (returnedBook.addToDatabase()) {
        		    case "Success":
        		    	returnHint.setTextFill(Color.GREEN);
        		        returnHint.setText("Book returned successfully.");
        	        	feeLabel.setVisible(false);
        	        	returnButton.setVisible(false);
        		        break;
        		    case "Not_Borrowed":
        		    	returnHint.setTextFill(Color.RED);
        		        returnHint.setText("This book is not currently borrowed.");
        		        break;
        		    case "Error":
        		    	returnHint.setTextFill(Color.RED);
        		        returnHint.setText("An error occurred while returning the book.");
        		        break;
        		}
        	isbnTextField.clear();
        });
        
        // Navigate to the page for borrow book
        borrowBookButton.setOnAction(e->{
        	borderPane.setCenter(borrowPage);
        	backButton.setVisible(true);
        	registerButton.setVisible(false);
        });
        
        // Borrow a book
        borrowButton.setOnAction(e->{
        	LocalDate borrowLocalDate = borrowPicker.getValue();
            LocalDate returnLocalDate = returnPicker.getValue();
            returnPicker.setValue(returnLocalDate);
            if (borrowDate != null) {
                Date sqlBorrowDate = Date.valueOf(borrowLocalDate);
                Date sqlReturnDate = Date.valueOf(returnLocalDate);
                if (User.checkUserExist(idField.getText().toUpperCase())) {
                	// Object for the borrowedBook class
	                BorrowedBook borrowedBook = new BorrowedBook(idField.getText().toUpperCase(),isbnField.getText(), 
	                		sqlBorrowDate.toString(), sqlReturnDate.toString());
	                switch(borrowedBook.addToDatabase()) {
	                	case "Success":
	                		borrowHint.setTextFill(Color.GREEN);
	                		borrowHint.setText("Book borrowed successfully.");
                            registerButton.setVisible(false);
	                		break;
	                	case "Not_Available":
	                		borrowHint.setTextFill(Color.RED);
	                		borrowHint.setText("Book is not available for borrowing.");
                            registerButton.setVisible(false);
	                		break;
	                	case "No_Book":
	                		borrowHint.setTextFill(Color.RED);
	                		borrowHint.setText("No book found with the given ISBN.");
                            registerButton.setVisible(false);
	                		break;
	                	case "Error":
	                	default:
	                		borrowHint.setTextFill(Color.RED);
	                		borrowHint.setText("An error occurred while borrowing the book.");
                            registerButton.setVisible(false);
	                		break;
	                }
                } else {
                    borrowHint.setTextFill(Color.RED);
                    borrowHint.setText("The user doesn't exist! Please register.");
                    registerButton.setVisible(true);
                }
                idField.clear();
                isbnField.clear();
                borrowPicker.setValue(LocalDate.now());
                returnPicker.setValue(LocalDate.now().plusDays(20));
            }
        });
        
        // Navigate to check borrower history page
        historyButton.setOnAction(e->{
        	borderPane.setCenter(searchUserHistoryPage);
        	backButton.setVisible(true);
        	registerButton.setVisible(false);
        });
        
        // Search borrower history
        searchUserHistoryButton.setOnAction(e->{
        	returnedBookData.setAll(ReturnedBook.History(searchUserHistoryField.getText().toUpperCase()));
        	returnedBookTableView.setVisible(true);
        });
        
        Scene scene = new Scene(borderPane, 600, 400);
        stage.getIcons().add(new Image("file:images/library.png"));
        stage.setTitle("School Library Management System");
        stage.setScene(scene);
        stage.show();
    }
    
    // Launch the GUI
    public static void main(String[] args) {
        launch();
    }    
}