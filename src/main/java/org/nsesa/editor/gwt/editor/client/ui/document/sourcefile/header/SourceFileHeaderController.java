package org.nsesa.editor.gwt.editor.client.ui.document.sourcefile.header;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.nsesa.editor.gwt.core.client.mode.ActiveState;
import org.nsesa.editor.gwt.core.client.mode.ConsolidationMode;
import org.nsesa.editor.gwt.core.client.mode.DiffMode;
import org.nsesa.editor.gwt.core.client.mode.InlineEditingMode;
import org.nsesa.editor.gwt.core.client.util.Scope;
import org.nsesa.editor.gwt.core.shared.DocumentDTO;
import org.nsesa.editor.gwt.editor.client.event.document.DocumentModeChangeEvent;
import org.nsesa.editor.gwt.editor.client.event.document.DocumentRefreshRequestEvent;
import org.nsesa.editor.gwt.editor.client.ui.document.DocumentEventBus;
import org.nsesa.editor.gwt.editor.client.ui.document.sourcefile.SourceFileController;

import java.util.ArrayList;
import java.util.List;

import static org.nsesa.editor.gwt.core.client.util.Scope.ScopeValue.DOCUMENT;

/**
 * Date: 24/06/12 18:42
 *
 * @author <a href="philip.luppens@gmail.com">Philip Luppens</a>
 * @version $Id$
 */
@Singleton
@Scope(DOCUMENT)
public class SourceFileHeaderController {

    private final SourceFileHeaderView view;
    private final DocumentEventBus documentEventBus;
    private SourceFileController sourceFileController;

    private List<DocumentDTO> availableTranslations = new ArrayList<DocumentDTO>();
    private List<DocumentDTO> relatedDocuments = new ArrayList<DocumentDTO>();

    // some example modes
    private ToggleButton inlineEditingButton = new ToggleButton("Inline", "Inline", new ClickHandler() {
        @Override
        public void onClick(ClickEvent event) {
            final InlineEditingMode inline = (InlineEditingMode) sourceFileController.getDocumentController().getMode(InlineEditingMode.KEY);
            documentEventBus.fireEvent(new DocumentModeChangeEvent<InlineEditingMode>(sourceFileController.getDocumentController(), inline, new ActiveState(!inline.getState().isActive())));
        }
    });
    private ToggleButton diffingButton = new ToggleButton("Diffing", "Diffing", new ClickHandler() {
        @Override
        public void onClick(ClickEvent event) {
            final DiffMode diff = (DiffMode) sourceFileController.getDocumentController().getMode(DiffMode.KEY);
            documentEventBus.fireEvent(new DocumentModeChangeEvent<DiffMode>(sourceFileController.getDocumentController(), diff, new ActiveState(!diff.getState().isActive())));
        }
    });
    private ToggleButton consolidationButton = new ToggleButton("Consolidation", "Consolidation", new ClickHandler() {
        @Override
        public void onClick(ClickEvent event) {
            final ConsolidationMode consolidationMode = (ConsolidationMode) sourceFileController.getDocumentController().getMode(ConsolidationMode.KEY);
            documentEventBus.fireEvent(new DocumentModeChangeEvent<ConsolidationMode>(sourceFileController.getDocumentController(), consolidationMode, new ActiveState(!consolidationMode.getState().isActive())));
        }
    });

    @Inject
    public SourceFileHeaderController(final DocumentEventBus documentEventBus, final SourceFileHeaderView view) {
        assert view != null : "View is not set --BUG";

        this.view = view;
        this.documentEventBus = documentEventBus;

        view.addWidget(inlineEditingButton);
        view.addWidget(diffingButton);
        view.addWidget(consolidationButton);

        registerListeners();
    }

    private void registerListeners() {
        view.getTranslationsListBox().addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                final String documentID = view.getSelectedTranslationDocumentID();
                if (documentID != null) {

                    // change the document on our parent controller
                    final DocumentDTO document = getTranslationDocument(documentID);
                    if (document == null) {
                        // ? strange, we would not find the document with the given id.
                        // this should only happen if someone changed the list of documents
                        // but not update the translation list box (or if an incorrect value is specified, of course)
                        throw new RuntimeException("Could not find document with documentID " + documentID + " in the list of translations.");
                    }
                    sourceFileController.getDocumentController().setDocument(document);
                    // fire an update to get the new content
                    documentEventBus.fireEvent(new DocumentRefreshRequestEvent(sourceFileController.getDocumentController()));
                }
            }
        });
        view.getRelatedDocumentsListBox().addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                final String documentID = view.getSelectedRelatedDocumentID();
                if (documentID != null) {

                    // change the document on our parent controller
                    final DocumentDTO document = getRelatedDocument(documentID);
                    if (document == null) {
                        throw new RuntimeException("Could not find document with documentID " + documentID + " in the list of related documents.");
                    }
                    sourceFileController.getDocumentController().setDocument(document);
                    // fire an update to get the new content
                    documentEventBus.fireEvent(new DocumentRefreshRequestEvent(sourceFileController.getDocumentController()));
                }
            }
        });
    }

    private DocumentDTO getTranslationDocument(String documentID) {
        for (final DocumentDTO document : availableTranslations) {
            if (documentID.equals(document.getDocumentID())) {
                return document;
            }
        }

        return null;
    }

    private DocumentDTO getRelatedDocument(String documentID) {
        for (final DocumentDTO document : relatedDocuments) {
            if (documentID.equals(document.getDocumentID())) {
                return document;
            }
        }

        return null;
    }

    public SourceFileHeaderView getView() {
        return view;
    }

    public void setAvailableTranslations(final ArrayList<DocumentDTO> translations) {
        this.availableTranslations = translations;
        view.setAvailableTranslations(translations);
    }

    public void setRelatedDocuments(final ArrayList<DocumentDTO> relatedDocuments) {
        this.relatedDocuments = relatedDocuments;
        view.setRelatedDocuments(relatedDocuments);
    }

    public void setSelectedTranslation(final DocumentDTO selectedTranslation) {
        view.setSelectedTranslation(selectedTranslation);
    }

    public void setSelectedRelatedDocument(final DocumentDTO selectedRelatedDocument) {
        view.setSelectedRelatedDocument(selectedRelatedDocument);
    }

    public void setSourceFileController(SourceFileController sourceFileController) {
        this.sourceFileController = sourceFileController;
    }
}
