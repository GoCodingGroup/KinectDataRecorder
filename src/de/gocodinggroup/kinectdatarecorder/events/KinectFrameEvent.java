package de.gocodinggroup.kinectdatarecorder.events;

import java.nio.*;
import java.util.*;

import de.gocodinggroup.util.*;

/**
 * Container class for all kinect data in a frame
 * 
 * @author Dominik
 * @created 23.09.2016
 */
public abstract class KinectFrameEvent extends Event {
	/** timestamp of this frame */
	private long timestamp;

	public KinectFrameEvent() {
		this(new Date().getTime());
	}

	public KinectFrameEvent(long timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * Retrieve timestamp at which this frame occured
	 * 
	 * @return
	 */
	public long getTimestamp() {
		return timestamp;
	}

	/**
	 * Compresses all this event's data into a single byte buffer (allows for
	 * dynamic compression, and multithreading if needed)
	 * 
	 * @return
	 */
	public abstract ByteBuffer getCompressedData();
}
