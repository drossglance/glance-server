package uk.frequency.glance.server.business.logic.waveline.streamgraph;
import java.util.*;

/**
 * BasicLateOnsetSort
 * Sorts by onset, but does not partition to the outsides of the graph in
 * order to illustrate short-sighted errors found during design process.
 *
 * @author Lee Byron
 * @author Martin Wattenberg
 */
public class BasicLateOnsetSort extends LayerSort {

  public String getName() {
    return "Late Onset Sorting, Top to Bottom";
  }

  @SuppressWarnings("unchecked")
public Layer[] sort(Layer[] layers) {
    // first sort by onset
    Arrays.sort(layers, new OnsetComparator(true));

    return layers;
  }

}
