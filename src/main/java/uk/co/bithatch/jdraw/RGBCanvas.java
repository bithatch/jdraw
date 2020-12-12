/**
 * MIT License
 * Copyright (c) 2020 Bithatch (tanktarta@gmail.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package uk.co.bithatch.jdraw;

/**
 * The Class RGBCanvas.
 */
public class RGBCanvas {

	/** The Constant MAROON. */
	public final static int[] MAROON = new int[] { 0x80, 0x00, 0x00 };
	
	/** The Constant RED. */
	public final static int[] RED = new int[] { 0xff, 0x00, 0x00 };
	
	/** The Constant YELLOW. */
	public final static int[] YELLOW = new int[] { 0xff, 0xff, 0x00 };
	
	/** The Constant GREEN. */
	public final static int[] GREEN = new int[] { 0x00, 0x80, 0x00 };
	
	/** The Constant LIME. */
	public final static int[] LIME = new int[] { 0x00, 0xff, 0x00 };
	
	/** The Constant WHITE. */
	public final static int[] WHITE = new int[] { 0xff, 0xff, 0xff };
	
	/** The Constant PINK. */
	public final static int[] PINK = new int[] { 0xff, 0x00, 0xff };
	
	/** The Constant BLUE. */
	public final static int[] BLUE = new int[] { 0x00, 0x00, 0xff };
	
	/** The Constant LIGHT_BLUE. */
	public final static int[] LIGHT_BLUE = new int[] { 0x00, 0x80, 0xff };
	
	/** The Constant CYAN. */
	public final static int[] CYAN = new int[] { 0x00, 0xff, 0xff };
	
	/** The Constant PURPLE. */
	public final static int[] PURPLE = new int[] { 0x80, 0x00, 0xff };
	
	/** The Constant ORANGE. */
	public final static int[] ORANGE = new int[] { 0xff, 0x80, 0x00 };
	
	/** The Constant BLACK. */
	public final static int[] BLACK = new int[] { 0x00, 0x00, 0x00 };

	/** The Constant QUARTER_PI. */
	public final static double QUARTER_PI = Math.PI / 4f;
	
	/** The Constant HALF_PI. */
	public final static double HALF_PI = Math.PI / 2f;
	
	/** The Constant THREE_QUARTER_PI. */
	public final static double THREE_QUARTER_PI = QUARTER_PI * 3;

	static final int BPP = 0;

	private int[] colourRGB;
	private Backing backing;
	private Gradient gradient;
	private int translateX, translateY;

	/**
	 * Instantiates a new RGB canvas.
	 *
	 * @param width the width
	 * @param height the height
	 */
	public RGBCanvas(int width, int height) {
		this(new ByteBufferBacking(width, height));
	}

	/**
	 * Instantiates a new RGB canvas.
	 *
	 * @param backing the backing
	 */
	public RGBCanvas(Backing backing) {
		this.backing = backing;
		clear();
		setColour(WHITE);
	}

	/**
	 * Translate.
	 *
	 * @param x the x
	 * @param y the y
	 */
	public void translate(int x, int y) {
		setTranslateX(x);
		setTranslateY(y);
	}

	/**
	 * Gets the translate X.
	 *
	 * @return the translate X
	 */
	public int getTranslateX() {
		return translateX;
	}

	/**
	 * Sets the translate X.
	 *
	 * @param translateX the new translate X
	 */
	public void setTranslateX(int translateX) {
		this.translateX = translateX;
	}

	/**
	 * Gets the translate Y.
	 *
	 * @return the translate Y
	 */
	public int getTranslateY() {
		return translateY;
	}

	/**
	 * Sets the translate Y.
	 *
	 * @param translateY the new translate Y
	 */
	public void setTranslateY(int translateY) {
		this.translateY = translateY;
	}

	/**
	 * Gets the gradient.
	 *
	 * @return the gradient
	 */
	public Gradient getGradient() {
		return gradient;
	}

	/**
	 * Sets the gradient.
	 *
	 * @param gradient the new gradient
	 */
	public void setGradient(Gradient gradient) {
		this.gradient = gradient;
	}

	/**
	 * Gets the colour.
	 *
	 * @return the colour
	 */
	public int[] getColour() {
		return colourRGB;
	}

	/**
	 * Sets the colour.
	 *
	 * @param colourRGB the new colour
	 */
	public void setColour(int[] colourRGB) {
		this.colourRGB = colourRGB;
	}

