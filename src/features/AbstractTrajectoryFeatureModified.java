package features;
import traJ.TrajectoryModified;

public abstract class AbstractTrajectoryFeatureModified {

	protected double[] result = null;
	
	/**
	 * @return return the result;
	 */
	public abstract double[] evaluate();
	
	/**
	 * @return Returns the result, but does not recalculate when it was calculated earlier
	 */
	public double[] getValue(){
		if(result==null){
			result = evaluate();
		}
		return result;
	}
	/**
	 * 
	 * @return The name of the feature
	 */
	public abstract String getName();
	/**
	 * Short name of the feature. Should not contain any spaces.
	 * @return A shortened name of the feature
	 */
	public abstract String getShortName();
	public abstract void setTrajectory(TrajectoryModified t);
	
	

}