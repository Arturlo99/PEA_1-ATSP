
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/*
 * Data: 12.11.2020r
 * @author Artur So³tys
 * 
 * Zadanie projektowe nr 1. 
 * Projektowanie Efektywnych Algorytmów
 * Asymetryczny Problem Komiwoja¿era
 */


public class Main {

	// Menu g³ówne programu
	@SuppressWarnings("resource")
	public static void main(String[] args) throws FileNotFoundException {
		int selection;
		ArrayGraph graph = null;
		final int maxSalesmanDistance = 300;
		do {
			System.out.println("1. Wczytanie danych z pliku");
			System.out.println("2. Wygenerowanie danych losowych");
			System.out.println("3. Wyœwietlenie ostatnio wczytanych lub wygenerowanych danych");
			System.out.println("4. Przegl¹d zupe³ny");
			System.out.println("5. Podzial i ograniczenia");
			System.out.println("6. Programowanie dynamiczne");
			System.out.println("7. Pomiary");
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
					long nanosActualTime = System.nanoTime();
					ArrayList<Integer> route = Graph.ATSPBruteForce(graph);
					long executionTime = System.nanoTime() - nanosActualTime;

					// Wyswietlenie trasy
					int distFromStart = 0;
					int length = 0;
					System.out.print(route.get(0) + " -> ");
					for (int i = 1; i < route.size(); i++) {
						length = graph.getWeight(route.get(i - 1), route.get(i));
						distFromStart += length;

						System.out.print(route.get(i));
						if(i!= route.size()-1) {
							System.out.print(" -> ");
						}
					}
					System.out.println();
					System.out.println("Dlugosc trasy: " + distFromStart);

					System.out.println("Czas wykonania algorytmu [ms]: " + executionTime/1000000);
				} else
					System.out.println(" Brak zaladowanych danych ");
			}
				break;
			case 5: {
				if (graph != null) {
					long nanosActualTime = System.nanoTime();
					ArrayList<Integer> route = Graph.ATSPBranchAndBound(graph);
					long executionTime = System.nanoTime() - nanosActualTime;

					// Wyswietlenie trasy
					int distFromStart = 0;
					int length = 0;
					System.out.print(route.get(0) + " -> ");
					for (int i = 1; i < route.size(); i++) {
						length = graph.getWeight(route.get(i - 1), route.get(i));
						distFromStart += length;

						System.out.print(route.get(i));
						if(i!= route.size()-1) {
							System.out.print(" -> ");
						}
					}
					System.out.println();
					System.out.println("Dlugosc trasy: " + distFromStart);

					System.out.println("Czas wykonania algorytmu [ms]: " + executionTime/1000000);
				} else
					System.out.println(" Brak zaladowanych danych ");
			}
				break;
				
			case 6: {
				if(graph != null) {
					long nanosActualTime = System.nanoTime();
					int minCost = Graph.ATSPDynamicProgramming(graph);
					long executionTime = System.nanoTime() - nanosActualTime;
					
					System.out.println("Dlugosc trasy: " + minCost);

					System.out.println("Czas wykonania algorytmu [ms]: " + executionTime/1000000);
				}
			}
				break;
			
			case 7: {

		            int average;
		            int max_iteration = 100; // iloœæ pomiarów dla danego N
		            System.out.println();
		            System.out.println("----- BB -----");
		            for (int nIterator = 9; nIterator < 15; nIterator++) {
		                int sum = 0;
		                for (int actualIterator = 0; actualIterator < max_iteration; actualIterator++) {
		                	ArrayGraph graph2 = new ArrayGraph(nIterator);
		                    Graph.generateRandomFullGraph(graph2, maxSalesmanDistance);
		                    long nanosActualTime = System.nanoTime();
		                    Graph.ATSPBranchAndBound(graph2);
		                    long executionTime = (System.nanoTime() - nanosActualTime)/1000000;
		                    sum += executionTime;
		                    System.out.print(" \r" + (100 / max_iteration)* (actualIterator + 1) + "%");
		                }
		                System.out.println();
		                average = sum / max_iteration;
		                System.out.println("N = " + nIterator + ", sredni czas = " + average + "[ms]" );
	            }
		            
				
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
