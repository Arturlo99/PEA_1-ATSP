import java.util.ArrayList;
import java.util.Comparator;

//Klasa do porównywania List przechowuj¹cych obiekty typu Integer
public class ArrayListComparator implements Comparator<ArrayList<Integer>> {

	@Override
	public int compare(ArrayList<Integer> o1, ArrayList<Integer> o2) {
		if (o1.get(0).intValue() > o2.get(0).intValue())
			return 1;
		if (o1.get(0).intValue() < o2.get(0).intValue()) 
			return -1;
		else
			return 0;
	}

}
