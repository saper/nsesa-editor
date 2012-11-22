package org.nsesa.editor.gwt.dialog.client.ui.handler.create;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.nsesa.editor.gwt.dialog.client.ui.handler.modify.author.AuthorPanelController;
import org.nsesa.editor.gwt.dialog.client.ui.handler.modify.author.AuthorPanelView;
import org.nsesa.editor.gwt.dialog.client.ui.rte.RichTextEditor;

/**
 * Date: 24/06/12 21:44
 *
 * @author <a href="philip.luppens@gmail.com">Philip Luppens</a>
 * @version $Id$
 */
public class AmendmentDialogCreateViewImpl extends Composite implements AmendmentDialogCreateView {

    interface MyUiBinder extends UiBinder<Widget, AmendmentDialogCreateViewImpl> {
    }

    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);
    @UiField
    Button cancelButton;
    @UiField
    Button saveButton;
    @UiField
    DockLayoutPanel dockPanel;
    @UiField
    HTML title;

    @UiField(provided = true)
    final RichTextEditor newText;

    @UiField
    TabLayoutPanel tabLayoutPanel;

    @UiField(provided = true)
    AuthorPanelView authorPanelView;

    @Inject
    public AmendmentDialogCreateViewImpl(@Named("newText") final RichTextEditor newText,
                                         final AuthorPanelController authorPanelController) {
        this.newText = newText;
        this.authorPanelView = authorPanelController.getView();
        final Widget widget = uiBinder.createAndBindUi(this);
        initWidget(widget);
    }

    public void setTitle(final String title) {
        this.title.setHTML(title);
    }

    @Override
    protected void onAttach() {
        super.onAttach();
        selectTab(0);
        dockPanel.setHeight("100%");
        dockPanel.setWidth("100%");
        tabLayoutPanel.setWidth("100%");
        tabLayoutPanel.setHeight("100%");
    }

    private void selectTab(final int i) {
        tabLayoutPanel.selectTab(i);
    }

    @Override
    public HasClickHandlers getSaveButton() {
        return saveButton;
    }

    @Override
    public HasClickHandlers getCancelButton() {
        return cancelButton;
    }
}