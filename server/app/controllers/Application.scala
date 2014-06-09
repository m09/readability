package controllers


import eu.crydee.readability.uima.DictUsagePipeline
import eu.crydee.readability.uima.ts.Original
import eu.crydee.readability.uima.ts.Revised
import eu.crydee.readability.uima.ts.TxtSuggestion
import eu.crydee.readability.uima.ts.PosSuggestion
import eu.crydee.readability.uima.ts.Token
import org.apache.uima.analysis_engine.AnalysisEngine
import org.apache.uima.cas.FeatureStructure
import org.apache.uima.fit.util.CasUtil
import org.apache.uima.fit.util.JCasUtil
import org.apache.uima.jcas.cas.StringArray
import org.apache.uima.jcas.JCas
import play.api._
import play.api.libs.functional.syntax._
import play.api.libs.json._
import play.api.mvc._
import scala.collection.JavaConversions._

case class Input(data: String, dict: Dict)

sealed abstract class Dict
case object Normal extends Dict
case object Filtered extends Dict

object Application extends Controller {

  private val aeN = DictUsagePipeline buildAe("file:data/fullTxt.xml", "file:data/fullPos.xml", false)
  private val aeF = DictUsagePipeline buildAe("file:data/filteredTxt.xml", "file:data/filteredPos.xml", false)
  private val tokT = CasUtil.getType(aeN.newCAS, classOf[Token])
  private val tokBegF = tokT.getFeatureByBaseName("begin")
  private val tokEndF = tokT.getFeatureByBaseName("end")
  private val tokPosF = tokT.getFeatureByBaseName("POS")
  private val revT = CasUtil.getType(aeN.newCAS, classOf[Revised])
  private val revScrF = revT.getFeatureByBaseName("score")
  private val revCntF = revT.getFeatureByBaseName("count")
  private val revTxtF = revT.getFeatureByBaseName("text")
  private val revTokF = revT.getFeatureByBaseName("tokens")
  private val revPosF = revT.getFeatureByBaseName("pos")

  implicit val inputReads: Reads[Input] = (
    (JsPath \ "data").read[String] and
    (JsPath \ "dict").read[String].map {
      case "filtered" => Filtered
      case _ => Normal
    }
  )(Input.apply _)
  
  def writeRev(rev: FeatureStructure): JsValue = Json.obj(
    "text"    -> rev.getStringValue (revTxtF),
    "tokens"  -> rev.getFeatureValue(revTokF).asInstanceOf[StringArray].toArray,
    "count"   -> rev.getIntValue    (revCntF),
    "score"   -> rev.getDoubleValue (revScrF)
  )

  def writeTxtOriginal(ori: Original): JsValue = Json.obj(
    "text"   -> ori.getText,
    "tokens" -> ori.getTokens.toArray.map( writeTxtToken )
  )

  def writePosOriginal(ori: Original): JsValue = Json.obj(
    "text"   -> ori.getText,
    "tokens" -> ori.getTokens.toArray.map( writePosToken )
  )

  def writeTxtToken(tok: FeatureStructure): JsValue = Json.obj(
    "begin" -> tok.getIntValue   (tokBegF),
    "end"   -> tok.getIntValue   (tokEndF)
  )

  def writePosToken(tok: FeatureStructure): JsValue = Json.obj(
    "pos"   -> tok.getStringValue(tokPosF),
    "begin" -> tok.getIntValue   (tokBegF),
    "end"   -> tok.getIntValue   (tokEndF)
  )

  implicit val txtSuggestionWrites = new Writes[TxtSuggestion] {
    def writes(suggestion: TxtSuggestion): JsValue = Json.obj(
      "original" -> writeTxtOriginal(suggestion.getOriginal),
      "revised"  -> suggestion.getRevised.toArray.map( writeRev )
    )
  }

  implicit val posSuggestionWrites = new Writes[PosSuggestion] {
    def writes(suggestion: PosSuggestion): JsValue = Json.obj(
      "original" -> writePosOriginal(suggestion.getOriginal),
      "revised"  -> suggestion.getRevised.toArray.map( writeRev )
    )
  }

  def annotate = Action(parse.json) { request =>
    def work(data: String, ae: AnalysisEngine) = {
      val jcas = ae.newJCas()
      jcas.setDocumentText(data)
      ae.process(jcas)
      val posSuggs : scala.collection.Iterable[PosSuggestion] = JCasUtil.select(
        jcas,
        classOf[PosSuggestion])
      val txtSuggs : scala.collection.Iterable[TxtSuggestion] = JCasUtil.select(
        jcas,
        classOf[TxtSuggestion])
      val tokens : scala.collection.Iterable[Token] = JCasUtil.select(
        jcas,
        classOf[Token])
      Ok(
        Json.obj(
          "text"        -> jcas.getDocumentText,
          "tokens"      -> tokens.map( t =>
            Json.obj(
              "text"    -> t.getCoveredText,
              "begin"   -> t.getBegin,
              "end"     -> t.getEnd,
              "pos"     -> t.getPOS
            )
          ),
          "annotations" -> Json.obj(
            "text" -> Json.toJson(txtSuggs),
            "pos"  -> Json.toJson(posSuggs)
          )
        )
      ).withHeaders(headers : _*)
    }
    request.body.validate[Input].map {
      case input => {
        input.dict match {
          case Normal => work(input.data, aeN)
          case Filtered => work(input.data, aeF)
        }
      }
    }.recoverTotal {
      e => BadRequest("JSON parsing impossible: " + JsError.toFlatJson(e))
    }
  }

  def headers = List(
    "Access-Control-Allow-Origin" -> "*",
    "Access-Control-Allow-Methods" -> "POST",
    "Access-Control-Max-Age" -> "360",
    "Access-Control-Allow-Headers" -> "Origin, Content-Type, Accept"
  )

  def options = Action { request =>
    NoContent.withHeaders(headers : _*)
  }
}
