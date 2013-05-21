package org.jbpt.test.petri.unfolding;

import junit.framework.TestCase;

public class SoundUnfoldingExtensiveTestB extends TestCase {

	/*protected static final String MODELS_DIR = "src/test/resources/models/process_json/allmodels";
	
	public void testComparison() throws SerializationException, IOException, TransformationException {
		File modelsDir = new File(MODELS_DIR);
		FileWriter fstream = new FileWriter("target/convert.bat");
		BufferedWriter out = new BufferedWriter(fstream);
		
		int count = 0;
		DirectedGraphAlgorithms<ControlFlow<FlowNode>,FlowNode> dga = new DirectedGraphAlgorithms<ControlFlow<FlowNode>,FlowNode>();
		for (String name : modelsDir.list()) {
			if (name.endsWith(".json"))	{
				ProcessModel p = loadProcess(MODELS_DIR + File.separator + name);
				if (dga.isCyclic(p)) continue;
				
				count++;
				if (count<450) continue;
				System.out.println(count);
				
				System.out.print(name + " ... ");
				NetSystem net = ProcessModel2NetSystem.transform(p);
				int cp = 1; int ct = 1;
				for (Place place : net.getPlaces()) place.setName("p"+cp++);
				for (Transition trans : net.getTransitions()) trans.setName("t"+ct++);
				net.loadNaturalMarking();
				
				boolean flag = true;
				SoundUnfolding unf = null;
				try {
					unf = new SoundUnfolding(net);
					
					if (unf.isSound()) {
						System.out.print("\tSOUND");
						
						String fileName = name+".NET";
						IOUtils.toFile(fileName+".dot", net.toDOT());
						out.write("dot -Tpng -o"+fileName+".png "+fileName+".dot\n");
						
						IOccurrenceNet bpnet = unf.getOccurrenceNet();
						
						fileName = name+".UNF";
						IOUtils.toFile(fileName+".dot", bpnet.toDOTcs(unf.getLocallyUnsafeConditions()));
						out.write("dot -Tpng -o"+fileName+".png "+fileName+".dot\n");
					}
					else {
						String fileName = name+".NET";
						IOUtils.toFile(fileName+".dot", net.toDOT());
						out.write("dot -Tpng -o"+fileName+".png "+fileName+".dot\n");
						
						IOccurrenceNet bpnet = unf.getOccurrenceNet();
						
						fileName = name+".UNF.UNSAFE";
						IOUtils.toFile(fileName+".dot", bpnet.toDOTcs(unf.getLocallyUnsafeConditions()));
						out.write("dot -Tpng -o"+fileName+".png "+fileName+".dot\n");
						
						fileName = name+".UNF.DEADLOCK";
						IOUtils.toFile(fileName+".dot", bpnet.toDOTcs(unf.getLocalDeadlockConditions()));
						out.write("dot -Tpng -o"+fileName+".png "+fileName+".dot\n");
						
						System.out.print("\tUNSOUND");
					}
				}
				catch (Exception e) {
					System.out.print("\tWARNING");
					flag = false;
				}
				
				boolean soundLola = false;
				try {
					soundLola = LolaSoundnessChecker.analyzeSoundness(net).isClassicalSound();
					if (soundLola) System.out.println("\tSOUND");
					else System.out.println("\tUNSOUND");	
				} catch (IOException e) {
					System.out.println("\tWARNING");
					flag = false;
				}
				
				if (flag) {
					if (unf.isSound() != soundLola) out.close();
					assertEquals(soundLola, unf.isSound());
					if (count==600) break;
				}
			}
		}
		
		out.close();
	}
	
	protected ProcessModel loadProcess(String filename) throws SerializationException, IOException {
		String line;
		StringBuilder sb = new StringBuilder();
		BufferedReader reader = new BufferedReader(new FileReader(filename));
		while ((line = reader.readLine()) != null) {
			sb.append(line);
		}
		reader.close();
		return JSON2Process.convert(sb.toString());
	}*/
		
}
