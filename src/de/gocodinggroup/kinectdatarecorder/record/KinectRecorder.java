package de.gocodinggroup.kinectdatarecorder.record;

import java.io.*;

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
	/** Listeners for writing data on FrameEvents */
	private StreamWriter colorListener;
	private StreamWriter depthListener;
	private StreamWriter skeletonListener;

	private class StreamWriter implements EventListener {
		private FileOutputStream stream;
		private int maxFrames;

		public StreamWriter(int maxFrames, File f) throws FileNotFoundException {
			this.stream = new FileOutputStream(f);
			this.maxFrames = maxFrames;
		}

		@Override
		public void eventReceived(Event event) {
			if (this.maxFrames <= 0) return;
			try {
				this.maxFrames--;
				if (this.maxFrames <= 0) {
					this.end();
					System.out.println("Finished writing to: " + this.stream);
					return;
				}
				synchronized (this) {
					this.stream.getChannel().write(((KinectFrameEvent) event).getCompressedData());
				}
			} catch (IOException e) {
				System.err.println("Writing data failed!");
				e.printStackTrace();
			}
		}

		public void end() throws IOException {
			this.maxFrames = 0;
			synchronized (this) {
				this.stream.flush();
				this.stream.close();
			}
		}
	}

	public void recordDepthTo(int frames, File depthFile) throws FileNotFoundException {
		this.depthListener = new StreamWriter(frames, depthFile);
	}

	public void recordColorTo(int frames, File colorFile) throws FileNotFoundException {
		this.colorListener = new StreamWriter(frames, colorFile);
	}

	public void recordSkeletonTo(int frames, File skeletonFile) throws FileNotFoundException {
		this.skeletonListener = new StreamWriter(frames, skeletonFile);
	}

	/**
	 * Starts recording data, limiting the record to x amount of frames
	 * 
	 * @param frames
	 *           -1 if you want to record indefinitely, positive integer if you
	 *           want to
	 */
	public void startRecord() {
		if (this.colorListener != null)
			EventManager.registerEventListenerForEvent(KinectColorFrameEvent.class, this.colorListener);

		if (this.depthListener != null)
			EventManager.registerEventListenerForEvent(KinectDepthFrameEvent.class, this.depthListener);

		if (this.skeletonListener != null)
			EventManager.registerEventListenerForEvent(KinectSkeletonFrameEvent.class, this.skeletonListener);
	}

	/**
	 * Stops recording
	 * 
	 * @throws IOException
	 */
	public void stopRecord() throws IOException {
		EventManager.removeEventListenerForEvent(KinectColorFrameEvent.class, this.colorListener);
		EventManager.removeEventListenerForEvent(KinectColorFrameEvent.class, this.depthListener);
		EventManager.removeEventListenerForEvent(KinectColorFrameEvent.class, this.skeletonListener);

		if (this.colorListener != null) this.colorListener.end();
		if (this.depthListener != null) this.depthListener.end();
		if (this.skeletonListener != null) this.skeletonListener.end();

		this.colorListener = null;
		this.depthListener = null;
		this.skeletonListener = null;
		System.out.println("Stopped recording");
	}
}
