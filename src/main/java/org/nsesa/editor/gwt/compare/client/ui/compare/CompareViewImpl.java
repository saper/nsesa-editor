/**
 * Copyright 2013 European Parliament
 *
 * Licensed under the EUPL, Version 1.1 or - as soon they will be approved by the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 *
 * http://joinup.ec.europa.eu/software/page/eupl
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the Licence is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and limitations under the Licence.
 */
package org.nsesa.editor.gwt.compare.client.ui.compare;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.nsesa.editor.gwt.core.shared.RevisionDTO;

import java.util.List;

/**
 * Default implementation of the {@link CompareView} using UIBinder.
 * Date: 24/06/12 21:44
 *
 * @author <a href="mailto:philip.luppens@gmail.com">Philip Luppens</a>
 * @version $Id$
 */
@Singleton
public class CompareViewImpl extends Composite implements CompareView {

    interface MyUiBinder extends UiBinder<Widget, CompareViewImpl> {
    }

    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

    @UiField
    Button rollbackButton;
    @UiField
    Anchor cancelAnchor;

    @UiField
    ScrollPanel scrollPanel;
    @UiField
    HTMLPanel richTextEditor;
    @UiField
    HorizontalPanel revisionsPanel;
    @UiField
    ListBox revisionsA;
    @UiField
    ListBox revisionsB;
    @UiField
    HorizontalPanel revisionPickerPanel;
    @UiField
    Label revisionVersusLabel;
    @UiField
    HorizontalPanel timeline;

    @Inject
    public CompareViewImpl() {
        final Widget widget = uiBinder.createAndBindUi(this);
        initWidget(widget);
        //show class name tool tip in hosted mode
        if (!GWT.isScript())
            widget.setTitle(this.getClass().getName());

        revisionPickerPanel.setCellHorizontalAlignment(revisionsA, HasHorizontalAlignment.ALIGN_CENTER);
        revisionPickerPanel.setCellHorizontalAlignment(revisionVersusLabel, HasHorizontalAlignment.ALIGN_CENTER);
        revisionPickerPanel.setCellHorizontalAlignment(revisionsB, HasHorizontalAlignment.ALIGN_CENTER);
    }

    @Override
    public Button getRollbackButton() {
        return rollbackButton;
    }

    @Override
    public Anchor getCancelAnchor() {
        return cancelAnchor;
    }

    @Override
    public void adaptScrollPanel() {
        if (getOffsetHeight() > 0) {
            scrollPanel.setHeight((getOffsetHeight() - 110) + "px");
        }
        if (getOffsetWidth() > 0) {
            scrollPanel.setWidth((getOffsetWidth()) + "px");
        }
    }

    @Override
    public void setAvailableRevisions(List<RevisionDTO> revisions) {
        revisionsA.clear();
        revisionsB.clear();

        revisionPickerPanel.setVisible(revisions.size() > 1);
        rollbackButton.setEnabled(revisions.size() > 1);

        if (revisions.size() > 1) {
            List<RevisionDTO> revisionDTOsWithoutLatest = revisions.subList(1, revisions.size());
            int versionA = revisionDTOsWithoutLatest.size();
            int versionB = revisions.size();

            for (RevisionDTO revision : revisionDTOsWithoutLatest) {
                final String format = DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_TIME_SHORT).format(revision.getCreationDate());
                String entry = versionA-- + ") " + format + " - " + revision.getPerson().getDisplayName();
                if (revision == revisions.get(revisions.size() - 1)) {
                    // last element
                    revisionsA.addItem(entry + " (Initial version)", revision.getRevisionID());
                } else {
                    revisionsA.addItem(entry, revision.getRevisionID());
                }
            }

            for (RevisionDTO revision : revisions) {
                final String format = DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_TIME_SHORT).format(revision.getCreationDate());
                String entry = versionB-- + ") " + format + " - " + revision.getPerson().getDisplayName();
                if (revision == revisions.get(0)) {
                    // first element
                    revisionsB.addItem(entry + " (Latest version)", revision.getRevisionID());
                } else if (revision == revisions.get(revisions.size() - 1)) {
                    // last element
                    revisionsB.addItem(entry + " (Initial version)", revision.getRevisionID());
                } else {
                    revisionsB.addItem(entry, revision.getRevisionID());
                }
            }
        }
    }

    @Override
    public void setRevision(String revisionContent) {
        richTextEditor.getElement().setInnerHTML(revisionContent);
    }

    @Override
    public ListBox getRevisionsA() {
        return revisionsA;
    }

    @Override
    public ListBox getRevisionsB() {
        return revisionsB;
    }

    @Override
    public HorizontalPanel getTimeline() {
        return timeline;
    }
}
