package org.nsesa.editor.gwt.editor.client.ui.document.content;

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
import org.nsesa.editor.gwt.core.client.event.ResizeEvent;
import org.nsesa.editor.gwt.core.client.event.ResizeEventHandler;
import org.nsesa.editor.gwt.core.client.util.Scope;
import org.nsesa.editor.gwt.editor.client.ui.document.DocumentEventBus;

import java.util.logging.Logger;

import static org.nsesa.editor.gwt.core.client.util.Scope.ScopeValue.DOCUMENT;

/**
 * Date: 24/06/12 16:39
 *
 * @author <a href="philip.luppens@gmail.com">Philip Luppens</a>
 * @version $Id$
 */
@Singleton
@Scope(DOCUMENT)
public class ContentViewImpl extends Composite implements ContentView {

    interface MyUiBinder extends UiBinder<Widget, ContentViewImpl> {
    }

    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);
    private static final Logger LOG = Logger.getLogger(ContentViewImpl.class.getName());

    /**
     * This correction value is supposed to be the height of the header and footer (plus any margin that might come into play)
     */
    private static final int SCROLLBAR_OFFSET = 73;

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
        registerListeners();

    }

    private void registerListeners() {
        documentEventBus.addHandler(ResizeEvent.TYPE, new ResizeEventHandler() {
            @Override
            public void onEvent(ResizeEvent event) {

                final int height = event.getHeight() - SCROLLBAR_OFFSET;
                LOG.info("Setting scroll panel to " + height);
                setScrollPanelHeight(height + "px");
            }
        });
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
    public ScrollPanel getScrollPanel() {
        return scrollPanel;
    }
}
