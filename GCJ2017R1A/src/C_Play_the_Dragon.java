import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

public class C_Play_the_Dragon {
	public static void main(String[] args) {
		CommonUtils.preamble();

		String inputFileName, outputFileName;
		if (args.length > 0) {
			inputFileName = args[0];
		} else {
//			 inputFileName = "-sample1.in";
//			 inputFileName = "-sample2.in";
			 inputFileName = "-small-practice.in";
			// inputFileName = "-small-attempt0.in";
//			inputFileName = "-large-practice.in";
			// inputFileName = "-large.in";
			inputFileName = C_Play_the_Dragon.class.getSimpleName().substring(0, 1) + inputFileName;
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
			C_Play_the_Dragon solver = new C_Play_the_Dragon();
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
			int Hd = Integer.parseInt(splitted[0]);
			fullHd = Hd;
			int Ad = Integer.parseInt(splitted[1]);
			int Hk = Integer.parseInt(splitted[2]);
			int Ak = Integer.parseInt(splitted[3]);
			int buff = Integer.parseInt(splitted[4]);
			debuff = Integer.parseInt(splitted[5]);
			
			System.out.printf("Case #%d: Hd=%d, Ad=%d, Hk=%d, Ak=%d, B=%d, D=%d\n", testCaseNumber, Hd, Ad, Hk, Ak, buff, debuff);
			w.printf("Case #%d:", testCaseNumber);
			
			String answer = "IMPOSSIBLE";
			
			int nBuffs = 0;
			long buffedAd = Ad; 
			while ((Hk + buffedAd + buff - 1) / (buffedAd + buff) + 1 < (Hk + buffedAd - 1) / buffedAd) {
				nBuffs++;
				buffedAd += buff;
			}
			
			int nMinPeriodsToKill = (int)((Hk + buffedAd - 1) / buffedAd + nBuffs);
			int nMinDefences = Integer.MAX_VALUE;
			
			if (debuff == 0) {
				if (!(nMinPeriodsToKill > 2 && Hd <= 2 * Ak)) {
					nMinDefences = (nMinPeriodsToKill - 1) / ((Hd - 1) / Ak + 1);
				}
			} else if (nMinPeriodsToKill == 1 || nMinPeriodsToKill == 2 && Hd > Ak) {
				nMinDefences = 0;
			} else {
				memoCache = new HashMap<IntPack, Integer>();
				int defences1 = Hd <= 2 * Ak ? Integer.MAX_VALUE : minDefence(Ak, nMinPeriodsToKill - 2, Hd - Ak, 0);
				memoCache = new HashMap<IntPack, Integer>();
				int defences2 = minDefence(Ak, nMinPeriodsToKill, Hd, 0);
				
				nMinDefences = Math.min(defences1, defences2);
			}
			
			if (nMinDefences < Integer.MAX_VALUE) {
				answer = String.valueOf(nMinPeriodsToKill + nMinDefences);
			}

			System.out.printf("=> #buff=%d, #PeriodsToKill=%d, #cure+debuff=%d, answer=%s\n", nBuffs, nMinPeriodsToKill, nMinDefences, answer);
			w.printf(" %s\n", answer);

			////////////////////////////////////////////////////////////////////////////////////
		}

		br.close();
	}

	private int fullHd;
	private int debuff;
	private HashMap<IntPack, Integer> memoCache;
	
	private int minDefence(int Ak, int n, int currentHd, int level) { //assumes debuff > 0
		if (Ak <= 0 || n <= (currentHd - 1) / Ak) {
			return 0;
		}
		
		IntPack key = new IntPack(Ak, n, currentHd);
		Integer ret = memoCache.get(key);
		
		if (ret != null) {
			return ret;
		}
		
		ret = Integer.MAX_VALUE;
		int debuffedAk = Math.max(0, Ak - debuff);
		if (currentHd > debuffedAk) { //debuff
			ret = Math.min(ret, 1 + minDefence(debuffedAk, n, currentHd - debuffedAk, level + 1));
		}
		
		if (currentHd <= Ak) { // cure
			if (fullHd > 2 * Ak) {
				ret = Math.min(ret, 1 + minDefence(Ak, n, fullHd - Ak, level + 1));
			}
		} else {	// do nothing, let the enemy attack ******** TOO SLOW
			ret = Math.min(ret, minDefence(Ak, n - 1, currentHd - Ak, level + 1));
		}
		
		memoCache.put(key, ret);
		return ret;
	}
}