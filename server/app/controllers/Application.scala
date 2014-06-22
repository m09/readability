package controllers

import com.google.common.collect.TreeMultimap
import eu.crydee.readability.uima.DictUsagePipeline
import eu.crydee.readability.uima.ts.PosSuggestion
import eu.crydee.readability.uima.ts.Revision
import eu.crydee.readability.uima.ts.TxtRevisions
import eu.crydee.readability.uima.ts.PosRevisions
import eu.crydee.readability.uima.ts.Revisions
import eu.crydee.readability.uima.ts.Rewriting
import eu.crydee.readability.uima.ts.RewritingSpan
import eu.crydee.readability.uima.ts.Rewritings
import eu.crydee.readability.uima.ts.Suggestion
import eu.crydee.readability.uima.ts.Token
import eu.crydee.readability.uima.ts.TxtSuggestion
import java.util.Map.Entry
import java.util.UUID
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
import scala.collection.Iterable
import scala.collection.JavaConversions._

case class Input(data: String, dict: Dict)

sealed abstract class Dict
case object Normal extends Dict
case object Filtered extends Dict

object Application extends Controller {

  private val aeN = DictUsagePipeline buildAe(
    "file:data/fullTxt.xml",
    "file:data/fullPos.xml",
    false)
  private val aeF = DictUsagePipeline buildAe(
    "file:data/filteredTxt.xml",
    "file:data/filteredPos.xml",
    false)

  implicit val inputReads: Reads[Input] = (
    (JsPath \ "data").read[String] and
    (JsPath \ "dict").read[String].map {
      case "filtered" => Filtered
      case _ => Normal
    }
  )(Input.apply _)
  
  implicit val tokenWrites = new Writes[Token] {
    def writes(token: Token): JsValue = Json.obj(
      "pos"   -> token.getPOS(),
      "begin" -> token.getBegin(),
      "end"   -> token.getEnd()
    )
  }

  implicit val uuidWrites = new Writes[UUID] {
    def writes(uuid: UUID): JsValue = JsString(uuid.toString)
  }

  implicit val revisionWrites = new Writes[Revision] {
    def writes(revision: Revision): JsValue = Json.obj(
      "tokens" -> revision.getTokens.toArray,
      "text"   -> revision.getText,
      "count"  -> revision.getCount,
      "score"  -> revision.getScore
    )
  }

  implicit val revisionsWrites = new Writes[Revisions] {
    def writes(r: Revisions): JsValue =
      Json.toJson((0 until r.getRevisions.size) map (i => r.getRevisions(i)))
  }

  implicit val suggestionWrites = new Writes[Suggestion] {
    def writes(suggestion: Suggestion): JsValue = Json.obj(
      "begin" -> suggestion.getBegin,
      "end"   -> suggestion.getEnd,
      "revisionsId" -> suggestion.getRevisions.getId
    )
  }

  implicit val rewritingSpanWrites = new Writes[RewritingSpan] {
    def writes(r: RewritingSpan): JsValue = Json.obj(
      "begin"          -> r.getBegin,
      "end"            -> r.getEnd,
      "revisionsId"    -> r.getRevisionsId,
      "revisionsIndex" -> r.getRevisionsIndex
    )
  }

  implicit val rewritingWrites = new Writes[Rewriting] {
    def writes(r: Rewriting): JsValue = Json.obj(
      "revisions" -> Json.toJson(
        (0 until r.getRevisions.size) map (i => r.getRevisions(i))),
      "score"     -> r.getScore
    )
  }

  implicit val rewritingsWrites = new Writes[Rewritings] {
    def writes(rewritings: Rewritings): JsValue = {
      val s = rewritings.getRewritings.size
      JsArray(
        (0 until s) map (i => Json.toJson(rewritings.getRewritings(i)))
      )
    }
  }

  def annotate = Action(parse.json) { request =>
    def work(data: String, ae: AnalysisEngine) = {
      val jcas = ae.newJCas()
      jcas.setDocumentText(data)
      ae.process(jcas)
      val posSuggs: Iterable[Suggestion] = JCasUtil.select(
        jcas,
        classOf[PosSuggestion])
      val txtSuggs: Iterable[Suggestion] = JCasUtil.select(
        jcas,
        classOf[TxtSuggestion])
      val tokens: Iterable[Token] = JCasUtil.select(
        jcas,
        classOf[Token])
      val txtRevisions: Iterable[Revisions] = JCasUtil.select(
        jcas,
        classOf[TxtRevisions])
      val posRevisions: Iterable[Revisions] = JCasUtil.select(
        jcas,
        classOf[PosRevisions])
      val rewritings: Rewritings = JCasUtil.selectSingle(
        jcas,
        classOf[Rewritings])
      Ok(
        Json.obj(
          "text"        -> jcas.getDocumentText,
          "tokens"      -> tokens,
          "revisions"   -> Json.obj(
            "text" -> JsObject(
              txtRevisions.toSeq map (rs =>
                rs.getId -> Json.toJson(rs)
              )
            ),
            "pos" -> JsObject(
              posRevisions.toSeq map (rs =>
                rs.getId -> Json.toJson(rs)
              )
            )
          ),
          "annotations" -> Json.obj(
            "text" -> Json.toJson(txtSuggs),
            "pos"  -> Json.toJson(posSuggs)
          ),
          "rewritings" -> rewritings
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
