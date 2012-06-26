package org.nsesa.editor.gwt.core.server.service;

import org.joda.time.DateTime;
import org.nsesa.editor.gwt.core.client.service.GWTService;
import org.nsesa.editor.gwt.core.shared.ClientContext;

/**
 * Date: 24/06/12 19:57
 *
 * @author <a href="philip.luppens@gmail.com">Philip Luppens</a>
 * @version $Id$
 */
public class GWTServiceImpl extends SpringRemoteServiceServlet implements GWTService {
    @Override
    public ClientContext authenticate(ClientContext clientContext) {
        clientContext.setPrincipal("dummy-" + new DateTime().getMillis());
        clientContext.setRoles(new String[]{"USER", "ADMIN"});
        return clientContext;
    }
}
