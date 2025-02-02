package org.osate.ge.errormodel;

import java.util.stream.Stream;

import javax.inject.Named;

import org.osate.aadl2.AadlPackage;
import org.osate.ge.BusinessObjectContext;
import org.osate.ge.di.Activate;
import org.osate.ge.di.Names;
import org.osate.ge.errormodel.model.BehaviorTransitionTrunk;
import org.osate.ge.errormodel.model.ErrorTypeExtension;
import org.osate.ge.errormodel.model.ErrorTypeLibrary;
import org.osate.ge.errormodel.util.ErrorModelGeUtil;
import org.osate.xtext.aadl2.errormodel.errorModel.ErrorBehaviorStateMachine;
import org.osate.xtext.aadl2.errormodel.errorModel.ErrorBehaviorTransition;
import org.osate.xtext.aadl2.errormodel.errorModel.ErrorModelLibrary;
import org.osate.xtext.aadl2.errormodel.errorModel.ErrorType;

public class ErrorModelBusinessObjectProvider {

	@Activate
	public Stream<?> getBusinessObjects(final @Named(Names.BUSINESS_OBJECT_CONTEXT) BusinessObjectContext boc) {
		final Object bo = boc.getBusinessObject();
		if (bo instanceof AadlPackage) {
			final ErrorModelLibrary lib = ErrorModelGeUtil.getErrorModelLibrary((AadlPackage) bo);
			if (lib == null) {
				return null;
			}

			return Stream.concat(Stream.of(new ErrorTypeLibrary(lib)), lib.getBehaviors().stream());
		} else if (bo instanceof ErrorTypeLibrary) {
			final ErrorTypeLibrary etl = (ErrorTypeLibrary) bo;
			return etl.getErrorModelLibrary().getTypes().stream();
		} else if(bo instanceof ErrorBehaviorStateMachine) {
			final ErrorBehaviorStateMachine stateMachine = (ErrorBehaviorStateMachine)bo;
			return Stream.concat(Stream.concat(stateMachine.getEvents().stream(),
					stateMachine.getStates().stream()),
					stateMachine.getTransitions().stream());
		} else if (bo instanceof ErrorBehaviorTransition) {
			// See ErrorBehaviorTransitionHandler for details regarding how the business objects related to error behavior transitions are represented.
			final ErrorBehaviorTransition t = (ErrorBehaviorTransition) bo;
			return t.getDestinationBranches().isEmpty() ? Stream.empty()
					: Stream.concat(Stream.of(new BehaviorTransitionTrunk(t)), t.getDestinationBranches().stream());
		} else if(bo instanceof ErrorType) {
			final ErrorType errorType = (ErrorType)bo;
			if(errorType.getSuperType() != null) {
				return Stream.of(new ErrorTypeExtension(errorType.getSuperType(), errorType));
			}
		}

		return null;
	}
}
