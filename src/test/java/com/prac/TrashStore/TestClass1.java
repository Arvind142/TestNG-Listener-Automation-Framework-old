package com.prac.TrashStore;

import org.testng.annotations.Test;
import com.prac.framework.util.TestNGBase;

public class TestClass1 extends TestNGBase {
	@Test(enabled = true)
	public void logPrimitiveComparison() {
		// test case name
		String testName = className + "." + (new Object() {
		}.getClass().getEnclosingMethod().getName());
		try {
			logInfo(testName, "Execution started", "!!");
			
			// all comparison

			// INT
			log(testName, "Int Comparison", 12, 12);
			log(testName, "Int Comparison", 124, 12);

			Integer[] expi = { 10, 12, 14 };
			Integer[] acti = { 10, 12, 14 };
			log(testName, "Int[] Comparison", expi, acti);
			Integer[] inacti = { 10, 12, 12 };
			log(testName, "Int[] Comparison", expi, inacti);

			// Long
			log(testName, "Long Comparison", Long.parseLong("1234567890"), Long.parseLong("1234567890"));
			log(testName, "Long Comparison", Long.parseLong("1234567890"), Long.parseLong("12345678901234"));

			Long[] expl = { Long.parseLong("1234567890"), Long.parseLong("1234567890") };
			Long[] actl = { Long.parseLong("1234567890"), Long.parseLong("1234567890") };
			log(testName, "Long[] Comparison", expl, actl);
			Long[] inactl = { Long.parseLong("1234567890"), Long.parseLong("12345678901234") };
			log(testName, "Long[] Comparison", expl, inactl);

			// float
			log(testName, "float Comparison", 12.0f, 12.0f);
			log(testName, "float Comparison", 124.0f, 12.0f);

			Float[] expf = { 12.0f, 12.0f };
			Float[] actf = { 12.0f, 12.0f };
			log(testName, "float[] Comparison", expf, actf);
			Float[] inactf = { 124.0f, 12.0f };
			log(testName, "float[] Comparison", expf, inactf);

			// double
			log(testName, "double Comparison", 14.0d, 14.0d);
			log(testName, "double Comparison", 124.0d, 14.0d);

			Double[] expd = { 12.0d, 12.0d };
			Double[] actd = { 12.0d, 12.0d };
			log(testName, "double[] Comparison", expd, actd);
			Double[] inactd = { 124.0d, 12.0d };
			log(testName, "double[] Comparison", expd, inactd);

			// String
			log(testName, "String Comparison", "ABC", "ABC");
			log(testName, "String Comparison", "ABCD", "ABC");
			
			String[] exps = { "ABC", "ABC" };
			String[] acts = { "ABC", "ABC" };
			log(testName, "String[] Comparison", exps, acts);
			String[] inacts = { "ABCD", "ABC"};
			log(testName, "String[] Comparison", exps, inacts);

			logInfo(testName, "Execution stopped", "!!");
		} catch (Exception e) {
			e.printStackTrace();
			logError(testName, "execution...", e.getMessage());
		}

	}
}
