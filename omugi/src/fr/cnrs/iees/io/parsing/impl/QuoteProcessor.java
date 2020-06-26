package fr.cnrs.iees.io.parsing.impl;

import java.util.ArrayList;
import java.util.List;

/**
 * An interface to process Strings with quotes
 * @author J. Gignoux - 26 juin 2020
 *
 */
public interface QuoteProcessor {

	public static List<String> unQuote(String quotedString, char[] quotes) {
		List<String> result = new ArrayList<>();
		if (quotedString.indexOf(quotes[0])<0)
			result.add(quotedString);
		else {
			String endString = quotedString;
			String s = "";
			while (endString.indexOf(quotes[0])>=0) {
				// get opening quote
				int cutoff = endString.indexOf(quotes[0]);
				s = endString.substring(0,cutoff);
				// this is the string before the opening quote
				result.add(s);
				endString = endString.substring(cutoff+1);
				// get closing quote
				cutoff = endString.indexOf(quotes[1]);
				s = endString.substring(0,cutoff);
				result.add(s);
				endString = endString.substring(cutoff+1);
			}
			result.add(endString);
		}
		return result;
	}


}
