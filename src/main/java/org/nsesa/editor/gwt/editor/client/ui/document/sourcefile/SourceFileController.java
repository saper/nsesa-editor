package org.nsesa.editor.gwt.editor.client.ui.document.sourcefile;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ScrollEvent;
import com.google.gwt.event.dom.client.ScrollHandler;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.nsesa.editor.gwt.core.client.ClientFactory;
import org.nsesa.editor.gwt.core.client.ServiceFactory;
import org.nsesa.editor.gwt.core.client.amendment.AmendableWidgetWalker;
import org.nsesa.editor.gwt.core.client.event.CriticalErrorEvent;
import org.nsesa.editor.gwt.core.client.event.amendment.AmendmentContainerCreateEvent;
import org.nsesa.editor.gwt.core.client.event.widget.AmendableWidgetSelectEvent;
import org.nsesa.editor.gwt.core.client.mode.InlineEditingMode;
import org.nsesa.editor.gwt.core.client.ui.amendment.AmendmentController;
import org.nsesa.editor.gwt.core.client.ui.overlay.AmendmentAction;
import org.nsesa.editor.gwt.core.client.ui.overlay.document.AmendableWidget;
import org.nsesa.editor.gwt.core.client.ui.overlay.document.AmendableWidgetUIListener;
import org.nsesa.editor.gwt.core.client.util.Counter;
import org.nsesa.editor.gwt.editor.client.event.document.DocumentScrollEvent;
import org.nsesa.editor.gwt.editor.client.ui.document.DocumentController;
import org.nsesa.editor.gwt.editor.client.ui.document.DocumentEventBus;
import org.nsesa.editor.gwt.editor.client.ui.document.sourcefile.actionbar.ActionBarController;
import org.nsesa.editor.gwt.editor.client.ui.document.sourcefile.content.ContentController;
import org.nsesa.editor.gwt.editor.client.ui.document.sourcefile.header.SourceFileHeaderController;
import org.nsesa.editor.gwt.editor.client.ui.document.sourcefile.marker.MarkerController;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Date: 28/01/13 15:27
 *
 * @author <a href="philip.luppens@gmail.com">Philip Luppens</a>
 * @version $Id$
 */
@Singleton
public class SourceFileController implements AmendableWidgetUIListener, AmendableWidgetWalker {

    private static final Logger LOG = Logger.getLogger(SourceFileController.class.getName());

    private final ClientFactory clientFactory;
    private final ServiceFactory serviceFactory;

    private final DocumentEventBus documentEventBus;

    private final MarkerController markerController;
    private final SourceFileHeaderController sourceFileHeaderController;
    private final ContentController contentController;
    private final ActionBarController actionBarController;

    private final SourceFileView view;

    // logical parent
    private DocumentController documentController;

    private List<AmendableWidget> amendableWidgets;

    private AmendableWidget activeAmendableWidget;

    @Inject
    public SourceFileController(ClientFactory clientFactory, ServiceFactory serviceFactory,
                                DocumentEventBus documentEventBus, MarkerController markerController,
                                SourceFileHeaderController sourceFileHeaderController,
                                SourceFileView sourceFileView,
                                ContentController contentController, ActionBarController actionBarController) {
        this.clientFactory = clientFactory;
        this.serviceFactory = serviceFactory;
        this.view = sourceFileView;
        this.documentEventBus = documentEventBus;
        this.markerController = markerController;
        this.markerController.setSourceFileController(this);
        this.sourceFileHeaderController = sourceFileHeaderController;
        this.sourceFileHeaderController.setSourceFileController(this);
        this.contentController = contentController;
        this.actionBarController = actionBarController;

        registerListeners();
    }

    private void registerListeners() {
        contentController.getView().getScrollPanel().addScrollHandler(new ScrollHandler() {
            @Override
            public void onScroll(ScrollEvent event) {
                documentEventBus.fireEvent(new DocumentScrollEvent(documentController));
            }
        });
    }

    public AmendableWidget getTopVisibleAmenableWidget() {
        return contentController.getCurrentVisibleAmendableWidget();
    }

    public void setContent(String documentContent) {

        // clean up
        if (amendableWidgets != null && !amendableWidgets.isEmpty()) {
            for (final AmendableWidget amendableWidget : amendableWidgets) {
                amendableWidget.onDetach();
            }
        }
        contentController.setContent(documentContent);
    }

    public AmendableWidget overlay(final com.google.gwt.dom.client.Element element, final AmendableWidgetUIListener UIListener) {
        // Assert that the element is attached.
        // assert Document.get().getBody().isOrHasChild(element) : "element is not attached to the document -- BUG";

        final AmendableWidget root = documentController.getOverlayFactory().getAmendableWidget(element);
        if (root != null) {
            walk(root, new AmendableWidgetWalker.AmendableVisitor() {
                @Override
                public boolean visit(AmendableWidget visited) {
                    // if the widget is amendable, register a listener for its events
                    if (visited != null && visited.isAmendable() != null && visited.isAmendable()) {
                        visited.setUIListener(UIListener);
                    }
                    return true;
                }
            });
        }
        return root;
    }

