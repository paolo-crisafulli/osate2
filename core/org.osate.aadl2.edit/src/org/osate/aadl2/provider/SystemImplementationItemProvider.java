/**
 * <copyright>
 * Copyright  2008 by Carnegie Mellon University, all rights reserved.
 *
 * Use of the Open Source AADL Tool Environment (OSATE) is subject to the terms of the license set forth
 * at http://www.eclipse.org/org/documents/epl-v10.html.
 *
 * NO WARRANTY
 *
 * ANY INFORMATION, MATERIALS, SERVICES, INTELLECTUAL PROPERTY OR OTHER PROPERTY OR RIGHTS GRANTED OR PROVIDED BY
 * CARNEGIE MELLON UNIVERSITY PURSUANT TO THIS LICENSE (HEREINAFTER THE "DELIVERABLES") ARE ON AN "AS-IS" BASIS.
 * CARNEGIE MELLON UNIVERSITY MAKES NO WARRANTIES OF ANY KIND, EITHER EXPRESS OR IMPLIED AS TO ANY MATTER INCLUDING,
 * BUT NOT LIMITED TO, WARRANTY OF FITNESS FOR A PARTICULAR PURPOSE, MERCHANTABILITY, INFORMATIONAL CONTENT,
 * NONINFRINGEMENT, OR ERROR-FREE OPERATION. CARNEGIE MELLON UNIVERSITY SHALL NOT BE LIABLE FOR INDIRECT, SPECIAL OR
 * CONSEQUENTIAL DAMAGES, SUCH AS LOSS OF PROFITS OR INABILITY TO USE SAID INTELLECTUAL PROPERTY, UNDER THIS LICENSE,
 * REGARDLESS OF WHETHER SUCH PARTY WAS AWARE OF THE POSSIBILITY OF SUCH DAMAGES. LICENSEE AGREES THAT IT WILL NOT
 * MAKE ANY WARRANTY ON BEHALF OF CARNEGIE MELLON UNIVERSITY, EXPRESS OR IMPLIED, TO ANY PERSON CONCERNING THE
 * APPLICATION OF OR THE RESULTS TO BE OBTAINED WITH THE DELIVERABLES UNDER THIS LICENSE.
 *
 * Licensee hereby agrees to defend, indemnify, and hold harmless Carnegie Mellon University, its trustees, officers,
 * employees, and agents from all claims or demands made against them (and any related losses, expenses, or
 * attorney's fees) arising out of, or relating to Licensee's and/or its sub licensees' negligent use or willful
 * misuse of or negligent conduct or willful misconduct regarding the Software, facilities, or other rights or
 * assistance granted by Carnegie Mellon University under this License, including, but not limited to, any claims of
 * product liability, personal injury, death, damage to property, or violation of any laws or regulations.
 *
 * Carnegie Mellon University Software Engineering Institute authored documents are sponsored by the U.S. Department
 * of Defense under Contract F19628-00-C-0003. Carnegie Mellon University retains copyrights in all material produced
 * under this contract. The U.S. Government retains a non-exclusive, royalty-free license to publish or reproduce these
 * documents, or allow others to do so, for U.S. Government purposes only pursuant to the copyright license
 * under the contract clause at 252.227.7013.
 * </copyright>
 *
 * $Id: SystemImplementationItemProvider.java,v 1.23 2011-04-11 13:36:08 lwrage Exp $
 */
package org.osate.aadl2.provider;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ViewerNotification;
import org.osate.aadl2.Aadl2Factory;
import org.osate.aadl2.Aadl2Package;
import org.osate.aadl2.SystemImplementation;

