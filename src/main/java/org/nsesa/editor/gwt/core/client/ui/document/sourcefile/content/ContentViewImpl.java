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
package org.nsesa.editor.gwt.core.client.ui.document.sourcefile.content;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.HandlerRegistration;
import org.nsesa.editor.gwt.core.client.event.ResizeEvent;
import org.nsesa.editor.gwt.core.client.event.ResizeEventHandler;
import org.nsesa.editor.gwt.core.client.ui.document.DocumentEventBus;
import org.nsesa.editor.gwt.core.client.util.Scope;

import java.util.logging.Logger;

import static org.nsesa.editor.gwt.core.client.util.Scope.ScopeValue.DOCUMENT;

/**
 * Default implementation of {@link ContentView} using UIBinder.
 * Date: 24/06/12 16:39
 *
 * @author <a href="mailto:philip.luppens@gmail.com">Philip Luppens</a>
 * @version $Id$
 */
@Singleton
@Scope(DOCUMENT)
public class ContentViewImpl extends Composite implements ContentView {

    private HandlerRegistration resizeEventHandlerRegistration;

    interface MyUiBinder extends UiBinder<Widget, ContentViewImpl> {
    }

    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);
    private static final Logger LOG = Logger.getLogger(ContentViewImpl.class.getName());

    /**
     * This correction value is supposed to be the height of the header and footer (plus any margin that might come into play)
     */
    private static final int DEFAULT_OFFSET = 122;

    private int scrollPanelOffset = DEFAULT_OFFSET;

    private DocumentEventBus documentEventBus;

    @UiField
    ScrollPanel scrollPanel;
    @UiField
    HTML contentPanel;

    @Inject
    public ContentViewImpl(final DocumentEventBus documentEventBus) {
        this.documentEventBus = documentEventBus;
        final Widget widget = uiBinder.createAndBindUi(this);
        initWidget(widget);
        //show class name tool tip in hosted mode
        if (!GWT.isScript())
            widget.setTitle(this.getClass().getName());

        registerListeners();
    }

    private void registerListeners() {
        resizeEventHandlerRegistration = documentEventBus.addHandler(ResizeEvent.TYPE, new ResizeEventHandler() {
            @Override
            public void onEvent(ResizeEvent event) {
                // this needs to be a fixed height, or you will not get a scroll bar.
                final int height = event.getHeight() - scrollPanelOffset;
                LOG.info("Setting scroll panel to " + height);
                setScrollPanelHeight(height + "px");
            }
        });
    }

    /**
     * Removes all registered event handlers from the event bus and UI.
     */
    public void removeListeners() {
        resizeEventHandlerRegistration.removeHandler();
    }

    /**
     * Due to layout issues in GWT wrt a scrollPanel inside a Docklayout, we need to manually
     * specify the correct height for the scrollPanel (or it will scale to 100%, and no overflow
     * will occur on the scrollPanel.
     * <p/>
     * This is currently being called when the eventBus receives a {@link ResizeEvent}.
     *
     * @param height the height
     */
    protected void setScrollPanelHeight(final String height) {
        scrollPanel.setHeight(height);
    }

    @Override
    public void setContent(String documentContent) {
        contentPanel.setHTML(documentContent);
    }

    @Override
    public Element getContentElement() {
        return contentPanel.getElement();
    }

    @Override
    public HTML getContentPanel() {
        return contentPanel;
    }

    @Override
    public ScrollPanel getScrollPanel() {
        return scrollPanel;
    }

    public void setScrollPanelOffset(int scrollPanelOffset) {
        this.scrollPanelOffset = scrollPanelOffset;
    }
}
