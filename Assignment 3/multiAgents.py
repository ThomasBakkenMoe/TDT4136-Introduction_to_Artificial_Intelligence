# multiAgents.py
# --------------
# Licensing Information:  You are free to use or extend these projects for
# educational purposes provided that (1) you do not distribute or publish
# solutions, (2) you retain this notice, and (3) you provide clear
# attribution to UC Berkeley, including a link to http://ai.berkeley.edu.
# 
# Attribution Information: The Pacman AI projects were developed at UC Berkeley.
# The core projects and autograders were primarily created by John DeNero
# (denero@cs.berkeley.edu) and Dan Klein (klein@cs.berkeley.edu).
# Student side autograding was added by Brad Miller, Nick Hay, and
# Pieter Abbeel (pabbeel@cs.berkeley.edu).


from util import manhattanDistance
from game import Directions
import random, util

from game import Agent

class ReflexAgent(Agent):
    """
    A reflex agent chooses an action at each choice point by examining
    its alternatives via a state evaluation function.

    The code below is provided as a guide.  You are welcome to change
    it in any way you see fit, so long as you don't touch our method
    headers.
    """


    def getAction(self, gameState):
        """
        You do not need to change this method, but you're welcome to.

        getAction chooses among the best options according to the evaluation function.

        Just like in the previous project, getAction takes a GameState and returns
        some Directions.X for some X in the set {NORTH, SOUTH, WEST, EAST, STOP}
        """
        # Collect legal moves and successor states
        legalMoves = gameState.getLegalActions()

        # Choose one of the best actions
        scores = [self.evaluationFunction(gameState, action) for action in legalMoves]
        bestScore = max(scores)
        bestIndices = [index for index in range(len(scores)) if scores[index] == bestScore]
        chosenIndex = random.choice(bestIndices) # Pick randomly among the best

        "Add more of your code here if you want to"

        return legalMoves[chosenIndex]

    def evaluationFunction(self, currentGameState, action):
        """
        Design a better evaluation function here.

        The evaluation function takes in the current and proposed successor
        GameStates (pacman.py) and returns a number, where higher numbers are better.

        The code below extracts some useful information from the state, like the
        remaining food (newFood) and Pacman position after moving (newPos).
        newScaredTimes holds the number of moves that each ghost will remain
        scared because of Pacman having eaten a power pellet.

        Print out these variables to see what you're getting, then combine them
        to create a masterful evaluation function.
        """
        # Useful information you can extract from a GameState (pacman.py)
        successorGameState = currentGameState.generatePacmanSuccessor(action)
        newPos = successorGameState.getPacmanPosition()
        newFood = successorGameState.getFood()
        newGhostStates = successorGameState.getGhostStates()
        newScaredTimes = [ghostState.scaredTimer for ghostState in newGhostStates]

        "*** YOUR CODE HERE ***"
        return successorGameState.getScore()

def scoreEvaluationFunction(currentGameState):
    """
    This default evaluation function just returns the score of the state.
    The score is the same one displayed in the Pacman GUI.

    This evaluation function is meant for use with adversarial search agents
    (not reflex agents).
    """
    return currentGameState.getScore()

class MultiAgentSearchAgent(Agent):
    """
    This class provides some common elements to all of your
    multi-agent searchers.  Any methods defined here will be available
    to the MinimaxPacmanAgent, AlphaBetaPacmanAgent & ExpectimaxPacmanAgent.

    You *do not* need to make any changes here, but you can if you want to
    add functionality to all your adversarial search agents.  Please do not
    remove anything, however.

    Note: this is an abstract class: one that should not be instantiated.  It's
    only partially specified, and designed to be extended.  Agent (game.py)
    is another abstract class.
    """

    def __init__(self, evalFn = 'scoreEvaluationFunction', depth = '2'):
        self.index = 0 # Pacman is always agent index 0
        self.evaluationFunction = util.lookup(evalFn, globals())
        self.depth = int(depth)


class MinimaxAgent(MultiAgentSearchAgent):
    """
    Your minimax agent (question 2)
    """

    def getAction(self, gameState):
        """
        Returns the minimax action from the current gameState using self.depth
        and self.evaluationFunction.

        Here are some method calls that might be useful when implementing minimax.

        gameState.getLegalActions(agentIndex):
        Returns a list of legal actions for an agent
        agentIndex=0 means Pacman, ghosts are >= 1

        gameState.generateSuccessor(agentIndex, action):
        Returns the successor game state after an agent takes an action

        gameState.getNumAgents():
        Returns the total number of agents in the game

        gameState.isWin():
        Returns whether or not the game state is a winning state

        gameState.isLose():
        Returns whether or not the game state is a losing state
        """
        "*** YOUR CODE HERE ***"

        # get the legal actions for agent 0 (pacman)
        legalActions = gameState.getLegalActions(0)
        # Generate one successor state for each legal action
        # These are the nodes inhabiting the 2nd layer of the
        successorStates = [gameState.generateSuccessor(0, action) for action in legalActions]
        maxNodeScore = -float('inf')  # maxNodeScore is initialized as negative infinity.
        chosenIndex = 0

        for i in range(len(successorStates)):
            nodeScore = self.evaluateNode(successorStates[i], 1, 0)
            if nodeScore > maxNodeScore:
                maxNodeScore = nodeScore
                chosenIndex = i  # The index of the action with the highest score

        return legalActions[chosenIndex]

    def evaluateNode(self, gameState, agentIndex, depth):

        # If max depth is reached or the current gameState (being evaluated) is a win or a loss, return.
        if depth == self.depth or gameState.isWin() or gameState.isLose():
            return self.evaluationFunction(gameState)

        legalActions = gameState.getLegalActions(agentIndex)
        successorStates = [gameState.generateSuccessor(agentIndex, action) for action in legalActions]

        # MAX, only for pacman (agent 0)
        if agentIndex == 0:
            maxNodeScore = -float('inf')  # maxNodeScore is initialized as negative infinity.
            for successorState in successorStates:
                # using max() so that maxNodeScore is only updated if the return from the recursive self.evaluateNode is
                # bigger than the current maxNodeScore
                maxNodeScore = max(maxNodeScore, self.evaluateNode(successorState, 1, depth))
            return maxNodeScore

        # MIN, for the ghosts (agent > 0)
        if agentIndex > 0:
            minNodeScore = float('inf')  # minNodeScore is initialized as negative infinity.
            for successorState in successorStates:
                # using min() so that minNodeScore is only updated if the return from the recursive self.evaluateNode is
                # smaller than the current minNodeScore
                if agentIndex+1 == gameState.getNumAgents():
                    minNodeScore = min(minNodeScore, self.evaluateNode(successorState, 0, depth+1))
                else:
                    minNodeScore = min(minNodeScore, self.evaluateNode(successorState, agentIndex+1, depth))
            return minNodeScore


