package math;

import ij.IJ;
import ij.measure.CurveFitter;
import ij.measure.Minimizer;


public class PowerLawCurveFitModified {
	
	private double alpha;
	private double dc;
	private double goodness;

	public PowerLawCurveFitModified() {
		
	}
	
	public void doFit(double[] xdata, double[] ydata) {
		doFit(xdata, ydata, false, 0, 0);
	}
	
	public void doFit(double[] xdata, double[] ydata, double initalAlpha, double  initalDiffCoeff){
		doFit(xdata, ydata, true, initalAlpha, initalDiffCoeff);
	}
	
	private void doFit(double[] xdata, double[] ydata, boolean useInitialGuess, double initalAlpha, double  initalDiffCoeff){
			CurveFitter fitter = new CurveFitter(xdata, ydata);
			if(useInitialGuess){
				fitter.setInitialParameters(new double[]{initalDiffCoeff,alpha});
			}
			double init[] =null;
			if(useInitialGuess){
				init = new double[]{initalAlpha,initalDiffCoeff};
			}
			fitter.doFit(CurveFitter.POWER_REGRESSION);
			double params[] = fitter.getParams();
			boolean failed = (fitter.getStatus()!=Minimizer.SUCCESS);
			if(failed){
				alpha = -1;
				dc = -1;
				goodness = 0;
			}else{
				alpha = params[1];
				dc = params[0]/4.0; 
				goodness = fitter.getFitGoodness();
			}
			if(failed || alpha < 0 || dc < 0){
		
				fitter = new CurveFitter(xdata, ydata);
				for(int i = 0; i < ydata.length; i++){
					ydata[i] = Math.log(ydata[i]);
				}
				//fitter.doFit(CurveFitter.POWER_REGRESSION);
				fitter.doCustomFit("y=sqrt(a*a)*log(x)+log(4*sqrt(b*b))", init, false);
				params = fitter.getParams();
				alpha = Math.abs(params[0]);
				dc = Math.abs(params[1]); 
				goodness = fitter.getFitGoodness();
			}
	}
	
	public double getAlpha(){
		return alpha;
	}
	
	public double getDiffusionCoefficient(){
		return dc;
	}
	
	public double getGoodness(){
		return goodness;
	}

}
