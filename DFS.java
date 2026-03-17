class Solution {
    int [][] dirs = {{-1,0}, {0,-1}, {0, 1}, {1,0}};
    public void solve(char[][] board) {
        int r = board.length, c = board[0].length;
        for(int i = 0; i < r; i++) {
            for(int j = 0; j < c; j++) {
                if (i == 0 || i == r-1 || j == 0 || j == c-1) {
                    if (board[i][j] == 'O')
                        dfs(r, c, board, i , j);
                }
            }
        }
         for(int i = 0; i < r; i++) {
            for(int j = 0; j < c; j++) {
               if (board[i][j] == 'O') {
                   board[i][j] = 'X';
                }
                if (board[i][j] == 'T') {
                   board[i][j] = 'O';
                }
            }
        }
    }

    private void dfs(int r , int c, char[][]  board, int i, int j) {
        board[i][j] = 'T';
        for(int [] d : dirs) {
            int ni = i + d[0], nj = j + d[1];
            if (ni >= r || nj >= c || ni < 0 || nj < 0 || board[ni][nj] == 'T')
                continue; // out of matrix
            if (board[ni][nj] == 'O')
                dfs(r, c, board, ni, nj);    
        }

    }
}



Time Complexity:
O(m × n)

Space Complexity:
O(m × n) worst case recursion stack.

Pattern: Mark from boundaries instead of checking internal conditions


Mistake: Didn't read question thoroughly, Made boundary o to x lol. Forgot to update the surrounded region.


______________________________________________________________________________________________________________________________________________
Input: board = [["o","a","a","n"],["e","t","a","e"],["i","h","k","r"],["i","f","l","v"]], words = ["oath","pea","eat","rain"]
Output: ["eat","oath"]

class Solution {
    int dirs[][] = {{-1,0},{1,0},{0,1},{0,-1}};

    public List<String> findWords(char[][] board, String[] words) {
        List<String> ans = new ArrayList<>();

        for(String word : words) {
            boolean found = false;
            boolean[][] visited = new boolean[board.length][board[0].length];

            for(int i=0;i<board.length;i++){
                for(int j=0;j<board[0].length;j++){
                    if(check(board, visited, i, j, word, 0)){
                        ans.add(word);
                        found = true;
                        break;
                    }
                }
                if(found) break;
            }
        }

        return ans;
    }

    private boolean check(char[][] b, boolean[][] visited, int i, int j, String word, int w) {

        if(i<0 || j<0 || i>=b.length || j>=b[0].length)
            return false;

        if(visited[i][j])
            return false;

        if(b[i][j] != word.charAt(w))
            return false;

        if(w == word.length()-1)
            return true;

        visited[i][j] = true;

        for(int[] d : dirs){
            if(check(b, visited, i+d[0], j+d[1], word, w+1)){
                visited[i][j] = false;
                return true;
            }
        }

        visited[i][j] = false;
        return false;
    }
}

Time Complexity:
    O(W * M * N * 4^L)

Space Complexity
    O(m × n + L)

Pattern:
    Insert all words into a Trie
    Start DFS from every board cell
    While moving in DFS:
    Follow the Trie path
    If Trie node contains a word → add to result
    Mark board cell as visited ('#')
    Backtrack

Mistake:    
    out of box range check should be first.
    Didn't used visited array
    Calculated TC incorrect hence incorrect approach uses (brute)
    TRIE possiblity
    TRIE mistake:   for(int[] d : dirs){
               if(i+d[0] < 0 || j+d[1] < 0 || i + d[0]>= b.length || j +d[1]>= b[0].length || b[i+d[0]][j+d[1]] == '#') continue;
            dfs(i+d[0], j + d[1], b, root.arr[temp -'a']);
        } // Failing for single element


   TC :(M * N * 4^L)     
   class Solution {
    List<String> ans = new ArrayList<>();
    int dirs[][] = {{-1,0},{1,0},{0,1},{0,-1}};

    public List<String> findWords(char[][] board, String[] words) {
        Trie root = new Trie("");
        Trie temp = root;

        for(String w : words) {
            temp = root;
            for(char i : w.toCharArray()) {

                if (temp.arr[i - 'a'] == null)
                    temp.arr[i - 'a'] = new Trie(temp.wor + i);

                temp = temp.arr[i-'a'];
            }
            temp.isFound = true;
        }

        for(int i = 0; i < board.length; i++) {
            for(int j = 0; j < board[0].length; j++) {

                if (root.arr[board[i][j]-'a'] != null) {
                    dfs(i, j, board, root);
                }

            }
        }
        return ans;
    }

    private void dfs(int i, int j, char[][] b, Trie root) {

        if(i < 0 || j < 0 || i >= b.length || j >= b[0].length)
            return;

        if (b[i][j] == '#') return;

        if (root.arr[b[i][j] - 'a'] == null) return;

        root = root.arr[b[i][j] - 'a'];

        if (root.isFound) {
            ans.add(root.wor);
            root.isFound = false;
        }

        char temp = b[i][j];
        b[i][j] = '#';

        for(int[] d : dirs){
            dfs(i + d[0], j + d[1], b, root);
        }

        b[i][j] = temp;
    }
}

