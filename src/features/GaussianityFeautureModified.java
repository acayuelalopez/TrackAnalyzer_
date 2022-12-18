package features;
import traJ.TrajectoryModified;

public class GaussianityFeautureModified extends AbstractTrajectoryFeatureModified {

	private TrajectoryModified t;
	private int timelag;
	private String name = "Gaussianity";
	private String sname = "GAUSS";
	
	/**
	 * @param t The trajectory for which the gaussianity is to be calulated.
	 * @param timelag Timelag as dimensionless interger (1,2,3...)
	 */
	public GaussianityFeautureModified(TrajectoryModified t, int timelag) {
		this.t = t;
		this.timelag = timelag;
	}
	@Override
	public double[] evaluate() {
		MeanSquaredDisplacmentFeatureModified msdf = new MeanSquaredDisplacmentFeatureModified(t, timelag);
		QuartricMomentFeatureModified qart = new QuartricMomentFeatureModified(t, timelag);
		
		double msd = msdf.evaluate()[0];
		double q = qart.evaluate()[0];
		
		double res = (2*q)/(3*msd*msd) - 1;
		result = new double[] {res};
		return result;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getShortName() {
		return sname;
	}

	@Override
	public void setTrajectory(TrajectoryModified t) {
		this.t = t;
		result = null;
		
	}

}

