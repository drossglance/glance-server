package uk.frequency.glance.server.business.logic.waveline.streamgraph;
/**
 * ColorPicker
 * Interface for new coloring algorithms.
 *
 * @author Lee Byron
 * @author Martin Wattenberg
 */
public interface ColorPicker {

  public void colorize(Layer[] layers);

  public String getName();

}
