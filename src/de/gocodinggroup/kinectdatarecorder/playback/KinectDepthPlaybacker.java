package de.gocodinggroup.kinectdatarecorder.playback;

import java.io.*;
import java.nio.*;
import java.nio.channels.*;

import de.gocodinggroup.kinectdatarecorder.events.*;
import de.gocodinggroup.util.*;

public class KinectDepthPlaybacker extends KinectPlaybacker {
	/** The fileInputStream for color files */
	private final FileChannel depthIn;

	/** The file we read from */
	private final RandomAccessFile file;

	/** The input buffer */
	private final ByteBuffer inputBuffer;

	/** Whether or not we should stop playback */
	private boolean shouldStop = false;

	public KinectDepthPlaybacker(File depthFile) throws FileNotFoundException {
		this.file = new RandomAccessFile(depthFile, "r");
		this.depthIn = this.file.getChannel();

		// TODO refactor using constants/file header information
		this.inputBuffer = ByteBuffer.allocateDirect(KinectDepthFrameEvent.DEPTH_BUFFER_SIZE);
	}

	@Override
	public void run() {
		while (!this.shouldStop) {
			try {
				// reset color in to start from the beginning
				this.depthIn.position(0);
				long previousTimestamp = -1;
				while (!this.shouldStop && this.depthIn.read(this.inputBuffer) > 0) {
					// This is for delta calculation. We have to account for
					// execution time, otherwise our frames are sent out to slow
					long before = System.currentTimeMillis();

					// flip input buffer to make it readable
					this.inputBuffer.flip();

					// Craft Kinect color Frame from Data
					KinectDepthFrameEvent frameEvent = new KinectDepthFrameEvent(this.inputBuffer);
					// System.out.println("delta: " + (System.nanoTime() - before));
					long currentTimestamp = frameEvent.getTimestamp();
					if (previousTimestamp == -1) previousTimestamp = currentTimestamp;

					long timeTaken = System.currentTimeMillis() - before;
					long sleepTime = currentTimestamp - previousTimestamp - timeTaken;

					// Limit fps (sleep until timestamp delta is reached)
					if (sleepTime > 0) Thread.sleep(sleepTime);
					previousTimestamp = currentTimestamp;

					// Dispatch event
					EventManager.dispatchAndWait(frameEvent);

					// clear buffer
					this.inputBuffer.clear();
				}
			} catch (IOException e) {
				System.err.println("ERROR reading from depth file");
				e.printStackTrace();
			} catch (InterruptedException e) {
				// break from this loop
				break;
			}
		}

		try {
			this.depthIn.close();
			this.file.close();
		} catch (IOException e) {
			System.out.println("ERROR closing depth playback resources");
			e.printStackTrace();
		}
	}

	@Override
	public void tearDown() throws InterruptedException {
		this.shouldStop = true;
		this.join();
	}
}
