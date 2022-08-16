package DiffusionCoefficientEstimator;
import traJ.TrajectoryModified;

public interface AbstractDiffusionCoefficientEstimatorModified {
	
	/**
	 * @param t Trajectory
	 * @param fps Frames per second [Hz]
	 * @return Returns the diffusion coefficent
	 */
	double[] getDiffusionCoefficient(TrajectoryModified t, double fps);

}