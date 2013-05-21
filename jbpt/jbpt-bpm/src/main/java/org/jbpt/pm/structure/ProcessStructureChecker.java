package org.jbpt.pm.structure;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jbpt.pm.ProcessModel;


/**
 * This class uses multiple small and light-weight checks to check the structure of a {@link ProcessModel}.
 * @author Christian Wiggert
 *
 */
public class ProcessStructureChecker {

	/**
	 * Add the checks to use here.
	 * @return
	 */
	private static List<ICheck> getChecks() {
		return Arrays.asList (
				new UniqueIdCheck(),
				new ContainsTaskCheck(),
				new FlowCountCheck(),
				new SourceAndSinkCheck(),
				new PathCheck());
				//new UnstructuredOrCheck());
	}
	
	/**
	 * Checks the given process for structural errors. 
	 * If the returned list of errors is empty, the process contains no errors.
	 * @param process
	 * @return list of errors, empty if none exist
	 */
	public static List<String> checkStructure(ProcessModel process) {
		List<String> errors = new ArrayList<String>();
		for (ICheck check:getChecks()) {
			try {
				errors.addAll(check.check(process));
			} catch (Exception e) {
				errors.add("An error occured during a structure test.");
			}
		}
		return errors;
	}
}
