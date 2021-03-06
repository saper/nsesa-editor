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
package org.nsesa.editor.gwt.core.client.ui.overlay.document;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IsWidget;
import org.nsesa.editor.gwt.core.client.ui.document.OverlayWidgetAware;
import org.nsesa.editor.gwt.core.client.ui.overlay.Format;
import org.nsesa.editor.gwt.core.client.ui.overlay.NumberingType;
import org.nsesa.editor.gwt.core.shared.OverlayWidgetOrigin;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * Interface for an overlay widget (a higher level widget that can be translated into one or more DOM elements).
 * Forms a tree with a parent and children. Can be amendable, immutable and more.
 * <p/>
 * Usually instantiated by an {@link OverlayFactory}.
 * <p/>
 * Date: 27/06/12 17:52
 *
 * @author <a href="mailto:philip.luppens@gmail.com">Philip Luppens</a>
 * @version $Id$
 */
public interface OverlayWidget extends IsWidget, HasWidgets, OverlayWidgetWalker {

    /**
     * Check if this overlay widget is amendable or not.
     *
     * @return true if it is amendable, or null if it is not defined.
     */
    Boolean isAmendable();

    /**
     * Check if this widget has any amendments.
     *
     * @return true if the widget has any amendments on it.
     */
    boolean isAmended();

    /**
     * Check if the overlay has been completed (this might not always be the case for lazy-loading approaches when
     * performance is a problem).
     *
     * @return <tt>true</tt> if the overlay widget was fully loaded (meaning its children were enumerated and set). If
     *         not, we should use the {@link OverlayStrategy} to retrieve and overlay the children.
     */
    boolean areChildrenInitialized();

    /**
     * Set the flag that the children of this overlay widget were retrieved and overlaid.
     *
     * @param childrenInitialized whether or not the children were overlaid
     */
    void setChildrenInitialized(boolean childrenInitialized);

    /**
     * Sets the flag whether or not this amendment is considered amendable (that is,
     * new amendments can be created by the user). Note that you can also
     *
     * @param amendable flag whether or not this widget is amendable - or null if it should inherit it from its
     *                  ancestor.
     */
    void setAmendable(Boolean amendable);

    /**
     * If a widget is immutable, it will not be possible amend this.
     *
     * @return true if this widget is immutable, or <tt>null</tt> if it has not been specified.
     */
    Boolean isImmutable();

    /**
     * Specify whether or not this widget is immutable.
     *
     * @param immutable true if this widget should be immutable, or <tt>null</tt> if it should inherit it from its
     *                  parent.
     */
    void setImmutable(Boolean immutable);

    /**
     * Sets a listener on this widget that will receive callbacks on various UI events suchs as clicks, hoovering, ...
     *
     * @param UIListener the listener to set.
     */
    void setUIListener(OverlayWidgetUIListener UIListener);

    void setListener(OverlayWidgetListener listener);

    /**
     * Sets the logical parent overlay widget - not that this is not necessarily the actual parent element!
     *
     * @param parent the parent to set.
     */
    void setParentOverlayWidget(OverlayWidget parent);

    /**
     * Returns the listing of all the parents overlay widgets, sorted descending by distance to this widget (so
     * first the root, then grandfather, father, ...).
     *
     * @return the list of parent overlay widgets - should not return <tt>null</tt>
     */
    List<OverlayWidget> getParentOverlayWidgets();

    /**
     * Returns the listing of all overlay children, sorted by their appearance.
     *
     * @return the list of overlay child widgets
     */
    List<OverlayWidget> getChildOverlayWidgets();

    /**
     * Get the direct parent overlay widget.
     *
     * @return the parent, or <tt>null</tt> if this is a root widget
     */
    OverlayWidget getParentOverlayWidget();

    /**
     * Get the previous sibling or <tt>null</tt> if there is none.
     *
     * @param overlayWidgetSelector the widget selector
     * @return the previous sibling
     */
    OverlayWidget getPreviousSibling(OverlayWidgetSelector overlayWidgetSelector);

    /**
     * Get the next sibling or <tt>null</tt> if there is none.
     *
     * @param overlayWidgetSelector the widget selector
     * @return the next sibling
     */
    OverlayWidget getNextSibling(OverlayWidgetSelector overlayWidgetSelector);

    /**
     * Returns the next {@link OverlayWidget} in a depth first search.
     *
     * @param overlayWidgetSelector the widget selector
     * @return the next widget, or <tt>null</tt> if there is none
     */
    OverlayWidget next(OverlayWidgetSelector overlayWidgetSelector);

    /**
     * Returns the previous {@link OverlayWidget} in a depth-first search.
     *
     * @param overlayWidgetSelector the widget selector
     * @return the previous widget, or <tt>null</tt> if there is none
     */
    OverlayWidget previous(OverlayWidgetSelector overlayWidgetSelector);

