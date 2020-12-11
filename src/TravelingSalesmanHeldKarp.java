import java.util.*;

/**
 * klasa z metodami do algorytmu Helda-Karpa
 *
 * Z³o¿onoœæ czasowa - O(2^n * n^2) Z³o¿onoœæ przestrzenna - O(2^n)
 *
 * https://en.wikipedia.org/wiki/Held%E2%80%93Karp_algorithm
 */
abstract class TravelingSalesmanHeldKarp {

	static void printRoute(Map<Index, Integer> parent, int totalVertices) {
		Set<Integer> set = new HashSet<>();
		for (int i = 0; i < totalVertices; i++) {
			set.add(i);
		}
		Integer start = 0;
		Deque<Integer> stack = new LinkedList<>();
		while (true) {
			stack.push(start);
			set.remove(start);
			start = parent.get(Index.createIndex(start, set));
			if (start == null) {
				break;
			}
		}
		StringJoiner joiner = new StringJoiner(" -> ");
		stack.forEach(v -> joiner.add(String.valueOf(v)));
		System.out.println(joiner.toString());
	}

	// obliczenie kosztu 
	static int getCost(Set<Integer> set, int prevVertex, Map<Index, Integer> minCostDP) {
		set.remove(prevVertex);
		Index index = Index.createIndex(prevVertex, set);
		int cost = minCostDP.get(index);
		set.add(prevVertex);
		return cost;
	}

	// Generowania wszystkich mo¿liwych zbiorów wierzcho³ków
	static List<Set<Integer>> generateCombinations(int n) {
		int input[] = new int[n];
		//tablica wierzcho³kow bez wierzcho³ka 0
		for (int i = 0; i < input.length; i++) {
			input[i] = i + 1;
		}
		List<Set<Integer>> allSets = new ArrayList<>();
		int result[] = new int[input.length];
		generateCombination(input, 0, 0, allSets, result);
		Collections.sort(allSets, new SetSizeComparator());
		return allSets;
	}

	static void generateCombination(int input[], int start, int position, List<Set<Integer>> allSets, int result[]) {
		if (position == input.length) {
			return;
		}
		Set<Integer> set = createSet(result, position);
		allSets.add(set);
		for (int i = start; i < input.length; i++) {
			result[position] = input[i];
			generateCombination(input, i + 1, position + 1, allSets, result);
		}
	}
	
	//Tworzenie nowego zbioru
	static Set<Integer> createSet(int input[], int position) {
		//zbiór pusty
		if (position == 0) {
			return new HashSet<>();
		}
		
		Set<Integer> set = new HashSet<>();
		for (int i = 0; i < position; i++) {
			set.add(input[i]);
		}
		return set;
	}
}