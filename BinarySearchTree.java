//package edu.belmont.csc.src.search;

public class BinarySearchTree {
    static class BSTNode {
        // TODO: Add the internal data members
        BSTNode right, left;
        int value, height, bf;
        BSTNode(int data){
           right = null;
           left = null;
           value = data;
        }
        // END OF TODO
    }

    public BSTNode root;
    private int nodeCount = 1; // # of nodes in the tree.

    public int size() {
        return nodeCount;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * Return whether a value exists in the tree
     * @param value
     * @return
     */
    public boolean contains(int value) {
        return contains(root, value);
    }

    /**
     * A recursive helper method to check if a value exists in the tree
     * @param node - current node
     * @param value - value to search for
     * @return true if found and false otherwise
     */
    private boolean contains(BSTNode node, int value) {
        // TODO: implement this method
        if(node == null) return false;
        int cur = node.value;
        //left
        if(value < cur)
            return contains(node.left, value);
        //right
        if(value > cur)
            return contains(node.right, value);
        return true;
    }

    /**
     * Insert a value to the AVL tree. Duplicates are not allowed
     * @param value - value to be added
     * @return true if insertion is successful and false otherwise
     */
    public boolean insert(int value) {
        if (!contains(root, value)) {
            root = insert(root, value);
            nodeCount++;
            return true;
        }
        return false;
    }

    /**
     * Recursive helper method to insert the given value to the tree while maintaining the BST invariant
     * @param node - root node of current subtree
     * @param value - value to be inserted
     * @return root node of current balanced subtree
     */
    private BSTNode insert(BSTNode node, int value) {
        // TODO: implement this method
        if(node == null) return new BSTNode(value);
        if(value < node.value)
            node.left = insert(node.left, value);
        else
            node.right = insert(node.right, value);
        update(node);
        return balance(node);
    }

    /**
     * Update a node's internal data when modified (hint: needed during insertion and removal)
     * @param node - node to be updated
     */
    private void update(BSTNode node) {
        // TODO: implement this method
        int LH = height(node.left), RH = height(node.right);
        if(LH > RH)
            node.height = height(node.left);
        else
            node.height = height(node.right);

        node.bf = height(node.left) - height(node.right);
    }

    /**
     * Recursively find the height of the left or right subtree
     * @param node root of the subtree
     * @return return the height of the subtree
     */
    public int height(BSTNode node){
        if(node == null) return 0;
        int leftHeight = height(node.left) + 1;
        int rightHeight = height(node.right) + 1;
        if(leftHeight > rightHeight)
           return leftHeight;
        else
           return rightHeight;
    }

    /**
     * Re-balance a node if its bf is +2 or -2 (hint: needed during insertion and removal)
     * @param node - root of current subtree
     * @return root of balanced subtree
     */
    private BSTNode balance(BSTNode node) {
        // TODO: implement this method using leftRotate and rightRotate
        //right heavy
        if(node.bf < -1) {
            //right right case
            if(node.right.bf <= 0) {
                return leftRotate(node);
            }
            //right left case
            else {
                node.right = rightRotate(node.right);
                return leftRotate(node);
            }
        }
        //left heavy
        else if(node.bf > 1) {
            //left left case
            if (node.left.bf >= 0){
                return rightRotate(node);
            }
            //left right case
            else {
                node.left = leftRotate(node.left);
                return rightRotate(node);
            }
        }
        if(root == null) root = node;
        //bf = 0, 1, or -1
        return node;
    }

    /**
     * Performs left rotation on the specified node
     * @param node - root of subtree
     * @return new root of subtree
     */
    private BSTNode leftRotate(BSTNode node) {
        // TODO: implement this method - make sure it calls the private remove helper method
        BSTNode t = node.right;
        node.right = t.left;
        t.left = node;
        return t;
    }

    /**
     * Performs right rotation on the specified node
     * @param node - root of subtree
     * @return new root of subtree
     */
    private BSTNode rightRotate(BSTNode node) {
        // TODO: implement this method - make sure it calls the private remove helper method
        BSTNode t = node.left;
        node.left = t.right;
        t.right = node;
        return t;
    }

    // Remove a value from this binary tree if it exists, O(log(n))
    /**
     * Removes the specified value from the tree
     * @param element - element to be removed
     * @return true if successfully removed and false otherwise
     */
    public boolean remove(int element) {
        // TODO: implement this method - make sure it calls the private remove helper method
        if(contains(root, element)){
            root = remove(root, element);
            nodeCount--;
            return true;
        }
        return false;
    }

    /**
     * A recursive helper method to remove the element from the specified tree
     * @param node - root of current subtree
     * @param element - element to be removed
     * @return root of balanced subtree
     */
    private BSTNode remove(BSTNode node, int element) {
        // TODO: implement this method - make sure it calls the private remove helper method
        //base case: not in tree
        if(node == null) return node;
        //if less than root go left
        if(element < node.value)
            node.left = remove(node.left, element);
        //if greater than root go right
        else if(element > node.value)
            node.right = remove(node.right, element);
        //found
        else{
            //case1: no children & case2: only child
            if(node.right == null || node.left == null){
                BSTNode child;
                if(node.left != null)
                    child = node.left;
                else
                    child  = node.right;

                //case1: no children
                if(child == null) {
                    node = null;
                } else{
                    //case2: only child
                    node = child;
                }
            }
            //case3: two children
            else {
                if(node.left.height < node.right.height){
                    // right sub-tree
                    //find min on right subtree
                    int minElem = min(node.right);
                    node.value = minElem;
                    //smallest in right sub tree
                    BSTNode tmp = remove(node.right, minElem);
                    node.right = tmp;
                }
                else{
//                    //left subtree removal
//                    //find max on left subtree
//                    BSTNode maxElem = node.left;
//                    while(maxElem.right != null){
//                        maxElem = maxElem.right;
//                    }
//                    //swap with max
//                    maxElem = node;
//                    node.value = maxElem.value;
//                    //largest in left subtree
//                    node.right = remove(node.right, node.value);
                    int maxElem = max(node.left);
                    node.value = maxElem;
                    BSTNode tmp = remove(node.left, maxElem);
                    node.left = tmp;
                }
            }
        }
        if (node == null) return node;
        update(node);
        return balance(node);
    }

    //helper methods fro min and max
    private int max(BSTNode node){
        while(node.right != null){
            node = node.right;
        }
        return node.value;
    }
    private int min(BSTNode node){
        while(node.left != null){
            node = node.left;
        }
        return node.value;
    }

    /**
     * A recursive method to check if a given tree is a valid binary search tree
     * @param node - root of tree
     * @return true if valid and false otherwise
     */
    public boolean isValid(BSTNode node) {
        // TODO: implement this method - make sure it calls the private remove helper method
        //empty tree is still valid
        if(node == null) return true;

        //check bst
//       if( node.left == null && node.right == null) return true;
//        else if(node.left == null) {
//            if (node.right.value > node.value) {
//                return isValid(node.right);
//            }
//        }
//        else if (root.left.value < node.value) {
//            return isValid(node.left);
//        }
//        return isValid(node.left) && isValid(node.right);

        //check avl
//        if(Math.abs(node.left.height - node.right.height) <= 1) return true;
//        }

        //inorder check
        if(!isValid(node.left)) return false;
        int prev = 0;
        if(node.value <= prev) return false;
        prev = node.value;
        if(!isValid(node.right)) return false;

        return true;
    }

    public void printTree(){
        System.out.print("Preorder: ");
        preorder(root);
        System.out.println();
        System.out.print("Postorder: ");
        postorder(root);
        System.out.println();
        System.out.print("Inorder: ");
        inorder(root);
        System.out.println();
    }

    private void preorder(BSTNode t){
        //root node first
        if(t == null) return;
        System.out.print(t.value + " ");
        preorder(t.left);
        preorder(t.right);
    }
    private void postorder(BSTNode t){
        //root node last
        if(t == null) return;
        postorder(t.left);
        postorder(t.right);
        System.out.print(t.value + " ");
    }
    public void inorder(BSTNode t){
        //ascending order
        if(t == null) return;
        inorder(t.left);
        System.out.print(t.value + " ");
        inorder(t.right);
    }

    // Driver method
    public static void main(String[] args)
    {
        BinarySearchTree tree = new BinarySearchTree();
        tree.root = new BSTNode(1);
        tree.insert(5);
        tree.insert(4);
        tree.insert(2);
        tree.insert(3);
        tree.insert(1);

        tree.printTree();
        System.out.println("Tree Size: " + tree.size());

        System.out.println("This tree is a valid BST: " + tree.isValid(tree.root));

        tree.remove(2);
        tree.remove(1);

        tree.printTree();

        System.out.println("This tree is a valid BST: " + tree.isValid(tree.root));

        // TODO: add your own test cases. For full credit, your tests must check that all methods are correctly implemented
        tree.remove(5);
        tree.remove(3);
        tree.remove(4);
        tree.printTree();
        System.out.println("Tree Size: " + tree.size());
        System.out.println("This tree is a valid BST: " + tree.isValid(tree.root));
        tree.isEmpty();
        System.out.println("Empty Tree? " + tree.isEmpty());

        tree.insert(2);
        tree.insert(3);
        tree.insert(6);
        tree.insert(7);
        tree.insert(12);
        tree.insert(8);
        System.out.println("Tree Size: " + tree.size());
        tree.printTree();
        System.out.println("This tree is a valid BST: " + tree.isValid(tree.root));

        System.out.println("Contains -99? "+ tree.contains(-99));
        System.out.println("Contains 8? "+ tree.contains(8));
        System.out.println("Contains 6? "+ tree.contains(6));
        System.out.println("Contains 7? "+ tree.contains(7));
        System.out.println("Contains 12? "+ tree.contains(12));
        System.out.println("Contains 3? "+ tree.contains(3));
        System.out.println("Contains 2? "+ tree.contains(2));
        System.out.println("Contains 0? "+ tree.contains(0));

        tree.printTree();

        System.out.println("Remove Root 7");
        tree.remove(7);
        tree.printTree();
        System.out.println("Tree Size: " + tree.size());
        System.out.println("This tree is a valid BST: " + tree.isValid(tree.root));

        System.out.println("Insert Right Child 19");
        tree.insert(19);
        tree.printTree();
        System.out.println("Tree Size: " + tree.size());
        System.out.println("This tree is a valid BST: " + tree.isValid(tree.root));

        System.out.println("Insert Left Child of 17");
        tree.insert(17);
        tree.printTree();
        System.out.println("Tree Size: " + tree.size());
        System.out.println("This tree is a valid BST: " + tree.isValid(tree.root));

        System.out.println("Insert Left Child 1");
        tree.insert(1);
        tree.printTree();
        System.out.println("Tree Size: " + tree.size());
        System.out.println("This tree is a valid BST: " + tree.isValid(tree.root));

        System.out.println("Insert Right Child 4");
        tree.insert(4);
        tree.printTree();
        System.out.println("Tree Size: " + tree.size());
        System.out.println("This tree is a valid BST: " + tree.isValid(tree.root));

        System.out.println("Delete Right Leaf Node 4");
        tree.remove(4);
        tree.printTree();
        System.out.println("Tree Size: " + tree.size());
        System.out.println("This tree is a valid BST: " + tree.isValid(tree.root));

        System.out.println("Delete Left Leaf Node 17");
        tree.remove(17);
        tree.printTree();
        System.out.println("Tree Size: " + tree.size());
        System.out.println("This tree is a valid BST: " + tree.isValid(tree.root));

    }
}
