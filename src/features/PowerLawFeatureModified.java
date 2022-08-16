package features;


import java.util.ArrayList;

import org.apache.commons.lang3.ArrayUtils;

import de.biomedical_imaging.traj.math.PowerLawCurveFit;
import traJ.TrajectoryModified;

/**
 * Fits the function y = 4*a*x^b to the MSD data and calculates the exponent alpha (b) and the diffusion coefficient (a).
 * @author Thorsten Wagner
 *
 */
public class PowerLawFeatureModified extends AbstractTrajectoryFeatureModified {


	private TrajectoryModified t;
	private int minlag;
	private int maxlag;
	private AbstractMeanSquaredDisplacmentEvaluatorModified msdeval;
	private int evaluateIndex = 0;
	private boolean useInitialGuess;
	private double initalDiffusionCoefficient;
	private double initalAlpha;
	private double fps;
	private double timelag;
	
	/**
	 * @param t Trajectory for which alpha and diffusion coefficient is to be calculated.
	 * @param minlag Minimum timelag for the MSD curve calculation
	 * @param maxlag Maximum timelag for the MSD curve calculation
	 */
	public PowerLawFeatureModified(TrajectoryModified t, double fps, int minlag, int maxlag) {
		this.t = t;
		this.minlag = minlag;
		this.maxlag = maxlag;
		this.fps = fps;
		this.timelag = 1/fps;
		msdeval = new MeanSquaredDisplacmentFeatureModified(null, 0);
		((MeanSquaredDisplacmentFeatureModified)msdeval).setOverlap(false);
		evaluateIndex = 0;
		useInitialGuess = false;
	
	}
	
	/**
	 * 
	 * @param t Trajectory for which alpha and diffusion coefficient is to be calculated.
	 * @param minlag Minimum timelag for the MSD curve calculation
	 * @param maxlag Maximum timelag for the MSD curve calculation
	 * @param initalAlpha Initial guess for alpha
	 * @param initialDiffusionCoefficient Initial guess for the diffusion coefficient.
	 */
	public PowerLawFeatureModified(TrajectoryModified t, double fps, int minlag, int maxlag, double initalAlpha, double initialDiffusionCoefficient) {
		this.t = t;
		this.minlag = minlag;
		this.maxlag = maxlag;
		this.fps = fps;
		this.timelag = 1/fps;
		msdeval = new MeanSquaredDisplacmentFeatureModified(null, 0);
		((MeanSquaredDisplacmentFeatureModified)msdeval).setOverlap(false);
		evaluateIndex = 0;
		useInitialGuess = true;
		this.initalAlpha = initalAlpha;
		this.initalDiffusionCoefficient = initialDiffusionCoefficient;
	}
	
	/**
	 * @return An double array with the elements [0]= alpha, [1]=diffusion coefficient [2]=goodness of fit
	 */
	@Override
	public double[] evaluate() {
		
		ArrayList<Double> xDataList = new ArrayList<Double>();
		ArrayList<Double> yDataList = new ArrayList<Double>();
		msdeval.setTrajectory(t);
		double[][] data = new double[maxlag-minlag+1][3];

		for(int i = minlag; i <= maxlag; i++){
			msdeval.setTimelag(i);
			data[i-minlag][0] = i*timelag;
			double[] res = msdeval.evaluate();
			data[i-minlag][1] = res[evaluateIndex];
			data[i-minlag][2] = (int)res[2];
	

		}

		//Weightening
		for(int i = 0; i < (maxlag-minlag+1); i++){
			double x = data[i][0];
			double y = data[i][1];
			int np = (int)data[i][2];
			
			for(int j = 0; j < np; j++){
				xDataList.add(x);
				yDataList.add(y);
			}
		}
		
		double[] xData = ArrayUtils.toPrimitive(xDataList.toArray(new Double[0]));
		double[] yData = ArrayUtils.toPrimitive(yDataList.toArray(new Double[0]));
		
		PowerLawCurveFit pwFit = new PowerLawCurveFit();
		
		if(useInitialGuess){
			pwFit.doFit(xData,yData,initalAlpha,initalDiffusionCoefficient);
		}else{
			pwFit.doFit(xData,yData);
		}
		result = new double[]{pwFit.getAlpha(),pwFit.getDiffusionCoefficient(),pwFit.getGoodness()};
	
		return result;
	}
	
	/**
	 * If a custim mean squared displacement evaluator is used, evaluateIndex should be the array index of the MSD value.
	 * @param evaluateIndex
	 */
	public void setEvaluateIndex(int evaluateIndex){
		this.evaluateIndex = evaluateIndex;
	}
	
	public void setMeanSquaredDisplacmentEvaluator(AbstractMeanSquaredDisplacmentEvaluatorModified msdeval){
		this.msdeval = msdeval;
	}

	@Override
	public String getName() {
		
		return "Power-Law-Feature";
	}

	@Override
	public void setTrajectory(TrajectoryModified t) {
		this.t = t;
		result = null;
		
	}

	@Override
	public String getShortName() {
		return "POWER";
	}

}
