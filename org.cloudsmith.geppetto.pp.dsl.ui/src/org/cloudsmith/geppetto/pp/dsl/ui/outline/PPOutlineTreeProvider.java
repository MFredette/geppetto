/**
 * Copyright (c) 2011 Cloudsmith Inc. and other contributors, as listed below.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *   Cloudsmith
 * 
 */
/*
 * generated by Xtext
 */
package org.cloudsmith.geppetto.pp.dsl.ui.outline;

import java.util.List;

import org.cloudsmith.geppetto.pp.AppendExpression;
import org.cloudsmith.geppetto.pp.Definition;
import org.cloudsmith.geppetto.pp.Expression;
import org.cloudsmith.geppetto.pp.HostClassDefinition;
import org.cloudsmith.geppetto.pp.ImportExpression;
import org.cloudsmith.geppetto.pp.NodeDefinition;
import org.cloudsmith.geppetto.pp.PuppetManifest;
import org.cloudsmith.geppetto.pp.ResourceBody;
import org.cloudsmith.geppetto.pp.ResourceExpression;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.xtext.ui.editor.outline.IOutlineNode;
import org.eclipse.xtext.ui.editor.outline.impl.DefaultOutlineTreeProvider;

import com.google.common.collect.Lists;

/**
 * customization of the default outline structure
 * 
 */
public class PPOutlineTreeProvider extends DefaultOutlineTreeProvider {
	// @Inject
	// // @OutlineLabelProvider
	// private ILabelProvider labelProvider;

	// private static List<EObject> NO_CHILDREN = null; // TODO: HACK WHILE MIGRATING TO XTEXT 2.0

	// // This helper is injected as a private helper in AbstractLabelProvider, but is difficult
	// // to use when mapping structural features.
	// @Inject
	// private IImageHelper imageHelper;

	private static final Class<?>[] structuralClasses = {
			HostClassDefinition.class, Definition.class, NodeDefinition.class, ResourceExpression.class,
			AppendExpression.class, ImportExpression.class };

	protected void _createChildren(IOutlineNode parentNode, Definition modelElement) {
		for(EObject childElement : getRelevantChildren(modelElement.getStatements()))
			createNode(parentNode, childElement);
	}

	protected void _createChildren(IOutlineNode parentNode, HostClassDefinition modelElement) {
		for(EObject childElement : getRelevantChildren(modelElement.getStatements()))
			createNode(parentNode, childElement);
	}

	protected void _createChildren(IOutlineNode parentNode, ImportExpression modelElement) {
		for(EObject childElement : modelElement.getValues())
			createNode(parentNode, childElement);
	}

	protected void _createChildren(IOutlineNode parentNode, NodeDefinition modelElement) {
		for(EObject childElement : getRelevantChildren(modelElement.getStatements()))
			createNode(parentNode, childElement);
	}

	protected void _createChildren(IOutlineNode parentNode, PuppetManifest modelElement) {
		for(EObject childElement : getRelevantChildren(modelElement.getStatements()))
			createNode(parentNode, childElement);
	}

	protected void _createChildren(IOutlineNode parentNode, ResourceExpression modelElement) {
		for(EObject childElement : modelElement.getResourceData())
			createNode(parentNode, childElement);
	}

	protected boolean _isLeaf(Definition modelElement) {
		return false;
	}

	/**
	 * protected List<EObject> getChildren(EObject ele) {
	 * return NO_CHILDREN;
	 * }
	 */
	@Override
	protected boolean _isLeaf(EObject ele) {
		return true;
	}

	protected boolean _isLeaf(HostClassDefinition modelElement) {
		return false;
	}

	protected boolean _isLeaf(ImportExpression ele) {
		// a single import is represented by the import expression expression
		return (ele.getValues().size() <= 1);
	}

	protected boolean _isLeaf(NodeDefinition modelElement) {
		return false;
	}

	protected boolean _isLeaf(PuppetManifest ele) {
		return false;
	}

	/**
	 * protected List<EObject> getChildren(ResourceBody ele) {
	 * return NO_CHILDREN;
	 * }
	 */
	protected boolean _isLeaf(ResourceBody ele) {
		return true;
	}

	protected boolean _isLeaf(ResourceExpression ele) {
		return ele.getResourceData().size() <= 1;
	}

	protected List<EObject> getRelevantChildren(List<Expression> ele) {
		List<EObject> result = Lists.newArrayList();
		TreeIterator<Object> sitor = EcoreUtil.getAllContents(ele, true);
		while(sitor.hasNext()) {
			Object o = sitor.next();
			if(hasRelevantClass(o)) {
				result.add((EObject) o);
				sitor.prune();
			}
		}
		return result;
	}

	private boolean hasRelevantClass(Object o) {
		int limit = structuralClasses.length;
		for(int i = 0; i < limit; i++)
			if(structuralClasses[i].isAssignableFrom(o.getClass()))
				return true;
		return false;
	}

}