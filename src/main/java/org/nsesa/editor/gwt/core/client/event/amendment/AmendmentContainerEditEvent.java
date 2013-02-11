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
package org.nsesa.editor.gwt.core.client.event.amendment;

import com.google.gwt.event.shared.GwtEvent;
import org.nsesa.editor.gwt.core.client.ui.amendment.AmendmentController;

/**
 * Date: 24/06/12 20:14
 *
 * @author <a href="philip.luppens@gmail.com">Philip Luppens</a>
 * @version $Id$
 */
public class AmendmentContainerEditEvent extends GwtEvent<AmendmentContainerEditEventHandler> {

    public static final Type<AmendmentContainerEditEventHandler> TYPE = new Type<AmendmentContainerEditEventHandler>();

    private final AmendmentController amendmentController;

    public AmendmentContainerEditEvent(AmendmentController amendmentController) {
        this.amendmentController = amendmentController;
    }

    @Override
    public Type<AmendmentContainerEditEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(AmendmentContainerEditEventHandler handler) {
        handler.onEvent(this);
    }

    public AmendmentController getAmendmentController() {
        return amendmentController;
    }
}
