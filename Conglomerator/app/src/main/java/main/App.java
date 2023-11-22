package main;

import java.util.ArrayList;
import javafx.application.Application;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.stage.Stage;

public class App extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) {

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
