package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._

/**
 * Simple app -- we'll use Application as the main controller
 * that takes form input
 */
object Application extends Controller {
  val reformatForm = Form(
    "formatText" -> nonEmptyText
  )


  def index = Action {
    Ok(views.html.index(reformatForm))
  }

  def reformat = Action { implicit request =>
    reformatForm.bindFromRequest.fold(
      errors => BadRequest(views.html.error()),
      formatText => {
        // Received the format text, now reformat it
        Ok(reformatArticle(formatText))
      }
    )
                       }



  private def reformatArticle(body: String) = {
    /* HTML forms have line breaks as \r\n, per
     * but Javascript POST requests return with \n
     * http://stackoverflow.com/questions/9456170/how-to-handle-line-breaks-in-html-forms
     */

    val paraAndHeaders = body.split("\n")

    val MAX_HEADER_LENGTH = 10
    val SENTINEL_VALUE = "<!--sentinelvalue-->"

    /* We detect a header by splitting the body on newline
     * Usually, a header is prefaced by two newlines ("\n\n"),
     * which creates a space "" on split
     * Thus, the order of the conditionals is important
     * for the sentinel value that we'll insert on two newlines */
    def wrapTag(s: String,  tag: String) : String = "<" + tag + ">" + s + "</" + tag + ">"
    val NEWLINE = "\n"
    val output = paraAndHeaders.foldLeft("")((o, line) => {
      if (line == "")
        o + SENTINEL_VALUE
      else if (o.takeRight(SENTINEL_VALUE.length) == SENTINEL_VALUE)
        o + wrapTag(line, "h2") + NEWLINE
      else if ((line.split(" ").length <= MAX_HEADER_LENGTH) && (line.split(" ").length > 1))
        o + wrapTag(line, "h2") + NEWLINE
      else
        o + wrapTag(line, "p") + NEWLINE
    })

    output.replace(SENTINEL_VALUE, "")
  }

}
