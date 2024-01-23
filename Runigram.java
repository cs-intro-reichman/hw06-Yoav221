
// This class uses the Color class, which is part of a package called awt,
// which is part of Java's standard class library.
import java.awt.Color;

/** A library of image processing functions. */
public class Runigram {

	public static void main(String[] args) {

		//// Hide / change / add to the testing code below, as needed.

		// Tests the reading and printing of an image:
		Color[][] i1 = read("ironman.ppm");
		Color[][] i2 = read("thor.ppm");

		Color[][] imageOut;

		morph(i1, i2, 50);

	}

	/**
	 * Returns a 2D array of Color values, representing the image data
	 * stored in the given PPM file.
	 */
	public static Color[][] read(String fileName) {
		In in = new In(fileName);
		// Reads the file header, ignoring the first and the third lines.
		in.readString();
		int numCols = in.readInt();
		int numRows = in.readInt();
		in.readInt();
		// Creates the image array
		Color[][] image = new Color[numRows][numCols];
		for (int i = 0; i < numRows; i++) {
			for (int j = 0; j < numCols; j++) {
				Color c = new Color(in.readInt(), in.readInt(), in.readInt());
				image[i][j] = c;
			}
		}
		// Reads the RGB values from the file, into the image array.
		// For each pixel (i,j), reads 3 values from the file,
		// creates from the 3 colors a new Color object, and
		// makes pixel (i,j) refer to that object.
		//// Replace the following statement with your code.
		return image;
	}

	// Prints the RGB values of a given color.
	public static void printColor(Color c) {

		System.out.print("(");
		System.out.printf("%3s,", c.getRed()); // Prints the red component
		System.out.printf("%3s,", c.getGreen()); // Prints the green component
		System.out.printf("%3s", c.getBlue()); // Prints the blue component
		System.out.print(")  ");
	}

	// Prints the pixels of the given image.
	// Each pixel is printed as a triplet of (r,g,b) values.
	// This function is used for debugging purposes.
	// For example, to check that some image processing function works correctly,
	// we can apply the function and then use this function to print the resulting
	// image.
	public static void print(Color[][] image) {
		for (int i = 0; i < image.length; i++) {
			for (int j = 0; j < image[0].length; j++) {
				printColor(image[i][j]);
			}
			System.out.println();
		}
		//// Replace this comment with your code
	}