    /**
     * Returns the widget root - if this is not a root itself, it will reverse the tree until a root element is found.
     *
     * @return the root widget which contains this widget.
     */
    OverlayWidget getRoot();

    /**
     * Add an overlay widget as a child.
     *
     * @param child the child to add.
     */
    void addOverlayWidget(OverlayWidget child);

    /**
     * Add an overlay widget as a child at the given index.
     *
     * @param child the child to add.
     * @param index the index where to add the child (-1 means at the end)
     */
    void addOverlayWidget(OverlayWidget child, int index);

    /**
     * Called when adopting the lower level amendable element into the DOM tree.
     */
    void onAttach();

    /**
     * Check if the underlying amendable element is attached to the DOM or not.
     *
     * @return <tt>true</tt> if it is attached
     */
    boolean isAttached();

    /**
     * Called when the underlying element is detached from the DOM.
     */
    void onDetach();

    /**
     * Add an overlay widget as a child at position <tt>index</tt>, but do not perform a runtime validation check.
     *
     * @param child          the child to add
     * @param index          the position to insert the widget at (-1 means it will be added at the end)
     * @param skipValidation <tt>true</tt> to skip validation.
     */
    void addOverlayWidget(OverlayWidget child, int index, boolean skipValidation);

    /**
     * Remove an overlay child. Throws an exception if the passed widget is not an actual child (that is,
     * {@link #getChildOverlayWidgets()} contains the widget).
     *
     * @param child the child widget to remove.
     */
    void removeOverlayWidget(OverlayWidget child);

    /**
     * Add an amendment controller to this overlay widget.
     *
     * @param amendmentController the amendment controller to add.
     */
    void addOverlayWidgetAware(OverlayWidgetAware amendmentController);

    /**
     * Remove an amendment controller from this overlay widget.
     *
     * @param amendmentController the amendment controller to remove.
     */
    void removeAmendmentController(OverlayWidgetAware amendmentController);

    /**
     * Returns the type of the overlay widget (defaults to the local node name - so without prefix, if any).
     *
     * @return the type
     */
    String getType();

    /**
     * Set the type of this overlay widget. You are normally not supposed to call this method directly (it will be
     * set by the {@link OverlayFactory}.
     *
     * @param type the type of this overlay widget (eg. chapter, speech, b, ...)
     */
    void setType(String type);

    /**
     * Get the id of this overlay widget, if any.
     *
     * @return the id of this widget.
     */
    String getId();

    /**
     * Set the id of this overlay widget. Must be unique (we won't check for this, so it is your responsibility).
     *
     * @param id the id of this widget.
     */
    void setId(String id);

    /**
     * Get the child content (text + other elements) for this overlay widget.
     *
     * @return the child nodes serialized as a <tt>String</tt>.
     */
    String getInnerHTML();

    /**
     * Set the text content of this overlay widget. While this technically could contain tags, you are advised
     * to properly escape any content you wish to set.
     *
     * @param innerHTML the text content for this node.
     */
    void setInnerHTML(String innerHTML);

    /**
     * Get the HTMLPanel to attach the amendments on. If it does not yet exist, it will be created and attached
     * to the document.
     *
     * @return the HTML panel with the amendments, if any.
     */
    HTMLPanel getAmendmentControllersHolderElement();

    /**
     * Get the structure indicator as it is coming from xsd
     *
     * @return The structure indicator
     */
    StructureIndicator getStructureIndicator();

    /**
     * Get the numbering type of this overlay widget. If it was not set using {@link #setNumberingType(org.nsesa.editor.gwt.core.client.ui.overlay.NumberingType)},
     * then it will be guessed.
     *
     * @return the numbering type, if any.
     */
    NumberingType getNumberingType();

    /**
     * Set the numbering type on this overlay widget (eg. '1', 'A', '-', ...). Typically used in conjunction with
     * {@link #getFormat()} to format the literal index.
     *
     * @param numberingType the numbering type to use
     */
    void setNumberingType(NumberingType numberingType);

    /**
     * Get the format for this overlay widget. If it was not set using {@link #setFormat(org.nsesa.editor.gwt.core.client.ui.overlay.Format)},
     * then the format will be guessed.
     *
     * @return the format for this literal index, if any.
     */
    Format getFormat();

    /**
     * Set the format on this overlay widget (eg. ')', '()', '.', ...). The format is typically used in conjunction with the {@link #getNumberingType()}
     * to create the literal index.
     *
     * @param format the formatter to use for the literal index.
     */
    void setFormat(Format format);

    /**
     * Get the assigned number, or <tt>null</tt> if no number was assigned. Note that this is 1-based.
     *
     * @return the assigned number.
     */
    Integer getAssignedNumber();

