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
package org.nsesa.editor.gwt.core.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * A reference to an {@link org.nsesa.editor.gwt.core.client.ui.overlay.document.OverlayWidget}, used by a
 * {@link org.nsesa.editor.gwt.amendment.client.amendment.AmendmentInjectionPointFinder} to find the correct
 * widget to amend, and used when passing information when creating a new element.
 * <p/>
 * Date: 10/07/12 22:34
 *
 * @author <a href="mailto:philip.luppens@gmail.com">Philip Luppens</a>
 * @version $Id$
 */
public class AmendableWidgetReference implements IsSerializable {

    private String referenceID;

    /**
     * Boolean flag to see if the reference requires the creation of a new element before injecting
     */
    private boolean creation;

    /**
     * Boolean flag to see if the this reference is a sibling rather than a child, in case of a <tt>creation</tt>
     */
    private boolean sibling;
    /**
     * The namespace URI, if any.
     */
    private String namespaceURI;

    /**
     * The path to the matching node (usually XPath-like, see {@link org.nsesa.editor.gwt.core.client.util.OverlayUtil#xpath(String, org.nsesa.editor.gwt.core.client.ui.overlay.document.OverlayWidget)}
     */
    private String path;

    /**
     * The type name of the widget to create.
     */
    private String type;

    /**
     * The offset at which to create this widget under the parent {@link org.nsesa.editor.gwt.core.client.ui.overlay.document.OverlayWidget}
     */
    private Integer offset;

    public AmendableWidgetReference() {
    }

    public AmendableWidgetReference(boolean creation, boolean sibling, String path, String namespaceURI, String type, int offset) {
        this.creation = creation;
        this.sibling = sibling;
        this.namespaceURI = namespaceURI;
        this.path = path;
        this.type = type;
        this.offset = offset;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isCreation() {
        return creation;
    }

    public void setCreation(boolean creation) {
        this.creation = creation;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public boolean isSibling() {
        return sibling;
    }

    public void setSibling(boolean sibling) {
        this.sibling = sibling;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNamespaceURI() {
        return namespaceURI;
    }

    public void setNamespaceURI(String namespaceURI) {
        this.namespaceURI = namespaceURI;
    }

    public String getReferenceID() {
        return referenceID;
    }

    public void setReferenceID(String referenceID) {
        this.referenceID = referenceID;
    }

    public AmendableWidgetReference deepCopy() {
        final AmendableWidgetReference copy = new AmendableWidgetReference();
        copy.setCreation(creation);
        copy.setNamespaceURI(namespaceURI);
        copy.setOffset(offset);
        copy.setPath(path);
        copy.setReferenceID(referenceID);
        copy.setSibling(sibling);
        copy.setType(type);
        return copy;
    }

    @Override
    public String toString() {
        return "[ref = {offset: " + offset + ", path: " + path + ", type: " + type + ", ns: " + namespaceURI + ", UUID: " + referenceID + "}]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AmendableWidgetReference)) return false;

        AmendableWidgetReference reference = (AmendableWidgetReference) o;

        if (creation != reference.creation) return false;
        if (!offset.equals(reference.offset)) return false;
        if (sibling != reference.sibling) return false;
        if (!namespaceURI.equals(reference.namespaceURI)) return false;
        if (!path.equals(reference.path)) return false;
        if (!referenceID.equals(reference.referenceID)) return false;
        if (!type.equals(reference.type)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = referenceID.hashCode();
        result = 31 * result + (creation ? 1 : 0);
        result = 31 * result + (sibling ? 1 : 0);
        result = 31 * result + namespaceURI.hashCode();
        result = 31 * result + path.hashCode();
        result = 31 * result + type.hashCode();
        result = 31 * result + offset;
        return result;
    }
}
