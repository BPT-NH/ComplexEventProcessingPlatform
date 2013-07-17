package sushi.application.pages.transformation.patternbuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.HeadersToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.extensions.markup.html.repeater.tree.table.TreeColumn;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import sushi.application.components.form.WarnOnExitForm;
import sushi.application.components.tree.TreeExpansion;
import sushi.application.components.tree.TreeExpansionModel;
import sushi.application.components.tree.TreeTableProvider;
import sushi.application.pages.transformation.AdvancedTransformationRuleEditorPanel;
import sushi.application.pages.transformation.patternbuilder.model.ElementOptionsPanel;
import sushi.application.pages.transformation.patternbuilder.model.EventTypeAliasPanel;
import sushi.application.pages.transformation.patternbuilder.model.EventTypeElementOptionsPanel;
import sushi.application.pages.transformation.patternbuilder.model.EveryDistinctPatternOperatorPanel;
import sushi.application.pages.transformation.patternbuilder.model.FilterExpressionPanel;
import sushi.application.pages.transformation.patternbuilder.model.PatternElementTreeTable;
import sushi.application.pages.transformation.patternbuilder.model.RepeatPatternOperatorRangePanel;
import sushi.application.pages.transformation.patternbuilder.model.UntilPatternOperatorRangePanel;
import sushi.event.SushiEventType;
import sushi.event.collection.SushiTreeElement;
import sushi.transformation.collection.SushiPatternTree;
import sushi.transformation.element.EventTypeElement;
import sushi.transformation.element.FilterExpressionConnectorElement;
import sushi.transformation.element.FilterExpressionConnectorEnum;
import sushi.transformation.element.FilterExpressionElement;
import sushi.transformation.element.PatternOperatorElement;
import sushi.transformation.element.PatternOperatorEnum;

public class PatternBuilderPanel extends Panel {
	
	private static final long serialVersionUID = -3517674159437927655L;
	private SushiEventType selectedEventType;
	private Form<Void> layoutForm;
	private DropDownChoice<PatternOperatorEnum> patternOperatorDropDownChoice;
	private PatternOperatorEnum selectedPatternOperator;
	private DropDownChoice<SushiEventType> eventTypeDropDownChoice;
	private TreeTableProvider<Serializable> patternTreeTableProvider;
	private PatternElementTreeTable patternTreeTable;

	private SushiPatternTree patternTree;
	private PatternBuilderPanel patternBuilderPanel;
	private AdvancedTransformationRuleEditorPanel advancedRuleEditorPanel;
	private DropDownChoice<FilterExpressionConnectorEnum> filterExpressionConnectorDropDownChoice;
	private FilterExpressionConnectorEnum filterExpressionConnector;

	public PatternBuilderPanel(String id, final AdvancedTransformationRuleEditorPanel advancedRuleEditorPanel) {
		super(id);
		
		this.advancedRuleEditorPanel = advancedRuleEditorPanel;
		this.patternBuilderPanel = this;
		
		layoutForm = new WarnOnExitForm("layoutForm");
		add(layoutForm);
		
		patternTree = new SushiPatternTree();
		patternTreeTableProvider = new TreeTableProvider<Serializable>(patternTree.getRoots());
		
		buildEventTypeDropDownChoice();
		buildPatternOperatorDropDownChoice();
		buildFilterExpressionConnectorDropDownChoice();
		buildButtons();
		buildPatternTreeTable();
	}

	private void buildEventTypeDropDownChoice() {
		
		List<SushiEventType> eventTypes = SushiEventType.findAll();
		eventTypeDropDownChoice = new DropDownChoice<SushiEventType>("eventTypeDropDownChoice", new PropertyModel<SushiEventType>(this, "selectedEventType"), eventTypes);
		eventTypeDropDownChoice.setOutputMarkupId(true);
		layoutForm.add(eventTypeDropDownChoice);
	}
	
