package org.jbpt.test.graph;

import java.util.ArrayList;
import java.util.Collection;

import org.jbpt.algo.CombinationGenerator;

import junit.framework.TestCase;

public class CombinationGeneratorTest extends TestCase{

	public void testSomeBehavior() {
		Collection<String> c = new ArrayList<String>();
		c.add("a"); c.add("b"); c.add("c"); c.add("d");
		
		CombinationGenerator<String> cg = new CombinationGenerator<String>(c, 3);
		while (cg.hasMore ()) {
			System.out.println(cg.getNextCombination());
		}
	}

}
