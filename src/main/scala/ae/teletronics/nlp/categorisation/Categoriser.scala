package ae.teletronics.nlp.categorisation

/**
  * Created by hhravn on 31/03/16.
  */
trait Categoriser {
  def categorise(text: String): Array[String]

  def words(text: String): Array[String] = { text.split("\\s") }
}
