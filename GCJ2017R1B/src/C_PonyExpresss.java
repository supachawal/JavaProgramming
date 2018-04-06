import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.TreeMap;

public class C_PonyExpresss {
	public static void main(String[] args) {
//		CommonUtils.preamble();

		String inputFileName, outputFileName;
		if (args.length > 0) {
			inputFileName = args[0];
		} else {
//			 inputFileName = "-sample1.in";
//			 inputFileName = "-sample2.in";
//			 inputFileName = "-small-practice.in";
			// inputFileName = "-small-attempt0.in";
			inputFileName = "-large-practice.in";
			// inputFileName = "-large.in";
			inputFileName = C_PonyExpresss.class.getSimpleName().substring(0, 1) + inputFileName;
		}

		if (args.length > 1) {
			outputFileName = args[1];
		} else {
			outputFileName = inputFileName.split("[.]in")[0].concat(".out");
		}

		System.out.printf("============ START %s --> %s ===========\n", inputFileName, outputFileName);

		File inputFile = new File(inputFileName);
		File outputFile = new File(outputFileName);

		try {
			PrintWriter outputWriter = new PrintWriter(outputFile, "ISO-8859-1");
			C_PonyExpresss solver = new C_PonyExpresss();
			solver.solve(inputFile, outputWriter);
			outputWriter.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.printf("============ END ============\n");
//		CommonUtils.postamble();
	}

	public void solve(final File aFile, PrintWriter w) throws FileNotFoundException, IOException {
		String textLine;
		BufferedReader br;
		int testCaseNumber = 0, testCaseCount;

		br = new BufferedReader(new FileReader(aFile));
		textLine = br.readLine();
		testCaseCount = Integer.parseInt(textLine);
		while (++testCaseNumber <= testCaseCount && (textLine = br.readLine()) != null) {
			String[] splitted = textLine.split("\\s+");
			int N = Integer.parseInt(splitted[0]);
			int Q = Integer.parseInt(splitted[1]);
			
			System.out.printf("Case #%d: N=%d, Q=%d\n", testCaseNumber, N, Q);
			w.printf("Case #%d:", testCaseNumber);
			
			City[] cities = new City[N];
			StringBuilder answer = new StringBuilder();
			int i, j;
			
			for (i = 0; i < N; i++) {
				textLine = br.readLine();
				splitted = textLine.split("\\s+");
				cities[i] = new City(i, Integer.parseInt(splitted[0]), Integer.parseInt(splitted[1]));
			}
			
			for (i = 0; i < N; i++) {
				textLine = br.readLine();
				splitted = textLine.split("\\s+");
				for (j = 0; j < N; j++) {
					if (i != j) {
						int distance = Integer.parseInt(splitted[j]);
						if (i != j && distance >= 0) {
							ArrayList<City> neighborList = cities[i].neighborMap.get(distance);
							if (neighborList == null) {
								neighborList = new ArrayList<City>();
								cities[i].neighborMap.put(distance, neighborList);
							}
							neighborList.add(cities[j]);
						}
					}
				}
			}
			
//			if (testCaseNumber == 53) {
//				System.err.printf("Breaked Test\n");
//			}

			for (int k = 0; k < Q; k++) {
				if (k > 0) {
					for (City city : cities) {
						city.reset();
					}
				}
				textLine = br.readLine();
				splitted = textLine.split("\\s+");
				int u = Integer.parseInt(splitted[0]) - 1;
				int v = Integer.parseInt(splitted[1]) - 1;
				double hoursToReachDest = earliestArrivalTime(cities[u], cities[v]);
				answer.append(removeTrailingZeros(String.format(" %.9f", hoursToReachDest)));
			}

			System.out.printf("=> answer=%s\n", answer);
			w.printf("%s\n", answer);

			////////////////////////////////////////////////////////////////////////////////////
		}

		br.close();
	}

	private static String removeTrailingZeros(String numStr) {
		int nAbsurdZeros = 0;
		int posPoint = numStr.indexOf('.') + 2;
		for (int i = numStr.length() - 1; i >= posPoint && numStr.charAt(i) == '0'; i--) {
			nAbsurdZeros++;	
		}
		
		return numStr.substring(0, numStr.length() - nAbsurdZeros);
	}
	
	private static double earliestArrivalTime(City from, City to) {
		ArrayDeque<City> Q = new ArrayDeque<City>();
		from.minArrivalTime = 0.0;
		from.mainHorse.arrivalTime = 0.0;
		Q.add(from);
		
		while (!Q.isEmpty()) {
			City p = Q.remove();
			if (p != to) {
				p.floodHorses(Q);
			}
		}
		return to.minArrivalTime;
	}

	private static class Horse {
		public int homeCityIndex;
		public int endurance;
		public int speed;
		public double arrivalTime;
		public boolean alreadyCheckedOut;

		public Horse(int homeCityIndex, int endurance, int speed, double arrivalTime) {
			this.homeCityIndex = homeCityIndex;
			this.endurance = endurance;
			this.speed = speed;
			this.arrivalTime = arrivalTime;
			this.alreadyCheckedOut = false;
		}

		@Override
		public String toString() {
			if (alreadyCheckedOut) {
				return String.format("{homeCity:%d, arrivalTime:%s, checkedOut:true}", homeCityIndex, arrivalTime);
			}
			return String.format("{homeCity:%d, endurance:%d, speed:%d, arrivalTime:%s}"
					, homeCityIndex, endurance, speed, arrivalTime);
		}
	}
	
	private static class City {
		private int cityIndex;
		public double minArrivalTime;
		Horse mainHorse;
		public HashMap<Integer, Horse> horseMap;
		public TreeMap<Integer, ArrayList<City>> neighborMap = new TreeMap<Integer, ArrayList<City>>();

		@Override
		public String toString() {
			StringBuilder neighborsStr = new StringBuilder();
			int distanceIndex = 0;
			neighborsStr.append('[');
			for (Entry<Integer, ArrayList<City>> neighborList : neighborMap.entrySet()) {
				if (distanceIndex++ > 0) {
					neighborsStr.append(", ");
				}
				int distance = neighborList.getKey().intValue();
				neighborsStr.append("{ distance:").append(distance).append(", cities:[");
				int neighborIndex = 0;
				for (City neighborCity : neighborList.getValue()) {
					if (neighborIndex++ > 0) {
						neighborsStr.append(',');
					}
					neighborsStr.append(neighborCity.cityIndex);
				}
				
				neighborsStr.append("]}");
			}
			neighborsStr.append(']');

			return String.format("{ index:%d, minArrivalTime:%s,\n   horses:%s,\n   neighbors:%s }\n"
					, cityIndex, minArrivalTime, horseMap == null ? "null" : horseMap.values(), neighborsStr);
		}

		public City(int cityIndex, int horseEndurance, int horseSpeed) {
			this.cityIndex = cityIndex;
			mainHorse = new Horse(cityIndex, horseEndurance, horseSpeed, Double.POSITIVE_INFINITY);
			reset();
		}

		public void reset() {
			minArrivalTime = Double.POSITIVE_INFINITY;
			mainHorse.arrivalTime = Double.POSITIVE_INFINITY;
			mainHorse.alreadyCheckedOut = false;
			horseMap = new HashMap<Integer, Horse>();
			horseMap.put(cityIndex, mainHorse);
		}

		public void floodHorses(final ArrayDeque<City> Q) {
			for (Entry<Integer, ArrayList<City>> neighborList : neighborMap.entrySet()) {
				int distance = neighborList.getKey().intValue();

				for (City neighborCity : neighborList.getValue()) {
					boolean isFlooded = false;
					
					for (Horse horse : horseMap.values()) {
						if (!horse.alreadyCheckedOut) {
							int hc = horse.homeCityIndex;
							int balanceEndurance = horse.endurance - distance;
							double newArrivalTime = horse.arrivalTime + (double)distance / horse.speed;
							
							if (balanceEndurance >= 0) {
								Horse visited = neighborCity.horseMap.get(hc);
								if (visited == null || visited.arrivalTime > newArrivalTime) {
									
									if (visited == null) {
										neighborCity.horseMap.put(hc, new Horse(hc, balanceEndurance, horse.speed, newArrivalTime));
									} else {
										visited.endurance = balanceEndurance;
										visited.arrivalTime = newArrivalTime;
										visited.alreadyCheckedOut = false;
									}
									
									if (neighborCity.minArrivalTime > newArrivalTime) {
										neighborCity.minArrivalTime = newArrivalTime;
										neighborCity.mainHorse.arrivalTime = newArrivalTime;
										neighborCity.mainHorse.alreadyCheckedOut = false;
									}
									isFlooded = true;
								}
							}
						}
					}
					
					if (isFlooded) {
						Q.add(neighborCity);
					}
				}
			}

			for (Horse horse : horseMap.values()) {
				horse.alreadyCheckedOut = true;
			}
		}
	}
}