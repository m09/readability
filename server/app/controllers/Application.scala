package controllers

import com.google.common.collect.TreeMultimap
import eu.crydee.readability.corpus.Corpus
import eu.crydee.readability.uima.server.DictUsageAEBuilder
import eu.crydee.readability.uima.core.model.Score
import eu.crydee.readability.uima.core.ts.Token
import eu.crydee.readability.uima.server.model.Monoids
import eu.crydee.readability.uima.server.model.Monoid
import eu.crydee.readability.uima.server.ts.RewritingsBySemiring
import eu.crydee.readability.uima.server.ts.RewritingsByScoreThenSemiring
import eu.crydee.readability.uima.server.ts.Revision
import eu.crydee.readability.uima.server.ts.TxtRevisions
import eu.crydee.readability.uima.server.ts.Revisions
import eu.crydee.readability.uima.server.ts.Rewriting
import eu.crydee.readability.uima.server.ts.RewritingSpan
import eu.crydee.readability.uima.server.ts.Rewritings
import eu.crydee.readability.uima.server.ts.Suggestion
import eu.crydee.readability.uima.server.ts.TxtSuggestion
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

object Application extends Controller {

  private val ae = DictUsageAEBuilder buildAe(Corpus.url, false)

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
      "score"  -> revision.getScore.toArray
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

  implicit val rbsWrites = new Writes[RewritingsBySemiring] {
    def writes(rbs: RewritingsBySemiring): JsValue = {
      val s = rbs.getRewritingsBySemiring.size
      JsArray(
        (0 until s) map (i => Json.toJson(
          rbs.getRewritingsBySemiring(i)))
      )
    }
  }

  implicit val rbstsWrites = new Writes[RewritingsByScoreThenSemiring] {
    def writes(rbsts: RewritingsByScoreThenSemiring): JsValue = {
      val s = rbsts.getRewritingsByScoreThenSemiring.size
      JsArray(
        (0 until s) map (i => Json.toJson(
          rbsts.getRewritingsByScoreThenSemiring(i)))
      )
    }
  }

  implicit val scoreWrites = new Writes[Score] {
    def writes(s: Score): JsValue = JsString(s.toString)
  }

  implicit val monoidWrites = new Writes[Monoid] {
    def writes(m: Monoid): JsValue = JsString(m.toString)
  }

  def annotate = Action(parse.json) { request =>
    def work(data: String) = {
      val jcas = ae.newJCas()
      jcas.setDocumentText(data)
      ae.process(jcas)
      val txtSuggs: Iterable[Suggestion] = JCasUtil.select(
        jcas,
        classOf[TxtSuggestion])
      val tokens: Iterable[Token] = JCasUtil.select(
        jcas,
        classOf[Token])
      val txtRevisions: Iterable[Revisions] = JCasUtil.select(
        jcas,
        classOf[TxtRevisions])
      val rbsts: RewritingsByScoreThenSemiring = JCasUtil.selectSingle(
        jcas,
        classOf[RewritingsByScoreThenSemiring])
      Ok(
        Json.obj(
          "scores"      -> Json.toJson(Score.values),
          "monoids"   -> Json.toJson(Monoids.values),
          "text"        -> jcas.getDocumentText,
          "tokens"      -> tokens,
          "revisions"   -> JsObject(
            txtRevisions.toSeq map (rs =>
              rs.getId -> Json.toJson(rs)
            )
          ),
          "annotations" -> Json.toJson(txtSuggs),
          "rewritings" -> Json.toJson(rbsts)
        )
      ).withHeaders(headers : _*)
    }
    request.body.validate[String].map {
      case input => work(input)
    }.recoverTotal {
      e => BadRequest("JSON parsing impossible: " + JsError.toFlatJson(e))
    }
  }

  def headers = List(
    "Access-Control-Allow-Origin" -> "*",
    "Access-Control-Allow-Methods" -> "OPTIONS, POST",
    "Access-Control-Max-Age" -> "360",
    "Access-Control-Allow-Headers" -> "Origin, Content-Type, Accept"
  )

  def options = Action { request =>
    NoContent.withHeaders(headers : _*)
  }
}
