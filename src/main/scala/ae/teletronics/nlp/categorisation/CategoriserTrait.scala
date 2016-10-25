package ae.teletronics.nlp.categorisation

import java.util.UUID

/**
  * Created by trym on 26-09-2016.
  */
trait CategoriserTrait {

  def categorise(sentence: String): List[CategoryMatch]

  def getCategories(): List[Category]

  def getCategory(id: UUID): Option[Category]

  def createCategory(name: String, entries: List[String]): Category

  def deleteCategory(id: UUID): Category

  def updateCategory(topic: Category): Category
}
