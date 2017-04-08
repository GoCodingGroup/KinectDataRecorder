package de.gocodinggroup.kinectdatarecorder.events;

import java.nio.*;

public class KinectColorFrameEvent extends KinectFrameEvent {
	private static final int COLOR_HEADER_SIZE = 3 * Integer.BYTES + 1 * Long.BYTES;
	private static final int COLOR_FRAME_WIDTH = 1920;
	private static final int COLOR_FRAME_HEIGHT = 1080;
	private static final int COLOR_FRAME_BYTES_PER_PIXEL = Byte.BYTES * 4;
	private static final int COLOR_BUFFER_SIZE = COLOR_HEADER_SIZE
			+ COLOR_FRAME_WIDTH * COLOR_FRAME_HEIGHT * COLOR_FRAME_BYTES_PER_PIXEL;

	/** color frame data */
	private byte[] data;

	private ByteBuffer b;

	/**
	 * Create new KinectColorFrameEvent. Timestamp will be generated from current
	 * time
	 * 
	 * @param data
	 */
	public KinectColorFrameEvent(ByteBuffer compressedData) {
		// Generate timestamp
		super();

		this.data = new byte[COLOR_BUFFER_SIZE - COLOR_HEADER_SIZE];

		// TODO: rework
		compressedData.getInt();
		compressedData.getInt();
		compressedData.getInt();
		compressedData.getLong();
		compressedData.get(this.data);
	}

	public KinectColorFrameEvent(byte[] data) {
		super();
		this.data = data;
		this.b = ByteBuffer.allocateDirect(COLOR_BUFFER_SIZE);
	}

	/**
	 * Create new KinectColorFrameEvent with a certain timestamp
	 * 
	 * @param timestamp
	 * @param data
	 */
	public KinectColorFrameEvent(long timestamp, byte[] data) {
		// Assign the timestamp
		super(timestamp);

		this.data = data;
		this.b = ByteBuffer.allocateDirect(COLOR_BUFFER_SIZE);
	}

	/**
	 * Retrieve color data stored in this event
	 * 
	 * @return
	 */
	public byte[] getData() {
		return this.data;
	}

	@Override
	public ByteBuffer getCompressedData() {
		b.clear();
		b.putInt(0xEBEBEBEB);
		b.putInt(COLOR_FRAME_WIDTH);
		b.putInt(COLOR_FRAME_HEIGHT);
		b.putLong(this.getTimestamp());

		// TODO: actually compress
		b.put(this.getData());

		// prepare for read
		b.flip();
		return b;
	}
}
