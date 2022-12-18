package features;


import ij.IJ;
import traJ.TrajectoryModified;
import DiffusionCoefficientEstimator.RegressionDiffusionCoefficientEstimatorModified;

/**
 * Following Saxton [1], the probability p that a trajectory is trapped
 * inside a radius R is:
 *   p = 1 - exp(0.2048-2.5117(D*t/R^2))
 * where D is the diffusion coefficient, and t is time duration of the trajectory.
 * 
 * For this feature R is estimated by the maximum distance between two positions and
 * D is estimated by the the covariance estimator.
 * @author Thorsten Wagner
 *
 */
public class TrappedProbabilityFeatureModified extends AbstractTrajectoryFeatureModified{
	private TrajectoryModified t;
	
	/**
	 * 
	 * @param t Trajectory
	 */
	public TrappedProbabilityFeatureModified(TrajectoryModified t) {
		this.t = t;
	}
	
	@Override
	public double[] evaluate() {
		MaxDistanceBetweenTwoPositionsFeatureModified dtwop = new MaxDistanceBetweenTwoPositionsFeatureModified(t);
		double r = dtwop.evaluate()[0]/2;
		
		//CovarianceDiffusionCoefficientEstimator dcEst = new CovarianceDiffusionCoefficientEstimator(t, 1);
		RegressionDiffusionCoefficientEstimatorModified dcEst = new RegressionDiffusionCoefficientEstimatorModified(t,1,1, 2);
		double D = dcEst.evaluate()[0];
		double time = t.size();

		double p = 1- Math.exp(0.2048-2.5117*(D*time/(r*r)));
		result = new double[]{p};
		return result;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Trapped trajectory probability";
	}

	@Override
	public String getShortName() {
		return "TRAPPED";
	}

	@Override
	public void setTrajectory(TrajectoryModified t) {
		this.t = t;
		result = null;
		
	}

}
