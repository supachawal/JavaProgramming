import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

class DistinctCodes_BADPROBLEM {
/*Example

Input:
2
INBY
BYBY

Output:
3
2
*/
	// https://www.codechef.com/problems/DISTCODE
	// TAG: fast table look up
	
	static final boolean[] isACountryCode = {false, false, true, true, true, true, true, false, true, false, false, true, true, true, true, false, true, true, true, true, true, false, true, true, false, true, true, true, false, true, true, true, true, true, true, true, false, false, true, true, true, false, false, true, true, true, false, true, true, false, true, true, true, false, true, true, false, true, true, true, true, false, true, true, true, true, true, false, false, true, true, false, true, true, false, true, true, true, false, false, false, false, true, false, false, false, false, true, true, false, true, false, true, false, false, false, false, false, false, false, false, false, false, true, false, false, true, false, true, false, true, true, false, false, false, false, false, false, false, false, false, true, true, true, true, false, false, false, false, false, false, false, false, false, false, false, false, false, true, true, true, false, true, false, true, false, false, true, false, false, false, false, false, true, false, false, true, true, false, true, true, true, true, true, true, false, false, true, true, true, false, true, true, true, true, true, true, false, true, false, true, false, false, false, false, false, false, false, false, false, false, false, true, false, true, true, false, false, false, true, false, true, true, false, false, false, false, false, false, false, false, true, true, false, false, false, false, false, false, true, true, true, true, false, true, true, true, true, false, false, false, false, false, false, false, false, false, false, true, false, false, false, false, false, false, false, true, false, true, true, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true, false, true, true, true, false, false, false, true, true, false, true, false, true, false, false, false, false, true, false, true, true, true, true, true, false, false, false, false, false, true, false, true, false, false, false, false, false, false, true, true, true, true, true, false, false, true, false, true, false, true, true, true, true, true, true, false, false, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, false, true, true, true, false, true, false, false, true, false, false, true, true, false, true, false, true, true, false, false, false, false, true, false, false, false, false, false, false, false, false, false, false, false, false, true, false, false, false, false, false, false, false, false, false, false, false, false, false, true, false, false, false, true, true, true, true, false, false, true, true, true, true, false, false, false, true, true, true, false, false, true, false, true, false, true, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true, false, false, false, false, false, false, false, false, false, true, false, false, false, true, false, true, false, true, false, false, false, true, true, true, true, true, false, true, true, true, true, true, true, true, true, true, false, false, true, true, true, true, true, false, false, true, true, false, false, true, true, false, true, true, true, false, true, true, false, true, true, true, true, false, true, false, true, false, true, true, false, false, true, true, false, false, false, false, false, true, false, false, false, true, false, true, false, false, false, false, false, true, false, false, false, false, false, true, true, true, false, true, false, true, false, true, false, true, false, false, false, false, true, false, false, false, false, false, false, true, false, false, false, false, false, false, false, false, false, false, true, false, false, false, false, false, false, false, false, false, false, false, false, true, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true, false, false, true, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true, true, false, false, false, false, false, true, false, false, false, false, false, false, false, false, false, false, false, true, false, false, false, false, false, false, false, false, false, false, false, false, false }; 

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		PrintWriter pw = new PrintWriter(System.out);

		int testCaseNumber = 0, testCaseCount;
		String textLine;
//		String[] splitted;
		int answer;
		
		int i;

// PRE-BUILD boolean array
// String[] sortedCountryCodes = {"AC","AD","AE","AF","AG","AI","AL","AM","AN","AO","AQ","AR","AS","AT","AU","AW","AX","AZ",
//  "BA","BB","BD","BE","BF","BG","BH","BI","BJ","BM","BN","BO","BR","BS","BT","BV","BW","BY","BZ","CA","CC","CD","CF","CG",
//  "CH","CI","CK","CL","CM","CN","CO","CR","CS","CU","CV","CX","CY","CZ","DE","DJ","DK","DM","DO","DZ","EC","EE","EG","EH",
//  "ER","ES","ET","EU","FI","FJ","FK","FM","FO","FR","FX","GA","GB","GD","GE","GF","GG","GH","GI","GL","GM","GN","GP","GQ",
//  "GR","GS","GT","GU","GW","GY","HK","HM","HN","HR","HT","HU","ID","IE","IL","IM","IN","IO","IQ","IR","IS","IT","JE","JM",
//  "JO","JP","KE","KG","KH","KI","KM","KN","KP","KR","KW","KY","KZ","LA","LB","LC","LI","LK","LR","LS","LT","LU","LV","LY",
//  "MA","MC","MD","ME","MF","MG","MH","MK","ML","MM","MN","MO","MP","MQ","MR","MS","MT","MU","MV","MW","MX","MY","MZ","NA"
//  ,"NB",
//  "NC","NE","NF","NG","NI","NL","NO","NP","NR","NT","NU","NZ","OM","PA","PE","PF","PG","PH","PK","PL","PM","PN","PR","PS",
//  "PT","PW","PY","QA","RE","RO","RS","RU","RW","SA","SB","SC","SD","SE","SG","SH","SI","SJ","SK","SL","SM","SN","SO","SR",
//  "SS","ST","SU","SV","SY","SZ","TC","TD","TF","TG","TH","TJ","TK","TM","TN","TO","TP","TR","TT","TV","TW","TZ","UA","UG",
//  "UK","UM","US","UY","UZ","VA","VC","VE","VG","VI","VN","VU","WF","WS","XK"
//  ,"YB",
//  "YE","YT","YU","ZA","ZM","ZW"};
//
//		pw.print("{");
//		for (i = 'A'; i <= 'Z'; i++) {
//			for (int j = 'A'; j <= 'Z'; j++) {
//				String s = String.format("%c%c", i, j);
//				int point = Arrays.binarySearch(sortedCountryCodes, 0, sortedCountryCodes.length - 1, s);
//				pw.printf("%b, ", (point >= 0 && sortedCountryCodes[point].equals(s)));
//			}
//		}
//		pw.println("};");

		textLine = br.readLine();
		testCaseCount = Integer.parseInt(textLine);
		while (++testCaseNumber <= testCaseCount && (textLine = br.readLine()) != null) {
			boolean[] counted = new boolean[isACountryCode.length];
			answer = 0;
			for (i = textLine.length() - 1; i > 0; i--) {
				int testIndex = (textLine.charAt(i - 1) - 'A') * 26 + textLine.charAt(i) - 'A';  
				if (isACountryCode[testIndex] && !counted[testIndex]) {
					counted[testIndex] = true;
					answer++;
				}
			}

//			if (testCaseNumber == 6) {
//				System.gc();
//			}
			
			pw.printf("%d\n", answer);
		}
		pw.close();
		br.close();
	}
}
