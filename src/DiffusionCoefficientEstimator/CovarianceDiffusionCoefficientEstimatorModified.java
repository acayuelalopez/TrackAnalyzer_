package DiffusionCoefficientEstimator;
import features.AbstractTrajectoryFeatureModified;
import traJ.TrajectoryModified;
import traJ.TrajectoryValidIndexTimelagIteratorModified;

/**
 * This class implements the covariance estimator as described in:
 * C. L. Vestergaard, P. C. Blainey, and H. Flyvbjerg, 
 * “Optimal estimation of diffusion coefficients from single-particle trajectories,” 
 * Phys. Rev. E - Stat. Nonlinear, Soft Matter Phys., vol. 89, no. 2, p. 022726, Feb. 2014.
 * @author Thorsten Wagner (wagner@biomedical-imaging.de)
 *
 */
public class CovarianceDiffusionCoefficientEstimatorModified extends AbstractTrajectoryFeatureModified implements AbstractDiffusionCoefficientEstimatorModified {
	
	private TrajectoryModified t;
	private double fps;
	
	 public CovarianceDiffusionCoefficientEstimatorModified() {
		// TODO Auto-generated constructor stub
	}
	
	public CovarianceDiffusionCoefficientEstimatorModified(TrajectoryModified t, double fps) {
		this.t = t;
		this.fps = fps;
	}
	
	
	private double getDistanceProductX(TrajectoryModified t, int n,int m){
		double xn = t.get(n+1).x - t.get(n).x;
		double xm = t.get(m+1).x - t.get(m).x; 

		return xn*xm;
	}
	
	private double getDistanceProductY(TrajectoryModified t,int n,int m){
		double xn = t.get(n+1).y - t.get(n).y;
		double xm = t.get(m+1).y - t.get(m).y;
		return xn*xm;
	}
	
	private double getDistanceProductZ(TrajectoryModified t,int n,int m){
		double xn = t.get(n+1).z - t.get(n).z;
		double xm = t.get(m+1).z - t.get(m).z;
		return xn*xm;
	}
	
	

	/**
	 * @return [0] diffusion coefficient [1] localization noise (sigma) in x-direction [2] loc. noise (sigma) in y-diretction [3] loc. noise (sigma) in z-direction
	 */
	public double[] getDiffusionCoefficient(TrajectoryModified t, double fps) {
		double[] cov = getCovData(t, fps,0);
		return cov;
	}
	
	
	private double[] getCovData(TrajectoryModified track, double fps, double R){
		
		double sumX = 0;
		double sumX2 = 0;
		double sumY = 0;
		double sumY2 = 0;
		double sumZ = 0;
		double sumZ2 = 0;
		int N=0;
		int M=0;
		TrajectoryValidIndexTimelagIteratorModified it = new TrajectoryValidIndexTimelagIteratorModified(track, 1);
		while(it.hasNext()){
			int i = it.next();
			sumX = sumX + getDistanceProductX(track,i, i) ;
			sumY = sumY + getDistanceProductY(track,i, i) ;
			sumZ = sumZ + getDistanceProductZ(track,i, i) ;
			N++;
			if((i+2) < track.size() &&  track.get(i+2) !=null){
				sumX2 = sumX2 + getDistanceProductX(track,i, i+1) ;
				sumY2 = sumY2 + getDistanceProductY(track,i, i+1);
				sumZ2 = sumZ2 + getDistanceProductZ(track,i, i+1);
				M++;
			}
		}
		
		double msdX = (sumX/(N));
		
		double msdY = (sumY/(N));
		double msdZ = (sumZ/(N));
		
		double covX = (sumX2/(M) );
		double covY = (sumY2/(M) );
		double covZ = (sumZ2/(M) );
		
		double termXA = msdX/2 * fps;
		double termXB = covX * fps ;
		double termYA = msdY/2 * fps;
		double termYB = covY * fps;
		double termZA = msdZ/2 * fps;
		double termZB = covZ * fps;
		
		double DX = termXA+termXB;	
		double DY = termYA+termYB;
		double DZ = termZA+termZB;
		double D;
		D= (DX+DY+DZ)/track.getDimension();
	
		
		double[] data  = new double[4]; //[0] = Diffusioncoefficient, [1] = LocNoiseX, [2] = LocNoiseY
		data[0] = D;
		data[1] = Math.sqrt(Math.abs(covX)); //R*msdX + (2*R-1)+covX;
		data[2] = Math.sqrt(Math.abs(covY)); //R*msdY + (2*R-1)+covY;
		data[3] = Math.sqrt(Math.abs(covZ)); //R*msdZ + (2*R-1)+covZ;
		
		return data;
	}

	@Override
	public double[] evaluate() {
		result = getDiffusionCoefficient(t, fps);
		return result;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Diffusion coefficient (Covariance)";
	}

	@Override
	public String getShortName() {
		// TODO Auto-generated method stub
		return "DC-COV";
	}

	@Override
	public void setTrajectory(TrajectoryModified t) {
		this.t = t;
		
	}
		
}
