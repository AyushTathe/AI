#include <iostream>
#include <vector>
#include <queue>
#include <unordered_map>

using namespace std;

struct VectorHash {
    size_t operator()(const std::vector<int>& v) const {
        std::hash<int> hasher;
        size_t seed = 0;
        for (int i : v) {
            seed ^= hasher(i) + 0x9e3779b9 + (seed << 6) + (seed >> 2);
        }
        return seed;
    }
};

bool isValid(vector<int>& queens, int row, int col) {
    for (int i = 0; i < row; ++i) {
        if (queens[i] == col || abs(row - i) == abs(col - queens[i])) {
            return false;
        }
    }
    return true;
}

void displayBoard(vector<int>& queens) {
    int n = queens.size();
    for (int i = 0; i < n; ++i) {
        for (int j = 0; j < n; ++j) {
            if (queens[i] == j) {
                cout << "Q ";
            } else {
                cout << ". ";
            }
        }
        cout << endl;
    }
    cout << endl;
}

void bfsNQueens(int n) {
    queue<vector<int>> q;
    unordered_map<vector<int>, vector<int>, VectorHash> parent; 
    vector<int> queens(n, -1);
    q.push(queens);
    
    while (!q.empty()) {
        vector<int> curr = q.front();
        q.pop();
        int row = 0;
        while (row < n && curr[row] != -1) {
            row++;
        }
        if (row == n) {
            cout << "Solution found:" << endl;
            displayBoard(curr);
            // Display the BFS traversal tree
            cout << "BFS Traversal Tree:" << endl;
            vector<int> node = curr;
            while (parent.find(node) != parent.end()) {
                displayBoard(node);
                cout << "|\nV\n";
                node = parent[node];
            }
            displayBoard(node);
            return;
        }
        cout << "BFS Step " << row << ":" << endl;
        for (int col = 0; col < n; ++col) {
            if (isValid(curr, row, col)) {
                vector<int> next = curr;
                next[row] = col;
                q.push(next);
                parent[next] = curr; 
                displayBoard(next);
            }
        }
    }
    cout << "No solution found!" << endl;
}

int main() {
    int n;
    cout << "Enter the size of the chessboard (N x N): ";
    cin >> n;
    bfsNQueens(n);
    return 0;
}
