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
package org.nsesa.editor.app.xsd.model;

import com.sun.xml.xsom.*;

import java.util.Collection;

/**
 * Interface to generate objects of type {@link OverlayClass} from different xsd components.
 *
 * @author <a href="mailto:stelian.groza@gmail.com">Stelian Groza</a>
 * @author <a href="mailto:philip.luppens@gmail.com">Philip Luppens</a> (cleanup and documentation)
 *         Date: 18/10/12 15:36
 */
public interface OverlayClassGenerator {
    /**
     * Defines a root overlay class. When parsing an xsd schema one root overlay class is created.
     */
    public static class OverlayRootClass extends OverlayClass {
        public OverlayRootClass() {
            super("XS Root class", null, null);
        }
    }

    /**
     * Defines a schema overlay class. When parsing xsd schema-s, for each xsd schema one schema overlay class is created .
     */
    public static class OverlaySchemaClass extends OverlayClass {
        public OverlaySchemaClass(String nameSpace) {
            super("XS Schema class", nameSpace, null);
        }
    }

    /**
     * Return tree result of overlay classes after xsd parsing.
     *
     * @return the root overlay class
     */
    OverlayRootClass getResult();

    /**
     * Start generation of overlay classes.
     *
     * @param schemas the collection of schemas to use
     */
    void generate(Collection<XSSchema> schemas);

    /**
     * Generates overlay class from simple type component.
     *
     * @param simpleType The simple type processed
     */
    void generate(XSSimpleType simpleType);

    /**
     * Generates overlay class from complex type component.
     *
     * @param complexType The complex type processed
     */
    void generate(XSComplexType complexType);

    /**
     * Generates overlay class from attribute type component.
     *
     * @param attribute The attribute type that will be processed
     */
    void generate(XSAttributeDecl attribute);

    /**
     * Generates overlay class from group type component.
     *
     * @param modelGroup The xsd group processed
     */
    void generate(XSModelGroupDecl modelGroup);

    /**
     * Generates overlay class from attribute group type component.
     *
     * @param attrGroup The xsd attribute group processed
     */
    void generate(XSAttGroupDecl attrGroup);

    /**
     * Generates overlay class from XSElementDecl type component.
     *
     * @param element the underlying element
     */
    void generate(XSElementDecl element);

}
