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
package org.nsesa.editor.gwt.amendment.client.event.amendment;

import com.google.gwt.event.shared.GwtEvent;
import org.nsesa.editor.gwt.amendment.client.ui.amendment.AmendmentController;

/**
 * An event indicating that an amendment controller has been updated and replaced by a new revision.
 * Date: 24/06/12 20:14
 *
 * @author <a href="mailto:philip.luppens@gmail.com">Philip Luppens</a>
 * @version $Id$
 */
public class AmendmentContainerUpdatedEvent extends GwtEvent<AmendmentContainerUpdatedEventHandler> {

    public static final Type<AmendmentContainerUpdatedEventHandler> TYPE = new Type<AmendmentContainerUpdatedEventHandler>();

    private final AmendmentController oldRevision;
    private final AmendmentController newRevision;

    public AmendmentContainerUpdatedEvent(AmendmentController oldRevision, AmendmentController newRevision) {
        this.oldRevision = oldRevision;
        this.newRevision = newRevision;
    }

    @Override
    public Type<AmendmentContainerUpdatedEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(AmendmentContainerUpdatedEventHandler handler) {
        handler.onEvent(this);
    }

    public AmendmentController getNewRevision() {
        return newRevision;
    }

    public AmendmentController getOldRevision() {
        return oldRevision;
    }
}
