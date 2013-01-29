package org.nsesa.editor.gwt.editor.client.ui.document.sourcefile.actionbar;

import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.inject.ImplementedBy;

/**
 * Date: 24/06/12 21:44
 *
 * @author <a href="philip.luppens@gmail.com">Philip Luppens</a>
 * @version $Id$
 */
@ImplementedBy(ActionBarViewImpl.class)
public interface ActionBarView extends IsWidget {
    FocusWidget getModifyHandler();

    FocusWidget getDeleteHandler();

    FocusWidget getBundleHandler();

    FocusWidget getMoveHandler();

    FocusWidget getChildHandler();

    FocusWidget getTranslateHandler();

    void setLocation(String location);

    void attach();

    void setStyleName(String styleName);
}