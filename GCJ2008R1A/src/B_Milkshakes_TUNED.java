import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

public class B_Milkshakes_TUNED {
	public class CustomerFavorite {
		public int flavor;
		public boolean malted;
		public CustomerFavorite(int N, int initFlavor, boolean initMalted) {
			flavor = initFlavor;
			malted = initMalted;
		}
	}
	
	public static void main(String[] args) {
		CommonUtils.preamble();
		String inputFileName, outputFileName;

		if (args.length > 0) {
			inputFileName = args[0];
		} else {
//			inputFileName = "-sample1.in";
//			inputFileName = "-sample2.in";
//			inputFileName = "-small-practice.in";
			inputFileName = "-large-practice.in";
			inputFileName = B_Milkshakes_TUNED.class.getSimpleName().substring(0, 1) + inputFileName;
		}
		
		if (args.length > 1) {
			outputFileName = args[1];
		} else {
			outputFileName = inputFileName.split("[.]in")[0].concat(".out");
		}

		System.out.printf("%s --> %s\n", inputFileName, outputFileName);
		
		File inputFile = new File(inputFileName);
		File outputFile = new File(outputFileName);
		
		try {
			PrintWriter outputWriter = new PrintWriter(outputFile, "ISO-8859-1");
			B_Milkshakes_TUNED solver = new B_Milkshakes_TUNED();
			solver.solve(inputFile, outputWriter);
			outputWriter.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		CommonUtils.postamble();
	}

	private long iterationCounter = 0;

	@SuppressWarnings("unchecked")
	public boolean solve(final File aFile, PrintWriter w) {
		String textLine;
		boolean result = false;
		BufferedReader br;
		int testCaseNumber = 0, testCaseCount;
		String [] splitted;
		String answer;

		boolean header = true;
		int N = 0;	 // # of flavors
		int M = 0;	// # of customers
		int T;
		LinkedList<CustomerFavorite> [] X; //customersLikes;
		
		try {
			br = new BufferedReader(new FileReader(aFile));
			textLine = br.readLine();
			testCaseCount = Integer.parseInt(textLine);
			
			while ((testCaseNumber += header ? 1 : 0) <= testCaseCount && (textLine = br.readLine()) != null) {
				if (header) {
					N = Integer.parseInt(textLine);
				} else {
/////////////////////////////////////////////////////////////////////////////////////
					M = Integer.parseInt(textLine);
					X = new LinkedList[M];
					for (int i = 0; i < M; i++ ) {
						splitted = br.readLine().split("\\s+");
						T = Integer.parseInt(splitted[0]);
						X[i] = new LinkedList<CustomerFavorite>();
						for (int j = 0; j < T; j++) {
							X[i].add(new CustomerFavorite(N, Integer.parseInt(splitted[1 + 2 * j]) - 1, Integer.parseInt(splitted[2 + 2 * j]) != 0));
						}
					}

					iterationCounter = 0;
					answer = formatMilkShakeBatch(optimalMilkShakeBatch(N, X, M), N);

					w.printf("Case #%d: %s\n", testCaseNumber, answer);
					System.out.printf("Case #%d: N=%d, M=%d, answer=%s, iterations=%d\n", testCaseNumber, N, M, CommonUtils.showString(answer, 100), iterationCounter);
////////////////////////////////////////////////////////////////////////////////////
				}
				
				header = !header;
			}

			br.close();
			result = true;
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return result;
	}

	private static String formatMilkShakeBatch(BigInteger b, int n) {
		if (b.compareTo(BigInteger.ZERO) < 0) {
			return "IMPOSSIBLE";
		}
		
		StringBuilder sb = new StringBuilder();
		int last = n - 1;
		for(int i = 0; i <= last; i++) {
			sb.append(b.testBit(i) ? '1' : '0');
			
			if (i < last) {
				sb.append(' ');
			}
		}
		return sb.toString();
	}

	private BigInteger optimalMilkShakeBatch(int n, LinkedList<CustomerFavorite>[] X, int m) {
		BigInteger b = BigInteger.ZERO;
		int i;
		HashMap<Integer/*flavor*/, HashSet<Integer/*customer index*/>> flavorMap = new HashMap<Integer, HashSet<Integer>>();
		HashSet<Integer/*customer index*/> flavorCustomers;
		MinHeap mh = new MinHeap(m);		// ---- minheap

		// ---- Build map <flavor, customer index>
		for (i = 0; i < m; i++) {
			for (CustomerFavorite cf : X[i]) {
				flavorCustomers = flavorMap.get(cf.flavor);
				if (flavorCustomers == null) {
					flavorCustomers = new HashSet<Integer>();
					flavorMap.put(cf.flavor, flavorCustomers);
				}
				flavorCustomers.add(i);
				
				// build priority queue (give the highest priority to the customer whose taste is the most specific)
				if (cf.malted && mh.getIndexOfKey(i) < 0) {
					mh.insertItem(new KeyValuePair(i, X[i].size()));
				}
			}
		}
		
		while (mh.getItemCount() > 0) {
			KeyValuePair r = mh.popRoot();
			i = r.key;
			int customerLikes = X[i].size(); 
			if (customerLikes > 1) {
				break;
			} else if (customerLikes == 1) {
				// freeze force
				CustomerFavorite cf = X[i].removeFirst();
				b = b.setBit(cf.flavor);
				
				// remove flavor for other customers, if conflict without spare flavors -> IMPOSSIBLE 
				flavorCustomers = flavorMap.remove(cf.flavor);
				flavorCustomers.remove(i);
				
				for (Integer xi : flavorCustomers) {
					LinkedList<CustomerFavorite> o = X[xi];
					int otherCustomerLikes = o.size();
					Iterator<CustomerFavorite> it = o.iterator();
					while (it.hasNext()) {
						iterationCounter++;
						CustomerFavorite cfOther = it.next();
						if (cfOther.flavor == cf.flavor) {
							if (cfOther.malted == cf.malted) { // satisfied  -> remove customer
								o.clear();
								break;
							} 
							 //conflict
							if (otherCustomerLikes == 1) {
								return BigInteger.ONE.negate();			/////////////////////////// SHORT CUT EXIT /////////////////////////
							} 
							
							it.remove();		// remove from Xi
						}
					}

					if (o.size() == 0) {
						mh.deleteItem(xi); //update minheap
						// remove customers from flavorMap
						for(HashSet<Integer> h : flavorMap.values()) {
							h.remove(xi);
						}
					} else {
						mh.updateItem(new KeyValuePair(xi, otherCustomerLikes), o.size()); //update minheap
					}
				}
			}
		}
		
		return b;
	}
}