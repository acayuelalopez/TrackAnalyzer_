package features;
import math.MomentsCalculatorModified;
import traJ.TrajectoryModified;

public class KurtosisFeatureModified extends AbstractTrajectoryFeatureModified {

	private TrajectoryModified t;
	
	/**
	 * 
	 * @param t Trajectory for which the kurtosis is to be calculated.
	 */
	public KurtosisFeatureModified(TrajectoryModified t) {
		this.t = t;
	}
	
	/**
	 * @return An double array with the elements [0] = Kurtosis
	 */
	@Override
	public double[] evaluate() {
		MomentsCalculatorModified moments = new MomentsCalculatorModified(t);
		result = new double[] {moments.calculateNthMoment(4)};
		return result;
	}

	@Override
	public String getName() {
		return "Kurtosis";
	}

	@Override
	public String getShortName() {
		return "KURT";
	}

	@Override
	public void setTrajectory(TrajectoryModified t) {
		this.t =t ;
		result = null;
		
	}

}