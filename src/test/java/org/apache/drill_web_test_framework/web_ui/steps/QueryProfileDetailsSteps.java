/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.drill_web_test_framework.web_ui.steps;

import org.apache.drill_web_test_framework.web_ui.WebBrowser;
import org.apache.drill_web_test_framework.web_ui.pages.BasePage;
import org.apache.drill_web_test_framework.web_ui.pages.QueryProfileDetailsPage;
import org.apache.drill_web_test_framework.web_ui.pages.QueryProfileDetailsPage.QueryType;
import org.json.JSONObject;
import org.testng.asserts.SoftAssert;

import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

public class QueryProfileDetailsSteps extends BaseSteps {

  public QueryProfileDetailsSteps openProfile(String queryProfile) {
    openUrl("/profiles/" + queryProfile);
    return this;
  }

  public void validatePage() {
    validateQueryProfileOverview();
    validateQueryProfileDuration();
    validateFragmentOverview();
    validateFragments();
    validateOperatorOverview();
    validateOperators();
  }

  private void validateQueryProfileOverview() {
    SoftAssert softAssert = new SoftAssert();
    softAssert.assertEquals(getPage().getProfileLabelText(), getProfileFromURL(getURL()));
    List<List<String>> profileOverview = getPage().getProfileOverview();
    softAssert.assertEquals(profileOverview.get(0),
        Arrays.asList("State", "Foreman", "Total Fragments", "Total Cost", "Queue"));
    List<String> profileOverviewContent = profileOverview.get(1);
    softAssert.assertEquals(profileOverviewContent.get(0), "Succeeded");
    softAssert.assertFalse(profileOverviewContent.get(1).equals(""), "Empty Forman name!");
    softAssert.assertTrue(profileOverviewContent.get(2).matches("\\d+"), "Incorrect total fragments number!");
    softAssert.assertTrue(profileOverviewContent.get(3).matches("^[0-9]{1,3}(,[0-9]{3})*$"), "Incorrect total cost number!");
    softAssert.assertEquals(profileOverviewContent.get(4), "Unknown");
    softAssert.assertAll();
  }

  private void validateQueryProfileDuration() {
    SoftAssert softAssert = new SoftAssert();
    List<List<String>> durationTable = getPage().getProfileDuration();
    softAssert.assertEquals(durationTable.get(0),
        Arrays.asList("Planning", "Queued", "Execution", "Total"));
    List<String> duration = durationTable.get(1);
    duration.forEach(value ->
        softAssert.assertTrue(value.matches("\\d+\\.\\d+ \\D+"), "Duration is incorrect!"));
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
    getPage().refresh();
    softAssert.assertEquals(duration, getPage().getProfileDuration().get(1));
    softAssert.assertAll();
  }

  private void validateFragmentOverview() {
    SoftAssert softAssert = new SoftAssert();
    softAssert.assertEquals(getPage().getFragmentOverview().get(0),
        Arrays.asList("Major Fragment", "Minor Fragments Reporting", "First Start", "Last Start", "First End", "Last End", "Min Runtime", "Avg Runtime", "Max Runtime", "% Busy", "Max Peak Memory"));
    getPage().getFragmentOverview()
        .stream()
        .skip(1)
        .forEach(majorFragment -> {
      softAssert.assertTrue(majorFragment.get(0).matches("\\d+-xx-xx"), "Wrong major fragment name!");
      softAssert.assertTrue(majorFragment.get(1).matches("\\d+ / \\d+"), "Wrong minor fragments value!");
      IntStream.range(2, 9).forEach(i ->
          softAssert.assertTrue(majorFragment.get(i).matches("\\d+\\.\\d+s"), "Wrong durations!"));
      softAssert.assertTrue(majorFragment.get(9).matches("\\d+\\.\\d+%"), "Wrong busy value!");
      softAssert.assertTrue(majorFragment.get(10).matches("\\d+\\D+"), "Wrong memory value!");
    });
    softAssert.assertAll();
  }

