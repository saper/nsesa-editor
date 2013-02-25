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
package org.nsesa.editor.gwt.core.client.ui.document.sourcefile.actionbar.create;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.inject.ImplementedBy;
import org.nsesa.editor.gwt.core.client.ui.overlay.document.OverlayWidget;

/**
 * Date: 24/06/12 21:44
 *
 * @author <a href="philip.luppens@gmail.com">Philip Luppens</a>
 * @version $Id$
 */
@ImplementedBy(ActionBarCreatePanelViewImpl.class)
public interface ActionBarCreatePanelView extends IsWidget {
    void setUIListener(UIListener uiListener);

    void attach();

    void addChildAmendableWidget(String title, OverlayWidget overlayWidget);

    void addSiblingAmendableWidget(String title, final OverlayWidget overlayWidget);

    void setSeparatorVisible(boolean visible);

    void clearAmendableWidgets();

    public static interface UIListener {
        void onClick(OverlayWidget newChild, boolean sibling);
    }
}