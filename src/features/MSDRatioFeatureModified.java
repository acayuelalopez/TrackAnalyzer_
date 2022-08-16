package features;

import traJ.TrajectoryModified;
/**
 * Calculates ratio of the MSD for two give timelags.
 * 
 * @author Thorsten Wagner
 *
 */
public class MSDRatioFeatureModified  extends AbstractTrajectoryFeatureModified {
	private int timelag1;
	private int timelag2;
	private TrajectoryModified t;
	/**
	 * 
	 * @param t Trajectory for which the MSD ratio is to be calculated
	 * @param timelag1 Timelag for the numerator MSD
	 * @param timelag2 Timelag for the denominator MSD
	 */
	public MSDRatioFeatureModified(TrajectoryModified t, int timelag1, int timelag2) {
		this.t = t;
		this.timelag1 = timelag1;
		this.timelag2 = timelag2;
	}
	/**
	 * @return An double array with the elements [0] = MSD Ratio
	 */
	@Override
	public double[] evaluate() {
		MeanSquaredDisplacmentFeatureModified msdf1 = new MeanSquaredDisplacmentFeatureModified(t, timelag1);
		MeanSquaredDisplacmentFeatureModified msdf2 = new MeanSquaredDisplacmentFeatureModified(t, timelag2);

		double msd1 = msdf1.evaluate()[0];
		double msd2 = msdf2.evaluate()[0];
		double res = (msd1)/(msd2) - 1.0*timelag1/timelag2;
		result = new double[]{res};
		return result;
	}

	@Override
	public String getName() {
		return "Mean squared displacment ratio";
	}

	@Override
	public String getShortName() {
		return "MSDR";
	}

	@Override
	public void setTrajectory(TrajectoryModified t) {
		this.t = t;
		result = null;
		
	}

}