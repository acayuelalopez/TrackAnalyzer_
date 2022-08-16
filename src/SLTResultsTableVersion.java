import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import fiji.plugin.trackmate.features.edges.EdgeAnalyzer;
import javax.swing.ImageIcon;
import org.jgrapht.graph.DefaultWeightedEdge;

import fiji.plugin.trackmate.Dimension;
import fiji.plugin.trackmate.FeatureModel;
import fiji.plugin.trackmate.Model;
import fiji.plugin.trackmate.SelectionModel;
import fiji.plugin.trackmate.Spot;
import fiji.plugin.trackmate.TrackMate;
import fiji.plugin.trackmate.action.AbstractTMAction;
import fiji.plugin.trackmate.features.edges.EdgeTargetAnalyzer;
import fiji.plugin.trackmate.features.edges.EdgeTimeLocationAnalyzer;
import fiji.plugin.trackmate.features.track.TrackDurationAnalyzer;
import fiji.plugin.trackmate.features.track.TrackSpeedStatisticsAnalyzer;

import fiji.plugin.trackmate.util.ModelTools;
import ij.IJ;
import ij.WindowManager;
import ij.measure.ResultsTable;
import ij.text.TextPanel;
import ij.text.TextWindow;

public class SLTResultsTableVersion{

	//public static final ImageIcon ICON = new ImageIcon(TrackMateWizard.class.getResource("images/calculator.png"));

	public static final String NAME = "Export statistics to tables";

	public static final String KEY = "EXPORT_STATS_TO_IJ";

	public static final String INFO_TEXT = "<html>" + "Compute and export all statistics to 3 ImageJ results table. "
			+ "Statistisc are separated in features computed for: " + "<ol> " + "	<li> spots in filtered tracks; "
			+ "	<li> links between those spots; " + "	<li> filtered tracks. " + "</ol> "
			+ "For tracks and links, they are recalculated prior to exporting. Note "
			+ "that spots and links that are not in a filtered tracks are not part " + "of this export." + "</html>";

	private static final String SPOT_TABLE_NAME = "Spots in tracks statistics";

	private static final String EDGE_TABLE_NAME = "Links in tracks statistics";

	private static final String TRACK_TABLE_NAME = "Track statistics";

	private static final String ID_COLUMN = "ID";

	private static final String TRACK_ID_COLUMN = "TRACK_ID";

	private ResultsTable spotTable;

	private ResultsTable edgeTable;

	private ResultsTable trackTable;

	private final SelectionModel selectionModel;
	public static final String KEYLINEAR = "Linear track analysis";

	public static final String TRACK_TOTAL_DISTANCE_TRAVELED = "TOTAL_DISTANCE_TRAVELED";

	public static final String TRACK_MAX_DISTANCE_TRAVELED = "MAX_DISTANCE_TRAVELED";

	public static final String TRACK_CONFINMENT_RATIO = "CONFINMENT_RATIO";

	public static final String TRACK_MEAN_STRAIGHT_LINE_SPEED = "MEAN_STRAIGHT_LINE_SPEED";

	public static final String TRACK_LINEARITY_OF_FORWARD_PROGRESSION = "LINEARITY_OF_FORWARD_PROGRESSION";

	// public static final String TRACK_MEAN_DIRECTIONAL_CHANGE_RATE =
	// "MEAN_DIRECTIONAL_CHANGE_RATE";

	public static final String TOTAL_ABSOLUTE_ANGLE_XY = "TOTAL_ABSOLUTE_ANGLE_XY";

	public static final String TOTAL_ABSOLUTE_ANGLE_YZ = "TOTAL_ABSOLUTE_ANGLE_YZ";

	public static final String TOTAL_ABSOLUTE_ANGLE_ZX = "TOTAL_ABSOLUTE_ANGLE_ZX";

	public static final List<String> FEATURES = new ArrayList<>(9);

	public static final Map<String, String> FEATURE_NAMES = new HashMap<>(9);

	public static final Map<String, String> FEATURE_SHORT_NAMES = new HashMap<>(9);

	public static final Map<String, Dimension> FEATURE_DIMENSIONS = new HashMap<>(9);

