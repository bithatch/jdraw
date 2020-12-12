# JDraw

A very simple, no-dependencies drawing API for Java, providing some primitive operations including lines, rectangles, circles, ellipses and arcs. 

It doesn't actually draw TO anything, you must provide your own implementation of `Backing` to actually paint the pixels. 

This project is intended for use with Snake, my project for Razer devices on Linux, but is made available in case others may find it useful. 

It doesn't have anything fancy like anti-alias, transformations or even line widths, and there are currently no plans to develop it any further, but contributions are of course welcome!

## Dependencies

No dependencies other than Java itself.

## Configuring your project

The library will be available in Maven Central, but for now is available from the SNAPSHOT repository.

### Maven

```xml
	<dependency>
		<groupId>uk.co.bithatch</groupId>
		<artifactId>jdraw</artifactId>
		<version>0.1-SNAPSHOT</version>
	</dependency>
```

## Usage

To add to your own project, first you need an implementation of `Backing`. For example, if you wanted to draw on a `BufferedImage`, you would do the following. 

**Note this is a pointless example really, as BufferedImage can be drawn on with Java's own Graphics API, but it is a quick way to get some useful output from JDraw**.

```java

public class MyBacking implements Backing {

	public BufferedImage img;

	public 	MyBacking() {
		img = new BufferedImage(512, 512, BufferedImage.TYPE_INT_ARGB);
	}

	@Override
	public int getWidth() {
		return img.getWidth();
	}

	@Override
	public int getHeight() {
		return img.getHeight();
	}

	@Override
	public void plot(int x, int y, int[] rgb) {
		bimg.setRGB(x, y, (0xff << 24) | (rgb[0] << 16) | (rgb[1] << 8) | (rgb[2]));
		draw.repaint();
	}
}
```

Now you have a `Backing` implementation, you can draw on it using JDraw. The following will draw  a 100x100 red rectangle at 10, 10.

```java
	MyBacking backing = new MyBacking();
	RGBCanvas canvas = new RGBCanvas(backing);
	
	canvas.setColour(RGBCanvas.RED);
	canvas.fillRect(10, 10, 100, 100);
	
	// Now do something useful with your drawing, for example save it as a PNG..
	ImageIO.write(backing.img, "png", new File("my-image.png"));
	
```


