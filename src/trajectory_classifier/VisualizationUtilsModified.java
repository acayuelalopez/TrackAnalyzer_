package trajectory_classifier;

import ij.gui.PolygonRoi;
import ij.gui.Roi;
import ij.gui.TextRoi;
import ij.process.FloatPolygon;
import traJ.TrajectoryModified;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import features.AbstractMeanSquaredDisplacmentEvaluatorModified;
import features.MeanSquaredDisplacmentFeatureModified;

public class VisualizationUtilsModified {

	public static ArrayList<Roi> generateVisualizationRoisFromTrack(SubtrajectoryModified t, Color c, boolean showID) {
		return generateVisualizationRoisFromTrack(t, c, showID, 1);
	}


	public static ArrayList<Roi> generateVisualizationRoisFromTrack(SubtrajectoryModified t, Color c, boolean showID,
			double pixelsize) {
		ArrayList<Roi> proi = new ArrayList<Roi>();
		FloatPolygon p = new FloatPolygon();
		double sumx = 0;
		double sumy = 0;
		TextRoi.setFont("TimesRoman", 5, Font.PLAIN);
		for (int i = 0; i < t.getParent().size(); i++) {
			int to = t.size();
			if (i < t.size()) {

				sumx += t.get(i).x / pixelsize;
				sumy += t.get(i).y / pixelsize;
				p.addPoint(t.get(i).x / pixelsize, t.get(i).y / pixelsize);

				to = i + 1;
			}

			PolygonRoi pr = new PolygonRoi(new FloatPolygon(p.xpoints, p.ypoints, to), PolygonRoi.POLYLINE);
			pr.setStrokeColor(c);
			pr.setPosition(t.getRelativeStartTimepoint() + i + 1);
			proi.add(pr);

			if (showID) {
				long parentID = t.getParent().getID();
				TextRoi troi = new TextRoi(sumx / to, sumy / to, " " + parentID + ":" + t.getID() + " ");
				troi.setPosition(t.getRelativeStartTimepoint() + i + 1);
				troi.setFillColor(Color.BLACK);
				troi.setStrokeColor(c);
				troi.setAntialiased(true);
				proi.add(troi);
			}
		}
		return proi;
	}

}