/**
 * Copyright 2013 European Parliament
 *
 * Licensed under the EUPL, Version 1.1 or - as soon they will be approved by the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 *
 * http://joinup.ec.europa.eu/software/page/eupl
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the Licence is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and limitations under the Licence.
 */
package org.nsesa.editor.gwt.amendment.client.amendment;

import com.google.inject.ImplementedBy;
import org.nsesa.editor.gwt.amendment.client.ui.amendment.AmendmentController;
import org.nsesa.editor.gwt.core.client.ui.overlay.document.OverlayWidget;
import org.nsesa.editor.gwt.core.client.ui.document.DocumentController;

/**
 * This interface is responsible for providing the correct overlay widget to add the amendment to. Note that this
 * is not required to return the same {@link org.nsesa.editor.gwt.core.client.ui.overlay.document.OverlayWidget} as
 * the one that was provided by the {@link AmendmentInjectionPointFinder}.
 * <p/>
 * For example, if the amendment introduces a new Point 1 under a Paragraph B, then the implementation should create
 * a new Point element under the paragraph B, and return this newly created Point to attach the amendment on.
 * <p/>
 * Date: 30/11/12 11:10
 *
 * @author <a href="mailto:philip.luppens@gmail.com">Philip Luppens</a>
 * @version $Id$
 */
@ImplementedBy(DefaultAmendmentInjectionPointProvider.class)
public interface AmendmentInjectionPointProvider {

    /**
     * Provides the overlay widget to attach the <tt>amendmentController</tt> to. Can be instantiated if the amendment
     * introduces a new element (in which case {@link org.nsesa.editor.gwt.core.client.ui.overlay.document.OverlayWidget#getOrigin()}
     * would return {@link org.nsesa.editor.gwt.core.shared.OverlayWidgetOrigin#AMENDMENT} or
     * {@link org.nsesa.editor.gwt.core.client.ui.overlay.document.OverlayWidget#isIntroducedByAnAmendment()} would
     * return <tt>true</tt>).
     *
     * @param amendmentController   the amendment controller to provide the injection point for
     * @param root                  the root overlay widget node
     * @param documentController    the containing document controller
     * @return  the overlay widget to add the amendment controller to, or null if it cannot be found/created
     */
    OverlayWidget provideInjectionPoint(AmendmentController amendmentController, OverlayWidget root, DocumentController documentController);
}
