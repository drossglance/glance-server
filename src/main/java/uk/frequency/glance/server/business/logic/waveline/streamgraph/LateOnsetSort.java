package uk.frequency.glance.server.business.logic.waveline.streamgraph;
import java.util.*;

/**
 * LateOnsetSort
 * Sorts by onset, and orders to the outsides of the graph.
 *
 * This is the sort technique preferred when using late-onset data, which the
 * Streamgraph technique is best suited to represent
 *
 * @author Lee Byron
 * @author Martin Wattenberg
 */
public class LateOnsetSort extends LayerSort {

  public String getName() {
    return "Late Onset Sorting, Evenly Weighted";
  }

  @SuppressWarnings("unchecked")
public Layer[] sort(Layer[] layers) {
    // first sort by onset
    Arrays.sort(layers, new OnsetComparator(true));

    return orderToOutside(layers);
  }

}
