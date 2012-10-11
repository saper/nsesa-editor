package org.nsesa.editor.gwt.dialog.client.ui.handler.widget;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ProvidesResize;
import com.google.inject.Inject;
import org.nsesa.editor.gwt.core.client.ClientFactory;
import org.nsesa.editor.gwt.core.client.ui.overlay.Locator;
import org.nsesa.editor.gwt.core.client.ui.overlay.document.AmendableWidget;
import org.nsesa.editor.gwt.core.shared.AmendmentContainerDTO;
import org.nsesa.editor.gwt.dialog.client.event.CloseDialogEvent;
import org.nsesa.editor.gwt.dialog.client.ui.handler.AmendmentUIHandler;

/**
 * Main amendment dialog. Allows for the creation and editing of amendments. Typically consists of a two
 * column layout (with the original proposed text on the left, and a rich text editor on the right).
 * <p/>
 * Requires an {@link org.nsesa.editor.gwt.core.shared.AmendmentContainerDTO} and {@link org.nsesa.editor.gwt.core.client.ui.overlay.document.AmendableWidget} to be set before it can be displayed.
 * Date: 24/06/12 21:42
 *
 * @author <a href="philip.luppens@gmail.com">Philip Luppens</a>
 * @version $Id$
 */
public class AmendmentWidgetController extends Composite implements ProvidesResize, AmendmentUIHandler {

    private final ClientFactory clientFactory;

    private final AmendmentWidgetView view;

    private final Locator locator;

    private AmendmentContainerDTO amendment;

    private AmendableWidget amendableWidget;

    @Inject
    public AmendmentWidgetController(final ClientFactory clientFactory, final AmendmentWidgetView view,
                                     final Locator locator) {
        this.clientFactory = clientFactory;
        this.view = view;
        this.locator = locator;
        registerListeners();
    }

    private void registerListeners() {
        view.getCancelButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                clientFactory.getEventBus().fireEvent(new CloseDialogEvent());
            }
        });
    }

    @Override
    public AmendmentWidgetView getView() {
        return view;
    }

    @Override
    public void setAmendmentAndWidget(AmendmentContainerDTO amendment, AmendableWidget amendableWidget) {
        this.amendment = amendment;
        this.amendableWidget = amendableWidget;

        if (amendableWidget != null) {
            view.setTitle(locator.getLocation(amendableWidget, clientFactory.getClientContext().getDocumentIso(), false));
            view.setOriginalContent(amendableWidget.getContent());
            view.setAmendmentContent(amendableWidget.getContent());
        } else if (amendment != null) {
            // TODO edit the amendment
        } else {
            throw new NullPointerException("Neither amendment nor amendable widget are set.");
        }
    }
}