import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Random;

public abstract class Graph implements GraphI, Comparable<T> {
	protected int vertexAmount;

	public int getVertexAmount() {
		return vertexAmount;
	}

	public void setVertexAmount(int vertexAmount) {
		this.vertexAmount = vertexAmount;
	}

	static void generateRandomFullGraph(Graph graph, int maxWeight) {
		Random random = new Random();
		for (int i = 0; i < graph.vertexAmount; i++) {
			for (int j = 0; j < graph.vertexAmount; j++) {
				if (i != j) {
					// Bez warunku na krawedzie juz wygenerowane...
					// ...z tym radzi sobie juz metoda addEdge
					int randomWeight = ((random.nextInt() %maxWeight) + maxWeight + 1 )%maxWeight;
					graph.addEdge(i, j, randomWeight);
				}
			}
		}

	}

	// Algorytm przegl¹dania zupe³nego (Brute Force)
	static ArrayList<Integer> ATSPBruteForce(Graph graph) {

		int[] vertexArray = new int[graph.vertexAmount - 1];

		// Utworzenie "spisu wierzcho³ków dodaj¹c je kolejno do tablicy
		for (int i = 0; i < graph.vertexAmount - 1; i++) {
			vertexArray[i] = i+1;
		}

		ArrayList<Integer> minCombination = new ArrayList<Integer>();
		int minRoute = -1;

		do {
			ArrayList<Integer> combination = new ArrayList<Integer>();

			// Dodanie wierzcholka startowego i pierwszego na trasie
			combination.add(0);
			combination.add(vertexArray[0]);

			// dodanie reszty wierzcho³ków
			for (int i = 1; i < vertexArray.length; i++) {
				combination.add(vertexArray[i]);
			}

			// Powrot do wierzcholka startowego
			combination.add(0);

			int route = 0;
			for (int i = 1; i < combination.size(); i++) {
				route += graph.getWeight(combination.get(i-1).intValue(), combination.get(i).intValue());
			}

			if (minRoute == -1 || route < minRoute) {
				minRoute = route;
				minCombination = combination;
			}

		} while (nextPermutation.findNextPermutation(vertexArray));

		return minCombination;
	}
	
	
	// Algorytm podzia³u i ograniczeñ wykorzystuj¹cy kolejke priorytetowa i niejawnie utworzone drzewo
	static ArrayList<Integer> ATSPBranchAndBound(Graph graph){
		
		PriorityQueue<ArrayList<Integer>> routeQueue = new PriorityQueue<ArrayList<Integer>>();
	    ArrayList<Integer> optimalRoute = new ArrayList<Integer>();     // Tu bedziemy zapisywac optymalne (w danej chwili) rozwiazanie
	    int optimalRouteLength = -1;            // zamiast nieskonczonosci

	    // Kolejne to wierzcholki na trasie Pierwszy element listy to dlugosc trasy
	    //
	    ArrayList<Integer> currentRoute = new ArrayList<Integer>();     // Niejawne tworzenie drzewa, tu bedzie korzen
	    currentRoute.add(0);              // Poczatkowe oszacowanie nie ma znaczenia
	    currentRoute.add(0);              // Wierzcholek startowy (korzen drzewa rozwiazan)
	    routeQueue.add(currentRoute);          // Dodanie do kolejki korzenia

	    while(!routeQueue.isEmpty())
	    {
	        // Przypisanie korzenia oraz jego usuniêcie z kolejki
	        currentRoute = routeQueue.poll();

	        // Sprawdzenie, czy rozwiazanie jest warte rozwijania, czy odrzucic
	        if(optimalRouteLength == -1 || currentRoute.get(0) < optimalRouteLength)
	        {
	            for(int i = 0; i < graph.vertexAmount; i++)
	            {
	                // Petla wykonywana dla kazdego potomka rozpatrywanego wlasnie rozwiazania w drzewie
	                // Ustalenie, czy dany wierzcholek mozna jeszcze wykorzystac, czy juz zostal uzyty
	                boolean vertexUsed = false;
	                for(int j = 1; j < currentRoute.size(); j++)
	                {
	                    if(currentRoute.get(j) == i)
	                    {
	                        vertexUsed = true;
	                        break;
	                    }
	                }
	                if(vertexUsed)
	                    continue;

	                // Niejawne utworzenie nowego wezla reprezuntujacego rozpatrywane rozwiazanie...
	                ArrayList<Integer> nextRoute = currentRoute;
	                nextRoute.add(i);

	                // Dalej bedziemy postepowac roznie...
	                if(nextRoute.size() > graph.vertexAmount)
	                {
	                    // Doszlismy wlasnie do liscia
	                    // Dodajemy droge powrotna, nie musimy nic szacowac
	                    // (wszystko juz wiemy)
	                    nextRoute.add(0);

	                    nextRoute.set(0, 0);

	                    for(int j = 1; j < nextRoute.size() - 1; j++)
	                    {
	                        // Liczymy dystans od poczatku do konca

	                        nextRoute.set(0, nextRoute.get(0) + graph.getWeight(nextRoute.get(j), nextRoute.get(j + 1)));
	                    }
	                    if(optimalRouteLength == -1 || nextRoute.get(0) < optimalRouteLength)
	                    {
	                        optimalRouteLength = nextRoute.get(0);
	                        nextRoute.remove(nextRoute.get(0));
	                        optimalRoute = nextRoute;
	                    }
	                }
	                else
	                {
	                    // Liczenie tego, co juz wiemy, od nowa...
	                    // (dystans od poczatku)
	                    nextRoute.set(0, 0);
	                    for(int j = 1; j < nextRoute.size() - 1; j++)
	                    {
	                    	
	                        nextRoute.set(0, nextRoute.get(0) + graph.getWeight(nextRoute.get(j), nextRoute.get(j + 1)));
	                    }

	                    // Reszte szacujemy...
	                    // Pomijamy od razu wierzcholek startowy
	                    for(int j = 1; j < graph.vertexAmount; j++)
	                    {
	                        // Odrzucenie wierzcholkow juz umieszczonych na trasie
	                        vertexUsed = false;
	                        for(int k = 1; k < currentRoute.size(); k++)
	                        {
	                            if(j == currentRoute.get(k))
	                            {
	                                vertexUsed = true;
	                                break;
	                            }
	                        }
	                        if(vertexUsed)
	                            continue;

	                        int minEdge = -1;
	                        for(int k = 0; k < graph.vertexAmount; k++)
	                        {
	                            // Odrzucenie krawedzi do wierzcholka 0 przy ostatnim wierzcholku w czesciowym rozwiazaniu
	                            // Wyjatkiem jest ostatnia mozliwa krawedz
	                            if(j == i && k == 0)
	                                continue;

	                            // Odrzucenie krawedzi do wierzcholka umieszczonego juz na rozwazanej trasie
	                            vertexUsed = false;
	                            for(int l = 2; l < nextRoute.size(); l++)
	                            {
	                                if(k == nextRoute.get(l))
	                                {
	                                    vertexUsed = true;
	                                    break;
	                                }
	                            }
	                            if(vertexUsed)
	                                continue;

	                            // Odrzucenie samego siebie
	                            if(k == j)
	                                continue;

	                            // Znalezienie najkrotszej mozliwej jeszcze do uzycia krawedzi
	                            int consideredLength = graph.getWeight(j, k);

	                            if(minEdge == -1)
	                                minEdge = consideredLength;
	                            else if(minEdge > consideredLength)
	                                minEdge = consideredLength;
	                        }
	                        nextRoute.set(0, nextRoute.get(0) + minEdge);
	                    }

	                    // ...i teraz zastanawiamy sie co dalej
	                    if(optimalRouteLength == -1 || nextRoute.get(0) < optimalRouteLength)
	                    {
	                        routeQueue.add(nextRoute);
	                    }
	                }
	            }
	        }
	        else
	        {
	            // Jezeli jedno rozwiazanie odrzucilismy, to wszystkie inne tez mozemy
	            // (kolejka priorytetowa, inne nie moga byc lepsze)
	            break;
	        }
	    }

	    return optimalRoute;
	}
	
	
	
	
	

	@Override
	public boolean addEdge(int v, int w, int weight) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeEdge(int v, int w) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getWeight(int v, int w) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void displayGraph() {
		// TODO Auto-generated method stub

	}

}