  private void validateFragments() {
    SoftAssert softAssert = new SoftAssert();
    setImplicitWait(0);
    getPage().getFragments()
        .forEach(fragment -> {
          softAssert.assertEquals(fragment.get(0),
              Arrays.asList("Minor Fragment", "Hostname", "Start", "End", "Runtime", "Max Records", "Max Batches", "Last Update", "Last Progress", "Peak Memory", "State"));
          fragment.stream()
              .skip(1)
              .forEach(minorFragment -> {
                softAssert.assertTrue(minorFragment.get(0).matches("\\d+-\\d+-xx"), "Wrong minor fragment name!");
                softAssert.assertFalse(minorFragment.get(1).equals(""), "Empty hostname!");
                IntStream.range(2, 5).forEach(i ->
                    softAssert.assertTrue(minorFragment.get(i).matches("\\d+\\.\\d+s"), "Wrong durations!"));
                IntStream.range(5, 7).forEach(i ->
                    softAssert.assertTrue(minorFragment.get(i).matches("^[0-9]{1,3}(,[0-9]{3})*$"), "Wrong number of records / batches!"));
                IntStream.range(7, 9).forEach(i ->
                    softAssert.assertTrue(minorFragment.get(i).matches("\\d+:\\d+:\\d+"), "Wrong update / progress time!"));
                softAssert.assertTrue(minorFragment.get(9).matches("\\d+\\D+"), "Wrong memory value!");
                softAssert.assertEquals(minorFragment.get(10), "FINISHED");
              });
        });
    resetImplicitWait();
    softAssert.assertAll();
  }

  private void validateOperatorOverview() {
    SoftAssert softAssert = new SoftAssert();
    List<List<String>> operatorOverview = getPage().getOperatorOverview();
    softAssert.assertEquals(operatorOverview.get(0),
        Arrays.asList("Operator ID", "Type", "Avg Setup Time", "Max Setup Time", "Avg Process Time", "Max Process Time", "Min Wait Time", "Avg Wait Time", "Max Wait Time", "% Fragment Time", "% Query Time", "Rows", "Avg Peak Memory", "Max Peak Memory"));
    operatorOverview.stream()
        .skip(1)
        .forEach(operator -> {
          softAssert.assertTrue(operator.get(0).matches("\\d+-xx-\\d+"), "Wrong operator ID!");
          softAssert.assertFalse(operator.get(1).equals(""), "Operator name is empty!");
          IntStream.range(2, 9).forEach(i ->
              softAssert.assertTrue(operator.get(i).matches("\\d+\\.\\d+s"), "Wrong time value!"));
          IntStream.range(9, 11).forEach(i ->
              softAssert.assertTrue(operator.get(i).matches("\\d+\\.\\d+%"), "Wrong time percentage!"));
          softAssert.assertTrue(operator.get(11).matches("^[0-9]{1,3}(,[0-9]{3})*$"), "Wrong number of rows!");
          IntStream.range(12, 14).forEach(i ->
              softAssert.assertTrue(operator.get(i).matches("(\\d+\\D+|-)"), "Wrong memory value!"));
        });
    softAssert.assertAll();
  }

  private void validateOperators() {
    SoftAssert softAssert = new SoftAssert();
    setImplicitWait(0);
    getPage().getOperators().forEach(operator -> {
      softAssert.assertEquals(operator.get(0),
          Arrays.asList("Minor Fragment", "Hostname", "Setup Time", "Process Time", "Wait Time", "Max Batches", "Max Records", "Peak Memory"));
      operator.stream()
          .skip(1)
          .forEach(minorFragment -> {
            softAssert.assertTrue(minorFragment.get(0).matches("\\d+-\\d+-\\d+"), "Wrong minor fragment name!");
            softAssert.assertFalse(minorFragment.get(1).equals(""), "Hostname is empty!");
            IntStream.range(2, 5).forEach(i ->
                softAssert.assertTrue(minorFragment.get(i).matches("\\d+\\.\\d+\\D+"), "Wrong time value!"));
            IntStream.range(5, 7).forEach(i ->
                softAssert.assertTrue(minorFragment.get(i).matches("^[0-9]{1,3}(,[0-9]{3})*$"), "Wrong number of records / batches!"));
            softAssert.assertTrue(minorFragment.get(7).matches("(\\d+\\D+|-)"), "Wrong memory value!");
          });
    });
    resetImplicitWait();
    softAssert.assertAll();
  }

  private String getProfileFromURL(String url) {
    return url.substring(url.lastIndexOf('/') + 1);
  }

  public QueryProfileDetailsSteps navigateTab(String tabText) {
    getPage().navigateTab(tabText);
    return this;
  }

  public String activeTab() {
    return getPage().activeTab();
  }

  public String activePanel() {
    return getPage().activePanelId();
  }

  public String getQueryText() {
    return getPage()
        .waitForQueryText()
        .getQueryText();
  }

  public QueryProfileDetailsSteps setQueryText(String text) {
    getPage().setQueryText(text);
    return this;
  }

  public QueryResultsSteps rerunSQL() {
    getPage().setQueryType(QueryType.SQL)
        .rerunQuery();
    return getSteps(QueryResultsSteps.class);
  }

  public QueryResultsSteps rerunPhysical() {
    getPage().setQueryType(QueryType.PHYSICAL)
        .rerunQuery();
    return getSteps(QueryResultsSteps.class);
  }

