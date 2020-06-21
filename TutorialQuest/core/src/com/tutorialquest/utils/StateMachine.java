package com.tutorialquest.utils;

import java.util.*;

public class StateMachine {

	protected Map<Integer, State> states = new HashMap<>();
	public State currentState = null;
	private int firstState = -1;

	public static class State {
		protected StateMachine stateMachine;
		protected float timeoutTime = 0f;
		protected float probability = -1f;
		protected int timeoutState = -1;
		protected float timeoutLimit = 0;
		public int id = -1;

		public State(
			StateMachine stateMachine,
			int id,
			float probability,
			float timeLimit,
			int timeoutState) {
			this.id = id;
			this.stateMachine = stateMachine;
			this.probability = probability;
			this.timeoutState = timeoutState;
			this.timeoutLimit = timeLimit;
		}

		public String getName() {
			return "?";
		}

		public boolean update(float deltaTime) {

			if (timeoutState >= 0)
			{
				if (timeoutTime < timeoutLimit) timeoutTime += deltaTime;
				else
				{
					stateMachine.setCurrentState(timeoutState);
					timeoutTime = 0;
					return false;
				}
			}

			return true;
		}

		public void enter() {

		}

		public void exit() {

		}

	}

	public static class DecisionState extends State {
		public DecisionState(StateMachine stateMachine, int id, float probability, float timeLimit, int timeoutState) {
			super(stateMachine, id, probability, timeLimit, timeoutState);
		}

		public void enter() {
			List<State> choices = new LinkedList<>();
			for (Map.Entry<Integer, State> candidate : stateMachine.states.entrySet()) {
				if (candidate.getValue().probability < 0) continue;
				float chance = Utils.random.nextFloat();
				if (chance <= candidate.getValue().probability) choices.add(candidate.getValue());
			}

			if (choices.isEmpty()) {
				stateMachine.setCurrentState(id);
				return;
			}

			stateMachine.setCurrentState(choices.get(Utils.random.nextInt(choices.size())).id);
		}
	}

	public void setCurrentState(int stateID) {
		if (states.containsKey(stateID)) {
			if (currentState != null)
				currentState.exit();
			currentState = states.get(stateID);
			currentState.enter();
		}
	}

	public String getStateName() {
		if(currentState != null) return currentState.getName();
		return "?";
	}

	public void update(float deltaTime) {
		if (firstState >= 0) {
			setCurrentState(firstState);
			firstState = -1;
			return;
		}

		if (currentState != null) currentState.update(deltaTime);
	}

	public void addState(State state, boolean first) {
		if (state.id < 0) return;
		if (!states.containsKey(state.id)) {
			states.put(state.id, state);
			if (first) this.firstState = state.id;
		}
	}
}
