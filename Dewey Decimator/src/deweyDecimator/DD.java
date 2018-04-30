package deweyDecimator;

import java.util.ArrayList;

import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class DD extends Application{

	private SQLManager sql = new SQLManager();
	private Stage stage;
	private Scene patronView;
	private Scene libView;
	private Scene adminView;
	private Scene addPatron;
	private Scene fines; 
	private Scene addAdmin;
	private Scene addLib;

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
		this.patronView = patronView;
		this.libView = libView;
		this.adminView = adminView;
		this.addPatron = addPatron;
		this.fines = fines;
		this.addAdmin = addAdmin;
		this.addLib = addLib;
		
		// populate from database
		

		//Launch Program
		stage.setHeight(600);
		stage.setWidth(1000);
		stage.setTitle("Dewey Decimator");
		stage.setScene(login);
		stage.show();
	}

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
		//grid.setVgrow(title, Priority.ALWAYS);
		
		//Button Actions
		enter.setOnAction(e -> {
			String loginID = userID.getText();
			int userType = SQLManager.ERROR;
			userType = sql.verifyLogin(loginID);
			
			switch(userType) {
			case SQLManager.PATRON:
				System.out.println("caught PATRON");
				stage.setScene(patronView);
				//System.out.println("SET PATRON");
				break;
			case SQLManager.LIBRARIAN:
				stage.setScene(libView);
				break;
			case SQLManager.ADMIN:
				stage.setScene(adminView);
				break;
			}
		});

		//Launch Scene
		bpMaster.setCenter(grid);
		Scene loginScene = new Scene(bpMaster);
		return loginScene;
	}

	private Scene createPatronView()
	{
		BorderPane bpMaster = new BorderPane();
		HBox menu = createTabs();
		
		VBox options = new VBox(15);
		options.setAlignment(Pos.CENTER);
		options.setStyle("-fx-background-color: #5e5f66;");
		options.setPrefSize(Double.MAX_VALUE, Double.MAX_VALUE);
		HBox searchBar = new HBox(100);
		searchBar.setAlignment(Pos.CENTER_LEFT);
		searchBar.setStyle("-fx-border-color: #000000;"
				+ "-fx-background-color: #5e5f66; -fx-border-radius: 8 8 8 8");
		searchBar.setPadding(new Insets(0, 0, 0, 15));
		
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
		
		VBox radioButtons = new VBox();
		
		ToggleGroup searchType = new ToggleGroup();
		RadioButton author = new RadioButton("Author");
		author.setToggleGroup(searchType);
		//author.setSearchType("Author"); // TODO: CREATE "setSearchType" method
		author.setSelected(true);
		RadioButton title = new RadioButton("Title");
		title.setToggleGroup(searchType);
		//title.setSearchType("Title"); // TODO: CREATE "setSearchType" method
		RadioButton isbn = new RadioButton("ISBN");
		isbn.setToggleGroup(searchType);
		//isbn.setSearchType("ISBN"); // TODO: CREATE "setSearchType" method
		radioButtons.getChildren().addAll(author, title, isbn);
		searchBar.getChildren().addAll(searchGrid, search, radioButtons);
		
		options.getChildren().addAll(searchBar);
		
		//Launch Scene
		bpMaster.setTop(menu);
		bpMaster.setCenter(options);
		Scene patronViewScene = new Scene(bpMaster);
		return patronViewScene;
	}
	
	private Scene createLibView() {
		//Initialize Large Scale Layouts
		BorderPane bpMaster = new BorderPane();
		HBox menu = createTabs();

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

		//Launch Scene
		bpMaster.setTop(menu);
		bpMaster.setCenter(options);
		Scene libViewScene = new Scene(bpMaster);
		return libViewScene;
	}

	private Scene createAdminView() {
		BorderPane bpMaster = new BorderPane();
		HBox menu = createTabs();
		VBox vbox = new VBox(5);

		vbox.setStyle("-fx-background-color: #5e5f66;");
		vbox.setAlignment(Pos.TOP_CENTER);
		vbox.setPadding(new Insets(30, 50, 30, 50));

		Button addAdminB = new Button("Add Administrator");
		addAdminB.setPrefWidth(Double.MAX_VALUE);
		Button addLibB = new Button("Add Librarian");
		addLibB.setPrefWidth(Double.MAX_VALUE);
		vbox.getChildren().addAll(addAdminB, addLibB);

		addAdminB.setOnAction(e -> {
			stage.setHeight(200);
			stage.setWidth(500);
			stage.setTitle("Add Aministrator");
			stage.setScene(addAdmin);
		});
		addLibB.setOnAction(e -> {
			stage.setHeight(200);
			stage.setWidth(500);
			stage.setTitle("Add Librarian");
			stage.setScene(addLib);
		});

		bpMaster.setTop(menu);
		bpMaster.setCenter(vbox);
		Scene adminViewScene = new Scene(bpMaster);
		return adminViewScene;
	}

	private Scene createFines() {
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setStyle("-fx-background-color: #5e5f66;");
		grid.setVgap(15);
		grid.setHgap(15);

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
		

		//TODO: back button?
		
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
		});
		
		save.setOnAction(e -> {
			sql.addFines(Integer.parseInt(cardNumTF.getText()), Double.parseDouble(finesTF.getText()));
		});

		Scene finesScene = new Scene(grid);
		return finesScene;
	}

	private Scene createAddPatron() {
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

		Text firstName = new Text("First Name");
		Text lastName = new Text("Last Name");
		Text address = new Text("Address");
		Text phoneNum = new Text("Phone #");
		TextField firstNameTF = new TextField();
		//firstNameTF.setPrefWidth(Double.MAX_VALUE);
		TextField lastNameTF = new TextField();
		//lastNameTF.setPrefWidth(Double.MAX_VALUE);
		TextField addressTF = new TextField();
		//addressTF.setPrefWidth(Double.MAX_VALUE);
		TextField phoneNumTF = new TextField();
		//phoneNumTF.setPrefWidth(Double.MAX_VALUE);
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
		
		create.setOnAction(e -> {
			//get info
			String fn = firstNameTF.getText();
			String ln = lastNameTF.getText();
			String ad = addressTF.getText();
			String pn = phoneNumTF.getText();
			
			// feed to SQL
			sql.addUser(fn, ln, ad, pn,"patron");
		});

		create.setOnAction(e -> {
			sql.addPatron(firstNameTF.getText(), lastNameTF.getText(), addressTF.getText(), phoneNumTF.getText());
		});

		Scene addPatronScene = new Scene(grid);
		return addPatronScene;
	}

	private Scene createAddAdmin() {
		GridPane grid = new GridPane();
		grid.setVgap(10);
		grid.setHgap(10);
		grid.setStyle("-fx-background-color: #5e5f66;");
		grid.setPadding(new Insets(30, 50, 30, 50));
		grid.setAlignment(Pos.TOP_CENTER);
		ColumnConstraints c = new ColumnConstraints();
		c.setHalignment(HPos.RIGHT);
		grid.getColumnConstraints().addAll(new ColumnConstraints(),c);

		Text firstName = new Text("First Name");
		Text lastName = new Text("Last Name");
		TextField firstNameTF = new TextField();
		TextField lastNameTF = new TextField();
		grid.add(firstName, 0, 0);
		grid.add(lastName, 0, 1);
		grid.add(firstNameTF, 1, 0);
		grid.add(lastNameTF, 1, 1);
		Button create = new Button("Create");
		grid.add(create, 1, 2);

		create.setOnAction(e -> {
			stage.setHeight(200);
			stage.setWidth(500);
			stage.setTitle("Dewey Decimator - Administrator");
			stage.setScene(adminView);
			sql.addAdmin(firstNameTF.getText(), lastNameTF.getText());
		});

		Scene addAdminScene = new Scene(grid);
		return addAdminScene;
	}

	private Scene createAddLib() {
		GridPane grid = new GridPane();
		grid.setVgap(10);
		grid.setHgap(10);
		grid.setStyle("-fx-background-color: #5e5f66;");
		grid.setPadding(new Insets(30, 50, 30, 50));
		grid.setAlignment(Pos.TOP_CENTER);
		ColumnConstraints c = new ColumnConstraints();
		c.setHalignment(HPos.RIGHT);
		grid.getColumnConstraints().addAll(new ColumnConstraints(),c);

		Text firstName = new Text("First Name");
		Text lastName = new Text("Last Name");
		TextField firstNameTF = new TextField();
		TextField lastNameTF = new TextField();
		grid.add(firstName, 0, 0);
		grid.add(lastName, 0, 1);
		grid.add(firstNameTF, 1, 0);
		grid.add(lastNameTF, 1, 1);
		Button create = new Button("Create");
		grid.add(create, 1, 2);

		create.setOnAction(e -> {
			stage.setHeight(200);
			stage.setWidth(500);
			stage.setTitle("Dewey Decimator - Administrator");
			stage.setScene(adminView);
			sql.addLib(firstNameTF.getText(), lastNameTF.getText());
		});

		Scene addLibScene = new Scene(grid);
		return addLibScene;
	}

	private HBox createTabs() {
		HBox menu = new HBox();
		menu.setAlignment(Pos.BOTTOM_RIGHT);
		menu.setStyle("-fx-background-color: #5e5f66;");
		String buttonStyle = "-fx-background-color: #5e5f66;"
				+ "-fx-border-color: #000000; -fx-border-radius: 10 10 0 0"; 
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
			stage.setHeight(600);
			stage.setWidth(800);
			stage.setTitle("Dewey Decimator - Librarian");
			stage.setScene(libView);
		});
		adminScene.setOnAction(e -> {
			stage.setHeight(200);
			stage.setWidth(500);
			stage.setTitle("Dewey Decimator - Administrator");
			stage.setScene(adminView);
		});

		return menu;
	}


	public static void main(String[] args) {
		
		launch(args);
	}

}