/**
 * This is the item provider adapter for a {@link org.osate.aadl2.SystemImplementation} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class SystemImplementationItemProvider extends ComponentImplementationItemProvider {
	/**
	 * This constructs an instance from a factory and a notifier.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SystemImplementationItemProvider(AdapterFactory adapterFactory) {
		super(adapterFactory);
	}

	/**
	 * This returns the property descriptors for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public List<IItemPropertyDescriptor> getPropertyDescriptors(Object object) {
		if (itemPropertyDescriptors == null) {
			super.getPropertyDescriptors(object);

		}
		return itemPropertyDescriptors;
	}

	/**
	 * This specifies how to implement {@link #getChildren} and is used to deduce an appropriate feature for an
	 * {@link org.eclipse.emf.edit.command.AddCommand}, {@link org.eclipse.emf.edit.command.RemoveCommand} or
	 * {@link org.eclipse.emf.edit.command.MoveCommand} in {@link #createCommand}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Collection<? extends EStructuralFeature> getChildrenFeatures(Object object) {
		if (childrenFeatures == null) {
			super.getChildrenFeatures(object);
			childrenFeatures.add(Aadl2Package.eINSTANCE.getSystemImplementation_OwnedBusSubcomponent());
			childrenFeatures.add(Aadl2Package.eINSTANCE.getSystemImplementation_OwnedDataSubcomponent());
			childrenFeatures.add(Aadl2Package.eINSTANCE.getSystemImplementation_OwnedDeviceSubcomponent());
			childrenFeatures.add(Aadl2Package.eINSTANCE.getSystemImplementation_OwnedMemorySubcomponent());
			childrenFeatures.add(Aadl2Package.eINSTANCE.getSystemImplementation_OwnedProcessSubcomponent());
			childrenFeatures.add(Aadl2Package.eINSTANCE.getSystemImplementation_OwnedProcessorSubcomponent());
			childrenFeatures.add(Aadl2Package.eINSTANCE.getSystemImplementation_OwnedSubprogramSubcomponent());
			childrenFeatures.add(Aadl2Package.eINSTANCE.getSystemImplementation_OwnedSubprogramGroupSubcomponent());
			childrenFeatures.add(Aadl2Package.eINSTANCE.getSystemImplementation_OwnedSystemSubcomponent());
			childrenFeatures.add(Aadl2Package.eINSTANCE.getSystemImplementation_OwnedVirtualBusSubcomponent());
			childrenFeatures.add(Aadl2Package.eINSTANCE.getSystemImplementation_OwnedVirtualProcessorSubcomponent());
		}
		return childrenFeatures;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EStructuralFeature getChildFeature(Object object, Object child) {
		// Check the type of the specified child object and return the proper feature to use for
		// adding (see {@link AddCommand}) it as a child.

		return super.getChildFeature(object, child);
	}

	/**
	 * This returns SystemImplementation.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object getImage(Object object) {
		return overlayImage(object, getResourceLocator().getImage("full/obj16/SystemImplementation"));
	}

	/**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getText(Object object) {
		String label = ((SystemImplementation) object).getName();
		return label == null || label.length() == 0 ? getString("_UI_SystemImplementation_type")
				: getString("_UI_SystemImplementation_type") + " " + label;
	}

	/**
	 * This handles model notifications by calling {@link #updateChildren} to update any cached
	 * children and by creating a viewer notification, which it passes to {@link #fireNotifyChanged}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void notifyChanged(Notification notification) {
		updateChildren(notification);

		switch (notification.getFeatureID(SystemImplementation.class)) {
		case Aadl2Package.SYSTEM_IMPLEMENTATION__OWNED_BUS_SUBCOMPONENT:
		case Aadl2Package.SYSTEM_IMPLEMENTATION__OWNED_DATA_SUBCOMPONENT:
		case Aadl2Package.SYSTEM_IMPLEMENTATION__OWNED_DEVICE_SUBCOMPONENT:
		case Aadl2Package.SYSTEM_IMPLEMENTATION__OWNED_MEMORY_SUBCOMPONENT:
		case Aadl2Package.SYSTEM_IMPLEMENTATION__OWNED_PROCESS_SUBCOMPONENT:
		case Aadl2Package.SYSTEM_IMPLEMENTATION__OWNED_PROCESSOR_SUBCOMPONENT:
		case Aadl2Package.SYSTEM_IMPLEMENTATION__OWNED_SUBPROGRAM_SUBCOMPONENT:
		case Aadl2Package.SYSTEM_IMPLEMENTATION__OWNED_SUBPROGRAM_GROUP_SUBCOMPONENT:
		case Aadl2Package.SYSTEM_IMPLEMENTATION__OWNED_SYSTEM_SUBCOMPONENT:
		case Aadl2Package.SYSTEM_IMPLEMENTATION__OWNED_VIRTUAL_BUS_SUBCOMPONENT:
		case Aadl2Package.SYSTEM_IMPLEMENTATION__OWNED_VIRTUAL_PROCESSOR_SUBCOMPONENT:
			fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), true, false));
			return;
		}
		super.notifyChanged(notification);
	}

	/**
	 * This adds {@link org.eclipse.emf.edit.command.CommandParameter}s describing the children
	 * that can be created under this object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected void collectNewChildDescriptors(Collection<Object> newChildDescriptors, Object object) {
		super.collectNewChildDescriptors(newChildDescriptors, object);

		newChildDescriptors
				.add(createChildParameter(Aadl2Package.eINSTANCE.getSystemImplementation_OwnedBusSubcomponent(),
						Aadl2Factory.eINSTANCE.createBusSubcomponent()));

		newChildDescriptors
				.add(createChildParameter(Aadl2Package.eINSTANCE.getSystemImplementation_OwnedDataSubcomponent(),
						Aadl2Factory.eINSTANCE.createDataSubcomponent()));

		newChildDescriptors
				.add(createChildParameter(Aadl2Package.eINSTANCE.getSystemImplementation_OwnedDeviceSubcomponent(),
						Aadl2Factory.eINSTANCE.createDeviceSubcomponent()));

		newChildDescriptors
				.add(createChildParameter(Aadl2Package.eINSTANCE.getSystemImplementation_OwnedMemorySubcomponent(),
						Aadl2Factory.eINSTANCE.createMemorySubcomponent()));

		newChildDescriptors
				.add(createChildParameter(Aadl2Package.eINSTANCE.getSystemImplementation_OwnedProcessSubcomponent(),
						Aadl2Factory.eINSTANCE.createProcessSubcomponent()));

		newChildDescriptors
				.add(createChildParameter(Aadl2Package.eINSTANCE.getSystemImplementation_OwnedProcessorSubcomponent(),
						Aadl2Factory.eINSTANCE.createProcessorSubcomponent()));

		newChildDescriptors
				.add(createChildParameter(Aadl2Package.eINSTANCE.getSystemImplementation_OwnedSubprogramSubcomponent(),
						Aadl2Factory.eINSTANCE.createSubprogramSubcomponent()));

		newChildDescriptors.add(
				createChildParameter(Aadl2Package.eINSTANCE.getSystemImplementation_OwnedSubprogramGroupSubcomponent(),
						Aadl2Factory.eINSTANCE.createSubprogramGroupSubcomponent()));

		newChildDescriptors
				.add(createChildParameter(Aadl2Package.eINSTANCE.getSystemImplementation_OwnedSystemSubcomponent(),
						Aadl2Factory.eINSTANCE.createSystemSubcomponent()));

		newChildDescriptors
				.add(createChildParameter(Aadl2Package.eINSTANCE.getSystemImplementation_OwnedVirtualBusSubcomponent(),
						Aadl2Factory.eINSTANCE.createVirtualBusSubcomponent()));

		newChildDescriptors.add(
				createChildParameter(Aadl2Package.eINSTANCE.getSystemImplementation_OwnedVirtualProcessorSubcomponent(),
						Aadl2Factory.eINSTANCE.createVirtualProcessorSubcomponent()));
	}

}
