/**
 * Copyright 2013 European Parliament
 *
 * Licensed under the EUPL, Version 1.1 or – as soon they will be approved by the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 *
 * http://joinup.ec.europa.eu/software/page/eupl
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the Licence is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and limitations under the Licence.
 */
package org.nsesa.editor.app.xsd.model;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Bean to keep basic information about class.
 * Date: 05/08/12 13:36
 *
 * @author <a href="philip.luppens@gmail.com">Philip Luppens</a>
 * @version $Id$
 */
public class OverlayClass extends OverlayNode {
    public static final Logger LOG = LoggerFactory.getLogger(OverlayClass.class);
    public static final Comparator<OverlayClass> DEFAULT_COMPARATOR = new Comparator<OverlayClass>() {
        @Override
        public int compare(OverlayClass o1, OverlayClass o2) {
//            int result = o1.getChildren().size() - o2.getChildren().size();
//            if (result != 0) {
//                return result;
//            }
            int result = o1.name.compareTo(o2.name);
            return result;
        }
    };


    private String packageName;
    private Class<?>[] interfaces;
    private SimpleTypeRestriction restriction;

    private OverlayClass parent;

    private List<OverlayClass> children;
    private List<OverlayProperty> properties;

    public OverlayClass() {
        super();
        this.properties = new ArrayList<OverlayProperty>();
        this.children = new ArrayList<OverlayClass>();
    }

    public OverlayClass(String name, String nameSpace, OverlayType overlayType) {
        super(name, nameSpace, overlayType);
        this.properties = new ArrayList<OverlayProperty>();
        this.children = new ArrayList<OverlayClass>();
    }

    public List<OverlayClass> getChildren() {
        return children;
    }

    public List<OverlayClass> getOrderedChildren() {
        Collections.sort(children, OverlayClass.DEFAULT_COMPARATOR);
        return children;
    }

    public void setChildren(List<OverlayClass> children) {
        this.children = children;
    }

    public SimpleTypeRestriction getRestriction() {
        return restriction;
    }

    public List<OverlayProperty> getProperties() {
        return properties;
    }

    public void setProperties(List<OverlayProperty> properties) {
        this.properties = properties;
    }

    public OverlayClass getParent() {
        return parent;
    }

    public void setParent(OverlayClass parent) {
        this.parent = parent;
    }

    public boolean isDescendentOf(String parentName) {
        boolean result = false;
        OverlayClass nodeParent = this;
        while (nodeParent != null) {
            if (parentName.equalsIgnoreCase(nodeParent.getName())) {
                result = true;
                break;
            }
            nodeParent = nodeParent.getParent();
        }
        return result;
    }

    public String[] getImports(PackageNameGenerator packageNameGenerator) {
        Set<String> imports = new LinkedHashSet<String>();
        if (parent != null && (parent.isComplex() || parent.isElement() || parent.isSimple())) {
            String packageName = packageNameGenerator.getPackageName(parent);
            imports.add(packageName + "." + StringUtils.capitalize(parent.getClassName()));
        }
        for (OverlayProperty property : properties) {
            String packageName = packageNameGenerator.getPackageName(property);
            if ("java.lang".equals(packageName)) {
                continue;
            }
            imports.add(packageName + "." + StringUtils.capitalize(property.getClassName()));
        }
        // add imports for parent properties
        if (getParent() != null) {
            for (OverlayProperty property : getParent().getAllAttributesProperties()) {
                String packageName = packageNameGenerator.getPackageName(property);
                if ("java.lang".equals(packageName)) {
                    continue;
                }
                imports.add(packageName + "." + StringUtils.capitalize(property.getClassName()));
            }
        }


        return imports.toArray(new String[imports.size()]);
    }

    public Class<?>[] getInterfaces() {
        return interfaces;
    }

    public void setInterfaces(Class<?>[] interfaces) {
        this.interfaces = interfaces;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public void process(OverlayClassProcessor processor) {
        processor.process(this);
    }

    public void setRestriction(SimpleTypeRestriction typeRestriction) {
        this.restriction = typeRestriction;
    }

    public boolean isEnumeration() {
        return isSimple() && restriction != null &&
                restriction.getEnumeration() != null &&
                restriction.getEnumeration().length > 0;
    }

    /**
     * Returns true when it contains wild card properties
     *
     * @return
     */
    public boolean hasWildCardProperties() {
        boolean result = false;
        for (OverlayProperty property : properties) {
            if (property.isWildCard()) {
                result = true;
                break;
            }
        }
        return result;
    }

    public Set<String> getAllowedSubTypes() {
        Set<String> set = new HashSet<String>();
        OverlayClass aClass = this;
        while (aClass != null && (aClass.isComplex() || aClass.isSimple() || aClass.isElement())) {
            for (OverlayProperty property : aClass.getProperties()) {
                if (property.isAttribute()) {
                    continue;
                }
                if (property.isWildCard()) {
                    set.add("*");
                } else {
                    set.add(property.getClassName());
                }
            }
            aClass = aClass.getParent();
        }
        return set;
    }

    public List<OverlayProperty> getAllAttributesProperties() {
        OverlayProperty.Filter filter = new OverlayProperty.Filter() {
            @Override
            public boolean apply(OverlayProperty property) {
                return property.isAttribute();
            }
        };
        return getAllFilteredProperties(filter);
    }

    public List<OverlayProperty> getAllNonAttributesProperties() {
        OverlayProperty.Filter filter = new OverlayProperty.Filter() {
            @Override
            public boolean apply(OverlayProperty property) {
                return !property.isAttribute();
            }
        };
        return getAllFilteredProperties(filter);
    }

    private List<OverlayProperty> getAllFilteredProperties(OverlayProperty.Filter filter) {
        List<OverlayProperty> result = new ArrayList<OverlayProperty>();
        OverlayClass aClass = this;
        while (aClass != null && (aClass.isComplex() || aClass.isSimple() || aClass.isElement())) {
            for (OverlayProperty property : aClass.getProperties()) {
                if (filter.apply(property)) {
                    result.add(property);
                }
            }
            aClass = aClass.getParent();
        }
        return result;
    }

    @Override
    public String toString() {
        return super.toString() + '\'' +
                "OverlayClass{" +
                "packageName='" + packageName + '\'' +
                ", interfaces=" + (interfaces == null ? null : Arrays.asList(interfaces)) +
                ", restriction=" + restriction +
                ", parent=" + parent +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OverlayClass)) return false;
        OverlayClass that = (OverlayClass) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (nameSpace != null ? !nameSpace.equals(that.nameSpace) : that.nameSpace != null) return false;
        if (overlayType != that.overlayType) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        return result;
    }
}
