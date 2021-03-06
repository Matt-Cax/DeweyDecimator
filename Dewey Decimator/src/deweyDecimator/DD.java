package deweyDecimator;

import java.util.ArrayList;

import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class DD extends Application{

	// Declare globals
	private SQLManager sql = new SQLManager();
	private int currentUser = 0;
	private Stage stage;
	private Scene patronView;
	private Scene libView;
	private Scene adminView;
	private Scene addPatron;
	private Scene fines; 
	private Scene addAdmin;
	private Scene addLib;
	private Scene addMedia;
	private Scene editMedia;

	@Override
	public void start(Stage stage) throws Exception {
		//Create all Scenes and make them global
		this.stage = stage;
		Scene login = createLogin();
		Scene patronView = createPatronView();
		Scene libView = createLibView();
		Scene adminView = createAdminView();
		Scene addPatron = createAddPatron();
		Scene fines = createFines();
		Scene addAdmin = createAddAdmin();
		Scene addLib = createAddLib();
		Scene addMedia = createAddMedia();
		Scene editMedia = createEditMedia();
		this.patronView = patronView;
		this.libView = libView;
		this.adminView = adminView;
		this.addPatron = addPatron;
		this.fines = fines;
		this.addAdmin = addAdmin;
		this.addLib = addLib;
		this.addMedia = addMedia;
		this.editMedia = editMedia;		

		//Launch Program
		stage.setHeight(600);
		stage.setWidth(1000);
		stage.setTitle("Dewey Decimator");
		stage.setScene(login);
		stage.show();
	}

	// Creates the login scene which checks for a valid user and sets their privileges
	private Scene createLogin() {
		//Initialize Large Scale Layouts
		BorderPane bpMaster = new BorderPane();
		bpMaster.setStyle("-fx-background-color: #5e5f66");
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);

		//Populate Large Scale Layouts
		Text title = new Text("Dewey Decimator");
		title.setStyle("-fx-font-size: 72px; -fx-font-family: monospace");
		Rectangle spacer = new Rectangle(10, 50, Color.TRANSPARENT);
		Text tooltip = new Text("Enter your UserID:");
		HBox hbox = new HBox(10);
		TextField userID = new TextField();
		userID.setPrefSize(540, 20);
		Button enter = new Button("Submit");
		enter.setPrefSize(90, 20);
		hbox.getChildren().addAll(userID, enter);
		grid.add(title, 0, 0);
		grid.add(spacer, 0, 1);
		grid.add(tooltip, 0, 2);
		grid.add(hbox, 0 ,3);
		
		//Button Actions
		enter.setOnAction(e -> {
			//Grab and verify user
			String loginID = userID.getText();
			int userType = SQLManager.ERROR;
			userType = sql.verifyLogin(loginID);
			
			//Set user privileges and open home screen
			switch(userType) {
			case SQLManager.PATRON:
				currentUser = userType;
				stage.setHeight(600);
				stage.setWidth(1000);
				stage.setTitle("Dewey Decimator - Patron");
				stage.setScene(patronView);
				break;
			case SQLManager.LIBRARIAN:
				currentUser = userType;
				stage.setHeight(600);
				stage.setWidth(800);
				stage.setTitle("Dewey Decimator - Librarian");
				stage.setScene(libView);
				break;
			case SQLManager.ADMIN:
				currentUser = userType;
				stage.setHeight(200);
				stage.setWidth(500);
				stage.setTitle("Dewey Decimator - Administrator");
				stage.setScene(adminView);
				break;
			}
		});

		//Launch Scene
		bpMaster.setCenter(grid);
		Scene loginScene = new Scene(bpMaster);
		return loginScene;
	}

	//Creates the Patron view scene which allows for media to be searched for
	private Scene createPatronView()
	{
		//Create large scale layouts
		BorderPane bpMaster = new BorderPane();
		HBox menu = createTabs(); // adds the view tabs
		
		//Create secondary layouts 
		VBox options = new VBox(15);
		options.setAlignment(Pos.CENTER);
		options.setStyle("-fx-background-color: #5e5f66;");
		options.setPrefSize(Double.MAX_VALUE, Double.MAX_VALUE);
		HBox searchBar = new HBox(100);
		searchBar.setAlignment(Pos.CENTER_LEFT);
		searchBar.setStyle("-fx-border-color: #000000;"
				+ "-fx-background-color: #5e5f66; -fx-border-radius: 8 8 8 8");
		searchBar.setPadding(new Insets(0, 0, 0, 15));
		
		// Create search bar and options
		GridPane searchGrid = new GridPane();
		searchGrid.setVgap(2);
		searchGrid.setHgap(10);
		searchGrid.getColumnConstraints().add(new ColumnConstraints(50));
		Text textCO = new Text("Search");
		TextField searchCOTF = new TextField();
		searchCOTF.setPrefSize(540, 10);
		searchGrid.add(textCO, 0, 0);
		searchGrid.add(searchCOTF, 1, 0);
		Button search = new Button("SEARCH");
		search.setPrefSize(100, 30);
		
		// This vbox contains the radiobuttons
		VBox radioButtons = new VBox();
		ToggleGroup searchType = new ToggleGroup();
		RadioButton author = new RadioButton("Author");
		author.setToggleGroup(searchType);
		author.setSelected(true); // Author radio button is selected by default
		RadioButton title = new RadioButton("Title");
		title.setToggleGroup(searchType);
		RadioButton isbn = new RadioButton("ISBN");
		isbn.setToggleGroup(searchType);
		radioButtons.getChildren().addAll(author, title, isbn); // adds buttons to the vbox
		
		searchBar.getChildren().addAll(searchGrid, search, radioButtons); // adds searchgrid, search button, and radiobuttons vbox to the searchbar
		
		//Results section box
		TextArea searchOutput = new TextArea();
		searchOutput.setEditable(false);
		VBox outputVbox = new VBox(searchOutput);
	
		options.getChildren().addAll(searchBar, outputVbox); // 
		
		//Generate results of search based on user input
		search.setOnAction(e -> 
		{
			if(searchType.getSelectedToggle() == author)
			{
				
				searchOutput.setText(formatSearchOutput(sql.searchForBook("author", searchCOTF.getText())));
			}
			else if(searchType.getSelectedToggle() == title)
			{
				searchOutput.setText(formatSearchOutput(sql.searchForBook("title", searchCOTF.getText())));
			}
			else if(searchType.getSelectedToggle() == isbn)
			{
				searchOutput.setText(formatSearchOutput(sql.searchForBook("ISBN", searchCOTF.getText())));
			}
			else
			{
				searchOutput.setText("Error: no search type selected");
			}
		});
		
		
		//Launch Scene
		bpMaster.setTop(menu);
		bpMaster.setCenter(options);
		Scene patronViewScene = new Scene(bpMaster);
		return patronViewScene;
	}

	//Creates the Librarian view scene which allows for media to be checked in and out as well as 
	//patrons, media and fines be added and media edited
	private Scene createLibView() {
		//Initialize Large Scale Layouts
		BorderPane bpMaster = new BorderPane();
		HBox menu = createTabs();

		//Create check in and check out fields
		VBox options = new VBox(15);
		options.setAlignment(Pos.CENTER);
		options.setStyle("-fx-background-color: #5e5f66;");
		options.setPrefSize(Double.MAX_VALUE, Double.MAX_VALUE);
		HBox checkOut = new HBox(100);
		checkOut.setAlignment(Pos.CENTER_LEFT);
		checkOut.setStyle("-fx-border-color: #000000;"
				+ "-fx-background-color: #5e5f66; -fx-border-radius: 8 8 8 8");
		checkOut.setPadding(new Insets(0, 0, 0, 15));
		GridPane checkOutGrid = new GridPane();
		checkOutGrid.setVgap(2);
		checkOutGrid.setHgap(10);
		checkOutGrid.getColumnConstraints().add(new ColumnConstraints(50));
		Text textCO = new Text("Check Out");
		Text bookIDCO = new Text("Book ID");
		Text patronIDCO = new Text("Patron ID");
		TextField bookCOTF = new TextField();
		bookCOTF.setPrefSize(540, 10);
		TextField patronCOTF = new TextField();
		patronCOTF.setPrefSize(540, 10);
		checkOutGrid.add(textCO, 0, 0);
		checkOutGrid.add(bookIDCO, 0, 1);
		checkOutGrid.add(patronIDCO, 0, 2);
		checkOutGrid.add(bookCOTF, 1, 1);
		checkOutGrid.add(patronCOTF, 1, 2);
		Button out = new Button("OUT");
		out.setPrefSize(50, 30);
		checkOut.getChildren().addAll(checkOutGrid, out);
		HBox checkIn = new HBox(100);
		checkIn.setAlignment(Pos.CENTER_LEFT);
		checkIn.setStyle("-fx-border-color: #000000;"
				+ "-fx-background-color: #5e5f66; -fx-border-radius: 8 8 8 8");
		checkIn.setPadding(new Insets(0, 0, 0, 15));
		GridPane checkInGrid = new GridPane();
		checkInGrid.setVgap(2);
		checkInGrid.setHgap(10);
		checkInGrid.getColumnConstraints().add(new ColumnConstraints(50));
		Text textCI = new Text("Check In");
		Text bookIDCI = new Text("Book ID");
		Text patronIDCI = new Text("Patron ID");
		TextField bookCITF = new TextField();
		bookCITF.setPrefSize(540, 10);
		TextField patronCITF = new TextField();
		patronCITF.setPrefSize(540, 10);
		checkInGrid.add(textCI, 0, 0);
		checkInGrid.add(bookIDCI, 0, 1);
		checkInGrid.add(patronIDCI, 0, 2);
		checkInGrid.add(bookCITF, 1, 1);
		checkInGrid.add(patronCITF, 1, 2);
		Button in = new Button("IN");
		in.setPrefSize(50, 30);
		checkIn.getChildren().addAll(checkInGrid, in);
		HBox buttons = new HBox(100);
		buttons.setAlignment(Pos.CENTER);
		buttons.setPadding(new Insets(0, 25, 0, 25));
		Button addPatronB = new Button("Add Patron");
		addPatronB.setPrefSize(200, 50);
		Button addMediaB = new Button("Add Media");
		addMediaB.setPrefSize(200, 50);
		Button editMediaB = new Button("Edit Media");
		editMediaB.setPrefSize(200, 50);
		Button finesB = new Button("Fines");
		finesB.setPrefSize(200, 50);
		buttons.getChildren().addAll(addPatronB, addMediaB, editMediaB, finesB);
		options.getChildren().addAll(checkOut, checkIn, buttons);

		//Button Actions
		addPatronB.setOnAction(e -> {
			stage.setHeight(350);
			stage.setWidth(500);
			stage.setTitle("Add Patron");
			stage.setScene(addPatron);
		});
		finesB.setOnAction(e -> {
			stage.setHeight(200);
			stage.setWidth(500);
			stage.setTitle("Fines");
			stage.setScene(fines);
		});
		editMediaB.setOnAction(e -> {
			stage.setHeight(600);
			stage.setWidth(500);
			stage.setTitle("Edit Media");
			stage.setScene(editMedia);
		});
		out.setOnAction(e -> {
			String bookid = bookCOTF.getText();
			String uid = patronCOTF.getText();
			
			//create loan associated with card
			sql.checkOut(bookid, uid);
		});
		in.setOnAction(e -> {
			String bookid = bookCITF.getText();
			String uid = patronCITF.getText();
			
			//delete loan associated with card
			sql.checkIn(bookid, uid);
			
		});
		addMediaB.setOnAction(e -> {
			stage.setHeight(500);
			stage.setWidth(400);
			stage.setTitle("Add Media");
			stage.setScene(addMedia);
		});

		//Launch Scene
		bpMaster.setTop(menu);
		bpMaster.setCenter(options);
		Scene libViewScene = new Scene(bpMaster);
		return libViewScene;
	}

	//Creates the Administrator View which allows for admins and librarians to be added
	private Scene createAdminView() {
		//Create large scale layouts
		BorderPane bpMaster = new BorderPane();
		HBox menu = createTabs();
		VBox vbox = new VBox(5);
		vbox.setStyle("-fx-background-color: #5e5f66;");
		vbox.setAlignment(Pos.TOP_CENTER);
		vbox.setPadding(new Insets(30, 50, 30, 50));

		//Create buttons
		Button addAdminB = new Button("Add Administrator");
		addAdminB.setPrefWidth(Double.MAX_VALUE);
		Button addLibB = new Button("Add Librarian");
		addLibB.setPrefWidth(Double.MAX_VALUE);
		vbox.getChildren().addAll(addAdminB, addLibB);

		//Button actions
		addAdminB.setOnAction(e -> {
			stage.setHeight(350);
			stage.setWidth(500);
			stage.setTitle("Add Aministrator");
			stage.setScene(addAdmin);
		});
		addLibB.setOnAction(e -> {
			stage.setHeight(350);
			stage.setWidth(500);
			stage.setTitle("Add Librarian");
			stage.setScene(addLib);
		});

		//Launch Scene
		bpMaster.setTop(menu);
		bpMaster.setCenter(vbox);
		Scene adminViewScene = new Scene(bpMaster);
		return adminViewScene;
	}

	//Creates the fines scene which allows for a fine to be created
	private Scene createFines() {
		//Create Large scale layouts
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setStyle("-fx-background-color: #5e5f66;");
		grid.setVgap(15);
		grid.setHgap(15);

		//Populate large scale layouts
		Text cardNumT = new Text("Library Card #");
		Text finesT = new Text("Fines");
		TextField cardNumTF  = new TextField();
		TextField finesTF = new TextField();
		Button search = new Button("Search");
		Button save = new Button("Save");
		grid.add(cardNumT, 0, 0);
		grid.add(finesT, 0, 1);
		grid.add(cardNumTF, 1, 0);
		grid.add(finesTF, 1, 1);
		grid.add(search, 2, 1);
		grid.add(save, 2, 2);
		
		//Button Actions
		search.setOnAction(e -> {
			//get card number
			String cn = cardNumTF.getText();
			
			//pull fines (SQL)
			String f = sql.getFines(cn);
			finesTF.setText(f);
		});
		save.setOnAction(e -> {
			// get card number
			String cn = cardNumTF.getText();
			
			// get fines
			String f = finesTF.getText();
			
			// save (SQL)
			sql.setFines(cn, f);
			
			//reset to librarian view
			stage.setHeight(600);
			stage.setWidth(800);
			stage.setTitle("Dewey Decimator - Librarian");
			stage.setScene(libView);
		});

		//Launch Scene
		Scene finesScene = new Scene(grid);
		return finesScene;
	}

	//Creates the add patron scene which allows for a patron to be created
	private Scene createAddPatron() {
		//Create Large Scale layouts
		GridPane grid = new GridPane();
		grid.setStyle("-fx-background-color: #5e5f66;");
		grid.setVgap(5);
		grid.setHgap(5);
		grid.setPadding(new Insets(50, 15, 20, 15));
		grid.setAlignment(Pos.CENTER);
		ColumnConstraints c1 = new ColumnConstraints();
		c1.setHalignment(HPos.CENTER);
		ColumnConstraints c2 = new ColumnConstraints();
		c2.setHalignment(HPos.RIGHT);
		grid.getColumnConstraints().addAll(c1,c2);

		//Populate large scale layouts
		Text firstName = new Text("First Name");
		Text lastName = new Text("Last Name");
		Text address = new Text("Address");
		Text phoneNum = new Text("Phone #");
		TextField firstNameTF = new TextField();
		TextField lastNameTF = new TextField();
		TextField addressTF = new TextField();
		TextField phoneNumTF = new TextField();
		grid.add(firstName, 0, 0);
		grid.add(lastName, 0, 1);
		grid.add(address, 0, 2);
		grid.add(phoneNum, 0, 3);
		grid.add(firstNameTF, 1, 0);
		grid.add(lastNameTF, 1, 1);
		grid.add(addressTF, 1, 2);
		grid.add(phoneNumTF, 1, 3);
		Button create = new Button("Create");
		grid.add(create, 1, 4);
		
		//Button Actions
		create.setOnAction(e -> {
			//get info
			String fn = firstNameTF.getText();
			String ln = lastNameTF.getText();
			String ad = addressTF.getText();
			String pn = phoneNumTF.getText();
			
			// feed to SQL
			sql.addUser(fn, ln, ad, pn,"Patron");
			
			//reset to librarian view
			stage.setHeight(600);
			stage.setWidth(800);
			stage.setTitle("Dewey Decimator - Librarian");
			stage.setScene(libView);
			stage.setScene(libView);
		});

		//Launch Scene
		Scene addPatronScene = new Scene(grid);
		return addPatronScene;
	}

	//Creates the add media scene which allows for a new media element to be created
	private Scene createAddMedia() {
		//Create Pane and Text Fields
		Pane pane = new Pane();
		pane.setStyle("-fx-background-color: #5e5f66;");
		Text ISBN = new Text("ISBN");
		Text title = new Text("Title");
		Text author = new Text("Author");
		Text publisher = new Text("Publisher");
		Text publicationdate = new Text("Publication Date");
		Text edition = new Text("Edition");
		Text booktype = new Text("Type");
		Text medium = new Text("Medium");
		Text genre = new Text("Genre");
		Text libraryaddress = new Text("Location address");
		
		TextField ISBNTF = new TextField("12343333");
		TextField titleTF = new TextField("title");
		TextField authorTF = new TextField("author name");
		TextField publisherTF = new TextField("publisher");
		TextField publicationdateTF = new TextField("2018-01-01");
		TextField editionTF = new TextField("first edition");
		TextField booktypeTF = new TextField("YA");
		TextField mediumTF = new TextField("paper back");
		TextField genreTF = new TextField("horror");
		TextField libraryaddressTF = new TextField("123 Library Lane");
		
		//Initialize spacing variables
		int startx = 50;
		int starty = 50;
		int xspace = 150;
		int yspace = 30;
		
		//Place elements in scene
		ISBN.relocate(startx, starty);
		title.relocate(startx, starty+yspace);
		author.relocate(startx, starty+yspace*2);
		publisher.relocate(startx, starty+yspace*3);
		publicationdate.relocate(startx, starty+yspace*4);
		edition.relocate(startx, starty+yspace*5);
		booktype.relocate(startx, starty+yspace*6);
		medium.relocate(startx, starty+yspace*7);
		genre.relocate(startx, starty+yspace*8);
		libraryaddress.relocate(startx, starty+yspace*9);
		
		startx = startx + xspace;
		
		ISBNTF.relocate(startx, starty);
		titleTF.relocate(startx, starty+yspace);
		authorTF.relocate(startx, starty+yspace*2);
		publisherTF.relocate(startx, starty+yspace*3);
		publicationdateTF.relocate(startx, starty+yspace*4);
		editionTF.relocate(startx, starty+yspace*5);
		booktypeTF.relocate(startx, starty+yspace*6);
		mediumTF.relocate(startx, starty+yspace*7);
		genreTF.relocate(startx, starty+yspace*8);
		libraryaddressTF.relocate(startx, starty+yspace*9);
		
		Button addButton = new Button("add");
		addButton.relocate(startx + 110, starty+yspace*10);
		
		pane.getChildren().addAll(ISBN, title, author, publisher, publicationdate, edition, booktype, medium, genre, libraryaddress);
		pane.getChildren().addAll(ISBNTF, titleTF, authorTF, publisherTF, publicationdateTF, editionTF, booktypeTF, mediumTF, genreTF, libraryaddressTF);
		pane.getChildren().addAll(addButton);
		
		//Button Actions
		addButton.setOnAction(e -> {
			// run SQL add
			sql.addMedia(libraryaddressTF.getText(), ISBNTF.getText(), titleTF.getText(), authorTF.getText(),
					publisherTF.getText(), publicationdateTF.getText(), editionTF.getText(), booktypeTF.getText(),
					mediumTF.getText(), genreTF.getText());
			
			//reset to librarian view
			stage.setHeight(600);
			stage.setWidth(800);
			stage.setTitle("Dewey Decimator - Librarian");
			stage.setScene(libView);
		});

		//Launch Scene
		Scene addMediaScene = new Scene(pane);
		return addMediaScene;
	}
	
	//Creates the edit media scene which allows for a media element to be edited
	private Scene createEditMedia() {
		//Create pane and options
		Pane pane = new Pane();
		pane.setStyle("-fx-background-color: #5e5f66;");
		Button searchB = new Button("search");
		Button saveB = new Button("save");
		Button deleteB = new Button("delete");
		
		Text rID = new Text("ResourceID");
		TextField rIDTF = new TextField();
		
		//Position options
		rID.relocate(50 , 50);
		rIDTF.relocate(200, 50);
		searchB.relocate(375, 50);
		
		pane.getChildren().addAll(rID, rIDTF, searchB);
		
		//Create input fields
		Text ISBN = new Text("ISBN");
		Text title = new Text("Title");
		Text author = new Text("Author");
		Text publisher = new Text("Publisher");
		Text publicationdate = new Text("Publication Date");
		Text edition = new Text("Edition");
		Text booktype = new Text("Type");
		Text medium = new Text("Medium");
		Text genre = new Text("Genre");
		Text libraryaddress = new Text("Location address");
		
		TextField ISBNTF = new TextField();
		TextField titleTF = new TextField();
		TextField authorTF = new TextField();
		TextField publisherTF = new TextField();
		TextField publicationdateTF = new TextField();
		TextField editionTF = new TextField();
		TextField booktypeTF = new TextField();
		TextField mediumTF = new TextField();
		TextField genreTF = new TextField();
		TextField libraryaddressTF = new TextField();
		
		//Initialize positioning variables
		int startx = 50;
		int starty = 100;
		int xspace = 150;
		int yspace = 30;
		
		//Place input fields
		ISBN.relocate(startx, starty);
		title.relocate(startx, starty+yspace);
		author.relocate(startx, starty+yspace*2);
		publisher.relocate(startx, starty+yspace*3);
		publicationdate.relocate(startx, starty+yspace*4);
		edition.relocate(startx, starty+yspace*5);
		booktype.relocate(startx, starty+yspace*6);
		medium.relocate(startx, starty+yspace*7);
		genre.relocate(startx, starty+yspace*8);
		libraryaddress.relocate(startx, starty+yspace*9);
		
		startx = startx + xspace;
		
		ISBNTF.relocate(startx, starty);
		titleTF.relocate(startx, starty+yspace);
		authorTF.relocate(startx, starty+yspace*2);
		publisherTF.relocate(startx, starty+yspace*3);
		publicationdateTF.relocate(startx, starty+yspace*4);
		editionTF.relocate(startx, starty+yspace*5);
		booktypeTF.relocate(startx, starty+yspace*6);
		mediumTF.relocate(startx, starty+yspace*7);
		genreTF.relocate(startx, starty+yspace*8);
		libraryaddressTF.relocate(startx, starty+yspace*9);
		
		saveB.relocate(startx + 110, starty+yspace*10);
		deleteB.relocate(startx + 40, starty+yspace*10);
		
		saveB.setDisable(true);
		deleteB.setDisable(true);
		
		pane.getChildren().addAll(ISBN, title, author, publisher, publicationdate, edition, booktype, medium, genre, libraryaddress);
		pane.getChildren().addAll(ISBNTF, titleTF, authorTF, publisherTF, publicationdateTF, editionTF, booktypeTF, mediumTF, genreTF, libraryaddressTF);
		pane.getChildren().addAll(saveB, deleteB);
		
		//Button Actions
		searchB.setOnAction(e -> {
			//Find Media Element
			String rIDs = rIDTF.getText();
			String[] output = sql.getBookInfo(rIDs);
			
			// populate everything
			ISBNTF.setText(output[0]);
			titleTF.setText(output[1]);
			authorTF.setText(output[2]);
			publisherTF.setText(output[3]);
			publicationdateTF.setText(output[4]);
			editionTF.setText(output[5]);
			booktypeTF.setText(output[6]);
			mediumTF.setText(output[7]);
			genreTF.setText(output[8]);
			libraryaddressTF.setText(output[9]);
			
			//enable editing and deleting
			saveB.setDisable(false);
			deleteB.setDisable(false);
		});
		saveB.setOnAction(e -> {
			//Get user inputs
			String[] input = new String[10];
			input[0] = ISBNTF.getText();
			input[1] = titleTF.getText();
			input[2] = authorTF.getText();
			input[3] = publisherTF.getText();
			input[4] = publicationdateTF.getText();
			input[5] = editionTF.getText();
			input[6] = booktypeTF.getText();
			input[7] = mediumTF.getText();
			input[8] = genreTF.getText();
			input[9] = libraryaddressTF.getText();
			
			String rid = rIDTF.getText();
			
			//Update Media
			sql.setBookInfo(rid, input);
			
			//reset to librarian view
			stage.setHeight(600);
			stage.setWidth(800);
			stage.setTitle("Dewey Decimator - Librarian");
			stage.setScene(libView);
			
			//clear text boxes
			rIDTF.clear();
			ISBNTF.clear();
			titleTF.clear();
			authorTF.clear();
			publisherTF.clear();
			publicationdateTF.clear();
			editionTF.clear();
			booktypeTF.clear();
			mediumTF.clear();
			genreTF.clear();
			libraryaddressTF.clear();
		});
		deleteB.setOnAction(e -> {
			String rid = rIDTF.getText();
			
			//Delete media item
			sql.deleteBook(rid);
			
			//reset to librarian view
			stage.setHeight(600);
			stage.setWidth(800);
			stage.setTitle("Dewey Decimator - Librarian");
			stage.setScene(libView);
			
			//clear text boxes
			rIDTF.clear();
			ISBNTF.clear();
			titleTF.clear();
			authorTF.clear();
			publisherTF.clear();
			publicationdateTF.clear();
			editionTF.clear();
			booktypeTF.clear();
			mediumTF.clear();
			genreTF.clear();
			libraryaddressTF.clear();
		});
		
		//Launch Scene
		Scene editMediaScene = new Scene(pane);
		return editMediaScene;
	}

	//Creates the add admin scene which allows for a new admin user to be created
	private Scene createAddAdmin() {
		//Create Large Scale Scenes
		GridPane grid = new GridPane();
		grid.setStyle("-fx-background-color: #5e5f66;");
		grid.setVgap(5);
		grid.setHgap(5);
		grid.setPadding(new Insets(50, 15, 20, 15));
		grid.setAlignment(Pos.CENTER);
		ColumnConstraints c1 = new ColumnConstraints();
		c1.setHalignment(HPos.CENTER);
		ColumnConstraints c2 = new ColumnConstraints();
		c2.setHalignment(HPos.RIGHT);
		grid.getColumnConstraints().addAll(c1,c2);

		//Populate with text fields
		Text firstName = new Text("First Name");
		Text lastName = new Text("Last Name");
		Text address = new Text("Address");
		Text phoneNum = new Text("Phone #");
		TextField firstNameTF = new TextField();
		TextField lastNameTF = new TextField();
		TextField addressTF = new TextField();
		TextField phoneNumTF = new TextField();
		grid.add(firstName, 0, 0);
		grid.add(lastName, 0, 1);
		grid.add(address, 0, 2);
		grid.add(phoneNum, 0, 3);
		grid.add(firstNameTF, 1, 0);
		grid.add(lastNameTF, 1, 1);
		grid.add(addressTF, 1, 2);
		grid.add(phoneNumTF, 1, 3);
		Button create = new Button("Create");
		grid.add(create, 1, 4);
		
		//Button Actions
		create.setOnAction(e -> {
			//get info
			String fn = firstNameTF.getText();
			String ln = lastNameTF.getText();
			String ad = addressTF.getText();
			String pn = phoneNumTF.getText();
			
			// feed to SQL
			sql.addUser(fn, ln, ad, pn,"Administrator");
			
			//Reset to Admin view
			stage.setHeight(200);
			stage.setWidth(500);
			stage.setTitle("Dewey Decimator - Administrator");
			stage.setScene(adminView);
		});

		//Launch Scene
		Scene addAdminScene = new Scene(grid);
		return addAdminScene;
	}

	//Creates the add librarian scene which allos for a new librarian user to be created
	private Scene createAddLib() {
		//Create Large Scale layouts
		GridPane grid = new GridPane();
		grid.setStyle("-fx-background-color: #5e5f66;");
		grid.setVgap(5);
		grid.setHgap(5);
		grid.setPadding(new Insets(50, 15, 20, 15));
		grid.setAlignment(Pos.CENTER);
		ColumnConstraints c1 = new ColumnConstraints();
		c1.setHalignment(HPos.CENTER);
		ColumnConstraints c2 = new ColumnConstraints();
		c2.setHalignment(HPos.RIGHT);
		grid.getColumnConstraints().addAll(c1,c2);

		//Populate layouts with text fields
		Text firstName = new Text("First Name");
		Text lastName = new Text("Last Name");
		Text address = new Text("Address");
		Text phoneNum = new Text("Phone #");
		TextField firstNameTF = new TextField();
		TextField lastNameTF = new TextField();
		TextField addressTF = new TextField();
		TextField phoneNumTF = new TextField();
		grid.add(firstName, 0, 0);
		grid.add(lastName, 0, 1);
		grid.add(address, 0, 2);
		grid.add(phoneNum, 0, 3);
		grid.add(firstNameTF, 1, 0);
		grid.add(lastNameTF, 1, 1);
		grid.add(addressTF, 1, 2);
		grid.add(phoneNumTF, 1, 3);
		Button create = new Button("Create");
		grid.add(create, 1, 4);
		
		//Button Actions
		create.setOnAction(e -> {
			//get info
			String fn = firstNameTF.getText();
			String ln = lastNameTF.getText();
			String ad = addressTF.getText();
			String pn = phoneNumTF.getText();
			
			// feed to SQL
			sql.addUser(fn, ln, ad, pn,"Librarian");
			
			//Reset to Admin view
			stage.setHeight(200);
			stage.setWidth(500);
			stage.setTitle("Dewey Decimator - Administrator");
			stage.setScene(adminView);
		});

		//Launch Scene
		Scene addLibScene = new Scene(grid);
		return addLibScene;
	}

	//Creates tabs that allows users to access other views
	private HBox createTabs() {
		//Create Large Scale Layouts
		HBox menu = new HBox();
		menu.setAlignment(Pos.BOTTOM_RIGHT);
		menu.setStyle("-fx-background-color: #f0f0f0;");
		String buttonStyle = "-fx-background-color: #5e5f66;"
				+ "-fx-border-color: #000000; -fx-border-radius: 10 10 0 0"; 
		//Create a tab for each view
		Button adminScene = new Button("Admin");
		adminScene.setPrefSize(80, 20);
		adminScene.setStyle(buttonStyle);
		Button libScene = new Button("Librarian");
		libScene.setPrefSize(80, 20);
		libScene.setStyle(buttonStyle);
		Button patronScene = new Button("Patron");
		patronScene.setPrefSize(80, 20);
		patronScene.setStyle(buttonStyle);
		menu.getChildren().addAll(patronScene, libScene, adminScene);

		//Button Actions
		patronScene.setOnAction(e -> {
			stage.setHeight(600);
			stage.setWidth(1000);
			stage.setTitle("Dewey Decimator - Patron");
			stage.setScene(patronView);
		});
		libScene.setOnAction(e -> {
			if (currentUser!=SQLManager.PATRON) {
			stage.setHeight(600);
			stage.setWidth(800);
			stage.setTitle("Dewey Decimator - Librarian");
			stage.setScene(libView);
			}
		});
		adminScene.setOnAction(e -> {
			if (currentUser==SQLManager.ADMIN) {
			stage.setHeight(200);
			stage.setWidth(500);
			stage.setTitle("Dewey Decimator - Administrator");
			stage.setScene(adminView);
			}
		});

		return menu;
	}

	//Formats the tuple returned from SQL to be more pleasant to the eye
	private String formatSearchOutput(String str) {
		//Initialize strings
		StringBuilder rtnStr = new StringBuilder();
		String divider = "----------------------------------------------------------------------------\n";
		rtnStr.append(divider);
		//Split query at known token
		String[] arr = str.split("; ");
		//Determine number of tuples
		int r = (arr.length)/11 - 1;
		String[][] mat = new String[11][r+1];
		
		//Isolate tuples into rows
		for (int j=0; j<=r; j++) {
			for (int i=0; i<11; i++) {
				mat[i][j] = arr[i+j*11];
			}
		}
		
		//Create output for each tuple
		for (int k=0; k<=r; k++) {
			rtnStr.append(String.format("%s by %s\n", mat[3][k], mat[4][k]));
			rtnStr.append(String.format("Published by: %s in %s %s\n", mat[5][k], mat[6][k], mat[7][k]));
			rtnStr.append(String.format("%s, %s, %s, %s", mat[2][k], mat[8][k], mat[9][k],mat[10][k]));
			rtnStr.append("\n");
			rtnStr.append(divider);
		}
		
		//Return nice looking string
		return rtnStr.toString();
	}

	//Launch UI
	public static void main(String[] args) {
		launch(args);
	}

}
