#include <iostream>
#include <vector>
#include <unordered_map>
#include <unordered_set>

template<typename V, typename D>
class Constraint {
public:
    std::vector<V> variables;

    virtual bool satisfied(std::unordered_map<V, D>& assignment) = 0;
};

template<typename V, typename D>
class CSP {
public:
    std::vector<V> variables;
    std::unordered_map<V, std::vector<D>> domains;
    std::unordered_map<V, std::vector<Constraint<V, D>*>> constraints;

    void add_constraint(Constraint<V, D>* constraint) {
        for (V variable : constraint->variables) {
            if (domains.find(variable) == domains.end()) {
                throw std::invalid_argument("Variable in constraint not in CSP");
            }
            constraints[variable].push_back(constraint);
        }
    }

    bool consistent(V variable, std::unordered_map<V, D>& assignment) {
        for (Constraint<V, D>* constraint : constraints[variable]) {
            if (!constraint->satisfied(assignment)) {
                return false;
            }
        }
        return true;
    }

    std::unordered_map<V, D> backtracking_search(std::unordered_map<V, D>& assignment) {
        if (assignment.size() == variables.size()) {
            return assignment;
        }

        V unassigned_variable;
        for (V variable : variables) {
            if (assignment.find(variable) == assignment.end()) {
                unassigned_variable = variable;
                break;
            }
        }

        for (D value : domains[unassigned_variable]) {
            std::unordered_map<V, D> local_assignment = assignment;
            local_assignment[unassigned_variable] = value;
            if (consistent(unassigned_variable, local_assignment)) {
                std::unordered_map<V, D> result = backtracking_search(local_assignment);
                if (!result.empty()) {
                    return result;
                }
            }
        }

        return std::unordered_map<V, D>();
    }
};

class SendMoreMoneyConstraint : public Constraint<char, int> {
public:
    SendMoreMoneyConstraint(std::vector<char>& letters) {
        variables = letters;
    }

    bool satisfied(std::unordered_map<char, int>& assignment) override {
        std::unordered_set<int> unique_values;
        for (auto& pair : assignment) {
            if (unique_values.find(pair.second) != unique_values.end()) {
                return false;
            }
            unique_values.insert(pair.second);
        }

        if (assignment.size() == variables.size()) {
            int s = assignment['S'];
            int e = assignment['E'];
            int n = assignment['N'];
            int d = assignment['D'];
            int m = assignment['M'];
            int o = assignment['O'];
            int r = assignment['R'];
            int y = assignment['Y'];
            int send = s * 1000 + e * 100 + n * 10 + d;
            int more = m * 1000 + o * 100 + r * 10 + e;
            int money = m * 10000 + o * 1000 + n * 100 + e * 10 + y;
            return send + more == money;
        }

        return true;
    }
};

int main() {
    std::cout << "Cryptarithmetic problem: " << std::endl;
    std::cout << "   SEND" << std::endl;
    std::cout << " + MORE" << std::endl;
    std::cout << "-------" << std::endl;
    std::cout << " MONEY" << std::endl;
    std::vector<char> letters = {'S', 'E', 'N', 'D', 'M', 'O', 'R', 'Y'};
    std::unordered_map<char, std::vector<int>> possible_digits;
    for (char letter : letters) {
        possible_digits[letter] = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
    }
    possible_digits['M'] = {1}; 
    CSP<char, int> csp;
    csp.variables = letters;
    csp.domains = possible_digits;
    csp.add_constraint(new SendMoreMoneyConstraint(letters));

    std::unordered_map<char, int> assignment;
    assignment = csp.backtracking_search(assignment);

    if (assignment.empty()) {
        std::cout << "No solution found!" << std::endl;
    } else {
        std::cout << "Here are the results:" << std::endl;
        for (auto& pair : assignment) {
            std::cout << pair.first << ": " << pair.second << std::endl;
        }
    }

    return 0;
}
