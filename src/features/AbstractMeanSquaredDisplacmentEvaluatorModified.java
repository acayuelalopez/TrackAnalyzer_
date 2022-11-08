package features;
import traJ.TrajectoryModified;

public interface AbstractMeanSquaredDisplacmentEvaluatorModified {
	
	public void setTimelag(int timelag);
	
	public void setTrajectory(TrajectoryModified t);
	
	public double[] evaluate();
	
}