	/**
	 * Returns an image which is the horizontally flipped version of the given
	 * image.
	 */
	public static Color[][] flippedHorizontally(Color[][] image) {
		int rows = image.length;
		int cols = image[0].length;
		Color[][] flipImage = new Color[rows][cols];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				flipImage[i][j] = image[i][cols - 1 - j];
			}
		}
		return flipImage;
	}

	/**
	 * Returns an image which is the vertically flipped version of the given image.
	 */
	public static Color[][] flippedVertically(Color[][] image) {
		int rows = image.length;
		int cols = image[0].length;
		Color[][] flipImage = new Color[rows][cols];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				flipImage[i][j] = image[rows - 1 - i][j];
			}
		}
		return flipImage;
	}

	// Computes the luminance of the RGB values of the given pixel, using the
	// formula
	// lum = 0.299 * r + 0.587 * g + 0.114 * b, and returns a Color object
	// consisting
	// the three values r = lum, g = lum, b = lum.
	public static Color luminance(Color pixel) {
		int R = pixel.getRed();
		int G = pixel.getGreen();
		int B = pixel.getBlue();
		int lum = (int) ((0.299 * R) + (0.587 * G) + (0.114 * B));
		Color grayPixel = new Color(lum, lum, lum);
		return grayPixel;
	}

	/**
	 * Returns an image which is the grayscaled version of the given image.
	 */
	public static Color[][] grayScaled(Color[][] image) {
		int rows = image.length;
		int cols = image[0].length;
		Color[][] grayImage = new Color[rows][cols];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				grayImage[i][j] = luminance(image[i][j]);
			}
		}
		return grayImage;
	}

	/**
	 * Returns an image which is the scaled version of the given image.
	 * The image is scaled (resized) to have the given width and height.
	 */
	public static Color[][] scaled(Color[][] image, int width, int height) {

		int h0 = image.length;
		int w0 = image[0].length;

		Color[][] scaledImage = new Color[height][width];
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				scaledImage[i][j] = image[(int) (i * ((double) h0 / height))][(int) (j * ((double) w0 / width))];
			}
		}
		return scaledImage;
	}

	/**
	 * Computes and returns a blended color which is a linear combination of the two
	 * given
	 * colors. Each r, g, b, value v in the returned color is calculated using the
	 * formula
	 * v = alpha * v1 + (1 - alpha) * v2, where v1 and v2 are the corresponding r,
	 * g, b
	 * values in the two input color.
	 */
	public static Color blend(Color c1, Color c2, double alpha) {
		int R1 = c1.getRed();
		int G1 = c1.getGreen();
		int B1 = c1.getBlue();

		int R2 = c2.getRed();
		int G2 = c2.getGreen();
		int B2 = c2.getBlue();

		int R = (int) Math.round(alpha * R1 + (1 - alpha) * R2);
		int G = (int) Math.round(alpha * G1 + (1 - alpha) * G2);
		int B = (int) Math.round(alpha * B1 + (1 - alpha) * B2);

		Color c = new Color(R, G, B);
		return c;
	}

	/**
	 * Cosntructs and returns an image which is the blending of the two given
	 * images.
	 * The blended image is the linear combination of (alpha) part of the first
	 * image
	 * and (1 - alpha) part the second image.
	 * The two images must have the same dimensions.
	 */
	public static Color[][] blend(Color[][] image1, Color[][] image2, double alpha) {
		assert (image1.length == image2.length & image1[0].length == image2[0].length)
				: "The dimensions of the two images are different";

		int height = image1.length;
		int width = image1[0].length;

		Color[][] blendImage = new Color[height][width];

		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				blendImage[i][j] = blend(image1[i][j], image2[i][j], alpha);
			}
		}
		return blendImage;
	}

	/**
	 * Morphs the source image into the target image, gradually, in n steps.
	 * Animates the morphing process by displaying the morphed image in each step.
	 * Before starting the process, scales the target image to the dimensions
	 * of the source image.
	 */
	public static void morph(Color[][] source, Color[][] target, int n) {
		int height = source.length;
		int width = source[0].length;

		if (height != target.length || width != target[0].length) {
			target = scaled(target, width, height);
		}

		Color[][] blendImage = new Color[height][width];

		for (int i = 0; i <= n; i++) {
			int alpha = (n - i) / n;
			blendImage = blend(source, target, alpha);
			display(blendImage);
			StdDraw.pause(500);
		}
	}

	/** Creates a canvas for the given image. */
	public static void setCanvas(Color[][] image) {
		StdDraw.setTitle("Runigram 2023");
		int height = image.length;
		int width = image[0].length;
		StdDraw.setCanvasSize(height, width);
		StdDraw.setXscale(0, width);
		StdDraw.setYscale(0, height);
		// Enables drawing graphics in memory and showing it on the screen only when
		// the StdDraw.show function is called.
		StdDraw.enableDoubleBuffering();
	}

	/** Displays the given image on the current canvas. */
	public static void display(Color[][] image) {
		int height = image.length;
		int width = image[0].length;
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				// Sets the pen color to the pixel color
				StdDraw.setPenColor(image[i][j].getRed(),
						image[i][j].getGreen(),
						image[i][j].getBlue());
				// Draws the pixel as a filled square of size 1
				StdDraw.filledSquare(j + 0.5, height - i - 0.5, 0.5);
			}
		}
		StdDraw.show();
	}
}
