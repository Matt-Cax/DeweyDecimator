import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
	
	private Stage stage;
	private Scene libView;
	private Scene addPatron;
	
	@Override
	public void start(Stage stage) throws Exception {
		//Create all Scenes and make them global
		this.stage = stage;
		Scene login = createLogin();
		Scene libView = createLibView();
		Scene addPatron = createAddPatron();
		this.libView = libView;
		this.addPatron = addPatron;
		
		//Launch Program
		stage.setTitle("Dewey Decimator");
		stage.setHeight(600);
		stage.setWidth(800);
		stage.setScene(login);
		stage.show();
	}

	private Scene createLogin() {
		//Initialize Large Scale Layouts
		BorderPane bpMaster = new BorderPane();
		bpMaster.setStyle("-fx-background-color: #f5deb3");
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
			stage.setScene(libView);
		});
		
		//Launch Scene
		bpMaster.setCenter(grid);
		Scene login = new Scene(bpMaster);
		return login;
	}
	
	private Scene createLibView() {
		//Initialize Large Scale Layouts
		BorderPane bpMaster = new BorderPane();
		HBox menu = createTabs();
		
		VBox options = new VBox(15);
		options.setAlignment(Pos.CENTER);
		options.setStyle("-fx-background-color: #f5deb3;");
		HBox checkOut = new HBox(100);
		checkOut.setAlignment(Pos.CENTER_LEFT);
		checkOut.setStyle("-fx-border-color: #000000;"
				+ "-fx-background-color: #f5f5f5; -fx-border-radius: 8 8 8 8");
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
				+ "-fx-background-color: #f5f5f5; -fx-border-radius: 8 8 8 8");
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
			stage.setScene(addPatron);
		});
		
		//Launch Scene
		bpMaster.setTop(menu);
		bpMaster.setCenter(options);
		Scene patronView = new Scene(bpMaster);
		return patronView;
	}
	
	private Scene createAddPatron() {
		GridPane grid = new GridPane();
		grid.setStyle("-fx-background-color: #f5deb3;");
		grid.setPadding(new Insets(50, 15, 20, 15));
		grid.setAlignment(Pos.CENTER);
		ColumnConstraints c = new ColumnConstraints();
		c.setHalignment(HPos.RIGHT);
		grid.getColumnConstraints().addAll(new ColumnConstraints(),c);
		
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
		
		
		Scene addPatronScene = new Scene(grid);
		return addPatronScene;
	}
	
	private HBox createTabs() {
		HBox menu = new HBox();
		menu.setAlignment(Pos.BOTTOM_RIGHT);
		menu.setStyle("-fx-background-color: #f5f5f5;");
		String buttonStyle = "-fx-background-color: #f5f5f5;"
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
		libScene.setOnAction(e -> {
			stage.setTitle("Dewey Decimator - Librarian");
			stage.setScene(libView);
		});
		
		return menu;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

}
