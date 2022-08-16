package trajectory_classifier;

import ij.IJ;
import traJ.TrajectoryModified;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.renjin.eval.EvalException;
import org.renjin.parser.ParseException;
import org.renjin.sexp.DoubleVector;
import org.renjin.sexp.SEXP;
import org.renjin.sexp.StringVector;

import DiffusionCoefficientEstimator.RegressionDiffusionCoefficientEstimatorModified;
import features.Asymmetry3FeatureModified;
import features.EfficiencyFeatureModified;
import features.FractalDimensionFeatureModified;
import features.GaussianityFeautureModified;
import features.KurtosisFeatureModified;
import features.MSDRatioFeatureModified;
import features.PowerLawFeatureModified;
import features.StraightnessFeatureModified;
import features.TrappedProbabilityFeatureModified;

public class RRFClassifierRenjinModified extends AbstractClassifierModified {

	// private ScriptEngine engine = null;
	ScriptEngine engine = null;
	private String pathToModel;
	private double[] confindence;
	private double timelag;

	public RRFClassifierRenjinModified(String pathToModel, double timelag) {
		this.pathToModel = pathToModel;
		this.timelag = timelag;
	}

	public void setTimelag(double timelag) {
		this.timelag = timelag;
	}

	@Override
	public String classify(TrajectoryModified t) {
		ArrayList<TrajectoryModified> tracks = new ArrayList<TrajectoryModified>();
		tracks.add(t);

		return classify(tracks)[0];
	}

	@Override
	public void start() {
//		RenjinScriptEngineFactory factory = new RenjinScriptEngineFactory();
//		engine = factory.getScriptEngine();

		ScriptEngineManager manager = new ScriptEngineManager();
		// create a Renjin engine:
		engine = manager.getEngineByName("Renjin");
		try {
			engine.eval("library(randomForest)");
			engine.eval("library(plyr)");
			engine.eval("load(\"" + pathToModel + "\")");
		} catch (ScriptException e) {
			e.printStackTrace();
		}

		// check if the engine has loaded correctly:
		if (engine == null) {
			throw new RuntimeException("Renjin Script Engine not found on the classpath.");
		}

	}

	@Override
	public void stop() {
		engine = null;

	}

