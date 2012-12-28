package org.nsesa.editor.gwt.core.client.ui.amendment.action;

import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.inject.ImplementedBy;

/**
 * Date: 24/06/12 21:44
 *
 * @author <a href="philip.luppens@gmail.com">Philip Luppens</a>
 * @version $Id$
 */
@ImplementedBy(AmendmentActionPanelViewImpl.class)
public interface AmendmentActionPanelView extends IsWidget {

    ComplexPanel getMainPanel();
}
