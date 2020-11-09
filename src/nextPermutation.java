
public class nextPermutation {

	// Funkcja do zamiany warto�ci w tablicy
	public static int[] swap(int data[], int left, int right) {

		int temp = data[left];
		data[left] = data[right];
		data[right] = temp;

		return data;
	}

	// Funkcja do odwracania podtablicy
	// zaczynaj�c od lewej id�c do prawej strony
	public static int[] reverse(int data[], int left, int right) {

		while (left < right) {
			int temp = data[left];
			data[left++] = data[right];
			data[right--] = temp;
		}

		// zwr�cenie odwr�conych danych
		return data;
	}

	// Funkcja znajduj�ca kolejn� permutacj� dla podanej tablicy liczb ca�kowitych
	public static boolean findNextPermutation(int data[]) {

		// Je�li dana tablica jest pusta lub zawiera tylko jeden element
		// znalezienie kolejnej permutacji jest niemo�liwe
		if (data.length <= 1)
			return false;

		int last = data.length - 2;

		while (last >= 0) {
			if (data[last] < data[last + 1]) {
				break;
			}
			last--;
		}

		if (last < 0)
			return false;

		int nextGreater = data.length - 1;

		for (int i = data.length - 1; i > last; i--) {
			if (data[i] > data[last]) {
				nextGreater = i;
				break;
			}
		}

		data = swap(data, nextGreater, last);

		data = reverse(data, last + 1, data.length - 1);

		return true;
	}
}