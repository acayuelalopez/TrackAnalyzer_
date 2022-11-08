package features;

import java.util.ArrayList;

import org.apache.commons.lang3.ArrayUtils;

import DiffusionCoefficientEstimator.AbstractDiffusionCoefficientEstimatorModified;
import DiffusionCoefficientEstimator.RegressionDiffusionCoefficientEstimatorModified;
import math.ConfinedDiffusionMSDCurveFitModified;
import traJ.TrajectoryModified;

/**
 * Fits a function to <r^2> = A [1-B*exp(-4*C*D*t/A)] Where A is the squared
 * corral size (radius), and B&C shape parameters of the corral and D the
 * diffusion coefficient. A, B, C and D are restricted to be positive. This
 * follows the description for correlad (confined) diffusion in Saxton, M.J. &
 * Jacobson, K., 1997. Single-particle tracking: applications to membrane
 * dynamics. Annual review of biophysics and biomolecular structure, 26,
 * pp.373–399.
 * 
 * @author Thorsten Wagner
 *
 */
public class ConfinedDiffusionParametersFeatureModified extends AbstractTrajectoryFeatureModified {

	private TrajectoryModified t;
	private double timelag;
	private AbstractDiffusionCoefficientEstimatorModified dcEst;
	private boolean useReducedModel;

	/**
	 * Constructs a newly allocated ConfinedDiffusionParametersFeature object. By
	 * default it uses the
	 * {@link de.biomedical_imaging.traJ.DiffusionCoefficientEstimator.RegressionDiffusionCoefficientEstimator}
	 * with min timelag 1 and maxtimelag 2
	 * 
	 * @param t       Trajectory for which the features should be estimated.
	 * @param timelag Timelag between two steps
	 */
	public ConfinedDiffusionParametersFeatureModified(TrajectoryModified t, double timelag, boolean useReducedModel) {
		this.t = t;
		this.timelag = timelag;
		dcEst = new RegressionDiffusionCoefficientEstimatorModified(null, 1 / timelag, 1, 2);
		this.useReducedModel = useReducedModel;
	}

	/**
	 * Constructs a newly allocated ConfinedDiffusionParametersFeature object.
	 * 
	 * @param t       Trajectory for which the features should be estimated.
	 * @param timelag Timelag between two steps
	 * @param dcEst   Estimateor for the diffusion coefficient.
	 */
	public ConfinedDiffusionParametersFeatureModified(TrajectoryModified t, double timelag, boolean useReducedModel,
			AbstractDiffusionCoefficientEstimatorModified dcEst) {
		this.t = t;
		this.timelag = timelag;
		this.dcEst = dcEst;
		this.useReducedModel = useReducedModel;
	}

	@Override
	/**
	 * @return Returns an double array with the elements [0] = squared radius (A),
	 *         [1] = Diffusion coefficent (D) [2] = shape parameter 1 (B), [3] shape
	 *         parameter 2 (C) and [4] Fit goodness. If reduced model was selected
	 *         then it return [0] = squared radius, [1] = diffusion coefficient, [3]
	 *         = goodness
	 */
	public double[] evaluate() {
		MeanSquaredDisplacmentFeatureModified msd = new MeanSquaredDisplacmentFeatureModified(t, 1);
		msd.setOverlap(false);

		ArrayList<Double> xDataList = new ArrayList<Double>();
		ArrayList<Double> yDataList = new ArrayList<Double>();

		for (int i = 1; i < t.size() / 3; i++) {
			msd.setTimelag(i);
			double[] res = msd.evaluate();
			double msdvalue = res[0];
			int N = (int) res[2];
			for (int j = 0; j < N; j++) {
				xDataList.add((double) i * timelag);
				yDataList.add(msdvalue);
			}
		}
		double[] xData = ArrayUtils.toPrimitive(xDataList.toArray(new Double[0]));
		double[] yData = ArrayUtils.toPrimitive(yDataList.toArray(new Double[0]));

		/*
		 * Estimate inital values
		 */
		MaxDistanceBetweenTwoPositionsFeatureModified maxdist = new MaxDistanceBetweenTwoPositionsFeatureModified(t);
		double estdia = maxdist.evaluate()[0];
		double estDC = dcEst.getDiffusionCoefficient(t, 1 / timelag)[0];

		double[] initialParams = new double[] { estdia * estdia, 0, 0, estDC };

		/*
		 * Do the fit and report the results
		 */

		ConfinedDiffusionMSDCurveFitModified cmsdfit = new ConfinedDiffusionMSDCurveFitModified();
		cmsdfit.setInitParameters(initialParams);
		cmsdfit.doFit(xData, yData, useReducedModel);
		if (useReducedModel) {
			result = new double[] { cmsdfit.getA(), cmsdfit.getD(), cmsdfit.getGoodness() };
		} else {
			result = new double[] { cmsdfit.getA(), cmsdfit.getD(), cmsdfit.getB(), cmsdfit.getC(),
					cmsdfit.getGoodness() };
		}

		return result;
	}

	@Override
	public String getName() {
		return "Confinement Parameters";
	}

	@Override
	public String getShortName() {
		return "CONFPARAM";
	}

	@Override
	public void setTrajectory(TrajectoryModified t) {
		this.t = t;
		result = null;
	}

}
