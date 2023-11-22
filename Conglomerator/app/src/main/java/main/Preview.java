package main;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javax.imageio.ImageIO;

public class Preview extends Stage {

	private final Pane root = new Pane();
	private final FileChooser saveAs = new FileChooser();
	private final Button saveAsBtn = new Button("Save");

	public Preview(Stage stage, Image img) {
		super();
		setScene(new Scene(root));
		setTitle("Preview conglomeration");
		ImageView imgV = new ImageView(img);
		Rectangle r = new Rectangle(img.getWidth() + 2, img.getHeight() + 2, Color.TRANSPARENT);
		r.setStroke(Color.BLACK);
		r.setStrokeType(StrokeType.INSIDE);
		root.getChildren().addAll(saveAsBtn, r, imgV);
		saveAsBtn.setPrefHeight(20);
		r.setLayoutY(20);
		imgV.setLayoutY(21);
		imgV.setLayoutX(1);
		root.setMinSize(100, 80);
		saveAs.getExtensionFilters().add(new ExtensionFilter("Image", "*.png"));

		saveAsBtn.setOnAction(a -> {
			File save = saveAs.showSaveDialog(stage);
			try {
				save.createNewFile();
				ImageIO.write(SwingFXUtils.fromFXImage(img, new BufferedImage((int) img.getWidth(), (int) img.getHeight(), BufferedImage.TYPE_INT_ARGB)), "png", save);
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		});
	}

}
