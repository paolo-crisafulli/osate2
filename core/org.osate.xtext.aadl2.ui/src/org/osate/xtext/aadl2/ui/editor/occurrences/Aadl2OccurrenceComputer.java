/*******************************************************************************
 * Copyright (c) 2011 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.osate.xtext.aadl2.ui.editor.occurrences;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMapWithExpectedSize;
import static java.util.Collections.emptyMap;

import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.source.Annotation;
import org.eclipse.xtext.findReferences.IReferenceFinder;
import org.eclipse.xtext.findReferences.TargetURIs;
import org.eclipse.xtext.resource.EObjectAtOffsetHelper;
import org.eclipse.xtext.resource.ILocationInFileProvider;
import org.eclipse.xtext.resource.IReferenceDescription;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.service.OperationCanceledManager;
import org.eclipse.xtext.ui.editor.XtextEditor;
import org.eclipse.xtext.ui.editor.model.IXtextDocument;
import org.eclipse.xtext.ui.editor.occurrences.DefaultOccurrenceComputer;
import org.eclipse.xtext.util.CancelIndicator;
import org.eclipse.xtext.util.ITextRegion;
import org.eclipse.xtext.util.TextRegion;
import org.eclipse.xtext.util.concurrent.CancelableUnitOfWork;
import org.osate.aadl2.ComponentImplementation;
import org.osate.aadl2.Realization;
import org.osate.xtext.aadl2.util.Aadl2LocationInFile;

import com.google.inject.Inject;
import com.google.inject.Provider;

@SuppressWarnings("restriction")
public class Aadl2OccurrenceComputer extends DefaultOccurrenceComputer {

	@Inject
	private EObjectAtOffsetHelper eObjectAtOffsetHelper;

	@Inject
	private ILocationInFileProvider locationInFileProvider;

	@Inject
	private IReferenceFinder referenceFinder;

	@Inject
	private Provider<TargetURIs> targetURIsProvider;

	@Inject
	private OperationCanceledManager operationCanceledManager;

	private static class EObjectReferenceAndIndex {
		EObject source;
		EReference reference;
		int idx;
	}

	@Override
	public Map<Annotation, Position> createAnnotationMap(XtextEditor editor, final ITextSelection selection,
			final SubMonitor monitor) {
		final IXtextDocument document = editor.getDocument();
		if (document != null) {
			return document.readOnly(new CancelableUnitOfWork<Map<Annotation, Position>, XtextResource>() {

				@Override
				public Map<Annotation, Position> exec(XtextResource resource, final CancelIndicator cancelIndicator)
						throws Exception {
					if (resource != null) {
						EObject target = eObjectAtOffsetHelper.resolveElementAt(resource, selection.getOffset());
						return getAnnotations(target, resource, document, monitor, cancelIndicator);
					}
					return emptyMap();
				}
			});
		} else {
			return emptyMap();
		}
	}

	protected Map<Annotation, Position> getAnnotations(EObject target, XtextResource resource,
			final IXtextDocument document, final SubMonitor monitor, final CancelIndicator cancelIndicator) {
		if (target != null && !target.eIsProxy()) {
			final List<EObjectReferenceAndIndex> references = newArrayList();
			IReferenceFinder.Acceptor acceptor = new IReferenceFinder.Acceptor() {
				@Override
				public void accept(IReferenceDescription reference) {
					throw new UnsupportedOperationException("Local references are announced per object");
				}

				@Override
				public void accept(EObject source, URI sourceURI, EReference eReference, int index,
						EObject targetOrProxy, URI targetURI) {
					EObjectReferenceAndIndex acceptMe = new EObjectReferenceAndIndex();
					acceptMe.source = source;
					acceptMe.reference = eReference;
					acceptMe.idx = index;
					references.add(acceptMe);
				}
			};
			Iterable<URI> targetURIs = getTargetURIs(target);
			if (!(targetURIs instanceof TargetURIs)) {
				TargetURIs result = targetURIsProvider.get();
				result.addAllURIs(targetURIs);
				targetURIs = result;
			}
			IProgressMonitor localMonitor = new NullProgressMonitor() {
				@Override
				public boolean isCanceled() {
					return monitor.isCanceled() || cancelIndicator.isCanceled();
				}
			};
			referenceFinder.findReferences((TargetURIs) targetURIs, resource, acceptor, localMonitor);
			operationCanceledManager.checkCanceled(cancelIndicator);
			Map<Annotation, Position> result = newHashMapWithExpectedSize(references.size() + 1);
			if (target.eResource() == resource) {
				if (!references.isEmpty() || canBeReferencedLocally(target)) {
					ITextRegion declarationRegion = locationInFileProvider.getSignificantTextRegion(target);
					addOccurrenceAnnotation(DECLARATION_ANNOTATION_TYPE, document, declarationRegion, result);
					declarationRegion = ((Aadl2LocationInFile) locationInFileProvider).getSecondaryTextRegion(target,
							!(target instanceof ComponentImplementation));
					if (declarationRegion != null) {
						addOccurrenceAnnotation(DECLARATION_ANNOTATION_TYPE, document, declarationRegion, result);
					}
				}
			}
			for (EObjectReferenceAndIndex highlightMe : references) {
				try {
					if (localMonitor.isCanceled()) {
						return emptyMap();
					}
					ITextRegion textRegion = locationInFileProvider.getSignificantTextRegion(highlightMe.source,
							highlightMe.reference, highlightMe.idx);
					if (target instanceof ComponentImplementation) {
						int l = ((ComponentImplementation) target).getTypeName().length() + 1;
						ITextRegion implNameRegion = new TextRegion(
								textRegion.getOffset() + l, textRegion.getLength() - l);
						addOccurrenceAnnotation(OCCURRENCE_ANNOTATION_TYPE, document, implNameRegion, result);
					} else {
						addOccurrenceAnnotation(OCCURRENCE_ANNOTATION_TYPE, document, textRegion, result);
					}
					addIndirectAnnotations(highlightMe, resource, result, document, monitor, cancelIndicator);
				} catch (Exception exc) {
					// outdated index information. Ignore
				}
			}
			return result;
		} else {
			return emptyMap();
		}
	}

	protected void addIndirectAnnotations(EObjectReferenceAndIndex highlightMe, XtextResource resource,
			Map<Annotation, Position> result, final IXtextDocument document, final SubMonitor monitor,
			final CancelIndicator cancelIndicator) {
		try {
			if (monitor.isCanceled()) {
				return;
			}
			if (highlightMe.source instanceof Realization) {
				EObject obj = highlightMe.source.eContainer();
				if (obj instanceof ComponentImplementation) {
					Map<Annotation, Position> newAnnotations = getImplementationAnnotations(
							(ComponentImplementation) obj, resource, document, monitor, cancelIndicator);
					result.putAll(newAnnotations);
				}
			}
		} catch (Exception exc) {
			// outdated index information. Ignore
		}
	}

	protected Map<Annotation, Position> getImplementationAnnotations(ComponentImplementation target,
			XtextResource resource, final IXtextDocument document, final SubMonitor monitor,
			final CancelIndicator cancelIndicator) {
		if (target != null && !target.eIsProxy()) {
			final List<EObjectReferenceAndIndex> references = newArrayList();
			IReferenceFinder.Acceptor acceptor = new IReferenceFinder.Acceptor() {
				@Override
				public void accept(IReferenceDescription reference) {
					throw new UnsupportedOperationException("Local references are announced per object");
				}

				@Override
				public void accept(EObject source, URI sourceURI, EReference eReference, int index,
						EObject targetOrProxy, URI targetURI) {
					EObjectReferenceAndIndex acceptMe = new EObjectReferenceAndIndex();
					acceptMe.source = source;
					acceptMe.reference = eReference;
					acceptMe.idx = index;
					references.add(acceptMe);
				}
			};
			Iterable<URI> targetURIs = getTargetURIs(target);
			if (!(targetURIs instanceof TargetURIs)) {
				TargetURIs result = targetURIsProvider.get();
				result.addAllURIs(targetURIs);
				targetURIs = result;
			}
			IProgressMonitor localMonitor = new NullProgressMonitor() {
				@Override
				public boolean isCanceled() {
					return monitor.isCanceled() || cancelIndicator.isCanceled();
				}
			};
			referenceFinder.findReferences((TargetURIs) targetURIs, resource, acceptor, localMonitor);
			operationCanceledManager.checkCanceled(cancelIndicator);
			Map<Annotation, Position> result = newHashMapWithExpectedSize(references.size() + 1);
			if (target.eResource() == resource) {
				if (!references.isEmpty() || canBeReferencedLocally(target)) {
					ITextRegion declarationRegion = ((Aadl2LocationInFile) locationInFileProvider)
							.getSecondaryTextRegion(target, true);
					if (declarationRegion != null) {
						addOccurrenceAnnotation(OCCURRENCE_ANNOTATION_TYPE, document, declarationRegion, result);
					}
				}
			}
			for (EObjectReferenceAndIndex highlightMe : references) {
				try {
					if (localMonitor.isCanceled()) {
						return emptyMap();
					}
					ITextRegion textRegion = locationInFileProvider.getSignificantTextRegion(highlightMe.source,
							highlightMe.reference, highlightMe.idx);
					ITextRegion typeNameRegion = new TextRegion(textRegion.getOffset(), target.getTypeName().length());
					addOccurrenceAnnotation(OCCURRENCE_ANNOTATION_TYPE, document, typeNameRegion, result);
					addIndirectAnnotations(highlightMe, resource, result, document, monitor, cancelIndicator);
				} catch (Exception exc) {
					// outdated index information. Ignore
				}
			}
			return result;
		} else {
			return emptyMap();
		}
	}

}
