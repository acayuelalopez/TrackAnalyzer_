package smileModified;

public class GaussianDistributionModified extends AbstractDistributionModified implements ExponentialFamilyModified {
    private static final long serialVersionUID = 2L;

    private static final double LOG2PIE_2 = Math.log(2 * Math.PI * Math.E) / 2;
    private static final double LOG2PI_2 = Math.log(2 * Math.PI) / 2;
    private static final GaussianDistributionModified singleton = new GaussianDistributionModified(0.0, 1.0);

    /** The mean. */
    public final double mu;
    /** The standard deviation. */
    public final double sigma;
    /** The variance. */
    private final double variance;
    /** Shannon entropy. */
    private final double entropy;
    /** The constant factor in PDF. */
    private final double pdfConstant;

    /**
     * Constructor
     * @param mu mean.
     * @param sigma standard deviation.
     */
    public GaussianDistributionModified(double mu, double sigma) {
        this.mu = mu;
        this.sigma = sigma;
        variance = sigma * sigma;

        entropy = Math.log(sigma) + LOG2PIE_2;
        pdfConstant = Math.log(sigma) + LOG2PI_2;
    }

    /**
     * Estimates the distribution parameters by MLE.
     * @param data the training data.
     * @return the distribution.
     */
    public static GaussianDistributionModified fit(double[] data) {
        double mu = MathExModified.mean(data);
        double sigma = MathExModified.sd(data);
        return new GaussianDistributionModified(mu, sigma);
    }

    /**
     * Returns the standard normal distribution.
     * @return the standard normal distribution.
     */
    public static GaussianDistributionModified getInstance() {
        return singleton;
    }

    @Override
    public int length() {
        return 2;
    }

    @Override
    public double mean() {
        return mu;
    }

    @Override
    public double variance() {
        return variance;
    }

    @Override
    public double sd() {
        return sigma;
    }

    @Override
    public double entropy() {
        return entropy;
    }

    @Override
    public String toString() {
        return String.format("Gaussian Distribution(%.4f, %.4f)", mu, sigma);
    }

    /**
     * The Box-Muller algorithm generate a pair of random numbers.
     * z1 is to cache the second one.
     */
    private double z1 = Double.NaN;

    /**
     * Generates a Gaussian random number with the Box-Muller algorithm.
     */
    @Override
    public double rand() {
        double z0, x, y, r, z;

        if (Double.isNaN(z1)) {
            do {
                x = MathExModified.random(-1, 1);
                y = MathExModified.random(-1, 1);
                r = x * x + y * y;
            } while (r >= 1.0);

            z = Math.sqrt(-2.0 * Math.log(r) / r);
            z1 = x * z;
            z0 = y * z;
        } else {
            z0 = z1;
            z1 = Double.NaN;
        }

        return mu + sigma * z0;
    }
    
    /**
     * Generates a Gaussian random number with the inverse CDF method.
     * @return a random number.
     */
    public double inverseCDF() {
        final double a0 = 2.50662823884;
        final double a1 = -18.61500062529;
        final double a2 = 41.39119773534;
        final double a3 = -25.44106049637;
        final double b0 = -8.47351093090;
        final double b1 = 23.08336743743;
        final double b2 = -21.06224101826;
        final double b3 = 3.13082909833;
        final double c0 = 0.3374754822726147;
        final double c1 = 0.9761690190917186;
        final double c2 = 0.1607979714918209;
        final double c3 = 0.0276438810333863;
        final double c4 = 0.0038405729373609;
        final double c5 = 0.0003951896511919;
        final double c6 = 0.0000321767881768;
        final double c7 = 0.0000002888167364;
        final double c8 = 0.0000003960315187;

        double y, r, x;

        double u = MathExModified.random();
        while (u == 0.0) {
            u = MathExModified.random();
        }
        
        y = u - 0.5;

        if (Math.abs(y) < 0.42) {
            r = y * y;
            x = y * (((a3 * r + a2) * r + a1) * r + a0)
                    / ((((b3 * r + b2) * r + b1) * r + b0) * r + 1);

        } else {
            r = u;
            if (y > 0) {
                r = 1 - u;
            }
            r = Math.log(-Math.log(r));
            x = c0 + r * (c1 + r * (c2 + r * (c3 + r * (c4 + r * (c5 + r * (c6 + r * (c7 + r * c8)))))));
            if (y < 0) {
                x = -(x);
            }
        }
        
        return mu + sigma * x;
    }

    @Override
    public double p(double x) {
        if (sigma == 0) {
            if (x == mu) {
                return 1.0;
            } else {
                return 0.0;
            }
        }

        return Math.exp(logp(x));
    }

    @Override
    public double logp(double x) {
        if (sigma == 0) {
            if (x == mu) {
                return 0.0;
            } else {
                return Double.NEGATIVE_INFINITY;
            }
        }

        double d = x - mu;
        return -0.5 * d * d / variance - pdfConstant;
    }

    @Override
    public double cdf(double x) {
        if (sigma == 0) {
            if (x < mu) {
                return 0.0;
            } else {
                return 1.0;
            }
        }

        return 0.5 * ErfModified.erfc(-0.707106781186547524 * (x - mu) / sigma);
    }

    /**
     * The quantile, the probability to the left of quantile(p) is p. This is
     * actually the inverse of cdf.
     *
     * Original algorythm and Perl implementation can
     * be found at: http://www.math.uio.no/~jacklam/notes/invnorm/index.html
     */
    @Override
    public double quantile(double p) {
        if (p < 0.0 || p > 1.0) {
            throw new IllegalArgumentException("Invalid p: " + p);
        }

        if (sigma == 0.0) {
            if (p < 1.0) {
                return mu - 1E-10;
            } else {
                return mu;
            }
        }
        return -1.41421356237309505 * sigma * ErfModified.inverfc(2.0 * p) + mu;
    }

    @Override
    public MixtureModified.Component M(double[] x, double[] posteriori) {
        double alpha = 0.0;
        double mean = 0.0;
        double sd = 0.0;

        for (int i = 0; i < x.length; i++) {
            alpha += posteriori[i];
            mean += x[i] * posteriori[i];
        }

        mean /= alpha;

        for (int i = 0; i < x.length; i++) {
            double d = x[i] - mean;
            sd += d * d * posteriori[i];
        }

        sd = Math.sqrt(sd / alpha);

        return new MixtureModified.Component(alpha, new GaussianDistributionModified(mean, sd));
    }
}
