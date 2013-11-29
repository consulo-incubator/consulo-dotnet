/* The following code was generated by JFlex 1.4.3 on 30.11.13 0:39 */

package org.mustbe.consulo.csharp.lang.lexer;

import java.util.*;
import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;
import org.mustbe.consulo.csharp.lang.psi.CSharpTokens;


/**
 * This class is a scanner generated by 
 * <a href="http://www.jflex.de/">JFlex</a> 1.4.3
 * on 30.11.13 0:39 from the specification file
 * <tt>_CSharpLexer.flex</tt>
 */
class _CSharpLexer implements FlexLexer {
  /** initial size of the lookahead buffer */
  private static final int ZZ_BUFFERSIZE = 16384;

  /** lexical states */
  public static final int YYINITIAL = 0;

  /**
   * ZZ_LEXSTATE[l] is the state in the DFA for the lexical state l
   * ZZ_LEXSTATE[l+1] is the state in the DFA for the lexical state l
   *                  at the beginning of a line
   * l is of the form l = 2*k, k a non negative integer
   */
  private static final int ZZ_LEXSTATE[] = { 
     0, 0
  };

  /** 
   * Translates characters to character classes
   */
  private static final String ZZ_CMAP_PACKED = 
    "\11\1\1\3\1\13\1\0\1\3\1\5\16\1\4\0\1\3\1\0"+
    "\1\10\1\0\1\2\2\0\1\6\1\44\1\45\1\0\1\0\1\47"+
    "\1\0\1\15\1\4\1\1\11\1\1\0\1\46\4\0\1\12\4\2"+
    "\1\2\1\2\21\2\1\2\2\2\1\42\1\7\1\43\1\0\1\2"+
    "\1\0\1\30\1\33\1\31\1\27\1\14\1\11\1\22\1\2\1\20"+
    "\2\2\1\34\1\35\1\21\1\26\1\32\1\2\1\24\1\17\1\23"+
    "\1\16\1\25\1\36\1\2\1\37\1\2\1\40\1\0\1\41\1\0"+
    "\41\1\2\0\4\2\4\0\1\2\2\0\1\1\7\0\1\2\4\0"+
    "\1\2\5\0\27\2\1\0\37\2\1\0\u013f\2\31\0\162\2\4\0"+
    "\14\2\16\0\5\2\11\0\1\2\21\0\130\1\5\0\23\1\12\0"+
    "\1\2\13\0\1\2\1\0\3\2\1\0\1\2\1\0\24\2\1\0"+
    "\54\2\1\0\46\2\1\0\5\2\4\0\202\2\1\0\4\1\3\0"+
    "\105\2\1\0\46\2\2\0\2\2\6\0\20\2\41\0\46\2\2\0"+
    "\1\2\7\0\47\2\11\0\21\1\1\0\27\1\1\0\3\1\1\0"+
    "\1\1\1\0\2\1\1\0\1\1\13\0\33\2\5\0\3\2\15\0"+
    "\4\1\14\0\6\1\13\0\32\2\5\0\13\2\16\1\7\0\12\1"+
    "\4\0\2\2\1\1\143\2\1\0\1\2\10\1\1\0\6\1\2\2"+
    "\2\1\1\0\4\1\2\2\12\1\3\2\2\0\1\2\17\0\1\1"+
    "\1\2\1\1\36\2\33\1\2\0\3\2\60\0\46\2\13\1\1\2"+
    "\u014f\0\3\1\66\2\2\0\1\1\1\2\20\1\2\0\1\2\4\1"+
    "\3\0\12\2\2\1\2\0\12\1\21\0\3\1\1\0\10\2\2\0"+
    "\2\2\2\0\26\2\1\0\7\2\1\0\1\2\3\0\4\2\2\0"+
    "\1\1\1\2\7\1\2\0\2\1\2\0\3\1\11\0\1\1\4\0"+
    "\2\2\1\0\3\2\2\1\2\0\12\1\4\2\15\0\3\1\1\0"+
    "\6\2\4\0\2\2\2\0\26\2\1\0\7\2\1\0\2\2\1\0"+
    "\2\2\1\0\2\2\2\0\1\1\1\0\5\1\4\0\2\1\2\0"+
    "\3\1\13\0\4\2\1\0\1\2\7\0\14\1\3\2\14\0\3\1"+
    "\1\0\11\2\1\0\3\2\1\0\26\2\1\0\7\2\1\0\2\2"+
    "\1\0\5\2\2\0\1\1\1\2\10\1\1\0\3\1\1\0\3\1"+
    "\2\0\1\2\17\0\2\2\2\1\2\0\12\1\1\0\1\2\17\0"+
    "\3\1\1\0\10\2\2\0\2\2\2\0\26\2\1\0\7\2\1\0"+
    "\2\2\1\0\5\2\2\0\1\1\1\2\6\1\3\0\2\1\2\0"+
    "\3\1\10\0\2\1\4\0\2\2\1\0\3\2\4\0\12\1\1\0"+
    "\1\2\20\0\1\1\1\2\1\0\6\2\3\0\3\2\1\0\4\2"+
    "\3\0\2\2\1\0\1\2\1\0\2\2\3\0\2\2\3\0\3\2"+
    "\3\0\10\2\1\0\3\2\4\0\5\1\3\0\3\1\1\0\4\1"+
    "\11\0\1\1\17\0\11\1\11\0\1\2\7\0\3\1\1\0\10\2"+
    "\1\0\3\2\1\0\27\2\1\0\12\2\1\0\5\2\4\0\7\1"+
    "\1\0\3\1\1\0\4\1\7\0\2\1\11\0\2\2\4\0\12\1"+
    "\22\0\2\1\1\0\10\2\1\0\3\2\1\0\27\2\1\0\12\2"+
    "\1\0\5\2\2\0\1\1\1\2\7\1\1\0\3\1\1\0\4\1"+
    "\7\0\2\1\7\0\1\2\1\0\2\2\4\0\12\1\22\0\2\1"+
    "\1\0\10\2\1\0\3\2\1\0\27\2\1\0\20\2\4\0\6\1"+
    "\2\0\3\1\1\0\4\1\11\0\1\1\10\0\2\2\4\0\12\1"+
    "\22\0\2\1\1\0\22\2\3\0\30\2\1\0\11\2\1\0\1\2"+
    "\2\0\7\2\3\0\1\1\4\0\6\1\1\0\1\1\1\0\10\1"+
    "\22\0\2\1\15\0\60\2\1\1\2\2\7\1\4\0\10\2\10\1"+
    "\1\0\12\1\47\0\2\2\1\0\1\2\2\0\2\2\1\0\1\2"+
    "\2\0\1\2\6\0\4\2\1\0\7\2\1\0\3\2\1\0\1\2"+
    "\1\0\1\2\2\0\2\2\1\0\4\2\1\1\2\2\6\1\1\0"+
    "\2\1\1\2\2\0\5\2\1\0\1\2\1\0\6\1\2\0\12\1"+
    "\2\0\2\2\42\0\1\2\27\0\2\1\6\0\12\1\13\0\1\1"+
    "\1\0\1\1\1\0\1\1\4\0\2\1\10\2\1\0\42\2\6\0"+
    "\24\1\1\0\2\1\4\2\4\0\10\1\1\0\44\1\11\0\1\1"+
    "\71\0\42\2\1\0\5\2\1\0\2\2\1\0\7\1\3\0\4\1"+
    "\6\0\12\1\6\0\6\2\4\1\106\0\46\2\12\0\51\2\7\0"+
    "\132\2\5\0\104\2\5\0\122\2\6\0\7\2\1\0\77\2\1\0"+
    "\1\2\1\0\4\2\2\0\7\2\1\0\1\2\1\0\4\2\2\0"+
    "\47\2\1\0\1\2\1\0\4\2\2\0\37\2\1\0\1\2\1\0"+
    "\4\2\2\0\7\2\1\0\1\2\1\0\4\2\2\0\7\2\1\0"+
    "\7\2\1\0\27\2\1\0\37\2\1\0\1\2\1\0\4\2\2\0"+
    "\7\2\1\0\47\2\1\0\23\2\16\0\11\1\56\0\125\2\14\0"+
    "\u026c\2\2\0\10\2\12\0\32\2\5\0\113\2\3\0\3\2\17\0"+
    "\15\2\1\0\4\2\3\1\13\0\22\2\3\1\13\0\22\2\2\1"+
    "\14\0\15\2\1\0\3\2\1\0\2\1\14\0\64\2\40\1\3\0"+
    "\1\2\3\0\2\2\1\1\2\0\12\1\41\0\3\1\2\0\12\1"+
    "\6\0\130\2\10\0\51\2\1\1\126\0\35\2\3\0\14\1\4\0"+
    "\14\1\12\0\12\1\36\2\2\0\5\2\u038b\0\154\2\224\0\234\2"+
    "\4\0\132\2\6\0\26\2\2\0\6\2\2\0\46\2\2\0\6\2"+
    "\2\0\10\2\1\0\1\2\1\0\1\2\1\0\1\2\1\0\37\2"+
    "\2\0\65\2\1\0\7\2\1\0\1\2\3\0\3\2\1\0\7\2"+
    "\3\0\4\2\2\0\6\2\4\0\15\2\5\0\3\2\1\0\7\2"+
    "\17\0\4\1\32\0\5\1\20\0\2\2\23\0\1\2\13\0\4\1"+
    "\6\0\6\1\1\0\1\2\15\0\1\2\40\0\22\2\36\0\15\1"+
    "\4\0\1\1\3\0\6\1\27\0\1\2\4\0\1\2\2\0\12\2"+
    "\1\0\1\2\3\0\5\2\6\0\1\2\1\0\1\2\1\0\1\2"+
    "\1\0\4\2\1\0\3\2\1\0\7\2\3\0\3\2\5\0\5\2"+
    "\26\0\44\2\u0e81\0\3\2\31\0\11\2\6\1\1\0\5\2\2\0"+
    "\5\2\4\0\126\2\2\0\2\1\2\0\3\2\1\0\137\2\5\0"+
    "\50\2\4\0\136\2\21\0\30\2\70\0\20\2\u0200\0\u19b6\2\112\0"+
    "\u51a6\2\132\0\u048d\2\u0773\0\u2ba4\2\u215c\0\u012e\2\2\0\73\2\225\0"+
    "\7\2\14\0\5\2\5\0\1\2\1\1\12\2\1\0\15\2\1\0"+
    "\5\2\1\0\1\2\1\0\2\2\1\0\2\2\1\0\154\2\41\0"+
    "\u016b\2\22\0\100\2\2\0\66\2\50\0\15\2\3\0\20\1\20\0"+
    "\4\1\17\0\2\2\30\0\3\2\31\0\1\2\6\0\5\2\1\0"+
    "\207\2\2\0\1\1\4\0\1\2\13\0\12\1\7\0\32\2\4\0"+
    "\1\2\1\0\32\2\12\0\132\2\3\0\6\2\2\0\6\2\2\0"+
    "\6\2\2\0\3\2\3\0\2\2\3\0\2\2\22\0\3\1\4\0";

