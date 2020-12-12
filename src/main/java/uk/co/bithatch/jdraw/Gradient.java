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
 * The Class Gradient.
 */
public class Gradient {
	private float[] stops = new float[0];
	private int[][] colours = new int[0][];

	/**
	 * Instantiates a new gradient.
	 */
	public Gradient() {
	}

	/**
	 * Instantiates a new gradient.
	 *
	 * @param stops the stops
	 * @param colours the colours
	 */
	public Gradient(float[] stops, int[]... colours) {
		this.stops = stops;
		this.colours = colours;
	}

	/**
	 * Gets the stops.
	 *
	 * @return the stops
	 */
	public float[] getStops() {
		return stops;
	}

	/**
	 * Adds the.
	 *
	 * @param stop the stop
	 * @param rgb the rgb
	 */
	public void add(float stop, int[] rgb) {
		float[] nstops = new float[stops.length + 1];
		System.arraycopy(stops, 0, nstops, 0, stops.length);
		nstops[stops.length] = stop;
		stops = nstops;

		int[][] ncolours = new int[colours.length + 1][];
		System.arraycopy(colours, 0, ncolours, 0, colours.length);
		ncolours[colours.length] = rgb;
		colours = ncolours;

	}

	/**
	 * Gets the.
	 *
	 * @param position the position
	 * @return the int[]
	 */
	public int[] get(float position) {
		if (stops.length == 0)
			throw new IllegalStateException("No stops.");
		int i = 0;
		for (; i < stops.length; i++) {
			if (position >= stops[i] && (i == stops.length - 1 || position <= stops[i + 1])) {
				if (i == stops.length - 1)
					return colours[i];
				else {
					int[] from = colours[i];
					int[] to = colours[i + 1];

					float range = stops[i + 1] - stops[i];
					float opos = position - stops[i];
					float frac = opos / range;

					return getInterpolated(from, to, frac);
				}
			}
		}
		throw new IllegalStateException(String.format("%f in %d stops", position, stops.length));
	}

	int[] getInterpolated(int[] from, int[] to, float frac) {
		return new int[] { (int) ((to[0] - from[0]) * frac) + from[0], (int) ((to[1] - from[1]) * frac) + from[1],
				(int) ((to[2] - from[2]) * frac) + from[2] };
	}

	/**
	 * Gets the colours.
	 *
	 * @return the colours
	 */
	public int[][] getColours() {
		return colours;
	}

}