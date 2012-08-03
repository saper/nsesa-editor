package org.nsesa.editor.gwt.editor.client.ui.header;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.inject.ImplementedBy;

/**
 * Date: 24/06/12 21:44
 *
 * @author <a href="philip.luppens@gmail.com">Philip Luppens</a>
 * @version $Id$
 */
@ImplementedBy(HeaderViewImpl.class)
public interface HeaderView extends IsWidget {
    void setStyleName(String style);
}