	/**
	 * Fill rect.
	 *
	 * @param x the x
	 * @param y the y
	 * @param w the w
	 * @param h the h
	 */
	public void fillRect(int x, int y, int w, int h) {
		if (w < 1 || h < 1)
			throw new IllegalArgumentException();

		if (gradient == null) {
			for (int yy = y; yy < y + h; yy++) {
				for (int xx = x; xx < x + w; xx++) {
					plot(xx, yy);
				}
			}
		} else {
			for (int yy = y; yy < y + h; yy++) {
				float frac = h == 1 ? 1 : (float) (yy - y) / ((float) h - 1);
				int[] rgb = gradient.get(frac);
				int fy = yy;
				withColour(() -> {
					for (int xx = x; xx < x + w; xx++) {
						plot(xx, fy);
					}
				}, rgb);

			}
		}
	}

	/**
	 * Fill circle.
	 *
	 * @param cx the cx
	 * @param cy the cy
	 * @param cr the cr
	 */
	public void fillCircle(int cx, int cy, int cr) {
		double r = cr, x, y;
		double p0 = (1) - r;
		x = 0;
		y = r;
		while (x <= y) {
			drawLine((int) (x + r) + cx, (int) (y + r) + cy, (int) (-x + r) + cx, (int) (y + r) + cy);
			drawLine((int) (y + r) + cx, (int) (x + r) + cy, (int) (-y + r) + cx, (int) (x + r) + cy);
			drawLine((int) (-x + r) + cx, (int) (-y + r) + cy, (int) (x + r) + cx, (int) (-y + r) + cy);
			drawLine((int) (-y + r) + cx, (int) (-x + r) + cy, (int) (y + r) + cx, (int) (-x + r) + cy);
			if (p0 <= 0) {
				x++;
				p0 += 1 + (2 * x);
			} else {
				x++;
				y--;
				p0 += 1 + (2 * x) - (2 * y);
			}
		}
	}

	/**
	 * Draw arc.
	 *
	 * @param cx the cx
	 * @param cy the cy
	 * @param radius the radius
	 * @param startAngle the start angle
	 * @param endAngle the end angle
	 */
	// based on Google answer by theta-ga
	public void drawArc(int cx, int cy, int radius, float startAngle, float endAngle) {
		float angle; // use as swap then use later
		float originalSA = startAngle;

		if (startAngle > endAngle) {
			float swapa = endAngle;
			endAngle = startAngle;
			startAngle = swapa;
		}
		if (startAngle == endAngle)
			return;

		// Big angle
		if (endAngle - startAngle >= 360) {
			drawCircle(cx, cy, radius);
			return;
		}

		// Shift together
		while (startAngle < 0 && endAngle < 0) {
			startAngle += 360;
			endAngle += 360;
		}
		while (startAngle > 360 && endAngle > 360) {
			startAngle -= 360;
			endAngle -= 360;
		}

		// Check if the angle to be drawn crosses 0
		boolean crossesZero = (startAngle < 0 && endAngle > 0) || (startAngle < 360 && endAngle > 360);

		// Push all values to 0 <= angle < 360
		while (startAngle >= 360)
			startAngle -= 360;
		while (endAngle >= 360)
			endAngle -= 360;
		while (startAngle < 0)
			startAngle += 360;
		while (endAngle < 0)
			endAngle += 360;

		if (endAngle == 0)
			endAngle = 360;
		else if (crossesZero) {
			drawArc(cx, cy, radius, originalSA, 359);
			startAngle = 0;
		}

		int x = 0;
		int y = radius;
		int p = 1 - radius;
		int d_e = 3;
		int d_se = -2 * radius + 5;

		do {
			// Calculate the angle the current point makes with the circle center
			angle = Math.round(Math.toDegrees(Math.atan2((float) y, (float) x)));

			// draw the circle points as long as they lie in the range specified

			if (x > 0) {
				// draw point in range 45 to 90 degrees
				if (angle >= startAngle && angle <= endAngle) {
					plot(cx + x, cy + y);
				}
				// draw point in range 180 to 225 degrees
				if (270 - angle >= startAngle && 270 - angle <= endAngle) {
					plot(cx - y, cy - x);
				}
				// draw point in range 225 to 270 degrees
				if (angle + 180 >= startAngle && angle + 180 <= endAngle) {
					plot(cx - x, cy - y);
				}
				// draw point in range 315 to 360 degrees
				if (angle + 270 >= startAngle && angle + 270 <= endAngle) {
					plot(cx + y, cy - x);
				}
			}

			// draw point in range 0 to 45 degrees
			if (90 - angle >= startAngle && 90 - angle <= endAngle) {
				plot(cx + y, cy + x);
			}

			// draw point in range 90 to 135 degrees
			if (180 - angle >= startAngle && 180 - angle <= endAngle) {
				plot(cx - x, cy + y);
			}

			// draw point in range 135 to 180 degrees
			if (angle + 90 >= startAngle && angle + 90 <= endAngle) {
				plot(cx - y, cy + x);
			}

			// draw point in range 270 to 315 degrees
			if (360 - angle >= startAngle && 360 - angle <= endAngle) {
				plot(cx + x, cy - y);
			}

			x++;

			if (p < 0) {
				p += d_e;
				d_e += 2;
				d_se += 2;
			} else {
				p += d_se;
				d_e += 2;
				d_se += 4;
				y--;
			}
		} while (x <= y);
	}

