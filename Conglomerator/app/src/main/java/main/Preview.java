package main;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Preview extends Stage {

	private final Pane root = new Pane();
	private final FileChooser saveAs = new FileChooser();
	private final Button saveAsBtn = new Button("Save");

	public Preview(Image img) {
		super();
		setScene(new Scene(root));
		setTitle("Preview conglomeration");
		ImageView imgV = new ImageView(img);
		Rectangle r = new Rectangle(img.getWidth(), img.getHeight(), Color.TRANSPARENT);
		r.setStroke(Color.BLACK);
		root.getChildren().addAll(saveAsBtn, r, imgV);
		saveAsBtn.setPrefSize(20, 20);
		r.setLayoutY(20);
		imgV.setLayoutY(20);
	}

}
