package main;

import java.io.File;
import java.io.IOException;
import javafx.geometry.VPos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
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
	public final Spinner<Integer> spinX = new Spinner<>(), spinY = new Spinner<>(), spinW = new Spinner<>(), spinH = new Spinner<>();
	private final Spinner spins[] = {spinX, spinY, spinW, spinH};
	private final Button cancelButton = new Button("Remove");

	public ImageSelector(Stage stage, App app, double width, double height, Color c) throws IOException {
		super();
		bkg = new Rectangle(width, height, c);
		getChildren().addAll(bkg, fileChooserBtn, fileName, cancelButton);
		getChildren().addAll(spins);

		fileChooserBtn.setLayoutY(height * 0.05);
		fileChooserBtn.setLayoutX(fileChooserBtn.getLayoutY());
		fileChooserBtn.setPrefWidth(0.2 * width);
		fileName.setTextOrigin(VPos.TOP);
		fileName.setFont(Font.font("Times New Roman", 16));
		fileName.setLayoutY(fileChooserBtn.getLayoutY());
		fileName.setLayoutX(fileChooserBtn.getLayoutX() + 0.23 * width);
		spinX.setLayoutX(width * 0.2);
		spinY.setLayoutX(width * 0.4);
		spinW.setLayoutX(width * 0.6);
		spinH.setLayoutX(width * 0.8);
		for (Spinner spin : spins) {
			spin.setPrefSize(width * 0.15, height * 0.3);
			spin.setLayoutY(height * 0.5);
		}

		fileChooser.setInitialDirectory(STARTING_DIRECTORY);
		fileChooserBtn.setOnAction(a -> {
			File select = fileChooser.showOpenDialog(stage);
			if (select != null) {
				imageFile = select;
				fileName.setText(imageFile.getParentFile().getParentFile().getName() + "/" + imageFile.getParentFile().getName() + "/" + imageFile.getName());
			}
		});

		cancelButton.setLayoutX(0.85 * width);
		cancelButton.setOnAction(a -> {
			app.removeSelector(this);
		});
	}

}
