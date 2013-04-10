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
import com.google.gwt.dom.client.Node;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.*;
import org.nsesa.editor.gwt.core.client.ui.amendment.AmendmentController;
import org.nsesa.editor.gwt.core.client.ui.overlay.Format;
import org.nsesa.editor.gwt.core.client.ui.overlay.NumberingType;
import org.nsesa.editor.gwt.core.client.util.NodeUtil;
import org.nsesa.editor.gwt.core.shared.OverlayWidgetOrigin;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Default implementation of the {@link OverlayWidget}. This is the main node class
 * in the overlay tree as it is generated by the {@link OverlayFactory}.
 * <p/>
 * This class is supposed to be subclassed by higher level widgets generated using the
 * {@link org.nsesa.editor.app.xsd.model.OverlayClassGenerator}s.
 * <p/>
 * Date: 27/06/12 17:52
 *
 * @author <a href="mailto:philip.luppens@gmail.com">Philip Luppens</a>
 * @version $Id$
 */
public class OverlayWidgetImpl extends ComplexPanel implements OverlayWidget, HasWidgets {

    private static final Logger LOG = Logger.getLogger(OverlayWidgetImpl.class.getName());

    /**
     * Default amendable flag for the root node if no attribute has been set.
     */
    public static final boolean DEFAULT_ROOT_WIDGET_AMENDABLE = true;

    /**
     * A listener for all the UI operations to call back on.
     */
    private OverlayWidgetUIListener UIListener;

    /**
     * A listener for all the amending operations to call back on.
     */
    private OverlayWidgetListener listener;

    /**
     * Supports lazy retrieval of properties
     */
    private OverlayStrategy overlayStrategy;

    /**
     * The logical parent overlay widget. If the parent is null, this is considered a root widget.
     */
    private OverlayWidget parentOverlayWidget;

    /**
     * A list of logical children.
     */
    private List<OverlayWidget> childOverlayWidgets = new ArrayList<OverlayWidget>();

    /**
     * A list of all the amendments on this widget.
     */
    private List<AmendmentController> amendmentControllers = new ArrayList<AmendmentController>();

    /**
     * Flag to indicate whether or not this widget is amendable by the user (not, that does not mean there are no
     * amendments on this element). Note that this value, if set, cascades into its children.
     */
    private Boolean amendable;

    /**
     * Type of the overlay widget - translates to the noden ame of the underlying element without namespace.
     */
    private String type;

    /**
     * Id of the underlying element.
     */
    private String id;

    /**
     * Origin of this widget - can be introduced by an amendment, or already exist in the source text.
     */
    private OverlayWidgetOrigin origin;

    /**
     * Flag to indicate that this widget is immutable. We use this flag to set a single element as non-amendable
     * without having to set each of its children as amendable. Does <strong>not</strong> cascade into its children.
     */
    private Boolean immutable;

    /**
     * The {@link Format} of the num element, if any.
     */
    private Format format;

    /**
     * The numbering type of the num element, if any.
     */
    private NumberingType numberingType;

    /**
     * The assigned index: this is used to determine the location in case the index is not sufficient to uniquely
     * identify this widget (eg. in case of indents, bullet points ...)
     * Note: this number is <strong>1-based</strong>!!
     */
    private Integer assignedNumber;

    /**
     * Contains the unformatted index of this overlay widget (eg. 'a', '2', '-', ...)
     */
    private String unformattedIndex;

    /**
     * Contains the formatted, original index of this overlay widget (eg. 'a)', '(2)', '-', '', ...)
     */
    private String formattedIndex;

    /**
     * The holder element for the amendments.
     */
    protected HTMLPanel amendmentControllersHolderElement;

    /**
     * keep a list with the allowed children that could be added*
     */
    private List<OverlayWidget> allowedChildren;

    /**
     * Default constructor.
     */
    public OverlayWidgetImpl() {
    }

    /**
     * Constructor for overlying by passing the underlying element.
     *
     * @param overlayElement the underlying element
     */
    public OverlayWidgetImpl(Element overlayElement) {
        setElement(overlayElement);
        // if we're not yet part of the DOM tree, try to attach
        if (!isAttached()) {
            onAttach();
        }
    }

