package ae.teletronics.nlp.categorisation

/**
  * Created by Boris on 2016-04-15.
  */

import java.io.File
import java.nio.file.{Files, Paths}
import java.util
import java.util.UUID
import scala.collection.JavaConversions._

import org.junit._
import Assert.assertThat
import ae.teletronics.nlp.categorisation.storage.TopicStore
import org.hamcrest.Matchers._

import scala.collection.JavaConversions._

@Test
class StrictCategoriserTest {

  private val storageFile = "target/storage-test.db"
  private val dummyUUID = new UUID(1L, 1L)

  @After
  def afterTest(): Unit = {
    Files.deleteIfExists(Paths.get(storageFile))
  }

  @Test
  def testExactMatch() = {
    val cat = Category("drugs", dummyUUID, List("cocaine"))
    val subj = new Categoriser(seededTopicStore(List(cat)))
    val result = subj.categorise("cocaine")

    assertThat(categoryNames(result), contains(cat.name))
  }

  @Test
  def testThatUniqueCategoryIsReturned() = {
    val cat1 = Category("drugs1", dummyUUID, List("cocaine"))
    val cat2 = Category("drugs2", dummyUUID, List("smack"))
    val cat3 = Category("drugs3", dummyUUID, List("heroin"))
    val cat4 = Category("drugs4", dummyUUID, List("amphetamine"))
    val subj = new Categoriser(seededTopicStore(List(cat1, cat2, cat3, cat4)))
    val result = subj.categorise("cocaine")

    assertThat(categoryNames(result), contains(cat1.name))
  }

  @Test
  def testNegativeMatch() = {
    val cat = Category("drugs", dummyUUID, List("cocaine"))
    val subj = new Categoriser(seededTopicStore(List(cat)))
    val result = subj.categorise("sunshine")

    assertThat(categoryNames(result).length, is(0))
  }

  @Test
  def testCaseInsensitiveMatch() = {
    val cat = Category("drugs", dummyUUID, List("CoCaInE"))
    val subj = new Categoriser(seededTopicStore(List(cat)))
    val result = subj.categorise("cocaine")

    assertThat(categoryNames(result), contains(cat.name))
  }

  @Test
  def testLabelMatch() = {
    val cat = Category("drugs", dummyUUID, List("coke/cocaine"))
    val subj = new Categoriser(seededTopicStore(List(cat)))
    val result = subj.categorise("coke")

    assertThat(result.length, is(1))
    assertThat(result.get(0).categoryName, is(cat.name))
    assertThat(result.get(0).entryMatches.length, is(1))
    assertThat(result.get(0).entryMatches.get(0).label, is("cocaine"))
    assertThat(result.get(0).entryMatches.get(0).exactEntry, is("coke"))
    assertThat(result.get(0).entryMatches.get(0).matcherName, is(new StrictMatcher().name))
    assertThat(result.get(0).entryMatches.get(0).matches.length, is(1))
    assertThat(result.get(0).entryMatches.get(0).matches.get(0).word, is("coke"))
  }

  def categoryNames(categoryMatches: util.List[CategoryMatch]): util.List[String] = {
    categoryMatches.map(_.categoryName)
  }

  def seededTopicStore(categories: List[Category]): TopicStore = {
    val storage = storageFile
    new File(storage).getParentFile().mkdirs() // ensure path exists
    val ts = new TopicStore(storage)
    categories.foreach(cat => ts.create(cat.name, cat.entries.toList))
    ts
  }
}
