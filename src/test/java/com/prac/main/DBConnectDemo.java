package com.prac.main;

import java.util.ArrayList;
import java.util.List;

public class DBConnectDemo {

//	public static void main(String[] args) {
//		List<Integer> content = new ArrayList<Integer>();
//		content.add(6);
//		content.add(5);
//		content.add(28);
//		for (Integer i : content) {
//			System.out.println("Number : " + i + ", solution: " + solve(i));
//		}
//
//	}
//
//	public static String solve(Integer N) {
//		Integer sum = 0;
//		for (Integer i = 1; i < N; i++) 
//			if (N % i == 0) 
//				sum += i;
//		if (sum == N) 
//			return "YES";
//		return "NO";
//	}
	public static void main(String[] args) {
		String[] str = {};// { "abcde", "fghij", "kmnlo", "pqrst", "uvwxyz" };
		int[][] query = { { 3, 3, 3 }, { 1, 15, 16 }, { 3, 5, 15 } };
		char[] arr = charAt(str.length, str, query.length, query);
		for (char c : arr)
			System.out.println("value: " + c);
	}

	static char[] charAt(int N, String[] str, int q, int[][] query) {
		char[] returnCont = new char[q];
		int counter = 0;
		if (q > 0 && q <= Math.pow(10, 5) && N > 0 && N <= Math.pow(10, 5)) {
			for (int i = 0; i < q; i++) {
				String appended = "";
				if (query[i][0] > query[i][1] || query[i][0] > N || query[i][1] > N || query[i][1] < 1
						|| query[i][0] < 1)
					continue;
				for (int j = query[i][0]; j <= query[i][1]; j++) {
					if (str[j - 1].length() >= 1 && str[j - 1].length() <= 50)
						appended += str[j - 1];
				}
				System.out.println("appended: " + appended);
				returnCont[counter] = appended.charAt(query[i][2] - 1);
				counter++;
			}
		}
		return returnCont;
	}
}
