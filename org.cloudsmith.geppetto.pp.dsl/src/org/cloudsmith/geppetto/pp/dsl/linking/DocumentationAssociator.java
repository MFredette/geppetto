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
package org.cloudsmith.geppetto.pp.dsl.linking;

import java.util.List;

import org.cloudsmith.geppetto.pp.Definition;
import org.cloudsmith.geppetto.pp.HostClassDefinition;
import org.cloudsmith.geppetto.pp.NodeDefinition;
import org.cloudsmith.geppetto.pp.dsl.adapters.DocumentationAdapter;
import org.cloudsmith.geppetto.pp.dsl.adapters.DocumentationAdapterFactory;
import org.cloudsmith.geppetto.pp.dsl.services.PPGrammarAccess;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.IGrammarAccess;
import org.eclipse.xtext.nodemodel.ICompositeNode;
import org.eclipse.xtext.nodemodel.INode;
import org.eclipse.xtext.nodemodel.util.NodeModelUtils;

import com.google.common.collect.Lists;
import com.google.inject.Inject;

/**
 * Provides handling of documentation comments.
 * 
 */
public class DocumentationAssociator {
	private final PPGrammarAccess ga;

	/**
	 * Expression that may have associated documentation. (TODO: Puppetdoc also lists Nodes global variables, custom facts, and
	 * Puppet plugins located in modules - but don't know which of those are applicable).
	 */
	private static final Class<?>[] documentable = { HostClassDefinition.class, Definition.class, NodeDefinition.class, };

	@Inject
	public DocumentationAssociator(IGrammarAccess ga) {
		this.ga = (PPGrammarAccess) ga;
	}

	private void associateDocumentation(EObject semantic, List<INode> commentSequence) {
		StringBuffer buf = new StringBuffer();
		for(INode n : commentSequence)
			buf.append(n.getText());

		DocumentationAdapter adapter = DocumentationAdapterFactory.eINSTANCE.adapt(semantic);
		adapter.setNodes(commentSequence);

	}

	/**
	 * Links comment nodes to classes listed in {@link #documentable} by collecting them in an
	 * adapter (for later processing by formatter/styler).
	 * 
	 * TODO: provide checks that documentation is consistent with the model
	 */
	protected void linkDocumentation(EObject model, IMessageAcceptor acceptor) {

		// a sequence of SL comment or a single ML comment that is immediately (no NL) before
		// a definition, class, or node is taken to be a documentation comment, as is associated with
		// the following semantic object using an adapter.
		//
		ICompositeNode node = NodeModelUtils.getNode(model);
		ICompositeNode root = node.getRootNode();
		List<INode> commentSequence = Lists.newArrayList();
		for(INode x : root.getAsTreeIterable()) {
			EObject grammarElement = x.getGrammarElement();
			// process comments
			if(grammarElement == ga.getSL_COMMENTRule() || grammarElement == ga.getML_COMMENTRule()) {
				// if nothing follows the comment (we are probably at the end)
				if(!x.hasNextSibling()) {
					commentSequence.clear();
					continue;
				}
				// if next is a blank line, throw away any collected comments.
				INode sibling = x.getNextSibling();
				if(sibling.getGrammarElement() == ga.getWSRule() && sibling.getText().contains("\n")) {
					commentSequence.clear();
					continue;
				}
				// if adding a ML comment, use only the last
				if(grammarElement == ga.getML_COMMENTRule())
					commentSequence.clear();
				commentSequence.add(x);

				// if next is not a comment, it may be an element that the documentation should be associated with
				EObject siblingElement = sibling.getGrammarElement();
				if(siblingElement == ga.getSL_COMMENTRule() || siblingElement == ga.getML_COMMENTRule())
					continue; // keep on collecting

				EObject semantic = NodeModelUtils.findActualSemanticObjectFor(sibling);
				found: {
					for(Class<?> clazz : documentable) {
						if(clazz.isAssignableFrom(semantic.getClass())) {
							// found sequence is documentation for semantic
							associateDocumentation(semantic, commentSequence);
							break found;
						}
					}
					commentSequence.clear();
				}
			}
		}

	}
}