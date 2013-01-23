package org.nsesa.editor.gwt.editor.client.ui.amendments.header;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.nsesa.editor.gwt.core.client.ClientFactory;
import org.nsesa.editor.gwt.core.client.ServiceFactory;
import org.nsesa.editor.gwt.core.client.event.CriticalErrorEvent;
import org.nsesa.editor.gwt.core.client.event.NotificationEvent;
import org.nsesa.editor.gwt.core.client.event.amendment.AmendmentContainerStatusUpdatedEvent;
import org.nsesa.editor.gwt.core.client.ui.amendment.AmendmentController;
import org.nsesa.editor.gwt.core.client.util.Scope;
import org.nsesa.editor.gwt.core.client.util.Selection;
import org.nsesa.editor.gwt.core.shared.AmendmentContainerDTO;
import org.nsesa.editor.gwt.editor.client.event.amendments.AmendmentControllerSelectionActionEvent;
import org.nsesa.editor.gwt.editor.client.event.amendments.AmendmentControllerSelectionEvent;
import org.nsesa.editor.gwt.editor.client.ui.document.DocumentEventBus;

import java.util.ArrayList;
import java.util.List;

import static org.nsesa.editor.gwt.core.client.util.Scope.ScopeValue.DOCUMENT;

/**
 * The controller for amendments panel header
 * User: groza
 * Date: 26/11/12
 * Time: 11:49
 * To change this template use File | Settings | File Templates.
 */
@Singleton
@Scope(DOCUMENT)
public class AmendmentsHeaderController {

    private final AmendmentsHeaderView view;
    private DocumentEventBus documentEventBus;
    private final ClientFactory clientFactory;
    private final ServiceFactory serviceFactory;

    @Inject
    public AmendmentsHeaderController(final AmendmentsHeaderView view,
                                      final ClientFactory clientFactory,
                                      final ServiceFactory serviceFactory,
                                      final DocumentEventBus documentEventBus
    ) {
        this.view = view;
        this.documentEventBus = documentEventBus;
        this.clientFactory = clientFactory;
        this.serviceFactory = serviceFactory;
        registerListeners();
        // register the selections
        registerSelections();
        // register the actions
        registerActions();
    }

    public AmendmentsHeaderView getView() {
        return view;
    }

    protected void registerActions() {
        final Button tableButton = new Button(clientFactory.getCoreMessages().amendmentActionTable());
        tableButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                documentEventBus.fireEvent(new AmendmentControllerSelectionActionEvent(new AmendmentControllerSelectionActionEvent.Action() {
                    @Override
                    public void execute(final List<AmendmentController> amendmentControllers) {
                        if (!amendmentControllers.isEmpty()) {
                            serviceFactory.getGwtAmendmentService().tableAmendmentContainers(clientFactory.getClientContext(),
                                    transformToDTOs(amendmentControllers),
                                    new AsyncCallback<AmendmentContainerDTO[]>() {
                                        @Override
                                        public void onFailure(Throwable caught) {
                                            clientFactory.getEventBus().fireEvent(new CriticalErrorEvent("Could not table amendment(s).", caught));
                                        }

                                        @Override
                                        public void onSuccess(AmendmentContainerDTO[] result) {
                                            int index = 0;
                                            for (final AmendmentContainerDTO dto : result) {
                                                final AmendmentController amendmentController = amendmentControllers.get(index);
                                                final String oldStatus = amendmentController.getModel().getAmendmentContainerStatus();
                                                amendmentController.setAmendment(dto);
                                                documentEventBus.fireEvent(new AmendmentContainerStatusUpdatedEvent(amendmentController, oldStatus));
                                                index++;
                                            }
                                            documentEventBus.fireEvent(new NotificationEvent(clientFactory.getCoreMessages().amendmentActionTableSuccessful(result.length)));
                                        }
                                    });
                        }
                    }
                }));
            }
        });
        view.addAction(tableButton);

        final Button withdrawButton = new Button(clientFactory.getCoreMessages().amendmentActionWithdraw());
        withdrawButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                documentEventBus.fireEvent(new AmendmentControllerSelectionActionEvent(new AmendmentControllerSelectionActionEvent.Action() {
                    @Override
                    public void execute(final List<AmendmentController> amendmentControllers) {
                        if (!amendmentControllers.isEmpty()) {
                            serviceFactory.getGwtAmendmentService().withdrawAmendmentContainers(clientFactory.getClientContext(),
                                    transformToDTOs(amendmentControllers),
                                    new AsyncCallback<AmendmentContainerDTO[]>() {
                                        @Override
                                        public void onFailure(Throwable caught) {
                                            clientFactory.getEventBus().fireEvent(new CriticalErrorEvent("Could not withdraw amendment(s).", caught));
                                        }

                                        @Override
                                        public void onSuccess(AmendmentContainerDTO[] result) {
                                            int index = 0;
                                            for (final AmendmentContainerDTO dto : result) {
                                                final AmendmentController amendmentController = amendmentControllers.get(index);
                                                final String oldStatus = amendmentController.getModel().getAmendmentContainerStatus();
                                                amendmentController.setAmendment(dto);
                                                documentEventBus.fireEvent(new AmendmentContainerStatusUpdatedEvent(amendmentController, oldStatus));
                                                index++;
                                            }
                                            documentEventBus.fireEvent(new NotificationEvent(clientFactory.getCoreMessages().amendmentActionWithdrawSuccessful(result.length)));
                                        }
                                    });
                        }
                    }
                }));
            }
        });
        view.addAction(withdrawButton);
    }

    private ArrayList<AmendmentContainerDTO> transformToDTOs(final List<AmendmentController> amendmentControllers) {
        return new ArrayList<AmendmentContainerDTO>(Collections2.transform(amendmentControllers, new Function<AmendmentController, AmendmentContainerDTO>() {
            @Override
            public AmendmentContainerDTO apply(AmendmentController input) {
                return input.getModel();
            }
        }));
    }


    protected void registerSelections() {

        Anchor selectAll = new Anchor("All");
        selectAll.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                documentEventBus.fireEvent(new AmendmentControllerSelectionEvent(new Selection.AllSelection<AmendmentController>()));
            }
        });
        view.addSelection(selectAll);
        Anchor selectNone = new Anchor("None");
        selectNone.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                documentEventBus.fireEvent(new AmendmentControllerSelectionEvent(new Selection.NoneSelection<AmendmentController>()));
            }
        });
        view.addSelection(selectNone);
    }

    private void registerListeners() {

    }
}
