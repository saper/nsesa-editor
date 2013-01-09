package org.nsesa.editor.gwt.dialog.client.ui.handler.common.content;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Names;
import org.nsesa.editor.gwt.core.client.ClientFactory;
import org.nsesa.editor.gwt.dialog.client.ui.rte.RichTextEditor;
import org.nsesa.editor.gwt.dialog.client.ui.rte.ckeditor.CKEditor;

import javax.inject.Named;

/**
 * Date: 24/06/12 15:11
 *
 * @author <a href="philip.luppens@gmail.com">Philip Luppens</a>
 * @version $Id$
 */
public class ContentPanelModule extends AbstractGinModule {
    @Override
    public void configure() {
        bind(RichTextEditor.class).annotatedWith(Names.named("originalText")).toProvider(OriginalTextProvider.class);
    }

    public static class OriginalTextProvider implements Provider<RichTextEditor> {

        @Inject
        ClientFactory clientFactory;

        @Inject
        @Named("richTextEditorCss")
        String cssPath;

        @Inject
        @Named("richTextEditorBodyClass")
        String bodyClass;

        @Override
        public RichTextEditor get() {
            return new CKEditor(cssPath, bodyClass, true);
        }
    }
}