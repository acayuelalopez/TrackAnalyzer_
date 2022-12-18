package trajectory_classifier;
import features.AbstractTrajectoryFeatureModified;

public class FeatureWorkerModified extends Thread {
	enum EVALTYPE{
		FIRST,SECOND,RATIO_01,RATIO_10,RATIO_12;
	}
	double[] result;
	AbstractTrajectoryFeatureModified c;
	EVALTYPE ev;
	int resIndex;
	
	public FeatureWorkerModified(double[] result, int resIndex, AbstractTrajectoryFeatureModified c, EVALTYPE ev) {
		this.result = result;
		this.c = c;
		this.ev =ev;
		this.resIndex = resIndex;
	}
	
	@Override
	public void run() {
		double[] res ;
			switch (ev) {
			case FIRST:
				res = c.getValue();
				result[resIndex] = res[0] ;
				break;
			case SECOND:
				res = c.getValue();
				result[resIndex] = res[1] ;
				break;
			case RATIO_01:
				res = c.getValue();
				result[resIndex] = res[0]/res[1];
				break;
			case RATIO_10:
				res = c.getValue();
				result[resIndex] = res[1]/res[0];
				break;
			case RATIO_12:
				res = c.getValue();
				result[resIndex] = res[1]/res[2];
				break;

			default:
				break;
			}
			
		}

}