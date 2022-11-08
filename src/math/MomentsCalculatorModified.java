package math;


import vecmath.Vector2d;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.EigenDecomposition;

import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;

import traJ.TrajectoryModified;



public class MomentsCalculatorModified {
	
	private TrajectoryModified t;
	
	public MomentsCalculatorModified(TrajectoryModified t) {
		this.t = t;
	}
	
	public double calculateNthMoment(int n){
		Array2DRowRealMatrix gyr = RadiusGyrationTensor2DModified.getRadiusOfGyrationTensor(t);
		EigenDecomposition eigdec = new EigenDecomposition(gyr);
		
		Vector2d eigv = new Vector2d(eigdec.getEigenvector(0).getEntry(0),eigdec.getEigenvector(0).getEntry(1));

		double[] projected = new double[t.size()];
		for(int i = 0; i < t.size(); i++){
			Vector2d pos = new Vector2d(t.get(i).x,t.get(i).y);
			double v = eigv.dot(pos);
			projected[i] = v;
		}
		
		Mean m = new Mean();
		StandardDeviation s = new StandardDeviation();
		double mean = m.evaluate(projected);
		double sd  = s.evaluate(projected);
		double sumPowN=0;

		for(int i = 0; i < projected.length; i++){
			sumPowN += Math.pow( (projected[i]-mean)/sd, n);
		}

		double nThMoment =  sumPowN/projected.length;
		
		return nThMoment;
	}
	

}