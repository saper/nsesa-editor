package org.nsesa.editor.gwt.core.client.ui.amendment;

import com.google.gwt.user.client.ui.IsWidget;

/**
 * Date: 24/06/12 21:44
 *
 * @author <a href="philip.luppens@gmail.com">Philip Luppens</a>
 * @version $Id$
 */
public interface AmendmentView extends IsWidget {

    void setJustification(String justification);

    void setTitle(String title);
}