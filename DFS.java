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