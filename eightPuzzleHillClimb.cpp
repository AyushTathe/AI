#include <iostream>
#include <iomanip>
#include <cmath>

using namespace std;

const char MOVE_UP = '1';
const char MOVE_DOWN = '2';
const char MOVE_LEFT = '3';
const char MOVE_RIGHT = '4';

void displayBoard(int** board)
{
    for(int i = 0; i < 3; i++)
    {
        for(int j = 0; j < 3; j++)
        {
            cout << setw(8) << board[i][j];
        }
        cout << endl;
    }
    cout << endl;
}

int calculateManhattanDistance(int** current_board, int** goal_board)
{
    int manhattan_distance = 0;
    for(int i = 0; i < 3; i++)
    {
        for(int j = 0; j < 3; j++)
        {
            if(current_board[i][j] > 0)
            {
                for(int k = 0; k < 3; k++)
                {
                    for(int l = 0; l < 3; l++)
                    {
                        if(goal_board[k][l] == current_board[i][j])
                        {
                            manhattan_distance += (abs(i - k) + abs(j - l));
                        }
                    }
                }
            }
        }
    }
    return manhattan_distance;
}

void moveTile(int** board, int move)
{
    int flag = 0;
    for(int i = 0; i < 3; i++)
    {
        for(int j = 0; j < 3; j++)
        {
            if(board[i][j] == 0)
            {
                if(move == 1 && i > 0)
                {
                    swap(board[i][j], board[i - 1][j]);
                    flag = 1;
                    break;
                }
                else if(move == 2 && i < 2)
                {
                    swap(board[i][j], board[i + 1][j]);
                    flag = 1;
                    break;
                }
                else if(move == 3 && j > 0)
                {
                    swap(board[i][j], board[i][j - 1]);
                    flag = 1;
                    break;
                }
                else if(move == 4 && j < 2)
                {
                    swap(board[i][j], board[i][j + 1]);
                    flag = 1;
                    break;
                }
            }
        }
        if(flag == 1) break;
    }
}

int evaluateBoard(int** current_board, int** goal_board, int move)
{
    int** temp_board = new int*[3];
    for(int i = 0; i < 3; i++)
    {
        temp_board[i] = new int[3];
        for(int j = 0; j < 3; j++)
        {
            temp_board[i][j] = current_board[i][j];
        }
    }
    moveTile(temp_board, move);
    displayBoard(temp_board);
    int manhattan = calculateManhattanDistance(temp_board, goal_board);
    cout << "Current Manhattan distance: " << manhattan << endl << endl << endl;
    for(int i = 0; i < 3; i++)
    {
        delete[] temp_board[i];
    }
    delete[] temp_board;
    return manhattan; 
}

void steepestAscentHillClimbing(int** current_board, int** goal_board, int former_move)
{
    int arr[4] = {100, 100, 100, 100};
    cout << "--------------------------------------------------------------------------------" << endl;
    for(int i = 0; i < 3; i++)
    {
        for(int j = 0; j < 3; j++)
        {
            if(current_board[i][j] == 0)
            {
                if(i > 0 && former_move != 2)
                {
                    cout << "Examining child (move 0 upwards)" << endl;
                    arr[0] = evaluateBoard(current_board, goal_board, 1);
                }
                if(i < 2 && former_move != 1)
                {
                    cout << "Examining child (move 0 downwards)" << endl;
                    arr[1] = evaluateBoard(current_board, goal_board, 2);
                }
                if(j > 0 && former_move != 4)
                {
                    cout << "Examining child (move 0 leftwards)" << endl;
                    arr[2] = evaluateBoard(current_board, goal_board, 3);
                }
                if(j < 2 && former_move != 3)
                {
                    cout << "Examining child (move 0 rightwards)" << endl;
                    arr[3] = evaluateBoard(current_board, goal_board, 4);
                }
            }
        }
        cout << endl;
    }
    int local_optimum = 99;
    int index = 0;
    for(int i = 0; i < 4; i++)
    {
        if(arr[i] < local_optimum)
        {
            local_optimum = arr[i];
            index = i + 1;
        }
    }
    moveTile(current_board, index);
    cout << "Next state = minimum Manhattan distance:" << endl;
    displayBoard(current_board);
    if(local_optimum == 0)
    {  
        cout << "Goal state reached" << endl;
        return;
    }
    else
    steepestAscentHillClimbing(current_board, goal_board, index);
}

int main()
{
    int** initial_board = new int*[3];
    for(int i = 0; i < 3; i++)
    {
        initial_board[i] = new int[3];
    }

    int** goal_board = new int*[3];
    for(int i = 0; i < 3; i++)
    {
        goal_board[i] = new int[3];
    }

    int player_input;
    cout << "Enter initial board configuration - 0 denotes empty position" << endl;
    for(int i = 0; i < 3; i++)
    {
        for(int j = 0; j < 3; j++)
        {
            cout << "Enter value at position [" << i << "][" << j << "]" << endl;
            cin >> player_input;
            initial_board[i][j] = player_input;
        }
    }
    
    cout << "--------------------------------------------------------------------------------" << endl;
    cout << "Enter final board configuration - 0 denotes empty position" << endl;
    for(int i = 0; i < 3; i++)
    {
        for(int j = 0; j < 3; j++)
        {
            cout << "Enter value at position [" << i << "][" << j << "]" << endl;
            cin >> player_input;
            goal_board[i][j] = player_input;
        }
    }

    cout << "\n---------------------------Initial Board-------------------------------\n" << endl;	
    displayBoard(initial_board);
    
    cout << "\n---------------------------Final Board---------------------------------\n" << endl;
    displayBoard(goal_board); 

    cout << "\n--------------------------------------------------------------------------------" << endl;
    cout << "\n--------------------------------------------------------------------------------" << endl;
        cout << "\nCalling Steepest Ascent Hill Climbing algorithm\n" << endl;
    steepestAscentHillClimbing(initial_board, goal_board, 0);
    for(int i = 0; i < 3; i++)
    {
        delete[] initial_board[i];
        delete[] goal_board[i];
    }
    delete[] initial_board;
    delete[] goal_board;
    return 0;
}
