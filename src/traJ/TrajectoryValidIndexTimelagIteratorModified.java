package traJ;
import java.util.Iterator;

/**
 * This iterator iterates over a trajectory. It returns the index i where i and i+timelag is valid for that track.
 * This iterator is especally helpfull, when the trajectory contain gaps (null positions).
 * @author Thorsten Wagner (wagner at biomedical minus imaging dot de)
 *
 */
public class TrajectoryValidIndexTimelagIteratorModified implements Iterator<Integer>{

	private TrajectoryModified t;
	private int timelag;
	boolean overlap = true;
	int currentIndex;
	
	/**
	 * @param t The trajectory
	 * @param timelag The timelag
	 */
	public TrajectoryValidIndexTimelagIteratorModified(TrajectoryModified t, int timelag) {
		this.t = t;
		this.timelag = timelag;
		this.overlap = true;
		currentIndex = 0;
	}
	
	/**
	 * @param t The trajectory
	 * @param timelag The timelag
	 * @param overlap True when valid positions are allowed to overlap
	 */
	public TrajectoryValidIndexTimelagIteratorModified(TrajectoryModified t, int timelag, boolean overlap) {
		this.t = t;
		this.timelag = timelag;
		this.overlap = overlap;
		currentIndex = 0;
		
	}
	
	public boolean hasNext() {
		for(int i = currentIndex; i < t.size(); i++){
			if(i+timelag>=t.size()){
				return false;
			}
			if((t.get(i) != null) && (t.get(i+timelag) != null)){
				return true;
			}
		}
		return false;
	}

	/**
	 * Give next index i where i and i+timelag is valid 
	 */
	public Integer next() {
		for(int i = currentIndex; i < t.size(); i++){
			if(i+timelag>=t.size()){
				return null;
			}
			if((t.get(i) != null) && (t.get(i+timelag) != null)){
				if(overlap){
					currentIndex = i+1;
				}
				else{
					currentIndex = i+timelag;
				}
				return i;
			}
		}
		
		return null;
	}
	
	@Override
	public void remove() {
		// TODO Auto-generated method stub
		
	}

}