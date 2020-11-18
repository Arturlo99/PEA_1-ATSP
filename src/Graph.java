
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Set;

public abstract class Graph {
	protected int vertexAmount;
	private static int INFINITY = 100000000;

	public int getVertexAmount() {
		return vertexAmount;
	}

	public void setVertexAmount(int vertexAmount) {
		this.vertexAmount = vertexAmount;
	}

	static void generateRandomFullGraph(ArrayGraph graph, int maxWeight) {
		Random random = new Random();
		for (int i = 0; i < graph.vertexAmount; i++) {
			for (int j = 0; j < graph.vertexAmount; j++) {
				if (i != j) {
					// Bez warunku na krawedzie juz wygenerowane...
					// ...z tym radzi sobie juz metoda addEdge
					int randomWeight = ((random.nextInt() % maxWeight) + maxWeight + 1) % maxWeight;
					graph.addEdge(i, j, randomWeight);
				}
			}
		}

	}


	static ArrayList<Integer> ATSPBruteForce(ArrayGraph graph) {

		int[] vertexArray = new int[graph.vertexAmount - 1];

		// Utworzenie "spisu wierzcho³ków dodaj¹c je kolejno do tablicy
		for (int i = 0; i < graph.vertexAmount - 1; i++) {
			vertexArray[i] = i + 1;
		}

		ArrayList<Integer> minCombination = new ArrayList<Integer>();
		int minRoute = -1;

		do {
			ArrayList<Integer> combination = new ArrayList<Integer>();

			// Dodanie wierzcholka startowego do listy
			combination.add(0);

			// dodanie reszty wierzcho³ków
			for (int i = 0; i < vertexArray.length; i++) {
				combination.add(vertexArray[i]);
			}

			// Powrot do wierzcholka startowego
			combination.add(0);

			int route = 0;
			for (int i = 1; i < combination.size(); i++) {
				route += graph.getWeight(combination.get(i - 1).intValue(), combination.get(i).intValue());
			}

			if (minRoute == -1 || route < minRoute) {
				minRoute = route;
				minCombination = combination;
			}

		} while (NextPermutation.findNextPermutation(vertexArray));

		return minCombination;
	}

