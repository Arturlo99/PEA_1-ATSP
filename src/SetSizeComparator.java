import java.util.Comparator;
import java.util.Set;

// Klasa do porównywania zbiorów obiektów typu Integer
public class SetSizeComparator implements Comparator<Set<Integer>>{
        @Override
        public int compare(Set<Integer> o1, Set<Integer> o2) {
            return o1.size() - o2.size();
        }
}