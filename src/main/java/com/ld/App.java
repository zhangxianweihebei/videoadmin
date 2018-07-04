package com.ld;
import com.ld.core.Lodding;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
public class App extends Application{


	@Override
	public void start(Stage stage) throws Exception {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Index.fxml"));
		Pane load = fxmlLoader.load();
		Lodding.build().setPane(load);
		Scene scene = new Scene(load);
		stage.setScene(scene);
		stage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