	@Override
	public String[] classify(ArrayList<TrajectoryModified> tracks) {

		int N = tracks.size();

		String[] result = new String[N];
		double[] fd = new double[N];
		double[] power = new double[N];
		Arrays.fill(power, -1);
		double[] asym3 = new double[N];
		double[] efficiency = new double[N];
		double[] kurtosis = new double[N];
		double[] msdratio = new double[N];
		double[] straightness = new double[N];
		double[] trappedness = new double[N];
		double[] gaussianity = new double[N];
		double[] pwrDCs = new double[N];
		Arrays.fill(power, -1);
		int cores = Runtime.getRuntime().availableProcessors();
		ExecutorService pool = Executors.newFixedThreadPool(cores);

		for (int i = 0; i < tracks.size(); i++) {
			TrajectoryModified t = tracks.get(i);

			FractalDimensionFeatureModified fdF = new FractalDimensionFeatureModified(t);
			pool.submit(new FeatureWorkerModified(fd, i, fdF, FeatureWorkerModified.EVALTYPE.FIRST));
			double initDC = 0;
			double initAlpha = 0;
			if (i - 1 > 0 && power[i - 1] > 0 && pwrDCs[i - 1] > 0) {
				initDC = pwrDCs[i - 1];
				initAlpha = power[i - 1];

			} else {
				RegressionDiffusionCoefficientEstimatorModified regest = new RegressionDiffusionCoefficientEstimatorModified(
						t, 1.0 / timelag, 1, 3);
				initDC = regest.evaluate()[0];
				initAlpha = 0.5;
			}

			PowerLawFeatureModified pwf = new PowerLawFeatureModified(t, 1 / timelag, 1, t.size() / 3, initAlpha,
					initDC);
			pool.submit(new FeatureWorkerModified(power, i, pwf, FeatureWorkerModified.EVALTYPE.FIRST));
			pool.submit(new FeatureWorkerModified(pwrDCs, i, pwf, FeatureWorkerModified.EVALTYPE.SECOND));

			Asymmetry3FeatureModified asymf3 = new Asymmetry3FeatureModified(t);
			pool.submit(new FeatureWorkerModified(asym3, i, asymf3, FeatureWorkerModified.EVALTYPE.FIRST));

			EfficiencyFeatureModified eff = new EfficiencyFeatureModified(t);
			pool.submit(new FeatureWorkerModified(efficiency, i, eff, FeatureWorkerModified.EVALTYPE.FIRST));

			KurtosisFeatureModified kurtf = new KurtosisFeatureModified(t);
			pool.submit(new FeatureWorkerModified(kurtosis, i, kurtf, FeatureWorkerModified.EVALTYPE.FIRST));

			MSDRatioFeatureModified msdr = new MSDRatioFeatureModified(t, 1, 5);
			pool.submit(new FeatureWorkerModified(msdratio, i, msdr, FeatureWorkerModified.EVALTYPE.FIRST));

			StraightnessFeatureModified straight = new StraightnessFeatureModified(t);
			pool.submit(new FeatureWorkerModified(straightness, i, straight, FeatureWorkerModified.EVALTYPE.FIRST));

			TrappedProbabilityFeatureModified trappf = new TrappedProbabilityFeatureModified(t);
			pool.submit(new FeatureWorkerModified(trappedness, i, trappf, FeatureWorkerModified.EVALTYPE.FIRST));

			GaussianityFeautureModified gaussf = new GaussianityFeautureModified(t, 1);
			pool.submit(new FeatureWorkerModified(gaussianity, i, gaussf, FeatureWorkerModified.EVALTYPE.FIRST));

		}
		pool.shutdown();

		try {
			pool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (false) {
			System.out.println("FD " + fd[0]);
			System.out.println("Power " + power[0]);
			System.out.println("asymmetry3 " + asym3[0]);
			System.out.println("efficiency " + efficiency[0]);
			System.out.println("kurtosis " + kurtosis[0]);
			System.out.println("msdratio " + msdratio[0]);
			System.out.println("straightness " + straightness[0]);
			System.out.println("trappedness " + trappedness[0]);
			System.out.println("gaussianity " + gaussianity[0]);
		}
		try {

			engine.put("fd", fd);
			engine.put("power", power);
			engine.put("asymmetry3", asym3);
			engine.put("efficiency", efficiency);
			engine.put("kurtosis", kurtosis);
			engine.put("msdratio", msdratio);
			engine.put("straightness", straightness);
			engine.put("trappedness", trappedness);
			engine.put("gaussianity", gaussianity);

			engine.eval("data<-data.frame(FD=fd," + "POWER=power,"
					+ "MSD.R=msdratio,ASYM3=asymmetry3,EFFICENCY=efficiency, KURT=kurtosis,"
					+ "STRAIGHTNESS=straightness," + "TRAPPED=trappedness,GAUSS=gaussianity)");

			engine.eval("features.predict <- predict(model,data,type=\"prob\")");
			engine.eval("fprob<-features.predict");

			if (tracks.size() > 1) {
				engine.eval("probs <- as.vector(apply(fprob[1:nrow(fprob),],1,max))");
				engine.eval("indexmax <- as.vector(apply(fprob[1:nrow(fprob),],1,which.max))");
			} else {
				engine.eval("probs <- max(fprob)");
				engine.eval("indexmax <- which.max(fprob)");
			}
			engine.eval("mynames <- colnames(fprob)");
			engine.eval("maxclass <- mynames[indexmax]");
			StringVector res = (StringVector) engine.eval("maxclass");
			result = res.toArray();
			DoubleVector confi = (DoubleVector) engine.eval("probs");
			confindence = confi.toDoubleArray();
		} catch (ParseException e) {
			System.out.println("R script parse error: " + e.getMessage());
		} catch (ScriptException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (EvalException e) {
			e.printStackTrace();
		}

		return result;
	}

	@Override
	public double[] getConfindence() {
		// TODO Auto-generated method stub
		return confindence;
	}

}
