package sushi.application.components.tree;

import org.apache.wicket.extensions.markup.html.repeater.tree.AbstractTree;
import org.apache.wicket.extensions.markup.html.repeater.tree.content.Folder;
import org.apache.wicket.model.IModel;


/**
 * removes the folder / document icons
 *
 * @param <T>
 */
public class TreeLinkLabel<T> extends Folder<T> {

	private static final long serialVersionUID = 3792811840659341439L;

	public TreeLinkLabel(String id, AbstractTree<T> tree, IModel<T> model) {
		super(id, tree, model);
	}
	
	@Override
	protected String getOtherStyleClass(T t) {
		 return "";
	}
	@Override
	protected String getClosedStyleClass() {
		 return "";
	}
	@Override
	protected String getOpenStyleClass() {
		 return "";
	}
	
	@Override
	protected boolean isClickable() {
		return true;
	}

}
