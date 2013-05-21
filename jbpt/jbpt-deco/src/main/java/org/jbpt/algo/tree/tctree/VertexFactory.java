package org.jbpt.algo.tree.tctree;

import org.jbpt.hypergraph.abs.IVertex;


public class VertexFactory<V extends IVertex> {
	
	private final Class<? extends IVertex> clazz;
	
	public VertexFactory(Class<? extends IVertex> clazz) {
		this.clazz = clazz;
	}
	
	@SuppressWarnings("unchecked")
	public V createInstance() {
		try {
			return (V) clazz.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
			return null;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return null;
		}
	}

}