package jyscript;

%%
%class JFlexJYScriptScanner
%unicode
%line
%column
%public
%type Token

%eofval{
  return token("EOF");
%eofval}

%{
	public class Token {

		public final String token;
		public final int line;
		public final int column;
		public final Object value;

		public Token(String token, int line, int column, Object value){
			this.token = token;
			this.line = line;
			this.column = column;
			this.value = value;
		}
	}
	
	private Token token(String name){
		return token(name, null);
	}

	private Token token(String name, Object value){
		return new Token(name, yyline, yycolumn, value);
	}
	
%}

delim = [ \t\n\r]
ws = {delim}+
letter = [A-Za-z]
digit = [0-9]
id = {letter}({letter}|{digit})*
number = {digit}+(\.{digit}+)?(E[+\-]?{digit}+)?

any = .
varassign = \${id}

%%

{ws} {/*keine Aktion*/}

{number}            {return token(JYSymbols.NUMBER, Integer.valueOf(yytext()));}

\+     				{return token(JYSymbols.PLUS);}
\-     				{return token(JYSymbols.MINUS);}
\*       			{return token(JYSymbols.MULT);}
\/     	 			{return token(JYSymbols.DIV);}

\{     				{return token(JYSymbols.LPC);}
\}     				{return token(JYSymbols.RPC);}
\(     				{return token(JYSymbols.LPR);}
\)     				{return token(JYSymbols.RPR);}
