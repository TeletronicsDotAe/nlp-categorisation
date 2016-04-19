package ae.teletronics.nlp.categorisation

/**
  * Created by hhravn on 31/03/16.
  */

import java.util
import org.junit._
import Assert.assertThat
import org.hamcrest.Matchers._
import scala.collection.JavaConversions._

@Test
class FuzzyCategoriserTest {

  @Test
  def testFuzzyMatch() = {
    val cat = Category("drugs", List("~cocaine"))
    val subj = new Categoriser(List(cat))
    val result = subj.categorise("cocaino")

    assertThat(categoryNames(result), contains(cat.name))
  }

  @Test
  def testTooDistantMatch() = {
    val cat = Category("drugs", List("~cocaine"))
    val subj = new Categoriser(List(cat))
    val result = subj.categorise("kocoino") // levenshtein distance of 3 from cocaine

    assertThat(categoryNames(result).length, is(0))
  }

  @Test
  def testMultipleWordsMultipleCategoriesFuzzyMatch() = {
    val cat1 = Category("drugs1", List("~cocaine"))
    val cat2 = Category("drugs2", List("~smack"))
    val cat3 = Category("drugs3", List("~heroin"))
    val cat4 = Category("drugs4", List("~amphetamine"))
    val subj = new Categoriser(List(cat1, cat2, cat3, cat4))
    val result = subj.categorise("cocaine smeck amphetaminex lalalala hiruen") // hiruen is distance 3 from heroin, smeck and smack are only distance 1, but the percentage distance is 20%, so no match.

    assertThat(categoryNames(result), containsInAnyOrder(cat1.name, cat4.name))
  }

  @Test
  def testFailingPercentageMatch() = {
    val cat1 = Category("weapons", List("~gun"))
    val subj = new Categoriser(List(cat1))
    val result = subj.categorise("gut")

    assertThat(categoryNames(result).length, is(0))
  }

  @Test
  def testSucceedingPercentageMatch() = {
    val cat1 = Category("weapons", List("~gunnery"))
    val subj = new Categoriser(List(cat1))
    val result = subj.categorise("guntery")

    assertThat(categoryNames(result), contains(cat1.name))
  }

  @Test
  def testDrugMatchSmackThreshold() = {
    val drugs: Category = new Category("drugs", List("spliff", "cocaine", "heroin", "marijuana", "smack", "weed"))
    val weapons: Category = new Category("weapons", List("gun", "pistol", "rifle", "ak-47", "9mm"))
    val subj = new Categoriser(List(drugs, weapons))

    val result = subj.categorise("Luv u 2 seyy w all my soul we just got back n now were puttn up tha tree igot them each an xtra gift2 putunderit4 now we miss our mom loveu")

    assertThat(categoryNames(result).length, is(0))
  }

  @Test
  def testDeepReturnStructure() = {
    val drugs: Category = new Category("drugs", List("~spliff", "~cocaine", "~heroin", "~marijuana", "~smack", "~weed"))
    val weapons: Category = new Category("weapons", List("~gunnery", "~pistol", "~winchester", "~ak-47", "~9mm"))
    val subj = new Categoriser(List(drugs, weapons))

    val result = subj.categorise("pistols winchesters cocaino gunnnns! gunner mj Heroin")

    assertThat(categoryNames(result), containsInAnyOrder(weapons.name, drugs.name))
    assertThat(categoryNames(result).length, is(2))

    assertThat(matchesForCategory(result, weapons.name).size(), is(1))
    assertThat(matchesForEntryInCategory(result, weapons.name, "winchester"), containsInAnyOrder("winchesters"))



    assertThat(matchesForCategory(result, drugs.name).length, is(2))
    assertThat(matchesForEntryInCategory(result, drugs.name, "cocaine").length, is(1))
    assertThat(matchesForEntryInCategory(result, drugs.name, "cocaine")(0), is("cocaino"))
    assertThat(matchesForEntryInCategory(result, drugs.name, "heroin").length, is(1))
    assertThat(matchesForEntryInCategory(result, drugs.name, "heroin")(0), is("Heroin"))
  }

  def categoryNames(categoryMatches: util.List[CategoryMatch]): util.List[String] = {
    categoryMatches.map(_.categoryName)
  }

  def matchesForCategory(categoryMatches: util.List[CategoryMatch], categoryName: String): util.List[EntryMatch] = {
    val maybeMatches: Option[util.List[EntryMatch]] = categoryMatches.toList.find(_.categoryName.equals(categoryName)).map(_.entryMatches)
    val empty: util.List[EntryMatch] = List.empty[EntryMatch].toList

    maybeMatches.getOrElse(empty)
  }

  def matchesForEntryInCategory(categoryMatches: util.List[CategoryMatch], categoryName: String, categoryWord: String): util.List[String] = {
    val maybeMatches: Option[util.List[String]] = categoryMatches
      .toList
      .find(_.categoryName.equals(categoryName))
      .map(_.entryMatches
        .filter(_.exactEntry.equals(categoryWord))
        .flatMap(em => em.matches.map(m => m.word))
        .toList
      )

    val empty: util.List[String] = List.empty[String].toList

    maybeMatches.getOrElse(empty)
  }
}
