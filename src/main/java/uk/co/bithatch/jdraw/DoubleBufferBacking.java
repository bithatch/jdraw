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

public class DoubleBufferBacking implements Backing {

	private Backing delegate;
	private int[][][] buffer;

	public DoubleBufferBacking(Backing delegate) {
		this.delegate = delegate;
		buffer = new int[delegate.getHeight()][][];
		for (int i = 0; i < delegate.getHeight(); i++) {
			buffer[i] = new int[delegate.getWidth()][3];
		}
	}

	@Override
	public int getWidth() {
		return delegate.getWidth();
	}

	@Override
	public int getHeight() {
		return delegate.getHeight();
	}

	@Override
	public void plot(int x, int y, int[] rgb) {
		buffer[y][x][0] = rgb[0];
		buffer[y][x][1] = rgb[1];
		buffer[y][x][2] = rgb[2];
	}

	public void commit() {
		int h = getHeight();
		int w = getWidth();
		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				delegate.plot(x, y, buffer[y][x]);
			}
		}
	}

}
