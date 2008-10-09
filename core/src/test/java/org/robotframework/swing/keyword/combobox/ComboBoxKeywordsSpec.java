package org.robotframework.swing.keyword.combobox;

import jdave.junit4.JDaveRunner;

import org.jmock.Expectations;
import org.junit.runner.RunWith;
import org.robotframework.swing.context.IContextVerifier;
import org.robotframework.swing.contract.FieldIsNotNullContract;
import org.robotframework.swing.contract.RobotKeywordContract;
import org.robotframework.swing.contract.RobotKeywordsContract;
import org.robotframework.swing.factory.OperatorFactory;
import org.robotframework.swing.keyword.MockSupportSpecification;
import org.robotframework.swing.operator.combobox.MyComboBoxOperator;


@RunWith(JDaveRunner.class)
public class ComboBoxKeywordsSpec extends MockSupportSpecification<ComboBoxKeywords> {
    private String comboBoxIdentifier = "someComboBox";

    public class Any {
        public ComboBoxKeywords create() {
            return new ComboBoxKeywords();
        }

        public void isRobotKeywordsAnnotated() {
            specify(context, satisfies(new RobotKeywordsContract()));
        }

        public void hasOperatorFactory() throws Throwable {
            specify(context, satisfies(new FieldIsNotNullContract("operatorFactory")));
        }

        public void hasContextVerifier() throws Throwable {
            specify(context, satisfies(new FieldIsNotNullContract("contextVerifier")));
        }

        public void hasSelectFromComboBoxKeyword() {
            specify(context, satisfies(new RobotKeywordContract("selectFromComboBox")));
        }

        public void hasSelectFromDropDownMenuKeyword() {
            specify(context, satisfies(new RobotKeywordContract("selectFromDropdownMenu")));
        }

        public void hasGetSelectedItemFromComboboxKeyword() {
            specify(context, satisfies(new RobotKeywordContract("getSelectedItemFromComboBox")));
        }

        public void hasGetSelectedItemFromDropDownMenuKeyword() {
            specify(context, satisfies(new RobotKeywordContract("getSelectedItemFromDropdownMenu")));
        }
    }

    public class WhenOperating {
        private OperatorFactory<MyComboBoxOperator> operatorFactory;
        private MyComboBoxOperator operator;

        public ComboBoxKeywords create() {
            operator = mock(MyComboBoxOperator.class);
            ComboBoxKeywords comboBoxKeywords = new ComboBoxKeywords();
            operatorFactory = injectMockTo(comboBoxKeywords, OperatorFactory.class);
            final IContextVerifier contextVerifier = injectMockTo(comboBoxKeywords, "contextVerifier", IContextVerifier.class);

            checking(new Expectations() {{
                one(operatorFactory).createOperator(with(equal(comboBoxIdentifier)));
                will(returnValue(operator));

                one(contextVerifier).verifyContext();
            }});

            return comboBoxKeywords;
        }

        public void selectsFromComboBox() {
            final String comboItemIdentifier = "someComboItem";

            checking(new Expectations() {{
                one(operator).pushComboButton();
                one(operator).selectItem(comboItemIdentifier);
            }});

            context.selectFromComboBox(comboBoxIdentifier, comboItemIdentifier);
        }

        public void getsSelectedItem() {
            final Object selectedItem = new Object();

            checking(new Expectations() {{
                one(operator).getSelectedItem();
                will(returnValue(selectedItem));
            }});

            specify(context.getSelectedItemFromComboBox(comboBoxIdentifier), must.equal(selectedItem));
        }
    }

    public class HandlingAliases {
        private boolean isAnAlias = false;
        public void checkBoxShouldNotBeCheckedIsAnAliasForCheckBoxShouldBeUnchecked() {
            final String comboItemIdentifier = "itemIdentifier";

            ComboBoxKeywords comboBoxKeywords = new ComboBoxKeywords() {
                @Override
                public void selectFromComboBox(String boxIdentifier, String itemIdentifier) {
                    if (boxIdentifier.equals(comboBoxIdentifier) && itemIdentifier.equals(comboItemIdentifier)) {
                        isAnAlias = true;
                    }
                }
            };

            comboBoxKeywords.selectFromDropdownMenu(comboBoxIdentifier, comboItemIdentifier);
            specify(isAnAlias);
        }
    }
}
