package sushi.application.pages.monitoring.bpmn.analysis.modal.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.wicket.MetaDataKey;
import org.apache.wicket.Session;

/**
 * Handles the expansion state of the tree.
 */
public class ProcessAnalysingTreeTableExpansion implements Set<ProcessAnalysingTreeTableElement>, Serializable {
	
	private static final long serialVersionUID = 1L;

	private static MetaDataKey<ProcessAnalysingTreeTableExpansion> KEY = new MetaDataKey<ProcessAnalysingTreeTableExpansion>(){};
	
	private Set<Integer> IDs = new HashSet<Integer>();
	private boolean inverse;
	
	public void expandAll() {
		IDs.clear();
		inverse = true;
    }
	
	public void collapseAll() {
		IDs.clear();
		inverse = false;
    }
	
	@Override
	public int size() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isEmpty() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean contains(Object object) {
		if (object instanceof ProcessAnalysingTreeTableElement) {
			ProcessAnalysingTreeTableElement node = (ProcessAnalysingTreeTableElement) object;
			if (inverse) {
				return !IDs.contains(node.getID());
			} else {
				return IDs.contains(node.getID());
			}
		} else {
			return false;
		}
	}

	@Override
	public Iterator<ProcessAnalysingTreeTableElement> iterator() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object[] toArray() {
		throw new UnsupportedOperationException();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean add(ProcessAnalysingTreeTableElement node) {
		if (inverse) {
			return IDs.remove(node.getID());
		} else {
			return IDs.add(node.getID());
		}
	}

	@Override
	public boolean remove(Object object) {
		if (object instanceof ProcessAnalysingTreeTableElement) {
			ProcessAnalysingTreeTableElement node = (ProcessAnalysingTreeTableElement) object;
			if (inverse) {
				return IDs.add(node.getID());
			} else {
				return IDs.remove(node.getID());
			}
		} else {
			return false;
		}
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean addAll(Collection<? extends ProcessAnalysingTreeTableElement> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void clear() {
		throw new UnsupportedOperationException();
	}

	public static ProcessAnalysingTreeTableExpansion get() {
		ProcessAnalysingTreeTableExpansion expansion = Session.get().getMetaData(KEY);
		if (expansion == null) {
			expansion = new ProcessAnalysingTreeTableExpansion();
			Session.get().setMetaData(KEY, expansion);
		}
		return expansion;
	}
}
