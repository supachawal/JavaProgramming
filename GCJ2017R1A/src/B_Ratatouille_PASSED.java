import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

public class B_Ratatouille_PASSED {
	public static void main(String[] args) {
		CommonUtils.preamble();

		String inputFileName, outputFileName;
		if (args.length > 0) {
			inputFileName = args[0];
		} else {
//			inputFileName = "-sample1.in";
//			inputFileName = "-sample2.in";
//			inputFileName = "-small-practice.in";
//			inputFileName = "-small-attempt0.in";
			inputFileName = "-large-practice.in";
//			inputFileName = "-large.in";
			inputFileName = B_Ratatouille_PASSED.class.getSimpleName().substring(0, 1) + inputFileName;
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
			B_Ratatouille_PASSED solver = new B_Ratatouille_PASSED();
			solver.solve(inputFile, outputWriter);
			outputWriter.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		CommonUtils.postamble();
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
			int P = Integer.parseInt(splitted[1]);
			int i, j;
			int[] oneServeIngredients = new int[N];
			int[][] ingredients = new int[N][P];

			System.out.printf("Case #%d: N=%d, P=%d,\n", testCaseNumber, N, P);
			w.printf("Case #%d:", testCaseNumber);

			textLine = br.readLine();
			System.out.printf("%s\n", textLine);
			splitted = textLine.split("\\s+");
			for (i = 0; i < N; i++) {
				oneServeIngredients[i] = Integer.parseInt(splitted[i]);
			}
			
			for (i = 0; i < N; i++) {
				textLine = br.readLine();
				System.out.printf("%s\n", textLine);
				splitted = textLine.split("\\s+");
				for (j = 0; j < P; j++) {
					ingredients[i][j] = Integer.parseInt(splitted[j]);
				}
			}
			
			int answer = validPackages(oneServeIngredients, ingredients);

			System.out.printf("=> answer=%d\n", answer);
			w.printf(" %d\n", answer);
			
////////////////////////////////////////////////////////////////////////////////////
		}

		br.close();
	}

	// Greedy
	private static int validPackages(int[] oneServeIngredients, int[][] ingredients) {
		int ret = 0;
		int N = ingredients.length;
		int P = ingredients[0].length;
		int i, ingredient, oneServe;
		int[] ingredientIndices = new int[N];

		for (i = 0; i < N; i++) {
			Arrays.sort(ingredients[i]);
		}

		while (true) {
			int nMaxServes = 0;
			int minIngredientIndex = -1;
			for (i = 0; i < N; i++) {
				ingredient = ingredients[i][ingredientIndices[i]];
				oneServe = oneServeIngredients[i];
				int divisor = oneServe * 11;
				int nServes = (ingredient * 10 + divisor - 1) / divisor;
//				int nServes = ingredient / oneServe;
				while (ingredient * 10 < oneServe * nServes * 9) {
					nServes--;
				}
				
				if (ingredient * 10 > oneServe * nServes * 11) {
					if (++ingredientIndices[i--] >= P) {
						return ret; // *********** SHORT CUT
					}
					continue;
				}

				if (nServes > nMaxServes) {
					nMaxServes = nServes;
					minIngredientIndex = i;
				}
			}

			for (i = 0; i < N; i++) {
				ingredient = ingredients[i][ingredientIndices[i]];
				oneServe = oneServeIngredients[i];
				if (ingredient * 10 < oneServe * nMaxServes * 9) {
					if (++ingredientIndices[i--] >= P) {
						return ret; // *********** SHORT CUT
					}
				}
			}

			for (i = 0; i < N; i++) {
				ingredient = ingredients[i][ingredientIndices[i]];
				oneServe = oneServeIngredients[i];
				if (ingredient * 10 > oneServe * nMaxServes * 11) {
					break;
				}
			}

			if (i == N) {
				ret++;

				for (i = 0; i < N; i++) {
					if (++ingredientIndices[i] >= P) {
						return ret; // *********** SHORT CUT
					}
				}
				
			} else if (minIngredientIndex >= 0 && ++ingredientIndices[minIngredientIndex] >= P) {
				return ret; // *********** SHORT CUT
			}
		}
	}
}