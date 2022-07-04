import java.util.ArrayList;

public class TreeNode {
    
    public ArrayList <TreeNode> children;
    public int value;
    public Game game;

    TreeNode(int value, Game game){
        this.children = new ArrayList<TreeNode>();
        this.value = value;
        this.game = game;
    }
    
    public TreeNode addChild(int val, Game game){
        TreeNode child = new TreeNode(val, game);
        children.add(child);
        return child;
    }
}