	private void buildPatternOperatorDropDownChoice() {
		patternOperatorDropDownChoice = new DropDownChoice<PatternOperatorEnum>("patternOperatorDropDownChoice", new PropertyModel<PatternOperatorEnum>(this, "selectedPatternOperator"), Arrays.asList(PatternOperatorEnum.values()));
		patternOperatorDropDownChoice.setOutputMarkupId(true);
		patternOperatorDropDownChoice.setEnabled(false);
		layoutForm.add(patternOperatorDropDownChoice);
	}
	
	private void buildFilterExpressionConnectorDropDownChoice() {
		filterExpressionConnectorDropDownChoice = new DropDownChoice<FilterExpressionConnectorEnum>("filterExpressionConnectorDropDownChoice", new PropertyModel<FilterExpressionConnectorEnum>(this, "filterExpressionConnector"), Arrays.asList(FilterExpressionConnectorEnum.values()));
		filterExpressionConnectorDropDownChoice.setOutputMarkupId(true);
		filterExpressionConnectorDropDownChoice.setEnabled(false);
		layoutForm.add(filterExpressionConnectorDropDownChoice);
	}
	
	private void buildButtons() {
		
		AjaxButton addButton = new AjaxButton("addButton", layoutForm) {
			private static final long serialVersionUID = 1L;
			@Override
			public void onSubmit(AjaxRequestTarget target, Form<?> form) {
				SushiTreeElement<Serializable> newTreeElement;
				Iterator<SushiTreeElement<Serializable>> iterator;
				if (eventTypeDropDownChoice.isEnabled() && selectedEventType != null) {
					newTreeElement = new EventTypeElement(patternTreeTableProvider.getNextID(), selectedEventType);
					if (patternTreeTable.getSelectedElements().isEmpty()) {
						patternTree.addElement(newTreeElement);
						patternTreeTableProvider.setRootElements(patternTree.getRoots());
						target.add(advancedRuleEditorPanel.getAttributeTreePanel().getAttributeTreeTable());
					}
				} else {
					if (patternOperatorDropDownChoice.isEnabled() && selectedPatternOperator != null) {
						newTreeElement = new PatternOperatorElement(patternTreeTableProvider.getNextID(), selectedPatternOperator);
					} else if (filterExpressionConnectorDropDownChoice.isEnabled() && filterExpressionConnector != null) {
						newTreeElement = new FilterExpressionConnectorElement(patternTreeTableProvider.getNextID(), filterExpressionConnector);
					} else {
						return;
					}
					iterator = patternTreeTable.getSelectedElements().iterator();
					SushiTreeElement<Serializable> parentOfSelectedElements = iterator.next().getParent();
					iterator = patternTreeTable.getSelectedElements().iterator();
					while (iterator.hasNext()) {
						iterator.next().setParent(newTreeElement);
					}
					if (parentOfSelectedElements != null) {
						newTreeElement.setParent(parentOfSelectedElements);
						iterator = patternTreeTable.getSelectedElements().iterator();
						while (iterator.hasNext()) {
							parentOfSelectedElements.removeChild(iterator.next());
						}
					}
					patternTree.addElement(newTreeElement);
					patternTreeTableProvider.setRootElements(patternTree.getRoots());
				}
					
				patternTreeTable.getSelectedElements().clear();
				target.add(patternTreeTable);
				updateOnTreeElementSelection(target);
	        }
	    };
	    layoutForm.add(addButton);
	}

	public DropDownChoice<SushiEventType> getEventTypeDropDownChoice() {
		return eventTypeDropDownChoice;
	}

	public DropDownChoice<PatternOperatorEnum> getPatternOperatorDropDownChoice() {
		return patternOperatorDropDownChoice;
	}

	public DropDownChoice<FilterExpressionConnectorEnum> getFilterExpressionConnectorDropDownChoice() {
		return filterExpressionConnectorDropDownChoice;
	}

	public TreeTableProvider<Serializable> getPatternTreeTableProvider() {
		return patternTreeTableProvider;
	}

