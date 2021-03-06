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
package org.nsesa.editor.gwt.dialog.client.ui.handler.common;

import com.google.gwt.inject.client.AbstractGinModule;
import org.nsesa.editor.gwt.dialog.client.ui.handler.common.authors.AuthorsPanelModule;
import org.nsesa.editor.gwt.dialog.client.ui.handler.common.content.ContentPanelModule;
import org.nsesa.editor.gwt.dialog.client.ui.handler.common.meta.MetaPanelModule;

/**
 * GIN module for the common panel components such as author, meta, or original content.
 * Date: 09/01/13 11:05
 *
 * @author <a href="mailto:philip.luppens@gmail.com">Philip Luppens</a>
 * @version $Id$
 */
public class AmendmentDialogCommonModule extends AbstractGinModule {
    @Override
    protected void configure() {
        install(new AuthorsPanelModule());
        install(new MetaPanelModule());
        install(new ContentPanelModule());
    }
}
