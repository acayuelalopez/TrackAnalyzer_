package trajectory_classifier;


import java.util.ArrayList;

import traJ.TrajectoryModified;


public abstract class AbstractClassifierModified {
	
	public abstract String classify(TrajectoryModified t);
	
	public abstract void start();
	
	public abstract void stop();

	public abstract String[] classify(ArrayList<TrajectoryModified> t);
	
	public abstract double[] getConfindence();

}