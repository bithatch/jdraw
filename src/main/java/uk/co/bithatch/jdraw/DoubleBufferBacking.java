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
