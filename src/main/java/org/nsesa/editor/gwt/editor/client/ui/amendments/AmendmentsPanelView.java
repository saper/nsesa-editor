package org.nsesa.editor.gwt.editor.client.ui.amendments;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.inject.ImplementedBy;
import org.nsesa.editor.gwt.core.client.ui.amendment.AmendmentController;

import java.util.List;
import java.util.Map;

/**
 * Date: 24/06/12 21:44
 *
 * @author <a href="philip.luppens@gmail.com">Philip Luppens</a>
 * @version $Id$
 */
@ImplementedBy(AmendmentsPanelViewImpl.class)
public interface AmendmentsPanelView extends IsWidget {
    public void setAmendments(Map<String, AmendmentController> amendments);

    public void refreshAmendments(Map<String, AmendmentController> amendments);

    public void setStyleName(String styleName);

    public List<String> getSelectedAmendments();

    public void selectAmendments(List<String> ids);

}
