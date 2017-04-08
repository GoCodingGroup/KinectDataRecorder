package de.gocodinggroup.kinectdatarecorder.playback;

import java.io.*;
import java.nio.*;
import java.nio.channels.*;

import de.gocodinggroup.kinectdatarecorder.events.*;
import de.gocodinggroup.util.*;

public class KinectColorPlaybacker extends KinectPlaybacker {
	/** The fileInputStream for color files */
	private final FileChannel colorIn;

	/** The file we read from */
	private final RandomAccessFile file;

	/** The input buffer */
	private final ByteBuffer inputBuffer;

	/** Whether or not we should stop playback */
	private boolean shouldStop = false;

	public KinectColorPlaybacker(File colorFile) throws FileNotFoundException {
		this.file = new RandomAccessFile(colorFile, "r");
		this.colorIn = this.file.getChannel();

		// TODO refactor using constants/file header information
		this.inputBuffer = ByteBuffer.allocate(1920 * 1080 * 4 + 3 * Integer.BYTES + 1 * Long.BYTES);
	}

	@Override
	public void run() {
		while (!this.shouldStop) {
			try {
				// reset color in to start from the beginning
				this.colorIn.position(0);
				long previousTimestamp = -1;
				while (!this.shouldStop && this.colorIn.read(this.inputBuffer) > 0) {
					// Prepare Buffer for reading
					this.inputBuffer.flip();

					// Craft Kinect color Frame from Data
					KinectColorFrameEvent frameEvent = new KinectColorFrameEvent(this.inputBuffer);
					long currentTimestamp = frameEvent.getTimestamp();
					if (previousTimestamp == -1) previousTimestamp = currentTimestamp;

					// Limit fps (sleep until timestamp delta is reached)
					Thread.sleep(currentTimestamp - previousTimestamp);
					previousTimestamp = currentTimestamp;

					// Dispatch event
					EventManager.dispatchAndWait(frameEvent);

					// clear buffer
					this.inputBuffer.clear();
				}
			} catch (IOException e) {
				System.err.println("ERROR reading from color file");
				e.printStackTrace();
			} catch (InterruptedException e) {
				// break from this loop
				break;
			}
		}

		try {
			this.colorIn.close();
			this.file.close();
		} catch (IOException e) {
			System.out.println("ERROR closing color playback resources");
			e.printStackTrace();
		}
	}

	@Override
	public void tearDown() throws InterruptedException {
		this.shouldStop = true;
		this.join();
	}
}