    @Override
    public void setOverlayStrategy(OverlayStrategy overlayStrategy) {
        this.overlayStrategy = overlayStrategy;
    }

    @Override
    public void addOverlayWidget(final OverlayWidget child) {
        addOverlayWidget(child, -1);
    }

    @Override
    public void addOverlayWidget(OverlayWidget child, int index) {
        addOverlayWidget(child, index, false);
    }

    @Override
    public void onAttach() {
        super.onAttach();
    }

    @Override
    public void onDetach() {
        // clean up as much as possible
        if (childOverlayWidgets != null) {
            for (final OverlayWidget child : childOverlayWidgets) {
                if (child.isAttached()) {
                    child.onDetach();
                }
            }
        }
        if (isAttached()) {
            super.onDetach();
            this.listener = null;
            this.overlayStrategy = null;
            this.origin = null;
            this.amendable = null;
            this.amendmentControllers = null;
            this.assignedNumber = null;
            this.amendmentControllersHolderElement = null;
            this.childOverlayWidgets = null;
            this.format = null;
            this.numberingType = null;
            this.id = null;
            this.immutable = null;
            this.type = null;
            this.UIListener = null;
        }
    }

    @Override
    public void addOverlayWidget(final OverlayWidget child, int index, boolean skipValidation) {
        if (child == null) throw new NullPointerException("Cannot add null child!");
        boolean vetoed = false;
        if (listener != null)
            vetoed = listener.beforeOverlayWidgetAdded(this, child);

        if (!vetoed) {
            if (!skipValidation) {
                // see if there is a wildcard in the allowed subtypes,
                OverlayWidget wildCard = null;
                if (!getAllowedChildTypes().contains(wildCard)) {
                    // no wildcard - see if the type is supported as a child widget
                    boolean canAdd = false;
                    for (OverlayWidget allowed : getAllowedChildTypes()) {
                        if (allowed.getType().equalsIgnoreCase(child.getType()) &&
                                allowed.getNamespaceURI().equalsIgnoreCase(child.getNamespaceURI())) {
                            canAdd = true;
                        }
                    }
                    if (!canAdd) {
                        LOG.warning(getType() + " does not support child type:" + child);
                    }
                }

                // if our child was somehow still connected to another widget, then clear this reference first
                if (child.getParentOverlayWidget() != null) {
                    if (child.getParentOverlayWidget().getChildOverlayWidgets().contains(child)) {
                        child.getParentOverlayWidget().removeOverlayWidget(child);
                    }
                }
            }
            if (index == -1) {
                if (!childOverlayWidgets.add(child)) {
                    throw new RuntimeException("Child already exists: " + child.getType());
                }
            } else {
                if (index > childOverlayWidgets.size()) {
                    throw new RuntimeException("Could not insert child Overlay widget at index " + index +
                            " because the size of the Overlay children is only " + childOverlayWidgets.size());
                }
                childOverlayWidgets.add(index, child);
            }
            child.setParentOverlayWidget(this);
            // inform the listener
            if (listener != null) listener.afterOverlayWidgetAdded(this, child);
        } else {
            LOG.info("OverlayWidget listener veto'ed the adding of the overlay widget.");
        }
    }

    @Override
    public void removeOverlayWidget(final OverlayWidget child) {
        if (child == null) throw new NullPointerException("Cannot remove null child!");

        boolean vetoed = false;
        if (listener != null)
            vetoed = listener.beforeOverlayWidgetRemoved(this, child);

        if (!vetoed) {
            if (!childOverlayWidgets.remove(child)) {
                throw new RuntimeException("Child widget not found: " + child);
            }
            child.setParentOverlayWidget(null);
            // inform the listener
            if (listener != null) listener.afterOverlayWidgetRemoved(this, child);
        } else {
            LOG.info("OverlayWidget listener veto'ed the removal of the overlay widget.");
        }
    }

