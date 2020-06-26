package fr.cnrs.iees.io.parsing.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

class QuoteProcessorTest {

	static char[] quotes = {'\"','\"'};
	String test = "	hasProperty timeOriginPropertySpec\n" +
			"		hasName = String(\"timeOrigin\")\n" +
			"		type = String(\"DateTimeType\")\n" +
			"		multiplicity = IntegerRange(\"1..1\")\n" +
			"	mustSatisfyQuery shortestTimeUnitTimeUnitValidityQuery\n" +
			"		className = String(\"au.edu.anu.twcore.archetype.tw.TimeUnitValidityQuery\")\n" +
			"		values = StringTable(([2]\"shortestTimeUnit\",\"scale\"))\n" +
			"";

	@Test
	void testUnQuote() {
		List<String> result = QuoteProcessor.unQuote(test, quotes);
		int i=0;
		for (String s:result)
			System.out.println(i+++": "+s);
	}

}
