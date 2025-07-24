package com.tavloo;

import java.util.ArrayList;

/**
 * Tree node implementation for the minimax algorithm used by the Bot.
 * Represents a game state in the decision tree.
 */
public class TreeNode {
    
    /** Child nodes in the decision tree */
    public ArrayList <TreeNode> children;
    /** Evaluation value of this game state */
    public int value;
    /** Game state represented by this node */
    public Game game;

    TreeNode(int value, Game game){
        this.children = new ArrayList<TreeNode>();
        this.value = value;
        this.game = game;
    }
    
    /**
     * Adds a child node to this tree node.
     * @param val The evaluation value for the child node
     * @param game The game state for the child node
     * @return The newly created child TreeNode
     */
    public TreeNode addChild(int val, Game game){
        TreeNode child = new TreeNode(val, game);
        children.add(child);
        return child;
    }
}
