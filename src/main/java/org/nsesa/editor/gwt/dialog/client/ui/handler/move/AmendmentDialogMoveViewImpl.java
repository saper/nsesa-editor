package org.nsesa.editor.gwt.dialog.client.ui.handler.move;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import com.google.inject.Inject;

/**
 * Date: 24/06/12 21:44
 *
 * @author <a href="philip.luppens@gmail.com">Philip Luppens</a>
 * @version $Id$
 */
public class AmendmentDialogMoveViewImpl extends Composite implements AmendmentDialogMoveView {

    interface MyUiBinder extends UiBinder<Widget, AmendmentDialogMoveViewImpl> {
    }

    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);
    @UiField
    Anchor cancelLink;
    @UiField
    Button saveButton;
    @UiField
    DockLayoutPanel dockPanel;

    @Inject
    public AmendmentDialogMoveViewImpl() {
        final Widget widget = uiBinder.createAndBindUi(this);
        initWidget(widget);
    }

    @Override
    public HasClickHandlers getSaveButton() {
        return saveButton;
    }

    @Override
    public HasClickHandlers getCancelLink() {
        return cancelLink;
    }
}
