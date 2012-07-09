package org.nsesa.editor.gwt.core.server.service;

import com.google.common.io.Files;
import org.nsesa.editor.gwt.core.client.service.GWTDocumentService;
import org.nsesa.editor.gwt.core.shared.ClientContext;
import org.nsesa.editor.gwt.core.shared.DocumentDTO;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Date: 24/06/12 19:57
 *
 * @author <a href="philip.luppens@gmail.com">Philip Luppens</a>
 * @version $Id$
 */
public class GWTDocumentServiceImpl extends SpringRemoteServiceServlet implements GWTDocumentService {

    private Map<String, Resource> documents;

    @Override
    public HashMap<String, String> getMetaInformation(final ClientContext clientContext, final String documentID) {
        return null;
    }

    @Override
    public DocumentDTO getDocument(final ClientContext clientContext, final String documentID) {
        final DocumentDTO document = new DocumentDTO();
        document.setDocumentID(documentID);
        document.setAmendable(true);
        document.setName("Document " + documentID);
        document.setLanguageIso("EN");
        return document;
    }

    @Override
    public String getDocumentFragment(final ClientContext clientContext, final String documentID, final String elementID) {
        return null;
    }

    @Override
    public ArrayList<DocumentDTO> getAvailableTranslations(final ClientContext clientContext, final String documentID) {
        ArrayList<DocumentDTO> translations = new ArrayList<DocumentDTO>();

        final DocumentDTO documentDTO1 = new DocumentDTO();
        documentDTO1.setLanguageIso("EN");
        documentDTO1.setName("English");
        documentDTO1.setDocumentID("1-English");
        translations.add(documentDTO1);

        final DocumentDTO documentDTO2 = new DocumentDTO();
        documentDTO2.setLanguageIso("FR");
        documentDTO2.setName("French");
        documentDTO2.setDocumentID("1-French");
        translations.add(documentDTO2);

        final DocumentDTO documentDTO3 = new DocumentDTO();
        documentDTO3.setLanguageIso("DE");
        documentDTO3.setName("German");
        documentDTO3.setDocumentID("1-German");
        translations.add(documentDTO3);

        return translations;
    }

    @Override
    public String getDocumentContent(ClientContext clientContext, String documentID) {
        Resource documentResource = documents.get(documentID);
        if (documentResource != null) {
            try {
                return Files.toString(documentResource.getFile(), Charset.forName("UTF-8"));
            } catch (IOException e) {
                throw new RuntimeException("Could not read file.");
            }
        }
        return null;
    }

    // Spring setters ----------------------


    public void setDocuments(Map<String, Resource> documents) {
        this.documents = documents;
    }
}