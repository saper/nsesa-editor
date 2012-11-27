package org.nsesa.editor.gwt.core.client.ui.amendment;

import com.google.gwt.core.client.GWT;
import com.google.inject.Inject;
import org.nsesa.editor.gwt.core.client.ClientFactory;
import org.nsesa.editor.gwt.core.client.ui.overlay.document.AmendableWidget;
import org.nsesa.editor.gwt.core.client.ui.overlay.document.OverlayFactory;
import org.nsesa.editor.gwt.core.client.util.Scope;
import org.nsesa.editor.gwt.core.shared.AmendmentContainerDTO;
import org.nsesa.editor.gwt.editor.client.ui.document.DocumentController;

import static org.nsesa.editor.gwt.core.client.util.Scope.ScopeValue.AMENDMENT;

/**
 * Date: 24/06/12 21:42
 *
 * @author <a href="philip.luppens@gmail.com">Philip Luppens</a>
 * @version $Id$
 */
@Scope(AMENDMENT)
public class AmendmentController {

    private final AmendmentInjector amendmentInjector = GWT.create(AmendmentInjector.class);

    private final AmendmentView view;
    private final AmendmentView extendedView;

    private final ClientFactory clientFactory;
    private OverlayFactory overlayFactory;
    private final AmendmentEventBus amendmentEventBus;

    private AmendmentContainerDTO amendment;

    private AmendableWidget parentAmendableWidget;

    private AmendableWidget amendmentWidget;

    /**
     * The document controller into which we are injected. If it is not set, we're not injected anywhere.
     */
    private DocumentController documentController;

    @Inject
    public AmendmentController(final ClientFactory clientFactory, final OverlayFactory overlayFactory) {
        this.clientFactory = clientFactory;
        this.overlayFactory = overlayFactory;

        this.view = amendmentInjector.getAmendmentView();
        this.extendedView = amendmentInjector.getAmendmentExtendedView();
        this.amendmentEventBus = amendmentInjector.getAmendmentEventBus();
        registerListeners();
    }

    private void registerListeners() {

    }

    public AmendmentContainerDTO getAmendment() {
        return amendment;
    }

    public void setAmendment(AmendmentContainerDTO amendment) {
        this.amendment = amendment;
        setBody(amendment.getXmlContent());
        setTitle("Amendment "+ amendment.getAmendmentContainerID());
    }

    private void setBody(String xmlContent) {
        view.setBody(xmlContent);
        extendedView.setBody(xmlContent);
    }

    public DocumentController getDocumentController() {
        return documentController;
    }

    public void setDocumentController(DocumentController documentController) {
        this.documentController = documentController;
    }

    public AmendmentView getView() {
        return view;
    }

    public AmendmentView getExtendedView() {
        return extendedView;
    }

    public void setTitle(String title) {
        this.view.setTitle(title);
        this.extendedView.setTitle(title);
    }

    public void setParentAmendableWidget(AmendableWidget parentAmendableWidget) {
        this.parentAmendableWidget = parentAmendableWidget;
    }

    public void setAmendmentWidget(AmendableWidget amendmentWidget) {
        this.amendmentWidget = amendmentWidget;
    }
}
