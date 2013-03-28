package uk.frequency.glance.server.business.logic.waveline;

import java.awt.Color;

import uk.frequency.glance.server.business.logic.waveline.streamgraph.ColorPicker;
import uk.frequency.glance.server.business.logic.waveline.streamgraph.Layer;

/**
 * @author Victor
 */
public class BasicColorPicker implements ColorPicker {

	Color[] colors;
	
	public BasicColorPicker(Color... colors) {
		this.colors = colors;
	}

	@Override
	public String getName() {
		return "Basic Colors";
	}

	@Override
	public void colorize(Layer[] layers) {
		for (int i = 0; i < layers.length; i++) {
			layers[i].rgb = colors[i%colors.length].getRGB();
		}
	}

	static float lerp(float v0, float v1, float t) {
		return v0 + (v1 - v0) * t;
	}

}
