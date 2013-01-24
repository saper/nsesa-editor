package org.nsesa.editor.gwt.editor.client.ui.amendments;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.nsesa.editor.gwt.core.client.event.amendment.*;
import org.nsesa.editor.gwt.core.client.ui.amendment.AmendmentController;
import org.nsesa.editor.gwt.core.client.util.Filter;
import org.nsesa.editor.gwt.core.client.util.FilterResponse;
import org.nsesa.editor.gwt.core.client.util.Scope;
import org.nsesa.editor.gwt.core.client.util.Selection;
import org.nsesa.editor.gwt.editor.client.event.amendments.AmendmentControllerSelectedEvent;
import org.nsesa.editor.gwt.editor.client.event.amendments.AmendmentControllerSelectedEventHandler;
import org.nsesa.editor.gwt.editor.client.event.document.DocumentRefreshRequestEvent;
import org.nsesa.editor.gwt.editor.client.event.document.DocumentRefreshRequestEventHandler;
import org.nsesa.editor.gwt.editor.client.event.filter.FilterRequestEvent;
import org.nsesa.editor.gwt.editor.client.event.filter.FilterRequestEventHandler;
import org.nsesa.editor.gwt.editor.client.event.filter.FilterResponseEvent;
import org.nsesa.editor.gwt.editor.client.ui.document.DocumentController;
import org.nsesa.editor.gwt.editor.client.ui.document.DocumentEventBus;

import java.util.*;

import static org.nsesa.editor.gwt.core.client.util.Scope.ScopeValue.DOCUMENT;

/**
 * Date: 24/06/12 21:42
 *
 * @author <a href="philip.luppens@gmail.com">Philip Luppens</a>
 * @version $Id$
 */
@Singleton
@Scope(DOCUMENT)
public class AmendmentsPanelController {
    private static final int AMENDMENTS_PER_PAGE = 2;

    private AmendmentsPanelView view;
    private DocumentEventBus documentEventBus;
    private DocumentController documentController;
    private Filter<AmendmentController> currentFilter;

    private Selection<AmendmentController> DEFAULT_SELECTION = new Selection.AllSelection<AmendmentController>();

    @Inject
    public AmendmentsPanelController(AmendmentsPanelView view,
                                     DocumentEventBus documentEventBus) {
        this.view = view;
        this.documentEventBus = documentEventBus;
        this.currentFilter = new Filter<AmendmentController>(0, AMENDMENTS_PER_PAGE,
                AmendmentController.ORDER_COMPARATOR, DEFAULT_SELECTION);
        registerListeners();
    }

    public AmendmentsPanelView getView() {
        return view;
    }

    private void registerListeners() {
        //EventBus eventBus = clientFactory.getEventBus();
        documentEventBus.addHandler(DocumentRefreshRequestEvent.TYPE, new DocumentRefreshRequestEventHandler() {
            @Override
            public void onEvent(DocumentRefreshRequestEvent event) {
                refreshAmendments();
            }
        });

        documentEventBus.addHandler(AmendmentContainerInjectedEvent.TYPE, new AmendmentContainerInjectedEventHandler() {
            @Override
            public void onEvent(AmendmentContainerInjectedEvent event) {
                refreshAmendments();
            }
        });
        documentEventBus.addHandler(AmendmentContainerDeletedEvent.TYPE, new AmendmentContainerDeletedEventHandler() {
            @Override
            public void onEvent(AmendmentContainerDeletedEvent event) {
                refreshAmendments();
            }
        });

        documentEventBus.addHandler(AmendmentContainerStatusUpdatedEvent.TYPE, new AmendmentContainerStatusUpdatedEventHandler() {
            @Override
            public void onEvent(AmendmentContainerStatusUpdatedEvent event) {
                refreshAmendments();
            }
        });
        documentEventBus.addHandler(AmendmentContainerStatusUpdatedEvent.TYPE, new AmendmentContainerStatusUpdatedEventHandler() {
            @Override
            public void onEvent(AmendmentContainerStatusUpdatedEvent event) {
                refreshAmendments();
            }
        });
        documentEventBus.addHandler(FilterRequestEvent.TYPE, new FilterRequestEventHandler() {
            @Override
            public void onEvent(FilterRequestEvent event) {
                currentFilter = event.getFilter();
                filterAmendments();
            }
        });
        documentEventBus.addHandler(AmendmentControllerSelectedEvent.TYPE, new AmendmentControllerSelectedEventHandler() {
            @Override
            public void onEvent(AmendmentControllerSelectedEvent event) {
                final Collection<String> ids = Collections2.transform(event.getSelected(), new Function<AmendmentController, String>() {
                    @Override
                    public String apply(final AmendmentController input) {
                        return input.getModel().getId();
                    }
                });
                view.selectAmendmentControllers(new ArrayList<String>(ids));
            }
        });

    }


    public void setDocumentController(final DocumentController documentController) {
        this.documentController = documentController;
    }

    private void applySelection(final Selection<AmendmentController> selection) {
        final List<String> ids = new ArrayList<String>();
        final List<AmendmentController> selected = new ArrayList<AmendmentController>();

        view.selectAmendmentControllers(ids);
    }

    private void refreshAmendments() {
        if (currentFilter != null) {
            currentFilter.setStart(0);
        } else {
            currentFilter = new Filter<AmendmentController>(0, AMENDMENTS_PER_PAGE,
                    AmendmentController.ORDER_COMPARATOR,
                    DEFAULT_SELECTION);

        }
        filterAmendments();
    }

    private void filterAmendments() {
        final Map<String, AmendmentController> amendments = new LinkedHashMap<String, AmendmentController>();
        final FilterResponse<AmendmentController> response = documentController.getAmendmentManager().getAmendmentControllers(currentFilter);
        for (final AmendmentController amendmentController : response.getResult()) {
            amendments.put(amendmentController.getModel().getId(), amendmentController);
        }
        view.refreshAmendmentControllers(amendments);
        // raise a filter response
        documentEventBus.fireEvent(new FilterResponseEvent(response.getTotalSize(), currentFilter));
    }
}

