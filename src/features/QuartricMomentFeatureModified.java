package features;
import traJ.TrajectoryModified;
import traJ.TrajectoryValidIndexTimelagIteratorModified;

/**
 * 
 * @author Thorsten Wagner
 *
 */
public class QuartricMomentFeatureModified extends AbstractTrajectoryFeatureModified {

	private TrajectoryModified t;
	private int timelag;
	private String shortname = "QMOMENT";
	
	public QuartricMomentFeatureModified(TrajectoryModified t, int timelag){
		this.t = t;
		this.timelag = timelag;
	}
	@Override
	public double[] evaluate() {
		double sum =0;
		TrajectoryValidIndexTimelagIteratorModified it = new TrajectoryValidIndexTimelagIteratorModified(t, timelag);
		int N = 0;
		while(it.hasNext()){
			int i = it.next();
			sum = sum + 
					Math.pow(t.get(i).x-t.get(i+timelag).x,4) + 
					Math.pow(t.get(i).y-t.get(i+timelag).y,4) +
					Math.pow(t.get(i).z-t.get(i+timelag).z,4);
			N++;
		}
		result = new double[] {sum/N};
		return result;
	}
	
	public void setTimelag(int timelag){
		this.timelag = timelag;
	}

	@Override
	public String getName() {
		return "Quartric Moment";
	}

	@Override
	public String getShortName() {
		return shortname;
	}
	
	public void setShortName(String name){
		shortname = name;
	}

	@Override
	public void setTrajectory(TrajectoryModified t) {
		this.t = t;
		result = null;
		
	}

}