    @Override
    public void addAmendmentController(final AmendmentController amendmentController) {
        if (amendmentController == null) throw new NullPointerException("Cannot add null amendment controller!");

        boolean vetoed = false;
        if (listener != null)
            vetoed = listener.beforeAmendmentControllerAdded(this, amendmentController);

        if (!vetoed) {
            if (amendmentControllers.contains(amendmentController)) {
                throw new RuntimeException("Amendment already exists: " + amendmentController);
            }
            if (!amendmentControllers.add(amendmentController)) {
                throw new RuntimeException("Could not add amendment controller: " + amendmentController);
            }

            // physical attach
            final HTMLPanel holderElement = getAmendmentControllersHolderElement();
            if (holderElement != null) {
                holderElement.add(amendmentController.getView());
                // set up a reference to this widget
                amendmentController.setAmendedOverlayWidget(this);
                // inform the listener
                if (listener != null) listener.afterAmendmentControllerAdded(this, amendmentController);
            } else {
                LOG.severe("No amendment holder panel could be added for this widget " + this);
            }
        } else {
            LOG.info("OverlayWidget listener veto'ed the adding of the amendment controller.");
        }
    }

    @Override
    public void removeAmendmentController(final AmendmentController amendmentController) {
        if (amendmentController == null) throw new NullPointerException("Cannot remove null amendment controller!");

        boolean vetoed = false;
        if (listener != null)
            vetoed = listener.beforeAmendmentControllerRemoved(this, amendmentController);

        if (!vetoed) {
            if (!amendmentControllers.contains(amendmentController)) {
                throw new RuntimeException("Amendment controller not found: " + amendmentController);
            }
            if (!amendmentControllers.remove(amendmentController)) {
                throw new RuntimeException("Could not remove amendment controller: " + amendmentController);
            }

            // physical remove
            amendmentController.getView().asWidget().removeFromParent();
            amendmentController.getExtendedView().asWidget().removeFromParent();

            // clear reference to this widget
            amendmentController.setAmendedOverlayWidget(null);

            // inform the listener
            if (listener != null) listener.afterAmendmentControllerRemoved(this, amendmentController);
        } else {
            LOG.info("OverlayWidget listener veto'ed the removal of the amendment controller.");
        }
    }

    @Override
    public void setUIListener(final OverlayWidgetUIListener UIListener) {
        this.UIListener = UIListener;
        if (UIListener != null) {
            // register a listener for the browser events
            sinkEvents(Event.ONKEYDOWN | Event.ONCLICK | Event.ONDBLCLICK | Event.ONMOUSEMOVE);
        }
    }

    @Override
    public void setListener(OverlayWidgetListener listener) {
        this.listener = listener;
    }

    @Override
    public void onBrowserEvent(final Event event) {
        // don't let events bubble up or you'd get parent widgets being invoked as well
        event.stopPropagation();
        if (UIListener != null) {
            switch (DOM.eventGetType(event)) {
                case Event.ONCLICK:
                    UIListener.onClick(this, event);
                    break;
                case Event.ONDBLCLICK:
                    UIListener.onDblClick(this, event);
                    break;
                case Event.ONMOUSEMOVE:
                    UIListener.onMouseOver(this, event);
                    break;
                case Event.ONMOUSEOUT:
                    UIListener.onMouseOut(this, event);
                    break;
                default:
                    //throw new UnsupportedOperationException("Unknown event.");
            }
        }
    }

