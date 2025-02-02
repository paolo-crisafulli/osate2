package org.osate.ge.internal.util;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.xtext.ui.resource.XtextLiveScopeResourceSetProvider;
import org.osate.ge.EmfContainerProvider;
import org.osate.ge.internal.ui.util.SelectionUtil;
import org.osate.xtext.aadl2.ui.internal.Aadl2Activator;

import com.google.inject.Injector;

public class ProjectUtil {
	/**
	 * Get affects projects. At this point, this function returns projects which directly or indirectly reference the project containing the model element.
	 * @param project
	 * @param relevantProjects
	 * @return relevantProjects
	 */
	public static Set<IProject> getAffectedProjects(final IProject project, final Set<IProject> relevantProjects) {
		if (project.isAccessible()) {
			if (relevantProjects.add(project)) {
				// Get referencing projects if the project was not already part of the relevant projects set
				for (final IProject referencingProject : project.getReferencingProjects()) {
					getAffectedProjects(referencingProject, relevantProjects);
				}

				// Get referencing projects if the project was not already part of the relevant projects set
				try {
					for (final IProject referencedProject : project.getReferencedProjects()) {
						getAffectedProjects(referencedProject, relevantProjects);
					}
				} catch (final CoreException e) {
					// Ignore
				}
			}
		}

		return relevantProjects;
	}

	public static IProject getProject(final URI elementUri) {
		final IPath projectPath = new Path(elementUri.toPlatformString(true)).uptoSegment(1);
		final IResource projectResource = ResourcesPlugin.getWorkspace().getRoot().findMember(projectPath);
		if (!(projectResource instanceof IProject)) {
			return null;
		}
		return (IProject) projectResource;
	}

	public static IProject getProjectForBoOrThrow(final Object bo) {
		return getResource(bo).flatMap(r -> Optional.ofNullable(r.getURI())).flatMap(SelectionUtil::getProject).orElseThrow(() -> new RuntimeException("Unable to get project for business object: " + bo));
	}

	public static Optional<IProject> getProjectForBo(final Object bo) {
		return getResource(bo).flatMap(r -> Optional.ofNullable(r.getURI())).flatMap(SelectionUtil::getProject);
	}

	private static Optional<Resource> getResource(final Object bo) {
		final EObject eObject;

		// Handle EObject instances without delegating to specialized handlers
		if (bo instanceof EObject) {
			eObject = (EObject) bo;
		} else if (bo instanceof EmfContainerProvider) { // Use the EMF Object container if the business object is not an EMF Object
			final EObject container = ((EmfContainerProvider) bo).getEmfContainer();
			if (container == null) {
				return Optional.empty();
			}

			eObject = container;
		} else {
			return Optional.empty();
		}

		return Optional.ofNullable(eObject.eResource());
	}

	/**
	 * Returns a live resource set based on the project or throws an exception if one cannot be returned.
	 * @param project
	 * @return
	 */
	public static ResourceSet getLiveResourceSet(IProject project) {
		final Injector injector = Objects.requireNonNull(
				Aadl2Activator.getInstance().getInjector(Aadl2Activator.ORG_OSATE_XTEXT_AADL2_AADL2),
				"Unable to retrieve injector");
		final XtextLiveScopeResourceSetProvider liveResourceSetProvider = Objects.requireNonNull(
				injector.getInstance(XtextLiveScopeResourceSetProvider.class),
				"Unable to retrieve live scope resource set provider");

		return Objects.requireNonNull(liveResourceSetProvider.get(project), "Unable to get live resource set");
	}
}
