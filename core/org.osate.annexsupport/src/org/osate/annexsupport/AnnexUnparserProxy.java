/*
 * <copyright>
 * Copyright  2005 by Carnegie Mellon University, all rights reserved.
 *
 * Use of the Open Source AADL Tool Environment (OSATE) is subject to the terms of the license set forth
 * at http://www.eclipse.org/legal/cpl-v10.html.
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
 */
package org.osate.annexsupport;

import org.eclipse.core.runtime.IConfigurationElement;
import org.osate.aadl2.AnnexLibrary;
import org.osate.aadl2.AnnexSubclause;

/**
 * @author lwrage
 * @version $Id: AnnexUnparserProxy.java,v 1.3 2009-10-09 18:49:32 lwrage Exp $
 */
public class AnnexUnparserProxy extends AnnexProxy implements AnnexUnparser {

	/** The annex unparser instance. */
	private AnnexUnparser unparser = null;

	/**
	 * @param configElem
	 */
	public AnnexUnparserProxy(IConfigurationElement configElem) {
		super(configElem);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osate.annexsupport.AnnexUnparser#unparseAnnexLibrary(
	 * edu.cmu.sei.aadl.model.core.AnnexLibrary, java.lang.String)
	 */
	@Override
	public String unparseAnnexLibrary(AnnexLibrary library, String indent) {
		AnnexUnparser unparser = getUnparser();

		if (unparser == null) {
			return "";
		}
		return unparser.unparseAnnexLibrary(library, indent).replaceAll("^ +", "");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osate.annexsupport.AnnexUnparser#unparseAnnexSubclause(
	 * edu.cmu.sei.aadl.model.core.AnnexSubclause, java.lang.String)
	 */
	@Override
	public String unparseAnnexSubclause(AnnexSubclause subclause, String indent) {
		AnnexUnparser unparser = getUnparser();

		if (unparser == null) {
			return "";
		}
		return unparser.unparseAnnexSubclause(subclause, indent).replaceAll("^ +", "");
	}

	private AnnexUnparser getUnparser() {
		if (unparser != null) {
			return unparser;
		}
		try {
			unparser = (AnnexUnparser) configElem.createExecutableExtension(ATT_CLASS);
		} catch (Exception e) {
			AnnexPlugin.logError("Failed to instantiate " + annexName + " unparser " + className + " in type: " + id
					+ " in plugin " + configElem.getDeclaringExtension().getContributor().getName(), e);
		}
		return unparser;
	}
}