	/**
	 * Fill ellipse.
	 *
	 * @param xc the xc
	 * @param yc the yc
	 * @param rx the rx
	 * @param ry the ry
	 */
	public void fillEllipse(float xc, float yc, float rx, float ry) {
		xc += rx;
		yc += ry;

		float dx, dy, d1, d2, x, y;
		x = 0;
		y = ry;

		d1 = (ry * ry) - (rx * rx * ry) + (0.25f * rx * rx);
		dx = 2 * ry * ry * x;
		dy = 2 * rx * rx * y;

		while (dx < dy) {

			drawLine((int) (x + xc), (int) (y + yc), (int) (-x + xc), (int) (y + yc));
			drawLine((int) (x + xc), (int) (-y + yc), (int) (-x + xc), (int) (-y + yc));

			if (d1 < 0) {
				x++;
				dx = dx + (2 * ry * ry);
				d1 = d1 + dx + (ry * ry);
			} else {
				x++;
				y--;
				dx = dx + (2 * ry * ry);
				dy = dy - (2 * rx * rx);
				d1 = d1 + dx - dy + (ry * ry);
			}
		}

		d2 = ((ry * ry) * ((x + 0.5f) * (x + 0.5f))) + ((rx * rx) * ((y - 1) * (y - 1))) - (rx * rx * ry * ry);

		while (y >= 0) {

			drawLine((int) (x + xc), (int) (y + yc), (int) (-x + xc), (int) (y + yc));
			drawLine((int) (x + xc), (int) (-y + yc), (int) (-x + xc), (int) (-y + yc));

			if (d2 > 0) {
				y--;
				dy = dy - (2 * rx * rx);
				d2 = d2 + (rx * rx) - dy;
			} else {
				y--;
				x++;
				dx = dx + (2 * ry * ry);
				dy = dy - (2 * rx * rx);
				d2 = d2 + dx - dy + (rx * rx);
			}
		}
	}

	/**
	 * Draw ellipse.
	 *
	 * @param xc the xc
	 * @param yc the yc
	 * @param rx the rx
	 * @param ry the ry
	 */
	public void drawEllipse(float xc, float yc, float rx, float ry) {
		xc += rx;
		yc += ry;

		float dx, dy, d1, d2, x, y;
		x = 0;
		y = ry;

		d1 = (ry * ry) - (rx * rx * ry) + (0.25f * rx * rx);
		dx = 2 * ry * ry * x;
		dy = 2 * rx * rx * y;

		while (dx < dy) {

			plot((int) (x + xc), (int) (y + yc));
			plot((int) (-x + xc), (int) (y + yc));
			plot((int) (x + xc), (int) (-y + yc));
			plot((int) (-x + xc), (int) (-y + yc));

			if (d1 < 0) {
				x++;
				dx = dx + (2 * ry * ry);
				d1 = d1 + dx + (ry * ry);
			} else {
				x++;
				y--;
				dx = dx + (2 * ry * ry);
				dy = dy - (2 * rx * rx);
				d1 = d1 + dx - dy + (ry * ry);
			}
		}

		d2 = ((ry * ry) * ((x + 0.5f) * (x + 0.5f))) + ((rx * rx) * ((y - 1) * (y - 1))) - (rx * rx * ry * ry);

		while (y >= 0) {

			plot((int) (x + xc), (int) (y + yc));
			plot((int) (-x + xc), (int) (y + yc));
			plot((int) (x + xc), (int) (-y + yc));
			plot((int) (-x + xc), (int) (-y + yc));

			if (d2 > 0) {
				y--;
				dy = dy - (2 * rx * rx);
				d2 = d2 + (rx * rx) - dy;
			} else {
				y--;
				x++;
				dx = dx + (2 * ry * ry);
				dy = dy - (2 * rx * rx);
				d2 = d2 + dx - dy + (rx * rx);
			}
		}
	}

