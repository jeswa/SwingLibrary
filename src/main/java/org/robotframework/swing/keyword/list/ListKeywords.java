/*
 * Copyright 2008-2011 Nokia Siemens Networks Oyj
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.robotframework.swing.keyword.list;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Assert;
import org.robotframework.javalib.annotation.ArgumentNames;
import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.annotation.RobotKeywordOverload;
import org.robotframework.javalib.annotation.RobotKeywords;
import org.robotframework.swing.common.IdentifierSupport;
import org.robotframework.swing.factory.OperatorFactory;
import org.robotframework.swing.list.ListOperator;
import org.robotframework.swing.list.ListOperatorFactory;
import org.robotframework.swing.util.SwingInvoker;

@RobotKeywords
public class ListKeywords extends IdentifierSupport {
    private OperatorFactory<ListOperator> operatorFactory = new ListOperatorFactory();

    @RobotKeyword("Clears selection from list.\n\n" + "Example:\n"
            + "| Clear Selection From List | _myList_ |\n")
    @ArgumentNames({ "identifier" })
    public void clearSelectionFromList(String identifier) {
        createOperator(identifier).clearSelection();
    }

    @RobotKeyword("Selects an item from the list.\n\n"
            + "Examples:\n"
            + "| Select From List | _myList_ | _myItem_ | # selects 'myItem'                   |\n"
            + "| Select From List | _myList_ | _0_      | # selects the first item in the list |\n\n"
            + "| Select From List | _myList_ | _myItem_ | _2_ | # doubleclicks on item |\n"
            + "Any number of list item identifiers can be provided to select multiple items at once:\n"
            + "| Select From List | _myList_ | _myItem_ | _anotherItem_ | _yetAnother_ |\n")
    @ArgumentNames({ "identifier", "listItemIdentifier",
            "*additionalItemIdentifiers" })
    public void selectFromList(final String identifier,
            final String listItemIdentifier,
            final String[] additionalItemIdentifiers) {
        final ListOperator operator = createOperator(identifier);
        // http://code.google.com/p/robotframework-swinglibrary/issues/detail?id=192
        SwingInvoker.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                operator.selectItems(intoList(listItemIdentifier,
                        additionalItemIdentifiers));

            }
        });

    }

    private List<String> intoList(final String listItemIdentifier,
            String[] additionalItemIdentifiers) {
        final List<String> itemIdentifiers = new ArrayList<String>() {
            {
                add(listItemIdentifier);
            }
        };
        CollectionUtils.addAll(itemIdentifiers, additionalItemIdentifiers);
        return itemIdentifiers;
    }

    @RobotKeyword("Clicks on a list item.\n\n"
            + "Examples:\n"
            + "| Click On List Item | _myList_ | _myItem_ |\n"
            + "| Click On List Item | _myList_ | _0_      | \n\n"
            + "An optional _click count_ parameter can be provided for example if a double click is required.\n"
            + "Default click count is one:\n"
            + "| Click On List Item | _myList_ | _myItem_ | _2_ | # doubleclicks on item |\n")
    @ArgumentNames({ "identifier", "listItemIdentifier", "clickCount=1" })
    public void clickOnListItem(String identifier, String listItemIdentifier, int clickCount) {
        createOperator(identifier).clickOnItem(listItemIdentifier, clickCount);
    }

    @RobotKeywordOverload
    public void clickOnListItem(String identifier, String listItemIdentifier) {
        clickOnListItem(identifier, listItemIdentifier, 1);
    }

    @RobotKeyword("Returns the item that's currently selected in the list.\n\n"
            + "Example:\n"
            + "| ${listItem}=    | Get Selected Value From List | _myList_      |\n"
            + "| Should Be Equal | _Expected Item_              | _${listItem}_ |\n")
    @ArgumentNames({ "identifier" })
    public Object getSelectedValueFromList(String identifier) {
        return createOperator(identifier).getSelectedValue();
    }

    @RobotKeyword("Returns the number of items contained in list.\n\n"
            + "Example:\n"
            + "| ${listItemCount}=    | Get List Item Count | _myList_      |\n"
            + "| Should Be Equal As Integers | _2_ | _${listItemCount}_ |\n")
    @ArgumentNames({ "identifier" })
    public int getListItemCount(String identifier) {
        return createOperator(identifier).getSize();
    }

    @RobotKeyword("Selects all list items.\n\n" + "Example:\n"
            + "| Select All List Items | _My List_ |\n")
    @ArgumentNames({ "identifier" })
    public void selectAllListItems(String identifier) {
        createOperator(identifier).selectAll();
    }

    @RobotKeyword("Returns all values from a list.\n\n" + "Example:\n"
            + "| ${values}= | Get List Values | _myList_ |\n"
            + "| Should Be Equal | ${values} | one, two, three |\n")
    @ArgumentNames({ "identifier" })
    public List<String> getListValues(String identifier) {
        return createOperator(identifier).getListValues();
    }

    @RobotKeyword("Fails if value not in list.\n\n" + "Example:\n"
            + "| List Should Contain | _myList_ | item |\n")
    @ArgumentNames({ "identifier", "value" })
    public void listShouldContain(String identifier, String value) {
        Assert.assertTrue("List " + identifier + " does not contain " + value,
                getListValues(identifier).contains(value));
    }

    @RobotKeyword("Fails if value is in list.\n\n" + "Example:\n"
            + "| List Should Not Contain | _myList_ | item |\n")
    @ArgumentNames({ "identifier", "value" })
    public void listShouldNotContain(String identifier, String value) {
        Assert.assertTrue("List " + identifier + " contains " + value,
                !getListValues(identifier).contains(value));
    }

    private ListOperator createOperator(String identifier) {
        return operatorFactory.createOperator(identifier);
    }
}
