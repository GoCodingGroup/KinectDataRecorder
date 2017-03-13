package de.gocodinggroup.kinectdatarecorder.events;

public class KinectColorFrameEvent extends KinectFrameEvent {
	/** color frame data */
	private byte[] data;

	/**
	 * Create new KinectColorFrameEvent. Timestamp will be generated from
	 * current time
	 * 
	 * @param data
	 */
	public KinectColorFrameEvent(byte[] data) {
		// Generate timestamp
		super();

		this.data = data;
	}

	/**
	 * Create new KinectColorFrameEvent with a certain timestamp
	 * @param timestamp
	 * @param data
	 */
	public KinectColorFrameEvent(long timestamp, byte[] data) {
		// Assign the timestamp
		super(timestamp);

		this.data = data;
	}

	/**
	 * Retrieve color data stored in this event
	 * 
	 * @return
	 */
	public byte[] getData() {
		return this.data;
	}
}
