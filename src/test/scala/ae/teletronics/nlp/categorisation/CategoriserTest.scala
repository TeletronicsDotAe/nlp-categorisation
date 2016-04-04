package ae.teletronics.nlp.categorisation

/**
  * Created by hhravn on 31/03/16.
  */

import org.junit._
import Assert.assertThat
import org.hamcrest.Matchers._
import scala.collection.JavaConversions._

@Test
class CategoriserTest {

  @Test
  def testExactMatch() = {
    val cat = Category("drugs", List("cocaine"))
    val subj = new SimpleCategoriser(List(cat))
    val result = subj.categorise("cocaine")

    assertThat(result.categories, contains(cat.name))
  }

  @Test
  def testThatUniqueCategoryIsReturned() = {
    val cat1 = Category("drugs1", List("cocaine"))
    val cat2 = Category("drugs2", List("smack"))
    val cat3 = Category("drugs3", List("heroin"))
    val cat4 = Category("drugs4", List("amphetamine"))
    val subj = new SimpleCategoriser(List(cat1, cat2, cat3, cat4))
    val result = subj.categorise("cocaine")

    assertThat(result.categories, contains(cat1.name))
  }

  @Test
  def testNegativeMatch() = {
    val cat = Category("drugs", List("cocaine"))
    val subj = new SimpleCategoriser(List(cat))
    val result = subj.categorise("sunshine")

    assertThat(result.categories.size(), is(0))
  }

  @Test
  def testFuzzyMatch() = {
    val cat = Category("drugs", List("cocaine"))
    val subj = new FuzzyCategoriser(List(cat), 1, 1)
    val result = subj.categorise("cocaino")

    assertThat(result.categories, contains(cat.name))
  }

  @Test
  def testTooDistantMatch() = {
    val cat = Category("drugs", List("cocaine"))
    val subj = new FuzzyCategoriser(List(cat), 2, 1)
    val result = subj.categorise("kocoino") // levenshtein distance of 3 from cocaine

    assertThat(result.categories.size(), is(0))
  }

  @Test
  def testMultipleWordsMultipleCategoriesFuzzyMatch() = {
    val cat1 = Category("drugs1", List("cocaine"))
    val cat2 = Category("drugs2", List("smack"))
    val cat3 = Category("drugs3", List("heroin"))
    val cat4 = Category("drugs4", List("amphetamine"))
    val subj = new FuzzyCategoriser(List(cat1, cat2, cat3, cat4), 2, 1)
    val result = subj.categorise("cocaine smeck amphetaminex lalalala hiruen") // hiruen is distance 3 from heroin

    assertThat(result.categories, containsInAnyOrder(cat1.name, cat2.name, cat4.name))
  }

  @Test
  def testFailingPercentageMatch() = {
    val cat1 = Category("weapons", List("gun"))
    val subj = new FuzzyCategoriser(List(cat1), 3, 0.33)
    val result = subj.categorise("gut")

    assertThat(result.categories.size(), is(0))
  }

  @Test
  def testSucceedingPercentageMatch() = {
    val cat1 = Category("weapons", List("gun"))
    val subj = new FuzzyCategoriser(List(cat1), 3, 0.34)
    val result = subj.categorise("gut")

    assertThat(result.categories, contains(cat1.name))
  }

  @Test
  def testDrugMatchSmackThreshold() = {
    val drugs: Category = new Category("drugs", List("spliff", "cocaine", "heroin", "marijuana", "smack", "weed"))
    val weapons: Category = new Category("weapons", List("gun", "pistol", "rifle", "ak-47", "9mm"))
    val subj = new FuzzyCategoriser(List(drugs, weapons), 3, 0.25)

    val result = subj.categorise("Luv u 2 seyy w all my soul we just got back n now were puttn up tha tree igot them each an xtra gift2 putunderit4 now we miss our mom loveu")

    assertThat(result.categories.size(), is(0))
  }

  @Test
  def testNewReturnStructure() = {
    val drugs: Category = new Category("drugs", List("spliff", "cocaine", "heroin", "marijuana", "smack", "weed"))
    val weapons: Category = new Category("weapons", List("gun", "pistol", "rifle", "ak-47", "9mm"))
    val subj = new FuzzyCategoriser(List(drugs, weapons), 1, 0.5)

    val result = subj.categorise("cocaino gu gn mj heroin")

    assertThat(result.categories, containsInAnyOrder(weapons.name, drugs.name))
    assertThat(result.categories.size(), is(2))

    assertThat(result.matchesForCategory(weapons.name).size(), is(2))
    assertThat(result.matchesForWordInCategory(weapons.name, "gun"), containsInAnyOrder("gu", "gn"))

    assertThat(result.matchesForCategory(drugs.name).size(), is(2))
    assertThat(result.matchesForWordInCategory(drugs.name, "cocaine").size(), is(1))
    assertThat(result.matchesForWordInCategory(drugs.name, "cocaine").get(0), is("cocaino"))
    assertThat(result.matchesForWordInCategory(drugs.name, "heroin").size(), is(1))
    assertThat(result.matchesForWordInCategory(drugs.name, "heroin").get(0), is("heroin"))
  }
}
