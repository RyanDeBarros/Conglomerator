package main;

import java.io.File;
import java.io.IOException;
import javafx.geometry.VPos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import static main.App.STARTING_DIRECTORY;

public class ImageSelector extends Parent {

	private final Rectangle bkg;
	public final FileChooser fileChooser = new FileChooser();
	private final Button fileChooserBtn = new Button("Open image");
	private final Text fileName = new Text("...");
	public File imageFile;
	public final TextField x = new TextField(), y = new TextField();
	private final Button cancelButton = new Button("Remove");

	private final Text valid = new Text("\u2714"), invalid = new Text("\u2717");

	public ImageSelector(Stage stage, App app, double width, double height, Color c) throws IOException {
		super();
		bkg = new Rectangle(width, height, c);
		bkg.setStroke(Color.BLACK);
		bkg.setStrokeType(StrokeType.INSIDE);
		getChildren().addAll(bkg, fileChooserBtn, fileName, cancelButton, x, y);

		fileChooserBtn.setLayoutY(height * 0.05);
		fileChooserBtn.setLayoutX(fileChooserBtn.getLayoutY());
		fileChooserBtn.setPrefSize(0.2 * width, 0.2 * height);
		fileName.setTextOrigin(VPos.TOP);
		fileName.setFont(Font.font("Times New Roman", 16));
		fileName.setLayoutY(fileChooserBtn.getLayoutY());
		fileName.setLayoutX(fileChooserBtn.getLayoutX() + 0.23 * width);
		x.setLayoutX(width * 0.6);
		y.setLayoutX(width * 0.8);
		x.setPromptText("X pos");
		y.setPromptText("Y pos");
		x.setPrefSize(width * 0.15, height * 0.3);
		x.setLayoutY(height * 0.5);
		x.setEditable(true);
		y.setPrefSize(width * 0.15, height * 0.3);
		y.setLayoutY(height * 0.5);
		y.setEditable(true);

		fileChooser.setInitialDirectory(STARTING_DIRECTORY);
		fileChooserBtn.setOnAction(a -> {
			File select = fileChooser.showOpenDialog(stage);
			if (select != null) {
				imageFile = select;
				fileName.setText(imageFile.getParentFile().getParentFile().getName() + "/" + imageFile.getParentFile().getName() + "/" + imageFile.getName());
//				fileChooser.setInitialFileName(imageFile.getAbsolutePath());
				fileChooser.setInitialFileName(imageFile.getParent());
			}
			valid.setVisible(valid());
			invalid.setVisible(!valid());
		});

		cancelButton.setLayoutX(0.85 * width);
		cancelButton.setLayoutY(fileChooserBtn.getLayoutY());
		cancelButton.setOnAction(a -> {
			app.removeSelector(this);
		});

		getChildren().addAll(valid, invalid);
		valid.setLayoutX(width * 0.02);
		invalid.setLayoutX(valid.getLayoutX());
		valid.setLayoutY(0.8 * height);
		invalid.setLayoutY(valid.getLayoutY());

		valid.setFill(Color.LIMEGREEN);
		invalid.setFill(Color.RED);
		valid.setFont(Font.font(20));
		invalid.setFont(Font.font(20));
		valid.setVisible(false);

		x.textProperty().addListener(l -> {
			valid.setVisible(valid());
			invalid.setVisible(!valid());
		});
		y.textProperty().addListener(l -> {
			valid.setVisible(valid());
			invalid.setVisible(!valid());
		});
	}

	public boolean valid() {
		return imageFile != null
				&& !x.getText().isEmpty()
				&& !y.getText().isEmpty()
				&& valueOf(x) >= 0
				&& valueOf(y) >= 0;
	}

	public static int valueOf(TextField field) {
		try {
			return Integer.parseInt(field.getText());
		} catch (NumberFormatException e) {
			return 0;
		}
	}

}
