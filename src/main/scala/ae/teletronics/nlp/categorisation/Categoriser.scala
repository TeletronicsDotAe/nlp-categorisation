package ae.teletronics.nlp.categorisation

/**
  * Created by hhravn on 31/03/16.
  */
trait Categoriser {

  def categorise(text: String): java.util.List[String]

  protected def words(text: String): List[String] = {
    text.split("\\s").toList
  }
}
