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
package org.nsesa.editor.gwt.dialog.client.ui.handler.common.author;

import com.google.inject.Inject;
import org.nsesa.editor.gwt.core.shared.PersonDTO;

/**
 * Simple controller for a person.
 * Date: 24/06/12 21:42
 *
 * @author <a href="mailto:philip.luppens@gmail.com">Philip Luppens</a>
 * @version $Id$
 */
public class AuthorController {

    /**
     * The main view.
     */
    protected final AuthorView view;

    protected PersonDTO person;

    @Inject
    public AuthorController(final AuthorView view) {
        this.view = view;
    }

    public PersonDTO getPerson() {
        return person;
    }

    public void setPerson(PersonDTO person) {
        this.person = person;
        view.setName(person.getDisplayName());
    }

    public void registerListeners() {

    }

    /**
     * Removes all registered event handlers from the event bus and UI.
     */
    public void removeListeners() {
    }

    /**
     * Return the view
     *
     * @return the view
     */
    public AuthorView getView() {
        return view;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AuthorController)) return false;

        AuthorController that = (AuthorController) o;

        if (person != null ? !person.equals(that.person) : that.person != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return person != null ? person.hashCode() : 0;
    }
}