	public static final Map<String, Boolean> IS_INT = new HashMap<>(9);

	public SLTResultsTableVersion(final SelectionModel selectionModel) {
		this.selectionModel = selectionModel;
	}

	/**
	 * Returns the results table containing the spot statistics, or
	 * <code>null</code> if the {@link #execute(TrackMate)} method has not been
	 * called.
	 *
	 * @return the results table containing the spot statistics.
	 */
	public ResultsTable executeSpot(TrackMate trackmate) {

		// Model
		final Model model = trackmate.getModel();
		final FeatureModel fm = model.getFeatureModel();

		// Export spots

		final Set<Integer> trackIDs = model.getTrackModel().trackIDs(true);
		final Collection<String> spotFeatures = trackmate.getModel().getFeatureModel().getSpotFeatures();

		spotTable = new ResultsTable();

		// Parse spots to insert values as objects
		for (final Integer trackID : trackIDs) {
			final Set<Spot> track = model.getTrackModel().trackSpots(trackID);
			// Sort by frame
			final List<Spot> sortedTrack = new ArrayList<>(track);
			Collections.sort(sortedTrack, Spot.frameComparator);

			for (final Spot spot : sortedTrack) {
				spotTable.incrementCounter();
				spotTable.addLabel(spot.getName());
				spotTable.addValue(ID_COLUMN, "" + spot.ID());
				spotTable.addValue("TRACK_ID", "" + trackID.intValue());
				for (final String feature : spotFeatures) {
					final Double val = spot.getFeature(feature);
					if (null == val) {
						spotTable.addValue(feature, "None");
					} else {
						if (fm.getSpotFeatureIsInt().get(feature).booleanValue()) {
							spotTable.addValue(feature, "" + val.intValue());
						} else {
							spotTable.addValue(feature, val.doubleValue());
						}
					}
				}
			}
		}

		return spotTable;

	}

	public ResultsTable executeLink(TrackMate trackmate) {

		// Model
		final Model model = trackmate.getModel();
		final FeatureModel fm = model.getFeatureModel();
		// Export edges

		final Set<Integer> trackIDs = model.getTrackModel().trackIDs(true);
		// Yield available edge feature
		final Collection<String> edgeFeatures = fm.getEdgeFeatures();

		edgeTable = new ResultsTable();

		// Sort by track
		for (final Integer trackID : trackIDs) {
			// Comparators
			final Comparator<DefaultWeightedEdge> edgeTimeComparator = ModelTools
					.featureEdgeComparator(EdgeTimeLocationAnalyzer.TIME, fm);
			final Comparator<DefaultWeightedEdge> edgeSourceSpotTimeComparator = new EdgeSourceSpotFrameComparator(
					model);

			final Set<DefaultWeightedEdge> track = model.getTrackModel().trackEdges(trackID);
			final List<DefaultWeightedEdge> sortedTrack = new ArrayList<>(track);

			/*
			 * Sort them by frame, if the EdgeTimeLocationAnalyzer feature is declared.
			 */

			if (model.getFeatureModel().getEdgeFeatures().contains(EdgeTimeLocationAnalyzer.KEY))
				Collections.sort(sortedTrack, edgeTimeComparator);
			else
				Collections.sort(sortedTrack, edgeSourceSpotTimeComparator);

			for (final DefaultWeightedEdge edge : sortedTrack) {
				edgeTable.incrementCounter();
				edgeTable.addLabel(edge.toString());
				edgeTable.addValue(TRACK_ID_COLUMN, "" + trackID.intValue());
				for (final String feature : edgeFeatures) {
					final Object o = fm.getEdgeFeature(edge, feature);
					if (o instanceof String) {
						continue;
					}
					final Number d = (Number) o;
					if (d == null) {
						edgeTable.addValue(feature, "None");
					} else {
						if (fm.getEdgeFeatureIsInt().get(feature).booleanValue()) {
							edgeTable.addValue(feature, "" + d.intValue());
						} else {
							edgeTable.addValue(feature, d.doubleValue());
						}

					}
				}

			}
		}

		// Hack to make the results tables in sync with selection model.

		// return edgeTable;
		return edgeTable;
	}