	public PatternElementTreeTable getPatternTreeTable() {
		return patternTreeTable;
	}

	public SushiPatternTree getPatternTree() {
		return patternTree;
	}
	
	public void setPatternTree(SushiPatternTree patternTree) {
		this.patternTree = patternTree;
	}
	
	public AdvancedTransformationRuleEditorPanel getAdvancedRuleEditorPanel() {
		return advancedRuleEditorPanel;
	}

	private void buildPatternTreeTable() {
		
		List<IColumn<SushiTreeElement<Serializable>, String>> columns = createColumns();
		
		patternTreeTable = new PatternElementTreeTable(
					"patternTreeTable", 
					columns, 
					patternTreeTableProvider, 
					Integer.MAX_VALUE, 
					new TreeExpansionModel<Serializable>(),
					this);
		
		patternTreeTable.setOutputMarkupId(true);
		TreeExpansion.get().expandAll();
		patternTreeTable.getTable().addTopToolbar(new HeadersToolbar<String>(patternTreeTable.getTable(), patternTreeTableProvider));
		
		layoutForm.addOrReplace(patternTreeTable);
	}
	
	private List<IColumn<SushiTreeElement<Serializable>, String>> createColumns() {
		List<IColumn<SushiTreeElement<Serializable>, String>> columns = new ArrayList<IColumn<SushiTreeElement<Serializable>, String>>();
    
		columns.add(new PropertyColumn<SushiTreeElement<Serializable>, String>(Model.of("ID"), "ID"));
		columns.add(new TreeColumn<SushiTreeElement<Serializable>, String>(Model.of("Elements")));
		
		columns.add(new AbstractColumn<SushiTreeElement<Serializable>, String>(new Model("")) {
			@Override
			public void populateItem(Item cellItem, String componentId, IModel rowModel) {
				Object treeTableElement = rowModel.getObject();
				if (treeTableElement instanceof EventTypeElement) {
					EventTypeElement eventTypeElement = (EventTypeElement) treeTableElement;
					cellItem.add(new EventTypeAliasPanel(componentId, eventTypeElement, advancedRuleEditorPanel));
				} else {
					cellItem.add(new Label(componentId));
				}
			}
		});
		
		columns.add(new AbstractColumn<SushiTreeElement<Serializable>, String>(new Model("")) {
			@Override
			public void populateItem(Item cellItem, String componentId, IModel rowModel) {
				
				Object treeTableElement = rowModel.getObject();
				if (treeTableElement instanceof EventTypeElement) {
					final EventTypeElement eventTypeElement = (EventTypeElement) treeTableElement;
					cellItem.add(new EventTypeElementOptionsPanel(componentId, eventTypeElement, patternTree, patternTreeTableProvider, patternTreeTable, patternBuilderPanel));
				} else if (treeTableElement instanceof FilterExpressionElement) {
					FilterExpressionElement filterExpressionElement = (FilterExpressionElement) treeTableElement;
					cellItem.add(new FilterExpressionPanel(componentId, filterExpressionElement, patternBuilderPanel));
				} else if (treeTableElement instanceof PatternOperatorElement) {
					PatternOperatorElement poElement = (PatternOperatorElement) treeTableElement;
					if (poElement.getValue() == PatternOperatorEnum.UNTIL) {
						cellItem.add(new UntilPatternOperatorRangePanel(componentId, poElement));
					} else if (poElement.getValue() == PatternOperatorEnum.REPEAT) {
						cellItem.add(new RepeatPatternOperatorRangePanel(componentId, poElement, advancedRuleEditorPanel));
					} else if (poElement.getValue() == PatternOperatorEnum.EVERY_DISTINCT) {
						cellItem.add(new EveryDistinctPatternOperatorPanel(componentId, poElement, advancedRuleEditorPanel));
					} else {
						cellItem.add(new Label(componentId));
					}
				} else {
					cellItem.add(new Label(componentId));
				}
			}
		});
		
		columns.add(new AbstractColumn<SushiTreeElement<Serializable>, String>(new Model("")) {
			@Override
			public void populateItem(Item cellItem, String componentId, IModel rowModel) {
				SushiTreeElement<Serializable> treeTableElement = (SushiTreeElement<Serializable>) rowModel.getObject();
				cellItem.add(new ElementOptionsPanel(componentId, treeTableElement, patternBuilderPanel));
			}
		});

		return columns;
	}
	
