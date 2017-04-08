package de.gocodinggroup.kinectdatarecorder.events;

import java.nio.*;

/**
 * Depth frame representation
 * 
 * @author Dominik
 * @created 23.09.2016
 */
public class KinectDepthFrameEvent extends KinectFrameEvent {
	private static final int DEPTH_FRAME_WIDTH = 512;
	private static final int DEPTH_FRAME_HEIGHT = 424;
	private static final int DEPTH_FRAME_HEADER_SIZE = 1 * Long.BYTES + 3 + Integer.BYTES;
	private static final int DEPTH_BUFFER_SIZE = DEPTH_FRAME_HEADER_SIZE
			+ DEPTH_FRAME_WIDTH * DEPTH_FRAME_HEIGHT * Byte.BYTES + DEPTH_FRAME_WIDTH * DEPTH_FRAME_HEIGHT * Short.BYTES
			+ DEPTH_FRAME_WIDTH * DEPTH_FRAME_HEIGHT * 3 * Float.BYTES;

	/** depth frame event parameters */
	private short[] depthFrame;
	private byte[] playerIndex;
	private float[] xyz;

	private ByteBuffer b;

	/**
	 * Initialize a new DepthFrameEvent from given data. If no data is available
	 * for a particular input, it is save to specify "null"
	 * 
	 * @param timestamp
	 * @param depthFrame
	 * @param playerIndex
	 * @param xyz
	 * @param uv
	 */
	public KinectDepthFrameEvent(long timestamp, short[] depthFrame, byte[] playerIndex, float[] xyz) {
		super(timestamp);
		this.depthFrame = depthFrame;
		this.playerIndex = playerIndex;
		this.xyz = xyz;
		this.b = ByteBuffer.allocateDirect(DEPTH_BUFFER_SIZE);
	}

	/**
	 * Initialize a new DepthFrameEvent from given data. If no data is available
	 * for a particular input, it is save to specify "null"
	 * 
	 * @param depthFrame
	 * @param playerIndex
	 * @param xyz
	 * @param uv
	 */
	public KinectDepthFrameEvent(short[] depthFrame, byte[] playerIndex, float[] xyz) {
		super();
		this.depthFrame = depthFrame;
		this.playerIndex = playerIndex;
		this.xyz = xyz;
		this.b = ByteBuffer.allocateDirect(DEPTH_BUFFER_SIZE);
	}

	/**
	 * Retrieve the depthFrame information stored in this frame
	 * 
	 * @return null if no data is present
	 */
	public short[] getDepthFrame() {
		return depthFrame;
	}

	/**
	 * Retrieve the player index information stored in this frame
	 * 
	 * @return null if no data is present
	 */
	public byte[] getPlayerIndex() {
		return playerIndex;
	}

	/**
	 * Retrieve the xyz information stored in this frame
	 * 
	 * @return null if no data is present
	 */
	public float[] getXyz() {
		return xyz;
	}

	@Override
	public ByteBuffer getCompressedData() {
		b.clear();
		b.putInt(0xEBEBEBEB);
		b.putInt(DEPTH_FRAME_WIDTH);
		b.putInt(DEPTH_FRAME_HEIGHT);
		b.putLong(this.getTimestamp());

		// TODO actually compress data
		b.asShortBuffer().put(this.depthFrame);
		b.put(playerIndex);
		b.asFloatBuffer().put(xyz);

		b.flip();
		return b;
	}
}
