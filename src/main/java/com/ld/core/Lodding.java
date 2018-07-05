package com.ld.core;

import com.jfoenix.controls.JFXSpinner;

import javafx.application.Platform;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Lodding {
	private static Lodding kit = new Lodding();
	Stage stg;
	Pane pane;
	JFXSpinner jfxSpinner = new JFXSpinner();
	public void setPane(Pane pane) {
		this.pane = pane;
	}
	public static Lodding build() {
		synchronized (kit) {
			if(kit.stg == null) {
				kit.stg = new Stage();
			}
			return kit;
		}
	}
	
	public void show() {
		jfxSpinner.setLayoutY(300);
		jfxSpinner.setLayoutX(600);
		Platform.runLater(()->{
			pane.getChildren().remove(jfxSpinner);
			pane.getChildren().add(jfxSpinner);
		});
	}
	
	public void hide() {
		Platform.runLater(()->{
			pane.getChildren().remove(jfxSpinner);
		});
		
	}

}
