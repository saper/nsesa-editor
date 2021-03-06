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
package org.nsesa.editor.gwt.dialog.client.ui.handler.common.meta;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.inject.ImplementedBy;

/**
 * View for the {@link MetaPanelController}.
 * Date: 24/06/12 21:44
 *
 * @author <a href="mailto:philip.luppens@gmail.com">Philip Luppens</a>
 * @version $Id$
 */
@ImplementedBy(MetaPanelViewImpl.class)
public interface MetaPanelView extends IsWidget {

    /**
     * Set the justification for this amendment.
     * @param justification the justification
     */
    void setJustification(String justification);

    /**
     * Set the notes for this amendment.
     * @param notes the notes
     */
    void setNotes(String notes);

    /**
     * Get the justification from the view.
     * @return the justification
     */
    String getJustification();

    /**
     * Get the notes from the view.
     * @return the notes
     */
    String getNotes();
}
