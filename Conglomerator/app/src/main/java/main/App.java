package main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

public class App extends Application {

	private final ArrayList<ImageSelector> selectors = new ArrayList<>();
	private final Pane root = new Pane();
	private final Pane scroll = new Pane();
	private final ScrollPane scrP = new ScrollPane(scroll);
	public static File STARTING_DIRECTORY;
	private int selectorHeight;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) {
		root.getChildren().add(scrP);

		scrP.setPrefSize(600, 600);
		scrP.setLayoutX(100);
		Button b = new Button("Add image");
		selectorHeight = 80;
		b.setOnAction(a -> addSelector(stage, 599, Color.SILVER));
		b.setPrefSize(80, 50);
		b.setLayoutX(10);
		b.setLayoutY(10);
		root.getChildren().add(b);
		STARTING_DIRECTORY = (new DirectoryChooser()).showDialog(stage);

		stage.setScene(new Scene(root));
		stage.setTitle("Conglomerator");
		stage.setResizable(false);
		stage.show();
	}

	private void addSelector(Stage stage, double width, Color c) {
		try {
			ImageSelector selector = new ImageSelector(stage, this, width, selectorHeight, c);
			scroll.getChildren().add(selector);
			selector.setLayoutY(selectors.size() * (selectorHeight + 10));
			selectors.add(selector);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public void removeSelector(ImageSelector selector) {
		int n = selectors.indexOf(selector);
		selectors.remove(selector);
		scroll.getChildren().remove(selector);
		positionSelectors(n);
	}

	private void positionSelectors(int n) {
		for (int i = n; i < selectors.size(); i++) {
			selectors.get(i).setLayoutY(i * (selectorHeight + 10));
		}
	}

	private static ArrayList<ImageElement> transfer(ArrayList<ImageSelector> selectors) throws IOException {
		ArrayList<ImageElement> elements = new ArrayList<>();
		for (ImageSelector selector : selectors) {
			ImageElement element = new ImageElement();
			element.image = new Image(App.class.getResource(selector.imageFile.getCanonicalPath()).toExternalForm());
			element.x = ImageSelector.valueOf(selector.x);
			element.y = ImageSelector.valueOf(selector.y);
			element.w = ImageSelector.valueOf(selector.w);
			element.h = ImageSelector.valueOf(selector.h);
			elements.add(element);
		}
		return elements;
	}

	private static WritableImage process(ArrayList<ImageElement> imageElements) {
		WritableImage wi = new WritableImage(width(imageElements), height(imageElements));
		PixelWriter pw = wi.getPixelWriter();
		for (ImageElement imageElement : imageElements) {
			PixelReader pr = imageElement.image.getPixelReader();
			for (int i = 0; i < imageElement.w; i++) {
				for (int j = 0; j < imageElement.h; j++) {
					pw.setColor(i + imageElement.x, j + imageElement.y, pr.getColor(i, j));
				}
			}
		}
		return wi;
	}

	private static int width(ArrayList<ImageElement> imageElements) {
		int w = 0;
		for (ImageElement imageElement : imageElements) {
			if (w < imageElement.w + imageElement.x) {
				w = imageElement.w + imageElement.x;
			}
		}
		return w;
	}

	private static int height(ArrayList<ImageElement> imageElements) {
		int h = 0;
		for (ImageElement imageElement : imageElements) {
			if (h < imageElement.h + imageElement.y) {
				h = imageElement.h + imageElement.y;
			}
		}
		return h;
	}

}
