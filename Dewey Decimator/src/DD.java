import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class DD extends Application{
	
	private Stage stage;
	private Scene login;
	private Scene patronView;
	
	@Override
	public void start(Stage stage) throws Exception {
		//Create all Scenes and make them global
		this.stage = stage;
		Scene login = createLogin();
		this.login = login;
		Scene patronView = createPatronView();
		this.patronView = patronView;
		
		//Launch Program
		stage.setTitle("Dewey Decimator");
		stage.setHeight(900);
		stage.setWidth(1200);
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
		Rectangle spacer = new Rectangle(10, 200, Color.TRANSPARENT);
		Text tooltip = new Text("Enter your UserID:");
		HBox hbox = new HBox(10);
		TextField userID = new TextField();
		Button enter = new Button("Submit");
		hbox.getChildren().addAll(userID, enter);
		grid.add(title, 0, 0);
		grid.add(spacer, 0, 1);
		grid.add(tooltip, 0, 2);
		grid.add(hbox, 0 ,3);
		
		//Button Actions
		enter.setOnAction(e -> {
			stage.setScene(patronView);
		});
		
		//Launch Scene
		bpMaster.setCenter(grid);
		Scene login = new Scene(bpMaster);
		return login;
	}
	
	private Scene createPatronView() {
		//Initialize Large Scale Layouts
		BorderPane bpMaster = new BorderPane();
		HBox menu = createTabs();
		
		//Launch Scene
		bpMaster.setTop(menu);
		Scene patronView = new Scene(bpMaster);
		return patronView;
	}
	
	private HBox createTabs() {
		HBox menu = new HBox();
		menu.setAlignment(Pos.BOTTOM_RIGHT);
		menu.setStyle("-fx-background-color: #f5deb3;");
		String buttonStyle = "-fx-background-color: #f5f5f5;"
				+ "-fx-border-color: #000000; -fx-border-radius: 10 10 0 0"; 
		Button adminScene = new Button("Admin");
		adminScene.setPrefSize(150, 50);
		adminScene.setStyle(buttonStyle);
		Button libScene = new Button("Librarian");
		libScene.setPrefSize(150, 50);
		libScene.setStyle(buttonStyle);
		Button patronScene = new Button("Patron");
		patronScene.setPrefSize(150, 50);
		patronScene.setStyle(buttonStyle);
		menu.getChildren().addAll(patronScene, libScene, adminScene);
		
		//TODO Button Actions
		
		
		return menu;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

}