	/**
	 * Draw circle.
	 *
	 * @param cx the cx
	 * @param cy the cy
	 * @param cr the cr
	 */
	public void drawCircle(int cx, int cy, int cr) {
		double r = cr, x, y;
		double p0 = (1) - r;
		x = 0;
		y = r;
		while (x <= y) {
			plot((int) (x + r) + cx, (int) (y + r) + cy);
			plot((int) (y + r) + cx, (int) (x + r) + cy);
			plot((int) (-x + r) + cx, (int) (y + r) + cy);
			plot((int) (-y + r) + cx, (int) (x + r) + cy);
			plot((int) (-x + r) + cx, (int) (-y + r) + cy);
			plot((int) (-y + r) + cx, (int) (-x + r) + cy);
			plot((int) (x + r) + cx, (int) (-y + r) + cy);
			plot((int) (y + r) + cx, (int) (-x + r) + cy);
			if (p0 <= 0) {
				x++;
				p0 += 1 + (2 * x);
			} else {
				x++;
				y--;
				p0 += 1 + (2 * x) - (2 * y);
			}
		}
	}

	/**
	 * Draw rect.
	 *
	 * @param x the x
	 * @param y the y
	 * @param w the w
	 * @param h the h
	 */
	public void drawRect(int x, int y, int w, int h) {
		if (w < 1 || h < 1)
			throw new IllegalArgumentException();

		drawLine(x, y, x + w - 1, y);
		drawLine(x + w - 1, y, x + w - 1, y + h - 1);
		drawLine(x + w - 1, y + h - 1, x, y + h - 1);
		drawLine(x, y + h - 1, x, y);
	}

	/**
	 * Draw line.
	 *
	 * @param x1 the x 1
	 * @param y1 the y 1
	 * @param x2 the x 2
	 * @param y2 the y 2
	 */
	public void drawLine(int x1, int y1, int x2, int y2) {
		int x, y;
		int dx, dy;
		int incx, incy;
		int balance;

		if (x2 >= x1) {
			dx = x2 - x1;
			incx = 1;
		} else {
			dx = x1 - x2;
			incx = -1;
		}

		if (y2 >= y1) {
			dy = y2 - y1;
			incy = 1;
		} else {
			dy = y1 - y2;
			incy = -1;
		}

		x = x1;
		y = y1;

		if (dx >= dy) {
			dy <<= 1;
			balance = dy - dx;
			dx <<= 1;

			while (x != x2) {
				plot(x, y);
				if (balance >= 0) {
					y += incy;
					balance -= dx;
				}
				balance += dy;
				x += incx;
			}
			plot(x, y);
		} else {
			dx <<= 1;
			balance = dx - dy;
			dy <<= 1;

			while (y != y2) {
				plot(x, y);
				if (balance >= 0) {
					x += incx;
					balance -= dy;
				}
				balance += dx;
				y += incy;
			}
			plot(x, y);
		}

		return;
	}

	/**
	 * Plot.
	 *
	 * @param x the x
	 * @param y the y
	 */
	public void plot(int x, int y) {
		x += translateX;
		y += translateY;
		if (x < 0 || x >= backing.getWidth() || y < 0 || y >= backing.getHeight())
			return;
		backing.plot(x + translateX, y + translateY, getColour());
	}

	/**
	 * With colour.
	 *
	 * @param withColour the with colour
	 * @param rgb the rgb
	 */
	public void withColour(Runnable withColour, int[] rgb) {
		Gradient g = getGradient();
		try {
			setGradient(null);
			int[] was = getColour();
			try {
				setColour(rgb);
				withColour.run();
			} finally {
				setColour(was);
			}
		} finally {
			setGradient(g);
		}
	}

	/**
	 * With gradient.
	 *
	 * @param withColour the with colour
	 * @param gradient the gradient
	 */
	public void withGradient(Runnable withColour, Gradient gradient) {
		Gradient was = getGradient();
		try {
			setGradient(gradient);
			withColour.run();
		} finally {
			setGradient(was);
		}
	}

	/**
	 * Clear.
	 */
	public void clear() {
		withColour(() -> fillRect(0, 0, backing.getWidth(), backing.getHeight()), BLACK);
	}

	/**
	 * Gets the width.
	 *
	 * @return the width
	 */
	public int getWidth() {
		return backing.getWidth();
	}

	/**
	 * Gets the height.
	 *
	 * @return the height
	 */
	public int getHeight() {
		return backing.getHeight();
	}
}
