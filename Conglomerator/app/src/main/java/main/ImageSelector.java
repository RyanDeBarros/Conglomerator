package main;

import java.io.File;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

public class ImageSelector extends Parent {

	private final Rectangle bkg;
	public final FileChooser fileChooser = new FileChooser();
	public final Button fileChooserBtn = new Button();
	private final Text fileName = new Text("...");
	public File imageFile;
	public final Spinner<Integer> spinX = new Spinner<>(), spinY = new Spinner<>(), spinW = new Spinner<>(), spinH = new Spinner<>();
	private final Spinner spins[] = {spinX, spinY, spinW, spinH};

	public ImageSelector(double width, double height, Color c) {
		super();
		bkg = new Rectangle(width, height, c);
		getChildren().addAll(bkg, fileChooserBtn, fileName);
		getChildren().addAll(spins);
	}

}