    @Override
    public void walk(AmendableWidgetWalker.AmendableVisitor visitor) {
        for (final AmendableWidget root : amendableWidgets) {
            root.walk(visitor);
        }
    }

    public void walk(final AmendableWidget toVisit, final AmendableWidgetWalker.AmendableVisitor visitor) {
        toVisit.walk(visitor);
    }

    public void scrollTo(Widget widget) {
        contentController.scrollTo(widget);
    }

    @Override
    public void onClick(AmendableWidget sender) {
        //        printDetails(sender);
        clientFactory.getEventBus().fireEvent(new AmendableWidgetSelectEvent(sender, documentController));
    }

    private void printDetails(final AmendableWidget amendableWidget) {
        final AmendableWidget previousNonIntroducedAmendableWidget = amendableWidget.getPreviousNonIntroducedAmendableWidget(false);
        System.out.println(">>>> " + (previousNonIntroducedAmendableWidget != null ? documentController.getLocator().getLocation(previousNonIntroducedAmendableWidget, "EN", false) : null));
        final AmendableWidget previousNonIntroducedAmendableWidget1 = amendableWidget.getPreviousNonIntroducedAmendableWidget(true);
        System.out.println(">>>> " + (previousNonIntroducedAmendableWidget1 != null ? documentController.getLocator().getLocation(previousNonIntroducedAmendableWidget1, "EN", false) : null));
        final AmendableWidget nextNonIntroducedAmendableWidget = amendableWidget.getNextNonIntroducedAmendableWidget(false);
        System.out.println(">>>> " + (nextNonIntroducedAmendableWidget != null ? documentController.getLocator().getLocation(nextNonIntroducedAmendableWidget, "EN", false) : null));
        final AmendableWidget nextNonIntroducedAmendableWidget1 = amendableWidget.getNextNonIntroducedAmendableWidget(true);
        System.out.println(">>>> " + (nextNonIntroducedAmendableWidget1 != null ? documentController.getLocator().getLocation(nextNonIntroducedAmendableWidget1, "EN", false) : null));
    }

    @Override
    public void onDblClick(AmendableWidget sender) {
        final InlineEditingMode inlineEditingMode = (InlineEditingMode) documentController.getMode(InlineEditingMode.KEY);
        if (inlineEditingMode != null) {
            if (!inlineEditingMode.getState().isActive()) {
                documentEventBus.fireEvent(new AmendmentContainerCreateEvent(sender, null, 0, AmendmentAction.MODIFICATION, documentController));
            }
        } else {
            documentEventBus.fireEvent(new AmendmentContainerCreateEvent(sender, null, 0, AmendmentAction.MODIFICATION, documentController));
        }
    }

    @Override
    public void onMouseOver(AmendableWidget sender) {
        actionBarController.attach(sender);
        actionBarController.setLocation(documentController.getLocator().getLocation(sender, documentController.getDocument().getLanguageIso(), false));
    }

    @Override
    public void onMouseOut(AmendableWidget sender) {
        // ignore
    }

    public void overlay() {
        long start = System.currentTimeMillis();
        final Element[] contentElements = contentController.getContentElements();
        if (amendableWidgets == null) amendableWidgets = new ArrayList<AmendableWidget>();
        for (final Element element : contentElements) {
            try {
                final AmendableWidget rootAmendableWidget = overlay(element, this);
                amendableWidgets.add(rootAmendableWidget);
            } catch (Exception e) {
                LOG.log(Level.SEVERE, "Exception while overlaying.", e);
                documentEventBus.fireEvent(new CriticalErrorEvent("Exception while overlaying.", e));
            }
        }
        LOG.info("Overlaying took " + (System.currentTimeMillis() - start) + "ms.");
    }

    public void renumberAmendments() {
        final Counter counter = new Counter();
        walk(new AmendableVisitor() {
            @Override
            public boolean visit(AmendableWidget visited) {
                if (visited.isAmended()) {
                    for (final AmendmentController amendmentController : visited.getAmendmentControllers()) {
                        amendmentController.setOrder(counter.incrementAndGet());
                    }
                }
                return true;
            }
        });
    }

    public AmendableWidget getActiveAmendableWidget() {
        return activeAmendableWidget;
    }

    public void setActiveAmendableWidget(AmendableWidget activeAmendableWidget) {
        this.activeAmendableWidget = activeAmendableWidget;
    }

    public void setDocumentController(DocumentController documentController) {
        this.documentController = documentController;
    }

    public List<AmendableWidget> getAmendableWidgets() {
        return amendableWidgets;
    }

    public void clearAmendableWidgets() {
        this.amendableWidgets = new ArrayList<AmendableWidget>();
    }

    public MarkerController getMarkerController() {
        return markerController;
    }

    public SourceFileHeaderController getSourceFileHeaderController() {
        return sourceFileHeaderController;
    }

    public ContentController getContentController() {
        return contentController;
    }

    public ActionBarController getActionBarController() {
        return actionBarController;
    }

    public DocumentController getDocumentController() {
        return documentController;
    }

    public SourceFileView getView() {
        return view;
    }
}
