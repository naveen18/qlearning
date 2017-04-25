package qlearning;

import org.jfree.ui.RefineryUtilities;

class QLearning {
	State currState = new State(0, 0);
	// action to int mapping up-0 down-1 left-2 right-3
	static int gridLength = 15;
	static int gridBreadth = 15;
	static final int[][] action = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };
	float[][][] qtable = new float[gridLength][gridBreadth][4];
	int[][][] reward = new int[gridLength][gridBreadth][4];
	float lrate = (float) 0.49;
	float dicount = (float) 0.29;

	class State {

		private int x, y;

		public State(int x, int y) {
			this.x = x;
			this.y = y;
		}

		public void setX(int x) {
			this.x = x;
		}

		public void setY(int y) {
			this.y = y;
		}

		public int getX() {
			return this.x;
		}

		public int getY() {
			return this.y;
		}

		public void nextState(int a) {
			// keep the next state same if try to escape the grid
			if (this.x + action[a][0] >= gridLength || this.x + action[a][0] < 0 || this.y + action[a][1] >= gridBreadth
					|| this.y + action[a][1] < 0)
				return;

			this.setX(this.getX() + action[a][0]);
			this.setY(this.getY() + action[a][1]);
		}

	}

	public int chooseAction(State curState) {
		// takes a greedy action i.e the one with max q value
		float maxval = (float) Integer.MIN_VALUE;
		int a = action.length - 1;
		for (int i = action.length - 1; i >= 0; i--) {
			if (maxval < this.qtable[curState.getX()][curState.getY()][i]) {
				maxval = this.qtable[curState.getX()][curState.getY()][i];
				a = i;
			}
		}
		return a;
	}

	public static int[] qlRun(QLearning ql, int numEpisode, String version) {
		int[] result = new int[numEpisode];
		int episode = 1;
		while (episode < numEpisode) {
			// for each episode start at 0,0
			ql.currState.setX(0);
			ql.currState.setY(0);
			int numSteps = 0;
			while ((ql.currState.getX() < QLearning.gridLength - 1)
					|| (ql.currState.getY() < QLearning.gridBreadth - 1)) {
				// run while loop till we reach bottom right corner

				// chose action based on q-values
				int a = ql.chooseAction(ql.currState);
				float oldval = ql.qtable[ql.currState.getX()][ql.currState.getY()][a];
				int x = ql.currState.getX();
				int y = ql.currState.getY();
				ql.currState.nextState(a);
				int nx = ql.currState.getX();
				int ny = ql.currState.getY();
				float maxval = Integer.MIN_VALUE;
				for (int i = 0; i < QLearning.action.length; i++) {
					maxval = Math.max(maxval, ql.qtable[nx][ny][i]);
				}
				// update qvalue
				ql.qtable[x][y][a] += ql.lrate * (ql.reward[x][y][a] + ql.dicount * maxval - oldval);
				numSteps++;
			}
			// store steps vs episode for plot
			result[episode] = numSteps;
			episode++;
		}
		return result;
	}

	public static void main(String[] args) {
		int numEpisode = 500;
		QLearning ql = new QLearning();
		for (int i = 0; i < QLearning.gridLength; i++) {
			for (int j = 0; j < QLearning.gridBreadth; j++) {
				for (int k = 0; k < QLearning.action.length; k++) {
					// reward is 0 if the action takes us to the bottom right
					// corner of maze else -1
					if ((i == QLearning.gridLength - 1 && j == QLearning.gridBreadth - 2 && k == 3)
							|| (i == QLearning.gridBreadth - 2 && j == QLearning.gridLength - 1 && k == 1))
						ql.reward[i][j][k] = 0;
					else
						ql.reward[i][j][k] = -1;
				}
			}
		}

		// plotting the average of 20 curves
		int avgIter = 20;
		int[] avgRes = new int[numEpisode];
		String version1 = "q-table initialized to 0";
		// initialize q tables with 0
		ql.initQtable(false);
		for (int i = 0; i < avgIter; i++) {
			int[] result = QLearning.qlRun(ql, numEpisode, version1);
			for (int j = 0; j < numEpisode; j++) {
				avgRes[j] += result[j];
			}
		}

		for (int j = 0; j < numEpisode; j++)
			avgRes[j] /= avgIter;
		ql.plotCurve(avgRes, version1);

		int[] avgRes1 = new int[numEpisode];
		String version2 = "q-table initialized to 1000";
		// initialize q tables with 1000
		ql.initQtable(true);
		for (int i = 0; i < avgIter; i++) {
			int[] result = QLearning.qlRun(ql, numEpisode, version2);
			for (int j = 0; j < numEpisode; j++) {
				avgRes1[j] += result[j];
			}
		}
		for (int j = 0; j < numEpisode; j++) {
			avgRes1[j] /= avgIter;
		}
		ql.plotCurve(avgRes1, version2);
	}

	void plotCurve(int[] data, String version) {
		Plot p = new Plot("Qlearning plot ", data, version);
		p.pack();
		RefineryUtilities.centerFrameOnScreen(p);
		p.setVisible(true);
	}

	void initQtable(boolean largeInit) {
		for (int i = 0; i < QLearning.gridLength; i++) {
			for (int j = 0; j < QLearning.gridBreadth; j++) {
				for (int k = 0; k < QLearning.action.length; k++) {
					if (largeInit) { // initialize by large positive number
						this.qtable[i][j][k] = 1000;
					} else {
						this.qtable[i][j][k] = 0;
					}
				}
			}
		}
	}

}