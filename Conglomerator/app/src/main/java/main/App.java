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
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class App extends Application {

	private final ArrayList<ImageSelector> selectors = new ArrayList<>();
	private final Pane root = new Pane();
	private final FlowPane scroll = new FlowPane();
	private final ScrollPane scrP = new ScrollPane(scroll);
	public static File STARTING_DIRECTORY;
	private int selectorHeight;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) {
		root.getChildren().add(scrP);

		scrP.setPrefSize(400, 600);
		stage.widthProperty().addListener((obs, oldV, newV) -> scrP.setPrefWidth(newV.doubleValue() - 120));
		scroll.setHgap(10);
		root.setMinSize(600, 600);

		scrP.setLayoutX(100);
		Button add = new Button("Add image");
		selectorHeight = 80;
		add.setOnAction(a -> addSelector(stage, 399, Color.SILVER));
		add.setPrefSize(80, 50);
		add.setLayoutX(10);
		add.setLayoutY(10);
		root.getChildren().add(add);
		STARTING_DIRECTORY = (new DirectoryChooser()).showDialog(stage);

		Button preview = new Button("Preview");
		preview.setPrefSize(80, 50);
		preview.setLayoutX(10);
		preview.setLayoutY(70);
		preview.setOnAction(a -> preview(stage));
		root.getChildren().add(preview);

		stage.setScene(new Scene(root));
		stage.setTitle("Conglomerator");
		stage.show();
	}

	private void addSelector(Stage stage, double width, Color c) {
		try {
			ImageSelector selector = new ImageSelector(stage, this, width, selectorHeight, c);
			scroll.getChildren().add(selector);
			selectors.add(selector);
//			positionSelectors(selectors.size() - 1);
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

	private void preview(Stage stage) {
		try {
			ArrayList<ImageElement> elements = transfer(selectors);
			if (elements.isEmpty()) {
				return;
			}
			WritableImage img = process(elements);
			Preview preview = new Preview(stage, img);
			preview.initOwner(stage);
			preview.initModality(Modality.APPLICATION_MODAL);
			preview.showAndWait();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	private static ArrayList<ImageElement> transfer(ArrayList<ImageSelector> selectors) throws IOException {
		ArrayList<ImageElement> elements = new ArrayList<>();
		for (ImageSelector selector : selectors) {
			if (selector.valid()) {
				ImageElement element = new ImageElement();
				element.image = new Image(selector.imageFile.getCanonicalPath());
				element.x = ImageSelector.valueOf(selector.x);
				element.y = ImageSelector.valueOf(selector.y);
				elements.add(element);
			}
		}
		return elements;
	}

	private static WritableImage process(ArrayList<ImageElement> imageElements) {
		WritableImage wi = new WritableImage(width(imageElements), height(imageElements));
		PixelWriter pw = wi.getPixelWriter();
		for (int x = 0; x < wi.getWidth(); x++) {
			for (int y = 0; y < wi.getHeight(); y++) {
				pw.setColor(x, y, Color.TRANSPARENT);
			}
		}
		for (ImageElement imageElement : imageElements) {
			PixelReader pr = imageElement.image.getPixelReader();
			for (int i = 0; i < imageElement.image.getWidth(); i++) {
				for (int j = 0; j < imageElement.image.getHeight(); j++) {
					pw.setColor(i + imageElement.x, j + imageElement.y, pr.getColor(i, j));
				}
			}
		}
		return wi;
	}

	private static int width(ArrayList<ImageElement> imageElements) {
		int w = 0;
		for (ImageElement imageElement : imageElements) {
			if (w < imageElement.image.getWidth() + imageElement.x) {
				w = (int) imageElement.image.getWidth() + imageElement.x;
			}
		}
		return w;
	}

	private static int height(ArrayList<ImageElement> imageElements) {
		int h = 0;
		for (ImageElement imageElement : imageElements) {
			if (h < imageElement.image.getHeight() + imageElement.y) {
				h = (int) imageElement.image.getHeight() + imageElement.y;
			}
		}
		return h;
	}

}
