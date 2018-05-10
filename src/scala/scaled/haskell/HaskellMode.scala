//
// Scaled Haskell Mode - a Scaled major mode for editing Haskell code
// http://github.com/scaled/haskell-mode/blob/master/LICENSE

package scaled.haskell

import scaled._
import scaled.code.Indenter
import scaled.grammar._
import scaled.code.{CodeConfig, Commenter}

@Plugin(tag="textmate-grammar")
class HaskellGrammarPlugin extends GrammarPlugin {
  import EditorConfig._
  import CodeConfig._

  override def grammars = Map("source.haskell" -> "Haskell.ndf")

  override def effacers = List(
    effacer("comment.line.dd-bar", docStyle),
    effacer("comment.line.triple-bar", docStyle),
    effacer("comment.line", commentStyle),
    effacer("comment.block.string", stringStyle),
    effacer("constant", constantStyle),
    effacer("invalid", warnStyle),
    effacer("keyword.directive", moduleStyle),
    effacer("keyword", keywordStyle),
    effacer("string", stringStyle),
    effacer("variable", variableStyle),
    effacer("entity.name.function", functionStyle),
    effacer("entity.name.type.module", moduleStyle),
    effacer("entity.name.type", typeStyle),
    effacer("support.other.module", moduleStyle),
    effacer("storage", variableStyle)
  )

  override def syntaxers = List(
    syntaxer("comment.line.triple-bar", Syntax.DocComment),
    syntaxer("comment.line.dd-bar", Syntax.DocComment),
    syntaxer("comment.line", Syntax.LineComment),
    syntaxer("constant", Syntax.OtherLiteral),
    syntaxer("string.quoted.triple", Syntax.HereDocLiteral),
    syntaxer("string", Syntax.StringLiteral)
  )
}

@Major(name="haskell",
       tags=Array("code", "project", "haskell"),
       pats=Array(".*\\.hs"),
       desc="A major mode for editing Haskell code.")
class HaskellMode (env :Env) extends GrammarCodeMode(env) {

  override def dispose () {} // nada for now

  override def langScope = "source.haskell"

  override def keymap = super.keymap.
    bind("self-insert-command", "'"); // don't auto-pair single quote

  // HACK: leave indent as-is
  override def computeIndent (row :Int) :Int = Indenter.readIndent(buffer, Loc(row, 0))

  override val commenter = new Commenter {
    override def linePrefix  = "--"
    override def blockOpen = ""
    override def blockClose = ""
    override def blockPrefix = ""
    override def docOpen   = "-- |"
    override def docPrefix   = "-- |"
  }
}
