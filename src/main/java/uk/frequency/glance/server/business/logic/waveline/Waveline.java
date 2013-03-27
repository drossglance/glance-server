package uk.frequency.glance.server.business.logic.waveline;

import static java.lang.Math.max;
import static java.lang.Math.min;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.interpolation.SplineInterpolator;

import uk.frequency.glance.server.business.logic.waveline.streamgraph.ColorPicker;
import uk.frequency.glance.server.business.logic.waveline.streamgraph.Layer;
import uk.frequency.glance.server.business.logic.waveline.streamgraph.LayerLayout;
import uk.frequency.glance.server.business.logic.waveline.streamgraph.LayerSort;
import uk.frequency.glance.server.business.logic.waveline.streamgraph.NoLayerSort;
import uk.frequency.glance.server.business.logic.waveline.streamgraph.StreamLayout;

public class Waveline {

	private static final int SMOOTHNESS = 10; //how many points are added in-between the actual data
	
	LayerLayout layout;
	LayerSort ordering;
	ColorPicker coloring;
	
	Layer[] layers;
	int numLayers, layerSize;
	int minX, maxX, dX;
	float minY, maxY, dY;
	
	public Waveline(Layer[] layers){
		this.layers = layers;
		ordering = new NoLayerSort();
		layout = new StreamLayout();
//		layout = new MinimizedWiggleLayout();
//		layout = new ThemeRiverLayout();
		coloring = new RandomColorPicker();
		init();
	}
	
	void init(){
		addBackgroundNoise(layers);
		interpolate(layers);
		numLayers = layers.length;
		layerSize = layers[0].size.length;
		layers = ordering.sort(layers);
		layout.layout(layers);
		coloring.colorize(layers);
		findBounds();
	}

	void findBounds(){
		minX = Integer.MAX_VALUE;
		maxX = Integer.MIN_VALUE;
		minY = Float.POSITIVE_INFINITY;
		maxY = Float.NEGATIVE_INFINITY;
		for (int i = 0; i < numLayers; ++i) {
			minX = min(minX, layers[i].onset);
			maxX = max(maxX, layers[i].end);
			for (int j = 0; j < layerSize; ++j) {
				minY = min(minY, layers[i].yTop[j]);
				maxY = max(maxY, layers[i].yBottom[j]);
			}
		}
		minX = Math.max(minX, 0);
		
		dX = maxX - minX;
		dY = maxY - minY;
	}
	
	void draw(Graphics2D g, int width, int height) {
		
		// paint each layer of the graph as a polygon
		for (int i = 0; i < numLayers; ++i) {
			Layer layer = layers[i];
			int begin = max(0, layer.onset - 1);
			int end = min(layerSize - 1, layer.end);

			g.setColor(new Color(layer.rgb));

			int nPoints = 2*(end-begin+1);
			if(nPoints <= 0) continue; //layers w/o data (or just 1 value)
			int[] x = new int[nPoints];
			int[] y = new int[nPoints];
			
			// draw top edge, left to right#
			for (int j=begin, c=0; j <= end; ++j, ++c) {
				x[c] = scaleX(j, width);
				y[c] = scaleY(layer.yTop[j], height);
			}

			// draw bottom edge, right to left
			for (int j=end, c=(end-begin+1); j >= begin; --j, ++c) {
				x[c] = scaleX(j, width);
				y[c] = scaleY(layer.yBottom[j], height);
			}

			g.fillPolygon(x, y, nPoints);
		}
	}
	
	int scaleX(int x, int width){
		return Math.round(((float)width/dX)*x);
	}
	
	int scaleY(float y, int height){
		return Math.round(((height/dY)*(y-minY)));
	}
	
	private void interpolate(Layer[] in){
		for (int i = 0; i < in.length; i++) {
			float[] y = interpolate(in[i].size);
			noNegatives(y);
			in[i] = new Layer(in[i].name, y);
		}
	}

	private float[] interpolate(float[] in) {
		int n = in.length;
		double x[] = new double[n];
		double y[] = new double[n];
		for (int i = 0; i < n; i++) {
			x[i] = i;
			y[i] = in[i];
		}

		UnivariateFunction f = new SplineInterpolator().interpolate(x, y);
		float[] out = new float[(n - 1) * SMOOTHNESS];
		for (int i = 0; i < n - 1; i++) {
			for (int j = 0; j < SMOOTHNESS; j++) {
				double interpX = x[i] + (double) j / SMOOTHNESS;
				double interpY = f.value(interpX);

				// int newX = (int) (interpX * SMOOTHNESS);
				float newY = (float) interpY * SMOOTHNESS;
				out[i * SMOOTHNESS + j] = newY;
			}
		}
		// int newX = (int) x[n - 1] * SMOOTHNESS;
		out[(n - 1) * SMOOTHNESS - 1] = (float) y[n - 1];

		return out;
	}
	
	private void noNegatives(float[] array){
		for (int i = 0; i < array.length; i++) {
			array[i] = Math.max(array[i], 0);
		}
	}
	
	private void addBackgroundNoise(Layer[] layers){
		for (int i = 0; i < layers.length; i++) {
			float[] layer = layers[i].size;
			for (int j = 0; j < layer.length; j++) {
				layer[j]++;
			}
		}
	}

	public Image render(int width, int height){
		BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		draw((Graphics2D)bi.getGraphics(), bi.getWidth(), bi.getHeight());
		return bi;
	}
	
}
