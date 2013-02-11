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
package org.nsesa.editor.gwt.core.client.ui.amendment;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.*;
import com.google.inject.Inject;
import org.nsesa.editor.gwt.core.client.ui.amendment.resources.Messages;
import org.nsesa.editor.gwt.core.client.util.Scope;

import static org.nsesa.editor.gwt.core.client.util.Scope.ScopeValue.AMENDMENT;

/**
 * Date: 24/06/12 21:44
 *
 * @author <a href="philip.luppens@gmail.com">Philip Luppens</a>
 * @version $Id$
 */
@Scope(AMENDMENT)
public class AmendmentViewImpl extends Composite implements AmendmentView {

    interface MyUiBinder extends UiBinder<Widget, AmendmentViewImpl> {
    }

    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

    private final Messages messages;

    @UiField
    Label title;

    @UiField
    Label status;

    @UiField
    HTMLPanel body;

    @UiField
    Image moreImage;

    @UiField
    Image editImage;

    @UiField
    Image deleteImage;

    @Inject
    public AmendmentViewImpl(final Messages messages) {
        this.messages = messages;
        final Widget widget = uiBinder.createAndBindUi(this);
        initWidget(widget);
    }

    @Override
    public HandlerRegistration addClickHandler(ClickHandler handler) {
        return addDomHandler(handler, ClickEvent.getType());
    }

    @Override
    public HandlerRegistration addDoubleClickHandler(DoubleClickHandler handler) {
        return addDomHandler(handler, DoubleClickEvent.getType());
    }

    @Override
    public void setTitle(String title) {
        super.setTitle(title);
        this.title.setText(title);
    }

    @Override
    public void setStatus(final String status) {
        if (status != null) {
            // do a lookup ...
            final String lookup = messages.getString(status.toLowerCase());
            if (lookup != null)
                this.status.setText(lookup);
            else
                this.status.setText(status);
        }
    }

    @Override
    public void onBrowserEvent(Event event) {
        event.stopPropagation();
        /*switch(DOM.eventGetType(event)) {
            case Event.ONDBLCLICK :

        }*/
        super.onBrowserEvent(event);
    }

    @Override
    public void setBody(String xmlContent) {
        body.getElement().setInnerHTML(xmlContent);
    }

    @Override
    public Element getBody() {
        return (Element) body.getElement().getFirstChildElement();
    }

    @Override
    public HasClickHandlers getMoreActionsButton() {
        return moreImage;
    }

    @Override
    public HasClickHandlers getEditButton() {
        return editImage;
    }

    @Override
    public HasClickHandlers getDeleteButton() {
        return deleteImage;
    }
}