class Trie {
    boolean isFound;
    Trie [] arr = new Trie[26];
    String wor = "";

    Trie(String h) {
        wor = h;
    }
}

__________________________________________________________________________________________________


Given a string containing digits from 2-9 inclusive, return all possible letter combinations that the number could represent. Return the answer in any order.

A mapping of digits to letters (just like on the telephone buttons) is given below. Note that 1 does not map to any letters.



class Solution {
    
    public List<String> letterCombinations(String digits) {
        List<String> ans = new ArrayList<>();
        List<String> adj = List.of("abc", "def", "ghi", "jkl", "mno", "pqrs", "tuv", "wxyz");
        recur(digits, 0, adj, new StringBuilder(), ans);
        return ans;
    }

    private void recur(String digits, int i, List<String> adj, StringBuilder temp, List<String> ans) {
        if (i == digits.length()) {
            ans.add(temp.toString());
            return;
        }
        int d = Integer.parseInt(""+digits.charAt(i));
        for(char j : adj.get(d-2).toCharArray()) {
            temp.append(j);
            recur(digits, i+1, adj, temp, ans);
            temp.deleteCharAt(temp.length()-1);
        }
    }
}


TC : o(4^n)
PATTERN : Backtracking
MISTAKE: failed to delete char. SO basically when recursion come back to previous stack it was refering to temp String which was updated by nextforward stacks
        Resulting in error. You can use final result without backtracking.



long fastPow(long a, long n) {
    long result = 1;

    while (n > 0) {

        if ((n & 1) == 1) {   // if n is odd
            result *= a;
        }

        a = a * a;   // square
        n = n >> 1;  // divide by 2
    }

    return result;
}
| Complexity     | Max N that usually passes |
| -------------- | ------------------------- |
| **O(1)**       | always                    |
| **O(log N)**   | up to (10^{18})           |
| **O(N)**       | (10^7 – 10^8)             |
| **O(N log N)** | (10^6 – 10^7)             |
| **O(N²)**      | (10^3 – 10^4)             |
| **O(N³)**      | (300 – 500)               |
| **O(2ⁿ)**      | (n ≤ 20)                  |
| **O(N!)**      | (n ≤ 10)                  |

______________________________________________________________________________________________________________________________________

Given an integer array nums and an integer k, return the kth largest element in the array.

Note that it is the kth largest element in the sorted order, not the kth distinct element.

class Solution {
    public int findKthLargest(int[] nums, int k) {
        PriorityQueue<Integer> pq = new PriorityQueue<>();
        for(int i = 0; i < nums.length; i++) {
            pq.add(nums[i]);
            if (pq.size() > k) pq.poll();
        }
        return pq.poll();

    }
}

Time Complexity: O(nlogk)
Each of the n elements is processed once. However, heap operations take O(logk) time, leading to an overall complexity of O(nlogk).

Space Complexity: O(k)
The solution uses a heap with a maximum of k elements.
PATTERN: if you want largest then take minheap and remove if getting full since you are removing smallest