  /** 
   * Translates characters to character classes
   */
  private static final char [] ZZ_CMAP = zzUnpackCMap(ZZ_CMAP_PACKED);

  /** 
   * Translates DFA states to action switch labels.
   */
  private static final int [] ZZ_ACTION = zzUnpackAction();

  private static final String ZZ_ACTION_PACKED_0 =
    "\1\0\1\1\1\2\1\3\1\1\1\4\1\5\1\1"+
    "\1\2\1\6\11\2\1\7\1\10\1\11\1\12\1\13"+
    "\1\14\1\15\1\16\1\17\2\4\2\5\1\20\14\2"+
    "\2\20\5\2\1\21\1\22\6\2\1\23\10\2\1\24"+
    "\3\2\1\25\1\26\7\2\1\27\1\2\1\30\1\31"+
    "\1\32\2\2\1\33\1\2\1\34\5\2\1\35\1\36"+
    "\1\37";

  private static int [] zzUnpackAction() {
    int [] result = new int[100];
    int offset = 0;
    offset = zzUnpackAction(ZZ_ACTION_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackAction(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }


  /** 
   * Translates a state to a row index in the transition table
   */
  private static final int [] ZZ_ROWMAP = zzUnpackRowMap();

  private static final String ZZ_ROWMAP_PACKED_0 =
    "\0\0\0\50\0\120\0\170\0\240\0\310\0\360\0\u0118"+
    "\0\u0140\0\50\0\u0168\0\u0190\0\u01b8\0\u01e0\0\u0208\0\u0230"+
    "\0\u0258\0\u0280\0\u02a8\0\50\0\50\0\50\0\50\0\50"+
    "\0\50\0\50\0\50\0\u02d0\0\50\0\u02f8\0\u0320\0\50"+
    "\0\u0348\0\u0370\0\u0398\0\u03c0\0\u03e8\0\u0410\0\u0438\0\u0460"+
    "\0\u0488\0\u04b0\0\u04d8\0\u0500\0\u0528\0\u0550\0\50\0\u0578"+
    "\0\u05a0\0\u05c8\0\u05f0\0\u0618\0\u0640\0\120\0\u0668\0\u0690"+
    "\0\u06b8\0\u06e0\0\u0708\0\u0730\0\120\0\u0758\0\u0780\0\u07a8"+
    "\0\u07d0\0\u07f8\0\u0820\0\u0848\0\u0870\0\120\0\u0898\0\u08c0"+
    "\0\u08e8\0\120\0\120\0\u0910\0\u0938\0\u0960\0\u0988\0\u09b0"+
    "\0\u09d8\0\u0a00\0\120\0\u0a28\0\120\0\120\0\120\0\u0a50"+
    "\0\u0a78\0\120\0\u0aa0\0\120\0\u0ac8\0\u0af0\0\u0b18\0\u0b40"+
    "\0\u0b68\0\120\0\120\0\120";

  private static int [] zzUnpackRowMap() {
    int [] result = new int[100];
    int offset = 0;
    offset = zzUnpackRowMap(ZZ_ROWMAP_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackRowMap(String packed, int offset, int [] result) {
    int i = 0;  /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int high = packed.charAt(i++) << 16;
      result[j++] = high | packed.charAt(i++);
    }
    return j;
  }

  /** 
   * The transition table of the DFA
   */
  private static final int [] ZZ_TRANS = zzUnpackTrans();

  private static final String ZZ_TRANS_PACKED_0 =
    "\2\2\1\3\1\4\1\5\1\4\1\6\1\2\1\7"+
    "\1\3\1\10\1\4\1\11\1\12\1\13\1\14\1\15"+
    "\1\16\1\3\1\17\1\3\1\20\1\3\1\21\1\3"+
    "\1\22\1\23\5\3\1\24\1\25\1\26\1\27\1\30"+
    "\1\31\1\32\1\33\51\0\2\3\6\0\1\3\2\0"+
    "\1\3\1\0\22\3\13\0\1\4\1\0\1\4\5\0"+
    "\1\4\40\0\1\34\43\0\5\6\1\0\1\35\1\36"+
    "\3\6\1\0\34\6\5\7\1\0\1\7\1\37\1\40"+
    "\2\7\1\0\34\7\10\0\1\41\40\0\2\3\6\0"+
    "\1\3\2\0\1\3\1\0\3\3\1\42\3\3\1\43"+
    "\12\3\11\0\2\3\6\0\1\3\2\0\1\3\1\0"+
    "\1\3\1\44\20\3\11\0\2\3\6\0\1\3\2\0"+
    "\1\3\1\0\5\3\1\45\14\3\11\0\2\3\6\0"+
    "\1\3\2\0\1\3\1\0\3\3\1\46\16\3\11\0"+
    "\2\3\6\0\1\3\2\0\1\47\1\0\12\3\1\50"+
    "\7\3\11\0\2\3\6\0\1\3\2\0\1\3\1\0"+
    "\21\3\1\51\11\0\2\3\6\0\1\3\2\0\1\3"+
    "\1\0\10\3\1\52\11\3\11\0\2\3\6\0\1\3"+
    "\2\0\1\53\1\0\22\3\11\0\2\3\6\0\1\3"+
    "\2\0\1\3\1\0\16\3\1\54\3\3\11\0\2\3"+
    "\6\0\1\3\2\0\1\3\1\0\1\55\21\3\10\0"+
    "\5\34\1\0\5\34\1\0\34\34\5\6\1\0\5\6"+
    "\1\0\34\6\5\7\1\0\5\7\1\0\34\7\5\41"+
    "\1\0\1\41\1\56\1\57\37\41\1\0\2\3\6\0"+
    "\1\3\2\0\1\3\1\0\1\60\21\3\11\0\2\3"+
    "\6\0\1\3\2\0\1\61\1\0\22\3\11\0\2\3"+
    "\6\0\1\3\2\0\1\3\1\0\2\3\1\62\17\3"+
    "\11\0\2\3\6\0\1\3\2\0\1\3\1\0\6\3"+
    "\1\63\3\3\1\64\7\3\11\0\2\3\6\0\1\3"+
    "\2\0\1\3\1\0\5\3\1\65\14\3\11\0\2\3"+
    "\6\0\1\3\2\0\1\3\1\0\20\3\1\66\1\3"+
    "\11\0\2\3\6\0\1\3\2\0\1\3\1\0\17\3"+
    "\1\67\2\3\11\0\2\3\6\0\1\3\2\0\1\3"+
    "\1\0\14\3\1\70\5\3\11\0\2\3\6\0\1\3"+
    "\2\0\1\3\1\0\2\3\1\71\17\3\11\0\2\3"+
    "\6\0\1\3\2\0\1\3\1\0\16\3\1\72\3\3"+
    "\11\0\2\3\6\0\1\3\2\0\1\3\1\0\12\3"+
    "\1\73\7\3\11\0\2\3\6\0\1\3\2\0\1\3"+
    "\1\0\15\3\1\74\4\3\10\0\5\41\1\0\5\41"+
    "\1\0\34\41\1\0\2\3\6\0\1\3\2\0\1\3"+
    "\1\0\17\3\1\75\2\3\11\0\2\3\6\0\1\3"+
    "\2\0\1\3\1\0\3\3\1\76\16\3\11\0\2\3"+
    "\6\0\1\3\2\0\1\3\1\0\3\3\1\77\16\3"+
    "\11\0\2\3\6\0\1\3\2\0\1\3\1\0\1\100"+
    "\1\3\1\101\17\3\11\0\2\3\6\0\1\3\2\0"+
    "\1\3\1\0\5\3\1\102\14\3\11\0\2\3\6\0"+
    "\1\3\2\0\1\103\1\0\22\3\11\0\2\3\6\0"+
    "\1\3\2\0\1\104\1\0\22\3\11\0\2\3\6\0"+
    "\1\3\2\0\1\105\1\0\22\3\11\0\2\3\6\0"+
    "\1\3\2\0\1\3\1\0\11\3\1\106\10\3\11\0"+
    "\2\3\6\0\1\3\2\0\1\107\1\0\22\3\11\0"+
    "\2\3\6\0\1\3\2\0\1\3\1\0\1\3\1\110"+
    "\20\3\11\0\2\3\6\0\1\3\2\0\1\3\1\0"+
    "\16\3\1\111\3\3\11\0\2\3\6\0\1\3\2\0"+
    "\1\3\1\0\5\3\1\112\14\3\11\0\2\3\6\0"+
    "\1\3\2\0\1\3\1\0\4\3\1\113\15\3\11\0"+
    "\2\3\6\0\1\3\2\0\1\3\1\0\13\3\1\114"+
    "\6\3\11\0\2\3\6\0\1\3\2\0\1\3\1\0"+
    "\3\3\1\115\16\3\11\0\2\3\6\0\1\3\2\0"+
    "\1\3\1\0\2\3\1\116\17\3\11\0\2\3\6\0"+
    "\1\3\2\0\1\3\1\0\6\3\1\117\13\3\11\0"+
    "\2\3\6\0\1\3\2\0\1\3\1\0\1\3\1\120"+
    "\20\3\11\0\2\3\6\0\1\3\2\0\1\3\1\0"+
    "\10\3\1\121\11\3\11\0\2\3\6\0\1\3\2\0"+
    "\1\3\1\0\4\3\1\122\15\3\11\0\2\3\6\0"+
    "\1\3\2\0\1\3\1\0\1\3\1\123\20\3\11\0"+
    "\2\3\6\0\1\3\2\0\1\3\1\0\2\3\1\124"+
    "\17\3\11\0\2\3\6\0\1\3\2\0\1\3\1\0"+
    "\5\3\1\125\14\3\11\0\2\3\6\0\1\3\2\0"+
    "\1\3\1\0\4\3\1\126\15\3\11\0\2\3\6\0"+
    "\1\3\2\0\1\3\1\0\13\3\1\127\6\3\11\0"+
    "\2\3\6\0\1\130\2\0\1\3\1\0\22\3\11\0"+
    "\2\3\6\0\1\3\2\0\1\3\1\0\14\3\1\131"+
    "\5\3\11\0\2\3\6\0\1\132\2\0\1\3\1\0"+
    "\22\3\11\0\2\3\6\0\1\3\2\0\1\3\1\0"+
    "\12\3\1\133\7\3\11\0\2\3\6\0\1\3\2\0"+
    "\1\3\1\0\13\3\1\134\6\3\11\0\2\3\6\0"+
    "\1\3\2\0\1\3\1\0\12\3\1\135\7\3\11\0"+
    "\2\3\6\0\1\3\2\0\1\3\1\0\12\3\1\136"+
    "\7\3\11\0\2\3\6\0\1\3\2\0\1\3\1\0"+
    "\5\3\1\137\14\3\11\0\2\3\6\0\1\3\2\0"+
    "\1\3\1\0\13\3\1\140\6\3\11\0\2\3\6\0"+
    "\1\3\2\0\1\3\1\0\13\3\1\141\6\3\11\0"+
    "\2\3\6\0\1\3\2\0\1\142\1\0\22\3\11\0"+
    "\2\3\6\0\1\3\2\0\1\143\1\0\22\3\11\0"+
    "\2\3\6\0\1\3\2\0\1\144\1\0\22\3\10\0";

  private static int [] zzUnpackTrans() {
    int [] result = new int[2960];
    int offset = 0;
    offset = zzUnpackTrans(ZZ_TRANS_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackTrans(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      value--;
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }


  /* error codes */
  private static final int ZZ_UNKNOWN_ERROR = 0;
  private static final int ZZ_NO_MATCH = 1;
  private static final int ZZ_PUSHBACK_2BIG = 2;
  private static final char[] EMPTY_BUFFER = new char[0];
  private static final int YYEOF = -1;
  private static java.io.Reader zzReader = null; // Fake

  /* error messages for the codes above */
  private static final String ZZ_ERROR_MSG[] = {
    "Unkown internal scanner error",
    "Error: could not match input",
    "Error: pushback value was too large"
  };

  /**
   * ZZ_ATTRIBUTE[aState] contains the attributes of state <code>aState</code>
   */
  private static final int [] ZZ_ATTRIBUTE = zzUnpackAttribute();

  private static final String ZZ_ATTRIBUTE_PACKED_0 =
    "\1\0\1\11\7\1\1\11\11\1\10\11\1\1\1\11"+
    "\2\1\1\11\16\1\1\11\65\1";

  private static int [] zzUnpackAttribute() {
    int [] result = new int[100];
    int offset = 0;
    offset = zzUnpackAttribute(ZZ_ATTRIBUTE_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackAttribute(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }

  /** the current state of the DFA */
  private int zzState;

  /** the current lexical state */
  private int zzLexicalState = YYINITIAL;

  /** this buffer contains the current text to be matched and is
      the source of the yytext() string */
  private CharSequence zzBuffer = "";

  /** this buffer may contains the current text array to be matched when it is cheap to acquire it */
  private char[] zzBufferArray;

  /** the textposition at the last accepting state */
  private int zzMarkedPos;

  /** the textposition at the last state to be included in yytext */
  private int zzPushbackPos;

  /** the current text position in the buffer */
  private int zzCurrentPos;

  /** startRead marks the beginning of the yytext() string in the buffer */
  private int zzStartRead;

  /** endRead marks the last character in the buffer, that has been read
      from input */
  private int zzEndRead;

  /**
   * zzAtBOL == true <=> the scanner is currently at the beginning of a line
   */
  private boolean zzAtBOL = true;

  /** zzAtEOF == true <=> the scanner is at the EOF */
  private boolean zzAtEOF;

  /** denotes if the user-EOF-code has already been executed */
  private boolean zzEOFDone;


  _CSharpLexer(java.io.Reader in) {
    this.zzReader = in;
  }

  /**
   * Creates a new scanner.
   * There is also java.io.Reader version of this constructor.
   *
   * @param   in  the java.io.Inputstream to read input from.
   */
  _CSharpLexer(java.io.InputStream in) {
    this(new java.io.InputStreamReader(in));
  }

  /** 
   * Unpacks the compressed character translation table.
   *
   * @param packed   the packed character translation table
   * @return         the unpacked character translation table
   */
  private static char [] zzUnpackCMap(String packed) {
    char [] map = new char[0x10000];
    int i = 0;  /* index in packed string  */
    int j = 0;  /* index in unpacked array */
    while (i < 1760) {
      int  count = packed.charAt(i++);
      char value = packed.charAt(i++);
      do map[j++] = value; while (--count > 0);
    }
    return map;
  }

  public final int getTokenStart(){
    return zzStartRead;
  }

  public final int getTokenEnd(){
    return getTokenStart() + yylength();
  }

  public void reset(CharSequence buffer, int start, int end,int initialState){
    zzBuffer = buffer;
    zzBufferArray = com.intellij.util.text.CharArrayUtil.fromSequenceWithoutCopying(buffer);
    zzCurrentPos = zzMarkedPos = zzStartRead = start;
    zzPushbackPos = 0;
    zzAtEOF  = false;
    zzAtBOL = true;
    zzEndRead = end;
    yybegin(initialState);
  }

  /**
   * Refills the input buffer.
   *
   * @return      <code>false</code>, iff there was new input.
   *
   * @exception   java.io.IOException  if any I/O-Error occurs
   */
  private boolean zzRefill() throws java.io.IOException {
    return true;
  }


  /**
   * Returns the current lexical state.
   */
  public final int yystate() {
    return zzLexicalState;
  }


  /**
   * Enters a new lexical state
   *
   * @param newState the new lexical state
   */
  public final void yybegin(int newState) {
    zzLexicalState = newState;
  }


  /**
   * Returns the text matched by the current regular expression.
   */
  public final CharSequence yytext() {
    return zzBuffer.subSequence(zzStartRead, zzMarkedPos);
  }


  /**
   * Returns the character at position <tt>pos</tt> from the
   * matched text.
   *
   * It is equivalent to yytext().charAt(pos), but faster
   *
   * @param pos the position of the character to fetch.
   *            A value from 0 to yylength()-1.
   *
   * @return the character at position pos
   */
  public final char yycharat(int pos) {
    return zzBufferArray != null ? zzBufferArray[zzStartRead+pos]:zzBuffer.charAt(zzStartRead+pos);
  }


  /**
   * Returns the length of the matched text region.
   */
  public final int yylength() {
    return zzMarkedPos-zzStartRead;
  }


  /**
   * Reports an error that occured while scanning.
   *
   * In a wellformed scanner (no or only correct usage of
   * yypushback(int) and a match-all fallback rule) this method
   * will only be called with things that "Can't Possibly Happen".
   * If this method is called, something is seriously wrong
   * (e.g. a JFlex bug producing a faulty scanner etc.).
   *
   * Usual syntax/scanner level error handling should be done
   * in error fallback rules.
   *
   * @param   errorCode  the code of the errormessage to display
   */
  private void zzScanError(int errorCode) {
    String message;
    try {
      message = ZZ_ERROR_MSG[errorCode];
    }
    catch (ArrayIndexOutOfBoundsException e) {
      message = ZZ_ERROR_MSG[ZZ_UNKNOWN_ERROR];
    }

    throw new Error(message);
  }


  /**
   * Pushes the specified amount of characters back into the input stream.
   *
   * They will be read again by then next call of the scanning method
   *
   * @param number  the number of characters to be read again.
   *                This number must not be greater than yylength()!
   */
  public void yypushback(int number)  {
    if ( number > yylength() )
      zzScanError(ZZ_PUSHBACK_2BIG);

    zzMarkedPos -= number;
  }


  /**
   * Contains user EOF-code, which will be executed exactly once,
   * when the end of file is reached
   */
  private void zzDoEOF() {
    if (!zzEOFDone) {
      zzEOFDone = true;
    
    }
  }


  /**
   * Resumes scanning until the next regular expression is matched,
   * the end of input is encountered or an I/O-Error occurs.
   *
   * @return      the next token
   * @exception   java.io.IOException  if any I/O-Error occurs
   */
  public IElementType advance() throws java.io.IOException {
    int zzInput;
    int zzAction;

    // cached fields:
    int zzCurrentPosL;
    int zzMarkedPosL;
    int zzEndReadL = zzEndRead;
    CharSequence zzBufferL = zzBuffer;
    char[] zzBufferArrayL = zzBufferArray;
    char [] zzCMapL = ZZ_CMAP;

    int [] zzTransL = ZZ_TRANS;
    int [] zzRowMapL = ZZ_ROWMAP;
    int [] zzAttrL = ZZ_ATTRIBUTE;

    while (true) {
      zzMarkedPosL = zzMarkedPos;

      zzAction = -1;

      zzCurrentPosL = zzCurrentPos = zzStartRead = zzMarkedPosL;

      zzState = ZZ_LEXSTATE[zzLexicalState];


      zzForAction: {
        while (true) {

          if (zzCurrentPosL < zzEndReadL)
            zzInput = zzBufferL.charAt(zzCurrentPosL++);
          else if (zzAtEOF) {
            zzInput = YYEOF;
            break zzForAction;
          }
          else {
            // store back cached positions
            zzCurrentPos  = zzCurrentPosL;
            zzMarkedPos   = zzMarkedPosL;
            boolean eof = zzRefill();
            // get translated positions and possibly new buffer
            zzCurrentPosL  = zzCurrentPos;
            zzMarkedPosL   = zzMarkedPos;
            zzBufferL      = zzBuffer;
            zzEndReadL     = zzEndRead;
            if (eof) {
              zzInput = YYEOF;
              break zzForAction;
            }
            else {
              zzInput = zzBufferL.charAt(zzCurrentPosL++);
            }
          }
          int zzNext = zzTransL[ zzRowMapL[zzState] + zzCMapL[zzInput] ];
          if (zzNext == -1) break zzForAction;
          zzState = zzNext;

          int zzAttributes = zzAttrL[zzState];
          if ( (zzAttributes & 1) == 1 ) {
            zzAction = zzState;
            zzMarkedPosL = zzCurrentPosL;
            if ( (zzAttributes & 8) == 8 ) break zzForAction;
          }

        }
      }

      // store back cached position
      zzMarkedPos = zzMarkedPosL;

      switch (zzAction < 0 ? zzAction : ZZ_ACTION[zzAction]) {
        case 28: 
          { return CSharpTokens.PUBLIC_KEYWORD;
          }
        case 32: break;
        case 23: 
          { return CSharpTokens.CLASS_KEYWORD;
          }
        case 33: break;
        case 16: 
          { return CSharpTokens.VERBATIM_STRING_LITERAL;
          }
        case 34: break;
        case 8: 
          { return CSharpTokens.RBRACE;
          }
        case 35: break;
        case 11: 
          { return CSharpTokens.LPAR;
          }
        case 36: break;
        case 1: 
          { return CSharpTokens.BAD_CHARACTER;
          }
        case 37: break;
        case 10: 
          { return CSharpTokens.RBRACKET;
          }
        case 38: break;
        case 2: 
          { return CSharpTokens.IDENTIFIER;
          }
        case 39: break;
        case 20: 
          { return CSharpTokens.VOID_KEYWORD;
          }
        case 40: break;
        case 5: 
          { return CSharpTokens.STRING_LITERAL;
          }
        case 41: break;
        case 27: 
          { return CSharpTokens.TYPEOF_KEYWORD;
          }
        case 42: break;
        case 18: 
          { return CSharpTokens.NEW_KEYWORD;
          }
        case 43: break;
        case 14: 
          { return CSharpTokens.COMMA;
          }
        case 44: break;
        case 17: 
          { return CSharpTokens.INT_KEYWORD;
          }
        case 45: break;
        case 25: 
          { return CSharpTokens.STRING_KEYWORD;
          }
        case 46: break;
        case 19: 
          { return CSharpTokens.ENUM_KEYWORD;
          }
        case 47: break;
        case 7: 
          { return CSharpTokens.LBRACE;
          }
        case 48: break;
        case 4: 
          { return CSharpTokens.CHARACTER_LITERAL;
          }
        case 49: break;
        case 29: 
          { return CSharpTokens.DELEGATE_KEYWORD;
          }
        case 50: break;
        case 3: 
          { return CSharpTokens.WHITE_SPACE;
          }
        case 51: break;
        case 6: 
          { return CSharpTokens.DOT;
          }
        case 52: break;
        case 15: 
          { return CSharpTokens.LINE_COMMENT;
          }
        case 53: break;
        case 22: 
          { return CSharpTokens.USING_KEYWORD;
          }
        case 54: break;
        case 26: 
          { return CSharpTokens.STATIC_KEYWORD;
          }
        case 55: break;
        case 24: 
          { return CSharpTokens.STRUCT_KEYWORD;
          }
        case 56: break;
        case 31: 
          { return CSharpTokens.NAMESPACE_KEYWORD;
          }
        case 57: break;
        case 30: 
          { return CSharpTokens.INTERFACE_KEYWORD;
          }
        case 58: break;
        case 9: 
          { return CSharpTokens.LBRACKET;
          }
        case 59: break;
        case 13: 
          { return CSharpTokens.SEMICOLON;
          }
        case 60: break;
        case 21: 
          { return CSharpTokens.EVENT_KEYWORD;
          }
        case 61: break;
        case 12: 
          { return CSharpTokens.RPAR;
          }
        case 62: break;
        default:
          if (zzInput == YYEOF && zzStartRead == zzCurrentPos) {
            zzAtEOF = true;
            zzDoEOF();
            return null;
          }
          else {
            zzScanError(ZZ_NO_MATCH);
          }
      }
    }
  }


}
