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

import java.nio.ByteBuffer;

/**
 * The Class ByteBufferBacking.
 */
public class ByteBufferBacking implements Backing {
	private ByteBuffer buffer;
	private int height;
	private int width;
	private byte[] col = new byte[3];

	/**
	 * Instantiates a new byte buffer backing.
	 *
	 * @param width the width
	 * @param height the height
	 */
	public ByteBufferBacking(int width, int height) {
		this.width = width;
		this.height = height;
		buffer = ByteBuffer.allocateDirect(getByteSize());
	}

	/**
	 * Plot.
	 *
	 * @param x the x
	 * @param y the y
	 * @param rgb the rgb
	 */
	@Override
	public synchronized void plot(int x, int y, int[] rgb) {
		col[0] = (byte) rgb[0];
		col[1] = (byte) rgb[1];
		col[2] = (byte) rgb[2];
		int idx = y * width + x;
		if (idx < 0 || idx >= buffer.capacity())
			/* Off-buffer */
			return;
		buffer.position(idx);
		buffer.put(col);
	}

	protected int getByteSize() {
		return width * height * RGBCanvas.BPP;
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
}