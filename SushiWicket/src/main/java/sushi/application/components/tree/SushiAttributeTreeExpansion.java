package sushi.application.components.tree;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.wicket.MetaDataKey;
import org.apache.wicket.Session;

import sushi.event.attribute.SushiAttribute;

/**
 * handles the expansion state of a tree component
 */
public class SushiAttributeTreeExpansion implements Set<SushiAttribute>, Serializable {
	
	private static final long serialVersionUID = 1L;

	private static MetaDataKey<SushiAttributeTreeExpansion> KEY = new MetaDataKey<SushiAttributeTreeExpansion>(){};
	
	private Set<String> IDs = new HashSet<String>();
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
		if (object instanceof SushiAttribute) {
			SushiAttribute node = (SushiAttribute) object;
			if (inverse) {
				return !IDs.contains(node.getIdentifier());
			} else {
				return IDs.contains(node.getIdentifier());
			}
		} else {
			return false;
		}
	}

	@Override
	public Iterator<SushiAttribute> iterator() {
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
	public boolean add(SushiAttribute node) {
		if (inverse) {
			return IDs.remove(node.getIdentifier());
		} else {
			return IDs.add(node.getIdentifier());
		}
	}

	@Override
	public boolean remove(Object object) {
		if (object instanceof SushiAttribute) {
			SushiAttribute node = (SushiAttribute) object;
			if (inverse) {
				return IDs.add(node.getIdentifier());
			} else {
				return IDs.remove(node.getIdentifier());
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
	public boolean addAll(Collection<? extends SushiAttribute> c) {
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

	public static SushiAttributeTreeExpansion get() {
		SushiAttributeTreeExpansion expansion = Session.get().getMetaData(KEY);
		if (expansion == null) {
			expansion = new SushiAttributeTreeExpansion();
			Session.get().setMetaData(KEY, expansion);
		}
		return expansion;
	}
}
