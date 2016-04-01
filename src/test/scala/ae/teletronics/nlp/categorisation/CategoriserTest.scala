package ae.teletronics.nlp.categorisation

/**
  * Created by hhravn on 31/03/16.
  */
import org.junit._
import Assert._
import scala.collection.JavaConversions._


@Test
class CategoriserTest {

  @Test
  def testExactMatch() = {
    val cat = Category(name = "drugs", words = List("cocaine"))
    val subj = new SimpleCategoriser(categories = List(cat))
    val result = subj.categorise("cocaine")

    assertNotNull(result)
    assertTrue(result.length == 1)
    assertTrue(result.contains(cat.name))
  }

  @Test
  def testThatUniqueCategoryIsReturned() = {
    val cat1 = Category(name = "drugs1", words = List("cocaine"))
    val cat2 = Category(name = "drugs2", words = List("smack"))
    val cat3 = Category(name = "drugs3", words = List("heroin"))
    val cat4 = Category(name = "drugs4", words = List("amphetamine"))
    val subj = new SimpleCategoriser(categories = List(cat1, cat2, cat3, cat4))
    val result = subj.categorise("cocaine")

    assertNotNull(result)
    assertTrue(result.length == 1)
    assertTrue(result.contains(cat1.name))
  }

  @Test
  def testNegativeMatch() = {
    val cat = Category(name = "drugs", words = List("cocaine"))
    val subj = new SimpleCategoriser(categories = List(cat))
    val result = subj.categorise("sunshine")

    assertNotNull(result)
    assertTrue(result.length == 0)
  }

  @Test
  def testFuzzyMatch() = {
    val cat = Category(name = "drugs", words = List("cocaine"))
    val subj = new FuzzyCategoriser(categories = List(cat), 1, 1)
    val result = subj.categorise("cocaino")

    assertNotNull(result)
    assertTrue(result.length == 1)
    assertTrue(result.contains(cat.name))
  }

  @Test
  def testTooDistantMatch() = {
    val cat = Category(name = "drugs", words = List("cocaine"))
    val subj = new FuzzyCategoriser(categories = List(cat), 2, 1)
    val result = subj.categorise("kocoino") // levenshtein distance of 3 from cocaine

    assertNotNull(result)
    assertTrue(result.length == 0)
  }

  @Test
  def testMultipleWordsMultipleCategoriesFuzzyMatch() = {
    val cat1 = Category(name = "drugs1", words = List("cocaine"))
    val cat2 = Category(name = "drugs2", words = List("smack"))
    val cat3 = Category(name = "drugs3", words = List("heroin"))
    val cat4 = Category(name = "drugs4", words = List("amphetamine"))
    val subj = new FuzzyCategoriser(List(cat1, cat2, cat3, cat4), 2, 1)
    val result = subj.categorise("cocaine smeck amphetaminex lalalala hiruen") // hiruen is distance 3 from heroin

    assertNotNull(result)
    assertTrue(result.length == 3)
    assertTrue(result.contains(cat1.name))
    assertTrue(result.contains(cat2.name))
    assertTrue(result.contains(cat4.name))
  }

  @Test
  def testFailingPercentageMatch() = {
    val cat1 = Category(name = "weapons", words = List("gun"))
    val subj = new FuzzyCategoriser(List(cat1), 3, 0.33)
    val result = subj.categorise("gut")

    assertNotNull(result)
    assertTrue(result.length == 0)
  }

  @Test
  def testSucceedingPercentageMatch() = {
    val cat1 = Category(name = "weapons", words = List("gun"))
    val subj = new FuzzyCategoriser(List(cat1), 3, 0.34)
    val result = subj.categorise("gut")

    assertNotNull(result)
    assertTrue(result.length == 1)
    assertTrue(result.contains(cat1.name))
  }
}
