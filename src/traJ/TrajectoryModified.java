package traJ;

import java.util.ArrayList;

import features.AbstractTrajectoryFeatureModified;
import vecmath.Point3dModified;


public class TrajectoryModified extends ArrayList<Point3dModified> {

	private static final long serialVersionUID = 1L;
	private int dimension; // Dimension of the trajectory
	private int relativeStartTimepoint; // Relative start point (Frame number)
	private long id; // Unique ID
	private static long idCounter = 1;
	private String type = ""; // Could be used to save information, like classified class...
	private ArrayList<AbstractTrajectoryFeatureModified> features; // Feature

	public TrajectoryModified(int dimension) {
		this.dimension = dimension;
		relativeStartTimepoint = 0;
		id = idCounter++;
		features = new ArrayList<AbstractTrajectoryFeatureModified>();
	}

	
	public TrajectoryModified(int dimension, int relativeStartTimepoint) {
		this.dimension = dimension;
		this.relativeStartTimepoint = relativeStartTimepoint;
		id = idCounter++;
		features = new ArrayList<AbstractTrajectoryFeatureModified>();
	}

	public TrajectoryModified() {
		relativeStartTimepoint = 0;
		features = new ArrayList<AbstractTrajectoryFeatureModified>();
	}


	public TrajectoryModified subList(int fromIndex, int toIndex) {
		TrajectoryModified t = new TrajectoryModified(dimension);

		for (int i = fromIndex; i < toIndex; i++) {
			t.add(this.get(i));
		}
		return t;
	}


	public ArrayList<AbstractTrajectoryFeatureModified> getFeatures() {
		return features;
	}


	public void addFeature(AbstractTrajectoryFeatureModified feature) {

		features.add(feature);
	}

	public TrajectoryModified getCopy() {
		TrajectoryModified t = new TrajectoryModified(dimension);
		for (int i = 0; i < this.size(); i++) {
			t.add(t.get(i));
		}
		return t;
	}


	public double[][] getPositionsAsArray() {
		double[][] posAsArr = new double[size()][3];
		for (int i = 0; i < size(); i++) {
			if (get(i) != null) {
				posAsArr[i][0] = get(i).x;
				posAsArr[i][1] = get(i).y;
				posAsArr[i][2] = get(i).z;
			} else {
				posAsArr[i] = null;
			}
		}
		return posAsArr;
	}


	public String toString() {
		String result = "";
		for (int i = 0; i < size(); i++) {
			result += " x: " + get(i).x + " y: " + get(i).y + " z: " + get(i).z + "\n";
		}
		return result;
	}

	public boolean add(Point3dModified e) {
		// TODO Auto-generated method stub
		return super.add(e);
	}


	public void scale(double v) {
		for (int i = 0; i < this.size(); i++) {
			this.get(i).scale(v);
			;
		}
	}


	public void offset(double x, double y, double z) {
		for (int i = 0; i < this.size(); i++) {
			this.get(i).add(new Point3dModified(x, y, z));
		}
	}

	
	public boolean add(double x, double y, double z) {
		return super.add(new Point3dModified(x, y, z));
	}



	public int getDimension() {
		return dimension;
	}


	public void setDimension(int dimension) {
		this.dimension = dimension;
	}

	public long getID() {
		return id;
	}

	
	public void setID(int id) {
		this.id = id;
	}


	public int getRelativeStartTimepoint() {
		return relativeStartTimepoint;
	}


	public void setRelativStartTimepoint(int timepoint) {
		relativeStartTimepoint = timepoint;
	}

	
	public void setType(String type) {
		this.type = type;
	}


	public String getType() {
		return type;
	}


	public static void restIDCounter() {
		idCounter = 1;
	}
}
