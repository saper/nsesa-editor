/**
 * Copyright 2013 European Parliament
 *
 * Licensed under the EUPL, Version 1.1 or - as soon they will be approved by the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 *
 * http://joinup.ec.europa.eu/software/page/eupl
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the Licence is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and limitations under the Licence.
 */
package org.nsesa.editor.gwt.core.client.validation;

import org.nsesa.editor.gwt.core.client.ui.overlay.document.OverlayWidget;

/**
 * A interface for a result of a validation done by a {@link Validator}.
 * Date: 19/02/13 13:45
 *
 * @author <a href="mailto:philip.luppens@gmail.com">Philip Luppens</a>
 * @version $Id$
 */
public interface ValidationResult {

    /**
     * Check if the validation was successful or not.
     * @return <tt>true</tt> if the validation was successful
     */
    boolean isSuccessful();

    /**
     * If the validation was not successful, retrieve the error message.
     * @return the error message, or <tt>null</tt> if no error message was given.
     */
    String getErrorMessage();

    /**
     * If the validation was not successful return the widget that cause problems, otherwise <code>null</code>
     * value will be returned. It might be the case to return null values for cases where the validation fails
     * but there is no invalid widget to return eg when check for well formed.
     * @return {@link OverlayWidget} widget that cause problems
     */
    OverlayWidget getInvalidWidget();

}
