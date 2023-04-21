package features;
import traJ.TrajectoryModified;

/**
 * Relates the net displacement to the sum of step lengths
 * @author Thorsten Wagner
 */
public class StraightnessFeatureModified extends AbstractTrajectoryFeatureModified {
	
	private TrajectoryModified t;
	
	public StraightnessFeatureModified(TrajectoryModified t) {
		this.t = t;
	}
	
	@Override
	public double[] evaluate() {
		result = new double[]{getStraightness()};
		return result;
	}
	
	public double getStraightness(){
		double sum = 0;
		for(int i = 1; i < t.size(); i++){
			sum += t.get(i).distance(t.get(i-1));
		}
		if(sum<Math.pow(10, -10)){
			return 0;
		}
		double straightness = (t.get(0).distance(t.get(t.size()-1)))/sum;
		return straightness;
	}

	@Override
	public String getName() {
		return "Straightness";
	}

	@Override
	public String getShortName() {
		
		return "STRAIGHTNESS";
	}

	@Override
	public void setTrajectory(TrajectoryModified t) {
		this.t = t;
		result = null;
		
	}

}