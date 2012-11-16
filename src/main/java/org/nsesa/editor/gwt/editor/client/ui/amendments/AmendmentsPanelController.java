package org.nsesa.editor.gwt.editor.client.ui.amendments;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.nsesa.editor.gwt.core.client.util.Scope;

import static org.nsesa.editor.gwt.core.client.util.Scope.ScopeValue.EDITOR;

/**
 * Date: 24/06/12 21:42
 *
 * @author <a href="philip.luppens@gmail.com">Philip Luppens</a>
 * @version $Id$
 */
@Singleton
@Scope(EDITOR)
public class AmendmentsPanelController {
    private final AmendmentsPanelView view;

    @Inject
    public AmendmentsPanelController(AmendmentsPanelView view) {
        this.view = view;
    }

    public AmendmentsPanelView getView() {
        return view;
    }
}
