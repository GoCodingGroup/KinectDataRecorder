package de.gocodinggroup.kinectdatarecorder.playback;

/**
 * Playbacker's subclasses are used to playback recorded kinect files. Use
 * start() to start playback and tearDown to finish and free resources
 * 
 * @author Dominik
 *
 */
public abstract class KinectPlaybacker extends Thread {
	/**
	 * Stops playback and tears playbacker down
	 * 
	 * @throws InterruptedException
	 */
	public abstract void tearDown() throws InterruptedException;
}
