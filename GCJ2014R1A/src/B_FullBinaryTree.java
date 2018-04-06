/* Problem B. Full Binary Tree
A tree is a connected graph with no cycles. 
A rooted tree is a tree in which one special vertex is called the root. If there is an edge between X and Y in a rooted tree, 
we say that Y is a child of X if X is closer to the root than Y (in other words, the shortest path from the root to X 
is shorter than the shortest path from the root to Y). 

A full binary tree is a rooted tree where every node has either exactly 2 children or 0 children. 

You are given a tree G with N nodes (numbered from 1 to N). You are allowed to delete some of the nodes. When a node is 
deleted, the edges connected to the deleted node are also deleted. Your task is to delete as few nodes as possible so that 
the remaining nodes form a full binary tree for some choice of the root from the remaining nodes. 

Input
The first line of the input gives the number of test cases, T. T test cases follow. The first line of each test case 
contains a single integer N, the number of nodes in the tree. The following N-1 lines each one will contain 
two space-separated integers: Xi Yi, indicating that G contains an undirected edge between Xi and Yi. 

Output

For each test case, output one line containing "Case #x: y", where x is the test case number (starting from 1) and y is 
the minimum number of nodes to delete from G to make a full binary tree. 

Limits
1 ≤ T ≤ 100.
1 ≤ Xi, Yi ≤ N
Each test case will form a valid connected tree. 

Small dataset
2 ≤ N ≤ 15.

Large dataset
2 ≤ N ≤ 1000.


Sample
Input 
3
3
2 1
1 3
7
4 5
4 2
1 2
3 1
6 4
3 7
4
1 2
2 3
3 4
   
Output 
Case #1: 0
Case #2: 2
Case #3: 1

In the first case, G is already a full binary tree (if we consider node 1 as the root), so we don't need to do anything. 

In the second case, we may delete nodes 3 and 7; then 2 can be the root of a full binary tree. 

In the third case, we may delete node 1; then 3 will become the root of a full binary tree (we could also have deleted 
node 4; then we could have made 2 the root).
*/

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.TreeSet;

