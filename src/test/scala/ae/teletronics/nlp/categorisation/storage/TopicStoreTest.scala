package ae.teletronics.nlp.categorisation.storage

import java.io.File
import java.nio.file.{Files, Paths}
import java.util
import java.util.UUID

import ae.teletronics.nlp.categorisation.Category
import org.junit.Assert._
import org.junit.{After, Before, Test}

/**
  * Created by trym on 26-09-2016.
  */
class TopicStoreTest {

  private val storageFile = "target/storage-test.db"
  private val underTest = new TopicStore(storageFile)
  private val key = new UUID(1L, 1L)
  private val name = "topic"
  private val emptyEntries = util.Arrays.asList[String]()
  private val newCategory = new Category(name, UUID.randomUUID(), emptyEntries)

  @After
  def afterTest(): Unit = {
    Files.deleteIfExists(Paths.get(storageFile))
  }

  @Before
  def beforeTest(): Unit = {
    new File(storageFile).getParentFile().mkdirs() // ensure path exists
  }

  @Test
  def testCreate(): Unit = {
    assertNull(underTest.create(newCategory))
    // return old value if overriding
    assertNotNull(underTest.create(newCategory))
  }

  @Test
  def testGet(): Unit = {
    // start state
    underTest.create(newCategory)
    // action
    assertNotNull(underTest.get(newCategory.id))
    assertEquals(name, underTest.get(newCategory.id).name)
    assertEquals(0, underTest.get(newCategory.id).entries.size())
  }

  @Test
  def testList(): Unit = {
    // start state
    underTest.create(name)
    // action
    assertNotNull(underTest.list())
    assertEquals(1, underTest.list().size)
  }

  @Test
  def testUpdate(): Unit = {
    // start state
    underTest.create(newCategory)
    assertNotNull(underTest.get(newCategory.id))
    val t = new Category(newCategory.name, newCategory.id, util.Arrays.asList("cat1"))
    // action
    assertNotNull(underTest.update(t))
  }

  @Test
  def testDelete(): Unit = {
    // start state
    underTest.create(newCategory)
    // action
    assertNotNull(underTest.delete(newCategory.id))
    // verify
    assertNotNull(underTest.list())
    assertEquals(0, underTest.list().size)
  }
}
