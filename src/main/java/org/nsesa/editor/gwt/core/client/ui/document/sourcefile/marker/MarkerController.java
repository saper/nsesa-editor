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
package org.nsesa.editor.gwt.core.client.ui.document.sourcefile.marker;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.nsesa.editor.gwt.core.client.event.ResizeEvent;
import org.nsesa.editor.gwt.core.client.event.ResizeEventHandler;
import org.nsesa.editor.gwt.core.client.event.SwitchTabEvent;
import org.nsesa.editor.gwt.core.client.event.SwitchTabEventHandler;
import org.nsesa.editor.gwt.core.client.event.amendment.*;
import org.nsesa.editor.gwt.core.client.ui.amendment.AmendmentController;
import org.nsesa.editor.gwt.core.client.ui.overlay.document.OverlayWidget;
import org.nsesa.editor.gwt.core.client.util.Scope;
import org.nsesa.editor.gwt.core.client.event.document.DocumentRefreshRequestEvent;
import org.nsesa.editor.gwt.core.client.event.document.DocumentRefreshRequestEventHandler;
import org.nsesa.editor.gwt.core.client.ui.document.DocumentEventBus;
import org.nsesa.editor.gwt.core.client.ui.document.sourcefile.SourceFileController;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import static org.nsesa.editor.gwt.core.client.util.Scope.ScopeValue.DOCUMENT;

/**
 * Date: 24/06/12 18:42
 *
 * @author <a href="philip.luppens@gmail.com">Philip Luppens</a>
 * @version $Id$
 */
@Singleton
@Scope(DOCUMENT)
public class MarkerController {

    private static final Logger LOG = Logger.getLogger("MarkerController");

    private final MarkerView view;

    private SourceFileController sourceFileController;

    private final DocumentEventBus documentEventBus;

    private static final Map<String, String> colorCodes = new HashMap<String, String>() {
        {
            put("candidate", "blue");
            put("tabled", "green");
            put("withdrawn", "brown");
        }
    };

    private final Timer timer = new Timer() {
        public void run() {
            if (sourceFileController != null) {
                view.clearMarkers();
                final ScrollPanel scrollPanel = sourceFileController.getContentController().getView().getScrollPanel();
                for (final AmendmentController amendmentController : sourceFileController.getDocumentController().getAmendmentManager().getAmendmentControllers()) {
                    if (amendmentController.getDocumentController() == sourceFileController.getDocumentController() && amendmentController.getView().asWidget().isAttached()) {
                        final int documentHeight = scrollPanel.getMaximumVerticalScrollPosition();
//                        LOG.info("Document height is: " + documentHeight);
                        final int amendmentTop = amendmentController.getView().asWidget().getAbsoluteTop() + scrollPanel.getVerticalScrollPosition();
                        final double division = (double) documentHeight / (double) amendmentTop;
//                        LOG.info("Amendment is: " + amendmentTop + ", and division is at " + division);
                        final FocusWidget focusWidget = view.addMarker(division, colorCodes.get(amendmentController.getModel().getAmendmentContainerStatus()));
                        focusWidget.addClickHandler(new ClickHandler() {
                            @Override
                            public void onClick(ClickEvent event) {
                                // TODO: this is a very poor solution to find a amendable widget to scroll to ...
                                if (!amendmentController.getAmendedOverlayWidget().asWidget().isVisible()) {
                                    final OverlayWidget amendedOverlayWidget = amendmentController.getAmendedOverlayWidget();
                                    if (amendedOverlayWidget != null) {
                                        amendedOverlayWidget.getOverlayElement().getPreviousSiblingElement();

                                        OverlayWidget previousNonIntroducedOverlayWidget = amendedOverlayWidget.getPreviousNonIntroducedOverlayWidget(false);
                                        while (previousNonIntroducedOverlayWidget != null && !previousNonIntroducedOverlayWidget.asWidget().isVisible()) {
                                            previousNonIntroducedOverlayWidget = previousNonIntroducedOverlayWidget.getPreviousNonIntroducedOverlayWidget(false);
                                        }
                                        if (previousNonIntroducedOverlayWidget != null)
                                            sourceFileController.scrollTo(previousNonIntroducedOverlayWidget.asWidget());
                                        else {
                                            sourceFileController.scrollTo(amendedOverlayWidget.getParentOverlayWidget().asWidget());
                                        }
                                    }
                                } else {
                                    sourceFileController.scrollTo(amendmentController.getView().asWidget());
                                }
                            }
                        });
                    }
                }
            }
        }
    };

    @Inject
    public MarkerController(final DocumentEventBus documentEventBus, final MarkerView view) {
        assert view != null : "View is not set --BUG";

        this.documentEventBus = documentEventBus;
        this.view = view;

        registerListeners();
    }

    private void registerListeners() {
        documentEventBus.addHandler(DocumentRefreshRequestEvent.TYPE, new DocumentRefreshRequestEventHandler() {
            @Override
            public void onEvent(DocumentRefreshRequestEvent event) {
                clearMarkers();
            }
        });

        documentEventBus.addHandler(AmendmentContainerDeletedEvent.TYPE, new AmendmentContainerDeletedEventHandler() {
            @Override
            public void onEvent(AmendmentContainerDeletedEvent event) {
                drawAmendmentControllers();
            }
        });

        documentEventBus.addHandler(AmendmentContainerInjectedEvent.TYPE, new AmendmentContainerInjectedEventHandler() {
            @Override
            public void onEvent(AmendmentContainerInjectedEvent event) {
                drawAmendmentControllers();
            }
        });

        documentEventBus.addHandler(AmendmentContainerStatusUpdatedEvent.TYPE, new AmendmentContainerStatusUpdatedEventHandler() {
            @Override
            public void onEvent(AmendmentContainerStatusUpdatedEvent event) {
                drawAmendmentControllers();
            }
        });

        documentEventBus.addHandler(AmendmentContainerStatusUpdatedEvent.TYPE, new AmendmentContainerStatusUpdatedEventHandler() {
            @Override
            public void onEvent(AmendmentContainerStatusUpdatedEvent event) {
                drawAmendmentControllers();
            }
        });

        documentEventBus.addHandler(SwitchTabEvent.TYPE, new SwitchTabEventHandler() {
            @Override
            public void onEvent(SwitchTabEvent event) {
                drawAmendmentControllers();
            }
        });
    }

    private void drawAmendmentControllers() {
        timer.cancel();
        timer.schedule(1000);
    }


    private void clearMarkers() {
        view.clearMarkers();
    }

    public MarkerView getView() {
        return view;
    }

    public void setSourceFileController(SourceFileController sourceFileController) {
        this.sourceFileController = sourceFileController;
        registerPrivateListeners();
    }

    private void registerPrivateListeners() {
        documentEventBus.addHandler(ResizeEvent.TYPE, new ResizeEventHandler() {
            @Override
            public void onEvent(ResizeEvent event) {
                view.asWidget().setHeight((event.getHeight() - sourceFileController.getContentController().getView().getScrollPanel().getAbsoluteTop()) + "px");
                drawAmendmentControllers();
            }
        });
    }
}