/**
 * Copyright 2013 European Parliament
 *
 * Licensed under the EUPL, Version 1.1 or – as soon they will be approved by the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 *
 * http://joinup.ec.europa.eu/software/page/eupl
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the Licence is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and limitations under the Licence.
 */
package org.nsesa.editor.gwt.core.client.ui.overlay.document;

import org.nsesa.editor.gwt.core.client.ui.amendment.AmendmentController;

/**
 * Date: 23/10/12 14:15
 *
 * @author <a href="philip.luppens@gmail.com">Philip Luppens</a>
 * @version $Id$
 */
public interface AmendableWidgetListener {
    // amendment operation callbacks

    boolean beforeAmendmentControllerAdded(AmendableWidget amendableWidget, AmendmentController amendmentController);

    void afterAmendmentControllerAdded(AmendableWidget amendableWidget, AmendmentController amendmentController);

    boolean beforeAmendmentControllerRemoved(AmendableWidget amendableWidget, AmendmentController amendmentController);

    void afterAmendmentControllerRemoved(AmendableWidget amendableWidget, AmendmentController amendmentController);

    boolean beforeAmendableWidgetAdded(AmendableWidget amendableWidget, AmendableWidget child);

    void afterAmendableWidgetAdded(AmendableWidget amendableWidget, AmendableWidget child);

    boolean beforeAmendableWidgetRemoved(AmendableWidget amendableWidget, AmendableWidget child);

    void afterAmendableWidgetRemoved(AmendableWidget amendableWidget, AmendableWidget child);
}
