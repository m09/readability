package controllers

import eu.crydee.readability.uima.DictUsagePipeline
import eu.crydee.readability.uima.ts.Revised
import eu.crydee.readability.uima.ts.Suggestion
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

  private val aeNormal = DictUsagePipeline buildAe("file:dict.xml", false)
  private val aeFiltered = DictUsagePipeline buildAe("file:filtered.xml", false)
  private val typeTok = CasUtil.getType(aeNormal.newCAS, classOf[Token])
  private val featTokBeg = typeTok.getFeatureByBaseName("begin")
  private val featTokEnd = typeTok.getFeatureByBaseName("end")
  private val featTokPos = typeTok.getFeatureByBaseName("POS")
  private val typeRev = CasUtil.getType(aeNormal.newCAS, classOf[Revised])
  private val featRevCnt = typeRev.getFeatureByBaseName("count")
  private val featRevTxt = typeRev.getFeatureByBaseName("text")
  private val featRevTok = typeRev.getFeatureByBaseName("tokens")
  private val featRevPos = typeRev.getFeatureByBaseName("pos")

  implicit val inputReads: Reads[Input] = (
    (JsPath \ "data").read[String] and
    (JsPath \ "dict").read[String].map {
      case "filtered" => Filtered
      case _ => Normal
    }
  )(Input.apply _)

  implicit val suggestionWrites = new Writes[Suggestion] {
    def writes(suggestion: Suggestion): JsValue = {
      val arr = suggestion.getRevised.toArray
      Json.obj(
        "original"    -> Json.obj(
          "text"      -> suggestion.getOriginal.getText,
          "tokens"    -> suggestion.getOriginal.getTokens.toArray.map( fs =>
            Json.obj(
              "pos"   -> fs.getStringValue(featTokPos),
              "begin" -> fs.getIntValue   (featTokBeg),
              "end"   -> fs.getIntValue   (featTokEnd)
            )
          )
        ),
        "revised"  -> arr.map( fs =>
          Json.obj(
            "text"    -> fs.getStringValue (featRevTxt),
            "tokens"  -> fs.getFeatureValue(featRevTok).asInstanceOf[StringArray].toArray,
            "pos"     -> fs.getFeatureValue(featRevPos).asInstanceOf[StringArray].toArray,
            "count"   -> fs.getIntValue    (featRevCnt)
          )
        )
      )
    }
  }

  def index = Action {
    Ok(views.html.index())
  }

  def annotate = Action(parse.json) { request =>
    def work(data: String, ae: AnalysisEngine) = {
      val jcas = ae.newJCas()
      jcas.setDocumentText(data)
      ae.process(jcas)
        val suggestions : scala.collection.Iterable[Suggestion] = JCasUtil.select(
          jcas,
          classOf[Suggestion])
      Ok(
          Json.obj(
            "text" -> jcas.getDocumentText,
            "annotations" -> Json.toJson(suggestions)
          )
        )
    }
    request.body.validate[Input].map {
      case input => {
        input.dict match {
          case Normal => work(input.data, aeNormal)
          case Filtered => work(input.data, aeFiltered)
        }
      }
    }.recoverTotal {
      e => BadRequest("JSON parsing impossible: " + JsError.toFlatJson(e))
    }
  }
}
