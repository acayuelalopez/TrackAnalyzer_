package features;
import traJ.TrajectoryModified;

/**
 * Caculates the center of gravity of an trajectory.
 * @author Thorsten Wagner
 *
 */
public class CenterOfGravityFeatureModified extends AbstractTrajectoryFeatureModified {

	private TrajectoryModified t;
	/**
	 * 
	 * @param t Trajectory for which the COG is to be calculated.
	 */
	public CenterOfGravityFeatureModified(TrajectoryModified t) {
		this.t = t;
	}
	/**
	 * @return Returns an double array with the elements [0]= COG X, [1]= COG Y, [2]=COG Z
	 */
	@Override
	public double[] evaluate() {
		double x = 0;
		double y = 0;
		double z = 0;
		
		for(int i = 0; i < t.size(); i++){
			x += t.get(i).x;
			y += t.get(i).y;
			z += t.get(i).z;
		}
		
		x = x/t.size();
		y = y/t.size();
		z = z/t.size();
		result = new double[] {x,y,z};
		return result;
	}

	@Override
	public String getName() {
		return "Center of gravity";
	}

	@Override
	public String getShortName() {
		return "COG";
	}

	@Override
	public void setTrajectory(TrajectoryModified t) {
		this.t = t;
		result = null;
		
	}

}

