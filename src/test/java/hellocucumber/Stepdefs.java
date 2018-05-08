package hellocucumber;

import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import cucumber.api.java.en.Then;
import static org.junit.Assert.*;

public class Stepdefs {
  @Given("^today is Sunday$")
  public void today_is_Sunday() {
    // Write code here that turns the phrase above into concrete actions
    throw new PendingException();
  }

  @When("^I ask whether it's Friday yet$")
  public void i_ask_whether_is_s_Friday_yet() {
    // Write code here that turns the phrase above into concrete actions
    throw new PendingException();
  }

  @Then("^I should be told \"([^\"]*)\"$")
  public void i_should_be_told(String arg1) {
    // Write code here that turns the phrase above into concrete actions
    throw new PendingException();
  }
}