package com.prac.stepDefinition;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class StepDefinition {

	@Given("I want to write a step with precondition")
	public void i_want_to_write_a_step_with_precondition() {
		System.out.println("::::i_want_to_write_a_step_with_precondition");
	}

	@Given("some other precondition")
	public void some_other_precondition() {
		System.out.println("::::some_other_precondition");
	}

	@When("I complete action")
	public void i_complete_action() {
		System.out.println("::::i_complete_action");
	}

	@When("some other action")
	public void some_other_action() {
		System.out.println("::::some_other_action");
	}

	@When("yet another action")
	public void yet_another_action() {
		System.out.println("::::yet_another_action");
	}

	@Then("I validate the outcomes")
	public void i_validate_the_outcomes() {
		System.out.println("::::i_validate_the_outcomes");
	}

	@Then("check more outcomes")
	public void check_more_outcomes() {
		System.out.println("::::check_more_outcomes");
	}

	@Given("I want to write a step with {char}")
	public void i_want_to_write_a_step_with() {
		System.out.println("::::i_want_to_write_a_step_with_name1");
	}

	@When("I check for the {int} in step")
	public void i_check_for_the_in_step(Integer int1) {
		System.out.println("::::i_check_for_the_in_step");
	}

	@Then("I verify the {$%s} in step")
	public void i_verify_the_in_step() {
		System.out.println("::::i_verify_the_success_in_step");
	}

}
