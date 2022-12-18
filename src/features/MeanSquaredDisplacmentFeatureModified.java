package features;
import traJ.TrajectoryModified;
import traJ.TrajectoryValidIndexTimelagIteratorModified;

/**
 * Calculates the mean squared displacement
 * @author Thorsten Wagner (wagner at biomedical - imaging.de
 *
 */
public class MeanSquaredDisplacmentFeatureModified extends AbstractTrajectoryFeatureModified implements AbstractMeanSquaredDisplacmentEvaluatorModified {
	
	private TrajectoryModified t;
	private int timelag;
	private boolean overlap =false;
	
	/**
	 * 
	 * @param t Trajectory for which the MSD is to be calculated.
	 * @param timelag Timeleg for msd caluclation (>= 1)
	 */
	public MeanSquaredDisplacmentFeatureModified(TrajectoryModified t, int timelag) {
		this.t = t;
		this.timelag = timelag;
	}
	
	public void setTimelag(int timelag){
		this.timelag = timelag;
	}
	
	public void setTrajectory(TrajectoryModified t){
		this.t = t;
		result =null;
	}
	
	/**
	 * 
	 * Calculates the mean squared displacement (MSD). Further more it calculates
	 * the relative variance of MSD according to:
	 * S. Huet, E. Karatekin, V. S. Tran, I. Fanget, S. Cribier, and J.-P. Henry, 
	 * “Analysis of transient behavior in complex trajectories: application to secretory vesicle dynamics.,” 
	 * Biophys. J., vol. 91, no. 9, pp. 3542–3559, 2006.
	 * 
	 * @param t Trajectory
	 * @param timelag 
	 * @return 
	 */
	private double[] getMeanSquaredDisplacment(TrajectoryModified t, int timelag){
		double msd = 0;
		double[] result = new double[3];
		if(t.size()==1){
			result[0] =0;
			result[1] =0;
			result[2] =1;
			return result;
		}
		
		if(timelag<1){
			throw new IllegalArgumentException("Timelag can not be smaller than 1");
		}
		TrajectoryValidIndexTimelagIteratorModified it = new TrajectoryValidIndexTimelagIteratorModified(t, timelag,overlap);
		int N = 0;
		while(it.hasNext()){
			int i = it.next();
			msd = msd + 
					Math.pow(t.get(i).x-t.get(i+timelag).x,2) + 
					Math.pow(t.get(i).y-t.get(i+timelag).y,2) +
					Math.pow(t.get(i).z-t.get(i+timelag).z,2);
			N++;
		}
		
		msd = msd/N; 
		
		result[0] = msd;
		result[1] = (timelag*(2*timelag*timelag+1.0))/(N-timelag+1.0); //Variance
		result[2] = N; //Number of data points
		return result;
	}

	@Override
	/**
	 * @return An double array with elements [0] = Mean squared displacment (in length unit squared), [1] = estimated variance, [2] = number of used datapoints
	 */
	public double[] evaluate() {
		// TODO Auto-generated method stub
		return getMeanSquaredDisplacment(t, timelag);
	}
	
	/**
	 * 
	 * @return Return the relative variance of MSD according formula (6) of:
	 * S. Huet, E. Karatekin, V. S. Tran, I. Fanget, S. Cribier, and J.-P. Henry, 
	 * “Analysis of transient behavior in complex trajectories: application to secretory vesicle dynamics.,” 
	 * Biophys. J., vol. 91, no. 9, pp. 3542–3559, 2006.
	 */
	public double getRelativeVariance() {
		return getMeanSquaredDisplacment(t, timelag)[1];
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Mean squared displacement-dt-"+timelag;
	}

	@Override
	public String getShortName() {
		// TODO Auto-generated method stub
		return "MSD";
	}
	
	public void setOverlap(boolean overlap){
		this.overlap = overlap;
	}
}