	public ResultsTable executeTrack(TrackMate trackmate) {

		FEATURES.add(TRACK_TOTAL_DISTANCE_TRAVELED);
		FEATURES.add(TRACK_MAX_DISTANCE_TRAVELED);
		FEATURES.add(TRACK_CONFINMENT_RATIO);
		FEATURES.add(TRACK_MEAN_STRAIGHT_LINE_SPEED);
		FEATURES.add(TRACK_LINEARITY_OF_FORWARD_PROGRESSION);
		// FEATURES.add( TRACK_MEAN_DIRECTIONAL_CHANGE_RATE );
		FEATURES.add(TOTAL_ABSOLUTE_ANGLE_XY);
		FEATURES.add(TOTAL_ABSOLUTE_ANGLE_YZ);
		FEATURES.add(TOTAL_ABSOLUTE_ANGLE_ZX);

		FEATURE_NAMES.put(TRACK_TOTAL_DISTANCE_TRAVELED, "Total distance traveled");
		FEATURE_NAMES.put(TRACK_MAX_DISTANCE_TRAVELED, "Max distance traveled");
		FEATURE_NAMES.put(TRACK_CONFINMENT_RATIO, "Confinment ratio");
		FEATURE_NAMES.put(TRACK_MEAN_STRAIGHT_LINE_SPEED, "Mean straight line speed");
		FEATURE_NAMES.put(TRACK_LINEARITY_OF_FORWARD_PROGRESSION, "Linearity of forward progression");
		// FEATURE_NAMES.put( TRACK_MEAN_DIRECTIONAL_CHANGE_RATE, "Mean directional
		// change rate" );
		FEATURE_NAMES.put(TOTAL_ABSOLUTE_ANGLE_XY, "Absolute angle in xy plane");
		FEATURE_NAMES.put(TOTAL_ABSOLUTE_ANGLE_YZ, "Absolute angle in yz plane");
		FEATURE_NAMES.put(TOTAL_ABSOLUTE_ANGLE_ZX, "Absolute angle in zx plane");

		FEATURE_SHORT_NAMES.put(TRACK_TOTAL_DISTANCE_TRAVELED, "Total dist.");
		FEATURE_SHORT_NAMES.put(TRACK_MAX_DISTANCE_TRAVELED, "Max dist.");
		FEATURE_SHORT_NAMES.put(TRACK_CONFINMENT_RATIO, "Cnfnmnt ratio");
		FEATURE_SHORT_NAMES.put(TRACK_MEAN_STRAIGHT_LINE_SPEED, "Mean v. line");
		FEATURE_SHORT_NAMES.put(TRACK_LINEARITY_OF_FORWARD_PROGRESSION, "Lin. fwd. progr.");
		// FEATURE_SHORT_NAMES.put( TRACK_MEAN_DIRECTIONAL_CHANGE_RATE, "Mean 𝛾 rate"
		// );
		FEATURE_SHORT_NAMES.put(TOTAL_ABSOLUTE_ANGLE_XY, "Abs. angle xy");
		FEATURE_SHORT_NAMES.put(TOTAL_ABSOLUTE_ANGLE_YZ, "Abs. angle yz");
		FEATURE_SHORT_NAMES.put(TOTAL_ABSOLUTE_ANGLE_ZX, "Abs. angle zx");

		FEATURE_DIMENSIONS.put(TRACK_TOTAL_DISTANCE_TRAVELED, Dimension.LENGTH);
		FEATURE_DIMENSIONS.put(TRACK_MAX_DISTANCE_TRAVELED, Dimension.LENGTH);
		FEATURE_DIMENSIONS.put(TRACK_CONFINMENT_RATIO, Dimension.NONE);
		FEATURE_DIMENSIONS.put(TRACK_MEAN_STRAIGHT_LINE_SPEED, Dimension.VELOCITY);
		FEATURE_DIMENSIONS.put(TRACK_LINEARITY_OF_FORWARD_PROGRESSION, Dimension.NONE);
		// FEATURE_DIMENSIONS.put( TRACK_MEAN_DIRECTIONAL_CHANGE_RATE, Dimension.RATE );
		FEATURE_DIMENSIONS.put(TOTAL_ABSOLUTE_ANGLE_XY, Dimension.ANGLE);
		FEATURE_DIMENSIONS.put(TOTAL_ABSOLUTE_ANGLE_YZ, Dimension.ANGLE);
		FEATURE_DIMENSIONS.put(TOTAL_ABSOLUTE_ANGLE_ZX, Dimension.ANGLE);

		IS_INT.put(TRACK_TOTAL_DISTANCE_TRAVELED, Boolean.FALSE);
		IS_INT.put(TRACK_MAX_DISTANCE_TRAVELED, Boolean.FALSE);
		IS_INT.put(TRACK_CONFINMENT_RATIO, Boolean.FALSE);
		IS_INT.put(TRACK_MEAN_STRAIGHT_LINE_SPEED, Boolean.FALSE);
		IS_INT.put(TRACK_LINEARITY_OF_FORWARD_PROGRESSION, Boolean.FALSE);
		// IS_INT.put( TRACK_MEAN_DIRECTIONAL_CHANGE_RATE, Boolean.FALSE );
		IS_INT.put(TOTAL_ABSOLUTE_ANGLE_XY, Boolean.FALSE);
		IS_INT.put(TOTAL_ABSOLUTE_ANGLE_YZ, Boolean.FALSE);
		IS_INT.put(TOTAL_ABSOLUTE_ANGLE_ZX, Boolean.FALSE);

		// Model
		final Model model = trackmate.getModel();
		final FeatureModel fm = model.getFeatureModel();
		// Export tracks

		final Set<Integer> trackIDs = model.getTrackModel().trackIDs(true);
		// Yield available edge feature

		trackTable = new ResultsTable();

		// Sort by track
		for (Integer trackID : trackIDs) {

			final List<Spot> spots = new ArrayList<>(model.getTrackModel().trackSpots(trackID));
			Collections.sort(spots, Spot.frameComparator);
			final Spot first = spots.get(0);

			/*
			 * Iterate over edges.
			 */

			final Set<DefaultWeightedEdge> edges = model.getTrackModel().trackEdges(trackID);

			double totalDistance = 0.0;
			double maxDistanceSq = Double.NEGATIVE_INFINITY;
			double maxDistance = 0.0;
			double dx = 0;
			double dy = 0;
			double dz = 0;

			for (final DefaultWeightedEdge edge : edges) {
				// Total distance travelled.
				final Spot source = model.getTrackModel().getEdgeSource(edge);
				final Spot target = model.getTrackModel().getEdgeTarget(edge);
				final double d = Math.sqrt(source.squareDistanceTo(target));
				totalDistance += d;

				// Max distance traveled.
				final double dToFirstSq = first.squareDistanceTo(target);
				if (dToFirstSq > maxDistanceSq) {
					maxDistanceSq = dToFirstSq;
					maxDistance = Math.sqrt(maxDistanceSq);
				}

				dx += target.getDoublePosition(0) - source.getDoublePosition(0);
				dy += target.getDoublePosition(1) - source.getDoublePosition(1);
				dz += target.getDoublePosition(2) - source.getDoublePosition(2);
			}

			// Dependency features.
			final double netDistance = fm.getTrackFeature(trackID, TrackDurationAnalyzer.TRACK_DISPLACEMENT);
			final double tTotal = fm.getTrackFeature(trackID, TrackDurationAnalyzer.TRACK_DURATION);
			final double vMean = fm.getTrackFeature(trackID, TrackSpeedStatisticsAnalyzer.TRACK_MEAN_SPEED);

			// Our features.
			final double confinmentRatio = netDistance / totalDistance;
			final double meanStraightLineSpeed = netDistance / tTotal;
			final double linearityForwardProgression = meanStraightLineSpeed / vMean;
			// final double meanAngleSpeed = sumAngleSpeed / nAngleSpeed;

			// Angle features.
			final double angleXY = Math.atan2(dy, dx);
			final double angleYZ = Math.atan2(dz, dy);
			final double angleZX = Math.atan2(dx, dz);
			final Collection<String> trackFeatures = fm.getTrackFeatures();
			trackTable.incrementCounter();
			trackTable.addLabel(model.getTrackModel().name(trackID));
			trackTable.addValue(TRACK_ID_COLUMN, "" + trackID.intValue());

			for (final String feature : trackFeatures) {
				final Double val = fm.getTrackFeature(trackID, feature);
				if (null == val) {
					trackTable.addValue(feature, "None");
				} else {
					if (fm.getTrackFeatureIsInt().get(feature).booleanValue()) {
						trackTable.addValue(feature, "" + val.intValue());
					} else {
						trackTable.addValue(feature, val.doubleValue());
					}
				}
			}

			trackTable.addValue(TRACK_TOTAL_DISTANCE_TRAVELED, "" + (double) Math.round(totalDistance * 1000d) / 1000d);
			trackTable.addValue(TRACK_MAX_DISTANCE_TRAVELED, "" + (double) Math.round(maxDistance * 1000d) / 1000d);
			trackTable.addValue(TRACK_MEAN_STRAIGHT_LINE_SPEED,
					"" + (double) Math.round(meanStraightLineSpeed * 1000d) / 1000d);
			trackTable.addValue(TRACK_LINEARITY_OF_FORWARD_PROGRESSION,
					"" + (double) Math.round(linearityForwardProgression * 1000d) / 1000d);
			trackTable.addValue(TOTAL_ABSOLUTE_ANGLE_XY, "" + (double) Math.round(angleXY * 1000d) / 1000d);
			trackTable.addValue(TOTAL_ABSOLUTE_ANGLE_YZ, "" + (double) Math.round(angleYZ * 1000d) / 1000d);
			trackTable.addValue(TOTAL_ABSOLUTE_ANGLE_ZX, "" + (double) Math.round(angleZX * 1000d) / 1000d);
			trackTable.addValue(TRACK_CONFINMENT_RATIO, "" + (double) Math.round(confinmentRatio * 1000d) / 1000d);
			trackTable.addValue("TRACK_CLASSIFICATION", "");
			// for (int row = 0; row < trackTable.size(); row++) {
			if (confinmentRatio == 0.0)
				trackTable.addValue("TRACK_CLASSIFICATION", "Total-Confined Track");
			if (confinmentRatio == 1.0)
				trackTable.addValue("TRACK_CLASSIFICATION", "Perfectly Straight Track");
			if (confinmentRatio > 0.0 && confinmentRatio <= 0.5)
				trackTable.addValue("TRACK_CLASSIFICATION", "Strongly Confined Track");
			if (confinmentRatio > 0.05 && confinmentRatio <= 0.25)
				trackTable.addValue("TRACK_CLASSIFICATION", "Purely Random Track");
			if (confinmentRatio > 0.25 && confinmentRatio < 1.0)
				trackTable.addValue("TRACK_CLASSIFICATION", "Fairly Straight Track");
			// }
		}

		// trackTable.show("Track statistics");
		return trackTable;
	}

	/**
	 * Returns the results table containing the edge statistics, or
	 * <code>null</code> if the {@link #execute(TrackMate)} method has not been
	 * called.
	 *
	 * @return the results table containing the edge statistics.
	 */
	public ResultsTable getEdgeTable() {
		return edgeTable;
	}

	/**
	 * Returns the results table containing the track statistics, or
	 * <code>null</code> if the {@link #execute(TrackMate)} method has not been
	 * called.
	 *
	 * @return the results table containing the track statistics.
	 */
	public ResultsTable getTrackTable() {
		return trackTable;
	}

	private static final class EdgeSourceSpotFrameComparator implements Comparator<DefaultWeightedEdge> {

		private final Model model;

		public EdgeSourceSpotFrameComparator(final Model model) {
			this.model = model;
		}

		@Override
		public int compare(final DefaultWeightedEdge e1, final DefaultWeightedEdge e2) {
			final double t1 = model.getTrackModel().getEdgeSource(e1).getFeature(Spot.FRAME).doubleValue();
			final double t2 = model.getTrackModel().getEdgeSource(e2).getFeature(Spot.FRAME).doubleValue();
			if (t1 < t2) {
				return -1;
			}
			if (t1 > t2) {
				return 1;
			}
			return 0;
		}

	}

}