package DiffusionCoefficientEstimator;
import java.util.ArrayList;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import features.AbstractMeanSquaredDisplacmentEvaluatorModified;
import features.AbstractTrajectoryFeatureModified;
import features.MeanSquaredDisplacmentFeatureModified;
import math.StraightLineFitModified;
import traJ.TrajectoryModified;
/**
 * 
 * @author Thorsten Wagner
 */
public class RegressionDiffusionCoefficientEstimatorModified extends AbstractTrajectoryFeatureModified implements AbstractDiffusionCoefficientEstimatorModified {
	private int lagMin;
	private int lagMax;
	private AbstractMeanSquaredDisplacmentEvaluatorModified msdevaluator;
	private TrajectoryModified t;
	private double fps;
	
	public RegressionDiffusionCoefficientEstimatorModified(int lagMin, int lagMax) {
		this.lagMin = lagMin;
		this.lagMax = lagMax;
		msdevaluator = new MeanSquaredDisplacmentFeatureModified(null, lagMin);
	}
	
	public RegressionDiffusionCoefficientEstimatorModified(TrajectoryModified t, double fps, int lagMin, int lagMax) {
		this.lagMin = lagMin;
		this.lagMax = lagMax;
		msdevaluator = new MeanSquaredDisplacmentFeatureModified(null, lagMin);
		this.t = t;
		this.fps = fps;
	}
	
	/**
	 * @return [0] = diffusion coefficent, [1] = slope, [2] = Intercept, [3] Goodness
	 */

	public double[] getDiffusionCoefficient(TrajectoryModified t, double fps) {
		if(t.size()==1){
			return null;
		}
		ArrayList<Double> xDataList = new ArrayList<Double>();
		ArrayList<Double> yDataList = new ArrayList<Double>();
		double msdhelp = 0;
		if(lagMin==lagMax){
			xDataList.add(0.0);
			yDataList.add(0.0);
		}
		msdevaluator.setTrajectory(t);
		msdevaluator.setTimelag(lagMin);
		
		for(int i = lagMin; i < lagMax+1; i++){
			msdevaluator.setTimelag(i);
			double[] res = msdevaluator.evaluate();
			msdhelp= res[0];
			int N = (int)res[2];
			for(int j = 0; j < N; j++){
				xDataList.add(i*1.0/fps);
				yDataList.add(msdhelp) ;
			}
		}
		double[] xdata = ArrayUtils.toPrimitive(xDataList.toArray(new Double[0]));
		double[] ydata = ArrayUtils.toPrimitive(yDataList.toArray(new Double[0]));
		StraightLineFitModified fdf = new StraightLineFitModified();
		fdf.doFit(xdata, ydata);
		
		result = new double[]{fdf.getB()/(2.0*t.getDimension()),fdf.getB()*2.0*t.getDimension(),fdf.getA(), fdf.getGoodness()};
		return result;
	}
	
	public void setTimelags(int lagMin, int lagMax){
		this.lagMin = lagMin;
		this.lagMax = lagMax;
	}
	
	
	public void setMeanSquaredDisplacementEvaluator(AbstractMeanSquaredDisplacmentEvaluatorModified msdeval){
		this.msdevaluator = msdeval;
	}

	@Override
	public double[] evaluate() {
		result = getDiffusionCoefficient(t, fps);
		return result;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Diffusion coefficient (Regression)";
	}

	@Override
	public String getShortName() {
		// TODO Auto-generated method stub
		return "DC-REG";
	}

	@Override
	public void setTrajectory(TrajectoryModified t) {
		this.t = t;
		
	}


}