	public void updatePatternTreeTable(AjaxRequestTarget target) {
		patternTreeTableProvider.setRootElements(patternTree.getRoots());	
		patternTreeTable.getSelectedElements().clear();
		target.add(patternTreeTable);
		updateOnTreeElementSelection(target);
	}

	public void updateOnTreeElementSelection(AjaxRequestTarget target) {
		int numberOfSelectedElements = patternTreeTable.numberOfSelectedElements();
		List<PatternOperatorEnum> operators;
		List<FilterExpressionConnectorEnum> connectors;
		SushiTreeElement<Serializable> firstSelectedElement;
		if (numberOfSelectedElements == 0) {
			eventTypeDropDownChoice.setEnabled(true);
			patternOperatorDropDownChoice.setEnabled(false);
			filterExpressionConnectorDropDownChoice.setEnabled(false);
		} else if (numberOfSelectedElements == 1) {
			firstSelectedElement = patternTreeTable.getSelectedElements().iterator().next();
			if (firstSelectedElement instanceof FilterExpressionElement || firstSelectedElement instanceof FilterExpressionConnectorElement) {
				connectors = FilterExpressionConnectorEnum.getUnaryOperators();
				filterExpressionConnectorDropDownChoice.setChoices(connectors);
				filterExpressionConnectorDropDownChoice.setEnabled(true);
				patternOperatorDropDownChoice.setEnabled(false);
			} else {
				operators = PatternOperatorEnum.getUnaryOperators();
				patternOperatorDropDownChoice.setChoices(operators);
				filterExpressionConnectorDropDownChoice.setEnabled(false);
				patternOperatorDropDownChoice.setEnabled(true);
			}
			eventTypeDropDownChoice.setEnabled(false);
		} else if (numberOfSelectedElements == 2) {
			firstSelectedElement = patternTreeTable.getSelectedElements().iterator().next();
			if (firstSelectedElement instanceof FilterExpressionElement || firstSelectedElement instanceof FilterExpressionConnectorElement) {
				connectors = FilterExpressionConnectorEnum.getBinaryOperators();
				filterExpressionConnectorDropDownChoice.setChoices(connectors);
				filterExpressionConnectorDropDownChoice.setEnabled(true);
				patternOperatorDropDownChoice.setEnabled(false);
			} else {
				operators = PatternOperatorEnum.getBinaryOperators();
				patternOperatorDropDownChoice.setChoices(operators);
				filterExpressionConnectorDropDownChoice.setEnabled(false);
				patternOperatorDropDownChoice.setEnabled(true);
			}
			eventTypeDropDownChoice.setEnabled(false);
		} else {
			patternOperatorDropDownChoice.setEnabled(false);
			eventTypeDropDownChoice.setEnabled(false);
		}
		target.add(filterExpressionConnectorDropDownChoice);
		target.add(patternOperatorDropDownChoice);
		target.add(eventTypeDropDownChoice);
	}

	public void clear(AjaxRequestTarget target) {
		eventTypeDropDownChoice.setEnabled(true);
		patternOperatorDropDownChoice.setEnabled(false);
		filterExpressionConnectorDropDownChoice.setEnabled(false);
		target.add(eventTypeDropDownChoice);
		target.add(patternOperatorDropDownChoice);
		target.add(filterExpressionConnectorDropDownChoice);
		patternTree = new SushiPatternTree();
		patternTreeTableProvider.setRootElements(patternTree.getRoots());
		target.add(patternTreeTable);
	}
}
