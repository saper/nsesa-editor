package org.nsesa.editor.gwt.core.client.service.gwt;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import org.nsesa.editor.gwt.core.shared.DiffRequest;
import org.nsesa.editor.gwt.core.shared.DiffResult;

import java.util.ArrayList;

/**
 * Date: 24/06/12 21:05
 *
 * @author <a href="philip.luppens@gmail.com">Philip Luppens</a>
 * @version $Id$
 */
@RemoteServiceRelativePath("gwtDiffService")
public interface GWTDiffService extends RemoteService {

    /**
     * Batch processing of diff requests.
     *
     * @param diffRequests the list of diff commands to execute
     * @return a list of results in the same order as the requests
     */
    ArrayList<DiffResult> diff(ArrayList<DiffRequest> diffRequests);

    /**
     * Returns the current version of this diffing service (so clients now when
     * to update their diffing).
     *
     * @return the version of this diffing service.
     */
    String getVersion();
}