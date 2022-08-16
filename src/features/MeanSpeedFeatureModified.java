package features;
import traJ.TrajectoryModified;

public class MeanSpeedFeatureModified extends AbstractTrajectoryFeatureModified {

	private TrajectoryModified t;
	private double timelag;

	/**
	 * 
	 * @param t       Trajectory for which the speed is to be calculated.
	 * @param timelag Timelag between two steps.
	 */
	public MeanSpeedFeatureModified(TrajectoryModified t, double timelag) {
		this.t = t;
		this.timelag = timelag;
	}

	@Override
	/**
	 * Calculates the mean curvlinear speed and the mean straight-line speed.
	 * 
	 * @return An double array where the first element is the mean curvilinear speed
	 *         and the second the mean straight-line speed.
	 */
	public double[] evaluate() {
		double sum = 0;
		for (int i = 1; i < t.size(); i++) {
			sum += t.get(i - 1).distance(t.get(i)) / timelag;
		}

		double meanspeed = sum / (t.size() - 1);

		double netDistance = t.get(0).distance(t.get(t.size() - 1));
		double straightLineSpeed = netDistance / ((t.size() - 1) * timelag);
		result = new double[] { meanspeed, straightLineSpeed };
		return result;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Mean Speed Feature";
	}

	@Override
	public String getShortName() {
		// TODO Auto-generated method stub
		return "MEANSPEED";
	}

	@Override
	public void setTrajectory(TrajectoryModified t) {
		this.t = t;
		result = null;
	}

}
