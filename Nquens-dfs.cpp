#include <iostream>
#include <vector>
#include <cmath>

using namespace std;

bool isSafe(vector<int>& board, int row, int col) {
    for (int i = 0; i < row; ++i) {
        if (board[i] == col || abs(i - row) == abs(board[i] - col)) {
            return false;
        }
    }
    return true;
}

bool solveNQueensDFS(vector<vector<int>>& result, vector<int>& board, int row, int n) {
    if (row == n) {
        result.push_back(board);
        return true;
    }

    bool res = false;
    for (int col = 0; col < n; ++col) {
        if (isSafe(board, row, col)) {
            board[row] = col;
            res = solveNQueensDFS(result, board, row + 1, n) || res;
            board[row] = -1;
        }
    }
    return res;
}

void printBoard(vector<int>& board) {
    int n = board.size();
    for (int i = 0; i < n; ++i) {
        for (int j = 0; j < n; ++j) {
            if (board[i] == j) {
                cout << "Q ";
            } else {
                cout << ". ";
            }
        }
        cout << endl;
    }
    cout << endl;
}

void solveNQueens(int n) {
    vector<vector<int>> result;
    vector<int> board(n, -1); 
    solveNQueensDFS(result, board, 0, n);

    for (int i = 0; i < result.size(); ++i) {
        cout << "Solution " << i + 1 << ":\n";
        for (int j = 0; j < n; ++j) {
            printBoard(result[i]);
        }
    }
}

int main() {
    int n;
    cout << "Enter the size of the chessboard (N x N): ";
    cin >> n;
    solveNQueens(n);
    return 0;
}
