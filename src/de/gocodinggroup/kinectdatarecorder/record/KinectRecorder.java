package de.gocodinggroup.kinectdatarecorder.record;

import java.io.*;
import java.nio.*;

import de.gocodinggroup.kinectdatarecorder.events.*;
import de.gocodinggroup.util.*;

/**
 * Use this Class to capture data from the Kinect This is essentially throwaway
 * code and thusly not implemented nicely. Don't use this for educational
 * purposes
 * 
 * @author Dominik
 *
 */
public class KinectRecorder {
	/** The color file stream */
	private final FileOutputStream colorStream;

	/** The depth file stream */
	private final FileOutputStream depthStream;

	/** The skeleton file stream */
	private final FileOutputStream skeletonStream;

	/** Buffers for the data */
	private final ByteBuffer colorBuffer;
	private final ByteBuffer depthBuffer;
	private final ByteBuffer skeletonBuffer;

	/** Listeners for writing data on FrameEvents */
	private final EventListener colorListener;
	private final EventListener depthListener;
	private final EventListener skeletonListener;

	/** Maximum amount of frames to capture */
	private int frames;

	/**
	 * Creates a new KinectRecorder that can record color information
	 * 
	 * @param colorFile
	 * @throws FileNotFoundException
	 */
	public KinectRecorder(File colorFile) throws FileNotFoundException {
		this.colorStream = new FileOutputStream(colorFile);
		this.depthStream = null;
		this.skeletonStream = null;

		this.colorBuffer = ByteBuffer.allocateDirect(1920 * 1080 * 4);
		this.depthBuffer = null;
		this.skeletonBuffer = null;

		this.colorListener = new EventListener() {
			@Override
			public void eventReceived(Event event) {
				try {
					frames--;
					if (frames <= 0) return;

					colorBuffer.put(((KinectColorFrameEvent) event).getData());
					colorBuffer.flip();
					colorStream.getChannel().write(colorBuffer);
					colorBuffer.clear();
				} catch (IOException e) {
					System.err.println("Writing color data failed!");
					e.printStackTrace();
				}
			}
		};
		this.depthListener = null;
		this.skeletonListener = null;
	}

	/**
	 * Creates a new KinectRecorder that can record depth and skeleton
	 * information
	 * 
	 * @param depthFile
	 * @param skeletonFile
	 * @throws FileNotFoundException
	 */
	public KinectRecorder(File depthFile, File skeletonFile) throws FileNotFoundException {
		this.colorStream = null;
		this.depthStream = new FileOutputStream(depthFile);
		this.skeletonStream = new FileOutputStream(skeletonFile);

		this.colorBuffer = null;
		this.depthBuffer = ByteBuffer.allocateDirect(-1); // TODO: find out
																			// required capacity
		this.skeletonBuffer = ByteBuffer.allocateDirect(-1); // TODO: find out
																				// required capacity

		this.colorListener = null;
		this.depthListener = new EventListener() {
			@Override
			public void eventReceived(Event event) {
				try {
					frames--;
					if (frames <= 0) return;

					depthBuffer.put(((KinectColorFrameEvent) event).getData());
					depthBuffer.flip();
					depthStream.getChannel().write(depthBuffer);
					depthBuffer.clear();
				} catch (IOException e) {
					System.err.println("Writing depth data failed!");
					e.printStackTrace();
				}
			}
		};
		this.skeletonListener = new EventListener() {
			@Override
			public void eventReceived(Event event) {
				try {
					skeletonBuffer.put(((KinectColorFrameEvent) event).getData());
					skeletonBuffer.flip();
					skeletonStream.getChannel().write(skeletonBuffer);
					skeletonBuffer.clear();
				} catch (IOException e) {
					System.err.println("Writing skeleton data failed!");
					e.printStackTrace();
				}
			}
		};
	}

	/**
	 * Starts recording data, limiting the record to x amount of frames
	 * 
	 * @param frames
	 *           -1 if you want to record indefinitely, positive integer if you
	 *           want to
	 */
	public void startRecord(int frames) {
		this.frames = frames;

		if (this.colorStream != null)
			EventManager.registerEventListenerForEvent(KinectColorFrameEvent.class, this.colorListener);

		if (this.depthStream != null)
			EventManager.registerEventListenerForEvent(KinectColorFrameEvent.class, this.depthListener);

		if (this.skeletonStream != null)
			EventManager.registerEventListenerForEvent(KinectColorFrameEvent.class, this.skeletonListener);
	}

	/**
	 * Stops recording
	 */
	public void stopRecord() {
		EventManager.removeEventListenerForEvent(KinectColorFrameEvent.class, this.colorListener);
		EventManager.removeEventListenerForEvent(KinectColorFrameEvent.class, this.depthListener);
		EventManager.removeEventListenerForEvent(KinectColorFrameEvent.class, this.skeletonListener);

		System.out.println("Stopped recording"); // FIXME
	}
}
