package drift;
import java.util.ArrayList;

import traJ.TrajectoryModified;
import traJ.TrajectoryValidIndexTimelagIteratorModified;


public class StaticDriftCalculatorModified<T extends TrajectoryModified>  {
	
	/**
	 * Calculates the static drift. Static means, that the drift does not change direction or intensity over time.
	 * 
	 * @param tracks Tracks which seems to exhibit a local drift
	 * @return The static drift over all trajectories
	 */
	public double[] calculateDrift(ArrayList<T> tracks){
		double[] result = new double[3];
		
		double sumX =0;
		double sumY = 0;
		double sumZ = 0;
		int N=0;
		for(int i = 0; i < tracks.size(); i++){
			T t = tracks.get(i);
			TrajectoryValidIndexTimelagIteratorModified it = new TrajectoryValidIndexTimelagIteratorModified(t,1);
	
			//for(int j = 1; j < t.size(); j++){
			while(it.hasNext()) {
				int j = it.next();
				sumX += t.get(j+1).x - t.get(j).x;
				sumY += t.get(j+1).y - t.get(j).y;
				sumZ += t.get(j+1).z - t.get(j).z;
				N++;
			}
		}
		result[0] = sumX/N;
		result[1] = sumY/N;
		result[2] = sumZ/N;
		return result;
	}

}