package main;

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
import javafx.stage.Stage;

public class App extends Application {

	private final ArrayList<ImageSelector> selectors = new ArrayList<>();
	private final Pane root = new Pane();
	private final Pane scroll = new Pane();
	private final ScrollPane scrP = new ScrollPane(scroll);
	private int y = 0;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) {
		root.getChildren().add(scrP);

		scrP.setPrefSize(500, 600);
		scrP.setLayoutX(100);
		Button b = new Button("Add image");
		b.setOnAction(a -> addSelector(500, 80, Color.SILVER));
		b.setPrefSize(80, 50);
		b.setLayoutX(10);
		b.setLayoutY(10);
		root.getChildren().add(b);

		stage.setScene(new Scene(root));
		stage.setTitle("Conglomerator");
		stage.show();
	}

	private void addSelector(double width, double height, Color c) {
		ImageSelector selector = new ImageSelector(width, height, c);
		scroll.getChildren().add(selector);
		selector.setLayoutY(y);
		y += height + 10;
		selectors.add(selector);
	}

	private static ArrayList<ImageElement> transfer(ArrayList<ImageSelector> selectors) throws IOException {
		ArrayList<ImageElement> elements = new ArrayList<>();
		for (ImageSelector selector : selectors) {
			ImageElement element = new ImageElement();
			element.image = new Image(App.class.getResource(selector.imageFile.getCanonicalPath()).toExternalForm());
			element.x = selector.spinX.getValue();
			element.y = selector.spinY.getValue();
			element.w = selector.spinW.getValue();
			element.h = selector.spinH.getValue();
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
