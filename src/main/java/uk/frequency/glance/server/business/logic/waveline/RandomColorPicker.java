package uk.frequency.glance.server.business.logic.waveline;

import java.awt.Color;

import uk.frequency.glance.server.business.logic.waveline.streamgraph.ColorPicker;
import uk.frequency.glance.server.business.logic.waveline.streamgraph.Layer;

/**
 * @author Victor
 */
public class RandomColorPicker implements ColorPicker {

	@Override
	public String getName() {
		return "Random Colors";
	}

	@Override
	public void colorize(Layer[] layers) {
		for (int i = 0; i < layers.length; i++) {
			float h = lerp(0.0f, 1f, (float) Math.random());
			float s = lerp(0.6f, 0.7f, (float) Math.random());
			float b = lerp(0.5f, 0.95f, (float) Math.random());

			layers[i].rgb = Color.HSBtoRGB(h, s, b);
		}
	}

	static float lerp(float v0, float v1, float t) {
		return v0 + (v1 - v0) * t;
	}

}
