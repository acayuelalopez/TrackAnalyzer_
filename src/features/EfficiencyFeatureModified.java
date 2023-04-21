package features;
import traJ.TrajectoryModified;

public class EfficiencyFeatureModified extends AbstractTrajectoryFeatureModified {
	
	private TrajectoryModified t;
	/**
	 * 
	 * @param t Trajectory for which the efficiency is to be calculated.
	 */
	public EfficiencyFeatureModified(TrajectoryModified t) {
		this.t = t;
	}
	
	/**
	 * @return Returns an double array with the elements [0]=Efficiency
	 */
	@Override
	public double[] evaluate() {
		result = new double[]{getEfficiency()};
		return result;
	}
	
	public double getEfficiency(){
		double sum = 0;
		for(int i = 1; i < t.size(); i++){
			double d = t.get(i).distance(t.get(i-1));
			sum += d*d;
		}
		if(sum<Math.pow(10, -10)){
			return 0;
		}
		double d = t.get(0).distance(t.get(t.size()-1));
		double eff = (d*d)/(t.size()*sum);
		return eff;
	}

	@Override
	public String getName() {
		return "Efficiency";
	}

	@Override
	public String getShortName() {
		
		return "EFFICENCY";
	}

	@Override
	public void setTrajectory(TrajectoryModified t) {
		this.t = t;
		result = null;
	}

}