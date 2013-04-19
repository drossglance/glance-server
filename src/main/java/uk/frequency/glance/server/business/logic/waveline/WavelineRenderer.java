package uk.frequency.glance.server.business.logic.waveline;

import static java.awt.RenderingHints.KEY_ANTIALIASING;
import static java.awt.RenderingHints.KEY_INTERPOLATION;
import static java.awt.RenderingHints.VALUE_ANTIALIAS_ON;
import static java.awt.RenderingHints.VALUE_INTERPOLATION_BICUBIC;
import static java.lang.Math.max;
import static java.lang.Math.min;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.interpolation.SplineInterpolator;

import uk.frequency.glance.server.business.logic.waveline.streamgraph.ColorPicker;
import uk.frequency.glance.server.business.logic.waveline.streamgraph.Layer;
import uk.frequency.glance.server.business.logic.waveline.streamgraph.LayerLayout;
import uk.frequency.glance.server.business.logic.waveline.streamgraph.LayerSort;
import uk.frequency.glance.server.business.logic.waveline.streamgraph.NoLayerSort;
import uk.frequency.glance.server.business.logic.waveline.streamgraph.StreamLayout;

public class WavelineRenderer {

	private static final int SMOOTHNESS = 20; //how many points are added in-between the actual data
	private static final Color SHADOW_BEGIN = new Color(0, 0, 0, .2f);
	private static final Color SHADOW_END = new Color(0, 0, 0, .05f);
	
	private LayerLayout layout;
	private LayerSort ordering;
	private ColorPicker coloring;
	private final float H_PADDING = .16f;
	
	private Layer[] layers;
	private int numLayers, layerSize;
	private int minX, maxX, dX;
	private float minY, maxY, dY;
	
	public WavelineRenderer(Layer[] layers, ColorPicker coloring){
		this.layers = layers;
		this.ordering = new NoLayerSort();
		this.layout = new StreamLayout();
//		this.layout = new StackLayout();
//		this.layout = new MinimizedWiggleLayout();
//		this.layout = new ThemeRiverLayout();
//		this.coloring = new RandomColorPicker();
		this.coloring = coloring;
		init();
	}
	
	void init(){
		addMinSize(layers, 1f); //TODO should depend on data's scale
		smoothen(layers);
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
		g.setRenderingHint(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON);
		g.setRenderingHint(KEY_INTERPOLATION, VALUE_INTERPOLATION_BICUBIC);
		
		drawShadow(g, width, height, 1.3f, 0, false); //fading upwards
		drawShadow(g, width, height, 1.3f, 50f, true); //fading downwards
		
		// paint each layer of the graph as a polygon
		for (int i = 0; i < numLayers; ++i) {
			Layer layer = layers[i];
			g.setColor(new Color(layer.rgb));
			drawLayer(g, layer, width, height);
		}
	}
	
	void drawLayer(Graphics2D g, Layer layer, int width, int height){
		int begin = max(0, layer.onset - 1);
		int end = min(layerSize - 1, layer.end);
		int nPoints = 2*(end-begin+1);
		if(nPoints <= 0) return; //layers w/o data (or just 1 value)
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
	
	void drawShadow(Graphics2D g, int width, int height, float scale, float yShift, boolean invertGradient) {

		//build shadow layer
		Layer shadow = shadowLayer(scale, yShift*dY/height);
		
		//find shadow bounds for the gradient
		float miny = Float.POSITIVE_INFINITY;
		float maxy = Float.NEGATIVE_INFINITY;
		for (int i = 0; i < shadow.size.length; i++) {
			miny = min(miny, shadow.yTop[i]);
			maxy = max(maxy, shadow.yBottom[i]);
		}
		int begin = scaleY(maxY, height);
		int end = scaleY(minY, height);
		if(invertGradient){
			int aux = begin;
			begin = end;
			end = aux;
		}
		
		//draw
		GradientPaint grad = new GradientPaint(0, begin, SHADOW_BEGIN, 0, end, SHADOW_END);
		g.setPaint(grad);
		drawLayer(g, shadow, width, height);
	}
	
	Layer shadowLayer(float scale, float yShift){
		//get gathered layers' contour
		float[] top = new float[layerSize];
		float[] bottom = new float[layerSize];
		float[] size = new float[layerSize];
		for (int i = 0; i < layerSize; i++) {
			top[i] = Float.POSITIVE_INFINITY;
			bottom[i] = Float.NEGATIVE_INFINITY;
			for (int j = 0; j < numLayers; j++) {
				top[i] = min(top[i], layers[j].yTop[i]);
				bottom[i] = max(bottom[i], layers[j].yBottom[i]);
			}
			size[i] = bottom[i] - top[i];
			
			//scale
			top[i] -= (size[i] / 2) * (scale - 1);
			bottom[i] += (size[i] / 2) * (scale - 1);
			size[i] *= scale;
			
			//displace
			top[i] += yShift;
			bottom[i] += yShift;
		}
		
		Layer shadow = new Layer("shadow", size);
		shadow.yTop = top;
		shadow.yBottom = bottom;
		return shadow;
	}
	
	int scaleX(int x, int width){
		float scaled = ((float) width / dX) * x;
		return Math.round(scaled);
	}
	
	int scaleY(float y, int height){
		float scaled = (height / dY) * (y - minY);
		float padded = scaled  * (1 - 2 * H_PADDING) + H_PADDING * height;
		return Math.round(padded);
	}
	
	private void smoothen(Layer[] in){
		for (int i = 0; i < in.length; i++) {
			float[] y = smoothen(in[i].size);
			noNegatives(y);
			in[i] = new Layer(in[i].name, y);
		}
	}

	private float[] smoothen(float[] in) {
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
	
	private void addMinSize(Layer[] layers, float amount) {
		for (int i = 0; i < layers.length; i++) {
			float[] layer = layers[i].size;
			for (int j = 0; j < layer.length; j++) {
				layer[j] += amount;
			}
		}
	}

	public BufferedImage render(int width, int height){
		BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		draw((Graphics2D)bi.getGraphics(), bi.getWidth(), bi.getHeight());
		return bi;
	}
	
}
