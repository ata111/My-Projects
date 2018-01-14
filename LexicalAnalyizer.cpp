#include "stdafx.h"
#include "iostream"
#include "string"
#include "fstream"
using namespace std;

bool keyword(string token)
{
	string table[] = { "int","float","double" };
	int flag = 0;
	for (int i = 0; i < 3; i++)
		if (token == table[i])
		{
			flag = 1;
			break;
		}

	return flag;
}
bool isoperator(char token)
{
	string operators[11] = { "=","==",">","<",">=","<=","+","*","-","%","/" };
	int flag = 0;
	for (int i = 0; i < 11; i++)
		if (token == '=' || token == '+' || token == '-' || token == '<')
		{
			flag = 1;
			break;
		}

	return flag;
}
bool isoperator(string token)
{

	string operators[12] = { "+=","=","==",">","<",">=","<=","+","*","-","%","/" };
	int flag = 0;
	for (int i = 0; i < 12; i++)
		if (operators[i] == token)
		{
			flag = 1;
			break;
		}
	return flag;
}

bool isdigit(string token)
{
	if (token[0] >= 48 && token[0] <= 57)
		return 1;
	else
		return 0;
}

bool isletter(char token)
{
	if ((token >= 65 && token <= 90) || (token >= 97 && token <= 122))
		return 1;
	else
		return 0;
}



bool identifer(string token)
{


	if (isoperator(token))
		return 0;

	int flag = 1; // we assum from the beginning that it's an identifer

	if (isdigit(token[0])) // identifer mustn't start with digit
		return 0;

	for (int i = 0; i < token.length(); i++)
		if (isoperator(token[i]))
		{
			flag = 0;
			break;
		}
	return flag;
}

void whatisthis(string token)
{

	ofstream ofs("C:\\New folder\\output.txt");
	if (keyword(token))
		cout << token << "\t" << "Keyword  \n";
	else if (identifer(token))
		cout << token << "\t" << "Identifer \n";
	else if (isoperator(token))
		cout << token << "\t" << "Operator \n";
	else if (isdigit(token))
		cout << token << "\t" << "Number \n";
	else
		cout << token << "\t" << "Error \n";


}

void cutToPieces(string token)
{
	//cout << " to cut " << token << endl;
	string empty = " ";
	string piece = "";
	token += ' ';

	for (int i = 0; i < token.length(); i++) {
		if (token[i] == ' ')
		{
			cout << " piece to whatisthis : " << piece << "  <-- " << endl;

			if (piece != empty)
				//whatisthis(piece);
				piece = "";

		}

		else
		{
			while (token[i] == ' ')
				i++;
			piece += token[i];


		}
	}
}


int addspaces(string &token)
{
	string before, after;
	int flag = 0;
	int startIndex = 0;
	for (int i = 0; i < token.length(); i++)
	{
		if (isoperator(token[i]) && isoperator(token[i + 1]))
		{

			before = token.substr(0, i);
			before += ' ';
			before += token[i];

			after = token[i + 1] + ' ';
			after += token.substr(i + 2, token.length());
			flag = 1;


		}
		else if (isoperator(token[i]))
		{

			before = token.substr(startIndex, i);

			startIndex = i + 1;
			before += ' ';
			before += token[i];


			after = ' ' + token.substr(i + 1, token.length());
			flag = 1;



		}


	}

	if (flag) {
		token = before + after;
		//cout << " token " << token << endl;
		cutToPieces(token);
		return 1;


	}
	else return 0;

}

void lexer()
{
	ifstream ifs("C:\\New folder\\input.txt");
	ofstream ofs("C:\\New folder\\output.txt");
	string line;
	string token;
	string text; // can be an identifer or a keyword
	string oper;
	string dig;
	int linenumber = 0;
	while (!ifs.eof())
	{
		getline(ifs, line);
		linenumber++;
		line += " "; // for the last token ( cuz it only stops when it sees a ' ' 
		for (int j = 0; j < line.length();)
		{
			
				if (isletter(line[j]) )// identifer or a keyword
				{
					while (isletter(line[j]) || isdigit(line[j]) )
					{
						text += line[j];
						j++;
					}
					if (keyword(text))
					{
						cout << text << " Keyword" << endl;
						text = "";
					}
					else if(isletter(text[0])) cout << text << " Identifer" << endl;
					text = "";
				}
				if (isoperator(line[j])) // operator += <=
					{
				
					while (isoperator(line[j]))
					{
						oper += line[j];
						
						j++;
					}
					if(isoperator(oper[0]))
						cout << oper << " operator" << endl;
					oper = "";
				
				}
				if (isdigit(line[j]))    // digit+
				{
					while (isdigit(line[j]))
					{
						dig += line[j];
						j++;
					}
					if(isletter(line[j]))
						cout << dig << " error" << endl;
					else cout << dig << " Digit" << endl;
					dig = "";

				}
				if (!isletter(line[j]) && !isdigit(line[j])  && !isoperator(line[j])) {
					j++;
					dig = "";
					oper = "";
					text = "";
	
				}





			
		} // end of for loop
	} // end of while ( end of file )
} // end of fucntion




int main()
{
	lexer();
	return 0;
}
