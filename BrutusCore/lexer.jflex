package com.brutus.parser;
import java_cup.runtime.ComplexSymbolFactory;
import java_cup.runtime.ComplexSymbolFactory.Location;
import java_cup.runtime.Symbol;
import java.lang.*;
import java.io.InputStreamReader;

%%

%class Lexer
%implements sym
%public
%unicode
%line
%column
%cup
%char
%{
	

    public Lexer(ComplexSymbolFactory sf, java.io.InputStream is){
		this(is);
        symbolFactory = sf;
    }
	public Lexer(ComplexSymbolFactory sf, java.io.Reader reader){
		this(reader);
        symbolFactory = sf;
    }
    
    private StringBuffer sb;
    private ComplexSymbolFactory symbolFactory;
    private int csline,cscolumn;

    public Symbol symbol(String name, int code){
		return symbolFactory.newSymbol(name, code,
						new Location(yyline+1,yycolumn+1, yychar), // -yylength()
						new Location(yyline+1,yycolumn+yylength(), yychar+yylength())
				);
    }
    public Symbol symbol(String name, int code, String lexem){
	return symbolFactory.newSymbol(name, code, 
						new Location(yyline+1, yycolumn +1, yychar), 
						new Location(yyline+1,yycolumn+yylength(), yychar+yylength()), lexem);
    }
    
    protected void emit_warning(String message){
    	System.out.println("scanner warning: " + message + " at : 2 "+ 
    			(yyline+1) + " " + (yycolumn+1) + " " + yychar);
    }
    
    protected void emit_error(String message){
    	System.out.println("scanner error: " + message + " at : 2" + 
    			(yyline+1) + " " + (yycolumn+1) + " " + yychar);
    }
%}
If = [Ii][Ff] | [Ss][Ee]
Then = [Tt]hen | THEN | allora | quindi 
Or = [Oo][Rr] | [Oo]
And = [Aa][Nn][Dd] | & | && | [Ee]
Newline    = \r | \n | \r\n
Whitespace = [ \t\f] | {Newline}
Var = \"[A-Za-z ][A-Za-z0-9 ]*\"
Number =  ([1-9][0-9]*|0)
Bool = [Tt]rue|[Ff]alse
double = (([0-9]+\.[0-9]*) | ([0-9]*\.[0-9]+)) (e|E('+'|'-')?[0-9]+)?
/* comments */
Comment = {TraditionalComment} | {EndOfLineComment}
TraditionalComment = "/*" {CommentContent} \*+ "/"
EndOfLineComment = "//" [^\r\n]* {Newline}
CommentContent = ( [^*] | \*+[^*/] )*

ident = ([:jletter:] | "_" ) ([:jletterdigit:] | [:jletter:] | "_" )*


%eofval{
    return symbolFactory.newSymbol("EOF",sym.EOF);
%eofval}

%state CODESEG

%%  

<YYINITIAL> {

  {Whitespace} {                              }
  {And}          { return symbolFactory.newSymbol("AND", AND); }
  
  {Or} 
  	| 
   "|"
    | 
   "||"          { return symbolFactory.newSymbol("OR", OR); }
   
  "="          { return symbolFactory.newSymbol("EQ", EQ); }
  {If} 		   { return symbolFactory.newSymbol("IF", IF); }
  {Then}       { return symbolFactory.newSymbol("THEN", THEN); }
  ";"          { return symbolFactory.newSymbol("PV", PV); }
  "+"          { return symbolFactory.newSymbol("PIU", PIU); }
  "-"          { return symbolFactory.newSymbol("MENO", MENO); }
  "*"          { return symbolFactory.newSymbol("MULT", MULT); }
  "n"          { return symbolFactory.newSymbol("N", N); }
  "("          { return symbolFactory.newSymbol("LPAREN", LPAREN); }
  ")"          { return symbolFactory.newSymbol("RPAREN", RPAREN); }
  "<"          { return symbolFactory.newSymbol("MIN", MIN); }
  ">"          { return symbolFactory.newSymbol("MAG", MAG); }
  "<="         { return symbolFactory.newSymbol("MINUG", MINUG); }
  ">="         { return symbolFactory.newSymbol("MAGUG", MAGUG); }
  "=="         { return symbolFactory.newSymbol("UGUG", UGUG); }
  "{"         { return symbolFactory.newSymbol("GL", GL); }
  "}"         { return symbolFactory.newSymbol("GR", GR); }
   {Bool}     { return symbolFactory.newSymbol("BOOL", BOOL, yytext() ); }
  
  {double}
  	 |
  {Number}     { return symbolFactory.newSymbol("NUMBER", NUMBER, Double.parseDouble(yytext())); }
   
   {Var}       { return symbolFactory.newSymbol("VAR", VAR, new String(yytext())); }
}



// error fallback
.|\n          { emit_warning("Unrecognized character '" +yytext()+"' -- ignored"); }
