package features;
import traJ.TrajectoryModified;

public class MaxDistanceBetweenTwoPositionsFeatureModified extends AbstractTrajectoryFeatureModified {

	private TrajectoryModified t;

	/**
	 * 
	 * @param t Trajectory for which the maximum distance is to be calculated.
	 */
	public MaxDistanceBetweenTwoPositionsFeatureModified(TrajectoryModified t) {
		this.t = t;
	}
	
	/**
	 * 
	 * @return An double array with the elements [0] = max distance 
	 */
	@Override
	public double[] evaluate() {
		double maxDistance = Double.MIN_VALUE;
		for(int i = 0; i < t.size(); i++){
			for(int j = i+1; j< t.size(); j++){
				double d = t.get(i).distance(t.get(j));
				if(d> maxDistance){
					maxDistance = d;
				}
			}
		}
		result = new double[] {maxDistance};
		return result;
	}

	@Override
	public String getName() {
		return "Maximum distance between two positions";
	}

	@Override
	public String getShortName() {
		return "MAX-DIST-POS";
	}

	@Override
	public void setTrajectory(TrajectoryModified t) {
		this.t = t;
		result = null;
	}

}