(To - DO How priority Queue structure uses ? HeapSort ? And sorting algo's like quicksort)
__________________________________________________________________________________________________

105. Construct Binary Tree from Preorder and Inorder Traversal
Medium
Topics
premium lock icon
Companies
Given two integer arrays preorder and inorder where preorder is the preorder traversal of a binary tree and inorder is the inorder traversal of the same tree, construct and return the binary tree.

 

Example 1:


Input: preorder = [3,9,20,15,7], inorder = [9,3,15,20,7]
Output: [3,9,20,null,null,15,7]


class Solution {

    public TreeNode buildTree(int[] preorder, int[] inorder) {
        return recur(preorder, 0, preorder.length - 1,
                     inorder, 0, inorder.length - 1);
    }

    private TreeNode recur(int[] p, int pStart, int pEnd,
                           int[] in, int iStart, int iEnd) {

        if (pStart > pEnd || iStart > iEnd)
            return null;

        TreeNode root = new TreeNode(p[pStart]);

        int mid = iStart;

        for (int k = iStart; k <= iEnd; k++) {
            if (in[k] == p[pStart]) {
                mid = k;
                break;
            }
        }

        int leftSize = mid - iStart;

        root.left = recur(p, pStart + 1, pStart + leftSize,
                          in, iStart, mid - 1);

        root.right = recur(p, pStart + leftSize + 1, pEnd,
                           in, mid + 1, iEnd);

        return root;
    }
}

Current code:

Time  : O(n²)
Space : O(n)

Optimized with HashMap:

Time : O(n)



MISTAKE: Approach was correct but failed to implement. Need extra care & choose early whether to take o or 1 based index. Failed to
    honor recursion pass by reference and root

    in recursion I was like root.left = recur(); but return type was void. Also created new Root() before recur call.


Optimized
class Solution {
    private int preorderIndex;
    private Map<Integer, Integer> mapping;

    public TreeNode buildTree(int[] preorder, int[] inorder) {
        mapping = new HashMap<>();
        for (int i = 0; i < inorder.length; i++) {
            mapping.put(inorder[i], i);
        }

        preorderIndex = 0;
        return build(preorder, 0, inorder.length - 1);        
    }

    private TreeNode build(int[] preorder, int start, int end) {
        if (start > end) return null;

        int rootVal = preorder[preorderIndex++];
        TreeNode root = new TreeNode(rootVal);
        int mid = mapping.get(rootVal);

        root.left = build(preorder, start, mid - 1);
        root.right = build(preorder, mid + 1, end);

        return root;
    }
}
______________________________________________________________________________________________________________________________________________


25. Reverse Nodes in k-Group







class Solution {
    public ListNode reverseKGroup(ListNode head, int k) {

        if (head == null || k == 1) return head;

        ListNode curr = head;
        ListNode prevTail = null;
        ListNode newHead = null;

        while (curr != null) {

            // check if k nodes exist
            ListNode check = curr;
            int count = 0;
            while (count < k && check != null) {
                check = check.next;
                count++;
            }

            if (count < k) { // not enough nodes
                if (prevTail != null) prevTail.next = curr;
                break;
            }

            // reverse k nodes
            ListNode prev = null;
            ListNode temp = curr;

            for (int i = 0; i < k; i++) {
                ListNode next = temp.next;
                temp.next = prev;
                prev = temp;
                temp = next;
            }

            if (newHead == null)
                newHead = prev;

            if (prevTail != null)
                prevTail.next = prev;

            prevTail = curr;
            curr = temp;
        }

        return newHead == null ? head : newHead;
    }
}

class Solution {
    public ListNode reverseKGroup(ListNode head, int k) {
        if (head == null) return null;

        ListNode tail = head;
        for (int i = 0; i < k; i++) {
            if (tail == null) return head;
            tail = tail.next;
        }

        ListNode newHead = reverse(head, tail);
        head.next = reverseKGroup(tail, k);
        return newHead;
    }

    private ListNode reverse(ListNode cur, ListNode end) {
        ListNode prev = null;
        while (cur != end) {
            ListNode next = cur.next;
            cur.next = prev;
            prev = cur;
            cur = next;
        }
        return prev;
    }
}

----
RECURSIOn

class Solution {
    public ListNode reverseKGroup(ListNode head, int k) {
        ListNode curr = head, prev = null, nex=null;
        int t  = k;
        boolean f = false;
        ListNode temp = head;
        for(int i = 0; i < k; i++) {
            if (temp == null) {
                f = true;
                return curr;
            }
            temp = temp.next;
        }
        while (t --> 0) {
            
            nex=curr.next;
            curr.next = prev;
            prev = curr;
            curr = nex;           
        }
        head.next = reverseKGroup(nex, k);
        return prev;
    }
}


Time complexity: O(n)
Space complexity: O(n/k) or O(n)
The algorithm is recursive: each recursive call processes k nodes. There will be approximately n / k recursive calls on the call stack at the deepest point.
This is O(n) in the worst case (when k = 1), but in practice often better.














MISTAKE:fail to think for recursion.
Fails to update first node to null started with second node.
Group boundaries (k) are not handled correctly

Remaining nodes (< k) must not be reversed


__________________________________________________________________________________
