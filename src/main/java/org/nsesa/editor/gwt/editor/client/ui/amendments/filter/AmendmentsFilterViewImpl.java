package org.nsesa.editor.gwt.editor.client.ui.amendments.filter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.nsesa.editor.gwt.core.client.util.Filter;
import org.nsesa.editor.gwt.core.client.util.Scope;
import org.nsesa.editor.gwt.core.client.util.Selection;
import org.nsesa.editor.gwt.editor.client.event.amendments.AmendmentsAction;
import org.nsesa.editor.gwt.editor.client.event.amendments.AmendmentsActionEvent;
import org.nsesa.editor.gwt.editor.client.event.amendments.AmendmentsSelectionEvent;
import org.nsesa.editor.gwt.editor.client.event.filter.FilterRequestEvent;
import org.nsesa.editor.gwt.editor.client.ui.document.DocumentEventBus;

import java.util.List;

import static org.nsesa.editor.gwt.core.client.util.Scope.ScopeValue.DOCUMENT;

/**
 * Implementation for AmendmentsFilterView interface
 * User: groza
 * Date: 26/11/12
 * Time: 11:51
 * To change this template use File | Settings | File Templates.
 */
@Singleton
@Scope(DOCUMENT)
public class AmendmentsFilterViewImpl extends Composite implements AmendmentsFilterView {

    private DocumentEventBus documentEventBus;

    interface MyUiBinder extends UiBinder<Widget, AmendmentsFilterViewImpl> {
    }
    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

    @UiField
    MenuBar menuFilter;

    @Inject
    public AmendmentsFilterViewImpl(DocumentEventBus documentEventBus) {
        this.documentEventBus = documentEventBus;
        final Widget widget = uiBinder.createAndBindUi(this);
        initWidget(widget);
    }

    @Override
    public Widget asWidget() {
        return this;
    }
    @Override
    public void setFilters(List<String> filterList) {
        for (final String filter : filterList) {
            this.menuFilter.addItem(filter, new Scheduler.ScheduledCommand() {
                @Override
                public void execute() {
                    documentEventBus.fireEvent(new FilterRequestEvent(new Filter(0,2, "order")));
                }
            });
        }

    }
}
