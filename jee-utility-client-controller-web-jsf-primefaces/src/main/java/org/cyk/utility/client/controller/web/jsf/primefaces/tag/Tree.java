package org.cyk.utility.client.controller.web.jsf.primefaces.tag;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.cyk.utility.__kernel__.DependencyInjection;
import org.cyk.utility.__kernel__.object.dynamic.AbstractObject;
import org.cyk.utility.__kernel__.properties.Properties;
import org.cyk.utility.clazz.ClassHelper;
import org.cyk.utility.client.controller.ControllerEntity;
import org.cyk.utility.client.controller.ControllerLayer;
import org.cyk.utility.client.controller.data.hierarchy.DataIdentifiedByString;
import org.cyk.utility.client.controller.web.jsf.primefaces.PrimefacesHelper;
import org.cyk.utility.collection.CollectionHelper;
import org.cyk.utility.server.persistence.query.filter.FilterDto;
import org.cyk.utility.value.ValueUsageType;
import org.primefaces.event.NodeExpandEvent;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Tree<NODE extends DataIdentifiedByString<NODE>> extends AbstractObject implements Serializable {
	private static final long serialVersionUID = 1L;

	private TreeNode root;
	private String rootLabel = "Available";
	private Boolean selectable;
	private TreeSelectionMode selectionMode;
	private TreeNode selection;
	private TreeNode[] rootSelectedNodes;
	private String selectionLabel = "Selected";
	
	private Class<NODE> nodeClass;
	private Collection<NODE> nodesNotHavingParent,initialSelectedNodes;
	private ControllerEntity<NODE> controller;
	private String fields = "identifier,code,name,numberOfChildren";
	private String type;
	
	@SuppressWarnings("unchecked")
	public void initialise() {
		if(__inject__(ClassHelper.class).isInstanceOf(nodeClass, DataIdentifiedByString.class)) {
			this.controller = __inject__(ControllerLayer.class).injectInterfaceClassFromEntityClass(nodeClass);	
		}
		root = new TreeNode();
		if(nodesNotHavingParent == null)
			nodesNotHavingParent = controller.read(new Properties().setFilters(new FilterDto().addField(DataIdentifiedByString.PROPERTY_PARENTS, null))
				.setIsPageable(Boolean.FALSE).setFields(fields));
		
		if(__inject__(CollectionHelper.class).isNotEmpty(nodesNotHavingParent))
			for(NODE index : nodesNotHavingParent)
				new TreeNode(type, index, root);
		
		if(Boolean.TRUE.equals(selectable)) {
			if(selectionMode == null)
				selectionMode = TreeSelectionMode.REMOVE_ADD;
			
			if(TreeSelectionMode.REMOVE_ADD.equals(selectionMode)) {
				selection = new TreeNode();
				if(__inject__(CollectionHelper.class).isNotEmpty(initialSelectedNodes)) {
					//Expand nodes
					Collection<NODE> expandables = new ArrayList<>(initialSelectedNodes);
					do {
						Collection<TreeNode> treeNodes = new ArrayList<>();
						for(NODE index : expandables) {
							TreeNode treeNode = (TreeNode) DependencyInjection.inject(PrimefacesHelper.class).getTreeNodeOf(root, index);
							if(treeNode != null) {
								if(treeNode.getNumberOfChildren() > 0)
									expand(treeNode);
								treeNodes.add(treeNode);
							}
						}
						for(TreeNode index : treeNodes)
							expandables.remove(index.getData());
					}while(!expandables.isEmpty());
					//Select leaves nodes
					for(NODE index : initialSelectedNodes) {
						TreeNode treeNode = (TreeNode) DependencyInjection.inject(PrimefacesHelper.class).getTreeNodeOf(root, index);
						if(treeNode != null && !treeNode.isParentOfOneOrMoreData(initialSelectedNodes) /*&& treeNode.getChildCount() == 0*/) {		
							select((NODE) treeNode.getData());
						}
					}
				}
			}else if(TreeSelectionMode.CHECKBOX.equals(selectionMode)) {
				/*if(__inject__(CollectionHelper.class).isNotEmpty(selectedNodes)) {
					Collection<TreeNode> collection = new ArrayList<TreeNode>();
					for(NODE index : selectedNodes) {
						
					}
				}*/	
			}
		}
	}
	
	public Collection<NODE> getSelectedNodes(Integer isHavingNumberOfChildren) {
		Collection<NODE> selectedNodes = null;
		if(selection!=null) {
			selectedNodes = (Collection<NODE>) selection.getDatas(isHavingNumberOfChildren,nodeClass);
		}
		return selectedNodes;
	}
	
	public Collection<NODE> getSelectedNodes() {
		return getSelectedNodes(null);
	}
	
	@SuppressWarnings("unchecked")
	private void __select__(NODE node,org.primefaces.model.TreeNode source,org.primefaces.model.TreeNode destination,Boolean isConsiderTreeNodeNumberOfChildren,Boolean isConsiderTreeNodeNumberOfLoadedChildren) {
		if(source == null || destination == null) {
			__logWarning__("Tree source and destination must be not null");
			return;
		}
		//What is the tree node associated to this data(node)
		TreeNode treeNode = (TreeNode) __inject__(PrimefacesHelper.class).getTreeNodeOf(source, node);
		if(treeNode == null) {
			__logWarning__(String.format("Tree node not found for data %s",node));
			return;
		}
		node = (NODE) treeNode.getData();	
		org.primefaces.model.TreeNode destinationRoot = null;
		org.primefaces.model.TreeNode parent = treeNode.getParent();
		treeNode.removeFromParent(isConsiderTreeNodeNumberOfChildren,isConsiderTreeNodeNumberOfLoadedChildren);	
		if(parent != null) {
			List<Object> nodesToBeCreated = new ArrayList<>();
			org.primefaces.model.TreeNode indexDestinationRoot = parent;
			while(destinationRoot == null && indexDestinationRoot!=null && indexDestinationRoot.getData()!=null) {
				destinationRoot = __inject__(PrimefacesHelper.class).getTreeNodeOf(destination, indexDestinationRoot.getData());
				if(destinationRoot == null) {
					nodesToBeCreated.add(indexDestinationRoot.getData());
					indexDestinationRoot = indexDestinationRoot.getParent();	
				}
			}
			
			if(destinationRoot == null) {
				destinationRoot = destination;
			}else {
				
			}
			
			Collections.reverse(nodesToBeCreated);
			for(Object index : nodesToBeCreated) {
				destinationRoot = new TreeNode(type, index, destinationRoot);
				destinationRoot.setExpanded(Boolean.TRUE);
			}
			destinationRoot = new TreeNode(type, node,null, destinationRoot);
		}
		
	}
	
	public void select(Collection<NODE> nodes) {
		for(NODE index : nodes) {
			__select__(index, root,selection, Boolean.TRUE, Boolean.FALSE);
		}
	}

	public void select(@SuppressWarnings("unchecked") NODE...nodes) {
		select(__inject__(CollectionHelper.class).instanciate(nodes));
	}
	
	@SuppressWarnings("unchecked")
	public void selectOne(NODE node) {
		select(node);
	}
	
	public void deselect(Collection<NODE> nodes) {
		for(NODE index : nodes) {
			__select__(index, selection,root, Boolean.FALSE, Boolean.TRUE);	
		}
	}
	
	public void deselect(@SuppressWarnings("unchecked") NODE...nodes) {
		deselect(__inject__(CollectionHelper.class).instanciate(nodes));
	}
	
	@SuppressWarnings("unchecked")
	public void deselectOne(NODE node) {
		deselect(node);
	}
	
	/* Event */
	
	@SuppressWarnings("unchecked")
	public void listenExpand(NodeExpandEvent event) {
		expand((NODE)event.getTreeNode().getData());
    }
	
	public void expand(TreeNode treeNode) {
		treeNode.setExpanded(Boolean.TRUE);
		@SuppressWarnings("unchecked")
		NODE node = (NODE) treeNode.getData();
		Collection<NODE> children = controller.read(new Properties().setFilters(new FilterDto().addField("parents"
				, Arrays.asList(node.getIdentifier()),ValueUsageType.SYSTEM)).setIsPageable(Boolean.FALSE).setFields(fields));
		if(__inject__(CollectionHelper.class).isNotEmpty(children))
			for(NODE index : children)
				new TreeNode(type, index, treeNode);
    }
	
	public void expand(NODE node) {
		expand((TreeNode) node.getProperties().getTreeNode());
    }
	
}