  public QueryResultsSteps rerunLogical() {
    getPage().setQueryType(QueryType.LOGICAL)
        .rerunQuery();
    return getSteps(QueryResultsSteps.class);
  }

  public boolean validatePlan(String pattern) {
    String plan = getPage().getPlan();
    Matcher matcher = Pattern.compile(pattern, Pattern.DOTALL | Pattern.MULTILINE)
        .matcher(plan);
    return matcher.find();
  }

  public String validateJson() {
    String profile = getPage().expandProfile()
        .waitForJSONText()
        .getFullJSONProfile();
    try {
      new JSONObject(profile);
    } catch (Exception e) {
      return e.getMessage();
    }
    return "Ok";
  }

  public VisualPlan getVisualPlan() {
    return getVisualPlan(getPage().getPlanNodes());
  }

  public VisualPlan getPrintingPlan() {
    return getVisualPlan(getPage().getPrintedPlanNodes());
  }

  public List<String> getOperatorsList() {
    return getPage().getOperatorsList();
  }

  public QueryProfileDetailsSteps clickAllCollapsibleItems() {
    getPage().clickAllCollapsibleItems();
    return this;
  }

  public QueryProfileDetailsSteps clickAllCollapsibleItemsReversed() {
    getPage().collapseAllItemsReversed();
    return this;
  }

  public QueryProfileDetailsSteps expandAllCollapsedItems() {
    getPage().expandAllCollapsedItems();
    return this;
  }

  public boolean isAllItemsCollapsed() {
    return getPage().isAllItemsCollapsed();
  }

  public boolean isAllItemsExpanded() {
    return getPage().isAllItemsExpanded();
  }

  private VisualPlan getVisualPlan(List<Map<String, String>> planNodes) {
    VisualPlan plan = null;
    VisualPlan.PlanNode head = null;
    Map<VisualPlan.PlanNode, Integer> coordinates = new HashMap<>();
    for (Map<String, String> nodeMap : planNodes) {
      String label = nodeMap.get("label");
      Integer coordinate = Integer.parseInt(nodeMap.get("coordinate"));
      if (plan == null) {
        plan = new VisualPlan();
        head = plan.append(label);
      } else {
        if (coordinate > coordinates.get(head)) {
          head = head.append(label);
        } else {
          while (!coordinate.equals(coordinates.get(head))) {
            head = head.parent;
          }
          head = head.parent.append(label);
        }
      }
      coordinates.put(head, coordinate);
    }
    return plan;
  }

  private QueryProfileDetailsPage getPage() {
    return BasePage.getPage(QueryProfileDetailsPage.class);
  }

  public QueryProfileDetailsSteps printPlan() {
    Thread t = getPage().printPlan();
    while (t.isAlive()) {
      try {
        Thread.sleep(100);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
      pressKey(KeyEvent.VK_ESCAPE);
    }
    WebBrowser.switchToOpenedWindow();
    return this;
  }

  public QueryProfileDetailsSteps closePrintWindow() {
    WebBrowser.closeWindow();
    return this;
  }

  public class VisualPlan {

    private PlanNode root;

    public PlanNode append(String label) {
      if (root == null) {
        root = new PlanNode(label);
      }
      return root;
    }

    public PlanNode getNode(String label) {
      return root.getNode(label);
    }

    @Override
    public boolean equals(Object obj) {
      if (obj == this) {
        return true;
      } else if (!(obj instanceof VisualPlan)) {
        return false;
      }
      return root.equals(((VisualPlan) obj).root);
    }

    @Override
    public int hashCode() {
      return root.hashCode();
    }

    @Override
    public String toString() {
      return root.toString();
    }

    public class PlanNode {

      private final String label;
      private PlanNode parent;
      private Set<PlanNode> children = new HashSet<>();

      private PlanNode(String label) {
        this.label = label;
      }

      public PlanNode append(String label) {
        PlanNode child = new PlanNode(label);
        child.parent = this;
        children.add(child);
        return child;
      }

      public PlanNode getNode(String label) {
        if (this.label.equals(label)) {
          return this;
        } else {
          for (PlanNode child : children) {
            PlanNode desiredNode = child.getNode(label);
            if (desiredNode != null) {
              return desiredNode;
            }
          }
        }
        return null;
      }

      @Override
      public boolean equals(Object obj) {
        if (obj == this) {
          return true;
        } else if (!(obj instanceof PlanNode)) {
          return false;
        }
        return label.equals(((PlanNode) obj).label) &&
            children.equals(((PlanNode) obj).children);
      }

      @Override
      public int hashCode() {
        return label.hashCode();
      }

      @Override
      public String toString() {
        StringBuilder result = new StringBuilder(label);
        for (PlanNode child : children) {
          result.append(" | ")
              .append(child.toString());
        }
        return result.toString();
      }
    }
  }
}
