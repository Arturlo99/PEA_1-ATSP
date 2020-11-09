import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

	@SuppressWarnings("resource")
	public static void main(String[] args) throws FileNotFoundException {
		// Graph.generateRandomFullGraph(new Graph(4), 5);
		int selection;
		Graph graph = null;
		final int maxSalesmanDistance = 300;
		do {
			System.out.println("1. Wczytanie danych z pliku");
			System.out.println("2. Wygenerowanie danych losowych");
			System.out.println("3. Wyœwietlenie ostatnio wczytanych lub wygenerowanych danych");
			System.out.println("4. Przegl¹d zupe³ny");
			System.out.println("5. Podzial i ograniczenia");
			// System.out.println("6. Przeglad zupelny");
			// System.out.println("7. Automatyczne pomiary (tabu search)");
			// System.out.println("8. Wczytaj dane z pliku TSPLIB FULL_MATRIX");
			// System.out.println("9. Wczytaj dane z pliku TSPLIB EUC_2D");
			System.out.println("Aby zakonczyc - 0");
			System.out.println("Wprowadz liczbê: ");
			Scanner sc = new Scanner(System.in);
			selection = sc.nextInt();
			switch (selection) {
			case 1: {
				System.out.println("Wprowadz nazwê pliku: ");
				sc = new Scanner(System.in);
				File file = new File(sc.nextLine());
				Scanner scanner = new Scanner(file);
				int firstLine = scanner.nextInt();
				graph = new ArrayGraph(firstLine);
				for (int i = 0; i < firstLine; i++) {
					for (int j = 0; j < firstLine; j++) {
						graph.addEdge(i, j, scanner.nextInt());
					}
				}

			}
				break;

			case 2: {
				int vertex;
				System.out.println("Liczba miast: ");
				vertex = sc.nextInt();

				graph = new ArrayGraph(vertex);

				Graph.generateRandomFullGraph(graph, maxSalesmanDistance);
			}
				break;
			case 3: {
				if (graph != null)
					graph.displayGraph();
				else
					System.out.println("Brak danych do wyœwietlenia");
			}
				break;
			case 4: {
				if (graph != null) {
					// pomiar czasu w ms
					long millisActualTime = System.currentTimeMillis();
					ArrayList<Integer> route = Graph.ATSPBruteForce(graph);
					long executionTime = System.currentTimeMillis() - millisActualTime;

					// Wyswietlenie trasy
					int distFromStart = 0;
					int length = 0;
					System.out.println(route.get(0) + "  " + length + "  " + distFromStart);
					for (int i = 1; i < route.size(); i++) {
						length = graph.getWeight(route.get(i - 1), route.get(i));
						distFromStart += length;

						System.out.println(route.get(i) + "  " + length + "  " + distFromStart);
					}

					System.out.println("Dlugosc trasy: " + distFromStart);

					System.out.println("Czas wykonania algorytmu [ms]: " + executionTime);
				} else
					System.out.println(" Brak zaladowanych danych ");
			}
				break;
			case 5: {
				if (graph != null) {
					long millisActualTime = System.currentTimeMillis();
					ArrayList<Integer> route = Graph.ATSPBranchAndBound(graph);
					long executionTime = System.currentTimeMillis() - millisActualTime;

					// Wyswietlenie trasy
					int distFromStart = 0;
					int length = 0;
					System.out.println(route.get(0) + "  " + length + "  " + distFromStart);
					for (int i = 1; i < route.size(); i++) {
						length = graph.getWeight(route.get(i - 1), route.get(i));
						distFromStart += length;

						System.out.println(route.get(i) + "  " + length + "  " + distFromStart);
					}

					System.out.println("Dlugosc trasy: " + distFromStart);

					System.out.println("Czas wykonania algorytmu [ms]: " + executionTime);
				} else
					System.out.println(" Brak zaladowanych danych ");
			}
				break;
			case 0: {
			}
				break;
			default: {
				System.out.println("Nieprawidlowy wybor");
			}
			}
		} while (selection != 0);

	}

}
