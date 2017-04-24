# qlearning
Qlearning Implementation in java for a 15x15 grid

Problem

Implement Q-learning (with the programming language of your choice)
and use it to find an optimal policy for traversing a 15x15 grid world with
4 actions that move one step in the standard compass directions. Each
episode starts with the learner in the upper left corner state and ends
when the learner enters the lower right corner state. Attempts to move off
the edges of the grid world should result in the agent staying in the same
state. Choose any reward structure that causes the learner to minimize
the number of steps required to end the episode. Initialize the Q-table to
all zeroes

Parameters:
1) Learning rate 0.49
2) Discount factor 0.29
3) Rewards is 0 if we reach goal(lower right corner) else -1