class AlphaBetaAgent(MultiAgentSearchAgent):
    """
    Your minimax agent with alpha-beta pruning (question 3)
    """

    def getAction(self, gameState):
        """
        Returns the minimax action using self.depth and self.evaluationFunction
        """
        "*** YOUR CODE HERE ***"

        alpha = -float('inf')
        beta = float('inf')

        # get the legal actions for agent 0 (pacman)
        legalActions = gameState.getLegalActions(0)
        # Generate one successor state for each legal action
        # These are the nodes inhabiting the 2nd layer of the
        successorStates = [gameState.generateSuccessor(0, action) for action in legalActions]
        maxNodeScore = -float('inf')  # maxNodeScore is initialized as negative infinity.
        chosenIndex = 0

        for i in range(len(successorStates)):
            nodeScore = self.evaluateNode(successorStates[i], 1, 0, alpha, beta)
            if nodeScore > maxNodeScore:
                maxNodeScore = nodeScore
                chosenIndex = i  # The index of the action with the highest score
                alpha = nodeScore

        return legalActions[chosenIndex]

    def evaluateNode(self, gameState, agentIndex, depth, alpha, beta):

        # If max depth is reached or the current gameState (being evaluated) is a win or a loss, return.
        if depth == self.depth or gameState.isWin() or gameState.isLose():
            return self.evaluationFunction(gameState)

        legalActions = gameState.getLegalActions(agentIndex)

        # MAX, only for pacman (agent 0)
        if agentIndex == 0:
            maxNodeScore = -float('inf')  # maxNodeScore is initialized as negative infinity.
            for action in legalActions:
                # only generate the successor state if it is actually needed
                successorState = gameState.generateSuccessor(agentIndex, action)
                # using max() so that maxNodeScore is only updated if the return from the recursive self.evaluateNode is
                # bigger than the current maxNodeScore
                maxNodeScore = max(maxNodeScore, self.evaluateNode(successorState, 1, depth, alpha, beta))

                # If the score of a node branch returns a score higher than beta: then this entire cluster of branches
                # can be ignored, MIN will prevent these branches from being selected
                if maxNodeScore > beta:
                    return maxNodeScore
                alpha = max(alpha, maxNodeScore)
            return maxNodeScore

        # MIN, for the ghosts (agent > 0)
        if agentIndex > 0:
            minNodeScore = float('inf')  # minNodeScore is initialized as negative infinity.
            for action in legalActions:
                # only generate the successor state if it is actually needed
                successorState = gameState.generateSuccessor(agentIndex, action)
                # using min() so that minNodeScore is only updated if the return from the recursive self.evaluateNode is
                # smaller than the current minNodeScore
                if agentIndex + 1 == gameState.getNumAgents():
                    minNodeScore = min(minNodeScore, self.evaluateNode(successorState, 0, depth+1, alpha, beta))
                else:
                    minNodeScore = min(minNodeScore, self.evaluateNode(successorState, agentIndex+1, depth, alpha, beta))

                # If the score of a node branch returns a score lower than alpha: then this entire cluster of branches
                # can be ignored, MAX will prevent these branches from being selected
                if minNodeScore < alpha:
                    return minNodeScore
                beta = min(beta, minNodeScore)
            return minNodeScore

class ExpectimaxAgent(MultiAgentSearchAgent):
    """
      Your expectimax agent (question 4)
    """

    def getAction(self, gameState):
        """
        Returns the expectimax action using self.depth and self.evaluationFunction

        All ghosts should be modeled as choosing uniformly at random from their
        legal moves.
        """
        "*** YOUR CODE HERE ***"
        util.raiseNotDefined()

def betterEvaluationFunction(currentGameState):
    """
    Your extreme ghost-hunting, pellet-nabbing, food-gobbling, unstoppable
    evaluation function (question 5).

    DESCRIPTION: <write something here so we know what you did>
    """
    "*** YOUR CODE HERE ***"
    util.raiseNotDefined()

# Abbreviation
better = betterEvaluationFunction