    @Override
    public void setParentOverlayWidget(OverlayWidget parent) {
        this.parentOverlayWidget = parent;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String getId() {
        if (overlayStrategy == null) return id;
        if (id == null) {
            id = overlayStrategy.getID(getElement());
        }
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getInnerHTML() {
        final Node clonedNode = getElement().cloneNode(true);
        NodeUtil.walk(clonedNode, new NodeUtil.NodeVisitor() {
            @Override
            public void visit(final Node node) {
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element el = node.cast();
                    // determine if this is an amendment holder element or not
                    if (el.getTagName().equals(getAmendmentControllersHolderElement().getElement().getTagName())
                            && el.getClassName().equals(getAmendmentControllersHolderElement().getElement().getClassName())) {
                        el.removeFromParent();
                    }
                }
            }
        });
        return ((Element) clonedNode.cast()).getInnerHTML();
    }

    @Override
    public void setInnerHTML(String innerHTML) {
        // TODO make sure to restore the amendments if this is called
        if (isAmended()) {
            throw new RuntimeException("[TODO] We're not yet restoring amendments when the inner HTML is set.");
        }
        getElement().setInnerHTML(innerHTML);
    }

    @Override
    public List<OverlayWidget> getParentOverlayWidgets() {
        final List<OverlayWidget> parents = new ArrayList<OverlayWidget>();
        OverlayWidget parent = getParentOverlayWidget();
        while (parent != null) {
            parents.add(parent);
            parent = parent.getParentOverlayWidget();
        }
        Collections.reverse(parents);
        return parents;
    }

    @Override
    public List<OverlayWidget> getChildOverlayWidgets() {
        return childOverlayWidgets;
    }

    @Override
    public OverlayWidget getParentOverlayWidget() {
        return parentOverlayWidget;
    }

    @Override
    public OverlayWidget getPreviousSibling() {
        if (parentOverlayWidget == null) return null;
        final int index = parentOverlayWidget.getChildOverlayWidgets().indexOf(this);
        // short circuit if we're already the first widget
        if (index == 0) return null;
        return parentOverlayWidget.getChildOverlayWidgets().get(index - 1);
    }

    @Override
    public OverlayWidget getNextSibling() {
        if (parentOverlayWidget == null) return null;
        final int index = parentOverlayWidget.getChildOverlayWidgets().indexOf(this);
        // short circuit if we're already the last widget
        if (index == parentOverlayWidget.getChildOverlayWidgets().size() - 1) return null;
        return parentOverlayWidget.getChildOverlayWidgets().get(index + 1);
    }

    @Override
    public OverlayWidget next(final OverlayWidgetSelector overlayWidgetSelector) {
        OverlayWidget next = null;

        // if we have any children, select the first one
        if (!getChildOverlayWidgets().isEmpty()) {
            next = getChildOverlayWidgets().get(0);
        }

        // if we have no children, but rather a sibling, select that one
        if (next == null) {
            OverlayWidget sibling = getNextSibling();
            if (sibling != null) next = sibling;
        }
        // if we still don't have any target, then visit the parent node, and see if this one has a sibling relative
        // to this overlay widget - if not, bubble up until we have the root, or until we find a new sibling
        if (next == null) {
            // get the next parent sibling, if any
            OverlayWidget toCheckForSiblings = getParentOverlayWidget();
            while (toCheckForSiblings != null) {
                OverlayWidget nextSibling = toCheckForSiblings.getNextSibling();
                if (nextSibling != null) {
                    next = nextSibling;
                    break;
                }
                toCheckForSiblings = toCheckForSiblings.getParentOverlayWidget();
            }
        }
        // finally, if we found a next node, pass it on to the selector - if the selector rejects the node,
        // then we simply skip it, and use it to base the next search on
        while (next != null) {
            if (!overlayWidgetSelector.select(next)) {
                if (LOG.isLoggable(Level.FINE))
                    LOG.fine("Skipping " + next + " because the selector rejected it.");
                next = next.next(overlayWidgetSelector);
            } else {
                break;
            }
        }
        return next;
    }

    @Override
    public OverlayWidget previous(final OverlayWidgetSelector overlayWidgetSelector) {
        OverlayWidget previous = null;

        // if we have a previous sibling, then we need to find the very last child node
        final OverlayWidget previousSibling = getPreviousSibling();
        if (previousSibling != null) {
            OverlayWidget toCheckForChildren = previousSibling;
            OverlayWidget lastChild = toCheckForChildren;
            while (toCheckForChildren != null && !toCheckForChildren.getChildOverlayWidgets().isEmpty()) {
                // select the last child - see if it has any children
                lastChild = toCheckForChildren.getChildOverlayWidgets().get(toCheckForChildren.getChildOverlayWidgets().size() - 1);
                toCheckForChildren = lastChild;
            }
            previous = lastChild;
        } else {
            // no previous sibling - take the parent
            previous = getParentOverlayWidget();
        }

        // finally, if we found a previous node, pass it on to the selector - if the selector rejects the node,
        // then we simply skip it, and use it to base the previous search on
        while (previous != null) {
            if (!overlayWidgetSelector.select(previous)) {
                if (LOG.isLoggable(Level.FINE))
                    LOG.fine("Skipping " + previous + " because the selector rejected it.");
                previous = previous.previous(overlayWidgetSelector);
            } else {
                break;
            }
        }
        return previous;
    }

    @Override
    public OverlayWidget getPreviousNonIntroducedOverlayWidget(final boolean sameType) {
        OverlayWidget previous = getPreviousSibling();
        while (previous != null) {
            if (!previous.isIntroducedByAnAmendment()) {
                if (sameType) {
                    if (previous.getType().equalsIgnoreCase(getType()))
                        return previous;
                } else {
                    return previous;
                }
            }
            previous = previous.getPreviousSibling();
        }
        return null;
    }

    @Override
    public OverlayWidget getNextNonIntroducedOverlayWidget(final boolean sameType) {
        OverlayWidget next = getNextSibling();
        while (next != null) {
            if (!next.isIntroducedByAnAmendment()) {
                if (sameType) {
                    if (next.getType().equalsIgnoreCase(getType()))
                        return next;
                } else {
                    return next;
                }
            }
            next = next.getNextSibling();
        }
        return null;
    }

    @Override
    public OverlayWidget getRoot() {
        return getParentOverlayWidget() != null ? getParentOverlayWidget().getRoot() : this;
    }

    @Override
    public String toString() {
        return "[Element " + type + "]";
    }

    /**
     * Check if this widget is amendable or not in the editor. If it has not been specified
     * (when {@code amendable == null}), then we will go up in the tree until the root widget has been found,
     * in which case the default {@link #DEFAULT_ROOT_WIDGET_AMENDABLE} is returned.
     *
     * @return true if the amendment is amendable, false otherwise. Should never return <tt>null</tt>.
     */
    @Override
    public Boolean isAmendable() {
        // has been set explicitly
        if (amendable != null) return amendable;
        // walk the parents until we find one that has been set, or default to DEFAULT_ROOT_WIDGET_AMENDABLE for the root
        return parentOverlayWidget != null ? parentOverlayWidget.isAmendable() : DEFAULT_ROOT_WIDGET_AMENDABLE;
    }

    @Override
    public boolean isAmended() {
        return !amendmentControllers.isEmpty();
    }

    @Override
    public void setAmendable(Boolean amendable) {
        this.amendable = amendable;
    }

    @Override
    public Boolean isImmutable() {
        // has been set explicitly
        if (immutable != null) return immutable;
        // walk the parents until we find one that has been set, or default to false for the root
        return parentOverlayWidget != null ? parentOverlayWidget.isImmutable() : false;
    }

    @Override
    public void setImmutable(Boolean immutable) {
        this.immutable = immutable;
    }

    @Override
    public HTMLPanel getAmendmentControllersHolderElement() {
        if (amendmentControllersHolderElement == null) {
            if (getElement() != null && getElement().getParentElement() != null) {
                // TODO: this holder should not be necessary ... should be added directly to to the element.
                amendmentControllersHolderElement = new HTMLPanel("");
                amendmentControllersHolderElement.getElement().setClassName("amendments");
                getElement().insertBefore(amendmentControllersHolderElement.getElement(), null);
                adopt(amendmentControllersHolderElement);
                assert amendmentControllersHolderElement.isAttached() : "Amendment Holder element is not attached.";
            }
        }
        return amendmentControllersHolderElement;
    }

    @Override
    public StructureIndicator getStructureIndicator() {
        return new StructureIndicator.DefaultStructureIndicator(1, 1);
    }

    @Override
    public Format getFormat() {
        if (overlayStrategy == null) return format;
        if (format == null) {
            format = overlayStrategy.getFormat(getElement());
        }
        return format;
    }

    @Override
    public void setFormat(final Format format) {
        this.format = format;
    }

    @Override
    public NumberingType getNumberingType() {
        if (overlayStrategy == null) return numberingType;
        if (numberingType == null) {
            numberingType = overlayStrategy.getNumberingType(getElement(), getAssignedNumber());
        }
        return numberingType;
    }

    @Override
    public void setNumberingType(final NumberingType numberingType) {
        this.numberingType = numberingType;
    }

    @Override
    public Integer getAssignedNumber() {
        if (assignedNumber == null) {
            if (parentOverlayWidget != null) {
                assignedNumber = getTypeIndex(false) + 1;
            }
        }
        return assignedNumber;
    }

    @Override
    public void setAssignedNumber(final Integer assignedNumber) {
        this.assignedNumber = assignedNumber;
    }

    @Override
    public LinkedHashMap<String, String> getAttributes() {
        return new LinkedHashMap<String, String>();
    }

    @Override
    public void setOrigin(OverlayWidgetOrigin origin) {
        this.origin = origin;
    }

    @Override
    public OverlayWidgetOrigin getOrigin() {
        return origin;
    }

    @Override
    public String getFormattedIndex() {
        if (overlayStrategy == null) return formattedIndex;
        if (formattedIndex == null) {
            formattedIndex = overlayStrategy.getFormattedIndex(getElement());
        }
        return formattedIndex;
    }

    @Override
    public void moveUp() {
        OverlayWidget parent = getParentOverlayWidget();
        if (parent != null) {
            int colIndex = parent.getChildOverlayWidgets().indexOf(this);
            if (colIndex > 0) {
                parent.removeOverlayWidget(this);
                parent.addOverlayWidget(this, colIndex - 1, true);
                move(this, parent);
            }
        }
    }

    public void setFormattedIndex(String formattedIndex) {
        this.formattedIndex = formattedIndex;
    }

    @Override
    public void moveDown() {
        OverlayWidget parent = getParentOverlayWidget();
        if (parent != null) {
            int colIndex = parent.getChildOverlayWidgets().indexOf(this);
            if (colIndex < parent.getChildOverlayWidgets().size() - 1) {
                parent.removeOverlayWidget(this);
                parent.addOverlayWidget(this, colIndex + 1, true);
                move(this, parent);
            }
        }
    }

    @Override
    public String getUnformattedIndex() {
        if (overlayStrategy == null) return unformattedIndex;
        if (unformattedIndex == null) {
            unformattedIndex = overlayStrategy.getUnFormattedIndex(getElement());
        }
        return unformattedIndex;
    }

    public void setUnformattedIndex(String unformattedIndex) {
        this.unformattedIndex = unformattedIndex;
    }

    /**
     * Check if this overlay widget was created by an amendment. Will traverse upwards if the origin was not specified.
     *
     * @return <tt>true</tt> if this widget was introduced by an amendment.
     */
    @Override
    public boolean isIntroducedByAnAmendment() {
        return origin != null ? origin == OverlayWidgetOrigin.AMENDMENT : getParentOverlayWidget() != null && getParentOverlayWidget().isIntroducedByAnAmendment();
    }

    @Override
    public Element getOverlayElement() {
        return getElement();
    }

    @Override
    public AmendmentController[] getAmendmentControllers() {
        return amendmentControllers.toArray(new AmendmentController[amendmentControllers.size()]);
    }

    @Override
    public int getTypeIndex() {
        return getTypeIndex(false);
    }

    @Override
    public int getTypeIndex(final boolean includeAmendments) {
        if (getParentOverlayWidget() != null) {
            final Iterator<OverlayWidget> iterator = getParentOverlayWidget().getChildOverlayWidgets().iterator();
            int count = 0;
            while (iterator.hasNext()) {
                final OverlayWidget aw = iterator.next();
                if (aw != null) {

                    if (aw == this) {
                        break;
                    }
                    if (aw.getType().equalsIgnoreCase(getType())) {
                        if (includeAmendments) {
                            count++;
                        } else {
                            if (!aw.isIntroducedByAnAmendment()) {
                                count++;
                            }
                        }
                    }
                }
            }
            return count;
        }
        return 0; // this is the case for the root node
    }

    @Override
    public int getIndex() {
        if (getParentOverlayWidget() != null) {
            final Iterator<OverlayWidget> iterator = getParentOverlayWidget().getChildOverlayWidgets().iterator();
            int count = 0;
            while (iterator.hasNext()) {
                final OverlayWidget aw = iterator.next();
                if (aw != null) {

                    if (aw == this) {
                        break;
                    }
                    if (!aw.isIntroducedByAnAmendment()) {
                        count++;
                    }
                }
            }
            return count;
        }
        return -1;
    }

    // DSL Way
    public String html() {
        return getInnerHTML();
    }

    public OverlayWidgetImpl html(String s) {
        setInnerHTML(s);
        return this;
    }

    @Override
    public String getNamespaceURI() {
        throw new NullPointerException("Should be overridden by subclass.");
    }

    /**
     * This will use a
     * depth-first search using {@link org.nsesa.editor.gwt.core.client.ui.overlay.document.OverlayWidget#getChildOverlayWidgets()}.
     * </P>
     * Depending on the visitor's return value from {@link org.nsesa.editor.gwt.core.client.amendment.OverlayWidgetWalker.OverlayWidgetVisitor#visit(org.nsesa.editor.gwt.core.client.ui.overlay.document.OverlayWidget)},
     * we will continue going deeper into the tree's leaves.
     * <p/>
     * Note that when a search is stopped short by the visitor, this will <strong>NOT</strong> prevent the search from
     * visiting the sibling of this node that has not yet been visited.
     *
     * @param visitor the visitor
     */
    @Override
    public void walk(OverlayWidgetVisitor visitor) {
        walk(this, visitor);
    }

    protected void walk(OverlayWidget toVisit, OverlayWidgetVisitor visitor) {
        if (visitor.visit(toVisit)) {
            if (toVisit != null) {
                for (final OverlayWidget child : toVisit.getChildOverlayWidgets()) {
                    walk(child, visitor);
                }
            }
        }
    }

    /**
     * Returns the list of the allowed child types as they are coming from {@link StructureIndicator} structure
     *
     * @return the list of the allowed child types
     */
    protected List<OverlayWidget> getAllowedChildTypes() {
        if (allowedChildren == null) {
            allowedChildren = new ArrayList<OverlayWidget>();
            List<StructureIndicator> stack = new ArrayList<StructureIndicator>();
            stack.add(getStructureIndicator());
            while (!stack.isEmpty()) {
                StructureIndicator structureIndicator = stack.remove(0);
                if (structureIndicator instanceof StructureIndicator.Element) {
                    StructureIndicator.Element elemIndicator = (StructureIndicator.Element) structureIndicator;
                    OverlayWidget candidate = elemIndicator.asWidget();
                    allowedChildren.add(candidate);
                } else {
                    if (structureIndicator.getIndicators() != null) {
                        stack.addAll(structureIndicator.getIndicators());
                    }
                }
            }
        }
        return allowedChildren;
    }

    /**
     * Move the widget in the dom according with the new structure in parent collection
     * @param widget The widget to be moved
     * @param parent The widget parent
     */
    private void move(OverlayWidget widget, OverlayWidget parent) {
        // physical attach
        com.google.gwt.user.client.Element parentElement = parent.getOverlayElement().cast();
        com.google.gwt.user.client.Element childElement = widget.getOverlayElement().cast();
        DOM.removeChild(parentElement, childElement);
        OverlayWidget next = widget.getNextSibling();
        if (next != null) {
            com.google.gwt.user.client.Element beforeElement = next.getOverlayElement().cast();
            DOM.insertBefore(parentElement, childElement, beforeElement);
        } else {
            DOM.appendChild(parentElement, childElement);
        }
    }

}
