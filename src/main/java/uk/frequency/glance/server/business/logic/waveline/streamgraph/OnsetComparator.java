package uk.frequency.glance.server.business.logic.waveline.streamgraph;
import java.util.*;

/**
 * OnsetSort
 * Compares two Layers based on their onset
 *
 * @author Lee Byron
 * @author Martin Wattenberg
 */
@SuppressWarnings("rawtypes")
public class OnsetComparator implements Comparator {

  public boolean ascending;

  public OnsetComparator(boolean ascending) {
    this.ascending = ascending;
  }

  public int compare(Object p, Object q){
    Layer pL = (Layer)p;
    Layer qL = (Layer)q;
    return (ascending ? 1 : -1) * (pL.onset - qL.onset);
  }

  public boolean equals(Object p, Object q){
    Layer pL = (Layer)p;
    Layer qL = (Layer)q;
    return pL.onset == qL.onset;
  }

}
