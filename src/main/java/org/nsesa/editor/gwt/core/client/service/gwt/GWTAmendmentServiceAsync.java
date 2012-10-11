package org.nsesa.editor.gwt.core.client.service.gwt;

import com.google.gwt.user.client.rpc.AsyncCallback;
import org.nsesa.editor.gwt.core.shared.AmendmentContainerDTO;
import org.nsesa.editor.gwt.core.shared.ClientContext;

import java.util.ArrayList;

public interface GWTAmendmentServiceAsync {

    /**
     * Get a single amendment container identified by <tt>id</tt>.
     *
     * @param clientContext the client context
     * @param id            the identifier for a single amendment container
     */
    void getAmendmentContainer(ClientContext clientContext, String id, AsyncCallback<AmendmentContainerDTO> async)
    ;

    /**
     * Get all existing amendment containers that should be visible to this client.
     *
     * @param clientContext the client context
     */
    void getAmendmentContainers(ClientContext clientContext, AsyncCallback<AmendmentContainerDTO[]> async)
    ;

    /**
     * Retrieves all revisions for a given amendment container.
     *
     * @param clientContext the client context
     * @param id            the identifier of the amendment container
     */
    void getRevisions(ClientContext clientContext, String id, AsyncCallback<AmendmentContainerDTO[]> async)
    ;

    /**
     * Save a set of amendment containers to the backend.
     *
     * @param clientContext       the client context
     * @param amendmentContainers the amendment container(s) to save
     */
    void saveAmendmentContainers(ClientContext clientContext, ArrayList<AmendmentContainerDTO> amendmentContainers, AsyncCallback<AmendmentContainerDTO[]> async)
    ;

    /**
     * Deletes a set of amendment containers.
     *
     * @param clientContext       the client context
     * @param amendmentContainers the amendment container(s) to delete
     */
    void deleteAmendmentContainers(ClientContext clientContext, ArrayList<AmendmentContainerDTO> amendmentContainers, AsyncCallback<AmendmentContainerDTO[]> async)
    ;

    /**
     * Tables a set of amendment containers.
     *
     * @param clientContext       the client context
     * @param amendmentContainers the amendment container(s) to table
     */
    void tableAmendmentContainers(ClientContext clientContext, ArrayList<AmendmentContainerDTO> amendmentContainers, AsyncCallback<AmendmentContainerDTO[]> async)
    ;

    /**
     * Withdraws a set of amendment containers.
     *
     * @param clientContext       the client context
     * @param amendmentContainers the amendment container(s) to withdraw
     */
    void withdrawAmendmentContainers(ClientContext clientContext, ArrayList<AmendmentContainerDTO> amendmentContainers, AsyncCallback<AmendmentContainerDTO[]> async)
    ;
}