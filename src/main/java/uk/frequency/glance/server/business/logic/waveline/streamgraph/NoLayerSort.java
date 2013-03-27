package uk.frequency.glance.server.business.logic.waveline.streamgraph;
/**
 * NoLayerSort
 * Does no sorting. Identity function.
 *
 * @author Lee Byron
 * @author Martin Wattenberg
 */
public class NoLayerSort extends LayerSort {

  public String getName() {
    return "No Sorting";
  }

  public Layer[] sort(Layer[] layers) {
    return layers;
  }

}
