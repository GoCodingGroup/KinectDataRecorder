package de.gocodinggroup.kinectdatarecorder.events;

import java.nio.*;

/**
 * Skeleton Frame event representation
 * 
 * @author Dominik
 * @created 23.09.2016
 */
public class KinectSkeletonFrameEvent extends KinectFrameEvent {
	// private static final int DEPTH_FRAME_WIDTH = 512;
	// private static final int DEPTH_FRAME_HEIGHT = 424;
	// private static final int DEPTH_FRAME_HEADER_SIZE = 1 * Long.BYTES + 3 +
	// Integer.BYTES;
	// private static final int DEPTH_BUFFER_SIZE = DEPTH_FRAME_HEADER_SIZE
	// + DEPTH_FRAME_WIDTH * DEPTH_FRAME_HEIGHT * Byte.BYTES + DEPTH_FRAME_WIDTH
	// * DEPTH_FRAME_HEIGHT * Short.BYTES
	// + DEPTH_FRAME_WIDTH * DEPTH_FRAME_HEIGHT * 3 * Float.BYTES;
	// TODO find out skeleton frame event size and figure out a format to use

	/** skeleton frame event */
	private boolean[] flags;
	private float[] positions;
	private float[] orientations;
	private byte[] state;

	public KinectSkeletonFrameEvent(boolean[] flags, float[] positions, float[] orientations, byte[] state) {
		super();
		this.flags = flags;
		this.positions = positions;
		this.orientations = orientations;
		this.state = state;
	}

	public KinectSkeletonFrameEvent(long timestamp, boolean[] flags, float[] positions, float[] orientations,
			byte[] state) {
		super(timestamp);
		this.flags = flags;
		this.positions = positions;
		this.orientations = orientations;
		this.state = state;
	}

	public boolean[] getFlags() {
		return flags;
	}

	public float[] getPositions() {
		return positions;
	}

	public float[] getOrientations() {
		return orientations;
	}

	public byte[] getState() {
		return state;
	}

	@Override
	public ByteBuffer getCompressedData() {
		// TODO: implement
		return null;
	}
}
