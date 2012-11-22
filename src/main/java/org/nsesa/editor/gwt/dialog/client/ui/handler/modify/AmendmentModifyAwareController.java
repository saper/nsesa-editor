package org.nsesa.editor.gwt.dialog.client.ui.handler.modify;

import com.google.gwt.user.client.ui.IsWidget;
import org.nsesa.editor.gwt.core.client.ui.overlay.document.AmendableWidget;
import org.nsesa.editor.gwt.core.shared.AmendmentContainerDTO;

/**
 * Date: 22/11/12 11:43
 *
 * @author <a href="philip.luppens@gmail.com">Philip Luppens</a>
 * @version $Id$
 */
public interface AmendmentModifyAwareController {

    IsWidget getView();

    String getTitle();

    boolean validate();

    void setAmendmentAndAmendableWidget(AmendmentContainerDTO amendment, AmendableWidget amendableWidget);
}