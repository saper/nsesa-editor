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
package org.nsesa.editor.gwt.core.client.util.select;

import com.googlecode.gwt.test.GwtModule;
import com.googlecode.gwt.test.GwtTest;
import junit.framework.Assert;
import org.junit.Test;
import org.nsesa.editor.gwt.core.client.ui.overlay.document.AmendableWidget;
import org.nsesa.editor.gwt.core.client.ui.overlay.document.AmendableWidgetImpl;

import java.util.List;

/**
 * Date: 22/01/13 16:38
 *
 * @author <a href="philip.luppens@gmail.com">Philip Luppens</a>
 * @version $Id$
 */

@GwtModule("org.nsesa.editor.gwt.editor.Editor")
public class SelectorTest extends GwtTest {

    final AmendableWidget root = new AmendableWidgetImpl() {
        {
            setType("rootNode");
            setId("root");
        }
    };

    final AmendableWidget level1 = new AmendableWidgetImpl() {
        {
            setType("typeA");
            setId("id1");
        }
    };
    final AmendableWidget level2 = new AmendableWidgetImpl() {
        {
            setType("typeB");
            setId("id2");
        }
    };
    final AmendableWidget level3 = new AmendableWidgetImpl() {
        {
            setType("typeC");
            setId("id3");
        }
    };

    final AmendableWidget level31 = new AmendableWidgetImpl() {
        {
            setType("typeC");
            setId("id3-1");
        }
    };
    final AmendableWidget level32 = new AmendableWidgetImpl() {
        {
            setType("typeC");
            setId("id3-2");
        }
    };
    final AmendableWidget level33 = new AmendableWidgetImpl() {
        {
            setType("typeD");
            setId("id3-3");
        }
    };
    final AmendableWidget level311 = new AmendableWidgetImpl() {
        {
            setType("typeC");
            setId("id3-1-1");
        }
    };

    {
        root.addAmendableWidget(level1);
        root.addAmendableWidget(level2);
        root.addAmendableWidget(level3);

        level3.addAmendableWidget(level31);
        level3.addAmendableWidget(level32);
        level3.addAmendableWidget(level33);

        level31.addAmendableWidget(level311);
    }


    @Test
    public void testXpathRootExpression() throws Exception {
        final List<AmendableWidget> selection = new Selector().select("rootNode[0]", root);
        Assert.assertEquals("Ensure one node is found", 1, selection.size());
        Assert.assertEquals("Ensure the correct node can be found", root, selection.get(0));
    }

    @Test
    public void testXpathRootExpressionWithTrailingSlash() throws Exception {
        final List<AmendableWidget> selection = new Selector().select("rootNode[0]/", root);
        Assert.assertEquals("Ensure one node is found", 1, selection.size());
        Assert.assertEquals("Ensure the correct node can be found", root, selection.get(0));
    }


    @Test
    public void testXpathLevel1Expression() throws Exception {
        final List<AmendableWidget> selection = new Selector().select("rootNode[0]/typeC[0]", root);
        Assert.assertEquals("Ensure one node is found", 1, selection.size());
        Assert.assertEquals("Ensure the correct node is found.", level3, selection.get(0));
    }

    @Test
    public void testXpathLevel2Expression() throws Exception {
        final List<AmendableWidget> selection = new Selector().select("rootNode[0]/typeC[0]/typeC[0]", root);
        Assert.assertEquals("Ensure one node is found", 1, selection.size());
        Assert.assertEquals("Ensure the correct node is found.", level31, selection.get(0));
    }

    @Test
    public void testXpathLevel3Expression() throws Exception {
        final List<AmendableWidget> selection = new Selector().select("rootNode[0]/typeC[0]/typeC[0]/typeC[0]", root);
        Assert.assertEquals("Ensure one node is found", 1, selection.size());
        Assert.assertEquals("Ensure the correct node is found.", level311, selection.get(0));
    }

    @Test
    public void testXpathLevel2ExpressionWithSecondNode() throws Exception {
        final List<AmendableWidget> selection = new Selector().select("rootNode[0]/typeC[0]/typeC[1]", root);
        Assert.assertEquals("Ensure one node is found", 1, selection.size());
        Assert.assertEquals("Ensure the correct node is found.", level32, selection.get(0));
    }

    @Test
    public void testXpathLevel2ExpressionWithWildCard() throws Exception {
        final List<AmendableWidget> selection = new Selector().select("rootNode[0]/*[0]/typeC[1]", root);
        Assert.assertEquals("Ensure one node is found", 1, selection.size());
        Assert.assertEquals("Ensure the correct node is found.", level32, selection.get(0));
    }

    @Test
    public void testXpathMultipleReturnValuesWithWildCard() throws Exception {
        final List<AmendableWidget> selection = new Selector().select("rootNode[0]/*[0]/*", root);
        Assert.assertEquals("Ensure three nodes are found", 3, selection.size());
        Assert.assertEquals("Ensure the correct node is found.", level31, selection.get(0));
        Assert.assertEquals("Ensure the correct node is found.", level32, selection.get(1));
        Assert.assertEquals("Ensure the correct node is found.", level33, selection.get(2));
    }

    @Test
    public void testXpathMultipleReturnValuesWitType() throws Exception {
        final List<AmendableWidget> selection = new Selector().select("rootNode[0]/*[0]/typeC", root);
        Assert.assertEquals("Ensure two nodes are found", 2, selection.size());
        Assert.assertEquals("Ensure the correct node is found.", level31, selection.get(0));
        Assert.assertEquals("Ensure the correct node is found.", level32, selection.get(1));
    }

    @Test
    public void testXpathNothingExpression() throws Exception {
        final List<AmendableWidget> selection = new Selector().select("rootNode[0]/typeF[0]", root);
        Assert.assertEquals("Ensure no node is found", 0, selection.size());
    }
}
