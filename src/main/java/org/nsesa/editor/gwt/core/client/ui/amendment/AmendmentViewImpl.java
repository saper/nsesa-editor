package org.nsesa.editor.gwt.core.client.ui.amendment;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import com.google.inject.Singleton;

/**
 * Date: 24/06/12 21:44
 *
 * @author <a href="philip.luppens@gmail.com">Philip Luppens</a>
 * @version $Id$
 */
@Singleton
public class AmendmentViewImpl extends Composite implements AmendmentView {
    interface MyUiBinder extends UiBinder<Widget, AmendmentViewImpl> {
    }

    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);
    @UiField
    FlexTable amendmentContentTable;
    @UiField
    FlexTable originalContentTable;
    @UiField
    Label title;
    @UiField
    Label justification;

    public AmendmentViewImpl() {
        final Widget widget = uiBinder.createAndBindUi(this);
        initWidget(widget);
    }

    @Override
    public void setJustification(String justification) {
        this.justification.setText(justification);
    }

    @Override
    public void setTitle(String title) {
        super.setTitle(title);
        this.title.setText(title);
    }

    // TODO: use safe HTML instead
    public void addAmendmentContentPart(final IsWidget part) {
        amendmentContentTable.add(part);
    }

    public void addOriginalContentPart(final IsWidget part) {
        originalContentTable.add(part);
    }

    public void clear() {
        amendmentContentTable.clear(true);
    }

    public void clearOriginalContainerTable() {
        originalContentTable.clear(true);
    }


}
