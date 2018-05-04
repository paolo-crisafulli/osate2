package org.osate.ui.wizards;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.IExportWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.dialogs.WizardExportResourcesPage;
import org.osate.aadl2.AadlPackage;
import org.osate.aadl2.Classifier;
import org.osate.aadl2.Element;
import org.osate.aadl2.Feature;
import org.osate.aadl2.modelsupport.modeltraversal.AadlProcessingSwitchWithProgress;
import org.osate.aadl2.modelsupport.resources.OsateResourceUtil;
import org.osate.aadl2.util.Aadl2Switch;

import com.google.common.collect.ListMultimap;
import com.google.common.collect.MultimapBuilder;

public class ExportDocumentationWizard extends Wizard implements IExportWizard {
	private ExportDocsPage1 thePage;

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		thePage = new ExportDocsPage1("Export AADLDoc", selection);
		addPage(thePage);
	}

	@Override
	public boolean performFinish() {
		AadlDocGenerator adg = new AadlDocGenerator(new NullProgressMonitor());
		thePage.getFilesToExport()
		.forEachRemaining(file -> getElements(file).forEach(elem -> adg.process((Element) elem)));
		renderRst(adg);
		return true;
	}

	private void renderRst(AadlDocGenerator adg) {
		StringBuilder sb = new StringBuilder();
		sb.append(".. include:: ../util/substitution.rst\n");
		sb.append(".. default-domain:: aadl\n\n");
		sb.append("######################\n");
		sb.append("Package: ");
		sb.append(adg.packageName);
		sb.append("\n######################\n");
		sb.append("\n\nPackage-level documentation goes here.");
		for (String classifierName : adg.classifiers.keySet()) {
			sb.append("\n\n.. classifier:: ");
			sb.append(classifierName);
			sb.append("\n\n\tClassifier-level documentation goes here");
			for(String subElementType : adg.classifiers.get(classifierName).keySet()) {
				sb.append("\n\n\t");
				for(String subElement : adg.classifiers.get(classifierName).get(subElementType)) {
					sb.append(":");
					sb.append(subElementType);
					sb.append(" ?Port?:");
					sb.append(subElement);
					sb.append("\n\t");
				}
			}
		}
		System.out.println(sb.toString());
	}

	private List<EObject> getElements(IResource iRes) {
		ResourceSet rs = OsateResourceUtil.createResourceSet();
		Resource res = rs.getResource(OsateResourceUtil.getResourceURI(iRes), true);
		return res.getContents();
	}

	private class AadlDocGenerator extends AadlProcessingSwitchWithProgress {

		private String packageName;
		private String currentClassifier;

		/**
		 * Maps Classifier name to type to name, eg, MySystem -> feature -> myPort
		 *
		 * Ordering of systems is not preserved. Ordering of subelements
		 * (features, connections, etc.) and ordering within subelements is
		 * preserved.
		 */
		public Map<String, ListMultimap<String, String>> classifiers = new HashMap<>();

		// Temp bag for holding current classifiers sub-elements
		private ListMultimap<String, String> children;

		protected AadlDocGenerator(IProgressMonitor pm) {
			super(pm);
			// TODO Auto-generated constructor stub
		}

		@Override
		protected void initSwitches() {
			aadl2Switch = new AadlDocSwitch();
		}

		public class AadlDocSwitch extends Aadl2Switch<String> {
			@Override
			public String caseAadlPackage(AadlPackage obj) {
				packageName = obj.getQualifiedName();
				processEList(obj.getOwnedPublicSection().getChildren());
				return NOT_DONE;
			}

			@Override
			public String caseClassifier(Classifier obj) {
				children = MultimapBuilder.linkedHashKeys().linkedListValues().build();
				classifiers.put(obj.getFullName(), children);
				processEList(obj.getChildren());
				return NOT_DONE;
			}

			@Override
			public String caseFeature(Feature obj) {
				children.put("Feature", obj.getFullName());
				return NOT_DONE;
			}
		}
	}

	private class ExportDocsPage1 extends WizardExportResourcesPage {

		protected ExportDocsPage1(String pageName, IStructuredSelection selection) {
			super(pageName, selection);
			// TODO Auto-generated constructor stub
		}

		@SuppressWarnings("unchecked") /* superclass specifies IResource in JavaDoc */
		public Iterator<IResource> getFilesToExport() {
			return getSelectedResourcesIterator();
		}

		@Override
		public void handleEvent(Event event) {
			// TODO Auto-generated method stub

		}

		@Override
		protected void createDestinationGroup(Composite parent) {
			// TODO Auto-generated method stub

		}

	}
}
