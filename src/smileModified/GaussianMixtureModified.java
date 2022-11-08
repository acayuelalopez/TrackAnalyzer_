package smileModified;

/**
 * Finite univariate Gaussian mixture. The EM algorithm is provide to learned
 * the mixture model from data. BIC score is employed to estimate the number
 * of components.
 *
 * @author Haifeng Li
 */
public class GaussianMixtureModified extends ExponentialFamilyMixtureModified {
    private static final long serialVersionUID = 2L;
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(GaussianMixtureModified.class);

    /**
     * Constructor.
     * @param components a list of multivariate Gaussian distributions.
     */
    public GaussianMixtureModified(Component... components) {
        this(0.0, 1, components);
    }

    /**
     * Constructor.
     * @param components a list of multivariate Gaussian distributions.
     * @param L the log-likelihood.
     * @param n the number of samples to fit the distribution.
     */
    private GaussianMixtureModified(double L, int n, Component... components) {
        super(L, n, components);

        for (Component component : components) {
            if (!(component.distribution instanceof GaussianDistributionModified)) {
                throw new IllegalArgumentException("Component " + component + " is not of Gaussian distribution.");
            }
        }
    }

    /**
     * Fits the Gaussian mixture model with the EM algorithm.
     * @param k the number of components.
     * @param x the training data.
     * @return the distribution.
     */
    public static GaussianMixtureModified fit(int k, double[] x) {
        if (k < 2)
            throw new IllegalArgumentException("Invalid number of components in the mixture.");

        double min = MathExModified.min(x);
        double max = MathExModified.max(x);
        double step = (max - min) / (k+1);

        Component[] components = new Component[k];
        for (int i = 0; i < k; i++) {
            components[i] = new Component(1.0/k, new GaussianDistributionModified(min+=step, step));
        }

        ExponentialFamilyMixtureModified model = fit(x, components);
        return new GaussianMixtureModified(model.L, x.length, model.components);
    }

    /**
     * Fits the Gaussian mixture model with the EM algorithm.
     * The number of components will be selected by BIC.
     * @param x the training data.
     * @return the distribution.
     */
    public static GaussianMixtureModified fit(double[] x) {
        if (x.length < 20) {
            throw new IllegalArgumentException("Too few samples.");
        }

        GaussianMixtureModified mixture = new GaussianMixtureModified(new Component(1.0, GaussianDistributionModified.fit(x)));
        double bic = mixture.bic(x);
        logger.info(String.format("The BIC of %s = %.4f", mixture, bic));

        for (int k = 2; k < x.length / 10; k++) {
            ExponentialFamilyMixtureModified model = fit(k, x);
            logger.info(String.format("The BIC of %s = %.4f", model, model.bic));

            if (model.bic <= bic) break;

            mixture = new GaussianMixtureModified(model.L, x.length, model.components);
            bic = model.bic;
        }

        return mixture;
    }

    /**
     * Split the most heterogeneous cluster along its main direction (eigenvector).
     */
    private static Component[] split(Component... components) {
        // Find most dispersive cluster (biggest sigma)
        int k = components.length;
        int index = -1;
        double maxSigma = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < k; i++) {
            Component c = components[i];
            if (c.distribution.sd() > maxSigma) {
                maxSigma = c.distribution.sd();
                index = i;
            }
        }

        // Splits the component
        Component component = components[index];
        double priori = component.priori / 2;
        double delta = component.distribution.sd();
        double mu = component.distribution.mean();
        
        Component[] mixture = new Component[k+1];
        System.arraycopy(components, 0, mixture, 0, k);
        mixture[index] = new Component(priori, new GaussianDistributionModified(mu + delta/2, delta));
        mixture[k] = new Component(priori, new GaussianDistributionModified(mu - delta/2, delta));
        return mixture;
    }
}
