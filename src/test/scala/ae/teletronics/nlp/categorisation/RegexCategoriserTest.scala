package ae.teletronics.nlp.categorisation

/**
  * Created by hhravn on 12/04/16.
  */

import java.io.File
import java.nio.file.{Files, Paths}
import java.util
import java.util.UUID
import scala.collection.JavaConversions._
import ae.teletronics.nlp.categorisation.storage.TopicStore
import org.hamcrest.Matchers._
import org.junit.Assert.assertThat
import org.junit._

import scala.collection.JavaConversions._

@Test
class RegexCategoriserTest {

  private val storageFile = "target/storage-test.db"
  private val dummyUUID = new UUID(1L, 1L)

  @Before
  def beforeTest(): Unit = {
    topicStore().clearAll()
  }

  @After
  def afterTest(): Unit = {
    topicStore().clearAll()
  }

  @Test
  def testExactMatch() = {
    val cat = Category("drugs", dummyUUID, List("/cocaine"))
    val subj = new Categoriser(seededTopicStore(List(cat)))
    val result = subj.categorise("cocaine")

    assertThat(categoryNames(result), contains(cat.name))
  }

  @Test
  def testRegexMatch() = {
    val cat = Category("drugs", dummyUUID, List("/coc.ine"))
    val subj = new Categoriser(seededTopicStore(List(cat)))
    val result = subj.categorise("cocaine")

    assertThat(categoryNames(result), contains(cat.name))
  }

  @Test
  def testRegex2Match() = {
    val cat = Category("drugs", dummyUUID, List("/[cCkK]{1}o[ck]{1}ain(e)?"))
    val subj = new Categoriser(seededTopicStore(List(cat)))
    val result = subj.categorise("kokaine")

    assertThat(categoryNames(result), contains(cat.name))
  }

  @Test
  def testRegexDeepMatchPositive() = {
    val cat = Category("drugs", dummyUUID, List("/.*[cCkK]{1}o[ck]{1}ain(e)?.*"))
    val subj = new Categoriser(seededTopicStore(List(cat)))
    val result = subj.categorise("superkokainehest")

    assertThat(categoryNames(result), contains(cat.name))
  }

  @Test
  def testRegexDeepMatchNegative() = {
    val cat = Category("drugs", dummyUUID, List("/.*[cCkK]{1}o[ck]{1}ain(e)?.*"))
    val subj = new Categoriser(seededTopicStore(List(cat)))
    val result = subj.categorise("superkokafinehest")

    assertThat(categoryNames(result), not(contains(cat.name)))
  }

  @Test
  def testLicensePlateExpression() = {
    val cat = Category("expressions", dummyUUID, List("/^[a-zA-Z]{2}\\d{2,5}$"))
    val subj = new Categoriser(seededTopicStore(List(cat)))
    val result = subj.categorise("aa12345")

    assertThat(categoryNames(result), contains(cat.name))
  }

  @Test
  def testLicensePlateExpressionNegative() = {
    val cat = Category("expressions", dummyUUID, List("/^[a-zA-Z]{2}\\d{2,5}$"))
    val subj = new Categoriser(seededTopicStore(List(cat)))
    val result = subj.categorise("aaa12345")

    assertThat(categoryNames(result), not(contains(cat.name)))
  }

  def categoryNames(categoryMatches: util.List[CategoryMatch]): util.List[String] = {
    categoryMatches.map(_.categoryName)
  }

  def topicStore(): TopicStore = {
    val storage = storageFile
    new File(storage).getParentFile().mkdirs() // ensure path exists
    new TopicStore(storage)
  }

  def seededTopicStore(categories: List[Category]): TopicStore = {
    val ts = topicStore()
    categories.foreach(cat => ts.create(cat.name, cat.entries.toList))
    ts
  }
}