	static ArrayList<Integer> ATSPBranchAndBound(ArrayGraph graph) {
		PriorityQueue<ArrayList<Integer>> priorityQueue = new PriorityQueue<ArrayList<Integer>>(new ArrayListComparator());;
		
		//W tej liœcie bêdzie zapisywane optymalne w danej chwili rozwi¹zanie
		ArrayList<Integer> optimalRoute = new ArrayList<Integer>(); 
		int optimalRouteLength = INFINITY; // optymalna d³ugoœc tras - na starcie jest to nieskoñczonoœæ

		// Pierwszy element listy to granica
		// liczba bêd¹ca ograniczeniem wartoœci rozwi¹zania jakie mo¿na uzyskaæ
		//dziêki rozwiniêciu (przejrzeniu potomków) danego wêz³a
		
		// Kolejne to wierzcholki trasy

		ArrayList<Integer> currentRoute = new ArrayList<Integer>(); // Niejawne tworzenie drzewa, tu bedzie korzen
		currentRoute.add(0); // Poczatkowe oszacowanie nie ma znaczenia
		currentRoute.add(0); // Wierzcholek startowy (korzen drzewa rozwiazan)
		priorityQueue.add(currentRoute); // Dodanie do kolejki korzenia
		
		//deklaracja nastêpnej trasy
		ArrayList<Integer> nextRoute;

		while (!priorityQueue.isEmpty()) {
			
			// Przypisanie korzenia oraz jego usuniêcie z kolejki
			currentRoute = priorityQueue.poll();
	
			// Sprawdzenie, czy rozwiazanie jest warte rozwijania, czy odrzucic
			if (optimalRouteLength == INFINITY || currentRoute.get(0) < optimalRouteLength) {
				for (int i = 0; i < graph.vertexAmount; i++) {
					// Petla wykonywana dla kazdego potomka rozpatrywanego wlasnie rozwiazania w
					// drzewie
					// Ustalenie, czy dany wierzcholek mozna jeszcze wykorzystac, czy juz zostal
					// uzyty
					boolean vertexUsed = false;
					for (int j = 1; j < currentRoute.size(); j++) {
						if (currentRoute.get(j) == i) {
							vertexUsed = true;
							break;
						}
					}
					if (vertexUsed)
						continue;

					// Utworzenie nowego wezla reprezuntujacego rozpatrywane rozwiazanie...
					nextRoute = new ArrayList<Integer>(currentRoute);
					nextRoute.add(i);

					// Dalej bedziemy postepowac roznie...
					if (nextRoute.size() > graph.vertexAmount) {
						// Dotarliœmy do liœcia
						// Dodajemy drogê do wierzcho³ka startowego
						nextRoute.add(0);

						nextRoute.set(0, 0);

						for (int j = 1; j < nextRoute.size() - 1; j++) {
							// Liczymy dystans od poczatku do konca

							nextRoute.set(0,
									nextRoute.get(0) + graph.getWeight(nextRoute.get(j), nextRoute.get(j + 1)));
						}
						if (optimalRouteLength == INFINITY || nextRoute.get(0) < optimalRouteLength) {
							optimalRouteLength = nextRoute.get(0);
							nextRoute.remove(0);
							optimalRoute = nextRoute;
						}
					} else {
						// Liczenie tego, co juz wiemy, od nowa...
						// (dystans od poczatku)
						nextRoute.set(0, 0);
						for (int j = 1; j < nextRoute.size() - 1; j++) {
							nextRoute.set(0,
									nextRoute.get(0) + graph.getWeight(nextRoute.get(j), nextRoute.get(j + 1)));
						}

						// Reszte szacujemy...
						// Pomijamy od razu wierzcholek startowy
						for (int j = 1; j < graph.vertexAmount; j++) {
							// Odrzucenie wierzcholkow juz umieszczonych na trasie
							vertexUsed = false;
							for (int k = 1; k < currentRoute.size(); k++) {
								if (j == currentRoute.get(k)) {
									vertexUsed = true;
									break;
								}
							}
							if (vertexUsed)
								continue;

							int minEdge = -1;
							for (int k = 0; k < graph.vertexAmount; k++) {
								// Odrzucenie krawedzi do wierzcholka 0 przy ostatnim wierzcholku w czesciowym
								// rozwiazaniu
								// Wyjatkiem jest ostatnia mozliwa krawedz
								if (j == i && k == 0)
									continue;

								// Odrzucenie krawedzi do wierzcholka umieszczonego juz na rozwazanej trasie
								vertexUsed = false;
								for (int l = 2; l < nextRoute.size(); l++) {
									if (k == nextRoute.get(l)) {
										vertexUsed = true;
										break;
									}
								}
								if (vertexUsed)
									continue;

								// Odrzucenie samego siebie
								if (k == j)
									continue;

								// Znalezienie najkrotszej mozliwej jeszcze do uzycia krawedzi
								int consideredLength = graph.getWeight(j, k);

								if (minEdge == -1)
									minEdge = consideredLength;
								else if (minEdge > consideredLength)
									minEdge = consideredLength;
							}
							nextRoute.set(0, nextRoute.get(0) + minEdge);
						}

						// 
						if (optimalRouteLength == INFINITY || nextRoute.get(0) < optimalRouteLength) {
							priorityQueue.add(nextRoute);
						}
					}
				}
			} else {
				// Jezeli jedno rozwiazanie odrzucilismy, to wszystkie inne tez mozemy
				// (kolejka priorytetowa, inne nie moga byc lepsze)
				break;
			}
		}

		return optimalRoute;

	}

	
	// Algorytm programowania dynamicznego Helda Karpa
	static int ATSPDynamicProgramming(ArrayGraph graph) {

		//Przechowywanie poœrednich wartoœci w Hash mapach
		
		Map<Index, Integer> minCostDPMap = new HashMap<>();
		
		// tu przechowujemy wierzcho³ek rodzica dla rozpatrywanego wierzcho³ka
		Map<Index, Integer> parent = new HashMap<>(); 

		// Lista przechowuj¹ca wszystkie podzbiory wierzcho³ków z pominiêciem wierzcho³ka startowego
		List<Set<Integer>> allSets = TravelingSalesmanHeldKarp.generateCombinations(graph.vertexAmount - 1);

		for (Set<Integer> set : allSets) {
			//Iteracja po wierzcho³kach grafu
			for (int currentVertex = 1; currentVertex < graph.vertexAmount; currentVertex++) {
				if (set.contains(currentVertex)) {
					continue;
				}
				Index index = Index.createIndex(currentVertex, set);
				int minCost = INFINITY;
				int minPrevVertex = 0;
				// Aby unikn¹æ ConcurrentModificationException kopiujemy zbiór podczas iteracji
				Set<Integer> copySet = new HashSet<>(set);
				//ustalenie z którego wierzcho³ka(minPrevVertex) do currentVertex droga bêdzie najkrótsza
				for (int prevVertex : set) {
					int cost = graph.getWeight(prevVertex, currentVertex)
							+ TravelingSalesmanHeldKarp.getCost(copySet, prevVertex, minCostDPMap);
					if (cost < minCost) {
						minCost = cost;
						minPrevVertex = prevVertex;
					}
				}
				// Gdy podzbiór jest pusty
				if (set.size() == 0) {
					minCost = graph.getWeight(0, currentVertex);
				}
				minCostDPMap.put(index, minCost);
				parent.put(index, minPrevVertex);
			}
		}

		Set<Integer> set = new HashSet<>();
		for (int i = 1; i < graph.vertexAmount; i++) {
			set.add(i);
		}
		int min = INFINITY;
		int prevVertex = -1;
		// Aby unikn¹æ ConcurrentModificationException kopiujemy zbiór podczas iteracji
		Set<Integer> copySet = new HashSet<>(set);
		// koñcowe obliczanie minimalnego kosztu trasy
		for (int k : set) {
			int cost = graph.getWeight(k, 0) + TravelingSalesmanHeldKarp.getCost(copySet, k, minCostDPMap);
			if (cost < min) {
				min = cost;
				prevVertex = k;
			}
		}

		parent.put(Index.createIndex(0, set), prevVertex);
		TravelingSalesmanHeldKarp.printRoute(parent, graph.vertexAmount);
		return min;
	}

}
