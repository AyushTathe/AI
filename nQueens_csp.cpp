#include <iostream>
#include <vector>

using namespace std;

const int N = 8;

bool Solve(vector<int> &board, int col);
bool PlaceQueen(vector<int> &board, int row, int col);
bool IsSafe(const vector<int> &board, int row, int col);
void PrintBoard(const vector<int> &board);

int main() {
    vector<int> board(N, -1); 
    bool foundSolution = Solve(board, 0);
    
    if (foundSolution) {
        cout << "Solution found:" << endl;
        PrintBoard(board);
    } else {
        cout << "No solution found." << endl;
    }
    
    return 0;
}

bool Solve(vector<int> &board, int col) {
    if (col >= N) return true; 
    
    for (int rowToTry = 0; rowToTry < N; rowToTry++) {
        if (PlaceQueen(board, rowToTry, col)) {
            cout << "Placing queen at (" << rowToTry << "," << col << ")" << endl;
            PrintBoard(board);
            if (Solve(board, col + 1)) return true; 
            board[col] = -1; 
            cout << "Backtracking..." << endl;
            PrintBoard(board);
        }
    }
    return false; 
}

bool PlaceQueen(vector<int> &board, int row, int col) {
    if (IsSafe(board, row, col)) {
        board[col] = row; 
        return true;
    }
    return false;
}

bool IsSafe(const vector<int> &board, int row, int col) {
    for (int prevCol = 0; prevCol < col; prevCol++) {
        int prevRow = board[prevCol];
        if (prevRow == row || abs(prevRow - row) == abs(prevCol - col)) {
            return false;
        }
    }
    return true;
}

void PrintBoard(const vector<int> &board) {
    for (int row = 0; row < N; row++) {
        for (int col = 0; col < N; col++) {
            if (board[col] == row) {
                cout << "Q ";
            } else {
                cout << ". ";
            }
        }
        cout << endl;
    }
    cout << endl;
}

