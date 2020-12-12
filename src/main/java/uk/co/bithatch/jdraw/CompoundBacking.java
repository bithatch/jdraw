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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The Class CompoundBacking.
 */
public class CompoundBacking implements Backing {

	private List<Backing> backings = new ArrayList<>();
	private int height;
	private int width;

	/**
	 * Instantiates a new compound backing.
	 *
	 * @param backings the backings
	 */
	public CompoundBacking(Backing... backings) {
		this.backings.addAll(Arrays.asList(backings));
		recalcSize();
	}

	/**
	 * Adds a Backing.
	 *
	 * @param backing the backing
	 */
	public void add(Backing backing) {
		synchronized (backings) {
			this.backings.add(backing);
			recalcSize();
		}
	}

	/**
	 * Removes a Backing.
	 *
	 * @param backing the backing
	 */
	public void remove(Backing backing) {
		synchronized (backings) {
			this.backings.add(backing);
			recalcSize();
		}
	}

	/**
	 * Gets the width.
	 *
	 * @return the width
	 */
	@Override
	public int getWidth() {
		return width;
	}

	/**
	 * Gets the height.
	 *
	 * @return the height
	 */
	@Override
	public int getHeight() {
		return height;
	}

	/**
	 * Plot.
	 *
	 * @param x   the x
	 * @param y   the y
	 * @param rgb the rgb
	 */
	@Override
	public void plot(int x, int y, int[] rgb) {
		synchronized (backings) {
			for (Backing b : backings)
				b.plot(x, y, rgb);
		}
	}

	protected void recalcSize() {
		for (Backing b : backings) {
			width = Math.max(width, b.getWidth());
			height = Math.max(height, b.getHeight());
		}
	}
}