    /**
     * Set the assigned number (1-based). This assigned number is then used for location determination in case no
     * num element is provided.
     *
     * @param assignedNumber the assigned number
     */
    void setAssignedNumber(Integer assignedNumber);

    /**
     * Get a map of all the attributes, where the key is the attribute name, and the value is the attribute's value.
     *
     * @return the map of attributes, should never return <tt>null</tt>
     */
    LinkedHashMap<String, String> getAttributes();

    /**
     * Set the origin of this widget.
     *
     * @param origin the origin of this widget.
     */
    void setOrigin(OverlayWidgetOrigin origin);

    /**
     * Returns the origin of this overlay widget, be it part of the document, or introduced by an amendment.
     *
     * @return the origin, cannot be <tt>null</tt>
     */
    OverlayWidgetOrigin getOrigin();

    /**
     * Check if this overlay widget is introduced by an amendment (meaning, its {@link OverlayWidgetImpl#getOrigin()}
     * is equal to {@link org.nsesa.editor.gwt.core.shared.OverlayWidgetOrigin#AMENDMENT}
     *
     * @return <tt>true</tt> if it was introduced by an amendment
     */
    boolean isIntroducedByAnAmendment();

    /**
     * Check if the overlay widget('s content) is generated (eg. by a template or snippet). We assume that a widget
     * is generated if its origin is {@link org.nsesa.editor.gwt.core.shared.OverlayWidgetOrigin#GENERATED} or
     * the content is blank, and this for the current widget and all its children.
     * @return <tt>true</tt> if the overlay widget is generated (eg. as part of a template).
     */
    boolean isGenerated();

    /**
     * Get the underlying element. Should never be <tt>null</tt>.
     *
     * @return the element.
     */
    Element getOverlayElement();

    /**
     * Returns all amendment controllers on this overlay widget.
     *
     * @return the amendment controllers.
     */
    List<OverlayWidgetAware> getOverlayWidgetAwareList();

    /**
     * Get the index of this widget in its parent's widget collection, but only counting the same types (so, if there
     * is another widget of another type in between, it will not be counted) <b>and</b> widgets that have not been
     * introduced by amendments (if you wish to control this, use {@link #getTypeIndex(boolean)} instead.
     *
     * @return the index, filtered by type, or -1 if it cannot be found.
     */
    int getTypeIndex();

    /**
     * Get the index of this widget in its parent's widget collection, but only counting the same types (so, if there
     * is another widget of another type in between, it will not be counted).
     *
     * @param includeAmendments if true, widgets with the same type that have been introduced by amendments, will
     *                          also be taken into account.
     * @return the index, filtered by type, or -1 if it cannot be found.
     */
    int getTypeIndex(final boolean includeAmendments);

    /**
     * Get the index of this widget in its parent's widget collection, but only counting widgets that have not been
     * introduced by amendments.
     *
     * @return the index, or -1 if it cannot be found.
     */
    int getIndex();

    /**
     * Get the index of this element in the DOM tree. Useful for inserting or DOM operations.
     *
     * @return the DOM index.
     */
    int getDomIndex();

    /**
     * Returns the namespace this overlay widget was generated for.
     *
     * @return the namespace URI.
     */
    String getNamespaceURI();

    /**
     * Set the overlay strategy (used for lazy loading)
     *
     * @param overlayStrategy the overlay strategy
     */
    void setOverlayStrategy(OverlayStrategy overlayStrategy);

    /**
     * Get the unformatted index, if any.
     *
     * @return the unformatted index.
     */
    String getUnformattedIndex();

    /**
     * Set the unformatted index for this widget.
     * @param unformattedIndex the unformatted index - use <tt>null</tt> to reset to the attribute.
     */
    void setUnformattedIndex(String unformattedIndex);

    /**
     * Get the formatted index, if any.
     *
     * @return the formatted index.
     */
    String getFormattedIndex();

    /**
     * Set the formatted index for this widget.
     * @param formattedIndex the formatted index - use <tt>null</tt> to reset to the attribute.
     */
    void setFormattedIndex(String formattedIndex);

    /**
     * Move this widget up in the parent collection.
     *
     * @return <tt>true</tt> if the widget has been moved up
     */
    boolean moveUp();

    /**
     * Move this widget down in the parent collection
     *
     * @return <tt>true</tt> if the widget has been moved down
     */
    boolean moveDown();

    /**
     * Check if this overlay widget is under (meaning one of its matches the given arguments) a certain given overlay widget identified via
     * the <tt>namespaceURI</tt> and its <tt>type</tt>.
     *
     * @param namespaceURI the namespace uri
     * @param type         the type
     * @return <tt>true</tt> if it has parent
     */
    boolean hasParent(String namespaceURI, String type);

    /**
     * Returns a list of allowed child types under this overlay widget.
     *
     * @return the list of allowed overlay widget child types.
     */
    List<OverlayWidget> getAllowedChildTypes();
}