public class B_FullBinaryTree {
	public static void main(String[] args) {
		CommonUtils.preamble();

		String inputFileName, outputFileName;
		if (args.length > 0) {
			inputFileName = args[0];
		} else {
//			inputFileName = "-sample1.in";
//			inputFileName = "-sample2.in";
			inputFileName = "-small-practice.in";
//			inputFileName = "-large-practice.in";
			inputFileName = B_FullBinaryTree.class.getSimpleName().substring(0, 1) + inputFileName;
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
			B_FullBinaryTree solver = new B_FullBinaryTree();
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
	private TreeSet<IntPair> degreeTree;
	private ArrayList<HashSet<Integer>> nodeNeighbors;
	private int originalNodeCount;
	private int leafNodeCount;
	private int minSoFar;
	
	public boolean solve(final File aFile, PrintWriter w) {
		String textLine;
		boolean result = false;
		BufferedReader br;
		int testCaseNumber = 0, testCaseCount;
		String [] splitted;
		int answer;
		int i, m, Xi, Yi;

		try {
			br = new BufferedReader(new FileReader(aFile));
			textLine = br.readLine();
			testCaseCount = Integer.parseInt(textLine);
			while (++testCaseNumber <= testCaseCount && (textLine = br.readLine()) != null) {
				m = Integer.parseInt(textLine) - 1;
				nodeNeighbors = new ArrayList<HashSet<Integer>>(m + 1);
				for (i = 0; i <= m; i++) {
					nodeNeighbors.add(new HashSet<Integer>());
				}

				for (i = 0; i < m; i++) {
					textLine = br.readLine();
					splitted = textLine.split("\\s+");
					Xi = new Integer(Integer.parseInt(splitted[0]) - 1);
					Yi = new Integer(Integer.parseInt(splitted[1]) - 1);
					nodeNeighbors.get(Xi).add(Yi);
					nodeNeighbors.get(Yi).add(Xi);
				}
				
				// this assumes that the graph is connected and non-empty.
				assert (m >= 0);

				degreeTree = new TreeSet<IntPair>();
				leafNodeCount = 0;
				for (i = 0; i <= m; i++) {
					int nodeDegree = nodeNeighbors.get(i).size();
					degreeTree.add(new IntPair(nodeDegree, i));
					if (nodeDegree == 1) {
						leafNodeCount++;
					}
				}
				
				originalNodeCount = m + 1;
				minSoFar = m;
				System.out.printf("Case #%d: nodeCount=%d, leafNodeCount=%d, ", testCaseNumber, originalNodeCount, leafNodeCount);
				iterationCounter = 0;
//				if (testCaseNumber == 101) {
//					System.out.printf("");
//				}
				answer = minNodesRemovedToBeFullBinaryTree();
				System.out.printf("answer=%d (iterations=%d)\n", answer, iterationCounter);

				w.printf("Case #%d: %d\n", testCaseNumber, answer);
////////////////////////////////////////////////////////////////////////////////////
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
	
	private int minNodesRemovedToBeFullBinaryTree() {
		// if it forms a full binary tree, return
		// Full Binary Tree Theorem: The number of leaves in a non-empty full binary tree is one more than the number of internal nodes.
		// n = 2 * leafCount - 1
		if (originalNodeCount - degreeTree.size() >= minSoFar) {
			return minSoFar;
		}
		if (degreeTree.last().v1 <= 3 && degreeTree.size() == 2 * leafNodeCount - 1) {
			return originalNodeCount - degreeTree.size(); 
		}
//		if (originalNodeCount == leafNodeCount + 1) {
//			return leafNodeCount - 2;
//		}
		if (degreeTree.size() == leafNodeCount + 1) {
			return originalNodeCount - degreeTree.size() + leafNodeCount - 2;
		}

		// Remove the 1-degree node recursively 
		IntPair currentDegreeInfo = degreeTree.first();
		int degree = currentDegreeInfo.v1;
		int nodeIndex = currentDegreeInfo.v2;
		while (degree == 1) {
			iterationCounter++;
			Integer itsNeighbor = nodeNeighbors.get(nodeIndex).iterator().next();
			// remove itself from tree
			degreeTree.remove(currentDegreeInfo);
			// it is no more a neighbor of its neighbor
			nodeNeighbors.get(itsNeighbor).remove(nodeIndex);
			// decrease degree of its neighbor
			int itsNeighborDegreeAfterRemoval = nodeNeighbors.get(itsNeighbor).size();
			degreeTree.remove(new IntPair(itsNeighborDegreeAfterRemoval + 1, itsNeighbor));
			degreeTree.add(new IntPair(itsNeighborDegreeAfterRemoval, itsNeighbor));
			
			if (itsNeighborDegreeAfterRemoval != 1) {
				leafNodeCount--;
			}

			// recurse
			minSoFar = Math.min(minSoFar, minNodesRemovedToBeFullBinaryTree());
			
			// restore
			if (itsNeighborDegreeAfterRemoval != 1) {
				leafNodeCount++;
			}
			degreeTree.add(currentDegreeInfo);
			nodeNeighbors.get(itsNeighbor).add(nodeIndex);
			degreeTree.remove(new IntPair(itsNeighborDegreeAfterRemoval, itsNeighbor));
			degreeTree.add(new IntPair(itsNeighborDegreeAfterRemoval + 1, itsNeighbor));
			
			// next loop
			currentDegreeInfo = degreeTree.higher(currentDegreeInfo);
			
			if (currentDegreeInfo == null) {
				degree = 0;
			} else {
				degree = currentDegreeInfo.v1;
				nodeIndex = currentDegreeInfo.v2;
			}
		}
		
		return minSoFar;
	}
}